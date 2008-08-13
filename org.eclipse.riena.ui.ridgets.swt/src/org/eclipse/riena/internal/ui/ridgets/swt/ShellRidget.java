/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.ui.ridgets.swt;

import javax.swing.JButton;

import org.eclipse.riena.ui.ridgets.AbstractRidget;
import org.eclipse.riena.ui.ridgets.IWindowRidget;
import org.eclipse.riena.ui.ridgets.UIBindingFailure;
import org.eclipse.riena.ui.ridgets.listener.IWindowRidgetListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 */
public class ShellRidget extends AbstractRidget implements IWindowRidget {

	private Shell shell;
	private String toolTip = null;
	private boolean blocked;
	private boolean closeable;
	private boolean active;

	public ShellRidget() {
		closeable = true;
		active = true;
	}

	public ShellRidget(Shell shell) {
		this();
		setUIControl(shell);
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IRidget#getUIControl()
	 */
	public Shell getUIControl() {
		return shell;
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IRidget#setUIControl(java.lang.Object)
	 */
	public void setUIControl(Object uiControl) {
		if (uiControl != null && !(uiControl instanceof Shell)) {
			throw new UIBindingFailure("uiControl of a ShellRidget must be a Shell but was a " //$NON-NLS-1$
					+ uiControl.getClass().getSimpleName());
		}
		shell = (Shell) uiControl;
		updateToolTip();
	}

	public void addWindowRidgetListener(IWindowRidgetListener listener) {
		// TODO Auto-generated method stub
	}

	public void removeWindowRidgetListener(IWindowRidgetListener listener) {
		// TODO Auto-generated method stub
	}

	public boolean isVisible() {
		return shell.isVisible();
	}

	public void setVisible(boolean visible) {
		shell.setVisible(visible);
	}

	public void setTitle(String title) {
		if (title != null) {
			shell.setText(title);
		}
	}

	public String getTitle() {
		return shell.getText();
	}

	public void setIcon(String iconName) {
		// TODO
		// shell.setImage();
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IWindowRidget#setDefaultButton(JButton)
	 */
	public void setDefaultButton(Object defaultButton) {
		if (defaultButton instanceof Button) {
			shell.setDefaultButton((Button) defaultButton);
		}
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IWindowRidget#getDefaultButton()
	 */
	public Object getDefaultButton() {
		return shell.getDefaultButton();
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IRidget#requestFocus()
	 */
	public void requestFocus() {
		if (getUIControl() != null) {
			getUIControl().setFocus();
		}
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IRidget#hasFocus()
	 */
	public boolean hasFocus() {
		if (getUIControl() != null) {
			return getUIControl().isFocusControl();
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IRidget#isFocusable()
	 */
	public boolean isFocusable() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IRidget#setFocusable(boolean)
	 */
	public void setFocusable(boolean focusable) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IRidget#getToolTipText()
	 */
	public String getToolTipText() {
		return toolTip;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.ui.ridgets.IRidget#setToolTipText(java.lang.String)
	 */
	public void setToolTipText(String toolTipText) {
		toolTip = toolTipText;

	}

	private void updateToolTip() {
		if (shell != null) {
			shell.setToolTipText(toolTip);
		}
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IRidget#isBlocked()
	 */
	public boolean isBlocked() {
		return blocked;
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IRidget#setBlocked(boolean)
	 */
	public void setBlocked(boolean blocked) {
		if (this.blocked != blocked) {
			this.blocked = blocked;

		}

	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IWindowRidget#setActive(boolean)
	 */
	public void setActive(boolean active) {
		if (this.active = active) {
			this.active = active;
			updateActive();
		}
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IWindowRidget#setCloseable(boolean)
	 */
	public void setCloseable(boolean closeable) {
		if (this.closeable != closeable) {
			this.closeable = closeable;
			updateCloseable();
		}
	}

	private void updateCloseable() {
		// TODO
	}

	private void updateActive() {
		shell.setEnabled(active);
	}

}
