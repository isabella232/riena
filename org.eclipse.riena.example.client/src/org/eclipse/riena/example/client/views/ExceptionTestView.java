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
		localNullPointer.setText("throw NullPointerException"); //$NON-NLS-1$
		localNullPointer.setBounds(48, 94, 246, 31);
		addUIControl(localNullPointer, "localNullPointerAction"); //$NON-NLS-1$

		final Label testingVariousExceptionsLabel = new Label(container, SWT.NONE);
		testingVariousExceptionsLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		testingVariousExceptionsLabel.setText("Testing various Exceptions"); //$NON-NLS-1$
		testingVariousExceptionsLabel.setBounds(26, 34, 173, 13);

		final Button uiprocessNullPointer1 = new Button(container, SWT.NONE);
		uiprocessNullPointer1.setText("throw NullPointerException in UIProcess (runJob)"); //$NON-NLS-1$
		uiprocessNullPointer1.setBounds(49, 149, 261, 23);
		addUIControl(uiprocessNullPointer1, "uiprocessNullPointerActionRunJob"); //$NON-NLS-1$

		final Button uiprocessNullPointer2 = new Button(container, SWT.NONE);
		uiprocessNullPointer2.setText("throw NullPointerException in UIProcess (finalUpdateUI)"); //$NON-NLS-1$
		uiprocessNullPointer2.setBounds(48, 188, 288, 23);
		addUIControl(uiprocessNullPointer2, "uiprocessNullPointerActionFinalUpdateUI"); //$NON-NLS-1$

		final Button uiprocessNullPointer3 = new Button(container, SWT.NONE);
		uiprocessNullPointer3.setText("throw NullPointerException in UIProcess (updateUI)"); //$NON-NLS-1$
		uiprocessNullPointer3.setBounds(48, 228, 288, 23);
		addUIControl(uiprocessNullPointer3, "uiprocessNullPointerActionUpdateUI"); //$NON-NLS-1$

		final Button serverNullPointer = new Button(container, SWT.NONE);
		serverNullPointer.setText("throw NullPointerException on server"); //$NON-NLS-1$
		serverNullPointer.setBounds(48, 411, 246, 31);
		parent.setLayout(new FillLayout());
		addUIControl(serverNullPointer, "serverNullPointerAction"); //$NON-NLS-1$
	}

}
