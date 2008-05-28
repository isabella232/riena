/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.sample.app.client.helloworld.views;

import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.swt.views.SWTBindingPropertyLocator;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleNodeView;
import org.eclipse.riena.sample.app.client.helloworld.controllers.HelloWorldSubModuleController;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * Very simple sub module view, that displays only a label with the text "Hello
 * World!".
 * 
 * @author schenkel
 */
public class HelloWorldSubModuleView extends SubModuleNodeView<HelloWorldSubModuleController> {

	public static final String ID = "org.eclipse.riena.sample.app.client.HelloWorldSubModuleView";

	/**
	 * Add a label with the text "Hello World!" to the view.
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void basicCreatePartControl(Composite parent) {
		parent.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		final Label helloLabel = new Label(parent, SWT.CENTER);
		helloLabel.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));

		FormLayout layout = new FormLayout();
		parent.setLayout(layout);
		helloLabel.setData(SWTBindingPropertyLocator.BINDING_PROPERTY, "labelRidget");
		addUIControl(helloLabel);

		// layout
		FormData data = new FormData();
		data.height = 20;
		data.width = 90;
		data.top = new FormAttachment(0, 25);
		data.left = new FormAttachment(0, 5);
		helloLabel.setLayoutData(data);
	}

	@Override
	protected HelloWorldSubModuleController createController(ISubModuleNode subModuleNode) {
		return new HelloWorldSubModuleController(subModuleNode);
	}

}
