/*******************************************************************************
 * Copyright (c) 2009 Florian Pirchner and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Florian Pirchner - initial implementation
 * compeople AG     - adjustments for Riena v1.2
 *******************************************************************************/
package org.eclipse.riena.sample.snippets;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.beans.common.StringBean;
import org.eclipse.riena.ui.ridgets.ILinkRidget;
import org.eclipse.riena.ui.ridgets.listener.ISelectionListener;
import org.eclipse.riena.ui.ridgets.listener.SelectionEvent;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Shows how to use {@link ILinkRidget}s.
 */
public final class SnippetLinkRidget001 {

	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		try {
			final Shell shell = UIControlsFactory.createShell(display);
			shell.setText(SnippetLinkRidget001.class.getSimpleName());
			GridLayoutFactory.fillDefaults().numColumns(2).margins(10, 10).equalWidth(false).spacing(20, 10)
					.applyTo(shell);

			/**
			 * Create the controls
			 */

			// Link
			UIControlsFactory.createLabel(shell, "Links:"); //$NON-NLS-1$
			final Link link1 = UIControlsFactory.createLink(shell);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(link1);

			// Link
			UIControlsFactory.createLabel(shell, ""); //$NON-NLS-1$
			final Link link2 = UIControlsFactory.createLink(shell);
			GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.CENTER).applyTo(link2);

			// Link
			UIControlsFactory.createLabel(shell, ""); //$NON-NLS-1$
			final Link link3 = UIControlsFactory.createLink(shell);
			GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.CENTER).applyTo(link2);

			// Browser
			final Label label = UIControlsFactory.createLabel(shell, "Browser:"); //$NON-NLS-1$
			GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP).applyTo(label);
			final Browser browser = new Browser(shell, SWT.BORDER);
			browser.setUrl("about:blank"); //$NON-NLS-1$
			GridDataFactory.fillDefaults().grab(true, false).grab(true, true).applyTo(browser);

			/**
			 * Create the ridgets
			 */

			final ISelectionListener listener = new LinkSelectionListener(browser);

			// Link
			final ILinkRidget linkRidget1 = (ILinkRidget) SwtRidgetFactory.createRidget(link1);
			linkRidget1.setText("<a>http://www.eclipse.org</a>"); //$NON-NLS-1$
			linkRidget1.addSelectionListener(listener);

			// Link
			final ILinkRidget linkRidget2 = (ILinkRidget) SwtRidgetFactory.createRidget(link2);
			linkRidget2
					.setText("Visit <a href=\"http://www.eclipse.org/riena\">Riena</a> or <a href=\"http://wiki.eclipse.org/Riena_Snippets\">Riena Snippets</a>"); //$NON-NLS-1$
			linkRidget2.addSelectionListener(listener);

			// Link (bound)
			final ILinkRidget linkRidget3 = (ILinkRidget) SwtRidgetFactory.createRidget(link3);
			final StringBean textBean = new StringBean("Visit <a href=\"http://www.redview.org\">Redview</a>"); //$NON-NLS-1$
			linkRidget3.bindToModel(textBean, StringBean.PROP_VALUE);
			linkRidget3.updateFromModel();
			linkRidget3.addSelectionListener(listener);

			shell.setSize(700, 700);
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

	// helping classes
	//////////////////

	private static final class LinkSelectionListener implements ISelectionListener {

		private final Browser browser;

		LinkSelectionListener(final Browser browser) {
			Assert.isNotNull(browser);
			this.browser = browser;
		}

		public void ridgetSelected(final SelectionEvent event) {
			final String url = (String) event.getNewSelection().get(0);
			System.out.println("Visiting: " + url); //$NON-NLS-1$
			browser.setUrl(url);
		}
	}
}
