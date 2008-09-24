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

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.ridgets.validation.MinLength;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Text field ridget with minimum length validation rule and direct writing.
 */
public final class SnippetTextRidget001 {

	public static void main(String[] args) {
		Display display = Display.getDefault();
		try {
			Shell shell = new Shell();
			GridLayoutFactory.fillDefaults().numColumns(2).margins(10, 10).spacing(20, 10).applyTo(shell);

			createLabel(shell, "Input (>5):"); //$NON-NLS-1$
			Text text = new Text(shell, SWT.BORDER);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(text);

			createLabel(shell, "Output:"); //$NON-NLS-1$
			Label label = createLabel(shell, ""); //$NON-NLS-1$
			GridDataFactory.fillDefaults().grab(true, false).applyTo(label);

			ILabelRidget labelRidget = (ILabelRidget) SwtRidgetFactory.createRidget(label);

			ITextFieldRidget textRidget = (ITextFieldRidget) SwtRidgetFactory.createRidget(text);
			textRidget.addValidationRule(new MinLength(5), ValidationTime.ON_UI_CONTROL_EDIT);
			textRidget.setDirectWriting(true);
			textRidget.bindToModel(BeansObservables.observeValue(labelRidget, ILabelRidget.PROPERTY_TEXT));
			textRidget.setText("more"); //$NON-NLS-1$

			shell.setSize(200, 200);
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

	private static Label createLabel(Shell shell, String caption) {
		Label result = new Label(shell, SWT.NONE);
		result.setText(caption);
		return result;
	}
}
