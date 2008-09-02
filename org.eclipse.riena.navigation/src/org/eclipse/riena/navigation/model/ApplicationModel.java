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

import org.eclipse.riena.navigation.IApplicationModel;
import org.eclipse.riena.navigation.INavigationHistoryListener;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.listener.IApplicationModelListener;

/**
 * Default implementation for the ApplicationModel
 */
public class ApplicationModel extends NavigationNode<IApplicationModel, ISubApplicationNode, IApplicationModelListener>
		implements IApplicationModel {

	/**
	 * Creates an ApplicationModel node which is the root of an application
	 * model tree.
	 * 
	 */
	public ApplicationModel() {
		super(null);
		initializeNavigationProcessor();
	}

	/**
	 * Creates an ApplicationModel node which is the root of an application
	 * model tree.
	 * 
	 * @param nodeId
	 *            Identifies the node in the application model tree.
	 */
	public ApplicationModel(NavigationNodeId nodeId) {
		super(nodeId);
		initializeNavigationProcessor();
	}

	/**
	 * Creates an ApplicationModel node which is the root of an application
	 * model tree.
	 * 
	 * @param nodeId
	 *            Identifies the node in the application model tree.
	 * @param label
	 *            Label of the application displayed in the title bar.
	 */
	public ApplicationModel(NavigationNodeId nodeId, String label) {
		this(nodeId);
		setLabel(label);
	}

	/**
	 * Creates an ApplicationModel node which is the root of an application
	 * model tree.
	 * 
	 * @param label
	 *            Label of the application displayed in the title bar.
	 */
	public ApplicationModel(String label) {
		this(null, label);
	}

	/**
	 * 
	 */
	protected void initializeNavigationProcessor() {
		setNavigationProcessor(new NavigationProcessor());
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationHistoryListenerable#
	 *      addNavigationHistoryListener
	 *      (org.eclipse.riena.navigation.INavigationHistoryListener)
	 */
	public void addNavigationHistoryListener(INavigationHistoryListener listener) {
		getNavigationProcessor().addNavigationHistoryListener(listener);
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationHistoryListenerable#
	 *      removeNavigationHistoryListener
	 *      (org.eclipse.riena.navigation.INavigationHistoryListener)
	 */
	public void removeNavigationHistoryListener(INavigationHistoryListener listener) {
		getNavigationProcessor().removeNavigationHistoryListener(listener);
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationHistoryListenerable#getHistoryBackSize()
	 */
	public int getHistoryBackSize() {
		return getNavigationProcessor().getHistoryBackSize();
	}

	/**
	 * @see org.eclipse.riena.navigation.INavigationHistoryListenerable#getHistoryForwardSize()
	 */
	public int getHistoryForwardSize() {
		return getNavigationProcessor().getHistoryForwardSize();
	}
}
