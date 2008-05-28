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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleNodeViewController;
import org.eclipse.riena.ui.core.uiprocess.ProgressProviderBridge;
import org.eclipse.riena.ui.core.uiprocess.UIProcess;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;

/**
 * 
 */
public class UIProcessDemoController extends SubModuleNodeViewController {

	private IActionRidget actionRidget;
	private boolean registered;

	public UIProcessDemoController(ISubModuleNode navigationNode) {
		super(navigationNode);
		Job.getJobManager().setProgressProvider(ProgressProviderBridge.instance());
	}

	public void setActionRidget(IActionRidget actionRidget) {
		this.actionRidget = actionRidget;
	}

	public IActionRidget getActionRidget() {
		return actionRidget;
	}

	@Override
	public void afterBind() {
		super.afterBind();
		if (getActionRidget() != null && !registered) {
			getActionRidget().addListener(new ProcessAction());
			registered = true;
		}

	}

	void runProgress() {
		// OLD:
		// UIProcess p = new UIProcess("sample task", new
		// DialogProgressProvider(new SwtUISynchronizer())) {
		// NEW: With extension point:
		UIProcess p = new UIProcess("sample task") {
			@Override
			public boolean runJob(IProgressMonitor monitor) {
				monitor.beginTask("sample task", 30); //$NON-NLS-1$
				for (int i = 0; i < 30; i++) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// System.out.print('#');
					monitor.worked(i);
				}
				// System.out.println("done.");
				return true;
			}
		};
		p.start();

	}

	private class ProcessAction implements IActionListener {

		public void callback() {
			runProgress();
		}

	}

}
