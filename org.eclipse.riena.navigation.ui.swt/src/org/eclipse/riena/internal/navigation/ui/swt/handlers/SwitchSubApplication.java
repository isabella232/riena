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
package org.eclipse.riena.internal.navigation.ui.swt.handlers;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;

import org.eclipse.riena.navigation.ApplicationNodeManager;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.ISubApplicationNode;

/**
 * Switch focus to the previous or next sub application tab. The direction of
 * the movement (forward / backward) is determined by appending the strings
 * ':next' or ':previous' to the handler declaration.
 * <p>
 * Example:
 * 
 * <pre>
 *   &lt;command
 *     id=&quot;org.eclipse.riena.navigation.ui.nextSubApplication&quot;
 *     name=&quot;Next sub-application&quot;
 *     categoryId=&quot;org.eclipse.riena.navigation.ui.swt&quot;
 *     defaultHandler=&quot;org.eclipse.riena.internal.navigation.ui.swt.handlers.SwitchSubApplication:next&quot;/&gt;
 * </pre>
 * 
 * Implementation notes: if the last tab is reached, the next tab will be the
 * first tab. If the first tab is reached is reached, the previous tab will be
 * the last tab.
 */
public class SwitchSubApplication extends AbstractHandler implements IExecutableExtension {

	private boolean toNext;

	public Object execute(ExecutionEvent event) throws ExecutionException {
		// assumes there is only one application node
		IApplicationNode applicationNode = ApplicationNodeManager.getApplicationNode();
		List<ISubApplicationNode> children = applicationNode.getChildren();
		ISubApplicationNode[] nodes = children.toArray(new ISubApplicationNode[children.size()]);
		ISubApplicationNode nextNode = toNext ? findNextNode(nodes) : findPreviousNode(nodes);
		if (nextNode != null) {
			nextNode.activate();
		}
		return null;
	}

	/**
	 * This method is called by the framework. Not intented to be called by
	 * clients.
	 */
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
			throws CoreException {
		toNext = "next".equalsIgnoreCase(String.valueOf(data)); //$NON-NLS-1$
	}

	// helping methods
	//////////////////

	/**
	 * This is NOT API - public for testing only.
	 */
	public ISubApplicationNode findNextNode(ISubApplicationNode[] nodes) {
		ISubApplicationNode result = null;
		int selectedCount = 0;
		for (ISubApplicationNode node : nodes) {
			if (node.isSelected()) {
				selectedCount++;
			} else {
				if (selectedCount == 1 && result == null) { // previous else implies !selected
					result = node;
				}
			}
		}
		if (selectedCount == 1 && result == null && !nodes[0].isSelected()) { // wrap around
			result = nodes[0];
		}
		return selectedCount == 1 ? result : null;
	}

	/**
	 * This is NOT API - public for testing only.
	 */
	public ISubApplicationNode findPreviousNode(ISubApplicationNode[] nodes) {
		ISubApplicationNode result = null;
		int selectedCount = 0;
		for (int i = nodes.length - 1; i >= 0; i--) {
			ISubApplicationNode node = nodes[i];
			if (node.isSelected()) {
				selectedCount++;
			} else {
				if (selectedCount == 1 && result == null) { // previous else implies !selected
					result = node;
				}
			}
		}
		int lastApp = nodes.length - 1;
		if (selectedCount == 1 && result == null && !nodes[lastApp].isSelected()) {
			result = nodes[lastApp];
		}
		return selectedCount == 1 ? result : null;
	}

}
