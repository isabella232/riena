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
 * An ID that identifies a node in the application model tree. The ID is used to
 * find navigate targets and to associated sub module nodes with their views.
 */
public interface INavigationNodeId {

	/**
	 * Returns the type of a node. Nodes of the same type are created using the
	 * same node builder. Sub module nodes of the same type use the same type of
	 * view. Both is configured using extensions (NavigationNodeType and
	 * SubModuleType). This typeId is used to find the right extension.
	 * 
	 * @see INavigationNodeBuilder
	 * @return The type ID of a navigation node.
	 */
	String getTypeId();

	/**
	 * The optional instance ID is used to differentiate between nodes of the
	 * same type. E.g. two nodes representing employees that have the same type
	 * ID could use the social security number as instance ID.
	 * 
	 * @return The instance ID of a navigation node.
	 */
	String getInstanceId();

}
