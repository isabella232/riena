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
 * Contains additional information for a navigation passed on to the target node
 * and/or used during its creation.
 * 
 * @see INavigationNode#navigate(INavigationNodeId, NavigationArgument)
 */
public class NavigationArgument {
	private Object argument;
	private String parentNodeId;
	private INavigationArgumentListener argumentListener;

	/**
	 * @param argument
	 * @param argumentListener
	 * @param parentNodeId
	 */
	public NavigationArgument(Object argument, INavigationArgumentListener argumentListener, String parentNodeId) {
		super();
		this.argument = argument;
		this.argumentListener = argumentListener;
		this.parentNodeId = parentNodeId;
	}

	/**
	 * @param argument
	 */
	public NavigationArgument(Object argument) {
		super();
		this.argument = argument;
	}

	/**
	 * @param argument
	 * @param argumentListener
	 */
	public NavigationArgument(Object argument, INavigationArgumentListener argumentListener) {
		super();
		this.argument = argument;
		this.argumentListener = argumentListener;
	}

	/**
	 * @return the externalParameter
	 */
	public Object getExternalParameter() {
		return argument;
	}

	/**
	 * @param externalParameter
	 *            the externalParameter to set
	 */
	public void setExternalParameter(Object externalParameter) {
		this.argument = externalParameter;
	}

	/**
	 * @return the parentNodeId
	 */
	public String getParentNodeId() {
		return parentNodeId;
	}

	/**
	 * @param parentNodeId
	 *            the parentNodeId to set
	 */
	public void setParentNode(String parentNodeId) {
		this.parentNodeId = parentNodeId;
	}

	/**
	 * @return the argumentListener
	 */
	public INavigationArgumentListener getArgumentListener() {
		return argumentListener;
	}

	/**
	 * @param argumentListener
	 *            the argumentListener to set
	 */
	public void setArgumentListener(INavigationArgumentListener argumentListener) {
		this.argumentListener = argumentListener;
	}

}
