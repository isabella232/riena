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
package org.eclipse.riena.ui.ridgets.swt;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.riena.ui.swt.utils.IPropertyNameProvider;
import org.eclipse.swt.widgets.Control;

/**
 * The <code>MessageBox</code> pops up a standard dialog box to display
 * messages.
 */
public class MessageBox implements IPropertyNameProvider {

	private String propertyName;
	private Control parent;
	private int result;
	private MessageDialog messageDialog;

	/*
	 * @see
	 * org.eclipse.riena.ui.swt.utils.IPropertyNameProvider#getPropertyName()
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/*
	 * @param propertyName the propertyName to set
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public MessageBox(Control parent) {

		super();

		this.parent = parent;
	}

	public void show(String title, String text, int type, String[] buttonLabels) {

		messageDialog = new MessageDialog(parent.getShell(), title, null, // accept the default window icon
				text, type, buttonLabels, 0) {
			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.dialogs.Dialog#close()
			 */
			@Override
			public boolean close() {
				boolean closed = super.close();
				messageDialog = null;

				return closed;
			}
		};

		result = messageDialog.open();
	}

	/**
	 * @return the result
	 */
	public int getResult() {
		return result;
	}

	public void requestFocus() {
		if (messageDialog != null) {
			messageDialog.getShell().setFocus();
		}
	}

	public boolean hasFocus() {
		if (messageDialog != null) {
			return messageDialog.getShell().isFocusControl();
		}
		return false;
	}

	public boolean isFocusable() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setFocusable(boolean focusable) {
		// TODO Auto-generated method stub
	}

	public void setVisible(boolean visible) {
		if (messageDialog != null) {
			messageDialog.getShell().setVisible(visible);
		}
	}
}
