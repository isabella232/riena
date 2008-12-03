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

import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.core.uiprocess.UIProcess;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 *
 */
public class ExceptionTestController extends SubModuleController {

	@Override
	public void configureRidgets() {
		// TODO Auto-generated method stub
		super.configureRidgets();

		IActionRidget localNullPtr = (IActionRidget) getRidget("localNullPointerAction");
		localNullPtr.addListener(new IActionListener() {

			public void callback() {
				throw new NullPointerException("test nullpointer exception ");

			}
		});

		IActionRidget uiprocessNullPtr = (IActionRidget) getRidget("uiprocessNullPointerAction");
		uiprocessNullPtr.addListener(new IActionListener() {

			public void callback() {
				UIProcess process = new UIProcess("TestException", true, getNavigationNode()) {

					@Override
					protected void afterRun(IProgressMonitor monitor) {
						// TODO Auto-generated method stub
						super.afterRun(monitor);
					}

					@Override
					protected void beforeRun(IProgressMonitor monitor) {
						// TODO Auto-generated method stub
						super.beforeRun(monitor);
					}

					@Override
					public void finalUpdateUI() {
						// TODO Auto-generated method stub
						super.finalUpdateUI();
					}

					@Override
					public boolean runJob(IProgressMonitor monitor) {
						throw new NullPointerException("nullpointer in runJob"); //$NON-NLS-1$
					}

					@Override
					public void updateProgress(int progress) {
						// TODO Auto-generated method stub
						super.updateProgress(progress);
					}

					@Override
					public void updateUi() {
						// TODO Auto-generated method stub
						super.updateUi();
					} //$NON-NLS-1$

				};
				process.start();

			}
		});
	}

}
