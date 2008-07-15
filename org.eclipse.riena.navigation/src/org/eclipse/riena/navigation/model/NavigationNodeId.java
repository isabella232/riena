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
package org.eclipse.riena.navigation.model;

import org.eclipse.riena.navigation.INavigationNodeId;

/**
 *
 */
public class NavigationNodeId implements INavigationNodeId {

	private String instanceId;
	private String typeId;

	public NavigationNodeId(String typeId, String instanceId) {
		this.typeId = typeId;
		this.instanceId = instanceId;
	}

	public NavigationNodeId(String typeId) {
		this(typeId, null);
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeId#getTypeId()
	 */
	public String getTypeId() {
		return typeId;
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeId#getInstanceId()
	 */
	public String getInstanceId() {
		return instanceId;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (other instanceof INavigationNodeId) {
			INavigationNodeId otherId = (INavigationNodeId) other;
			return equals(typeId, ((INavigationNodeId) other).getTypeId())
					&& equals(instanceId, otherId.getInstanceId());
		}
		return false;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		if (typeId != null) {
			return typeId.hashCode();
		}
		return 0;
	}

	private boolean equals(String string1, String string2) {
		return (string1 == null && string2 == null) || (string1 != null && string1.equals(string2));
	}

}
