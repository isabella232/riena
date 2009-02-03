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

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.riena.core.injector.extension.DoNotReplaceSymbols;
import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;

/**
 *
 */
@ExtensionInterface
public interface IForEachExtension {

	/**
	 * @return The name of the iteration variable, ie <code>obj</code> in
	 *         <code>for(Object obj : list);</code>
	 */
	String getElement();

	/**
	 * We do not want variables to be replaced as the eclipse variable
	 * replacement would return a string instead of a list of objects.
	 * 
	 * @return The definition of the list of objects to be iterated.
	 */
	@DoNotReplaceSymbols
	String getIn();

	/**
	 * @return A list of submodule node definitions that are children of the
	 *         receiver
	 */
	@MapName("submodule")
	ISubModuleNodeExtension[] getSubModuleNodes();

	IConfigurationElement getConfigurationElement();
}
