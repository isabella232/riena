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
import org.eclipse.riena.communication.core.progressmonitor.IProgressMonitorList;
import org.eclipse.riena.communication.core.progressmonitor.IProgressMonitorRegistry;
import org.eclipse.riena.core.injector.Inject;

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
	private final ICallMessageContextAccessor mca = new CallMsgCtxAcc();
	private final static String PROTOCOL = "hessian"; //$NON-NLS-1$
	private IProgressMonitorRegistry progressMonitorRegistry;

	/*
	 * (non-Javadoc)
	 * 
	 * @seexeval.rsd.core.IRemoteServiceFactory#create(xeval.rsd.core.
	 * RemoteServiceDescription)
	 */
	public RemoteServiceReference createProxy(RemoteServiceDescription endpoint) {
		String uri = endpoint.getURL();
		if (uri == null) {
			uri = "http://localhost/" + PROTOCOL + endpoint.getPath(); //$NON-NLS-1$
		}
		try {
			RienaHessianProxyFactory mhpf = new RienaHessianProxyFactory();

			mhpf.setCallMessageContextAccessor(mca);
			Object proxy = mhpf.create(endpoint.getServiceInterfaceClass(), uri);
			Object service = proxy;
			RemoteServiceReference serviceReference = new RemoteServiceReference(endpoint);
			// set the create proxy as service instance
			serviceReference.setServiceInstance(service);
			// inject progressmonitor registry into THIS
			Inject.service(IProgressMonitorRegistry.class.getName()).useRanking().into(this).andStart(
					Activator.getDefault().getContext());
			// the hessian proxy factory also implements ManagedService
			if (endpoint.getConfigPID() != null) {
				serviceReference.setConfigServiceInstance(mhpf);
				serviceReference.getDescription().setConfigPID(endpoint.getConfigPID());
			}
			return serviceReference;
		} catch (MalformedURLException e) {
			throw new RuntimeException("MalformedURLException", e); //$NON-NLS-1$
		}
	}

	public void bind(IProgressMonitorRegistry pmr) {
		this.progressMonitorRegistry = pmr;
	}

	public void unbind(IProgressMonitorRegistry pmr) {
		if (this.progressMonitorRegistry == pmr) {
			this.progressMonitorRegistry = null;
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
			ICallMessageContext mc = new MsgCtx(proxy);
			contexts.set(mc);
			return mc;
		}

		public ICallMessageContext getMessageContext() {
			return contexts.get();
		}

		class MsgCtx implements ICallMessageContext {

			private HashMap<String, List<String>> customRequestHeader;
			private IProgressMonitorList progressMonitorList;

			public MsgCtx(Object proxy) {
				super();
				if (progressMonitorRegistry != null) {
					progressMonitorList = progressMonitorRegistry.getProgressMonitors(proxy);
				} else {
					progressMonitorList = null;
				}
			}

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
				HttpURLConnection httpUrlConnection = RienaHessianProxyFactory.getHttpURLConnection();
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

			public IProgressMonitorList getProgressMonitorList() {
				return progressMonitorList;
			}
		}
	}
}
