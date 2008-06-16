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
import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Text;

/**
 * SWT control factory used in the example views
 */
public final class UIControlsFactory {

	private UIControlsFactory() {
		// prevent instantiation
	}

	public static Label createLabel(Composite parent, String caption) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(caption);
		label.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		return label;
	}

	public static Label createLabelOutput(Composite parent) {
		Label label = new Label(parent, SWT.BORDER);
		label.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		return label;
	}

	public static Text createText(Composite parent) {
		return new Text(parent, SWT.SINGLE | SWT.BORDER);
	}

	public static Text createTextNumeric(Composite parent) {
		return new Text(parent, SWT.SINGLE | SWT.BORDER | SWT.RIGHT);
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
		button.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		return button;
	}

	public static Button createButtonRadio(Composite parent) {
		Button button = new Button(parent, SWT.RADIO);
		button.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		return button;
	}

	public static Combo createCombo(Composite parent) {
		return new Combo(parent, SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
	}

	public static Composite createComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		return composite;
	}

	public static Group createGroup(Composite parent, String caption) {
		Group group = new Group(parent, SWT.NONE);
		group.setText(caption);
		group.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
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

	public static int getHeightHint(List list, int numItems) {
		Assert.isLegal(numItems > 0, "numItems must be greater than 0"); //$NON-NLS-1$
		int items = list.getItemHeight() * numItems;
		return items;
	}

}
