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

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.riena.communication.core.RemoteServiceDescription;
import org.eclipse.riena.communication.core.factory.IRemoteServiceFactory;
import org.eclipse.riena.communication.core.factory.RemoteServiceReference;
import org.eclipse.riena.communication.core.hooks.ICallMessageContext;
import org.eclipse.riena.communication.core.hooks.ICallMessageContextAccessor;
import org.eclipse.riena.communication.core.progressmonitor.IRemoteProgressMonitorList;
import org.eclipse.riena.communication.core.progressmonitor.IRemoteProgressMonitorRegistry;
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
	private IRemoteProgressMonitorRegistry remoteProgressMonitorRegistry;
	private final ICallMessageContextAccessor messageContextAccessor;
	private final RienaHessianProxyFactory rienaHessianProxyFactory;
	private final static String PROTOCOL = "hessian"; //$NON-NLS-1$
	private final static SecureRandom random = new SecureRandom();
	private final static long base = 115825100000L; // some base time in millisec around 14.9.2006 18:40 (arbitrary picked) just to make the long a short number

	public RemoteServiceFactoryHessian() {
		messageContextAccessor = new CallMsgCtxAcc();
		rienaHessianProxyFactory = new RienaHessianProxyFactory();
		rienaHessianProxyFactory.setCallMessageContextAccessor(messageContextAccessor);

		// inject progressmonitor registry into THIS
		Inject.service(IRemoteProgressMonitorRegistry.class.getName()).useRanking().into(this).andStart(
				Activator.getDefault().getContext());
	}

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
			Object proxy = rienaHessianProxyFactory.create(endpoint.getServiceInterfaceClass(), uri);
			RemoteServiceReference serviceReference = new RemoteServiceReference(endpoint);
			// set the create proxy as service instance
			serviceReference.setServiceInstance(proxy);
			return serviceReference;
		} catch (MalformedURLException e) {
			throw new RuntimeException("MalformedURLException", e); //$NON-NLS-1$
		}
	}

	public void bind(IRemoteProgressMonitorRegistry pmr) {
		this.remoteProgressMonitorRegistry = pmr;
	}

	public void unbind(IRemoteProgressMonitorRegistry pmr) {
		if (this.remoteProgressMonitorRegistry == pmr) {
			this.remoteProgressMonitorRegistry = null;
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

	public ICallMessageContextAccessor getMessageContextAccessor() {
		return messageContextAccessor;
	}

	class CallMsgCtxAcc implements ICallMessageContextAccessor {

		private ThreadLocal<ICallMessageContext> contexts = new ThreadLocal<ICallMessageContext>();

		public ICallMessageContext createMessageContext(Object proxy, String methodName, String requestId) {
			ICallMessageContext mc = new MsgCtx(proxy, methodName, requestId);
			contexts.set(mc);
			return mc;
		}

		public ICallMessageContext getMessageContext() {
			return contexts.get();
		}

		class MsgCtx implements ICallMessageContext {

			private HashMap<String, List<String>> customRequestHeader;
			private IRemoteProgressMonitorList remoteProgressMonitorList;
			private int bytesRead;
			private int totalBytesRead = 0;
			private int bytesWritten;
			private int totalBytesWritten = 0;
			private boolean firstEvent = true;
			private String methodName;
			private String requestId = null;

			public MsgCtx(Object proxy, String methodName, String requestId) {
				super();
				this.methodName = methodName;
				this.requestId = requestId;
				if (remoteProgressMonitorRegistry != null) {
					remoteProgressMonitorList = remoteProgressMonitorRegistry.getProgressMonitors(proxy);
				} else {
					remoteProgressMonitorList = null;
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

			public IRemoteProgressMonitorList getProgressMonitorList() {
				return remoteProgressMonitorList;
			}

			public void fireStartCall() {
				//				List<String> list = RienaHessianProxyFactory.getHttpURLConnection().getRequestProperties().get(
				//						"Content-Length"); //$NON-NLS-1$
				remoteProgressMonitorList.fireStartEvent();
				firstEvent = false;

			}

			public void fireEndCall() {
				// if no communication happened than this was a local call (like for equals or hashCode)
				if (totalBytesRead == 0 && totalBytesWritten == 0 && bytesRead == 0 && bytesWritten == 0) {
					return;
				}
				if (bytesRead != 0) {
					internalFireReadEvent();
				}
				remoteProgressMonitorList.fireEndEvent(totalBytesRead + totalBytesWritten);
			}

			public void fireReadEvent(int parmBytesRead) {
				if (firstEvent) {
					remoteProgressMonitorList.fireStartEvent();
					firstEvent = false;
				}

				if (bytesWritten != 0) {
					internalFireWriteEvent();
				}
				bytesRead += parmBytesRead;
				if (bytesRead >= IRemoteProgressMonitorList.BYTE_COUNT_INCR) {
					internalFireReadEvent();
				}
			}

			private void internalFireReadEvent() {
				totalBytesRead += bytesRead;
				bytesRead = 0;
				remoteProgressMonitorList.fireReadEvent(-1, totalBytesRead);
			}

			public void fireWriteEvent(int parmBytesWritten) {
				if (firstEvent) {
					fireStartCall();
				}

				bytesWritten += parmBytesWritten;
				if (bytesWritten >= IRemoteProgressMonitorList.BYTE_COUNT_INCR) {
					internalFireWriteEvent();
				}
			}

			private void internalFireWriteEvent() {
				totalBytesWritten += bytesWritten;
				bytesWritten = 0;
				remoteProgressMonitorList.fireWriteEvent(-1, totalBytesWritten);
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.riena.communication.core.hooks.ICallMessageContext
			 * #getMethodName()
			 */
			public String getMethodName() {
				return methodName;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.riena.communication.core.hooks.ICallMessageContext
			 * #getRequestId()
			 */
			public String getRequestId() {
				if (requestId == null || requestId.length() == 0) {
					// FINDBUGS: if random.nextInt()==Integer.MIN_VALUE then Math.abs(i) = i
					int i = random.nextInt();
					if (i == Integer.MIN_VALUE) {
						i = i + 1;
					}
					requestId = "RID-" + Long.toString(System.currentTimeMillis() - base + Math.abs(i), 36); //$NON-NLS-1$
				}
				return requestId;
			}

		}
	}
}
