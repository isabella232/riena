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
import org.eclipse.riena.core.exception.ExceptionFailure;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.ui.application.AbstractApplication;
import org.eclipse.riena.navigation.ui.controllers.ApplicationController;
import org.eclipse.riena.navigation.ui.swt.views.ApplicationAdvisor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

/**
 * Creates and starts an empty swt application Subclass to create own model or
 * controller.
 */
public abstract class SwtApplication extends AbstractApplication {

	private static String LOGIN_NON_ACTIVITY_DURATION = "riena.loginNonActivityDuration"; //$NON-NLS-1$

	private LoginNonActivityTimer loginNonActivityTimer;

	/**
	 * @see org.eclipse.riena.navigation.ui.application.AbstractApplication#createView(org.eclipse.equinox.app.IApplicationContext,
	 *      org.eclipse.riena.navigation.IApplicationNode)
	 */
	@Override
	public Object createView(IApplicationContext context, IApplicationNode pNode) {
		Display display = PlatformUI.createDisplay();
		try {
			ApplicationAdvisor advisor = new ApplicationAdvisor(createApplicationController(pNode));
			initializeLoginNonActivityTimer(display, context);
			int returnCode = PlatformUI.createAndRunWorkbench(display, advisor);
			if (returnCode == PlatformUI.RETURN_RESTART) {
				return IApplication.EXIT_RESTART;
			}
			return IApplication.EXIT_OK;
		} finally {
			display.dispose();
		}
	}

	protected ApplicationController createApplicationController(IApplicationNode pModel) {
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

	private void initializeLoginNonActivityTimer(Display display, IApplicationContext context) {

		if (isUseLoginNonActivityTimer()) {
			loginNonActivityTimer = new LoginNonActivityTimer(display, context, getLoginNonActivityDuration());
			loginNonActivityTimer.schedule();
		}
	}

	private boolean isUseLoginNonActivityTimer() {
		return getLoginNonActivityDuration() > 0;
	}

	/*
	 * Return the duration of non activity in the application, after which the
	 * login dialog is presented to the user again for a duration greater than
	 * 0. For a duration equal or less than 0 the login timer is not used at
	 * all. The property is specified as VM argument:
	 * -Driena.loginNonActivityDuration=x, where x measured in milliseconds.
	 * 
	 * @return the duration or -1, if system property does not exist.
	 */
	private int getLoginNonActivityDuration() {
		try {
			return Integer.parseInt(System.getProperty(LOGIN_NON_ACTIVITY_DURATION));
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	private class LoginNonActivityTimer implements Runnable {

		private Display display;
		private IApplicationContext context;
		private EventListener eventListener;
		private int nonActivityDuration;

		private LoginNonActivityTimer(Display display, IApplicationContext context, int nonActivityDuration) {
			super();

			this.display = display;
			this.context = context;
			this.nonActivityDuration = nonActivityDuration;
			initializeEventListener();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			display.syncExec(new Runnable() {
				public void run() {
					try {
						if (eventListener.activity) {
							schedule();
							return;
						}

						prePerformLogin(context);
						postPerformLogin(context, performLogin(context));
					} catch (Exception e) {
						throw new ExceptionFailure(e.getLocalizedMessage(), e);
					}
				}
			});
		}

		private void initializeEventListener() {

			eventListener = new EventListener();
			// TODO: which filters are really needed?
			display.addFilter(SWT.KeyDown, eventListener);
			display.addFilter(SWT.KeyUp, eventListener);
			display.addFilter(SWT.MouseDoubleClick, eventListener);
			display.addFilter(SWT.MouseDown, eventListener);
			display.addFilter(SWT.MouseEnter, eventListener);
			display.addFilter(SWT.MouseExit, eventListener);
			display.addFilter(SWT.MouseHover, eventListener);
			display.addFilter(SWT.MouseMove, eventListener);
			display.addFilter(SWT.MouseUp, eventListener);
			display.addFilter(SWT.MouseWheel, eventListener);
		}

		private void schedule() {
			eventListener.activity = false;
			display.timerExec(nonActivityDuration, this);
		}

		private class EventListener implements Listener {

			private boolean activity;

			private EventListener() {
				activity = false;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.
			 * swt.widgets.Event)
			 */
			public void handleEvent(Event event) {
				activity = true;
			}
		}
	}

	protected void prePerformLogin(IApplicationContext context) {
		// To minimize the workbench and show the login dialog later, the workbench has be made first invisible and then minimized.
		getWorkbenchShell().setVisible(false);
		getWorkbenchShell().setMinimized(true);
		// Make workbench visible to be shown as minimized in the (windows) task bar.
		getWorkbenchShell().setVisible(true);
	}

	protected void postPerformLogin(IApplicationContext context, Object result) {
		if (!EXIT_OK.equals(result)) {
			PlatformUI.getWorkbench().close();
		} else {
			getWorkbenchShell().setMinimized(false);
			loginNonActivityTimer.schedule();
		}
	}

	private Shell getWorkbenchShell() {

		// TODO: copied from org.eclipse.riena.navigation.ui.swt.views.DialogView (implement only once)
		if (PlatformUI.isWorkbenchRunning()) {
			return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		} else {
			return null;
		}
	}
}
