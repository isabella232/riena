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
package org.eclipse.riena.navigation.ui.swt.login;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.riena.beans.common.IntegerBean;
import org.eclipse.riena.navigation.ui.swt.views.AbstractControlledView;
import org.eclipse.riena.ui.ridgets.controller.AbstractWindowController;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * The abstract view for the login splash dialog.
 */
public abstract class AbstractLoginSplashView extends AbstractControlledView<AbstractWindowController> implements
		ILoginSplashView {

	protected IntegerBean result;
	private Control view;

	public AbstractLoginSplashView() {
		result = new IntegerBean(IApplication.EXIT_OK);
	}

	protected abstract Control buildView(Composite parent);

	protected abstract AbstractWindowController createController();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.navigation.ui.swt.login.ILoginSplashView#build(org.
	 * eclipse.swt.widgets.Composite)
	 */
	public void build(Composite parent) {

		if (view == null) {
			view = buildView(parent);
		}
		createAndBindController();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.internal.navigation.ui.login.ILoginDialogView#getResult
	 * ()
	 */
	public int getResult() {
		return result.getValue();
	}

	private void createAndBindController() {

		AbstractWindowController controller = createController();
		initialize(controller);
		bind(controller);
	}
}
