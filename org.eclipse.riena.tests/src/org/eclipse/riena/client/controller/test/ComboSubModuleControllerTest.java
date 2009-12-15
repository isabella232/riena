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

import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.beans.common.PersonFactory;
import org.eclipse.riena.example.client.controllers.ComboSubModuleController;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;

/**
 * Tests for the ComboSubModuleController.
 */
@NonUITestCase
public class ComboSubModuleControllerTest extends AbstractSubModuleControllerTest<ComboSubModuleController> {

	@Override
	protected ComboSubModuleController createController(ISubModuleNode node) {
		return new ComboSubModuleController(node);
	}

	public void testJohnDoe() {

		IComboRidget comboOne = controller.getRidget(IComboRidget.class, "comboOne"); //$NON-NLS-1$
		comboOne.setSelection(0);

		Person expected = PersonFactory.createPersonList().get(0);
		assertEquals(((Person) comboOne.getSelection()).getFirstname(), expected.getFirstname());
		assertEquals(((Person) comboOne.getSelection()).getLastname(), expected.getLastname());

		ITextRidget textFirst = controller.getRidget(ITextRidget.class, "textFirst"); //$NON-NLS-1$
		ITextRidget textLast = controller.getRidget(ITextRidget.class, "textLast"); //$NON-NLS-1$

		assertEquals(textFirst.getText(), expected.getFirstname());
		assertEquals(textLast.getText(), expected.getLastname());
	}

	public void testNoSelection() {

		IComboRidget comboOne = controller.getRidget(IComboRidget.class, "comboOne"); //$NON-NLS-1$
		comboOne.setSelection(null);
		assertEquals(-1, comboOne.getSelectionIndex());
		assertEquals(null, comboOne.getSelection());
	}
}