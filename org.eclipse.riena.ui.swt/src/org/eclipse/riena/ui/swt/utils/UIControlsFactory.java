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
package org.eclipse.riena.ui.swt.utils;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.nebula.widgets.compositetable.CompositeTable;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

import org.eclipse.riena.ui.swt.ChoiceComposite;
import org.eclipse.riena.ui.swt.DatePickerComposite;
import org.eclipse.riena.ui.swt.MasterDetailsComposite;
import org.eclipse.riena.ui.swt.MessageBox;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;

/**
 * TODO [ev] docs + method docs
 * 
 * @wbp.factory
 */
public class UIControlsFactory {

	/**
	 * @since 1.2
	 */
	public static final String KEY_LNF_STYLE = "lnfStyle"; //$NON-NLS-1$
	/**
	 * @since 1.2
	 */
	public static final String LNF_STYLE_SECTION_LABEL = "sectionLabel"; //$NON-NLS-1$
	public static final String KEY_TYPE = "type"; //$NON-NLS-1$
	public static final String TYPE_NUMERIC = "numeric"; //$NON-NLS-1$
	public static final String TYPE_DECIMAL = "decimal"; //$NON-NLS-1$
	public static final String TYPE_DATE = "date"; //$NON-NLS-1$

	/**
	 * @since 1.2
	 */
	protected static final Color SHARED_BG_COLOR;

	static {
		SHARED_BG_COLOR = LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND);
		Assert.isNotNull(SHARED_BG_COLOR);
	}

	/**
	 * @since 1.2
	 */
	protected UIControlsFactory() {
	}

	/**
	 * @wbp.factory.parameter.source text "myMenuItemText"
	 * @wbp.factory.parameter.source style org.eclipse.swt.SWT.NONE
	 * @since 1.2
	 */
	public static MenuItem createMenuItem(Menu parent, String text, int style) {
		MenuItem item = new MenuItem(parent, style);
		item.setText(text);
		return item;
	}

	/**
	 * @wbp.factory.parameter.source text "myMenuItemText"
	 * @since 1.2
	 */
	public static MenuItem createMenuItem(Menu parent, String text) {
		MenuItem item = new MenuItem(parent, SWT.None);
		item.setText(text);
		return item;
	}

	/**
	 * @wbp.factory.parameter.source text "myMenuItemText"
	 * @wbp.factory.parameter.source bindingId "myMenuItemId"
	 * @since 1.2
	 */
	public static MenuItem createMenuItem(Menu parent, String text, String bindingId) {
		MenuItem item = new MenuItem(parent, SWT.None);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(item, bindingId);
		item.setText(text);
		return item;
	}

	/**
	 * @since 1.2
	 */
	public static Menu createMenu(Control parent) {
		Menu item = new Menu(parent);
		return item;
	}

	/**
	 * @since 1.2
	 */
	public static Menu createMenu(MenuItem parent) {
		Menu item = new Menu(parent);
		return item;
	}

	/**
	 * @wbp.factory.parameter.source text "myLabelText"
	 */
	public static Label createLabel(Composite parent, String text) {
		return createLabel(parent, text, SWT.NONE);
	}

	/**
	 * @wbp.factory.parameter.source text "myLabelText"
	 * @wbp.factory.parameter.source style org.eclipse.swt.SWT.NONE
	 */
	public static Label createLabel(Composite parent, String text, int style) {
		Label label = new Label(parent, style);
		label.setText(text);
		label.setBackground(SHARED_BG_COLOR);
		return label;
	}

	/**
	 * @wbp.factory.parameter.source text "myLabelText"
	 * @wbp.factory.parameter.source style org.eclipse.swt.SWT.NONE
	 * @wbp.factory.parameter.source bindingId "myLabelId"
	 */
	public static Label createLabel(Composite parent, String text, int style, String bindingId) {
		Label label = createLabel(parent, text, style);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(label, bindingId);
		return label;
	}

	/**
	 * @wbp.factory.parameter.source text "myLabelText"
	 * @wbp.factory.parameter.source bindingId "myLabelId"
	 * @since 1.2
	 */
	public static Label createLabel(Composite parent, String text, String bindingId) {
		return createLabel(parent, text, SWT.None, bindingId);
	}

	/**
	 * @since 1.2
	 */
	public static Link createLink(Composite parent) {
		return createLink(parent, SWT.NONE);
	}

	/**
	 * @wbp.factory.parameter.source style org.eclipse.swt.SWT.NONE
	 * @since 1.2
	 */
	public static Link createLink(Composite parent, int style) {
		Link result = new Link(parent, SWT.NONE);
		result.setBackground(SHARED_BG_COLOR);
		return result;
	}

	/**
	 * @wbp.factory.parameter.source style org.eclipse.swt.SWT.NONE
	 * @wbp.factory.parameter.source bindingId "myLinkId"
	 * @since 1.2
	 */
	public static Link createLink(Composite parent, int style, String bindingId) {
		Link result = createLink(parent, style);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(result, bindingId);
		return result;
	}

	public static Text createText(Composite parent) {
		return new Text(parent, SWT.SINGLE | SWT.BORDER);
	}

	/**
	 * @wbp.factory.parameter.source style org.eclipse.swt.SWT.BORDER
	 */
	public static Text createText(Composite parent, int style) {
		return new Text(parent, style | SWT.BORDER);
	}

	/**
	 * @wbp.factory.parameter.source style org.eclipse.swt.SWT.BORDER
	 * @wbp.factory.parameter.source bindingId "myTextId"
	 */
	public static Text createText(Composite parent, int style, String bindingId) {
		Text text = new Text(parent, style | SWT.BORDER);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(text, bindingId);
		return text;
	}

	public static Text createTextDate(Composite parent) {
		Text result = new Text(parent, SWT.SINGLE | SWT.BORDER | SWT.RIGHT);
		result.setData(KEY_TYPE, TYPE_DATE);
		return result;
	}

	/**
	 * @wbp.factory.parameter.source bindingId "myTextDateId"
	 */
	public static Text createTextDate(Composite parent, String bindingId) {
		Text result = createTextDate(parent);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(result, bindingId);
		return result;
	}

	/**
	 * @since 1.2
	 */
	public static DatePickerComposite createDatePickerComposite(Composite parent) {
		DatePickerComposite result = new DatePickerComposite(parent, SWT.SINGLE | SWT.RIGHT);
		result.setData(KEY_TYPE, TYPE_DATE);
		return result;
	}

	/**
	 * @wbp.factory.parameter.source bindingId "myDatePickerId"
	 * @since 1.2
	 */
	public static DatePickerComposite createDatePickerComposite(Composite parent, String bindingId) {
		DatePickerComposite result = createDatePickerComposite(parent);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(result, bindingId);
		return result;
	}

	public static Text createTextDecimal(Composite parent) {
		Text result = new Text(parent, SWT.SINGLE | SWT.BORDER | SWT.RIGHT);
		result.setData(KEY_TYPE, TYPE_DECIMAL);
		return result;
	}

	/**
	 * @wbp.factory.parameter.source bindingId "myTextDecimalId"
	 */
	public static Text createTextDecimal(Composite parent, String bindingId) {
		Text result = createTextDecimal(parent);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(result, bindingId);
		return result;
	}

	public static Text createTextNumeric(Composite parent) {
		Text result = new Text(parent, SWT.SINGLE | SWT.BORDER | SWT.RIGHT);
		result.setData(KEY_TYPE, TYPE_NUMERIC);
		return result;
	}

	/**
	 * @wbp.factory.parameter.source bindingId "myTextNumericId"
	 */
	public static Text createTextNumeric(Composite parent, String bindingId) {
		Text result = createTextNumeric(parent);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(result, bindingId);
		return result;
	}

	/**
	 * @since 1.2
	 */
	public static Text createTextMultiWrap(Composite parent, boolean hScroll, boolean vScroll) {
		return createTextMulti(parent, SWT.WRAP, hScroll, vScroll);
	}

	public static Text createTextMulti(Composite parent, boolean hScroll, boolean vScroll) {
		return createTextMulti(parent, SWT.NONE, hScroll, vScroll);
	}

	/**
	 * @since 1.2
	 */
	public static Text createTextMulti(Composite parent, int style, boolean hScroll, boolean vScroll) {
		int txStyle = style | SWT.MULTI | SWT.BORDER;
		if (hScroll) {
			txStyle |= SWT.H_SCROLL;
		}
		if (vScroll) {
			txStyle |= SWT.V_SCROLL;
		}
		return new Text(parent, txStyle);
	}

	/**
	 * @wbp.factory.parameter.source bindingId "myTextMultiId"
	 */
	public static Text createTextMulti(Composite parent, boolean hScroll, boolean vScroll, String bindingId) {
		Text text = createTextMulti(parent, hScroll, vScroll);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(text, bindingId);
		return text;
	}

	/**
	 * @since 1.2
	 */
	public static Browser createBrowser(Composite parent, int style) {
		return new Browser(parent, style);
	}

	/**
	 * @wbp.factory.parameter.source style org.eclipse.swt.SWT.NONE
	 * @wbp.factory.parameter.source bindingId "myBrowserId"
	 * @since 1.2
	 */
	public static Browser createBrowser(Composite parent, int style, String bindingId) {
		Browser result = createBrowser(parent, style);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(result, bindingId);
		return result;
	}

	public static Button createButton(Composite parent) {
		return new Button(parent, SWT.PUSH);
	}

	/**
	 * @wbp.factory.parameter.source text "myButtonText"
	 */
	public static Button createButton(Composite parent, String text) {
		Button result = new Button(parent, SWT.PUSH);
		result.setText(text);
		return result;
	}

	/**
	 * @wbp.factory.parameter.source text "myButtonText"
	 * @wbp.factory.parameter.source bindingId "myButtonId"
	 */
	public static Button createButton(Composite parent, String text, String bindingId) {
		Button result = createButton(parent, text);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(result, bindingId);
		return result;
	}

	public static Button createButtonToggle(Composite parent) {
		return new Button(parent, SWT.TOGGLE);
	}

	/**
	 * @wbp.factory.parameter.source bindingId "myButtonToggleId"
	 */
	public static Button createButtonToggle(Composite parent, String bindingId) {
		Button button = createButtonToggle(parent);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(button, bindingId);
		return button;
	}

	public static Button createButtonCheck(Composite parent) {
		Button button = new Button(parent, SWT.CHECK);
		button.setBackground(SHARED_BG_COLOR);
		return button;
	}

	/**
	 * @wbp.factory.parameter.source bindingId "myButtonCheckId"
	 */
	public static Button createButtonCheck(Composite parent, String bindingId) {
		Button button = createButtonCheck(parent);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(button, bindingId);
		return button;
	}

	public static Button createButtonRadio(Composite parent) {
		Button button = new Button(parent, SWT.RADIO);
		button.setBackground(SHARED_BG_COLOR);
		return button;
	}

	/**
	 * @wbp.factory.parameter.source bindingId "myButtonRadioId"
	 */
	public static Button createButtonRadio(Composite parent, String bindingId) {
		Button button = createButtonRadio(parent);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(button, bindingId);
		return button;
	}

	public static DateTime createCalendar(Composite parent) {
		DateTime result = new DateTime(parent, SWT.CALENDAR);
		result.setBackground(SHARED_BG_COLOR);
		return result;
	}

	/**
	 * @wbp.factory.parameter.source bindingId "myCalendarId"
	 */
	public static DateTime createCalendar(Composite parent, String bindingId) {
		DateTime result = createCalendar(parent);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(result, bindingId);
		return result;
	}

	/**
	 * @since 1.2
	 */
	public static CCombo createCCombo(Composite parent) {
		return new CCombo(parent, SWT.BORDER | SWT.READ_ONLY);
	}

	/**
	 * @wbp.factory.parameter.source bindingId "myComboId"
	 * @since 1.2
	 */
	public static CCombo createCCombo(Composite parent, String bindingId) {
		CCombo combo = createCCombo(parent);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(combo, bindingId);
		return combo;
	}

	public static Combo createCombo(Composite parent) {
		return new Combo(parent, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
	}

	/**
	 * @wbp.factory.parameter.source bindingId "myComboId"
	 */
	public static Combo createCombo(Composite parent, String bindingId) {
		Combo combo = createCombo(parent);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(combo, bindingId);
		return combo;
	}

	public static Composite createComposite(Composite parent) {
		return createComposite(parent, SWT.NONE);
	}

	/**
	 * @wbp.factory.parameter.source style org.eclipse.swt.SWT.NONE
	 */
	public static Composite createComposite(Composite parent, int style) {
		Composite composite = new Composite(parent, style);
		composite.setBackground(SHARED_BG_COLOR);
		return composite;
	}

	/**
	 * @wbp.factory.parameter.source style org.eclipse.swt.SWT.NONE
	 */
	public static CompositeTable createCompositeTable(Composite parent, int style) {
		CompositeTable result = new CompositeTable(parent, style);
		result.setBackground(SHARED_BG_COLOR);
		return result;
	}

	/**
	 * @wbp.factory.parameter.source style org.eclipse.swt.SWT.NONE
	 * @wbp.factory.parameter.source multipleSelection false
	 */
	public static ChoiceComposite createChoiceComposite(Composite parent, int style, boolean multipleSelection) {
		return new ChoiceComposite(parent, style, multipleSelection);
	}

	/**
	 * @wbp.factory.parameter.source style org.eclipse.swt.SWT.NONE
	 * @wbp.factory.parameter.source multipleSelection false
	 * @wbp.factory.parameter.source bindingId "myChoiceCompositeId"
	 */
	public static ChoiceComposite createChoiceComposite(Composite parent, int style, boolean multipleSelection,
			String bindingId) {
		ChoiceComposite composite = createChoiceComposite(parent, style, multipleSelection);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(composite, bindingId);
		return composite;
	}

	/**
	 * @wbp.factory.parameter.source style org.eclipse.swt.SWT.MEDIUM
	 */
	public static DateTime createDate(Composite parent, int style) {
		DateTime result = new DateTime(parent, SWT.DATE | SWT.DROP_DOWN | style);
		result.setBackground(SHARED_BG_COLOR);
		return result;
	}

	/**
	 * @wbp.factory.parameter.source style org.eclipse.swt.SWT.MEDIUM
	 * @wbp.factory.parameter.source bindingId "myDateTimeId"
	 */
	public static DateTime createDate(Composite parent, int style, String bindingId) {
		DateTime result = createDate(parent, style);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(result, bindingId);
		return result;
	}

	/**
	 * @wbp.factory.parameter.source text "myGroupText"
	 */
	public static Group createGroup(Composite parent, String text) {
		Group group = new Group(parent, SWT.NONE);
		group.setText(text);
		group.setBackground(SHARED_BG_COLOR);
		return group;
	}

	public static List createList(Composite parent, boolean hScroll, boolean vScroll) {
		int style = SWT.BORDER | SWT.MULTI;
		if (hScroll) {
			style |= SWT.H_SCROLL;
		}
		if (vScroll) {
			style |= SWT.V_SCROLL;
		}
		return new List(parent, style);
	}

	/**
	 * @wbp.factory.parameter.source bindingId "myListId"
	 */
	public static List createList(Composite parent, boolean hScroll, boolean vScroll, String bindingId) {
		List list = createList(parent, hScroll, vScroll);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(list, bindingId);
		return list;
	}

	public static MasterDetailsComposite createMasterDetails(Composite parent) {
		return new MasterDetailsComposite(parent, SWT.NONE);
	}

	/**
	 * @wbp.factory.parameter.source bindingId "myMasterDetailId"
	 */
	public static MasterDetailsComposite createMasterDetails(Composite parent, String bindingId) {
		MasterDetailsComposite masterDetails = new MasterDetailsComposite(parent, SWT.NONE);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(masterDetails, bindingId);
		return masterDetails;
	}

	public static MessageBox createMessageBox(Composite parent) {
		return new MessageBox(parent);
	}

	public static Shell createShell(Display display) {
		Assert.isNotNull(display);
		Shell shell = new Shell(display);
		shell.setBackground(SHARED_BG_COLOR);
		return shell;
	}

	/**
	 * @wbp.factory.parameter.source style org.eclipse.swt.SWT.MEDIUM
	 */
	public static DateTime createTime(Composite parent, int style) {
		DateTime result = new DateTime(parent, SWT.TIME | style);
		result.setBackground(SHARED_BG_COLOR);
		return result;
	}

	/**
	 * @wbp.factory.parameter.source style org.eclipse.swt.SWT.MEDIUM
	 * @wbp.factory.parameter.source bindingId "myDateTimeId"
	 */
	public static DateTime createTime(Composite parent, int style, String bindingId) {
		DateTime result = createTime(parent, style);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(result, bindingId);
		return result;
	}

	/**
	 * @wbp.factory.parameter.source style org.eclipse.swt.SWT.NONE
	 */
	public static Tree createTree(Composite parent, int style) {
		return new Tree(parent, style);
	}

	/**
	 * @wbp.factory.parameter.source style org.eclipse.swt.SWT.NONE
	 * @wbp.factory.parameter.source bindingId "myTreeId"
	 */
	public static Tree createTree(Composite parent, int style, String bindingId) {
		Tree tree = createTree(parent, style);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(tree, bindingId);
		return tree;
	}

	/**
	 * @wbp.factory.parameter.source style org.eclipse.swt.SWT.NONE
	 */
	public static Table createTable(Composite parent, int style) {
		return new Table(parent, style);
	}

	/**
	 * @wbp.factory.parameter.source style org.eclipse.swt.SWT.NONE
	 * @wbp.factory.parameter.source bindingId "myTableId"
	 */
	public static Table createTable(Composite parent, int style, String bindingId) {
		Table table = createTable(parent, style);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(table, bindingId);
		return table;
	}

	public static int getWidthHint(Button button) {
		GC gc = new GC(button.getDisplay());
		try {
			FontMetrics fm = gc.getFontMetrics();
			int widthHint = Dialog.convertHorizontalDLUsToPixels(fm, IDialogConstants.BUTTON_WIDTH);
			Point minSize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
			return Math.max(widthHint, minSize.x);
		} finally {
			gc.dispose();
		}
	}

	public static int getWidthHint(Text text, int numChars) {
		GC gc = new GC(text.getDisplay());
		try {
			FontMetrics fm = gc.getFontMetrics();
			int widthHint = fm.getAverageCharWidth() * numChars;
			Point minSize = text.computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
			return Math.max(widthHint, minSize.x);
		} finally {
			gc.dispose();
		}
	}

	public static int getHeightHint(List list, int numItems) {
		Assert.isLegal(numItems > 0, "numItems must be greater than 0"); //$NON-NLS-1$
		int items = list.getItemHeight() * numItems;
		return items;
	}

}