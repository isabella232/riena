/*******************************************************************************
 * Copyright (c) 2007, 2008 compeople AG and others.
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.riena.communication.core.hooks.ICallMessageContext;
import org.eclipse.riena.communication.core.hooks.ICallMessageContextAccessor;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.util.ReflectionUtils;

import org.eclipse.equinox.log.Logger;
import org.osgi.service.log.LogService;

import com.caucho.hessian.client.HessianProxyFactory;
import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.AbstractSerializerFactory;
import com.caucho.hessian.io.SerializerFactory;

public class RienaHessianProxyFactory extends HessianProxyFactory {

	private ICallMessageContextAccessor mca;

	private final static ThreadLocal<HttpURLConnection> CONNECTIONS = new ThreadLocal<HttpURLConnection>();
	private final static Logger LOGGER = Activator.getDefault().getLogger(RienaHessianProxyFactory.class);

	public RienaHessianProxyFactory() {
		super();
		Inject.extension("org.eclipse.riena.communication.hessian.AbstractSerializerFactory").into(this).update( //$NON-NLS-1$
				"setFactory").andStart(Activator.getDefault().getContext()); //$NON-NLS-1$
		setHessian2Request(true);
		setHessian2Reply(true);
	}

	public void setFactory(IAbstractSerializerFactory[] factories) {
		for (IAbstractSerializerFactory factory : factories) {
			try {
				AbstractSerializerFactory serializerFactory = factory.createImplementation();
				getSerializerFactory().addFactory(serializerFactory);
			} catch (Exception e) {
				LOGGER.log(LogService.LOG_ERROR, "Error instantiation AbstractSerializerFactor = "
						+ factory.getImplementation());
				throw new RuntimeException("Error instantiation AbstractSerializerFactor = "
						+ factory.getImplementation(), e);
			}
		}
	}

	@Override
	protected URLConnection openConnection(URL url) throws IOException {
		URLConnection connection;
		ICallMessageContext mc = mca.getMessageContext();
		String methodName = mc.getMethodName();
		String requestId = mc.getRequestId();
		if (methodName != null || requestId != null) {
			if (requestId != null) {
				methodName = methodName + "&" + requestId; //$NON-NLS-1$
			}
			connection = super.openConnection(new URL(url.toString() + "?" + methodName)); //$NON-NLS-1$
		} else {
			connection = super.openConnection(url);
		}
		Map<String, List<String>> headers = mc.listRequestHeaders();
		if (headers != null) {
			for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
				// System.out.println("size:" + headers.get(hName).size());
				for (String hValue : entry.getValue()) {
					connection.addRequestProperty(entry.getKey(), hValue);
					// System.out.println(">>>" + hName + ":" + hValue);
				}
			}
		}
		CONNECTIONS.set((HttpURLConnection) connection);
		return connection;
	}

	@Override
	public AbstractHessianInput getHessianInput(final InputStream is) {
		final ICallMessageContext messageContext = mca.getMessageContext();
		if (messageContext.getProgressMonitorList() == null) {
			return super.getHessianInput(is);
		} else {
			return super.getHessianInput(new InputStream() {

				@Override
				public int read() throws IOException {
					int b = is.read();
					if (b != -1) {
						messageContext.fireReadEvent(1);
					}
					return b;
				}

			});
		}
	}

	@Override
	public AbstractHessianOutput getHessianOutput(final OutputStream os) {
		final ICallMessageContext messageContext = mca.getMessageContext();
		if (messageContext.getProgressMonitorList() == null) {
			return super.getHessianOutput(os);
		} else {
			return super.getHessianOutput(new OutputStream() {

				@Override
				public void write(int b) throws IOException {
					os.write(b);
					messageContext.fireWriteEvent(1);
				}

			});
		}
	}

	@Override
	public SerializerFactory getSerializerFactory() {
		SerializerFactory serializerFactory = super.getSerializerFactory();
		serializerFactory.setAllowNonSerializable(true);
		removeInputStreamSerializerDeserializer(serializerFactory);
		return serializerFactory;
	}

	private void removeInputStreamSerializerDeserializer(SerializerFactory serializerFactory) {
		HashMap<?, ?> staticDeSerMap = ReflectionUtils
				.getHidden(serializerFactory.getClass(), "_staticDeserializerMap"); //$NON-NLS-1$
		staticDeSerMap.remove(java.io.InputStream.class);
		staticDeSerMap.remove(StackTraceElement.class);
		HashMap<?, ?> staticSerMap = ReflectionUtils.getHidden(serializerFactory.getClass(), "_staticSerializerMap"); //$NON-NLS-1$
		staticSerMap.remove(java.io.InputStream.class);
	}

	public void setCallMessageContextAccessor(ICallMessageContextAccessor mca) {
		this.mca = mca;
	}

	protected static HttpURLConnection getHttpURLConnection() {
		return CONNECTIONS.get();
	}

}
