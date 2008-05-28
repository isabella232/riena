/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation.model;

import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.IModuleNodeListener;
import org.eclipse.riena.navigation.ISubModuleNode;

/**
 * Default implementation for the module node
 */
public class ModuleNode extends NavigationNode<IModuleNode, ISubModuleNode, IModuleNodeListener> implements IModuleNode {

	private boolean presentSingleSubModule;

	/**
	 * 
	 */
	public ModuleNode() {
		super();
	}

	/**
	 * @param children
	 */
	public ModuleNode(ISubModuleNode... children) {
		super(children);
	}

	/**
	 * @param label
	 * @param children
	 */
	public ModuleNode(String label, ISubModuleNode... children) {
		super(label, children);
	}

	/**
	 * @param label
	 */
	public ModuleNode(String label) {
		super(label);
	}

	/**
	 * @return the presentSingleSubModule
	 */
	public boolean isPresentSingleSubModule() {
		return presentSingleSubModule;
	}

	/**
	 * @param presentSingleSubModule
	 *            the presentSingleSubModule to set
	 */
	public void setPresentSingleSubModule(boolean presentSingleSubModule) {
		this.presentSingleSubModule = presentSingleSubModule;
		notifyPresentSingleSubModuleChanged();
	}

	private void notifyPresentSingleSubModuleChanged() {
		for (IModuleNodeListener next : getListeners()) {
			next.presentSingleSubModuleChanged(this);
		}
	}

	public boolean isPresentSubModules() {
		return isPresentSingleSubModule() || !(getChildren().size() == 1 && getChild(0).getChildren().isEmpty());
	}

}
