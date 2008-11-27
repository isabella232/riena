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

import org.eclipse.riena.core.util.ListenerList;
import org.eclipse.riena.core.util.ListenerList.Mode;

/**
 *
 */
public class ProgressMonitorListImpl implements IRemoteProgressMonitorList {

	private ListenerList<IRemoteProgressMonitor> progressMonitorList;

	public ProgressMonitorListImpl(IRemoteProgressMonitor[] progressMonitors) {
		super();
		this.progressMonitorList = new ListenerList<IRemoteProgressMonitor>(Mode.IDENTITY, IRemoteProgressMonitor.class);
		for (IRemoteProgressMonitor listener : progressMonitors) {
			this.progressMonitorList.add(listener);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.progressmonitor.IProgressMonitorList
	 * #fireReadEvent(int, int)
	 */
	public void fireReadEvent(int totalBytes, int bytesRead) {
		RemoteProgressMonitorEvent remoteProgressMonitorEvent = new RemoteProgressMonitorEvent(totalBytes, bytesRead);
		for (IRemoteProgressMonitor listener : progressMonitorList.getListeners()) {
			listener.response(remoteProgressMonitorEvent);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.progressmonitor.IProgressMonitorList
	 * #fireSendEvent(int, int)
	 */
	public void fireWriteEvent(int totalBytes, int bytesSent) {
		RemoteProgressMonitorEvent remoteProgressMonitorEvent = new RemoteProgressMonitorEvent(totalBytes, bytesSent);
		for (IRemoteProgressMonitor listener : progressMonitorList.getListeners()) {
			listener.request(remoteProgressMonitorEvent);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.progressmonitor.IProgressMonitorList
	 * #fireStartEvent()
	 */
	public void fireStartEvent() {
		for (IRemoteProgressMonitor listener : progressMonitorList.getListeners()) {
			listener.start();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.progressmonitor.IProgressMonitorList
	 * #fireEndEvent(int)
	 */
	public void fireEndEvent(int totalBytes) {
		RemoteProgressMonitorEvent remoteProgressMonitorEvent = new RemoteProgressMonitorEvent();
		for (IRemoteProgressMonitor listener : progressMonitorList.getListeners()) {
			listener.end();
		}
	}

}
