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
package org.eclipse.riena.navigation.ui.swt.application;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.riena.navigation.IApplicationModel;
import org.eclipse.riena.navigation.ui.application.AbstractApplication;
import org.eclipse.riena.navigation.ui.controllers.ApplicationController;
import org.eclipse.riena.navigation.ui.swt.views.ApplicationAdvisor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

/**
 * Creates and starts an empty swt application Subclass to create own model or
 * controller.
 */
public abstract class SwtApplication extends AbstractApplication {

	/**
	 * @see org.eclipse.riena.navigation.ui.application.AbstractApplication#createView(org.eclipse.equinox.app.IApplicationContext,
	 *      org.eclipse.riena.navigation.IApplicationModel)
	 */
	@Override
	public Object createView(IApplicationContext context, IApplicationModel pModel) {
		Display display = PlatformUI.createDisplay();
		try {
			ApplicationAdvisor advisor = new ApplicationAdvisor(createApplicationViewController(pModel));
			int returnCode = PlatformUI.createAndRunWorkbench(display, advisor);
			if (returnCode == PlatformUI.RETURN_RESTART) {
				return IApplication.EXIT_RESTART;
			}
			return IApplication.EXIT_OK;
		} finally {
			display.dispose();
		}
	}

	protected ApplicationController createApplicationViewController(IApplicationModel pModel) {
		return new ApplicationController(pModel);
	}

	public void stop() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null) {
			return;
		}
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed()) {
					workbench.close();
				}
			}
		});
	}

	protected abstract Bundle getBundle();

	protected String createIconPath(String subPath) {
		Bundle bundle = getBundle();

		if (bundle == null) {
			return null;
		}
		StringBuilder builder = new StringBuilder(bundle.getSymbolicName());
		builder.append(":"); //$NON-NLS-1$
		builder.append(subPath);
		return builder.toString();
	}

}
