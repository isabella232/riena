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

import org.eclipse.riena.example.client.views.NavigationSubModuleView;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubApplicationViewController;
import org.eclipse.riena.navigation.ui.controllers.SubModuleNodeViewController;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManagerAccessor;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;

/**
 * This controller adds dynamically nodes to the navigation.
 */
public class NavigationSubModuleViewController extends SubModuleNodeViewController {

	private IActionRidget addSubModuleToModuleBtn;
	private IActionRidget addSubModuleToSelfBtn;
	private IActionRidget addModuleBtn;
	private IActionRidget addModuleGroupBtn;

	/**
	 * @param navigationNode
	 */
	public NavigationSubModuleViewController(ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.controllers.SubModuleNodeViewController#afterBind()
	 */
	@Override
	public void afterBind() {
		super.afterBind();
		initRidgets();
		setDefaultButton(getAddModuleBtn());
	}

	/**
	 * Binds and updates the ridgets.<br>
	 * Sets texts and adds action listeners.
	 */
	private void initRidgets() {

		getAddSubModuleToModuleBtn().setText("Add &Sub-Module to Module"); //$NON-NLS-1$
		getAddSubModuleToModuleBtn().addListener(new IActionListener() {
			public void callback() {
				IModuleNode parent = getParentNodeOfType(getNavigationNode(), IModuleNode.class);
				ISubModuleNode newNode = createSubModuleNode("Added child SubModule to Module"); //$NON-NLS-1$
				parent.addChild(newNode);
				String text = "Sub-Module was added!";
				SubApplicationViewController subAppController = getSubApplicationController();
				subAppController.getStatuslineRidget().setMessage(text);
				subAppController.getStatuslineRidget().getStatuslineNumberRidget().setNumber(4711);
			}
		});

		getAddSubModuleToSelfBtn().setText("Add S&ub-Module to Self"); //$NON-NLS-1$
		getAddSubModuleToSelfBtn().addListener(new IActionListener() {
			public void callback() {
				ISubModuleNode newNode = createSubModuleNode("Added child SubModule to SubModule"); //$NON-NLS-1$
				getNavigationNode().addChild(newNode);
				String text = "Sub-Module was added!";
				SubApplicationViewController subAppController = getSubApplicationController();
				subAppController.getStatuslineRidget().info(text);
				subAppController.getStatuslineRidget().getStatuslineNumberRidget().setNumber(4711);
			}
		});

		getAddModuleBtn().setText("Add &Module"); //$NON-NLS-1$
		getAddModuleBtn().addListener(new IActionListener() {
			public void callback() {
				IModuleGroupNode parent = getParentNodeOfType(getNavigationNode(), IModuleGroupNode.class);
				parent.addChild(createModuleNode());
				String text = "Module was added!";
				SubApplicationViewController subAppController = getSubApplicationController();
				subAppController.getStatuslineRidget().warning(text);
				subAppController.getStatuslineRidget().getStatuslineNumberRidget().setNumber(4711);
			}
		});

		getAddModuleGroupBtn().setText("Add Module-&Group"); //$NON-NLS-1$
		getAddModuleGroupBtn().addListener(new IActionListener() {
			public void callback() {
				ISubApplicationNode parent = getParentNodeOfType(getNavigationNode(), ISubApplicationNode.class);
				parent.addChild(createModuleGroupNode());
				String text = "Module-Group was added!";
				SubApplicationViewController subAppController = getSubApplicationController();
				subAppController.getStatuslineRidget().error(text);
				subAppController.getStatuslineRidget().getStatuslineNumberRidget().setNumber(4711);
			}
		});

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

		IModuleGroupNode newModuleGroupNode = new ModuleGroupNode("Added child Module Group to SubApplication"); //$NON-NLS-1$
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

		IModuleNode newModuleNode = new ModuleNode("Added child Module to Module Group"); //$NON-NLS-1$
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

		ISubModuleNode newSubModuleNode = new SubModuleNode(label);
		SwtPresentationManagerAccessor.getManager().present(newSubModuleNode, NavigationSubModuleView.ID);

		return newSubModuleNode;

	}

	/**
	 * Returns the controller of the parent sub-application.
	 * 
	 * @return sub-application controller
	 */
	private SubApplicationViewController getSubApplicationController() {
		return (SubApplicationViewController) getNavigationNode().getParentOfType(ISubApplicationNode.class)
				.getPresentation();
	}

}
