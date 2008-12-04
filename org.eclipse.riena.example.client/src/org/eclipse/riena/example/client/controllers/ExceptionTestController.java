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

		IActionRidget uiprocessNullPtr1 = (IActionRidget) getRidget("uiprocessNullPointerActionRunJob");
		uiprocessNullPtr1.addListener(new IActionListener() {

			public void callback() {
				UIProcess process = new UIProcess("TestException", true, getNavigationNode()) {

					@Override
					public boolean runJob(IProgressMonitor monitor) {
						throw new NullPointerException("nullpointer in runJob"); //$NON-NLS-1$
					}

				};
				process.start();

			}
		});
		IActionRidget uiprocessNullPtr2 = (IActionRidget) getRidget("uiprocessNullPointerActionUpdateUI");
		uiprocessNullPtr2.addListener(new IActionListener() {

			public void callback() {
				UIProcess process = new UIProcess("TestException", true, getNavigationNode()) {

					public boolean runJob(IProgressMonitor monitor) {
						notifyUpdateUI();
						return false;
					}

					@Override
					public void updateUi() {
						throw new NullPointerException("nullpointer in updateUI"); //$NON-NLS-1$
					}

				};
				process.start();

			}
		});
		IActionRidget uiprocessNullPtr3 = (IActionRidget) getRidget("uiprocessNullPointerActionFinalUpdateUI");
		uiprocessNullPtr3.addListener(new IActionListener() {

			public void callback() {
				UIProcess process = new UIProcess("TestException", true, getNavigationNode()) {

					@Override
					public void finalUpdateUI() {
						throw new NullPointerException("nullpointer in finalUpdateUI"); //$NON-NLS-1$
					}

				};
				process.start();

			}
		});
	}

}
