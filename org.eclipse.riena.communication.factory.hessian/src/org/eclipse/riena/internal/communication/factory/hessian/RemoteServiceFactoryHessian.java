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

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.riena.communication.core.RemoteServiceDescription;
import org.eclipse.riena.communication.core.factory.IRemoteServiceFactory;
import org.eclipse.riena.communication.core.factory.RemoteServiceReference;
import org.eclipse.riena.communication.core.hooks.ICallMessageContext;
import org.eclipse.riena.communication.core.hooks.ICallMessageContextAccessor;

/**
 * This is a Hessian based implementation of {@link IRemoteServiceFactory}.
 * Creates RemoteServiceReferences for service end points based on Hessian
 * protocol.
 * <p>
 * RemoteServiceFactoryHessian becomes registered as OSGi Service with name
 * {@link IRemoteServiceFactory#ID}. The OSGi Service set the property
 * "riena.protocol=hessian".
 * 
 * @author Alexander Ziegler
 * @author Christian Campo
 * 
 */
public class RemoteServiceFactoryHessian implements IRemoteServiceFactory {
	private final static String PROTOCOL = "hessian";
	private ICallMessageContextAccessor mca = new CallMsgCtxAcc();

	/*
	 * (non-Javadoc)
	 * 
	 * @see xeval.rsd.core.IRemoteServiceFactory#create(xeval.rsd.core.RemoteServiceDescription)
	 */
	public RemoteServiceReference createProxy(RemoteServiceDescription endpoint) {
		String uri = endpoint.getURL();
		if (uri == null) {
			uri = "http://localhost/" + PROTOCOL + endpoint.getPath();
		}
		try {
			MyHessianProxyFactory mhpf = new MyHessianProxyFactory();

			mhpf.setCallMessageContextAccessor(mca);
			Object proxy = mhpf.create(endpoint.getServiceInterfaceClass(), uri);
			Object service = proxy;
			RemoteServiceReference serviceReference = new RemoteServiceReference(endpoint);
			// set the create proxy as service instance
			serviceReference.setServiceInstance(service);
			// the hessian proxy factory also implements ManagedService
			if (endpoint.getConfigid() != null) {
				serviceReference.setConfigServiceInstance(mhpf);
				serviceReference.setConfigServicePID(endpoint.getConfigid());
			}
			return serviceReference;
		} catch (MalformedURLException e) {
			throw new RuntimeException("MalformedURLException", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see xeval.rsd.core.IRemoteServiceFactory#protocol()
	 */
	public String getProtocol() {
		return PROTOCOL;
	}

	public void dispose() {
	}

	public ICallMessageContextAccessor getMessageContextAccessor() {
		return mca;
	}

	class CallMsgCtxAcc implements ICallMessageContextAccessor {

		private ThreadLocal<ICallMessageContext> contexts = new ThreadLocal<ICallMessageContext>();

		public ICallMessageContext createMessageContext(Object proxy) {
			ICallMessageContext mc = new MsgCtx();
			contexts.set(mc);
			return mc;
		}

		public ICallMessageContext getMessageContext() {
			return contexts.get();
		}

		class MsgCtx implements ICallMessageContext {

			private HashMap<String, List<String>> customRequestHeader;

			public List<String> getResponseHeaderValues(String name) {
				Map<String, List<String>> headers = listResponseHeaders();
				if (headers == null) {
					return null;
				}
				return headers.get(name);
			}

			public Map<String, List<String>> listRequestHeaders() {
				return customRequestHeader;
			}

			public Map<String, List<String>> listResponseHeaders() {
				HttpURLConnection httpUrlConnection = MyHessianProxyFactory.getHttpURLConnection();
				if (httpUrlConnection == null) {
					return null;
				}
				return httpUrlConnection.getHeaderFields();
			}

			public void addRequestHeader(String name, String value) {
				if (customRequestHeader == null) {
					customRequestHeader = new HashMap<String, List<String>>();
				}
				List<String> hValues = customRequestHeader.get(name);
				if (hValues == null) {
					hValues = new ArrayList<String>();
					customRequestHeader.put(name, hValues);
				}
				hValues.add(value);
			}
		}
	}
}
