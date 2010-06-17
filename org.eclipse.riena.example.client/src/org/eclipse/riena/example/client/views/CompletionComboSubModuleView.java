/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.example.client.views;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.CompletionCombo;
import org.eclipse.riena.ui.swt.CompletionCombo.AutoCompletionMode;
import org.eclipse.riena.ui.swt.CompletionCombo2;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Example for the CompletionCombo widget (combo with autocompletion as you
 * type).
 */
public class CompletionComboSubModuleView extends SubModuleView {
	public CompletionComboSubModuleView() {
	}

	public static final String ID = CompletionComboSubModuleView.class.getName();

	private CompletionCombo combo1;

	@Override
	protected void basicCreatePartControl(final Composite parent) {
		GridLayoutFactory.swtDefaults().numColumns(1).applyTo(parent);

		final GridDataFactory gdfFill = GridDataFactory.fillDefaults().grab(true, false);

		final Group grpCustom = createGroup(parent, "CompletionCombo (custom widget)"); //$NON-NLS-1$
		GridLayoutFactory.swtDefaults().numColumns(4).spacing(15, 5).equalWidth(false).margins(20, 20)
				.applyTo(grpCustom);
		gdfFill.applyTo(grpCustom);
		createCustom(grpCustom);
	}

	@Override
	public void setFocus() {
		combo1.forceFocus();
	}

	// helping methods
	//////////////////

	private void createCustom(final Composite parent) {
		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, "CompletionCombo"); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, "Selection"); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, "Text"); //$NON-NLS-1$

		final GridDataFactory grabFill = GridDataFactory.fillDefaults().grab(true, false);
		final GridDataFactory fill = GridDataFactory.fillDefaults();

		// row 1

		UIControlsFactory.createLabel(parent, "autocomplete, allow missmatch:"); //$NON-NLS-1$
		combo1 = UIControlsFactory.createCompletionCombo(parent, "combo1"); //$NON-NLS-1$
		grabFill.applyTo(combo1);
		combo1.setAutoCompletionMode(AutoCompletionMode.ALLOW_MISSMATCH);

		final Text selection1 = UIControlsFactory.createText(parent, SWT.BORDER, "selection1"); //$NON-NLS-1$ 
		grabFill.applyTo(selection1);

		UIControlsFactory.createText(parent, SWT.BORDER, "text1"); //$NON-NLS-1$ 

		// row 2

		UIControlsFactory.createLabel(parent, "autocomplete, no missmatch:"); //$NON-NLS-1$
		final CompletionCombo combo2 = UIControlsFactory.createCompletionCombo(parent, "combo2"); //$NON-NLS-1$
		fill.applyTo(combo2);

		final Text selection2 = UIControlsFactory.createText(parent, SWT.BORDER, "selection2"); //$NON-NLS-1$ 
		fill.applyTo(selection2);

		UIControlsFactory.createText(parent, SWT.BORDER, "text2"); //$NON-NLS-1$

		// row 3

		UIControlsFactory.createLabel(parent, "XXX experimental XXX"); //$NON-NLS-1$
		final CompletionCombo2 combo3 = new CompletionCombo2(parent, SWT.BORDER);
		combo3.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		addUIControl(combo3, "combo3"); //$NON-NLS-1$
		fill.applyTo(combo3);

		final Text selection3 = UIControlsFactory.createText(parent, SWT.BORDER, "selection3"); //$NON-NLS-1$ 
		fill.applyTo(selection3);

		UIControlsFactory.createText(parent, SWT.BORDER, "text3"); //$NON-NLS-1$
	}

	private Group createGroup(final Composite parent, final String title) {
		final Group result = UIControlsFactory.createGroup(parent, "Combo boxes"); //$NON-NLS-1$
		GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(true).margins(20, 20).applyTo(result);
		result.setText(title);
		return result;
	}
}
