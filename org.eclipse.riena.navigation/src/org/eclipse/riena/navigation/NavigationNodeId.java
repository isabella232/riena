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
public class NavigationNodeId {

	private String instanceId;
	private String typeId;
	private int hash = 0;

	public NavigationNodeId(String typeId, String instanceId) {
		this.typeId = typeId;
		this.instanceId = instanceId;
	}

	public NavigationNodeId(String typeId) {
		this(typeId, null);
	}

	/**
	 * Returns the type of a node. Nodes of the same type are created using the
	 * same node builder. Sub module nodes of the same type use the same type of
	 * view. Both is configured using extensions (NavigationNodeType and
	 * SubModuleType). This typeId is used to find the right extension.
	 * 
	 * @see INavigationNodeBuilder
	 * @return The type ID of a navigation node.
	 */
	public String getTypeId() {
		return typeId;
	}

	/**
	 * The optional instance ID is used to differentiate between nodes of the
	 * same type. E.g. two nodes representing employees that have the same type
	 * ID could use the social security number as instance ID.
	 * 
	 * @return The instance ID of a navigation node.
	 */
	public String getInstanceId() {
		return instanceId;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (other instanceof NavigationNodeId) {
			if (!getClass().equals(other.getClass())) {
				return false;
			}
			NavigationNodeId otherId = (NavigationNodeId) other;
			return equals(typeId, otherId.getTypeId()) && equals(instanceId, otherId.getInstanceId());
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("NavNodeId:");
		if (typeId != null) {
			sb.append(typeId);
		} else {
			sb.append("null");
		}
		if (instanceId != null) {
			sb.append("["); //$NON-NLS-1$
			sb.append(instanceId);
			sb.append("]"); //$NON-NLS-1$
		} else {
			sb.append("[null]");
		}
		return sb.toString();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if (hash == 0) {
			if (typeId != null) {
				hash += typeId.hashCode();
			}
			if (instanceId != null) {
				hash += instanceId.hashCode();
			}
		}
		return hash;
	}

	private boolean equals(String string1, String string2) {
		return (string1 == null && string2 == null) || (string1 != null && string1.equals(string2));
	}

}
