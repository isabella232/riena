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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.navigation.model.ApplicationNode;
import org.eclipse.riena.navigation.ui.controllers.ApplicationController;
import org.eclipse.riena.tests.collect.UITestCase;
import org.eclipse.riena.ui.swt.lnf.ImageLnfResource;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;
import org.eclipse.riena.ui.swt.utils.ImageStore;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class <code>ApplicationViewAdvisor</code>.
 */
@UITestCase
public class ApplicationViewAdvisorTest extends TestCase {

	private final static String ICON_ECLIPSE = "eclipse.gif";

	private ApplicationViewAdvisor advisor;
	private IWorkbenchWindowConfigurer winConfig;
	private ApplicationNode applicationNode;
	private ApplicationController controller;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		winConfig = EasyMock.createNiceMock(IWorkbenchWindowConfigurer.class);
		applicationNode = new ApplicationNode();
		controller = new ApplicationController(applicationNode);
		advisor = new ApplicationViewAdvisor(winConfig, controller);
	}

	/*
	 * @see junit.framework.TestCase#tearDown()
	 */
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
	 * Tests the <i>private</i> method {@code getLogoImage()}.
	 */
	public void testGetLogoImage() {
		RienaDefaultLnf originaLnf = LnfManager.getLnf();

		try {
			MyLnf lnf = new MyLnf();
			LnfManager.setLnf(lnf);
			lnf.initialize();

			Image eclipseImage = ImageStore.getInstance().getImage(ICON_ECLIPSE);
			lnf.setLogo(ICON_ECLIPSE);
			Image logoImage = ReflectionUtils.invokeHidden(advisor, "getLogoImage");
			assertNotNull(logoImage);
			assertEquals(eclipseImage.getBounds().width, logoImage.getBounds().width);
			assertEquals(eclipseImage.getBounds().height, logoImage.getBounds().height);

			Image missingImage = ImageStore.getInstance().getMissingImage();
			lnf.setLogo(ICON_ECLIPSE + "4711");
			logoImage = ReflectionUtils.invokeHidden(advisor, "getLogoImage");
			assertNotNull(logoImage);
			assertEquals(missingImage.getBounds().width, logoImage.getBounds().width);
			assertEquals(missingImage.getBounds().height, logoImage.getBounds().height);
		} finally {
			LnfManager.setLnf(originaLnf);
		}
	}

	/**
	 * Look and Feel where it is possible to change the image of the logo.
	 */
	private static class MyLnf extends RienaDefaultLnf {

		public void setLogo(String logo) {
			getResourceTable().put(LnfKeyConstants.TITLELESS_SHELL_LOGO, new ImageLnfResource(logo));
		}

	}

}
