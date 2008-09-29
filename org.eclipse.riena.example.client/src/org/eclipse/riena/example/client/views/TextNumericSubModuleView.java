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
package org.eclipse.riena.example.client.views;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.riena.example.client.controllers.TextNumericSubModuleController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * SWT {@link ITextFieldRidget} sample. TODO [ev] docs
 */
public class TextNumericSubModuleView extends SubModuleView<TextNumericSubModuleController> {

	public static final String ID = TextNumericSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {
		// TODO [ev] example for grouping on/off
		// TODO [ev] example for signed on/off
		// TODO [ev] example for primitive types
		// TODO [ev] example for BigInteger
		parent.setBackground(LnfManager.getLnf().getColor(ILnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(3, true));

		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, "Text Field"); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, "Model Value"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "String:"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createText(parent), "txtString"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createLabel(parent, ""), "lblString"); //$NON-NLS-1$//$NON-NLS-2$

		UIControlsFactory.createLabel(parent, "Double:"); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "Float:"); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "Long:"); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "Integer:"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextNumeric(parent), "txtInteger"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createLabel(parent, ""), "lblInteger"); //$NON-NLS-1$//$NON-NLS-2$

		UIControlsFactory.createLabel(parent, "Range[100,1000]:"); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "With leading Zero:"); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "Unformatted, max. 8 digits:"); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "Min. 3 digits:"); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "Bank Code:"); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$

		GridDataFactory gdf = GridDataFactory.fillDefaults();
		for (Control child : parent.getChildren()) {
			if (child instanceof Text) {
				gdf.applyTo(child);
			}
		}
	}

}
