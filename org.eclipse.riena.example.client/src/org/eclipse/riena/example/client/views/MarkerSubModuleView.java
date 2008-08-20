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
package org.eclipse.riena.example.client.views;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.riena.example.client.controllers.MarkerSubModuleController;
import org.eclipse.riena.internal.example.client.utils.UIControlsFactory;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.swt.ChoiceComposite;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

/**
 * Example for various marker types.
 * 
 * @see IMarkableRidget
 */
public class MarkerSubModuleView extends SubModuleView<MarkerSubModuleController> {

	public static final String ID = MarkerSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(ILnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(2, false));

		Group group1 = createMarkerOptionsGroup(parent);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(group1);
		Group group2 = createVisibilityOptionsGroup(parent);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(group2);
		Group group3 = createControlsGroup(parent);
		GridDataFactory.fillDefaults().grab(true, false).span(2, 1).applyTo(group3);
	}

	// helping methods
	// ////////////////

	private Group createMarkerOptionsGroup(Composite parent) {
		Group group = UIControlsFactory.createGroup(parent, "Marker Options:"); //$NON-NLS-1$
		group.setLayout(createFillLayout());

		Button checkMandatory = UIControlsFactory.createButtonCheck(group);
		addUIControl(checkMandatory, "checkMandatory"); //$NON-NLS-1$

		Button checkError = UIControlsFactory.createButtonCheck(group);
		addUIControl(checkError, "checkError"); //$NON-NLS-1$

		Button checkDisabled = UIControlsFactory.createButtonCheck(group);
		addUIControl(checkDisabled, "checkDisabled"); //$NON-NLS-1$

		Button checkOutput = UIControlsFactory.createButtonCheck(group);
		addUIControl(checkOutput, "checkOutput"); //$NON-NLS-1$

		return group;
	}

	private Group createVisibilityOptionsGroup(Composite parent) {
		Group group = UIControlsFactory.createGroup(parent, "Visibility Options:"); //$NON-NLS-1$
		group.setLayout(createFillLayout());

		Button checkHidden = UIControlsFactory.createButtonCheck(group);
		addUIControl(checkHidden, "checkHidden"); //$NON-NLS-1$

		return group;
	}

	private Group createControlsGroup(Composite parent) {
		Group group = UIControlsFactory.createGroup(parent, "UI-Controls:"); //$NON-NLS-1$
		GridLayoutFactory.swtDefaults().numColumns(2).equalWidth(false).margins(20, 20).applyTo(group);

		Composite composite;
		GridDataFactory hFillFactory = GridDataFactory.fillDefaults().grab(true, false);
		GridDataFactory vFillFactory = GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.BEGINNING);

		UIControlsFactory.createLabel(group, "Name:"); //$NON-NLS-1$
		Text textName = UIControlsFactory.createText(group);
		hFillFactory.applyTo(textName);
		addUIControl(textName, "textName"); //$NON-NLS-1$

		UIControlsFactory.createLabel(group, "Price:"); //$NON-NLS-1$
		Text textPrice = UIControlsFactory.createTextNumeric(group);
		hFillFactory.applyTo(textPrice);
		addUIControl(textPrice, "textPrice"); //$NON-NLS-1$

		UIControlsFactory.createLabel(group, "Age:"); //$NON-NLS-1$
		Combo comboAge = UIControlsFactory.createCombo(group);
		hFillFactory.applyTo(comboAge);
		addUIControl(comboAge, "comboAge"); //$NON-NLS-1$

		UIControlsFactory.createLabel(group, "Type:"); //$NON-NLS-1$
		composite = createComposite(group);
		Button radioRed = UIControlsFactory.createButtonRadio(composite);
		addUIControl(radioRed, "radioRed"); //$NON-NLS-1$
		Button radioWhite = UIControlsFactory.createButtonRadio(composite);
		addUIControl(radioWhite, "radioWhite"); //$NON-NLS-1$
		Button radioRose = UIControlsFactory.createButtonRadio(composite);
		addUIControl(radioRose, "radioRose"); //$NON-NLS-1$
		ChoiceComposite choiceType = new ChoiceComposite(composite, SWT.BORDER, false);
		choiceType.setOrientation(SWT.HORIZONTAL);
		addUIControl(choiceType, "choiceType"); //$NON-NLS-1$

		UIControlsFactory.createLabel(group, "Flavor:"); //$NON-NLS-1$
		composite = createComposite(group);
		Button checkDry = UIControlsFactory.createButtonCheck(composite);
		checkDry.setSelection(true);
		addUIControl(checkDry, "checkDry"); //$NON-NLS-1$
		Button checkSweet = UIControlsFactory.createButtonCheck(composite);
		addUIControl(checkSweet, "checkSweet"); //$NON-NLS-1$
		Button checkSour = UIControlsFactory.createButtonCheck(composite);
		addUIControl(checkSour, "checkSour"); //$NON-NLS-1$
		Button checkSpicy = UIControlsFactory.createButtonCheck(composite);
		addUIControl(checkSpicy, "checkSpicy"); //$NON-NLS-1$
		ChoiceComposite choiceFlavor = new ChoiceComposite(composite, SWT.BORDER, true);
		choiceFlavor.setOrientation(SWT.HORIZONTAL);
		addUIControl(choiceFlavor, "choiceFlavor"); //$NON-NLS-1$

		Label lblReviewed = UIControlsFactory.createLabel(group, "Reviewed by:"); //$NON-NLS-1$
		List listPersons = UIControlsFactory.createList(group, false, true);
		vFillFactory.applyTo(lblReviewed);
		int hHint = UIControlsFactory.getHeightHint(listPersons, 5);
		hFillFactory.hint(SWT.DEFAULT, hHint).applyTo(listPersons);
		addUIControl(listPersons, "listPersons"); //$NON-NLS-1$

		UIControlsFactory.createLabel(group, ""); //$NON-NLS-1$
		composite = createComposite(group);
		Button buttonToggle = UIControlsFactory.createButtonToggle(composite);
		addUIControl(buttonToggle, "buttonToggle"); //$NON-NLS-1$
		Button buttonPush = UIControlsFactory.createButton(composite);
		addUIControl(buttonPush, "buttonPush"); //$NON-NLS-1$

		return group;
	}

	// helping methods
	// ////////////////

	private Composite createComposite(Group group) {
		Composite composite = new Composite(group, SWT.NONE);
		composite.setBackground(LnfManager.getLnf().getColor(ILnfKeyConstants.SUB_MODULE_BACKGROUND));
		GridLayoutFactory.fillDefaults().numColumns(5).equalWidth(false).spacing(10, 0).applyTo(composite);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(composite);
		return composite;
	}

	private FillLayout createFillLayout() {
		FillLayout layout = new FillLayout(SWT.HORIZONTAL);
		layout.marginWidth = 20;
		layout.marginHeight = 20;
		return layout;
	}

}
