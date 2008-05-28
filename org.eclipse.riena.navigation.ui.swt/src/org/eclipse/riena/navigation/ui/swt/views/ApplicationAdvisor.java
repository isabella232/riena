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
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.riena.navigation.ui.controllers.ApplicationViewController;
import org.eclipse.riena.navigation.ui.swt.presentation.stack.TitlelessStackPresentationFactory;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationAdvisor extends WorkbenchAdvisor {

	private ApplicationViewController controller;

	public ApplicationAdvisor(ApplicationViewController controller) {
		this.controller = controller;
	}

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		TitlelessStackPresentationFactory workbenchPresentationFactory = new TitlelessStackPresentationFactory();
		configurer.setPresentationFactory(workbenchPresentationFactory);
		return new ApplicationViewAdvisor(configurer, controller);
	}

	public String getInitialWindowPerspectiveId() {
		return null;
	}

}
