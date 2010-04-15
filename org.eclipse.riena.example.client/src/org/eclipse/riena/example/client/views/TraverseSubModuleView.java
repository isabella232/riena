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

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * View of a sub-module with some <i>traverseable</i> SWT Widgets (e.g. {@code
 * Scale}).
 */
public class TraverseSubModuleView extends SubModuleView {

	public TraverseSubModuleView() {
	}

	public static final String ID = TraverseSubModuleView.class.getName();

	/**
	 * {@inheritDoc}
	 * <p>
	 * <i>Traverseable</i> SWT Widgets are added to the view.
	 */
	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		UIControlsFactory.createLabel(parent, "Degrees Fahrenheit:"); //$NON-NLS-1$

		UIControlsFactory.createSpinner(parent, SWT.BORDER, "fahrenheitSpinner"); //$NON-NLS-1$

		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

		UIControlsFactory.createLabel(parent, "Degree Celsius:"); //$NON-NLS-1$

		UIControlsFactory.createScale(parent, SWT.NONE, "celsiusScale"); //$NON-NLS-1$
		new Label(parent, SWT.NONE);

		Composite compositeCelsius = new Composite(parent, SWT.NONE);
		compositeCelsius.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		compositeCelsius.setLayout(new GridLayout(3, false));

		Label lblZero = new Label(compositeCelsius, SWT.NONE);
		lblZero.setText("0\u00B0"); //$NON-NLS-1$

		Label labelDummy = new Label(compositeCelsius, SWT.NONE);
		labelDummy.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));

		Label lblPlus50 = new Label(compositeCelsius, SWT.NONE);
		lblPlus50.setText("50\u00B0"); //$NON-NLS-1$

		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);

		UIControlsFactory.createLabel(parent, "Kelvin:"); //$NON-NLS-1$

		ProgressBar progressBar = UIControlsFactory.createProgressBar(parent, SWT.HORIZONTAL, "kelvinProgressBar"); //$NON-NLS-1$
		progressBar.setState(SWT.PAUSED);
		new Label(parent, SWT.NONE);

		Composite compositeKelvin = new Composite(parent, SWT.NONE);
		compositeKelvin.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		compositeKelvin.setLayout(new GridLayout(3, false));

		Label lbl255 = new Label(compositeKelvin, SWT.NONE);
		lbl255.setText("255"); //$NON-NLS-1$

		Label labelDummy2 = new Label(compositeKelvin, SWT.NONE);
		labelDummy2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));

		Label lbl300 = new Label(compositeKelvin, SWT.NONE);
		lbl300.setText("323"); //$NON-NLS-1$

	}

}
