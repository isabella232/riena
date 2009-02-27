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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.core.uiprocess.UIProcess;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;

/**
 * Example for blocking different parts of the user interface.
 */
public class BlockingSubModuleController extends SubModuleController {

	public static final String RIDGET_BLOCK_SUB_MODULE = "blockSubModule"; //$NON-NLS-1$

	public BlockingSubModuleController() {
		super();
	}

	public BlockingSubModuleController(ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	@Override
	public void configureRidgets() {
		super.configureRidgets();

		IActionRidget blockSubModule = (IActionRidget) getRidget(RIDGET_BLOCK_SUB_MODULE);
		blockSubModule.setText("Block SubModuleNode"); //$NON-NLS-1$
		blockSubModule.addListener(new IActionListener() {

			public void callback() {
				blockSubModule();
			}

		});
	}

	private void blockSubModule() {
		blockNode(getNavigationNode());
	}

	private void blockNode(INavigationNode<?> node) {
		new BlockerUIProcess(node).start();
	}

	private static class BlockerUIProcess extends UIProcess {

		private INavigationNode<?> toBlock;

		public BlockerUIProcess(INavigationNode<?> toBlock) {
			super("block", false); //$NON-NLS-1$
			this.toBlock = toBlock;
		}

		@Override
		public void initialUpdateUI(int totalWork) {
			toBlock.setBlocked(true);
		}

		@Override
		public void finalUpdateUI() {
			toBlock.setBlocked(false);
		}

		@Override
		public boolean runJob(IProgressMonitor monitor) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				return false;
			}

			return true;
		}

	}

}
