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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.ui.swt.MasterDetailsComposite;
import org.eclipse.riena.ui.swt.MessageBox;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;

/**
 * TODO [ev] docs + method docs
 * 
 * @wbp.factory
 */
public final class UIControlsFactory {

	public static final String KEY_TYPE = "type"; //$NON-NLS-1$
	public static final String TYPE_NUMERIC = "numeric"; //$NON-NLS-1$
	public static final String TYPE_DECIMAL = "decimal"; //$NON-NLS-1$
	public static final String TYPE_DATE = "date"; //$NON-NLS-1$

	private static final Color SHARED_BG_COLOR;

	static {
		SHARED_BG_COLOR = LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND);
		Assert.isNotNull(SHARED_BG_COLOR);
	}

	private UIControlsFactory() {
		// prevent instantiation
	}

	/**
	 * @wbp.factory.parameter.source caption "myLabelText"
	 */
	public static Label createLabel(Composite parent, String caption) {
		return createLabel(parent, caption, SWT.NONE);
	}

	/**
	 * @wbp.factory.parameter.source caption "myLabelText"
	 * @wbp.factory.parameter.source style org.eclipse.swt.SWT.NONE
	 */
	public static Label createLabel(Composite parent, String caption, int style) {
		Label label = new Label(parent, style);
		label.setText(caption);
		label.setBackground(SHARED_BG_COLOR);
		return label;
	}

	/**
	 * @wbp.factory.parameter.source caption "myLabelText"
	 * @wbp.factory.parameter.source style org.eclipse.swt.SWT.NONE
	 * @wbp.factory.parameter.source bindingId "myLabelId"
	 */
	public static Label createLabel(Composite parent, String caption, int style, String bindingId) {
		Label label = new Label(parent, style);
		label.setText(caption);
		label.setBackground(SHARED_BG_COLOR);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(label, bindingId);
		return label;
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
	 * @wbp.factory.parameter.source bindingId "MyTextDateId"
	 */
	public static Text createTextDate(Composite parent, String bindingId) {
		Text result = new Text(parent, SWT.SINGLE | SWT.BORDER | SWT.RIGHT);
		result.setData(KEY_TYPE, TYPE_DATE);
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
		Text result = new Text(parent, SWT.SINGLE | SWT.BORDER | SWT.RIGHT);
		result.setData(KEY_TYPE, TYPE_DECIMAL);
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
		Text result = new Text(parent, SWT.SINGLE | SWT.BORDER | SWT.RIGHT);
		result.setData(KEY_TYPE, TYPE_NUMERIC);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(result, bindingId);
		return result;
	}

	public static Text createTextOutput(Composite parent) {
		Text text = new Text(parent, SWT.BORDER | SWT.READ_ONLY);
		text.setEditable(false);
		text.setEnabled(false);
		return text;
	}

	/**
	 * @wbp.factory.parameter.source bindingId "myTextOutputId"
	 */
	public static Text createTextOutput(Composite parent, String bindingId) {
		Text text = new Text(parent, SWT.BORDER | SWT.READ_ONLY);
		text.setEditable(false);
		text.setEnabled(false);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(text, bindingId);
		return text;
	}

	public static Text createTextMulti(Composite parent, boolean hScroll, boolean vScroll) {
		int style = SWT.MULTI | SWT.BORDER;
		if (hScroll) {
			style |= SWT.H_SCROLL;
		}
		if (vScroll) {
			style |= SWT.V_SCROLL;
		}
		return new Text(parent, style);
	}

	/**
	 * @wbp.factory.parameter.source bindingId "myTextMultiId"
	 */
	public static Text createTextMulti(Composite parent, boolean hScroll, boolean vScroll, String bindingId) {
		int style = SWT.MULTI | SWT.BORDER;
		if (hScroll) {
			style |= SWT.H_SCROLL;
		}
		if (vScroll) {
			style |= SWT.V_SCROLL;
		}
		Text text = new Text(parent, style);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(text, bindingId);
		return text;
	}

	public static Button createButton(Composite parent) {
		return new Button(parent, SWT.PUSH);
	}

	/**
	 * @wbp.factory.parameter.source caption "myButtonText"
	 */
	public static Button createButton(Composite parent, String caption) {
		Button result = new Button(parent, SWT.PUSH);
		result.setText(caption);

		return result;
	}

	/**
	 * @wbp.factory.parameter.source caption "myButtonText"
	 * @wbp.factory.parameter.source bindingId "myButtonId"
	 */
	public static Button createButton(Composite parent, String caption, String bindingId) {
		Button result = new Button(parent, SWT.PUSH);
		result.setText(caption);
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
		Button button = new Button(parent, SWT.TOGGLE);
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
		Button button = new Button(parent, SWT.CHECK);
		button.setBackground(SHARED_BG_COLOR);
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
		Button button = new Button(parent, SWT.RADIO);
		button.setBackground(SHARED_BG_COLOR);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(button, bindingId);
		return button;
	}

	public static Combo createCombo(Composite parent) {
		return new Combo(parent, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
	}

	/**
	 * @wbp.factory.parameter.source bindingId "myComboId"
	 */
	public static Combo createCombo(Composite parent, String bindingId) {
		Combo combo = new Combo(parent, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
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

	public static Group createGroup(Composite parent, String caption) {
		Group group = new Group(parent, SWT.NONE);
		group.setText(caption);
		group.setBackground(SHARED_BG_COLOR);
		return group;
	}

	/**
	 * @wbp.factory.parameter.source caption "myGroupText"
	 * @wbp.factory.parameter.source bindingId "myGroupId"
	 */
	public static Group createGroup(Composite parent, String caption, String bindingId) {
		Group group = new Group(parent, SWT.NONE);
		group.setText(caption);
		group.setBackground(SHARED_BG_COLOR);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(group, bindingId);
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
		int style = SWT.BORDER | SWT.MULTI;
		if (hScroll) {
			style |= SWT.H_SCROLL;
		}
		if (vScroll) {
			style |= SWT.V_SCROLL;
		}
		List list = new List(parent, style);
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

	public static Shell createShell(Display display) {
		Assert.isNotNull(display);
		Shell shell = new Shell(display);
		shell.setBackground(SHARED_BG_COLOR);
		return shell;
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

	public static MessageBox createMessageBox(Composite parent) {
		return new MessageBox(parent);
	}

}
