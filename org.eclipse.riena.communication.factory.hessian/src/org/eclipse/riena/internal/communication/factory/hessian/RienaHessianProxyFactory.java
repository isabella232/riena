/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

import org.eclipse.riena.communication.core.hooks.ICallMessageContext;
import org.eclipse.riena.communication.core.hooks.ICallMessageContextAccessor;
import org.eclipse.riena.communication.core.publisher.RSDPublisherProperties;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

import com.caucho.hessian.client.HessianProxyFactory;
import com.caucho.hessian.io.SerializerFactory;

public class RienaHessianProxyFactory extends HessianProxyFactory implements ManagedService {

	private ICallMessageContextAccessor mca;
	private static ThreadLocal<HttpURLConnection> connections = new ThreadLocal<HttpURLConnection>();
	private URL url;

	public RienaHessianProxyFactory() {
		super();
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
		connections.set((HttpURLConnection) connection);
		return connection;
	}

	@Override
	public SerializerFactory getSerializerFactory() {
		SerializerFactory serializerFactory = super.getSerializerFactory();
		serializerFactory.setAllowNonSerializable(true);
		return serializerFactory;
	}

	public void setCallMessageContextAccessor(ICallMessageContextAccessor mca) {
		this.mca = mca;
	}

	public static HttpURLConnection getHttpURLConnection() {
		return connections.get();
	}

	@SuppressWarnings("unchecked")
	public void updated(Dictionary properties) throws ConfigurationException {
		if (properties == null) {
			return;
		}
		String urlStr = (String) properties.get(RSDPublisherProperties.PROP_REMOTE_PATH);
		if (urlStr != null) {
			try {
				URL tmpUrl = new URL(urlStr);
				this.url = tmpUrl;
			} catch (MalformedURLException e) {
				System.out.println("invalid url " + urlStr + e);
			}
		}

	}

}
