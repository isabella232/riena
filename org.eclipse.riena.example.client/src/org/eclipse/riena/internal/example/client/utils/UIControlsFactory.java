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
package org.eclipse.riena.internal.example.client.utils;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

/**
 * SWT control factory used in the example views
 */
public final class UIControlsFactory {

	private static final Color SHARED_BG_COLOR;

	static {
		SHARED_BG_COLOR = LnfManager.getLnf().getColor(ILnfKeyConstants.SUB_MODULE_BACKGROUND);
		Assert.isNotNull(SHARED_BG_COLOR);
	}

	private UIControlsFactory() {
		// prevent instantiation
	}

	public static Label createLabel(Composite parent, String caption) {
		return createLabel(parent, caption, SWT.NONE);
	}

	public static Label createLabel(Composite parent, String caption, int style) {
		Label label = new Label(parent, style);
		label.setText(caption);
		label.setBackground(SHARED_BG_COLOR);
		return label;
	}

	public static Text createText(Composite parent) {
		return new Text(parent, SWT.SINGLE | SWT.BORDER);
	}

	public static Text createText(Composite parent, int style) {
		return new Text(parent, style | SWT.BORDER);
	}

	public static Text createTextNumeric(Composite parent) {
		Text result = new Text(parent, SWT.SINGLE | SWT.BORDER | SWT.RIGHT);
		// TODO [ev] refactor - can't have magic keys here
		result.setData("type", "numeric"); //$NON-NLS-1$ //$NON-NLS-2$
		return result;
	}

	public static Text createTextOutput(Composite parent) {
		Text text = new Text(parent, SWT.BORDER | SWT.READ_ONLY);
		text.setEditable(false);
		text.setEnabled(false);
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

	public static Button createButton(Composite parent) {
		return new Button(parent, SWT.PUSH);
	}

	public static Button createButtonToggle(Composite parent) {
		return new Button(parent, SWT.TOGGLE);
	}

	public static Button createButtonCheck(Composite parent) {
		Button button = new Button(parent, SWT.CHECK);
		button.setBackground(SHARED_BG_COLOR);
		return button;
	}

	public static Button createButtonRadio(Composite parent) {
		Button button = new Button(parent, SWT.RADIO);
		button.setBackground(SHARED_BG_COLOR);
		return button;
	}

	public static Combo createCombo(Composite parent) {
		return new Combo(parent, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
	}

	public static Composite createComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setBackground(SHARED_BG_COLOR);
		return composite;
	}

	public static Group createGroup(Composite parent, String caption) {
		Group group = new Group(parent, SWT.NONE);
		group.setText(caption);
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
