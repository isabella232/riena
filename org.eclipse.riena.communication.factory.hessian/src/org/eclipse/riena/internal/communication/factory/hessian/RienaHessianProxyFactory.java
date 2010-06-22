/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.communication.factory.hessian;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.caucho.hessian.client.HessianProxyFactory;
import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.Hessian2Output;

import org.eclipse.riena.communication.core.hooks.ICallMessageContext;
import org.eclipse.riena.communication.core.hooks.ICallMessageContextAccessor;
import org.eclipse.riena.communication.core.zipsupport.ReusableBufferedInputStream;

public class RienaHessianProxyFactory extends HessianProxyFactory {

	private ICallMessageContextAccessor mca;
	private final static ThreadLocal<HttpURLConnection> CONNECTIONS = new ThreadLocal<HttpURLConnection>();
	private static boolean transferDataChunked = false; // set chunking to FALSE by default overwriting the hessian default
	private static boolean zipClientRequest = false;
	private static boolean propertiesInitialized = false;
	private static final String RIENA_COMMUNICATION_ZIP_PROPERTY = "riena.communication.zip"; //$NON-NLS-1$

	/**
	 * @param zipClientRequest
	 *            the zipClientRequest to set
	 */
	public static void setZipClientRequest(final boolean zipClientRequest) {
		RienaHessianProxyFactory.zipClientRequest = zipClientRequest;
	}

	/**
	 * @return the zipClientRequest
	 */
	public static boolean isZipClientRequest() {
		return zipClientRequest;
	}

	public RienaHessianProxyFactory() {
		super();
		getSerializerFactory().setAllowNonSerializable(true);
		getSerializerFactory().addFactory(new RienaSerializerFactory());
		setHessian2Request(true);
		setHessian2Reply(true);
		setChunkedPost(transferDataChunked);

		if (!propertiesInitialized) {
			final String zipProp = System.getProperty(RIENA_COMMUNICATION_ZIP_PROPERTY);
			if (zipProp != null && zipProp.length() > 0) {
				zipClientRequest = zipProp.equalsIgnoreCase("true");
			}
			propertiesInitialized = true;
		}
	}

	/**
	 * By default data is transfered NOT chunked. This method tests the current
	 * setting.
	 * 
	 * @return true if chunking for transfer is enabled, (false=DEFAULT)
	 */
	public static boolean isTransferDataChunked() {
		return transferDataChunked;
	}

	/**
	 * By default data is transfered NOT chunked, this method can be used to
	 * enable it.
	 * 
	 * @param transferDataChunked
	 *            true if data just be transfered in chunks (false=DEFAULT)
	 */
	public static void setTransferDataChunked(final boolean transferDataChunked) {
		RienaHessianProxyFactory.transferDataChunked = transferDataChunked;
	}

	@Override
	protected URLConnection openConnection(final URL url) throws IOException {
		URLConnection connection;
		final ICallMessageContext mc = mca.getMessageContext();
		String methodName = mc.getMethodName();
		final String requestId = mc.getRequestId();
		if (methodName != null || requestId != null) {
			if (requestId != null) {
				methodName = methodName + "&" + requestId; //$NON-NLS-1$
			}
			connection = super.openConnection(new URL(url.toString() + "?" + methodName)); //$NON-NLS-1$
		} else {
			connection = super.openConnection(url);
		}
		final Map<String, List<String>> headers = mc.listRequestHeaders();
		if (headers != null) {
			for (final Map.Entry<String, List<String>> entry : headers.entrySet()) {
				// System.out.println("size:" + headers.get(hName).size());
				for (final String hValue : entry.getValue()) {
					connection.addRequestProperty(entry.getKey(), hValue);
					// System.out.println(">>>" + hName + ":" + hValue);
				}
			}
		}
		if (isZipClientRequest()) {
			connection.addRequestProperty("Content-Encoding", "gzip"); //$NON-NLS-1$//$NON-NLS-2$
		}

		CONNECTIONS.set((HttpURLConnection) connection);
		return connection;
	}

	@Override
	public AbstractHessianInput getHessianInput(final InputStream is) {
		final ICallMessageContext messageContext = mca.getMessageContext();
		boolean inputWasGZIP = false;

		final InputStream myInputStream;

		try {
			if (isZipClientRequest()) {
				final InputStream zipTestInputStream = new ReusableBufferedInputStream(is);
				if (zipTestInputStream.markSupported()) {
					zipTestInputStream.mark(20);
					int readMAGIC;
					readMAGIC = zipTestInputStream.read() + zipTestInputStream.read() * 256;
					if (readMAGIC == GZIPInputStream.GZIP_MAGIC) {
						inputWasGZIP = true;
					}
					zipTestInputStream.reset();
				}
				if (inputWasGZIP) {
					myInputStream = new GZIPInputStream(zipTestInputStream);
				} else {
					myInputStream = zipTestInputStream;
				}
			} else {
				myInputStream = is;
			}
		} catch (final IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		if (messageContext.getProgressMonitorList() == null) {
			return super.getHessianInput(myInputStream);
		} else {
			return super.getHessianInput(new InputStream() {

				@Override
				public int read() throws IOException {
					final int b = myInputStream.read();
					if (b != -1) {
						messageContext.fireReadEvent(1);
					}
					return b;
				}

			});
		}
	}

	@Override
	public AbstractHessianOutput getHessianOutput(final OutputStream outputStreamParameter) {
		final ICallMessageContext messageContext = mca.getMessageContext();

		final OutputStream myOutputStream;
		if (isZipClientRequest()) {
			try {
				myOutputStream = new GZIPOutputStream(outputStreamParameter);
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			myOutputStream = outputStreamParameter;
		}

		if (messageContext.getProgressMonitorList() == null) {
			//			return super.getHessianOutput(myOutputStream);
			return getHessianOutputImpl(myOutputStream);
		} else {
			return getHessianOutputImpl(new OutputStream() {

				@Override
				public void write(final int b) throws IOException {
					myOutputStream.write(b);
					messageContext.fireWriteEvent(1);
				}

			});
		}
	}

	public void setCallMessageContextAccessor(final ICallMessageContextAccessor mca) {
		this.mca = mca;
	}

	protected static HttpURLConnection getHttpURLConnection() {
		return CONNECTIONS.get();
	}

	private AbstractHessianOutput getHessianOutputImpl(final OutputStream outputStreamParameter) {
		AbstractHessianOutput out;

		if (/* _isHessian2Request */true) {
			out = new Hessian2Output(outputStreamParameter) {

				@Override
				public void completeCall() throws IOException {
					super.completeCall();
					this.flush();
					if (outputStreamParameter instanceof GZIPOutputStream) {
						((GZIPOutputStream) outputStreamParameter).finish();
					}
					outputStreamParameter.flush();
				}
			};
			//		else {
			//			HessianOutput out1 = new HessianOutput(outputStreamParameter);
			//			out = out1;
			//
			//			if (/* _isHessian2Reply */true)
			//				out1.setVersion(2);
			//		}
		}

		out.setSerializerFactory(getSerializerFactory());

		return out;
	}

}
