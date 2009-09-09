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
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ActiveShellExpression;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.statushandlers.AbstractStatusHandler;
import org.eclipse.ui.statushandlers.StatusAdapter;

import org.eclipse.riena.core.exception.IExceptionHandlerManager;
import org.eclipse.riena.core.service.Service;
import org.eclipse.riena.internal.navigation.ui.swt.IAdvisorFactory;
import org.eclipse.riena.navigation.ui.controllers.ApplicationController;
import org.eclipse.riena.navigation.ui.swt.presentation.stack.TitlelessStackPresentationFactory;

public class ApplicationAdvisor extends WorkbenchAdvisor {

	private final ApplicationController controller;
	private final IAdvisorFactory advisorFactory;

	/**
	 * @noreference This constructor is not intended to be referenced by
	 *              clients.
	 */
	public ApplicationAdvisor(ApplicationController controller, IAdvisorFactory factory) {
		this.controller = controller;
		this.advisorFactory = factory;
	}

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		configurer.setPresentationFactory(new TitlelessStackPresentationFactory());
		return new ApplicationViewAdvisor(configurer, controller, advisorFactory);
	}

	@Override
	public String getInitialWindowPerspectiveId() {
		return null;
	}

	@Override
	public synchronized AbstractStatusHandler getWorkbenchErrorHandler() {
		return new AbstractStatusHandler() {
			@Override
			public void handle(StatusAdapter statusAdapter, int style) {
				Service.get(IExceptionHandlerManager.class).handleException(statusAdapter.getStatus().getException(),
						statusAdapter.getStatus().getMessage());
			}
		};
	}

	@Override
	public void postStartup() {
		overwriteStandardHandlers();
	}

	// helping methods
	//////////////////

	/**
	 * Rebinds certain commands that are by bound to handlers in org.eclipse.ui
	 * to a NOOP-Handler.
	 * 
	 * @see http://bugs.eclipse.org/269855
	 */
	private void overwriteStandardHandlers() {
		IWorkbench workbench = getWorkbenchConfigurer().getWorkbench();
		Shell shell = workbench.getActiveWorkbenchWindow().getShell();
		IHandlerService service = (IHandlerService) workbench.getService(IHandlerService.class);
		IHandler handler = new AbstractHandler() {
			public Object execute(ExecutionEvent event) throws ExecutionException {
				// Do nothing by design
				return null;
			}
		};
		String[] commandIds = { "org.eclipse.ui.newWizard", "org.eclipse.ui.window.previousView", //$NON-NLS-1$ //$NON-NLS-2$
				"org.eclipse.ui.window.nextView", "org.eclipse.ui.window.nextPerspective", //$NON-NLS-1$ //$NON-NLS-2$
				"org.eclipse.ui.window.previousPerspective", }; //$NON-NLS-1$
		for (String id : commandIds) {
			service.activateHandler(id, handler, new ActiveShellExpression(shell));
		}
	}
}
