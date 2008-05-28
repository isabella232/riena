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

import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.ISubApplication;
import org.eclipse.riena.navigation.ISubApplicationListener;

/**
 * Default implementation for the sub application
 */
public class SubApplication extends NavigationNode<ISubApplication, IModuleGroupNode, ISubApplicationListener> implements ISubApplication {

	/**
	 * 
	 */
	public SubApplication() {
		super();
	}

	/**
	 * @param children
	 */
	public SubApplication(IModuleGroupNode... children) {
		super(children);
	}

	/**
	 * @param label
	 * @param children
	 */
	public SubApplication(String label, IModuleGroupNode... children) {
		super(label, children);
	}

	/**
	 * @param label
	 */
	public SubApplication(String label) {
		super(label);
	}

	@Override
	protected void addChildParent(IModuleGroupNode child) {
		child.setParent(this);
	}

}
