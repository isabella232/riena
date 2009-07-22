package org.eclipse.riena.example.client.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import org.eclipse.riena.example.client.controllers.SalaryController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;

public class SalaryView extends SubModuleView<SalaryController> {

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.navigation.ui.swt.views.SubModuleView#
	 * basicCreatePartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setLayout(new RowLayout(SWT.HORIZONTAL));
		Label lbl = new Label(parent, SWT.None);
		lbl.setText("Salary"); //$NON-NLS-1$
	}
}
