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
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.riena.internal.core.exceptionmanager.ExceptionHandlerManagerAccessor;
import org.eclipse.riena.navigation.ui.controllers.ApplicationController;
import org.eclipse.riena.navigation.ui.swt.presentation.stack.TitlelessStackPresentationFactory;

import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.statushandlers.AbstractStatusHandler;
import org.eclipse.ui.statushandlers.StatusAdapter;

public class ApplicationAdvisor extends WorkbenchAdvisor {

	private ApplicationController controller;

	public ApplicationAdvisor(ApplicationController controller) {
		this.controller = controller;
	}

	/**
	 * @see org.eclipse.ui.application.WorkbenchAdvisor#createWorkbenchWindowAdvisor(org.eclipse.ui.application.IWorkbenchWindowConfigurer)
	 */
	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		TitlelessStackPresentationFactory workbenchPresentationFactory = new TitlelessStackPresentationFactory();
		configurer.setPresentationFactory(workbenchPresentationFactory);
		return new ApplicationViewAdvisor(configurer, controller);
	}

	/**
	 * @see org.eclipse.ui.application.WorkbenchAdvisor#getInitialWindowPerspectiveId()
	 */
	@Override
	public String getInitialWindowPerspectiveId() {
		return null;
	}

	@Override
	public synchronized AbstractStatusHandler getWorkbenchErrorHandler() {
		return new AbstractStatusHandler() {

			@Override
			public void handle(StatusAdapter statusAdapter, int style) {
				ExceptionHandlerManagerAccessor.getExceptionHandlerManager().handleCaught(
						statusAdapter.getStatus().getException(), statusAdapter.getStatus().getMessage());
			}
		};
	}

}
