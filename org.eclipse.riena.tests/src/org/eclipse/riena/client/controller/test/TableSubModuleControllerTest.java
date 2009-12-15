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
package org.eclipse.riena.client.controller.test;

import org.eclipse.riena.example.client.controllers.TableSubModuleController;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.internal.ui.ridgets.swt.ActionRidget;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;

/**
 * Tests for the TableSubModuleController.
 */
@NonUITestCase
public class TableSubModuleControllerTest extends AbstractSubModuleControllerTest<TableSubModuleController> {

	@Override
	protected TableSubModuleController createController(ISubModuleNode node) {

		return new TableSubModuleController(node);
	}

	public void testButtonDelete() {

		ITableRidget table = controller.getRidget(ITableRidget.class, "table"); //$NON-NLS-1$
		int count = table.getOptionCount();

		ActionRidget buttonDelete = (ActionRidget) controller.getRidget(IActionRidget.class, "buttonDelete"); //$NON-NLS-1$
		// select and delete one item
		table.setSelection(0);
		// FIXME assertEquals(0, table.getSelectionIndex());
		buttonDelete.fireAction();
		assertEquals(count - 1, table.getOptionCount());

		table.clearSelection();
		assertEquals(-1, table.getSelectionIndex());
		assertTrue(table.getSelection().isEmpty());

		// Fehler: Button nicht diabled -> s. table.updateFromModel(); -> selection changed event getriggert durch
		// tabelviewer
		// assertFalse(buttonDelete.isEnabled());
		// nothing selected -> nothing deleted
		buttonDelete.fireAction();
		assertEquals(count - 1, table.getOptionCount());

		// select and delete another item
		table.setSelection(0);
		assertFalse(table.getSelection().isEmpty());
		buttonDelete.fireAction();
		assertEquals(count - 2, table.getOptionCount());
	}
}