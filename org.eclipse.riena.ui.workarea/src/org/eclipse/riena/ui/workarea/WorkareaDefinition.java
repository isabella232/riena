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
package org.eclipse.riena.ui.workarea;

import org.eclipse.riena.ui.ridgets.controller.IController;

public class WorkareaDefinition implements IWorkareaDefinition {

	private final Class<? extends IController> controllerClass;
	private final Object viewId;
	private final boolean viewShared;
	
	
	public WorkareaDefinition(Object viewId) {
		this(viewId, false);
	}
	
	public WorkareaDefinition(Object viewId, boolean isViewShared) {
		this(null, viewId, isViewShared);
	}
	
	public WorkareaDefinition(Class<? extends IController> controllerClass, Object viewId) {
		this(controllerClass, viewId, false);
	}
	
	public WorkareaDefinition(Class<? extends IController> controllerClass, Object viewId, boolean isViewShared) {
		this.controllerClass = controllerClass;
		this.viewId = viewId;
		this.viewShared = isViewShared;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.riena.ui.workarea.IWorkareaDefinition#getControllerClass()
	 */
	public Class<? extends IController> getControllerClass() {
		return controllerClass;
	}

	
	/* (non-Javadoc)
	 * @see org.eclipse.riena.ui.workarea.IWorkareaDefinition#getControllerClass()
	 */
	public IController createController() throws IllegalAccessException, InstantiationException {
		
		if (getControllerClass() != null) {
			return getControllerClass().newInstance();
		} else {
			return null; 
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.riena.ui.workarea.IWorkareaDefinition#getViewId()
	 */
	public Object getViewId() {
		return viewId;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.riena.ui.workarea.IWorkareaDefinition#isViewShared()
	 */
	public boolean isViewShared() {
		return viewShared;
	}
}
