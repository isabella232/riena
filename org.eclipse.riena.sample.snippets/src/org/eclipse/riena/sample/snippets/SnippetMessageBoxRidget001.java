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
package org.eclipse.riena.sample.snippets;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.IMessageBoxRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.MessageBox;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * 'Hello World' using an {@link ILabelRidget}.
 */
public final class SnippetMessageBoxRidget001 {

	public static void main(String[] args) {
		Display display = Display.getDefault();
		try {
			Shell shell = new Shell();
			GridLayoutFactory.fillDefaults().applyTo(shell);

			MessageBox messageBox = new MessageBox(shell);
			final IMessageBoxRidget messageBoxRidget = (IMessageBoxRidget) SwtRidgetFactory.createRidget(messageBox);
			// Configure MessageBoxRidget:
			messageBoxRidget.setType(IMessageBoxRidget.Type.QUESTION);
			messageBoxRidget.setTitle("Bridgekeeper"); //$NON-NLS-1$
			messageBoxRidget.setText("What is your favourite colour?"); //$NON-NLS-1$
			// Set options
			IMessageBoxRidget.MessageBoxOption[] customOptions = new IMessageBoxRidget.MessageBoxOption[] {
					new IMessageBoxRidget.MessageBoxOption("Blue"), new IMessageBoxRidget.MessageBoxOption("Yellow") }; //$NON-NLS-1$//$NON-NLS-2$
			messageBoxRidget.setOptions(customOptions);

			Button button = new Button(shell, SWT.BORDER);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(button);

			IActionRidget actionRidget = (IActionRidget) SwtRidgetFactory.createRidget(button);
			actionRidget.setText("Show message box"); //$NON-NLS-1$
			actionRidget.addListener(new IActionListener() {
				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.riena.ui.ridgets.IActionListener#callback()
				 */
				public void callback() {
					messageBoxRidget.show();
				}
			});

			shell.pack();
			shell.open();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} finally {
			display.dispose();
		}
	}
}
