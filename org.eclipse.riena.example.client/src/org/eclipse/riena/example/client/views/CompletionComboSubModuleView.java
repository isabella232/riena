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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.CompletionCombo;
import org.eclipse.riena.ui.swt.CompletionCombo.ComboAutoCompletionMode;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Example for the CompletionCombo widget (combo with autocompletion as you
 * type).
 */
public class CompletionComboSubModuleView extends SubModuleView {

	public static final String ID = CompletionComboSubModuleView.class.getName();

	private static String[] DATA = new String[] { "Aachen", "Athens", "Austin", "Arkansas", "Ashland", "London", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			"Moskow", "New York", "Paris", "Portland", "Potzdam" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

	private CompletionCombo combo1;

	@Override
	protected void basicCreatePartControl(Composite parent) {
		GridLayoutFactory.swtDefaults().numColumns(1).applyTo(parent);

		GridDataFactory gdfFill = GridDataFactory.fillDefaults().grab(true, false);

		Group grpCustom = createGroup(parent, "CompletionCombo (custom widget)"); //$NON-NLS-1$
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

	private void createCustom(Composite parent) {
		UIControlsFactory.createLabel(parent, "autocomplete, allow missmatch"); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, "autocomplete, don't allow missmatch"); //$NON-NLS-1$

		combo1 = new CompletionCombo(parent, SWT.BORDER);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.FILL, SWT.CENTER).applyTo(combo1);
		combo1.setItems(DATA);
		combo1.setAutoCompletion(true, ComboAutoCompletionMode.ALLOW_MISSMATCH);

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
