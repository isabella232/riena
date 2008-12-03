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

import org.eclipse.riena.example.client.controllers.ExceptionTestController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.swtdesigner.SWTResourceManager;

/**
 *
 */
public class ExceptionTestView extends SubModuleView<ExceptionTestController> {

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.navigation.ui.swt.views.SubModuleView#
	 * basicCreatePartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void basicCreatePartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setBackground(SWTResourceManager.getColor(255, 255, 255));

		final Button localNullPointer = new Button(container, SWT.NONE);
		localNullPointer.setText("throw NullPointerException");
		localNullPointer.setBounds(48, 94, 246, 31);
		addUIControl(localNullPointer, "localNullPointerAction");

		final Label testingVariousExceptionsLabel = new Label(container, SWT.NONE);
		testingVariousExceptionsLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		testingVariousExceptionsLabel.setText("Testing various Exceptions");
		testingVariousExceptionsLabel.setBounds(26, 34, 173, 13);

		final Button uiprocessNullPointer = new Button(container, SWT.NONE);
		uiprocessNullPointer.setText("throw NullPointerException in UIProcess");
		uiprocessNullPointer.setBounds(49, 149, 245, 31);
		addUIControl(uiprocessNullPointer, "uiprocessNullPointerAction");

		final Button serverNullPointer = new Button(container, SWT.NONE);
		serverNullPointer.setText("throw NullPointerException on server");
		serverNullPointer.setBounds(48, 218, 246, 31);
		parent.setLayout(new FillLayout());
		addUIControl(serverNullPointer, "serverNullPointerAction");

	}

	private void initializeToolBar() {
		IToolBarManager toolBarManager = getViewSite().getActionBars().getToolBarManager();
	}

}
