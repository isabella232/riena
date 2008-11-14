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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.riena.ui.ridgets.AbstractRidget;
import org.eclipse.riena.ui.ridgets.IMessageBoxRidget;
import org.eclipse.riena.ui.ridgets.swt.MessageBox;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * The ridget for a message box.
 */
public class MessageBoxRidget extends AbstractRidget implements IMessageBoxRidget {

	private MessageBox messageBox;
	private String title;
	private String text;
	private boolean blocked;
	private boolean visible;
	private boolean focusable;
	private FocusListener focusManager;
	private Type type = Type.PLAIN;
	private MessageBoxOption[] options = OPTIONS_OK;

	/**
	 * Default constructor.
	 */
	public MessageBoxRidget() {

		super();

		focusable = true;
		focusManager = new FocusManager();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IMessageBoxRidget#getOptions()
	 */
	public MessageBoxOption[] getOptions() {
		return options;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IMessageBoxRidget#getText()
	 */
	public String getText() {
		return text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IMessageBoxRidget#getTitle()
	 */
	public String getTitle() {
		return title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IMessageBoxRidget#getType()
	 */
	public Type getType() {
		return type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.ui.ridgets.IMessageBoxRidget#setOptions(org.eclipse
	 * .riena.ui.ridgets.IMessageBoxRidget.MessageBoxOption[])
	 */
	public void setOptions(MessageBoxOption[] options) {
		this.options = options;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.ui.ridgets.IMessageBoxRidget#setText(java.lang.String)
	 */
	public void setText(String text) {
		this.text = text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.ui.ridgets.IMessageBoxRidget#setTitle(java.lang.String)
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.ui.ridgets.IMessageBoxRidget#setType(org.eclipse.riena
	 * .ui.ridgets.IMessageBoxRidget.Type)
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IMessageBoxRidget#show()
	 */
	public MessageBoxOption show() {

		if (messageBox != null) {
			if (type == null) {
				type = Type.PLAIN;
			}
			return show(type);
		} else {
			return null;
		}
	}

	private MessageBoxOption show(Type type) {

		messageBox.show(getTitle(), getText(), getType(type), getButtonLabels(type));

		return getResultOption();
	}

	private String[] getButtonLabels(Type type) {

		String[] labels = new String[options.length];
		for (int i = 0; i < options.length; i++) {
			labels[i] = getButtonLabel(options[i]);
		}

		return labels;
	}

	private String getButtonLabel(MessageBoxOption option) {

		if (OK.equals(option)) {
			return IDialogConstants.OK_LABEL;
		} else if (CANCEL.equals(option)) {
			return IDialogConstants.CANCEL_LABEL;
		} else if (YES.equals(option)) {
			return IDialogConstants.YES_LABEL;
		} else if (NO.equals(option)) {
			return IDialogConstants.NO_LABEL;
		} else {
			return option.getLabel();
		}
	}

	private MessageBoxOption getResultOption() {

		int result = messageBox.getResult();

		if (result == SWT.DEFAULT) {
			return CLOSED;
		} else {
			return getOptions()[result];
		}
	}

	private int getType(Type type) {

		switch (type) {
		case PLAIN:
			return MessageDialog.NONE;
		case INFORMATION:
			return MessageDialog.INFORMATION;
		case WARNING:
			return MessageDialog.WARNING;
		case ERROR:
			return MessageDialog.ERROR;
		case HELP:
			return MessageDialog.INFORMATION;
		case QUESTION:
			return MessageDialog.QUESTION;
		default:
			return MessageDialog.NONE;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IRidget#getID()
	 */
	public String getID() {
		if (getUIControl() != null) {
			return ((MessageBox) getUIControl()).getPropertyName();
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IRidget#getToolTipText()
	 */
	public String getToolTipText() {
		// not supported
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IRidget#getUIControl()
	 */
	public MessageBox getUIControl() {
		return messageBox;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IRidget#hasFocus()
	 */
	public boolean hasFocus() {
		return messageBox.hasFocus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IRidget#isBlocked()
	 */
	public boolean isBlocked() {
		return blocked;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IRidget#isFocusable()
	 */
	public boolean isFocusable() {
		return focusable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IRidget#isVisible()
	 */
	public boolean isVisible() {
		return messageBox != null && visible;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IRidget#requestFocus()
	 */
	public void requestFocus() {
		if (isFocusable()) {
			messageBox.requestFocus();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IRidget#setBlocked(boolean)
	 */
	public void setBlocked(boolean blocked) {

		this.blocked = blocked;
		setFocusable(!blocked);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IRidget#setFocusable(boolean)
	 */
	public void setFocusable(boolean focusable) {
		this.focusable = focusable;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.ui.ridgets.IRidget#setToolTipText(java.lang.String)
	 */
	public void setToolTipText(String toolTipText) {
		// not supported
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IRidget#setUIControl(java.lang.Object)
	 */
	public void setUIControl(Object uiControl) {
		assertUIControlType(uiControl, MessageBox.class);

		uninstallListeners();
		messageBox = (MessageBox) uiControl;
		installListeners();
		updateUIControl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IRidget#setVisible(boolean)
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
		updateUIControl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IRidget#updateFromModel()
	 */
	public void updateFromModel() {
		// do nothing
	}

	private void updateUIControl() {
		if (messageBox != null) {
			updateVisible();
		}
	}

	private void updateVisible() {
		messageBox.setVisible(visible);
	}

	/*
	 * Checks the type of the UI-control. If the test fails, some kind of
	 * unchecked exception is thrown.
	 * 
	 * @param uiControl The UI-control to be checked.
	 * 
	 * @param requiredUIControlType The required type.
	 */
	protected void assertUIControlType(Object uiControl, Class requiredUIControlType) {
		Assert.isTrue(uiControl == null || requiredUIControlType.isAssignableFrom(uiControl.getClass()),
				"Wrong UI-control type. Expected " + requiredUIControlType); //$NON-NLS-1$
	}

	/**
	 * Adds listeners to the <tt>uiControl</tt> after it was bound to the
	 * ridget.
	 */
	protected final void installListeners() {
		if (getUIControl() != null) {
			getUIControl().addFocusListener(focusManager);
		}
	}

	/**
	 * Removes listeners from the <tt>uiControl</tt> when it is about to be
	 * unbound from the ridget.
	 */
	protected final void uninstallListeners() {
		if (getUIControl() != null) {
			getUIControl().removeFocusListener(focusManager);
		}
	}

	/**
	 * Focus listener that also prevents the widget corresponding to this ridget
	 * from getting the UI focus when the ridget is not focusable.
	 * 
	 * @see MessageBoxRidget#setFocusable(boolean).
	 */
	private final class FocusManager extends FocusAdapter {

		@Override
		public void focusGained(FocusEvent e) {
			if (focusable) {
				fireFocusGained(new org.eclipse.riena.ui.ridgets.listener.FocusEvent(null, MessageBoxRidget.this));
			} else {
				Control control = (Control) e.widget;
				Composite parent = control.getParent();
				Control[] tabList = parent.getTabList();
				int i = findNextElement(control, tabList);
				if (i != -1) {
					Control nextFocusControl = tabList[i];
					nextFocusControl.setFocus();
				} else { // no suitable control found, try one level up
					Composite pParent = parent.getParent();
					if (pParent != null) {
						tabList = pParent.getTabList();
						i = findNextElement(parent, tabList);
						if (i != -1) {
							Control nextFocusControl = tabList[i];
							nextFocusControl.setFocus();
						}
					}
				}
			}
		}

		@Override
		public void focusLost(FocusEvent e) {
			if (focusable) {
				fireFocusLost(new org.eclipse.riena.ui.ridgets.listener.FocusEvent(MessageBoxRidget.this, null));
			}
		}

		private int findNextElement(Control control, Control[] controls) {
			int myIndex = -1;
			// find index for control
			for (int i = 0; myIndex == -1 && i < controls.length; i++) {
				if (controls[i] == control) {
					myIndex = i;
				}
			}
			// find next possible control
			int result = -1;
			for (int i = myIndex + 1; result == -1 && i < controls.length; i++) {
				Control candidate = controls[i];
				if (candidate.isEnabled() && candidate.isVisible()) {
					result = i;
				}
			}
			// find previous possible control
			for (int i = 0; result == -1 && i < myIndex; i++) {
				Control candidate = controls[i];
				if (candidate.isEnabled() && candidate.isVisible()) {
					result = i;
				}
			}
			return result;
		}
	};
}
