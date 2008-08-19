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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Maintains the progressMonitors that are interested in watched remoteservice
 * traffic
 */
public class ProgressMonitorRegistryImpl implements IProgressMonitorRegistry {

	private HashMap<Object, List<IProgressMonitor>> progressMonitors = new HashMap<Object, List<IProgressMonitor>>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.progressmonitor.IProgressMonitorRegistry
	 * #addProgressMonitor(java.lang.Object,
	 * org.eclipse.riena.communication.core.progressmonitor.IProgressMonitor,
	 * int)
	 */
	public void addProgressMonitor(Object callProxy, IProgressMonitor monitor, int monitorType) {
		List<IProgressMonitor> pmList = progressMonitors.get(callProxy);
		if (pmList == null) {
			pmList = new ArrayList<IProgressMonitor>();
			progressMonitors.put(callProxy, pmList);
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
	public IProgressMonitorList getProgressMonitors(Object callProxy) {
		List<IProgressMonitor> pmList = progressMonitors.get(callProxy);
		if (pmList == null) {
			return new ProgressMonitorListImpl(null);
		}
		return new ProgressMonitorListImpl(pmList.toArray(new IProgressMonitor[pmList.size()]));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.progressmonitor.IProgressMonitorRegistry
	 * #removeAllProgressMonitors(java.lang.Object)
	 */
	public void removeAllProgressMonitors(Object callProxy) {
		progressMonitors.remove(callProxy);
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
	public void removeProgressMonitor(IProgressMonitor monitor) {
		for (List<IProgressMonitor> pmList : progressMonitors.values()) {
			// I dont need to know if there is actually one in the list
			pmList.remove(monitor);
		}
	}
}
