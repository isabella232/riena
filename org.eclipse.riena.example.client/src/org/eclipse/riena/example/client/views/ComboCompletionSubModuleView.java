/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
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
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import org.eclipse.riena.example.client.views.CompletionCombo.ComboAutoCompletionMode;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * TODO [ev] javadoc
 */
public class ComboCompletionSubModuleView extends SubModuleView {

	public static final String ID = ComboCompletionSubModuleView.class.getName();

	private static String[] DATA = new String[] { "Aachen", "Athens", "Austin", "Arkansas", "Ashland", "London", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			"Moskow", "New York", "Paris", "Portland", "Potzdam" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

	private Combo combo1;

	@Override
	protected void basicCreatePartControl(Composite parent) {
		GridLayoutFactory.swtDefaults().numColumns(1).applyTo(parent);

		GridDataFactory gdfFill = GridDataFactory.fillDefaults().grab(true, false);

		Group grpCombo = createGroup(parent, "Combo Widgets"); //$NON-NLS-1$
		gdfFill.applyTo(grpCombo);
		createCombos(grpCombo);

		Group grpCCombo = createGroup(parent, "CCombo Widgets"); //$NON-NLS-1$
		gdfFill.applyTo(grpCCombo);
		createCCombos(grpCCombo);

		Group grpCustom = createGroup(parent, "Custom Widget"); //$NON-NLS-1$
		GridLayoutFactory.swtDefaults().numColumns(2).equalWidth(true).applyTo(grpCustom);
		gdfFill.applyTo(grpCustom);
		createCustom(grpCustom);
	}

	@Override
	public void setFocus() {
		combo1.forceFocus();
	}

	// helping methods
	//////////////////

	private void createCombos(Composite parent) {
		UIControlsFactory.createLabel(parent, "SWT.READ_ONLY - type first letter for completion"); //$NON-NLS-1$
		combo1 = new Combo(parent, SWT.READ_ONLY);

		UIControlsFactory.createLabel(parent, "SWT.NONE - matching prefix = autocomplete, ENTER = add"); //$NON-NLS-1$
		Combo combo2 = new Combo(parent, SWT.NONE);
		new ComboAutocompleter(combo2);

		UIControlsFactory.createLabel(parent, "SWT.SIMPLE - matching prefix = autocomplete, ENTER = add"); //$NON-NLS-1$
		Combo combo3 = new Combo(parent, SWT.SIMPLE | SWT.V_SCROLL);
		new ComboAutocompleter(combo3);

		for (Combo combo : new Combo[] { combo1, combo2, combo3 }) {
			GridDataFactory.fillDefaults().hint(-1, 50).applyTo(combo);
			combo.setItems(DATA);
		}
	}

	private void createCCombos(Composite parent) {
		UIControlsFactory.createLabel(parent, "SWT.READ_ONLY - 1st letter = autocomplete (when list is open!)"); //$NON-NLS-1$
		CCombo cCombo1 = new CCombo(parent, SWT.READ_ONLY | SWT.BORDER);

		UIControlsFactory.createLabel(parent, "SWT.NONE - matching prefix = autocomplete, ENTER = add"); //$NON-NLS-1$
		CCombo cCombo2 = new CCombo(parent, SWT.BORDER);
		new ComboAutocompleter(cCombo2);

		for (CCombo combo : new CCombo[] { cCombo1, cCombo2 }) {
			GridDataFactory.fillDefaults().applyTo(combo);
			combo.setItems(DATA);
		}
	}

	private void createCustom(Composite parent) {
		UIControlsFactory.createLabel(parent, "autocomplete, allow missmatch"); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, "autocomplete, don't allow missmatch"); //$NON-NLS-1$
		CompletionCombo combo = new CompletionCombo(parent, SWT.BORDER);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.CENTER).applyTo(combo);
		combo.setItems(DATA);
		combo.setAutoCompletion(true, ComboAutoCompletionMode.ALLOW_MISSMATCH);
		CompletionCombo combo2 = new CompletionCombo(parent, SWT.BORDER);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.CENTER).applyTo(combo2);
		combo2.setItems(DATA);
		combo2.setAutoCompletion(true);
	}

	private Group createGroup(Composite parent, String title) {
		Group result = UIControlsFactory.createGroup(parent, "Combo boxes"); //$NON-NLS-1$
		GridLayoutFactory.fillDefaults().numColumns(1).equalWidth(true).margins(20, 20).applyTo(result);
		result.setText(title);
		return result;
	}
}
