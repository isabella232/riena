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
package org.eclipse.riena.ui.swt.lnf.renderer;

import org.eclipse.riena.tests.RienaTestCase;
import org.eclipse.riena.tests.collect.UITestCase;
import org.eclipse.riena.ui.swt.lnf.FontLnfResource;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;
import org.eclipse.riena.ui.swt.utils.ImageUtil;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

/**
 * Tests of the class {@link DialogTitleBarRenderer}.
 */
@UITestCase
public class DialogTitleBarRendererTest extends RienaTestCase {

	private final static String PLUGIN_ID = "org.eclipse.riena.tests:";
	private final static String ICON_ECLIPSE = PLUGIN_ID + "/icons/eclipse.gif";

	private OpenDialogTitleBarRenderer renderer;
	private Shell shell;
	private GC gc;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		renderer = new OpenDialogTitleBarRenderer();
		shell = new Shell();
		renderer.setShell(shell);
		gc = new GC(shell);
		renderer.setBounds(0, 0, 100, 100);
	}

	@Override
	protected void tearDown() throws Exception {
		SwtUtilities.disposeWidget(shell);
		gc = null;
		renderer.dispose();
		renderer = null;

		super.tearDown();
	}

	/**
	 * Tests the method {@code paintTitle}.
	 */
	public void testPaintTitle() {

		DialogLnf lnf = new DialogLnf();
		LnfManager.setLnf(lnf);
		lnf.setHideOsBorder(true);
		shell.setText("Hello");

		Rectangle bounds = renderer.paintTitle(gc);
		assertFalse(bounds.equals(new Rectangle(0, 0, 0, 0)));

		shell.setText("");
		bounds = renderer.paintTitle(gc);
		assertEquals(new Rectangle(0, 0, 0, 0), bounds);

		shell.setText("Hello");
		lnf.setHideOsBorder(false);
		bounds = renderer.paintTitle(gc);
		assertEquals(new Rectangle(0, 0, 0, 0), bounds);

	}

	/**
	 * Tests the method {@code paintImage}.
	 */
	public void testPaintImage() {

		DialogLnf lnf = new DialogLnf();
		LnfManager.setLnf(lnf);
		lnf.setHideOsBorder(true);
		Image image = ImageUtil.getImage(ICON_ECLIPSE);
		shell.setImage(image);

		Rectangle bounds = renderer.paintImage(gc);
		assertFalse(bounds.equals(new Rectangle(0, 0, 0, 0)));
		assertEquals(image.getImageData().width, bounds.width);
		assertEquals(image.getImageData().height, bounds.height);

		lnf.setHideOsBorder(false);
		bounds = renderer.paintImage(gc);
		assertEquals(new Rectangle(0, 0, 0, 0), bounds);

		lnf.setHideOsBorder(true);
		shell.setImage(null);
		bounds = renderer.paintImage(gc);
		assertEquals(new Rectangle(0, 0, 0, 0), bounds);

	}

	/**
	 * This look and feel sets the font of the dialog and some setting can be
	 * changed for testing.
	 */
	private static class DialogLnf extends RienaDefaultLnf {

		@Override
		protected void initFontDefaults() {
			super.initFontDefaults();
			getResourceTable().put(ILnfKeyConstants.DIALOG_FONT, new FontLnfResource("Arial", 12, SWT.NONE));
		}

		public void setHideOsBorder(boolean hide) {
			getSettingTable().put(ILnfKeyConstants.DIALOG_HIDE_OS_BORDER, hide);
		}

	}

	/**
	 * This class changes the visibility of some method for testing and
	 * implements the abstract methods.
	 */
	private static class OpenDialogTitleBarRenderer extends DialogTitleBarRenderer {

		@Override
		public Rectangle paintTitle(GC gc) {
			return super.paintTitle(gc);
		}

		@Override
		public Rectangle paintImage(GC gc) {
			return super.paintImage(gc);
		}

	}

}
