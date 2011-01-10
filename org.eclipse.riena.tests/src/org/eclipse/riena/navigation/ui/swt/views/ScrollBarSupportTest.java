/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Tests of the class {@link ScrollBarSupport}.
 */
@UITestCase
public class ScrollBarSupportTest extends TestCase {

	private Shell shell;
	private ScrolledComposite scrolledComposite;
	private Composite content;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		shell = new Shell();
		scrolledComposite = new ScrolledComposite(shell, SWT.VERTICAL);
		content = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(content);
	}

	@Override
	protected void tearDown() throws Exception {
		SwtUtilities.dispose(shell);
		super.tearDown();
	}

	/**
	 * Tests the <i>protected</i> method {@code scrollUp(int)}.
	 */
	public void testScrollUp() {
		final ScrollBarSupport support = new ScrollBarSupport(scrolledComposite,
				new MockModuleNavigationComponentProvider());
		Point origin = scrolledComposite.getOrigin();
		assertEquals(0, origin.x);
		assertEquals(0, origin.y);

		ReflectionUtils.invokeHidden(support, "scrollDown", 30);
		origin = scrolledComposite.getOrigin();
		assertEquals(0, origin.x);
		assertEquals(30, origin.y);

		ReflectionUtils.invokeHidden(support, "scrollUp", 20);
		origin = scrolledComposite.getOrigin();
		assertEquals(0, origin.x);
		assertEquals(10, origin.y);

		ReflectionUtils.invokeHidden(support, "scrollUp", 20);
		origin = scrolledComposite.getOrigin();
		assertEquals(0, origin.x);
		assertEquals(0, origin.y);

	}

	/**
	 * Tests the <i>protected</i> method {@code scrollDown(int)}.
	 */
	public void testScrollDonw() {
		final ScrollBarSupport support = new ScrollBarSupport(scrolledComposite,
				new MockModuleNavigationComponentProvider());
		Point origin = scrolledComposite.getOrigin();
		assertEquals(0, origin.x);
		assertEquals(0, origin.y);

		ReflectionUtils.invokeHidden(support, "scrollDown", 30);
		origin = scrolledComposite.getOrigin();
		assertEquals(0, origin.x);
		assertEquals(30, origin.y);

		ReflectionUtils.invokeHidden(support, "scrollDown", 20);
		origin = scrolledComposite.getOrigin();
		assertEquals(0, origin.x);
		assertEquals(50, origin.y);

		ReflectionUtils.invokeHidden(support, "scrollDown", 20);
		origin = scrolledComposite.getOrigin();
		assertEquals(0, origin.x);
		assertEquals(70, origin.y);

	}

	private class MockModuleNavigationComponentProvider implements IModuleNavigationComponentProvider {

		public Composite getNavigationComponent() {
			return shell;
		}

		public Composite getScrolledComponent() {
			return null;
		}

		public int calculateBounds() {
			return 0;
		}

		public ModuleGroupView getModuleGroupViewForNode(final IModuleGroupNode moduleGroupNode) {
			return null;
		}

		public ModuleView getModuleViewForNode(final IModuleNode moduleGroupNode) {
			return null;
		}

		public IModuleGroupNode getActiveModuleGroupNode() {
			return null;
		}

		public ISubApplicationNode getSubApplicationNode() {
			return null;
		}

	}

}
