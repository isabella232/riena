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

import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * 'Hello World' using an {@link ILabelRidget}.
 */
public final class SnippetHelloWorld {

	public static void main(String[] args) {
		Display display = Display.getDefault();
		try {
			Shell shell = new Shell();
			GridLayoutFactory.fillDefaults().applyTo(shell);

			Label label = new Label(shell, SWT.BORDER);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(label);

			ILabelRidget ridget = (ILabelRidget) SwtRidgetFactory.createRidget(label);
			ridget.bindToModel(PojoObservables.observeValue(new HelloModel(), "text"));
			ridget.updateFromModel();

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

	private static final class HelloModel {
		private String text = "Hello Riena !";

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}
	}
}
