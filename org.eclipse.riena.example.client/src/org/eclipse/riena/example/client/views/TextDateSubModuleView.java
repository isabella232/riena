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
import org.eclipse.riena.example.client.controllers.TextDateSubModuleController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.ridgets.IDateTextRidget;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * SWT example for {@link IDateTextRidget}.
 */
public class TextDateSubModuleView extends SubModuleView<TextDateSubModuleController> {

	public static final String ID = TextDateSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(ILnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(3, true));

		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, "Text Field"); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, "Model Value"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "dd.MM.yyyy:"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextDate(parent), "indd.MM.yyyy"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextOutput(parent), "outdd.MM.yyyy"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "dd.MM.yy:"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextDate(parent), "indd.MM.yy"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextOutput(parent), "outdd.MM.yy"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "dd.MM:"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextDate(parent), "indd.MM"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextOutput(parent), "outdd.MM"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "MM.yyyy:"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextDate(parent), "inMM.yyyy"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextOutput(parent), "outMM.yyyy"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "yyyy:"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextDate(parent), "inyyyy"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextOutput(parent), "outyyyy"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "HH:mm:ss:"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextDate(parent), "inHH:mm:ss"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextOutput(parent), "outHH:mm:ss"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "HH:mm:"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextDate(parent), "inHH:mm"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextOutput(parent), "outHH:mm"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "dd.MM.yyyy HH:mm:"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextDate(parent), "indd.MM.yyyy_HH:mm"); //$NON-NLS-1$
		addUIControl(UIControlsFactory.createTextOutput(parent), "outdd.MM.yyyy_HH:mm"); //$NON-NLS-1$

		GridDataFactory gdf = GridDataFactory.fillDefaults();
		for (Control child : parent.getChildren()) {
			if (child instanceof Text) {
				gdf.applyTo(child);
			}
		}
	}
}
