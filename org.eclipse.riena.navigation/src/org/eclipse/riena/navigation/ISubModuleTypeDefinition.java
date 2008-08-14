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
package org.eclipse.riena.navigation;

import org.eclipse.riena.core.extension.MapContributor;
import org.eclipse.riena.ui.ridgets.viewcontroller.IController;
import org.osgi.framework.Bundle;

/**
 * Interface for a SubModuleType extension that defines how an activated
 * submodule appears in the work area.
 */
public interface ISubModuleTypeDefinition extends ITypeDefinition {

	/**
	 * @return A controller that controlles the UI widgets in the view through
	 *         ridgets (see org.eclipse.riena.ui.ridgets.IRidget)
	 */
	IController createController();

	/**
	 * Indicates whether the view is shared i.e. whether one instance of the
	 * view should be used for all submodule instances.
	 * 
	 * @return true if the specified view should be a shared view, false
	 *         otherwise
	 */
	boolean isShared();

	/**
	 * @return For the SWT-based Riena UI this is the ID of the view associated
	 *         with the submodule. Must match the ID field of an
	 *         "org.eclipse.ui.view" extension.
	 */
	String getView();

	@MapContributor
	Bundle getContributingBundle();

}
