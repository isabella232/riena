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
package org.eclipse.riena.communication.core.progressmonitor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.riena.internal.communication.core.factory.CallHooksProxy;

/**
 * Maintains the progressMonitors that are interested in watched remoteservice
 * traffic
 */
public class ProgressMonitorRegistryImpl implements IRemoteProgressMonitorRegistry {

	private HashMap<Object, List<IRemoteProgressMonitor>> remoteProgressMonitors = new HashMap<Object, List<IRemoteProgressMonitor>>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.progressmonitor.IProgressMonitorRegistry
	 * #addProgressMonitor(java.lang.Object,
	 * org.eclipse.riena.communication.core.progressmonitor.IProgressMonitor,
	 * int)
	 */
	public void addProgressMonitor(Object callProxy, IRemoteProgressMonitor monitor, int monitorType) {
		// TODO very awful hack !!!!!
		if (Proxy.isProxyClass(callProxy.getClass())) {
			InvocationHandler invocationHandler = Proxy.getInvocationHandler(callProxy);
			if (invocationHandler instanceof CallHooksProxy) {
				callProxy = ((CallHooksProxy) invocationHandler).getCallProxy();
			}
		}
		List<IRemoteProgressMonitor> pmList = remoteProgressMonitors.get(callProxy);
		if (pmList == null) {
			pmList = new ArrayList<IRemoteProgressMonitor>();
			remoteProgressMonitors.put(callProxy, pmList);
		}
		pmList.add(monitor);
		// monitorType is ignored TODO
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.progressmonitor.IProgressMonitorRegistry
	 * #getProgressMonitors(java.lang.Object)
	 */
	public IRemoteProgressMonitorList getProgressMonitors(Object callProxy) {
		// callProxy.equals(new Object());
		List<IRemoteProgressMonitor> pmList = remoteProgressMonitors.get(callProxy);
		if (pmList == null) {
			return null;
			//			return new ProgressMonitorListImpl(new IRemoteProgressMonitor[0]);
		}
		return new ProgressMonitorListImpl(pmList.toArray(new IRemoteProgressMonitor[pmList.size()]));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.progressmonitor.IProgressMonitorRegistry
	 * #removeAllProgressMonitors(java.lang.Object)
	 */
	public void removeAllProgressMonitors(Object callProxy) {
		remoteProgressMonitors.remove(callProxy);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.progressmonitor.IProgressMonitorRegistry
	 * #
	 * removeProgressMonitor(org.eclipse.riena.communication.core.progressmonitor
	 * .IProgressMonitor)
	 */
	public void removeProgressMonitor(IRemoteProgressMonitor monitor) {
		for (List<IRemoteProgressMonitor> pmList : remoteProgressMonitors.values()) {
			// I dont need to know if there is actually one in the list
			pmList.remove(monitor);
		}
	}
}
