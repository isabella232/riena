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

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.riena.example.client.controllers.FocusableViewController;
import org.eclipse.riena.internal.example.client.utils.UIControlsFactory;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleNodeView;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

/**
 * Example for the setFocusable property.
 * 
 * @see IRidget#setFocusable(boolean)
 */
public class FocusableView extends SubModuleNodeView<FocusableViewController> {

	public static final String ID = FocusableView.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		parent.setLayout(new GridLayout(1, false));

		GridDataFactory fillFactory = GridDataFactory.fillDefaults();

		Group groupVisibility = createVisibilityGroup(parent);
		fillFactory.applyTo(groupVisibility);

		Group groupA = createGroup(parent, "A", 5);
		fillFactory.applyTo(groupA);

		Group groupB = createGroup(parent, "B", 1);
		fillFactory.applyTo(groupB);
	}

	@Override
	protected FocusableViewController createController(ISubModuleNode subModuleNode) {
		return new FocusableViewController(subModuleNode);
	}

	// helping methods
	// ////////////////

	private Group createVisibilityGroup(Composite parent) {
		Group group = UIControlsFactory.createGroup(parent, "Visibility Options:");
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(2).applyTo(group);

		Button checkVisible = UIControlsFactory.createButtonCheck(group);
		addUIControl(checkVisible, "checkVisible"); //$NON-NLS-1$

		return group;
	}

	private Group createGroup(Composite parent, String groupCaption, int numElements) {
		Group group = UIControlsFactory.createGroup(parent, "Group #&" + groupCaption);
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(2).applyTo(group);

		Assert.isLegal(numElements > 0);
		for (int i = 0; i < numElements; i++) {
			String id = groupCaption + i;

			Button checkFocus = UIControlsFactory.createButtonCheck(group);
			addUIControl(checkFocus, "button" + id); //$NON-NLS-1$

			Text text = new Text(group, SWT.BORDER | SWT.SINGLE);
			addUIControl(text, "text" + id); //$NON-NLS-1$
		}

		return group;
	}
}