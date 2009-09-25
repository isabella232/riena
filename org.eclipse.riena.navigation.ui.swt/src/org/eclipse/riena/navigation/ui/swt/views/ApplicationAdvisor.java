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

import java.io.IOException;

import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;
import org.eclipse.jface.bindings.Scheme;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.keys.IBindingService;
import org.eclipse.ui.statushandlers.AbstractStatusHandler;
import org.eclipse.ui.statushandlers.StatusAdapter;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.exception.IExceptionHandlerManager;
import org.eclipse.riena.core.service.Service;
import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.internal.navigation.ui.swt.Activator;
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
		WorkbenchWindowAdvisor workbenchWindowAdvisor = new ApplicationViewAdvisor(configurer, controller,
				advisorFactory);
		Wire.instance(workbenchWindowAdvisor).andStart(Activator.getDefault().getContext());
		return workbenchWindowAdvisor;
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
		installDefaultBinding();
	}

	// helping methods
	//////////////////

	private void installDefaultBinding() {
		IBindingService bindingService = (IBindingService) PlatformUI.getWorkbench().getService(IBindingService.class);
		String scheme = advisorFactory.getKeyScheme();
		Scheme rienaScheme = bindingService.getScheme(scheme);
		try {
			// saving will activate (!) the scheme:
			bindingService.savePreferences(rienaScheme, bindingService.getBindings());
		} catch (IOException ioe) {
			Logger logger = Log4r.getLogger(Activator.getDefault(), this.getClass());
			logger.log(LogService.LOG_ERROR, "Could not activate scheme: " + scheme, ioe); //$NON-NLS-1$
		}
	}

}
