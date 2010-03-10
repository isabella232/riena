/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation;

import org.eclipse.riena.navigation.extension.INavigationAssembly2Extension;

/**
 * Abstract class that supplies some standard methods for INavigationAssembler
 */
public abstract class AbstractNavigationAssembler implements INavigationAssembler {

	private String id;
	private String parentNodeId;
	private int startOrder;
	private INavigationAssembly2Extension assembly;

	/**
	 * {@inheritDoc}
	 */
	public String getId() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getParentNodeId() {
		return parentNodeId;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setParentNodeId(String parentNodeId) {
		this.parentNodeId = parentNodeId;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getStartOrder() {
		return startOrder;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setStartOrder(int startOrder) {
		this.startOrder = startOrder;
	}

	/**
	 * {@inheritDoc}
	 */
	public INavigationAssembly2Extension getAssembly() {
		return assembly;
	}

	/**
	 * {@inheritDoc}
	 */
	public void setAssembly(INavigationAssembly2Extension assembly) {
		this.assembly = assembly;
	}

}
