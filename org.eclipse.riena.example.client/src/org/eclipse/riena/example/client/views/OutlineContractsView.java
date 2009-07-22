package org.eclipse.riena.example.client.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import org.eclipse.riena.example.client.controllers.OutlineContractsController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;

public class OutlineContractsView extends SubModuleView<OutlineContractsController> {

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
		lbl.setText("OutlineContracts"); //$NON-NLS-1$

	}

}
