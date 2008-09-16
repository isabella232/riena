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

import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubApplicationController;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;

/**
 * This controller adds dynamically nodes to the navigation.
 */
public class NavigationSubModuleController extends SubModuleController {

	private IActionRidget addSubModuleToModuleBtn;
	private IActionRidget addSubModuleToSelfBtn;
	private IActionRidget addModuleBtn;
	private IActionRidget addModuleGroupBtn;

	private int nodeCount = 0;

	public NavigationSubModuleController() {
		this(null);
	}

	/**
	 * @param navigationNode
	 */
	public NavigationSubModuleController(ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	/**
	 * Binds and updates the ridgets.<br>
	 * Sets texts and adds action listeners.
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#configureRidgets()
	 */
	public void configureRidgets() {

		getAddSubModuleToModuleBtn().setText("Add Sub-Module to &Root"); //$NON-NLS-1$
		getAddSubModuleToModuleBtn().addListener(new IActionListener() {
			public void callback() {
				ISubModuleNode newNode = createSubModuleNode("Node " + String.valueOf(nodeCount++)); //$NON-NLS-1$
				IModuleNode parent = getParentNodeOfType(getNavigationNode(), IModuleNode.class);
				parent.addChild(newNode);
				String text = "Sub-Module was added!"; //$NON-NLS-1$
				SubApplicationController subAppController = getSubApplicationController();
				subAppController.getStatuslineRidget().setMessage(text);
			}
		});

		getAddSubModuleToSelfBtn().setText("Add S&ub-Module this Node"); //$NON-NLS-1$
		getAddSubModuleToSelfBtn().addListener(new IActionListener() {
			public void callback() {
				ISubModuleNode navigationNode = getNavigationNode();
				navigationNode.addChild(createSubModuleNode("Node " + String.valueOf(nodeCount++))); //$NON-NLS-1$
				String text = "Sub-Module was added!"; //$NON-NLS-1$
				SubApplicationController subAppController = getSubApplicationController();
				subAppController.getStatuslineRidget().setMessage(text);
			}
		});

		getAddModuleBtn().setText("Add &Module"); //$NON-NLS-1$
		getAddModuleBtn().addListener(new IActionListener() {
			public void callback() {
				IModuleGroupNode parent = getParentNodeOfType(getNavigationNode(), IModuleGroupNode.class);
				parent.addChild(createModuleNode());
				String text = "Module was added!"; //$NON-NLS-1$
				SubApplicationController subAppController = getSubApplicationController();
				subAppController.getStatuslineRidget().setMessage(text);
			}
		});

		getAddModuleGroupBtn().setText("Add Module-&Group"); //$NON-NLS-1$
		getAddModuleGroupBtn().addListener(new IActionListener() {
			public void callback() {
				ISubApplicationNode parent = getParentNodeOfType(getNavigationNode(), ISubApplicationNode.class);
				parent.addChild(createModuleGroupNode());
				String text = "Module-Group was added!"; //$NON-NLS-1$
				SubApplicationController subAppController = getSubApplicationController();
				subAppController.getStatuslineRidget().setMessage(text);
			}
		});

		setDefaultButton(getAddModuleBtn());

	}

	private <N extends INavigationNode<?>> N getParentNodeOfType(INavigationNode<?> node, Class<N> clazz) {
		return node.getParentOfType(clazz);
	}

	/**
	 * @return the addSubModuleToModuleBtn
	 */
	public IActionRidget getAddSubModuleToModuleBtn() {
		return addSubModuleToModuleBtn;
	}

	/**
	 * @param addSubModuleToModuleBtn
	 *            the addSubModuleToModuleBtn to set
	 */
	public void setAddSubModuleToModuleBtn(IActionRidget addSubModuleToModuleBtn) {
		this.addSubModuleToModuleBtn = addSubModuleToModuleBtn;
	}

	/**
	 * @return the addSubModuleToSelfBtn
	 */
	public IActionRidget getAddSubModuleToSelfBtn() {
		return addSubModuleToSelfBtn;
	}

	/**
	 * @param addSubModuleToSelfBtn
	 *            the addSubModuleToSelfBtn to set
	 */
	public void setAddSubModuleToSelfBtn(IActionRidget addSubModuleToSelfBtn) {
		this.addSubModuleToSelfBtn = addSubModuleToSelfBtn;
	}

	/**
	 * @return the addModuleBtn
	 */
	public IActionRidget getAddModuleBtn() {
		return addModuleBtn;
	}

	/**
	 * @param addModuleBtn
	 *            the addModuleBtn to set
	 */
	public void setAddModuleBtn(IActionRidget addModuleBtn) {
		this.addModuleBtn = addModuleBtn;
	}

	/**
	 * @return the addModuleGroupBtn
	 */
	public IActionRidget getAddModuleGroupBtn() {
		return addModuleGroupBtn;
	}

	/**
	 * @param addModuleGroupBtn
	 *            the addModuleGroupBtn to set
	 */
	public void setAddModuleGroupBtn(IActionRidget addModuleGroupBtn) {
		this.addModuleGroupBtn = addModuleGroupBtn;
	}

	/**
	 * Creates a new module group and adds a new module to the group.
	 * 
	 * @return module group
	 */
	private IModuleGroupNode createModuleGroupNode() {

		IModuleGroupNode newModuleGroupNode = new ModuleGroupNode(null);
		IModuleNode newModuleNode = createModuleNode();
		newModuleGroupNode.addChild(newModuleNode);

		return newModuleGroupNode;
	}

	/**
	 * Creates a new module and adds a new sub-module to the module.
	 * 
	 * @return module
	 */
	private IModuleNode createModuleNode() {

		IModuleNode newModuleNode = new ModuleNode(null, "Added child Module to Module Group"); //$NON-NLS-1$
		ISubModuleNode newSubModuleNode = createSubModuleNode("Added child SubModule to Module"); //$NON-NLS-1$
		newModuleNode.addChild(newSubModuleNode);

		return newModuleNode;
	}

	/**
	 * Creates a new sub-module with the given label.
	 * 
	 * @param label
	 *            - label of the sub-module
	 * @return sub-module
	 */
	private ISubModuleNode createSubModuleNode(String label) {

		ISubModuleNode newSubModuleNode = new SubModuleNode(
				new NavigationNodeId("org.eclipse.riena.example.navigation"), label); //$NON-NLS-1$

		return newSubModuleNode;

	}

	/**
	 * Returns the controller of the parent sub-application.
	 * 
	 * @return sub-application controller
	 */
	private SubApplicationController getSubApplicationController() {
		return (SubApplicationController) getNavigationNode().getParentOfType(ISubApplicationNode.class)
				.getNavigationNodeController();
	}

}
