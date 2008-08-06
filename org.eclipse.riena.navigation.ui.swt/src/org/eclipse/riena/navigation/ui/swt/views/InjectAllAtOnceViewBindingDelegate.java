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
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.riena.navigation.ui.swt.binding.DefaultSwtControlRidgetMapper;
import org.eclipse.riena.ui.ridgets.uibinding.IBindingManager;
import org.eclipse.riena.ui.ridgets.uibinding.InjectAllAtOnceBindingManager;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;

/**
 *
 */
public class InjectAllAtOnceViewBindingDelegate extends SWTViewBindingDelegate {

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.views.SWTViewBindingDelegate#createBindingManager()
	 */
	@Override
	protected IBindingManager createBindingManager() {
		return new InjectAllAtOnceBindingManager(new SWTBindingPropertyLocator(), new DefaultSwtControlRidgetMapper());
	}

}
