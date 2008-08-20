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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.Principal;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.riena.communication.core.hooks.ICallMessageContext;
import org.eclipse.riena.communication.core.hooks.ICallMessageContextAccessor;
import org.eclipse.riena.communication.core.progressmonitor.IProgressMonitorList;
import org.eclipse.riena.communication.core.publisher.RSDPublisherProperties;
import org.eclipse.riena.core.util.ReflectionUtils;

import org.eclipse.equinox.log.Logger;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.log.LogService;

import com.caucho.hessian.client.HessianProxyFactory;
import com.caucho.hessian.io.AbstractDeserializer;
import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.AbstractSerializerFactory;
import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.JavaDeserializer;
import com.caucho.hessian.io.Serializer;
import com.caucho.hessian.io.SerializerFactory;

public class RienaHessianProxyFactory extends HessianProxyFactory implements ManagedService {

	private ICallMessageContextAccessor mca;
	private URL url;

	private final static ThreadLocal<HttpURLConnection> CONNECTIONS = new ThreadLocal<HttpURLConnection>();
	private final static Logger LOGGER = Activator.getDefault().getLogger(RienaHessianProxyFactory.class.getName());

	public RienaHessianProxyFactory() {
		super();
		getSerializerFactory().addFactory(new AbstractSerializerFactory() {
			@Override
			public Deserializer getDeserializer(Class cl) throws HessianProtocolException {
				if (cl.isInterface() && (!cl.getPackage().getName().startsWith("java") || cl == Principal.class)) { //$NON-NLS-1$
					return new JavaDeserializer(cl);
				}
				return null;
			}

			@Override
			public Serializer getSerializer(Class cl) throws HessianProtocolException {
				return null;
			}
		});
	}

	@Override
	protected URLConnection openConnection(URL url) throws IOException {
		URLConnection connection;
		if (this.url != null) {
			connection = super.openConnection(this.url);
		} else {
			connection = super.openConnection(url);
		}
		ICallMessageContext mc = mca.getMessageContext();
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
		final IProgressMonitorList progressMonitorList = mca.getMessageContext().getProgressMonitorList();
		if (progressMonitorList == null) {
			return super.getHessianInput(is);
		} else {
			return super.getHessianInput(new InputStream() {

				private int progressBytes = 0;
				private int totalProgressBytes = 0;

				@Override
				public int read() throws IOException {
					int b = is.read();
					if (b == -1 || progressBytes > IProgressMonitorList.BYTE_COUNT_INCR) {
						totalProgressBytes += progressBytes;
						progressBytes = 0;
						progressMonitorList.fireReadEvent(-1, totalProgressBytes);
					}
					if (b == -1) {
						return b;
					}

					progressBytes++;
					return b;
				}

			});
		}
	}

	@Override
	public AbstractHessianOutput getHessianOutput(final OutputStream os) {
		final IProgressMonitorList progressMonitorList = mca.getMessageContext().getProgressMonitorList();
		if (progressMonitorList == null) {
			return super.getHessianOutput(os);
		} else {
			return super.getHessianOutput(new OutputStream() {

				private int progressBytes = 0;
				private int totalProgressBytes = 0;

				@Override
				public void write(int b) throws IOException {
					os.write(b);
					if (progressBytes > IProgressMonitorList.BYTE_COUNT_INCR) {
						totalProgressBytes += progressBytes;
						progressBytes = 0;
						progressMonitorList.fireReadEvent(-1, totalProgressBytes);
					}
				}

			});
		}
	}

	@Override
	public SerializerFactory getSerializerFactory() {
		SerializerFactory serializerFactory = super.getSerializerFactory();
		serializerFactory.setAllowNonSerializable(true);
		addSpecialDeserializer(serializerFactory);
		return serializerFactory;
	}

	@SuppressWarnings("unchecked")
	private void addSpecialDeserializer(SerializerFactory serializerFactory) {
		HashMap staticDeSerMap = ReflectionUtils.getHidden(serializerFactory.getClass(), "_staticDeserializerMap"); //$NON-NLS-1$
		staticDeSerMap.put(java.io.InputStream.class, new SpecificInputStreamDeserializer());
	}

	public void setCallMessageContextAccessor(ICallMessageContextAccessor mca) {
		this.mca = mca;
	}

	protected static HttpURLConnection getHttpURLConnection() {
		return CONNECTIONS.get();
	}

	@SuppressWarnings("unchecked")
	public void updated(Dictionary properties) throws ConfigurationException {
		if (properties == null)
			return;
		String urlStr = (String) properties.get(RSDPublisherProperties.PROP_REMOTE_PATH);
		if (urlStr == null)
			return;

		try {
			this.url = new URL(urlStr);
		} catch (MalformedURLException e) {
			LOGGER.log(LogService.LOG_ERROR, "invalid url " + urlStr, e); //$NON-NLS-1$
		}

	}

	static class SpecificInputStreamDeserializer extends AbstractDeserializer {
		public Object readObject(AbstractHessianInput in) throws IOException {

			byte[] bytes = in.readBytes();
			return new ByteArrayInputStream(bytes);
		}
	}

}
