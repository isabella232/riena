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
import org.eclipse.riena.ui.ridgets.viewcontroller.IViewController;
import org.osgi.framework.Bundle;

/**
 * A WorkAreaPresentationDefinition defines the work area to be activated.
 * 
 * @author Erich Achilles
 */
public interface ISubModuleTypeDefinition extends ITypeDefinition {

	/**
	 * Returns the view controller for this SubModuleType
	 */
	IViewController createController();

	/**
	 * Returns the view controller name
	 */
	String getController();

	/**
	 * Return true if the specified view should be a shared view, false
	 * otherwise
	 */
	boolean isShared();

	/**
	 * Return an view name
	 */
	String getView();

	@MapContributor
	Bundle getContributingBundle();

}
