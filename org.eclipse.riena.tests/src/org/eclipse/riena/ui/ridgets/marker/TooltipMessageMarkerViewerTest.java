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
package org.eclipse.riena.ui.ridgets.marker;

import junit.framework.TestCase;

import org.eclipse.riena.internal.ui.ridgets.swt.DefaultRealm;
import org.eclipse.riena.internal.ui.ridgets.swt.TextRidget;
import org.eclipse.riena.ui.core.marker.ErrorMessageMarker;
import org.eclipse.riena.ui.core.marker.MessageMarker;
import org.eclipse.swt.widgets.Shell;

/**
 * Tests for the TooltipMessageMarkerViewer.
 */
public class TooltipMessageMarkerViewerTest extends TestCase {

	private DefaultRealm realm;
	private Shell shell;
	private TooltipMessageMarkerViewer tooltipMessageMarkerViewer;
	private TextRidget ridget;
	private String errorMessage;
	private ErrorMessageMarker errorMessageMarker;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		realm = new DefaultRealm();
		shell = new Shell();

		ridget = new TextRidget();

		tooltipMessageMarkerViewer = new TooltipMessageMarkerViewer();
		tooltipMessageMarkerViewer.addRidget(ridget);
		tooltipMessageMarkerViewer.addMarkerType(ErrorMessageMarker.class);

		errorMessage = "TestErrorMessage";
		errorMessageMarker = new ErrorMessageMarker(errorMessage);
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		shell.dispose();
		shell = null;
		realm.dispose();
		realm = null;
		super.tearDown();
	}

	/**
	 * @throws Exception
	 *             Handled by JUnit.
	 */
	public void testSetMessage() throws Exception {

		assertNull(ridget.getToolTipText());

		ridget.addMarker(errorMessageMarker);

		assertEquals(errorMessage, ridget.getToolTipText());

		ridget.removeMarker(errorMessageMarker);

		assertNull(ridget.getToolTipText());
	}

	/**
	 * @throws Exception
	 *             Handled by JUnit.
	 */
	public void testSetMessageExistingTooltip() throws Exception {

		String existingTooltip = "TestExistingTooltip";

		ridget.setToolTipText(existingTooltip);

		ridget.addMarker(errorMessageMarker);

		assertEquals(errorMessage, ridget.getToolTipText());

		ridget.removeMarker(errorMessageMarker);

		assertEquals(existingTooltip, ridget.getToolTipText());
	}

	/**
	 * @throws Exception
	 *             Handled by JUnit.
	 */
	public void testAddRidgetWithMarker() throws Exception {

		TextRidget anotherRidget = new TextRidget();
		anotherRidget.addMarker(errorMessageMarker);

		tooltipMessageMarkerViewer.addRidget(anotherRidget);

		assertEquals(errorMessage, anotherRidget.getToolTipText());

		tooltipMessageMarkerViewer.removeRidget(anotherRidget);

		assertNull(anotherRidget.getToolTipText());
	}

	/**
	 * @throws Exception
	 *             Handled by JUnit.
	 */
	public void testAddMarkerType() throws Exception {

		TextRidget anotherRidget = new TextRidget();
		String anotherMessage = "TestAnotherMessage";
		MessageMarker anotherMarker = new MessageMarker(anotherMessage);
		anotherRidget.addMarker(anotherMarker);

		tooltipMessageMarkerViewer.addRidget(anotherRidget);

		assertNull(anotherRidget.getToolTipText());

		tooltipMessageMarkerViewer.addMarkerType(MessageMarker.class);

		assertEquals(anotherMessage, anotherRidget.getToolTipText());

		tooltipMessageMarkerViewer.removeMarkerType(MessageMarker.class);

		assertNull(anotherRidget.getToolTipText());
	}

}
