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

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.ui.ridgets.ILinkRidget;
import org.eclipse.riena.ui.ridgets.listener.ISelectionListener;
import org.eclipse.riena.ui.ridgets.listener.SelectionEvent;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Shows binding a LinkRidget to an arbitrary data object.
 */
public final class SnippetLinkRidget002 {

	public static void main(final String[] args) {
		final Display display = Display.getDefault();
		try {
			final Shell shell = UIControlsFactory.createShell(display);
			shell.setText(SnippetLinkRidget002.class.getSimpleName());
			GridLayoutFactory.fillDefaults().numColumns(1).margins(10, 10).equalWidth(false).spacing(20, 10)
					.applyTo(shell);

			/**
			 * Create the controls
			 */

			// Link
			final Link link1 = UIControlsFactory.createLink(shell);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(link1);

			// Browser
			final Browser browser = new Browser(shell, SWT.BORDER);
			browser.setUrl("about:blank"); //$NON-NLS-1$
			GridDataFactory.fillDefaults().grab(true, false).grab(true, true).applyTo(browser);

			/**
			 * Create the ridgets
			 */

			// Link
			final ILinkRidget linkRidget1 = (ILinkRidget) SwtRidgetFactory.createRidget(link1);
			final ClickCounter model = new ClickCounter();
			model.setText("Click to visit Riena"); //$NON-NLS-1$
			model.setLink("http://www.eclipse.org/riena/"); //$NON-NLS-1$
			linkRidget1.bindToModel(model, "linkAndText"); //$NON-NLS-1$
			linkRidget1.updateFromModel();

			linkRidget1.addSelectionListener(new ISelectionListener() {
				public void ridgetSelected(final SelectionEvent event) {
					final String url = (String) event.getNewSelection().get(0);
					System.out.println("Visiting: " + url); //$NON-NLS-1$
					browser.setUrl(url);

					model.increaseCount();
					linkRidget1.updateFromModel();
				}
			});

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

	private static final class ClickCounter {
		private int count = 0;
		private String link;
		private String text;

		public void increaseCount() {
			count++;
		}

		public void setLink(final String link) {
			this.link = link;
		}

		public void setText(final String text) {
			this.text = text;
		}

		@SuppressWarnings("unused")
		public String getLinkAndText() {
			return String.format("<a href=\"%s\">%s</a> - %d clicks counted", link, text, count); //$NON-NLS-1$
		}
	}

}
