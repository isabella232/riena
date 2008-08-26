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
/**
 *
 */
public class NavigationArgument {
	private Object inputParameter;
	private NavigationNodeId parentNodeId;
	private INavigationArgumentListener argumentListener;
	private boolean navigateAsync = false;

	/**
	 * @return the navigateAsync
	 */
	public boolean isNavigateAsync() {
		return navigateAsync;
	}

	/**
	 * @param navigateAsync
	 *            the navigateAsync to set
	 */
	public void setNavigateAsync(boolean navigateAsync) {
		this.navigateAsync = navigateAsync;
	}

	/**
	 * @param inputParameter
	 * @param argumentListener
	 * @param parentNodeId
	 */
	public NavigationArgument(Object argument, INavigationArgumentListener argumentListener,
			NavigationNodeId parentNodeId) {
		super();
		this.inputParameter = argument;
		this.argumentListener = argumentListener;
		this.parentNodeId = parentNodeId;
	}

	/**
	 */
	public NavigationArgument() {
		super();
	}

	/**
	 * @param inputParameter
	 */
	public NavigationArgument(Object argument) {
		super();
		this.inputParameter = argument;
	}

	/**
	 * @param inputParameter
	 * @param argumentListener
	 */
	public NavigationArgument(Object argument, INavigationArgumentListener argumentListener) {
		super();
		this.inputParameter = argument;
		this.argumentListener = argumentListener;
	}

	/**
	 * @return the externalParameter
	 */
	public Object getInputParameter() {
		return inputParameter;
	}

	/**
	 * @param externalParameter
	 *            the externalParameter to set
	 */
	public void setInputParameter(Object externalParameter) {
		this.inputParameter = externalParameter;
	}

	/**
	 * @return the parentNodeId
	 */
	public NavigationNodeId getParentNodeId() {
		return parentNodeId;
	}

	/**
	 * @param parentNodeId
	 *            the parentNodeId to set
	 */
	public void setParentNode(NavigationNodeId parentNodeId) {
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
