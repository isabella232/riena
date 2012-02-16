/*******************************************************************************
 * Copyright (c) 2009 Florian Pirchner and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Florian Pirchner - initial implementation
 * compeople AG     - created new example based on SnipetLinkRidget001
 *******************************************************************************/
package org.eclipse.riena.sample.snippets;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.ui.ridgets.IBrowserRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Showing an {@link IBrowserRidget} with it's URL property bound against a text
 * ridget.
 */
public final class SnippetBrowserRidget001 {

	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		try {
			final Shell shell = UIControlsFactory.createShell(display);
			shell.setText(SnippetBrowserRidget001.class.getSimpleName());
			GridLayoutFactory.fillDefaults().numColumns(1).margins(10, 10).equalWidth(false).spacing(20, 10)
					.applyTo(shell);

			final Text text = UIControlsFactory.createText(shell, SWT.SINGLE | SWT.BORDER);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(text);

			final Browser browser = new Browser(shell, SWT.BORDER);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(browser);

			final IBrowserRidget browserRidget = (IBrowserRidget) SwtRidgetFactory.createRidget(browser);

			final ITextRidget textRidget = (ITextRidget) SwtRidgetFactory.createRidget(text);
			textRidget.setDirectWriting(false);
			textRidget.bindToModel(browserRidget, IBrowserRidget.PROPERTY_URL);
			textRidget.setText("http://www.eclipse.org/"); //$NON-NLS-1$

			browserRidget.addPropertyChangeListener(IBrowserRidget.PROPERTY_URL, new PropertyChangeListener() {
				public void propertyChange(final PropertyChangeEvent evt) {
					textRidget.setText((String) evt.getNewValue());
				}
			});

			shell.setSize(500, 500);
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
