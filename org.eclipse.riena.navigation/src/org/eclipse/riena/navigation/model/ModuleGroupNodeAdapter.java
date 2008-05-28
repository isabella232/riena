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
import org.eclipse.riena.navigation.IModuleGroupNodeListener;
import org.eclipse.riena.navigation.IModuleNode;

/**
 * Default implementation for the IModuleGroupNodeListener
 */
public class ModuleGroupNodeAdapter extends NavigationNodeAdapter<IModuleGroupNode, IModuleNode> implements IModuleGroupNodeListener {

	public void presentWithSingleModuleChanged(IModuleGroupNode source) {

	}

}
