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

	public CustomerOverviewView() {
	}

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
		Text emailaddress;

		parent.setLayout(new FillLayout());
		Composite container = new Composite(parent, SWT.NONE);

		final Label personalLabel = new Label(container, SWT.NONE);
		personalLabel.setText("Name"); //$NON-NLS-1$
		personalLabel.setBounds(30, 27, 66, 21);

		final Label lFirstname = new Label(container, SWT.NONE);
		lFirstname.setForeground(SWTResourceManager.getColor(1, 0, 0));
		lFirstname.setFont(SWTResourceManager.getFont("", 14, SWT.NONE)); //$NON-NLS-1$
		lFirstname.setText("Firstname"); //$NON-NLS-1$
		lFirstname.setBounds(150, 25, 131, 32);

		firstname = new Text(container, SWT.BORDER);
		firstname.setData("binding_property", "firstname"); //$NON-NLS-1$ //$NON-NLS-2$
		firstname.setFont(SWTResourceManager.getFont("", 12, SWT.NONE)); //$NON-NLS-1$
		firstname.setBounds(291, 25, 181, 32);

		final Label lLastname = new Label(container, SWT.NONE);
		lLastname.setForeground(SWTResourceManager.getColor(1, 0, 0));
		lLastname.setFont(SWTResourceManager.getFont("", 14, SWT.NONE)); //$NON-NLS-1$
		lLastname.setText("Lastname"); //$NON-NLS-1$
		lLastname.setBounds(150, 67, 130, 23);

		lastname = new Text(container, SWT.BORDER);
		lastname.setData("binding_property", "lastname"); //$NON-NLS-1$ //$NON-NLS-2$
		lastname.setFont(SWTResourceManager.getFont("", 12, SWT.NONE)); //$NON-NLS-1$
		lastname.setBounds(290, 67, 182, 32);

		final Label lZipcity = new Label(container, SWT.NONE);
		lZipcity.setForeground(SWTResourceManager.getColor(1, 0, 0));
		lZipcity.setBounds(150, 173, 132, 23);
		lZipcity.setFont(SWTResourceManager.getFont("", 14, SWT.NONE)); //$NON-NLS-1$
		lZipcity.setText("Zipcode / City"); //$NON-NLS-1$

		final Label address = new Label(container, SWT.NONE);
		address.setBounds(30, 132, 86, 22);
		address.setText("Address"); //$NON-NLS-1$

		final Label lStreet = new Label(container, SWT.NONE);
		lStreet.setForeground(SWTResourceManager.getColor(1, 0, 0));
		lStreet.setBounds(150, 133, 132, 23);
		lStreet.setFont(SWTResourceManager.getFont("", 14, SWT.NONE)); //$NON-NLS-1$
		lStreet.setText("Street"); //$NON-NLS-1$

		street = new Text(container, SWT.BORDER);
		street.setData("binding_property", "street"); //$NON-NLS-1$ //$NON-NLS-2$
		street.setBounds(291, 125, 182, 32);
		street.setFont(SWTResourceManager.getFont("", 12, SWT.NONE)); //$NON-NLS-1$

		zipcode = new Text(container, SWT.BORDER);
		zipcode.setData("binding_property", "zipcode"); //$NON-NLS-1$ //$NON-NLS-2$
		zipcode.setBounds(290, 165, 66, 32);
		zipcode.setFont(SWTResourceManager.getFont("", 12, SWT.NONE)); //$NON-NLS-1$

		city = new Text(container, SWT.BORDER);
		city.setData("binding_property", "city"); //$NON-NLS-1$ //$NON-NLS-2$
		city.setBounds(377, 165, 182, 32);
		city.setFont(SWTResourceManager.getFont("", 12, SWT.NONE)); //$NON-NLS-1$

		final Label personal = new Label(container, SWT.NONE);
		personal.setBounds(30, 232, 86, 27);
		personal.setText("Personal"); //$NON-NLS-1$

		final Label lEmailAddress = new Label(container, SWT.NONE);
		lEmailAddress.setForeground(SWTResourceManager.getColor(1, 0, 0));
		lEmailAddress.setBounds(150, 233, 131, 32);
		lEmailAddress.setFont(SWTResourceManager.getFont("", 14, SWT.NONE)); //$NON-NLS-1$
		lEmailAddress.setText("E-mail address"); //$NON-NLS-1$

		final Label lBirthday = new Label(container, SWT.NONE);
		lBirthday.setForeground(SWTResourceManager.getColor(1, 0, 0));
		lBirthday.setBounds(150, 275, 132, 32);
		lBirthday.setFont(SWTResourceManager.getFont("", 14, SWT.NONE)); //$NON-NLS-1$
		lBirthday.setText("Birthdate"); //$NON-NLS-1$

		final Label lSalary = new Label(container, SWT.NONE);
		lSalary.setForeground(SWTResourceManager.getColor(1, 0, 0));
		lSalary.setBounds(150, 317, 132, 32);
		lSalary.setFont(SWTResourceManager.getFont("", 14, SWT.NONE)); //$NON-NLS-1$
		lSalary.setText("Salary"); //$NON-NLS-1$
		lSalary.setData("binding_property", "salaryLabel"); //$NON-NLS-1$ //$NON-NLS-2$

		emailaddress = new Text(container, SWT.BORDER);
		emailaddress.setData("binding_property", "emailaddress"); //$NON-NLS-1$ //$NON-NLS-2$
		emailaddress.setBounds(291, 233, 250, 32);
		emailaddress.setFont(SWTResourceManager.getFont("", 12, SWT.NORMAL)); //$NON-NLS-1$

		birthdate = new Text(container, SWT.BORDER);
		birthdate.setData("binding_property", "birthdate"); //$NON-NLS-1$ //$NON-NLS-2$
		birthdate.setData("type", "date"); //$NON-NLS-1$ //$NON-NLS-2$
		birthdate.setBounds(291, 275, 100, 32);
		birthdate.setFont(SWTResourceManager.getFont("", 12, SWT.NONE)); //$NON-NLS-1$

		salary = new Text(container, SWT.BORDER);
		salary.setData("binding_property", "salary"); //$NON-NLS-1$ //$NON-NLS-2$
		salary.setBounds(291, 317, 100, 32);
		salary.setFont(SWTResourceManager.getFont("", 12, SWT.NONE)); //$NON-NLS-1$
		salary.setData("type", "decimal"); //$NON-NLS-1$ //$NON-NLS-2$

		final Button saveButton = new Button(container, SWT.NONE);
		saveButton.setData("binding_property", "savea_action"); //$NON-NLS-1$ //$NON-NLS-2$
		saveButton.setFont(SWTResourceManager.getFont("", 12, SWT.NONE)); //$NON-NLS-1$
		saveButton.setText("Save"); //$NON-NLS-1$
		saveButton.setBounds(500, 465, 109, 38);

		final Button openEmailsButton = new Button(container, SWT.NONE);
		openEmailsButton.setData("binding_property", "openEmails_action"); //$NON-NLS-1$ //$NON-NLS-2$
		openEmailsButton.setFont(SWTResourceManager.getFont("", 12, SWT.NONE)); //$NON-NLS-1$
		openEmailsButton.setText("Open Emails"); //$NON-NLS-1$
		openEmailsButton.setBounds(625, 465, 109, 38);

		final Button buttonA = new Button(container, SWT.TOGGLE);
		buttonA.setText("A"); //$NON-NLS-1$
		buttonA.setBounds(650, 30, 36, 32);
		buttonA.setData("binding_property", "assistent"); //$NON-NLS-1$ //$NON-NLS-2$

		final Button buttonB = new Button(container, SWT.TOGGLE);
		buttonB.setBounds(700, 30, 36, 32);
		buttonB.setText("B"); //$NON-NLS-1$
		buttonB.setData("binding_property", "mandatory"); //$NON-NLS-1$ //$NON-NLS-2$

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
