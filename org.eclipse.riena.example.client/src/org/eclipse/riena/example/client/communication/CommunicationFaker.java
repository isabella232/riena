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
package org.eclipse.riena.example.client.communication;

import org.eclipse.riena.communication.core.progressmonitor.IRemoteProgressMonitorList;

public class CommunicationFaker {

	public void communicate(IRemoteProgressMonitorList progressMonitors) {
		startCommunication(progressMonitors);
		doCommunicate(progressMonitors);
		endCommuncation(progressMonitors);
	}

	private void endCommuncation(IRemoteProgressMonitorList progressMonitors) {
		progressMonitors.fireEndEvent(0);
	}

	private void startCommunication(IRemoteProgressMonitorList progressMonitors) {
		progressMonitors.fireStartEvent();
	}

	private void doCommunicate(IRemoteProgressMonitorList progressMonitors) {
		for (int i = 0; i < 100; i += 1) {
			sendData(progressMonitors);
			delay();
			receiveData(progressMonitors);
		}
	}

	private void sendData(IRemoteProgressMonitorList progressMonitors) {
		progressMonitors.fireWriteEvent(100, 1);
	}

	private void receiveData(IRemoteProgressMonitorList progressMonitors) {
		progressMonitors.fireReadEvent(100, 1);
	}

	private void delay() {
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
