/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.demo.client.views;

import com.swtdesigner.SWTResourceManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.MasterDetailsComposite;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 *
 */
public class CustomerContractView extends SubModuleView {
	public CustomerContractView() {
	}

	/**
	 * Create contents of the view part
	 * 
	 * @param parent
	 */
	@Override
	public void basicCreatePartControl(final Composite parent) {

		final Composite container = new Composite(parent, SWT.NONE);
		//		container.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		parent.setLayout(new FillLayout(SWT.VERTICAL));

		final Label personalLabel = new Label(container, SWT.NONE);
		personalLabel.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NORMAL));
		//		personalLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		personalLabel.setText("Name"); //$NON-NLS-1$
		personalLabel.setBounds(25, 20, 66, 28);

		final Label lFirstname = new Label(container, SWT.NONE);
		lFirstname.setForeground(SWTResourceManager.getColor(1, 0, 0));
		lFirstname.setFont(SWTResourceManager.getFont("Arial", 12, SWT.BOLD)); //$NON-NLS-1$
		lFirstname.setText("Firstname"); //$NON-NLS-1$
		lFirstname.setBounds(130, 23, 94, 22);

		final Text firstname = new Text(container, SWT.BORDER);
		firstname.setData("binding_property", "firstname"); //$NON-NLS-1$ //$NON-NLS-2$
		firstname.setFont(SWTResourceManager.getFont("", 12, SWT.NONE)); //$NON-NLS-1$
		firstname.setBounds(236, 20, 181, 32);

		final Label lLastname = new Label(container, SWT.NONE);
		lLastname.setForeground(SWTResourceManager.getColor(1, 0, 0));
		lLastname.setFont(SWTResourceManager.getFont("Arial", 12, SWT.BOLD)); //$NON-NLS-1$
		lLastname.setText("Lastname"); //$NON-NLS-1$
		lLastname.setBounds(130, 63, 93, 23);

		final Text lastname = new Text(container, SWT.BORDER);
		lastname.setData("binding_property", "lastname"); //$NON-NLS-1$ //$NON-NLS-2$
		lastname.setFont(SWTResourceManager.getFont("", 12, SWT.NONE)); //$NON-NLS-1$
		lastname.setBounds(235, 62, 182, 32);

		final Group group = new Group(container, SWT.NONE);
		group.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		group.setLayout(new FillLayout());
		group.setBounds(130, 120, 537, 310);

		final MasterDetailsComposite mdComposite = new MasterDetailsComposite(group, SWT.NONE, SWT.BOTTOM);
		mdComposite.getTable().setFont(SWTResourceManager.getFont("Arial", 8, SWT.NORMAL));
		mdComposite.getButtonApply().setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.BOLD));
		mdComposite.getButtonRemove().setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.BOLD));
		mdComposite.getButtonNew().setFont(SWTResourceManager.getFont("Tahoma", 10, SWT.BOLD));
		{
			final Composite details = mdComposite.getDetails();
			details.setLayout(new GridLayout(2, false));

			final Label lcontractNo = UIControlsFactory.createLabel(details, "ContractNo"); //$NON-NLS-1$
			lcontractNo.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
			final Text contractNo = UIControlsFactory.createText(details);
			lcontractNo.setForeground(SWTResourceManager.getColor(1, 0, 0));
			final GridData gd_contractNo = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
			gd_contractNo.widthHint = 115;
			contractNo.setLayoutData(gd_contractNo);
			//				contractNo.setData("binding_property", "contractno");
			mdComposite.addUIControl(contractNo, "contractno"); //$NON-NLS-1$

			final Label lDescription = UIControlsFactory.createLabel(details, "Description"); //$NON-NLS-1$
			lDescription.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
			final Text description = UIControlsFactory.createText(details);
			lDescription.setForeground(SWTResourceManager.getColor(1, 0, 0));
			final GridData gd_description = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
			gd_description.widthHint = 432;
			description.setLayoutData(gd_description);
			//				description.setData("binding_property", "description");
			mdComposite.addUIControl(description, "description"); //$NON-NLS-1$

			final Label lValue = UIControlsFactory.createLabel(details, "Value"); //$NON-NLS-1$
			lValue.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
			final Text value = UIControlsFactory.createText(details);
			lValue.setForeground(SWTResourceManager.getColor(1, 0, 0));
			final GridData gd_value = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
			gd_value.widthHint = 114;
			value.setLayoutData(gd_value);
			//				value.setData("binding_property", "value");
			mdComposite.addUIControl(value, "value"); //$NON-NLS-1$

			final Label lStatus = UIControlsFactory.createLabel(details, "Status"); //$NON-NLS-1$
			lStatus.setFont(SWTResourceManager.getFont("Arial", 10, SWT.BOLD));
			final Text status = UIControlsFactory.createText(details);
			lStatus.setForeground(SWTResourceManager.getColor(1, 0, 0));
			final GridData gd_status = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
			gd_status.widthHint = 114;
			status.setLayoutData(gd_status);
			//				status.setData("binding_property", "status");
			mdComposite.addUIControl(status, "status"); //$NON-NLS-1$
		}

		//		mdComposite.setData("binding_property","contracts");
		addUIControl(mdComposite, "contracts"); //$NON-NLS-1$
		mdComposite.setBounds(5, 15, 520, 350);

		final Button saveButton = new Button(container, SWT.NONE);
		saveButton.setBounds(625, 465, 109, 38);
		saveButton.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD)); //$NON-NLS-1$
		saveButton.setData("binding_property", "savea_action"); //$NON-NLS-1$ //$NON-NLS-2$
		saveButton.setText("Save"); //$NON-NLS-1$

		final Label contractsLabel = new Label(container, SWT.NONE);
		contractsLabel.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NORMAL));
		contractsLabel.setText("Contracts"); //$NON-NLS-1$
		contractsLabel.setBounds(25, 136, 81, 28);

		final Composite composite = new Composite(container, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(121, 117, 168));
		composite.setBounds(30, 105, 706, 2);

		final Composite composite2 = new Composite(container, SWT.NONE);
		composite2.setBackground(SWTResourceManager.getColor(121, 117, 168));
		composite2.setBounds(0, 450, 767, 2);

	}

}
