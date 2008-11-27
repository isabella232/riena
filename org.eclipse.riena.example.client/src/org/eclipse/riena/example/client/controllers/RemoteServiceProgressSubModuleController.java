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
package org.eclipse.riena.example.client.controllers;

import org.eclipse.riena.communication.core.progressmonitor.IRemoteProgressMonitorList;
import org.eclipse.riena.communication.core.progressmonitor.IRemoteProgressMonitorRegistry;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.example.client.communication.ServiceProgressVisualizer;
import org.eclipse.riena.internal.example.client.Activator;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;

public class RemoteServiceProgressSubModuleController extends SubModuleController {

	public static final String SERVICE_CALL_ACTION = "serviceCallAction"; //$NON-NLS-1$

	private IRemoteProgressMonitorRegistry remoteProgressMonitorRegistry;

	public RemoteServiceProgressSubModuleController() {
		Inject.service(IRemoteProgressMonitorRegistry.class.getName()).useRanking().into(this).andStart(
				Activator.getDefault().getContext());
	}

	public void bind(IRemoteProgressMonitorRegistry remoteProgressMonitorRegistry) {
		this.remoteProgressMonitorRegistry = remoteProgressMonitorRegistry;
	}

	public void unbind(IRemoteProgressMonitorRegistry remoteProgressMonitorRegistry) {
		this.remoteProgressMonitorRegistry = null;
	}

	@Override
	public void configureRidgets() {
		getActionRidget().setText("Simulate Call"); //$NON-NLS-1$
		registerRemoteServiceCallback();
	}

	private IActionRidget getActionRidget() {
		return IActionRidget.class.cast(getRidget(SERVICE_CALL_ACTION));
	}

	private void registerRemoteServiceCallback() {
		getActionRidget().addListener(new RemoteServiceCallbackAdapter());
	}

	private void simulateRemoteServiceCall() {
		//fork
		new CommunicationSimulator().start();
	}

	class CommunicationSimulator extends Thread {
		@Override
		public void run() {
			final Object proxy = new Object();
			ServiceProgressVisualizer serviceProgress = new ServiceProgressVisualizer("remote"); //$NON-NLS-1$
			remoteProgressMonitorRegistry.addProgressMonitor(proxy, serviceProgress,
					IRemoteProgressMonitorRegistry.MONITOR_MANY_CALLS);
			IRemoteProgressMonitorList progressMonitors = remoteProgressMonitorRegistry.getProgressMonitors(proxy);
			startCommunication(progressMonitors);
			communicate(progressMonitors);
			endCommuncation(progressMonitors);
			remoteProgressMonitorRegistry.removeAllProgressMonitors(proxy);
		}

		private void endCommuncation(IRemoteProgressMonitorList progressMonitors) {
			progressMonitors.fireEndEvent(0);
		}

		private void startCommunication(IRemoteProgressMonitorList progressMonitors) {
			progressMonitors.fireStartEvent();
		}

		private void communicate(IRemoteProgressMonitorList progressMonitors) {
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
				sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	class RemoteServiceCallbackAdapter implements IActionListener {

		public void callback() {
			simulateRemoteServiceCall();
		}

	}

}
