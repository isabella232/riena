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

/**
 * The ridget for a message box.
 */
public class MessageBoxRidget extends AbstractRidget implements IMessageBoxRidget {

	private MessageBox messageBox;
	private String title;
	private String text;
	private boolean blocked;
	private boolean visible;
	private Type type = Type.PLAIN;
	private MessageBoxOption[] options = OPTIONS_OK;

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
		updateUIControl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.ui.ridgets.IMessageBoxRidget#setText(java.lang.String)
	 */
	public void setText(String text) {
		this.text = text;
		updateUIControl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.ui.ridgets.IMessageBoxRidget#setTitle(java.lang.String)
	 */
	public void setTitle(String title) {
		this.title = title;
		updateUIControl();
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
		updateUIControl();
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
	public Object getUIControl() {
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
		return messageBox.isFocusable();
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
		messageBox.requestFocus();
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
		messageBox.setFocusable(focusable);
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

		messageBox = (MessageBox) uiControl;

		updateUIControl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IRidget#setVisible(boolean)
	 */
	public void setVisible(boolean visible) {
		// TODO: use hidden marker?
		this.visible = visible;

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
			// TODO: update???
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
}
