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
package org.eclipse.riena.demo.client.views;

import com.swtdesigner.SWTResourceManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.demo.client.controllers.CustomerOverviewController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;

/**
 *
 */
public class CustomerOverviewView extends SubModuleView<CustomerOverviewController> {

	/**
	 * Create contents of the view part
	 * 
	 * @param parent
	 */
	@Override
	public void basicCreatePartControl(Composite parent) {

		Text salary;
		Text birthdate;
		Text city;
		Text zipcode;
		Text street;
		Text lastname;
		Text firstname;

		parent.setLayout(new FillLayout());
		Composite container = new Composite(parent, SWT.NONE);

		final Label personalLabel = new Label(container, SWT.NONE);
		personalLabel.setText("Name");
		personalLabel.setBounds(30, 27, 66, 21);

		final Label Lfirstname = new Label(container, SWT.NONE);
		Lfirstname.setForeground(SWTResourceManager.getColor(1, 0, 0));
		Lfirstname.setFont(SWTResourceManager.getFont("", 14, SWT.NONE));
		Lfirstname.setText("Firstname");
		Lfirstname.setBounds(150, 25, 131, 32);

		firstname = new Text(container, SWT.BORDER);
		firstname.setData("binding_property", "firstname");
		firstname.setFont(SWTResourceManager.getFont("", 12, SWT.NONE));
		firstname.setBounds(291, 25, 181, 32);

		final Label Llastname = new Label(container, SWT.NONE);
		Llastname.setForeground(SWTResourceManager.getColor(1, 0, 0));
		Llastname.setFont(SWTResourceManager.getFont("", 14, SWT.NONE));
		Llastname.setText("Lastname");
		Llastname.setBounds(150, 67, 130, 23);

		lastname = new Text(container, SWT.BORDER);
		lastname.setData("binding_property", "lastname");
		lastname.setFont(SWTResourceManager.getFont("", 12, SWT.NONE));
		lastname.setBounds(290, 67, 182, 32);

		final Label Lzipcity = new Label(container, SWT.NONE);
		Lzipcity.setForeground(SWTResourceManager.getColor(1, 0, 0));
		Lzipcity.setBounds(150, 173, 132, 23);
		Lzipcity.setFont(SWTResourceManager.getFont("", 14, SWT.NONE));
		Lzipcity.setText("Zipcode / City");

		final Label address = new Label(container, SWT.NONE);
		address.setBounds(30, 132, 86, 22);
		address.setText("Address");

		final Label Lstreet = new Label(container, SWT.NONE);
		Lstreet.setForeground(SWTResourceManager.getColor(1, 0, 0));
		Lstreet.setBounds(150, 133, 132, 23);
		Lstreet.setFont(SWTResourceManager.getFont("", 14, SWT.NONE));
		Lstreet.setText("Street");

		street = new Text(container, SWT.BORDER);
		street.setData("binding_property", "street");
		street.setBounds(291, 125, 182, 32);
		street.setFont(SWTResourceManager.getFont("", 12, SWT.NONE));

		zipcode = new Text(container, SWT.BORDER);
		zipcode.setData("binding_property", "zipcode");
		zipcode.setBounds(290, 165, 66, 32);
		zipcode.setFont(SWTResourceManager.getFont("", 12, SWT.NONE));

		city = new Text(container, SWT.BORDER);
		city.setData("binding_property", "city");
		city.setBounds(377, 165, 182, 32);
		city.setFont(SWTResourceManager.getFont("", 12, SWT.NONE));

		final Label address_1 = new Label(container, SWT.NONE);
		address_1.setBounds(30, 232, 86, 27);
		address_1.setText("Personal");

		final Label Lbirthday = new Label(container, SWT.NONE);
		Lbirthday.setForeground(SWTResourceManager.getColor(1, 0, 0));
		Lbirthday.setBounds(150, 233, 118, 32);
		Lbirthday.setFont(SWTResourceManager.getFont("", 14, SWT.NONE));
		Lbirthday.setText("Birthdate");

		final Label Lsalary = new Label(container, SWT.NONE);
		Lsalary.setForeground(SWTResourceManager.getColor(1, 0, 0));
		Lsalary.setBounds(150, 275, 132, 32);
		Lsalary.setFont(SWTResourceManager.getFont("", 14, SWT.NONE));
		Lsalary.setText("Salary");
		Lsalary.setData("binding_property", "salaryLabel");

		birthdate = new Text(container, SWT.BORDER);
		birthdate.setData("binding_property", "birthdate");
		birthdate.setData("type", "date");
		birthdate.setBounds(291, 233, 100, 32);
		birthdate.setFont(SWTResourceManager.getFont("", 12, SWT.NONE));

		salary = new Text(container, SWT.BORDER);
		salary.setData("binding_property", "salary");
		salary.setBounds(291, 275, 100, 32);
		salary.setFont(SWTResourceManager.getFont("", 12, SWT.NONE));
		salary.setData("type", "decimal");

		final Button saveButton = new Button(container, SWT.NONE);
		saveButton.setData("binding_property", "savea_action");
		saveButton.setFont(SWTResourceManager.getFont("", 12, SWT.NONE));
		saveButton.setText("Save");
		saveButton.setBounds(625, 465, 109, 38);

		final Button aButton = new Button(container, SWT.TOGGLE);
		aButton.setText("A");
		aButton.setBounds(650, 30, 36, 32);
		aButton.setData("binding_property", "assistent");

		final Button aButton_1 = new Button(container, SWT.TOGGLE);
		aButton_1.setBounds(700, 30, 36, 32);
		aButton_1.setText("B");
		aButton_1.setData("binding_property", "mandatory");

		final Composite composite = new Composite(container, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(121, 117, 168));
		composite.setBounds(30, 110, 706, 2);

		final Composite composite2 = new Composite(container, SWT.NONE);
		composite2.setBackground(SWTResourceManager.getColor(121, 117, 168));
		composite2.setBounds(30, 214, 706, 2);

		final Composite composite3 = new Composite(container, SWT.NONE);
		composite3.setBackground(SWTResourceManager.getColor(121, 117, 168));
		composite3.setBounds(0, 450, 766, 2);
	}

}
