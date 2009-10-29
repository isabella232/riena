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
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Spinner;

import org.eclipse.riena.example.client.controllers.TraverseSubModuleController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;

/**
 *
 */
public class TraverseSubModuleView extends SubModuleView<TraverseSubModuleController> {
	public TraverseSubModuleView() {
	}

	public static final String ID = TraverseSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		Label lblDegreesFahrenheit = new Label(parent, SWT.NONE);
		lblDegreesFahrenheit.setText("Degrees Fahrenheit:");

		Spinner fahrenheitSpinner = new Spinner(parent, SWT.BORDER);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(fahrenheitSpinner, "fahrenheitSpinner");

		Label lblDegreeCelsius = new Label(parent, SWT.NONE);
		lblDegreeCelsius.setText("Degree Celsius:");

		Scale celsiusScale = new Scale(parent, SWT.NONE);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(celsiusScale, "celsiusScale");
		new Label(parent, SWT.NONE);

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite.setLayout(new GridLayout(3, false));

		Label lblZero = new Label(composite, SWT.NONE);
		lblZero.setText("0\u00B0");

		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));

		Label lblPlus50 = new Label(composite, SWT.NONE);
		lblPlus50.setText("50\u00B0");
	}

}
