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

import org.eclipse.riena.example.client.views.BlockingSubModuleView;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.core.uiprocess.UIProcess;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ILabelRidget;

/**
 * Example for blocking different parts of the user interface.
 * 
 * @see BlockingSubModuleView
 */
public class BlockingSubModuleController extends SubModuleController {

	public static final String RIDGET_BLOCK_MODULE = "blockModule"; //$NON-NLS-1$
	public static final String RIDGET_BLOCK_SUB_MODULE = "blockSubModule"; //$NON-NLS-1$
	public static final String RIDGET_BLOCK_SUB_APP = "blockSubApplication"; //$NON-NLS-1$
	public static final String RIDGET_STATUS = "status"; //$NON-NLS-1$

	private ILabelRidget status;

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
		blockSubModule.setText("Block current SubModule = Blocking"); //$NON-NLS-1$
		blockSubModule.addListener(new IActionListener() {
			public void callback() {
				blockNode(getNavigationNode());
			}
		});

		IActionRidget blockModule = (IActionRidget) getRidget(RIDGET_BLOCK_MODULE);
		blockModule.setText("Block current Module = Playground"); //$NON-NLS-1$
		blockModule.addListener(new IActionListener() {
			public void callback() {
				blockNode(getModuleNode());
			}
		});

		IActionRidget blockSubApp = (IActionRidget) getRidget(RIDGET_BLOCK_SUB_APP);
		blockSubApp.setText("Block current SubApplication = Playground Tab"); //$NON-NLS-1$
		blockSubApp.addListener(new IActionListener() {
			public void callback() {
				blockNode(getSubApplicationNode());
			}
		});

		status = (ILabelRidget) getRidget(RIDGET_STATUS);
	}

	// helping methods
	//////////////////

	private void blockNode(INavigationNode<?> node) {
		new BlockerUIProcess(node, status).start();
	}

	private INavigationNode<?> getModuleNode() {
		return getNavigationNode().getParentOfType(IModuleNode.class);
	}

	private INavigationNode<?> getSubApplicationNode() {
		return getNavigationNode().getParentOfType(ISubApplicationNode.class);
	}

	// helping classes
	//////////////////

	/**
	 * Blocks the given {@link INavigationNode} for a number of seconds.
	 */
	private static class BlockerUIProcess extends UIProcess {

		private final INavigationNode<?> toBlock;
		private final ILabelRidget labelRidget;

		public BlockerUIProcess(INavigationNode<?> toBlock, ILabelRidget labelRidget) {
			super("block", false); //$NON-NLS-1$
			this.toBlock = toBlock;
			this.labelRidget = labelRidget;
		}

		@Override
		public void initialUpdateUI(int totalWork) {
			labelRidget.setText(String.format("Blocking '%s' for 10s", toBlock.getLabel())); //$NON-NLS-1$
			toBlock.setBlocked(true);
		}

		@Override
		public void finalUpdateUI() {
			labelRidget.setText(String.format("Unblocked '%s'", toBlock.getLabel())); //$NON-NLS-1$
			toBlock.setBlocked(false);
		}

		@Override
		public boolean runJob(IProgressMonitor monitor) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				return false;
			}
			return true;
		}
	}
}
