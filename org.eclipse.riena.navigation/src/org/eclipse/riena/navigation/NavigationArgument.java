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

	/**
	 * this key is used in INavigationNode.getContext to address THIS (the
	 * NavigationArgument)
	 */
	public static final String CONTEXTKEY_ARGUMENT = "riena.navigation.argument"; //$NON-NLS-1$

	public static final String CONTEXTKEY_PARAMETER = "riena.navigation.parameter"; //$NON-NLS-1$

	private Object parameter;
	private NavigationNodeId parentNodeId;
	private IUpdateListener updateListener = null;
	private String ridgetId;

	/**
	 */
	public NavigationArgument() {
		super();
	}

	/**
	 * @param parameter
	 */
	public NavigationArgument(Object parameter) {
		super();
		this.parameter = parameter;
	}

	/**
	 * @param parameter
	 * @param ridgetId
	 *            requestFocus on a ridget
	 * @since 1.2
	 */
	public NavigationArgument(Object parameter, String ridgetId) {
		this(parameter);
		this.ridgetId = ridgetId;
	}

	/**
	 * @param parameter
	 * @param argumentListener
	 * @param parentNodeId
	 */
	public NavigationArgument(Object parameter, NavigationNodeId parentNodeId) {
		super();
		this.parameter = parameter;
		this.parentNodeId = parentNodeId;
	}

	/**
	 * @param parameter
	 * @param argumentListener
	 * @param parentNodeId
	 * @since 1.2
	 */
	public NavigationArgument(Object parameter, NavigationNodeId parentNodeId, String ridgetId) {
		super();
		this.parameter = parameter;
		this.parentNodeId = parentNodeId;
		this.ridgetId = ridgetId;
	}

	/**
	 * @param parameter
	 * @param updateListener
	 * @param parentNodeId
	 */
	public NavigationArgument(Object parameter, IUpdateListener updateListener, NavigationNodeId parentNodeId) {
		super();
		this.parameter = parameter;
		this.parentNodeId = parentNodeId;
		this.updateListener = updateListener;
	}

	/**
	 * @return
	 * @since 1.2
	 */
	public String getRidgetId() {
		return ridgetId;
	}

	/**
	 * @return the externalParameter
	 */
	public Object getParameter() {
		return parameter;
	}

	/**
	 * @return the parentNodeId
	 */
	public NavigationNodeId getParentNodeId() {
		return parentNodeId;
	}

	public IUpdateListener getUpdateListener() {
		return updateListener;
	}

	/**
	 * Value was changed, notify list
	 */
	public void fireValueChanged(Object parameter) {
		if (updateListener == null) {
			return;
		}
		updateListener.handleUpdate(parameter);
	}
}
