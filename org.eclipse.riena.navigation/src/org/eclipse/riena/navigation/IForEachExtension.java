/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2008 compeople AG                         *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.navigation;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.riena.core.extension.DoNotReplaceSymbols;
import org.eclipse.riena.core.extension.ExtensionInterface;
import org.eclipse.riena.core.extension.MapName;

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
