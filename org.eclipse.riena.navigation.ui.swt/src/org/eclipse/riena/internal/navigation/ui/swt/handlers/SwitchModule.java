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

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;

import org.eclipse.riena.navigation.ApplicationNodeManager;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.INavigationNode;

/**
 * Switch focus to the previous or next group in the navigation tree. The
 * direction of the movement (forward / backward) is determined by appending the
 * strings ':next' or ':previous' to the handler declaration.
 * <p>
 * Example:
 * 
 * <pre>
 *   &lt;command
 *     id=&quot;org.eclipse.riena.navigation.ui.nextGroup&quot;
 *     name=&quot;Next group&quot;
 *     categoryId=&quot;org.eclipse.riena.navigation.ui.swt&quot;
 *     defaultHandler=&quot;org.eclipse.riena.internal.navigation.ui.swt.handlers.SwitchModule:next&quot;/&gt;
 * </pre>
 * 
 * Implementation notes: if the last group is reached, the next group will be
 * the first group. If the first group is reached is reached, the previous group
 * will be the last group.
 */
public class SwitchModule extends AbstractNavigationHandler implements IExecutableExtension {

	private boolean toNext;

	public Object execute(ExecutionEvent event) throws ExecutionException {
		doSwitch(toNext);
		return null;
	}

	public void doSwitch(boolean forward) {
		// assumes there is only one application node
		IApplicationNode application = ApplicationNodeManager.getApplicationNode();
		INavigationNode<?>[] subModules = collectModules(application);
		INavigationNode<?> nextNode = forward ? findNextNode(subModules) : findPreviousNode(subModules, true);
		if (nextNode != null) {
			nextNode.activate();
		}
	}

	/**
	 * This method is called by the framework. Not intented to be called by
	 * clients.
	 */
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
			throws CoreException {
		toNext = "next".equalsIgnoreCase(String.valueOf(data)); //$NON-NLS-1$
	}

}
