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
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.riena.ui.ridgets.controller.IController;
import org.eclipse.riena.ui.ridgets.swt.uibinding.AbstractViewBindingDelegate;
import org.eclipse.riena.ui.ridgets.swt.uibinding.DefaultSwtBindingDelegate;

/**
 * View bound to a controller.
 */
public abstract class AbstractControlledView<C extends IController> {
	private AbstractViewBindingDelegate binding;
	private C controller;

	public C getController() {
		return controller;
	}

	protected void setController(C controller) {

		this.controller = controller;
	}

	public AbstractControlledView() {
		super();
		binding = createBinding();
	}

	/**
	 * @see de.compeople.scp.ui.swing.views.IControlledView#initialize(org.eclipse.riena.ui.ridgets.controller.IController)
	 */
	public void initialize(C controller) {
		binding.injectRidgets(controller);
	}

	/**
	 * Creates a delegate for the binding of view and controller.
	 * 
	 * @return delegate for binding
	 */
	protected AbstractViewBindingDelegate createBinding() {
		return new DefaultSwtBindingDelegate();
	}

	/**
	 * @see de.compeople.scp.ui.swing.views.IControlledView#bind(org.eclipse.riena.ui.ridgets.controller.IController)
	 */
	public void bind(C controller) {

		if (getController() != null) {
			unbind(getController());
		}
		if (controller != null) {
			setController(controller);
			binding.bind(controller);
			controller.afterBind();
		}
	}

	/**
	 * @see de.compeople.scp.ui.swing.views.IControlledView#unbind(org.eclipse.riena.ui.ridgets.controller.IController)
	 */
	public void unbind(C controller) {
		binding.unbind(controller);
		setController(null);
	}

	/**
	 * Adds the given control to the list of the controls that will be bound.
	 * 
	 * @param uiControl
	 *            the control to bind.
	 * @param propertyName
	 *            the name for binding the control.
	 */
	protected void addUIControl(Object uiControl, String propertyName) {
		binding.addUIControl(uiControl, propertyName);
	}
}
