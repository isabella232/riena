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
package org.eclipse.riena.internal.ui.ridgets.swt;

import junit.framework.TestCase;

import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * Tests of the class {@link AbstractItemProperties}.
 */
public class AbstractItemPropertiesTest extends TestCase {

	private Shell shell;

	@Override
	protected void setUp() throws Exception {
		shell = new Shell();
	}

	@Override
	protected void tearDown() throws Exception {
		SwtUtilities.disposeWidget(shell);
	}

	/**
	 * Tests the constructor of {@code AbstractItemProperties}.
	 */
	public void testItemProperties() {

		ToolItemRidget ridget = new ToolItemRidget();
		ToolBar toolbar = new ToolBar(shell, SWT.BORDER);
		ToolItem item = new ToolItem(toolbar, SWT.PUSH);
		ridget.setUIControl(item);

		ItemProperties itemProperties = new ItemProperties(ridget);
		int style = itemProperties.getStyle();
		assertEquals(SWT.PUSH, style);
		AbstractItemRidget retRidget = itemProperties.getRidget();
		assertSame(ridget, retRidget);

	}

	/**
	 * Tests the method {@code setAllProperties}.
	 */
	public void testSetAllProperties() {

		ToolItemRidget ridget = new ToolItemRidget();
		ToolBar toolbar = new ToolBar(shell, SWT.BORDER);
		ToolItem item = new ToolItem(toolbar, SWT.PUSH);
		SWTBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();
		String id = "id4711";
		locator.setBindingProperty(item, id);
		item.setText("Text4711");
		item.setData(new Object());
		ridget.setUIControl(item);

		ItemProperties itemProperties = new ItemProperties(ridget);
		ToolItem item2 = new ToolItem(toolbar, SWT.PUSH);
		itemProperties.setAllProperties(item2);
		String retId = locator.locateBindingProperty(item2);
		assertEquals(id, retId);
		assertEquals(item.getText(), item2.getText());
		assertSame(item.getData(), item2.getData());

	}

	private class ItemProperties extends AbstractItemProperties {

		public ItemProperties(AbstractItemRidget ridget) {
			super(ridget);
		}

		@Override
		Item createItem() {
			return null;
		}

		@Override
		public void setAllProperties(Item item) {
			super.setAllProperties(item);
		}

		@Override
		public int getStyle() {
			return super.getStyle();
		}

		@Override
		public AbstractItemRidget getRidget() {
			return super.getRidget();
		}

	}

}
