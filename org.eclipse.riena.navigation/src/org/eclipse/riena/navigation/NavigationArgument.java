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

import java.util.ArrayList;
import java.util.List;

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

	/**
	 * this key is used in INavigationNode.getContext to address the parameter
	 * within the NavigationArgument
	 */
	public static final String CONTEXT_KEY_PARAMETER = "riena.navigation.parameter"; //$NON-NLS-1$
	/**
	 * this key is used in INavigationNode.getContext to address THIS (the
	 * NavigationArgument)
	 */
	public static final String CONTEXT_KEY_ARGUMENT = "riena.navigation.argument"; //$NON-NLS-1$

	private Object parameter;
	private NavigationNodeId parentNodeId;
	private List<INavigationArgumentListener> argumentListeners = null;

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
	public NavigationArgument(Object argument, NavigationNodeId parentNodeId) {
		super();
		this.parameter = argument;
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
	 * @param argumentListener
	 *            the argumentListener to add
	 */
	public void addArgumentListener(INavigationArgumentListener argumentListener) {
		if (argumentListeners == null) {
			argumentListeners = new ArrayList<INavigationArgumentListener>();
		}
		argumentListeners.add(argumentListener);
	}

	/**
	 * @param argumentListener
	 *            the argumentListener to add
	 */
	public void removeArgumentListener(INavigationArgumentListener argumentListener) {
		if (argumentListeners == null) {
			return;
		}
		argumentListeners.remove(argumentListener);
	}

	/**
	 * Navigation Argument was changed, fire change event to all listeners
	 */
	public void fireValueChanged() {
		if (argumentListeners == null) {
			return;
		}
		for (INavigationArgumentListener listeners : argumentListeners) {
			listeners.valueChanged(this);
		}
	}
}
