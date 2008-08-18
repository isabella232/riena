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
package org.eclipse.riena.ui.ridgets.swt;

import org.eclipse.riena.ui.ridgets.swt.uibinding.DefaultSwtBindingDelegate;
import org.eclipse.riena.ui.ridgets.viewcontroller.IController;

/**
 * TODO [ev] docs
 */
public class SwtBindingDelegate extends DefaultSwtBindingDelegate {

	@Override
	public void bind(IController viewController) {
		// TODO [ev] discuss - move this to DefaultSwtViewBindingDelegate ?
		super.bind(viewController);
		viewController.afterBind();
	}
}
