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

		result = new MessageDialog(parent.getShell(), title, null, // accept the default window icon
				text, type, buttonLabels, 0).open(); // OK is the default
	}

	/**
	 * @return the result
	 */
	public int getResult() {
		return result;
	}
}
