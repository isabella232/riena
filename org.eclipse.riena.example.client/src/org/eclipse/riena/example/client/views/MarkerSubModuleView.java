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

import java.util.Arrays;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.nebula.widgets.compositetable.AbstractNativeHeader;
import org.eclipse.swt.nebula.widgets.compositetable.CompositeTable;
import org.eclipse.swt.nebula.widgets.compositetable.ResizableGridRowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import org.eclipse.riena.example.client.controllers.MarkerSubModuleController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.common.IComplexComponent;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.swt.ChoiceComposite;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Example for various marker types.
 * 
 * @see IMarkableRidget
 */
public class MarkerSubModuleView extends SubModuleView<MarkerSubModuleController> {

	public static final String ID = MarkerSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
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
		group.setLayout(createGridLayout(4));

		UIControlsFactory.createButtonCheck(group, "checkMandatory");//$NON-NLS-1$
		UIControlsFactory.createButtonCheck(group, "checkError"); //$NON-NLS-1$
		UIControlsFactory.createButtonCheck(group, "checkDisabled"); //$NON-NLS-1$
		UIControlsFactory.createButtonCheck(group, "checkOutput"); //$NON-NLS-1$

		return group;
	}

	private Group createVisibilityOptionsGroup(Composite parent) {
		Group group = UIControlsFactory.createGroup(parent, "Visibility Options:"); //$NON-NLS-1$
		group.setLayout(createGridLayout(2));

		UIControlsFactory.createButtonCheck(group, "checkHidden"); //$NON-NLS-1$
		UIControlsFactory.createButtonCheck(group, "checkHiddenParent"); //$NON-NLS-1$

		return group;
	}

	private Group createControlsGroup(Composite parent) {
		Group group = UIControlsFactory.createGroup(parent, "UI-Controls:"); //$NON-NLS-1$
		int defaultVSpacing = new GridLayout().verticalSpacing;
		GridLayoutFactory.swtDefaults().numColumns(2).equalWidth(false).margins(20, 20).spacing(10, defaultVSpacing)
				.applyTo(group);

		GridDataFactory hFillFactory = GridDataFactory.fillDefaults().grab(true, false);

		UIControlsFactory.createLabel(group, "Name:", "labeltextName"); //$NON-NLS-1$ //$NON-NLS-2$
		Text textName = UIControlsFactory.createText(group, SWT.SINGLE | SWT.BORDER, "textName"); //$NON-NLS-1$
		hFillFactory.applyTo(textName);

		UIControlsFactory.createLabel(group, "Price:", "labeltextPrice"); //$NON-NLS-1$ //$NON-NLS-2$
		Text textPrice = UIControlsFactory.createTextDecimal(group, "textPrice"); //$NON-NLS-1$
		hFillFactory.applyTo(textPrice);

		UIControlsFactory.createLabel(group, "Amount:", "labeltextAmount"); //$NON-NLS-1$ //$NON-NLS-2$
		Text textAmount = UIControlsFactory.createTextNumeric(group);
		hFillFactory.applyTo(textAmount);
		addUIControl(textAmount, "textAmount"); //$NON-NLS-1$

		UIControlsFactory.createLabel(group, "Production Date:", "labeltextDate"); //$NON-NLS-1$ //$NON-NLS-2$
		Composite cmpDate = createComposite(group, 3, true);
		Text textDate = UIControlsFactory.createTextDate(cmpDate);
		hFillFactory.applyTo(textDate);
		addUIControl(textDate, "textDate"); //$NON-NLS-1$
		DateTime dtDate = new DateTime(cmpDate, SWT.DATE | SWT.DROP_DOWN | SWT.MEDIUM);
		hFillFactory.applyTo(dtDate);
		addUIControl(dtDate, "dtDate"); //$NON-NLS-1$
		UIControlsFactory.createDatePickerComposite(cmpDate, "dtPicker"); //$NON-NLS-1$

		UIControlsFactory.createLabel(group, "Age:", "labelcomboAge"); //$NON-NLS-1$ //$NON-NLS-2$
		final Combo comboAge = UIControlsFactory.createCombo(group);
		hFillFactory.applyTo(comboAge);
		addUIControl(comboAge, "comboAge"); //$NON-NLS-1$

		UIControlsFactory.createLabel(group, "Size:", "labelccomboSize"); //$NON-NLS-1$ //$NON-NLS-2$
		final CCombo ccomboSize = UIControlsFactory.createCCombo(group);
		hFillFactory.applyTo(ccomboSize);
		addUIControl(ccomboSize, "ccomboSize"); //$NON-NLS-1$

		UIControlsFactory.createLabel(group, "Type:", "labelchoiceType"); //$NON-NLS-1$ //$NON-NLS-2$
		ChoiceComposite choiceType = new ChoiceComposite(group, SWT.BORDER, false);
		choiceType.setOrientation(SWT.HORIZONTAL);
		addUIControl(choiceType, "choiceType"); //$NON-NLS-1$

		UIControlsFactory.createLabel(group, "Flavor:", "labelchoiceFlavor"); //$NON-NLS-1$ //$NON-NLS-2$
		ChoiceComposite choiceFlavor = new ChoiceComposite(group, SWT.BORDER, true);
		choiceFlavor.setOrientation(SWT.HORIZONTAL);
		addUIControl(choiceFlavor, "choiceFlavor"); //$NON-NLS-1$

		Label lblReviewed = UIControlsFactory.createLabel(group, "Reviewed by:", "labellistPersons"); //$NON-NLS-1$ //$NON-NLS-2$
		GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.BEGINNING).applyTo(lblReviewed);
		Composite cmpReviewed = createComposite(group, 3, true);

		List listPersons = UIControlsFactory.createList(cmpReviewed, false, true);
		int hHint = UIControlsFactory.getHeightHint(listPersons, 5);
		hFillFactory.hint(150, hHint).applyTo(listPersons);
		addUIControl(listPersons, "listPersons"); //$NON-NLS-1$

		Table tablePersons = UIControlsFactory.createTable(cmpReviewed, SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		tablePersons.setLinesVisible(true);
		TableColumn tac1 = new TableColumn(tablePersons, SWT.NONE);
		tac1.setWidth(100);
		TableColumn tac2 = new TableColumn(tablePersons, SWT.NONE);
		tac2.setWidth(70);
		hFillFactory.hint(170, hHint).applyTo(tablePersons);
		addUIControl(tablePersons, "tablePersons"); //$NON-NLS-1$

		CompositeTable compTable = UIControlsFactory.createCompositeTable(cmpReviewed, SWT.BORDER);
		new Header(compTable, SWT.NONE);
		new Row(compTable, SWT.NONE);
		compTable.setRunTime(true);
		addUIControl(compTable, "compTable"); //$NON-NLS-1$
		hFillFactory.hint(200, hHint).applyTo(compTable);

		UIControlsFactory.createLabel(group, ""); //$NON-NLS-1$
		Composite cmpReviewed2 = createComposite(group, 3, true);

		Tree treePersons = UIControlsFactory.createTree(cmpReviewed2, SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION);
		hFillFactory.hint(150, hHint).applyTo(treePersons);
		addUIControl(treePersons, "treePersons"); //$NON-NLS-1$

		Tree treeWCols = UIControlsFactory.createTree(cmpReviewed2, SWT.V_SCROLL | SWT.BORDER | SWT.FULL_SELECTION
				| SWT.MULTI);
		treeWCols.setLinesVisible(true);
		treeWCols.setHeaderVisible(true);
		TreeColumn trc1 = new TreeColumn(treeWCols, SWT.NONE);
		trc1.setWidth(120);
		TreeColumn trc2 = new TreeColumn(treeWCols, SWT.NONE);
		trc2.setWidth(40);
		hFillFactory.hint(200, hHint).applyTo(treeWCols);
		addUIControl(treeWCols, "treeWCols"); //$NON-NLS-1$

		UIControlsFactory.createLabel(cmpReviewed2, ""); //$NON-NLS-1$

		UIControlsFactory.createLabel(group, "Buttons:", "labelbuttonToggle"); //$NON-NLS-1$ //$NON-NLS-2$
		Composite cmpButtons = createComposite(group, 5, false);
		Button buttonToggle = UIControlsFactory.createButtonToggle(cmpButtons);
		addUIControl(buttonToggle, "buttonToggle"); //$NON-NLS-1$
		Button buttonPush = UIControlsFactory.createButton(cmpButtons);
		buttonPush.setText("Push"); //$NON-NLS-1$
		addUIControl(buttonPush, "buttonPush"); //$NON-NLS-1$
		Button buttonRadioA = UIControlsFactory.createButtonRadio(cmpButtons);
		buttonRadioA.setText("Radio A"); //$NON-NLS-1$
		addUIControl(buttonRadioA, "buttonRadioA"); //$NON-NLS-1$
		Button buttonRadioB = UIControlsFactory.createButtonRadio(cmpButtons);
		buttonRadioB.setText("Radio B"); //$NON-NLS-1$
		addUIControl(buttonRadioB, "buttonRadioB"); //$NON-NLS-1$
		Button buttonCheck = UIControlsFactory.createButtonCheck(cmpButtons);
		buttonCheck.setText("Check"); //$NON-NLS-1$
		addUIControl(buttonCheck, "buttonCheck"); //$NON-NLS-1$

		return group;
	}

	private Composite createComposite(Group group, int numColumns, boolean equalWidth) {
		Composite composite = new Composite(group, SWT.NONE);
		composite.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		GridLayoutFactory.fillDefaults().numColumns(numColumns).equalWidth(equalWidth).spacing(10, 0)
				.applyTo(composite);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(composite);
		return composite;
	}

	private GridLayout createGridLayout(int numColumns) {
		GridLayout layout = new GridLayout(numColumns, false);
		layout.marginWidth = 20;
		layout.marginHeight = 20;
		return layout;
	}

	// helping classes
	//////////////////

	/**
	 * A header control for {@link CompositeTable}.
	 */
	private static final class Header extends AbstractNativeHeader {
		public Header(Composite parent, int style) {
			super(parent, style);
			setWeights(new int[] { 100, 100 });
			setColumnText(new String[] { "Last Name", "First Name" }); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * A row control with two text fields used by {@link CompositeTable}.
	 */
	private static final class Row extends Composite implements IComplexComponent {
		private final Text txtLast;
		private final Text txtFirst;

		public Row(Composite parent, int style) {
			super(parent, style);
			this.setLayout(new ResizableGridRowLayout());
			txtLast = new Text(this, SWT.BORDER);
			SWTBindingPropertyLocator.getInstance().setBindingProperty(txtLast, "txtLast"); //$NON-NLS-1$
			txtFirst = new Text(this, SWT.BORDER);
			SWTBindingPropertyLocator.getInstance().setBindingProperty(txtFirst, "txtFirst"); //$NON-NLS-1$
		}

		public java.util.List<Object> getUIControls() {
			return Arrays.asList(new Object[] { txtLast, txtFirst });
		}
	}

}
