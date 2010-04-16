/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.example.client.controllers;

import org.eclipse.riena.navigation.IJumpTargetListener;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;

/**
 * Controller demonstrating the use of {@link INavigationNode#jumpBack()} and
 * {@link IJumpTargetListener}
 */
public class DemoTargetSubModuleController extends SubModuleController {

	public final static String JUMP_BACK_RIDGET_ID = "jumpBackRidget"; //$NON-NLS-1$
	private IActionRidget jumpBackActionRidget;

	@Override
	public void configureRidgets() {
		super.configureRidgets();
		observerJumpBackActionRidget();
	}

	private void observerJumpBackActionRidget() {
		jumpBackActionRidget = getRidget(IActionRidget.class, JUMP_BACK_RIDGET_ID);
		jumpBackActionRidget.setEnabled(getNavigationNode().isJumpTarget());
		getNavigationNode().addJumpTargetListener(new IJumpTargetListener() {

			public void jumpTargetStateChanged(INavigationNode<?> node, JumpTargetState jumpTargetState) {
				jumpBackActionRidget.setEnabled(jumpTargetState == JumpTargetState.ENABLED);
			}

		});
		jumpBackActionRidget.addListener(new IActionListener() {

			public void callback() {
				getNavigationNode().jumpBack();
			}
		});
	}

}
