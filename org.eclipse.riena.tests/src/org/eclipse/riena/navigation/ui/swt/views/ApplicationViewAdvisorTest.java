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

import junit.framework.TestCase;

import org.easymock.EasyMock;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.internal.navigation.ui.swt.IAdvisorFactory;
import org.eclipse.riena.navigation.model.ApplicationNode;
import org.eclipse.riena.navigation.ui.controllers.ApplicationController;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class <code>ApplicationViewAdvisor</code>.
 */
@UITestCase
public class ApplicationViewAdvisorTest extends TestCase {

	private ApplicationViewAdvisor advisor;
	private IWorkbenchWindowConfigurer winConfig;
	private ApplicationNode applicationNode;
	private ApplicationController controller;

	@Override
	protected void setUp() throws Exception {
		winConfig = EasyMock.createNiceMock(IWorkbenchWindowConfigurer.class);
		applicationNode = new ApplicationNode();
		controller = new ApplicationController(applicationNode);
		IAdvisorFactory factory = EasyMock.createMock(IAdvisorFactory.class);
		advisor = new ApplicationViewAdvisor(winConfig, controller, factory);
	}

	@Override
	protected void tearDown() throws Exception {
		applicationNode = null;
		controller = null;
		winConfig = null;
		advisor.dispose();
		advisor = null;
	}

	/**
	 * Tests the method <code>initShell</code>.
	 */
	public void testInitShell() {
		Shell shell = new Shell();
		assertNotSame(SWT.INHERIT_FORCE, shell.getBackgroundMode());
		Point defMinSize = shell.getMinimumSize();

		ReflectionUtils.invokeHidden(advisor, "initShell", shell);

		assertFalse(defMinSize.equals(shell.getMinimumSize()));
		assertNotNull(shell.getData(SWTBindingPropertyLocator.BINDING_PROPERTY));

		SwtUtilities.disposeWidget(shell);
	}

	/**
	 * Tests the <i>private</i> method <code>initApplicationSize</code>.
	 */
	public void testInitApplicationSize() {
		System.setProperty("riena.application.width", "9000");
		System.setProperty("riena.application.height", "8000");
		EasyMock.reset(winConfig);
		winConfig.setInitialSize(new Point(9000, 8000));
		EasyMock.replay(winConfig);
		ReflectionUtils.invokeHidden(advisor, "initApplicationSize", winConfig);
		EasyMock.verify(winConfig);

		System.setProperty("riena.application.width", "90");
		System.setProperty("riena.application.height", "80");
		EasyMock.reset(winConfig);
		winConfig.setInitialSize(new Point(800, 600));
		EasyMock.replay(winConfig);
		ReflectionUtils.invokeHidden(advisor, "initApplicationSize", winConfig);
		EasyMock.verify(winConfig);
	}

	/**
	 * Tests that an IAdvisorFactory is used to create the ActionBarAdvisor.
	 */
	public void testInvokesAdvisorFactory() {
		IActionBarConfigurer actionConfig = EasyMock.createNiceMock(IActionBarConfigurer.class);
		ActionBarAdvisor actionAdvisor = new ActionBarAdvisor(actionConfig);

		IAdvisorFactory factory = EasyMock.createMock(IAdvisorFactory.class);
		EasyMock.expect(factory.createActionBarAdvisor(actionConfig)).andReturn(actionAdvisor);
		EasyMock.replay(factory);

		advisor = new ApplicationViewAdvisor(winConfig, controller, factory);
		advisor.createActionBarAdvisor(actionConfig);

		EasyMock.verify(factory);
	}
}
