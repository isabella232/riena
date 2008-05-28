package org.eclipse.riena.sample.app.client.helloworld.controllers;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.sample.app.client.Activator;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleNodeViewController;
import org.eclipse.riena.sample.app.common.model.IHelloWorldService;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;

public class HelloServerSubModuleController extends SubModuleNodeViewController {

	private IHelloWorldService service;

	private IActionRidget actionFacade;

	private ITextFieldRidget textFacade;

	private IActionListener callback;

	private MessageBean messageBean;

	public HelloServerSubModuleController(ISubModuleNode navigationNode) {
		super(navigationNode);
		startServiceTracker();
		callback = new ActionCallback();
	}

	private void startServiceTracker() {
		Inject.service(IHelloWorldService.class.getName()).into(this).andStart(Activator.getContext());
	}

	public void bind(IHelloWorldService service) {
		this.service = service;
	}

	public void unbind(IHelloWorldService service) {
		if (this.service == service) {
			this.service = null;
		}
	}

	public void setTextFacade(ITextFieldRidget textFacade) {
		this.textFacade = textFacade;
	}

	public ITextFieldRidget getTextFacade() {
		return textFacade;
	}

	public void setActionFacade(IActionRidget actionFacade) {
		this.actionFacade = actionFacade;
	}

	public IActionRidget getActionFacade() {
		return actionFacade;
	}

	@Override
	public void afterBind() {
		super.afterBind();
		intializeControlBindings();
	}

	private void intializeControlBindings() {
		messageBean = new MessageBean();
		textFacade.bindToModel(messageBean, "message");
		textFacade.updateFromModel();
		callback = new ActionCallback();
		actionFacade.addListener(callback);
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
			pcSupport.firePropertyChange("message", old, message);
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
