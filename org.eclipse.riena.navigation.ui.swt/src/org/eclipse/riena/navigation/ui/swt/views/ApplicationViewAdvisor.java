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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.riena.navigation.ISubApplication;
import org.eclipse.riena.navigation.ISubApplicationListener;
import org.eclipse.riena.navigation.model.NavigationTreeObserver;
import org.eclipse.riena.navigation.model.SubApplicationAdapter;
import org.eclipse.riena.navigation.ui.controllers.ApplicationViewController;
import org.eclipse.riena.navigation.ui.swt.binding.DefaultSwtControlRidgetMapper;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManagerAccessor;
import org.eclipse.riena.navigation.ui.swt.utils.ImageUtil;
import org.eclipse.riena.ui.ridgets.uibinding.DefaultBindingManager;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationViewAdvisor extends WorkbenchWindowAdvisor {

	private static final String SHELL_RIDGET_PROPERTY = "windowRidget"; //$NON-NLS-1$
	private ApplicationViewController controller;
	private ISubApplicationListener subApplicationListener;
	private NavigationTreeObserver navigationTreeObserver;

	private List<Object> uiControls;

	public ApplicationViewAdvisor(IWorkbenchWindowConfigurer configurer, ApplicationViewController pController) {
		super(configurer);
		uiControls = new ArrayList<Object>();
		controller = pController;
		initializeListener();
	}

	public void addUIControl(Composite control) {
		uiControls.add(control);
	}

	private void initializeListener() {
		subApplicationListener = new SubApplicationListener();
		navigationTreeObserver = new NavigationTreeObserver();
		navigationTreeObserver.addListener(subApplicationListener);
		navigationTreeObserver.addListenerTo(controller.getNavigationNode());
	}

	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return new ApplicationActionAdvisor(configurer, controller);
	}

	public void preWindowOpen() {
		configureView();
	}

	private void configureView() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setShowMenuBar(controller.isMenubarVisible());
		configurer.setShowStatusLine(false);
		configurer.setShowCoolBar(false);
		configurer.setTitle(controller.getNavigationNode().getLabel());
		configurer.setInitialSize(new Point(800, 600));
	}

	@Override
	public void createWindowContents(final Shell shell) {
		shell.setData(SWTBindingPropertyLocator.BINDING_PROPERTY, SHELL_RIDGET_PROPERTY);
		addUIControl(shell);
		shell.setImage(ImageUtil.getImage(controller.getNavigationNode().getIcon()));
		super.createWindowContents(shell);
	}

	/**
	 * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#postWindowOpen()
	 */
	@Override
	public void postWindowOpen() {
		super.postWindowOpen();
		doInitialBinding();
	}

	private void doInitialBinding() {
		DefaultBindingManager defaultBindingManager = createBindingManager();
		defaultBindingManager.injectRidgets(controller, uiControls);
		defaultBindingManager.bind(controller, uiControls);
		controller.afterBind();
	}

	protected DefaultBindingManager createBindingManager() {
		return new DefaultBindingManager(new SWTBindingPropertyLocator(), new DefaultSwtControlRidgetMapper());
	}

	private class SubApplicationListener extends SubApplicationAdapter {

		/**
		 * @see org.eclipse.riena.navigation.model.NavigationTreeAdapter#activated(org.eclipse.riena.navigation.ISubApplication)
		 */
		@Override
		public void activated(ISubApplication source) {
			if (source != null) {
				showPerspective(source);
			}
			super.activated(source);
		}
	}

	private void showPerspective(ISubApplication source) {
		try {
			PlatformUI.getWorkbench().showPerspective(
					SwtPresentationManagerAccessor.getManager().getSwtViewId(source).getId(),
					PlatformUI.getWorkbench().getActiveWorkbenchWindow());

		} catch (WorkbenchException e) {
			e.printStackTrace();
		}
	}

}
