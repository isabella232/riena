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

/**
 * A ModulePresentationFactory provides a ModulePresentationDefinition, given by
 * a presentationID
 * 
 * @author erich Achilles
 */
public interface IModulePresentationFactory {

	/**
	 * Returns the module presentation for this given presentationID
	 */

	IModulePresentationDefiniton getPresentation(String id);
}
