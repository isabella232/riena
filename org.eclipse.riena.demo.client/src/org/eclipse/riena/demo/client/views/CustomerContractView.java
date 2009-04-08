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
package org.eclipse.riena.demo.client.views;

import com.swtdesigner.SWTResourceManager;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.demo.client.controllers.CustomerContractController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.MasterDetailsComposite;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 *
 */
public class CustomerContractView extends SubModuleView<CustomerContractController> {
	public CustomerContractView() {
	}

	/**
	 * Create contents of the view part
	 * 
	 * @param parent
	 */
	@Override
	public void basicCreatePartControl(Composite parent) {

		Composite container = new Composite(parent, SWT.NONE);
		//		container.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		parent.setLayout(new FillLayout(SWT.VERTICAL));

		final Label personalLabel = new Label(container, SWT.NONE);
		//		personalLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		personalLabel.setText("Name");
		personalLabel.setBounds(25, 20, 66, 28);

		final Label Lfirstname = new Label(container, SWT.NONE);
		Lfirstname.setForeground(SWTResourceManager.getColor(1, 0, 0));
		Lfirstname.setFont(SWTResourceManager.getFont("", 14, SWT.NONE));
		Lfirstname.setText("Firstname");
		Lfirstname.setBounds(130, 23, 94, 22);

		Text firstname = new Text(container, SWT.BORDER);
		firstname.setData("binding_property", "firstname");
		firstname.setFont(SWTResourceManager.getFont("", 12, SWT.NONE));
		firstname.setBounds(236, 20, 181, 32);

		final Label Llastname = new Label(container, SWT.NONE);
		Llastname.setForeground(SWTResourceManager.getColor(1, 0, 0));
		Llastname.setFont(SWTResourceManager.getFont("", 14, SWT.NONE));
		Llastname.setText("Lastname");
		Llastname.setBounds(130, 63, 93, 23);

		Text lastname = new Text(container, SWT.BORDER);
		lastname.setData("binding_property", "lastname");
		lastname.setFont(SWTResourceManager.getFont("", 12, SWT.NONE));
		lastname.setBounds(235, 62, 182, 32);

		final Group group = new Group(container, SWT.NONE);
		group.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		group.setLayout(new FillLayout());
		group.setBounds(130, 120, 537, 310);

		MasterDetailsComposite mdComposite = new MasterDetailsComposite(group, SWT.NONE, SWT.BOTTOM) {
			protected void createDetails(Composite parent) {
				GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).applyTo(parent);

				Label lcontractNo = UIControlsFactory.createLabel(parent, "ContractNo");
				Text contractNo = UIControlsFactory.createText(parent);
				lcontractNo.setForeground(SWTResourceManager.getColor(1, 0, 0));
				GridDataFactory.fillDefaults().grab(true, false).applyTo(contractNo);
				//				contractNo.setData("binding_property", "contractno");
				addUIControl(contractNo, "contractno");

				Label lDescription = UIControlsFactory.createLabel(parent, "Description");
				Text description = UIControlsFactory.createText(parent);
				lDescription.setForeground(SWTResourceManager.getColor(1, 0, 0));
				GridDataFactory.fillDefaults().grab(true, false).applyTo(description);
				//				description.setData("binding_property", "description");
				addUIControl(description, "description");

				Label lValue = UIControlsFactory.createLabel(parent, "Value");
				Text value = UIControlsFactory.createText(parent);
				lValue.setForeground(SWTResourceManager.getColor(1, 0, 0));
				GridDataFactory.fillDefaults().grab(true, false).applyTo(value);
				//				value.setData("binding_property", "value");
				addUIControl(value, "value");

				Label lStatus = UIControlsFactory.createLabel(parent, "Status");
				Text status = UIControlsFactory.createText(parent);
				lStatus.setForeground(SWTResourceManager.getColor(1, 0, 0));
				GridDataFactory.fillDefaults().grab(true, false).applyTo(status);
				//				status.setData("binding_property", "status");
				addUIControl(status, "status");
			}
		};
		//		mdComposite.setData("binding_property","contracts");
		addUIControl(mdComposite, "contracts");
		mdComposite.setBounds(5, 15, 520, 350);

		final Button saveButton = new Button(container, SWT.NONE);
		saveButton.setBounds(625, 465, 109, 38);
		saveButton.setFont(SWTResourceManager.getFont("", 12, SWT.NONE));
		saveButton.setData("binding_property", "savea_action");
		saveButton.setText("Save");

		final Label contractsLabel = new Label(container, SWT.NONE);
		contractsLabel.setText("Contracts");
		contractsLabel.setBounds(25, 136, 81, 28);

		final Composite composite = new Composite(container, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(121, 117, 168));
		composite.setBounds(30, 105, 706, 2);

		final Composite composite2 = new Composite(container, SWT.NONE);
		composite2.setBackground(SWTResourceManager.getColor(121, 117, 168));
		composite2.setBounds(0, 450, 767, 2);

	}

}
