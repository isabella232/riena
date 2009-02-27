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

	public static final String CONTEXT_KEY_PARAMETER = "riena.navigation.parameter"; //$NON-NLS-1$

	private Object parameter;
	private NavigationNodeId parentNodeId;
	private INavigationArgumentListener argumentListener;

	// TODO see https://bugs.eclipse.org/bugs/show_bug.cgi?id=261832
	//private boolean navigateAsync = false;
	//
	//	/**
	//	 * @return the navigateAsync
	//	 */
	//	public boolean isNavigateAsync() {
	//		return navigateAsync;
	//	}
	//
	//	/**
	//	 * @param navigateAsync
	//	 *            the navigateAsync to set
	//	 */
	//	public void setNavigateAsync(boolean navigateAsync) {
	//		this.navigateAsync = navigateAsync;
	//	}

	/**
	 * @param parameter
	 * @param argumentListener
	 * @param parentNodeId
	 */
	public NavigationArgument(Object argument, INavigationArgumentListener argumentListener,
			NavigationNodeId parentNodeId) {
		super();
		this.parameter = argument;
		this.argumentListener = argumentListener;
		this.parentNodeId = parentNodeId;
	}

	/**
	 */
	public NavigationArgument() {
		super();
	}

	/**
	 * @param parameter
	 */
	public NavigationArgument(Object argument) {
		super();
		this.parameter = argument;
	}

	/**
	 * @param parameter
	 * @param argumentListener
	 */
	public NavigationArgument(Object argument, INavigationArgumentListener argumentListener) {
		super();
		this.parameter = argument;
		this.argumentListener = argumentListener;
	}

	/**
	 * @return the externalParameter
	 */
	public Object getParameter() {
		return parameter;
	}

	/**
	 * @param externalParameter
	 *            the externalParameter to set
	 */
	public void setParameter(Object externalParameter) {
		this.parameter = externalParameter;
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
