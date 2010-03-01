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

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.swt.ChoiceComposite;
import org.eclipse.riena.ui.swt.MasterDetailsComposite;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Example view with as many widgets as possible. Used for controller testing.
 */
public class ControllerTestsPlaygroundSubModuleView extends SubModuleView {
	public ControllerTestsPlaygroundSubModuleView() {
	}

	public static final Object ID = ControllerTestsPlaygroundSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setLayout(new FillLayout());

		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setLayout(new FillLayout());
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);

		Composite composite = UIControlsFactory.createComposite(scrolledComposite);
		GridLayoutFactory.swtDefaults().numColumns(2).applyTo(composite);

		Label label = UIControlsFactory.createLabel(composite,
				"Lots of widgets for lots of ridgets for lots of controller tests.", //$NON-NLS-1$
				SWT.NONE);
		GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.TOP).span(2, 1).applyTo(label);

		Group tableGroup = UIControlsFactory.createGroup(composite, "table"); //$NON-NLS-1$

		createTableGroup(tableGroup);

		Group comboGroup = UIControlsFactory.createGroup(composite, "combo"); //$NON-NLS-1$
		GridLayoutFactory.swtDefaults().applyTo(comboGroup);
		GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.TOP).applyTo(comboGroup);
		createComboGroup(comboGroup);

		Group browserGroup = UIControlsFactory.createGroup(composite, "browser and link"); //$NON-NLS-1$
		GridLayoutFactory.swtDefaults().numColumns(2).applyTo(browserGroup);
		GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.TOP).applyTo(browserGroup);
		createBrowserGroup(browserGroup);

		Group spinnerScaleGroup = UIControlsFactory.createGroup(composite, "spinner and scale"); //$NON-NLS-1$
		GridLayoutFactory.swtDefaults().numColumns(2).applyTo(spinnerScaleGroup);
		GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.TOP).applyTo(spinnerScaleGroup);
		createSpinnerScaleGroup(spinnerScaleGroup);

		Group masterDetailsGroup = UIControlsFactory.createGroup(composite, "masterDetails"); //$NON-NLS-1$
		GridLayoutFactory.swtDefaults().applyTo(masterDetailsGroup);
		GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.TOP).applyTo(masterDetailsGroup);
		createMasterDetailsGroup(masterDetailsGroup);

		Group dateTimeGroup = UIControlsFactory.createGroup(composite, "dateTime"); //$NON-NLS-1$
		GridLayoutFactory.swtDefaults().applyTo(dateTimeGroup);
		GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.TOP).applyTo(dateTimeGroup);
		createDateTimeGroup(dateTimeGroup);

		Group imageButtonGroup = UIControlsFactory.createGroup(composite, "buttons"); //$NON-NLS-1$
		GridLayoutFactory.swtDefaults().applyTo(imageButtonGroup);
		GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.TOP).applyTo(imageButtonGroup);
		createImageButtonGroup(imageButtonGroup);

		// TODO work in progress

		scrolledComposite.setContent(composite);
		scrolledComposite.setMinSize(650, 1500);
	}

	/**
	 * @param imageButtonGroup
	 */
	private void createImageButtonGroup(Group parent) {
		UIControlsFactory.createImageButton(parent, SWT.NONE, "imageButton"); //$NON-NLS-1$
		UIControlsFactory.createImageButton(parent, SWT.NONE, "arrowButton"); //$NON-NLS-1$
		UIControlsFactory.createImageButton(parent, SWT.HOT, "arrowHotButton"); //$NON-NLS-1$
	}

	private void createDateTimeGroup(Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));

		GridDataFactory gdf = GridDataFactory.fillDefaults().grab(true, false);
		Group group1 = createGroupDT(parent, "dtDate", "dtTime", "txt1", SWT.MEDIUM); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		group1.setText("DateTime #1"); //$NON-NLS-1$
		gdf.applyTo(group1);

		Group group2 = createGroupDT(parent, "dtDateOnly", null, "txt2", SWT.LONG); //$NON-NLS-1$ //$NON-NLS-2$
		group2.setText("DateTime #2"); //$NON-NLS-1$
		gdf.applyTo(group2);

		Group group3 = createGroupDT(parent, null, "dtTimeOnly", "txt3", SWT.SHORT); //$NON-NLS-1$ //$NON-NLS-2$
		group3.setText("DateTime #3"); //$NON-NLS-1$
		gdf.applyTo(group3);

		Group group4 = createGroupDT(parent, "dtCal", null, "txt4", SWT.CALENDAR); //$NON-NLS-1$ //$NON-NLS-2$
		group4.setText("DateTime #4"); //$NON-NLS-1$
		gdf.applyTo(group4);

		UIControlsFactory.createTextDate(parent, "dateText"); //$NON-NLS-1$
		UIControlsFactory.createButton(parent, "", "dateTimeButton"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private void createMasterDetailsGroup(Composite parent) {

		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));

		Composite composite = new Composite(parent, SWT.NONE);
		GridData data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = SWT.FILL;
		composite.setLayoutData(data);

		FillLayout layout = new FillLayout(SWT.HORIZONTAL);
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		composite.setLayout(layout);
		createMasterDetails(composite);

		UIControlsFactory.createButton(parent, "enable/disable", "enableDisable"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * @param parent
	 */
	private void createSpinnerScaleGroup(Composite parent) {
		Label lblKelvin = new Label(parent, SWT.NONE);
		lblKelvin.setText("Kelvin:"); //$NON-NLS-1$

		Slider kelvinSlider = new Slider(parent, SWT.BORDER);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(kelvinSlider, "kelvinSlider"); //$NON-NLS-1$

		Label lblDegreesFahrenheit = new Label(parent, SWT.NONE);
		lblDegreesFahrenheit.setText("Degrees Fahrenheit:"); //$NON-NLS-1$

		Spinner fahrenheitSpinner = new Spinner(parent, SWT.BORDER);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(fahrenheitSpinner, "fahrenheitSpinner"); //$NON-NLS-1$

		Label lblDegreeCelsius = new Label(parent, SWT.NONE);
		lblDegreeCelsius.setText("Degree Celsius:"); //$NON-NLS-1$

		Scale celsiusScale = new Scale(parent, SWT.NONE);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(celsiusScale, "celsiusScale"); //$NON-NLS-1$
		new Label(parent, SWT.NONE);

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		composite.setLayout(new GridLayout(3, false));

		Label lblZero = new Label(composite, SWT.NONE);
		lblZero.setText("0\u00B0"); //$NON-NLS-1$

		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));

		Label lblPlus50 = new Label(composite, SWT.NONE);
		lblPlus50.setText("50\u00B0"); //$NON-NLS-1$

	}

	/**
	 * @param parent
	 */
	private void createTableGroup(Composite parent) {
		GridLayoutFactory.swtDefaults().numColumns(3).applyTo(parent);
		GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.TOP).applyTo(parent);

		Table multiTable = UIControlsFactory.createTable(parent, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION,
				"multiTable"); //$NON-NLS-1$
		GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.FILL).grab(true, false).applyTo(multiTable);
		//		multiTable.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));

		List tableList = UIControlsFactory.createList(parent, false, false, "tableList"); //$NON-NLS-1$
		//		tableList.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.FILL).grab(true, false).applyTo(tableList);

		Button toggleButton = UIControlsFactory.createButtonToggle(parent, "toggleButton"); //$NON-NLS-1$
		toggleButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));

		Button tableButton = UIControlsFactory.createButton(parent, "copy selection", "copySelectionButton"); //$NON-NLS-1$ //$NON-NLS-2$
		tableButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		UIControlsFactory.createLabel(parent, "", "doubleClickLabel"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * @param comboGroup
	 */
	private void createComboGroup(Composite parent) {
		Label comboLabel = UIControlsFactory.createLabel(parent, "", SWT.NONE, "comboLabel"); //$NON-NLS-1$ //$NON-NLS-2$
		comboLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(parent, SWT.NONE);

		Combo combo = UIControlsFactory.createCombo(parent, "ageCombo"); //$NON-NLS-1$
		combo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
		CCombo cCombo = UIControlsFactory.createCCombo(parent, "ageCCombo"); //$NON-NLS-1$
		cCombo.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));

		UIControlsFactory.createText(parent, SWT.BORDER, "comboTextField"); //$NON-NLS-1$

		Button addToComboButton = UIControlsFactory.createButton(parent, "add", "addToComboButton"); //$NON-NLS-1$ //$NON-NLS-2$
		addToComboButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
	}

	private void createBrowserGroup(Composite parent) {
		GridLayoutFactory.swtDefaults().numColumns(2).equalWidth(false).margins(20, 20).spacing(20, 5).applyTo(parent);
		UIControlsFactory.createLabel(parent, "Links:"); //$NON-NLS-1$
		UIControlsFactory.createLink(parent, SWT.NONE, "link1"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		UIControlsFactory.createLink(parent, SWT.NONE, "link2"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		UIControlsFactory.createLink(parent, SWT.NONE, "link3"); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$
		UIControlsFactory.createLabel(parent, ""); //$NON-NLS-1$

		UIControlsFactory.createLabel(parent, "URL:"); //$NON-NLS-1$
		Text textLinkUrl = UIControlsFactory.createText(parent, SWT.SINGLE, "textLinkUrl"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().grab(true, false).applyTo(textLinkUrl);

		Label label = UIControlsFactory.createLabel(parent, "Browser:"); //$NON-NLS-1$
		GridDataFactory.fillDefaults().grab(false, true).applyTo(label);
		Browser browser = UIControlsFactory.createBrowser(parent, SWT.NONE, "browser"); //$NON-NLS-1$
		GridData gd_browser = new GridData(SWT.LEFT, SWT.FILL, true, false, 1, 1);
		gd_browser.widthHint = 150;
		gd_browser.heightHint = 50;
		browser.setLayoutData(gd_browser);
	}

	// helping methods
	//////////////////

	private Group createMasterDetails(Composite parent) {
		Group result = UIControlsFactory.createGroup(parent, "Master/Details:"); //$NON-NLS-1$
		FillLayout layout = new FillLayout(SWT.HORIZONTAL);
		layout.marginHeight = 20;
		layout.marginWidth = 20;
		result.setLayout(layout);

		MasterDetailsComposite mdComposite = new MasterDetailsComposite(result, SWT.NONE, SWT.BOTTOM);
		Composite details = mdComposite.getDetails();
		GridLayout layout2 = new GridLayout(2, false);
		details.setLayout(layout2);

		UIControlsFactory.createLabel(details, "First Name:"); //$NON-NLS-1$
		Text txtFirst = UIControlsFactory.createText(details, SWT.BORDER, "first"); //$NON-NLS-1$
		txtFirst.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		UIControlsFactory.createLabel(details, "Last Name:"); //$NON-NLS-1$
		Text txtLast = UIControlsFactory.createText(details, SWT.BORDER, "last"); //$NON-NLS-1$
		txtLast.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

		UIControlsFactory.createLabel(details, "Gender:"); //$NON-NLS-1$
		ChoiceComposite ccGender = new ChoiceComposite(details, SWT.NONE, false);
		ccGender.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		ccGender.setOrientation(SWT.HORIZONTAL);
		mdComposite.addUIControl(ccGender, "gender"); //$NON-NLS-1$

		UIControlsFactory.createLabel(details, "Pets:"); //$NON-NLS-1$
		ChoiceComposite ccPets = new ChoiceComposite(details, SWT.NONE, true);
		ccPets.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
		ccPets.setOrientation(SWT.HORIZONTAL);
		mdComposite.addUIControl(ccPets, "pets"); //$NON-NLS-1$

		this.addUIControl(mdComposite, "master"); //$NON-NLS-1$

		//DefaultButtonManager dbm = new DefaultButtonManager(parent.getShell());
		//dbm.addButton(mdComposite.getButtonApply(), mdComposite);

		return result;
	}

	private Group createGroupDT(Composite parent, String dateId, String timeId, String textId, int style) {
		Group group = UIControlsFactory.createGroup(parent, ""); //$NON-NLS-1$
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(2).applyTo(group);
		if (dateId != null) {
			Label label = UIControlsFactory.createLabel(group, "Date:"); //$NON-NLS-1$
			GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.BEGINNING).applyTo(label);
			DateTime dtDate;
			if (style != SWT.CALENDAR) {
				dtDate = UIControlsFactory.createDate(group, style);
			} else {
				dtDate = UIControlsFactory.createCalendar(group);
			}
			addUIControl(dtDate, dateId);
		}

		if (timeId != null) {
			UIControlsFactory.createLabel(group, "Time:"); //$NON-NLS-1$
			DateTime dtTime = UIControlsFactory.createTime(group, style);
			addUIControl(dtTime, timeId);
		}

		UIControlsFactory.createLabel(group, "Model:"); //$NON-NLS-1$
		Text text = UIControlsFactory.createText(group);
		GridDataFactory.fillDefaults().hint(220, SWT.DEFAULT).applyTo(text);
		addUIControl(text, textId);

		return group;
	}
}
