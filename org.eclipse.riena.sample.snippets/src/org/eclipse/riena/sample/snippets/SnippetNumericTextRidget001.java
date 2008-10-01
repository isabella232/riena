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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.ridgets.INumericValueTextFieldRidget;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.ridgets.util.beans.IntegerBean;
import org.eclipse.riena.ui.ridgets.validation.MaxNumberLength;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Demonstrates how to use a {@link INumericValueTextFieldRidget} with a
 * {@link MaxNumberLength} validation rule.
 */
public final class SnippetNumericTextRidget001 {

	public static void main(String[] args) {
		Display display = Display.getDefault();
		try {
			Shell shell = new Shell();
			shell.setBackground(LnfManager.getLnf().getColor(ILnfKeyConstants.SUB_MODULE_BACKGROUND));
			GridLayoutFactory.fillDefaults().numColumns(2).margins(10, 10).spacing(20, 10).applyTo(shell);

			UIControlsFactory.createLabel(shell, "Number:"); //$NON-NLS-1$
			Text txtInput = UIControlsFactory.createTextNumeric(shell);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(txtInput);

			UIControlsFactory.createLabel(shell, "Output:"); //$NON-NLS-1$
			Text txtOutput = UIControlsFactory.createText(shell);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(txtOutput);

			INumericValueTextFieldRidget rInput = (INumericValueTextFieldRidget) SwtRidgetFactory
					.createRidget(txtInput);
			rInput.setDirectWriting(true);
			rInput.addValidationRule(new MaxNumberLength(9), ValidationTime.ON_UI_CONTROL_EDIT);

			ITextFieldRidget rOutput = (ITextFieldRidget) SwtRidgetFactory.createRidget(txtOutput);
			rOutput.setOutputOnly(true);

			DataBindingContext dbc = new DataBindingContext();
			dbc.bindValue(BeansObservables.observeValue(rOutput, ITextFieldRidget.PROPERTY_TEXT), BeansObservables
					.observeValue(rInput, ITextFieldRidget.PROPERTY_TEXT), new UpdateValueStrategy(
					UpdateValueStrategy.POLICY_NEVER), new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));
			rInput.bindToModel(new IntegerBean(12345), IntegerBean.PROP_VALUE);
			rInput.updateFromModel();

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

}
