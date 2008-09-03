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
package org.eclipse.riena.sample.app.client.helloworld.controllers;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.sample.app.client.Activator;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.sample.app.common.model.IHelloWorldService;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;

public class HelloServerSubModuleController extends SubModuleController {

	private IActionRidget actionFacade;
	private ITextFieldRidget textFacade;
	private IActionListener callback;
	private IHelloWorldService service;
	private MessageBean messageBean;

	public HelloServerSubModuleController(ISubModuleNode navigationNode) {
		super(navigationNode);
		startServiceTracker();
		callback = new ActionCallback();
	}

	private void startServiceTracker() {
		Inject.service(IHelloWorldService.class.getName()).into(this).andStart(Activator.getDefault().getContext());
	}

	public void bind(IHelloWorldService service) {
		this.service = service;
	}

	public void unbind(IHelloWorldService service) {
		if (this.service == service) {
			this.service = null;
		}
	}

	@Override
	public void afterBind() {
		super.afterBind();
		intializeControlBindings();
	}

	private void intializeControlBindings() {
		messageBean = new MessageBean();
		textFacade.bindToModel(messageBean, "message"); //$NON-NLS-1$
		textFacade.updateFromModel();
		callback = new ActionCallback();
		actionFacade.addListener(callback);
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#configureRidgets()
	 */
	public void configureRidgets() {
		actionFacade = (IActionRidget) getRidget("actionFacade"); //$NON-NLS-1$
		textFacade = (ITextFieldRidget) getRidget("textFacade"); //$NON-NLS-1$
		callback = (IActionListener) getRidget("callback"); //$NON-NLS-1$
	}

	private final static class MessageBean {

		private String message;

		private PropertyChangeSupport pcSupport;

		MessageBean() {
			pcSupport = new PropertyChangeSupport(this);
		}

		public void addPropertyChangeListener(PropertyChangeListener listener) {
			pcSupport.addPropertyChangeListener(listener);
		}

		public void removePropertyChangeListener(PropertyChangeListener listener) {
			pcSupport.removePropertyChangeListener(listener);
		}

		public void setMessage(String message) {
			String old = this.message;
			this.message = message;
			pcSupport.firePropertyChange("message", old, message); //$NON-NLS-1$
		}

		public String getMessage() {
			return message;
		}
	}

	private class ActionCallback implements IActionListener {

		public void callback() {
			if (service != null) {
				messageBean.setMessage(service.getMessage());
				textFacade.updateFromModel();
			}
		}

	}

}
