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
package org.eclipse.riena.example.client.application;

import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.internal.example.client.Activator;
import org.eclipse.riena.navigation.IApplicationModel;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.model.ApplicationModel;
import org.eclipse.riena.navigation.model.NavigationNodeId;
import org.eclipse.riena.navigation.model.SubApplicationNode;
import org.eclipse.riena.navigation.ui.controllers.ApplicationController;
import org.eclipse.riena.navigation.ui.swt.application.SwtApplication;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManager;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManagerAccessor;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;
import org.osgi.framework.Bundle;

/**
 * Define the model of the application
 */
public class SwtExampleApplication extends SwtApplication {

	/**
	 * Creates a new instance of <code>SwtExampleApplication</code> and set the
	 * look and feel, if a class for the look and feel is given.
	 */
	@SuppressWarnings("unchecked")
	public SwtExampleApplication() {

		super();

		String lnfClassName = System.getProperty("riena.lnf", ""); //$NON-NLS-1$ //$NON-NLS-2$
		if (!StringUtils.isEmpty(lnfClassName)) {
			try {
				Class lnfClass = this.getBundle().loadClass(lnfClassName);
				RienaDefaultLnf lnf;
				lnf = (RienaDefaultLnf) lnfClass.newInstance();
				LnfManager.setLnf(lnf);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.application.SwtApplication#
	 *      createApplicationViewController
	 *      (org.eclipse.riena.navigation.IApplicationModel)
	 */
	@Override
	protected ApplicationController createApplicationViewController(IApplicationModel model) {
		ApplicationController controller = super.createApplicationViewController(model);
		controller.setMenubarVisible(true);
		return controller;
	}

	@Override
	protected IApplicationModel createModel() {

		SubApplicationNode subApplication = null;
		IModuleGroupNode moduleGroup = null;

		SwtPresentationManager presentation = SwtPresentationManagerAccessor.getManager();

		final ApplicationModel applicationModel = new ApplicationModel("Riena Navigation Example"); //$NON-NLS-1$
		applicationModel.setPresentationId(new NavigationNodeId("application"));
		applicationModel.setIcon(createIconPath(IExampleIcons.ICON_APPLICATION));

		// Navigation SubApplication
		applicationModel.create(new NavigationNodeId("org.eclipse.riena.example.navigation"));

		applicationModel.create(new NavigationNodeId("org.eclipse.riena.example.navigate"));

		// Playground SubApplication
		subApplication = new SubApplicationNode("Playground"); //$NON-NLS-1$
		subApplication.setPresentationId(new NavigationNodeId("playground")); //$NON-NLS-1$
		subApplication.setIcon(createIconPath(IExampleIcons.ICON_SAMPLE));
		presentation.present(subApplication, "subapplication.2"); //$NON-NLS-1$
		applicationModel.addChild(subApplication);

		// shared view demo
		applicationModel.create(new NavigationNodeId("org.eclipse.riena.example.sharedViews"));

		// uiProcess demo
		applicationModel.create(new NavigationNodeId("org.eclipse.riena.example.uiProcesses"));

		applicationModel.create(new NavigationNodeId("org.eclipse.riena.example.playground"));

		return applicationModel;
	}

	@Override
	protected Bundle getBundle() {
		return Activator.getDefault().getBundle();
	}

}
