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

/**
 * Interface for a registry that maintains the list of progressmonitors that are
 * interested in monitoring remote service progress
 */
public interface IRemoteProgressMonitorRegistry {

	int MONITOR_ONE_CALL = 1;
	int MONITOR_MANY_CALLS = 2;

	/**
	 * Add a new progress Monitor for the specified remote Service proxy
	 * 
	 * @param callProxy
	 * @param monitor
	 * @param monitorType
	 *            can be MONITOR_ONE_CALL or MONITOR_MANY_CALLS, the former
	 *            means all progressMonitors are removed after one call
	 */
	void addProgressMonitor(Object callProxy, IRemoteProgressMonitor monitor, int monitorType);

	/**
	 * Removes a specific progressmonitor instance
	 * 
	 * @param monitor
	 */
	void removeProgressMonitor(IRemoteProgressMonitor monitor);

	/**
	 * Removes all progressMonitors for a specific proxy instance
	 * 
	 * @param callProxy
	 */
	void removeAllProgressMonitors(Object callProxy);

	/**
	 * Gets a list object for all progressListener for a proxy
	 * 
	 * @param callProxy
	 * @return
	 */
	IRemoteProgressMonitorList getProgressMonitors(Object callProxy);
}
