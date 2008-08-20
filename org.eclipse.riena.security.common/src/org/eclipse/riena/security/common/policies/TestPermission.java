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
package org.eclipse.riena.security.common.policies;

import java.security.Permission;

public class TestPermission extends Permission {

	public TestPermission(String name) {
		super(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TestPermission) {
			return ((TestPermission) obj).getName().equals(this.getName());
		}
		return false;
	}

	@Override
	public String getActions() {
		return null;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean implies(Permission permission) {
		if (this.equals(permission)) {
			return true;
		}
		return false;
	}

}
