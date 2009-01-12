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
package org.eclipse.riena.sample.app.client.helloworld.views;

import org.eclipse.riena.internal.ui.ridgets.swt.ActionRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.TextRidget;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.sample.app.client.helloworld.controllers.CustomerDetailsSubModuleController;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class CustomerDetailsSubModuleView extends SubModuleView<CustomerDetailsSubModuleController> {

	public final static String ID = CustomerDetailsSubModuleView.class.getName();

	private static final int FIELD_WIDTH = 100;
	private final static int TOP = 10;
	private static final int LEFT = 10;
	private static final int SECTION_LABEL_WIDTH = 100;
	private static final int LABEL_WIDTH = 90;
	private static final int LINE_GAP = 12;
	private static final int COL_GAP = 30;

	private Composite contentArea;

	@Override
	protected void basicCreatePartControl(Composite parent) {
		setTitle("test"); //$NON-NLS-1$
		this.contentArea = parent;
		contentArea.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		contentArea.setLayout(new FormLayout());

		Label personLabel = createSectionLabel(contentArea, "Person"); //$NON-NLS-1$
		FormData fd = new FormData();
		fd.top = new FormAttachment(0, TOP);
		fd.left = new FormAttachment(0, LEFT);
		personLabel.setLayoutData(fd);

		Label kundennummerLabel = new Label(contentArea, SWT.LEFT);
		kundennummerLabel.setText("Customer No."); //$NON-NLS-1$
		kundennummerLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		fd = new FormData();
		fd.top = new FormAttachment(personLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(personLabel, SECTION_LABEL_WIDTH, SWT.LEFT);
		kundennummerLabel.setLayoutData(fd);

		Text numberText = new Text(contentArea, SWT.SINGLE);
		ITextRidget textFacade = new TextRidget();
		textFacade.setUIControl(numberText);
		getController().setNumberFacade(textFacade);
		numberText.setEditable(false);
		fd = new FormData();
		fd.top = new FormAttachment(kundennummerLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(kundennummerLabel, LABEL_WIDTH, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		numberText.setLayoutData(fd);

		Label nameLabel = new Label(contentArea, SWT.LEFT);
		nameLabel.setText("Last Name"); //$NON-NLS-1$
		nameLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		fd = new FormData();
		fd.top = new FormAttachment(kundennummerLabel, LINE_GAP);
		fd.left = new FormAttachment(kundennummerLabel, 0, SWT.LEFT);
		nameLabel.setLayoutData(fd);

		Text nameText = new Text(contentArea, SWT.BORDER | SWT.SINGLE);
		textFacade = new TextRidget();
		textFacade.setUIControl(nameText);
		getController().setNameFacade(textFacade);
		fd = new FormData();
		fd.top = new FormAttachment(nameLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(numberText, 0, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		nameText.setLayoutData(fd);

		Label vornameLabel = new Label(contentArea, SWT.LEFT);
		vornameLabel.setText("First Name"); //$NON-NLS-1$
		vornameLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		fd = new FormData();
		fd.top = new FormAttachment(nameLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(nameLabel, COL_GAP + FIELD_WIDTH + LABEL_WIDTH, SWT.LEFT);
		vornameLabel.setLayoutData(fd);

		Text firstnameText = new Text(contentArea, SWT.BORDER | SWT.SINGLE);
		textFacade = new TextRidget();
		textFacade.setUIControl(firstnameText);
		getController().setFirstnameFacade(textFacade);
		fd = new FormData();
		fd.top = new FormAttachment(vornameLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(vornameLabel, LABEL_WIDTH, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		firstnameText.setLayoutData(fd);

		Label birthdayLabel = new Label(contentArea, SWT.LEFT);
		birthdayLabel.setText("Birthday"); //$NON-NLS-1$
		birthdayLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		fd = new FormData();
		fd.top = new FormAttachment(nameLabel, LINE_GAP);
		fd.left = new FormAttachment(nameLabel, 0, SWT.LEFT);
		birthdayLabel.setLayoutData(fd);

		DateTime birthdayText = new DateTime(contentArea, SWT.BORDER | SWT.SINGLE);
		// ISWTObservableValue birthdayTextObserver
		// =SWTObservables.observeText(birthdayText, SWT.FocusOut);
		// Customer customer = getCustomer();
		// IObservableValue birthdayValueObserver =
		// BeansObservables.observeValue(customer.getBirth(), "birthDay");
		// context.bindValue(birthdayTextObserver, birthdayValueObserver, null,
		// null);
		fd = new FormData();
		fd.top = new FormAttachment(birthdayLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(nameText, 0, SWT.LEFT);
		birthdayText.setLayoutData(fd);

		Label birthplaceLabel = new Label(contentArea, SWT.LEFT);
		birthplaceLabel.setText("Birthplace"); //$NON-NLS-1$
		birthplaceLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		fd = new FormData();
		fd.top = new FormAttachment(birthdayLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(birthdayLabel, COL_GAP + FIELD_WIDTH + LABEL_WIDTH, SWT.LEFT);
		birthplaceLabel.setLayoutData(fd);

		Text birthplaceText = new Text(contentArea, SWT.BORDER | SWT.SINGLE);
		textFacade = new TextRidget();
		textFacade.setUIControl(birthplaceText);
		getController().setBirthplaceFacade(textFacade);
		fd = new FormData();
		fd.top = new FormAttachment(birthplaceLabel, 0, SWT.TOP);
		fd.left = new FormAttachment(birthplaceLabel, LABEL_WIDTH, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		birthplaceText.setLayoutData(fd);

		Button openOffers = new Button(contentArea, 0);
		IActionRidget actionAdapter = new ActionRidget();
		actionAdapter.setUIControl(openOffers);
		getController().setOffersFacade(actionAdapter);
		openOffers.setEnabled(false);
		openOffers.setText("Offers"); //$NON-NLS-1$
		fd = new FormData();
		fd.top = new FormAttachment(birthplaceText, LINE_GAP);
		fd.left = new FormAttachment(birthdayText, 0, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		openOffers.setLayoutData(fd);

		Button saveButton = new Button(contentArea, 0);
		actionAdapter = new ActionRidget();
		actionAdapter.setUIControl(saveButton);
		getController().setSaveFacade(actionAdapter);
		saveButton.setText("Save"); //$NON-NLS-1$
		// saveButton.addSelectionListener(new StoreCustomerListener());
		fd = new FormData();
		fd.top = new FormAttachment(birthplaceText, LINE_GAP);
		fd.left = new FormAttachment(birthplaceText, 0, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		saveButton.setLayoutData(fd);
	}

	private Label createSectionLabel(Composite parent, String text) {

		Label label = new Label(parent, SWT.LEFT);
		label.setText(text);
		label.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_DARK_GRAY));
		label.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		return label;

	}

	public Image getIcon() {
		return AbstractUIPlugin.imageDescriptorFromPlugin("de.compeople.scp.sample.client", "/icons/user_16.png") //$NON-NLS-1$ //$NON-NLS-2$
				.createImage();
	}

	public void setIcon(Image icon) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	protected CustomerDetailsSubModuleController createController(ISubModuleNode subModuleNode) {
		return new CustomerDetailsSubModuleController(subModuleNode);
	}

}
