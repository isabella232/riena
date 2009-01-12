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
package org.eclipse.riena.demo.client.customer.views;

import org.eclipse.riena.demo.client.customer.controllers.CustomerContractController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.swtdesigner.SWTResourceManager;

/**
 *
 */
public class CustomerContractView extends SubModuleView<CustomerContractController> {

	public static final String ID = "org.eclipse.riena.example.client.views.CustomerContractView"; //$NON-NLS-1$

	/**
	 * Create contents of the view part
	 * 
	 * @param parent
	 */
	@Override
	public void basicCreatePartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		parent.setLayout(new FillLayout());

		final Label missingCustomerContractLabel = new Label(container, SWT.NONE);
		missingCustomerContractLabel.setFont(SWTResourceManager.getFont("", 18, SWT.BOLD)); //$NON-NLS-1$
		missingCustomerContractLabel.setText("Customer Contract View Not Implemented"); //$NON-NLS-1$
		missingCustomerContractLabel.setBounds(10, 236, 630, 43);
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

}
