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

import org.easymock.EasyMock;
import org.eclipse.riena.internal.ui.ridgets.swt.DefaultRealm;
import org.eclipse.riena.internal.ui.ridgets.swt.TextRidget;
import org.eclipse.riena.ui.core.marker.ErrorMessageMarker;
import org.eclipse.riena.ui.core.marker.MessageMarker;
import org.eclipse.riena.ui.ridgets.IStatusbarRidget;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Tests for the StatusbarMessageMarkerViewer.
 */
public class StatusbarMessageMarkerViewerTest extends TestCase {

	private static final String EMPTY_STATUSBAR_MESSAGE = "TestEmptyStatusbar";

	private DefaultRealm realm;
	private Shell shell;
	private StatusbarMessageMarkerViewer statusbarMessageMarkerViewer;
	private IStatusbarRidget statusbarRidget;
	private Text text1;
	private Text text2;
	private TextRidget ridget1;
	private TextRidget ridget2;

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		realm = new DefaultRealm();
		shell = new Shell();
		shell.setLayout(new RowLayout(SWT.VERTICAL));

		text1 = new Text(shell, SWT.SINGLE);
		text2 = new Text(shell, SWT.SINGLE);
		ridget1 = new TextRidget();
		ridget2 = new TextRidget();
		ridget1.setUIControl(text1);
		ridget2.setUIControl(text2);

		statusbarRidget = EasyMock.createMock(IStatusbarRidget.class);

		statusbarMessageMarkerViewer = new StatusbarMessageMarkerViewer(statusbarRidget);
		statusbarMessageMarkerViewer.addRidget(ridget1);
		statusbarMessageMarkerViewer.addRidget(ridget2);
		statusbarMessageMarkerViewer.addMarkerType(ErrorMessageMarker.class);

		shell.setSize(100, 100);
		shell.setLocation(0, 0);
		shell.open();
		text1.setFocus();
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		ridget1 = null;
		ridget2 = null;
		text1.dispose();
		text1 = null;
		text2.dispose();
		text2 = null;
		shell.dispose();
		shell = null;
		realm.dispose();
		realm = null;
		super.tearDown();
	}

	public void testHandleFocusEvents() throws Exception {

		String testErrorMessage = "Test Error in Adapter 1";

		EasyMock.expect(statusbarRidget.getMessage()).andReturn(EMPTY_STATUSBAR_MESSAGE);
		statusbarRidget.setMessage(testErrorMessage);
		EasyMock.replay(statusbarRidget);

		ridget1.addMarker(new ErrorMessageMarker(testErrorMessage));

		EasyMock.verify(statusbarRidget);
		EasyMock.reset(statusbarRidget);

		EasyMock.expect(statusbarRidget.getMessage()).andReturn(testErrorMessage);
		statusbarRidget.setMessage(EMPTY_STATUSBAR_MESSAGE);
		EasyMock.replay(statusbarRidget);

		text2.setFocus();

		EasyMock.verify(statusbarRidget);
	}

	public void testHandleFocusEventsAndModifiedMessage() throws Exception {

		String testErrorMessage = "Test Error in Adapter 1";
		String testMessageBySomebodyElse = "Some message by somebody else";
		ErrorMessageMarker errorMessageMarker1 = new ErrorMessageMarker(testErrorMessage);

		EasyMock.expect(statusbarRidget.getMessage()).andReturn(EMPTY_STATUSBAR_MESSAGE);
		statusbarRidget.setMessage(testErrorMessage);
		EasyMock.replay(statusbarRidget);

		ridget1.addMarker(errorMessageMarker1);

		EasyMock.verify(statusbarRidget);
		EasyMock.reset(statusbarRidget);

		// at this point somebody else changes the statusbar

		EasyMock.expect(statusbarRidget.getMessage()).andReturn(testMessageBySomebodyElse);
		EasyMock.replay(statusbarRidget);

		text2.setFocus();

		EasyMock.verify(statusbarRidget);
		EasyMock.reset(statusbarRidget);

		EasyMock.expect(statusbarRidget.getMessage()).andReturn(testMessageBySomebodyElse);
		statusbarRidget.setMessage(testErrorMessage);
		EasyMock.replay(statusbarRidget);

		text1.setFocus();

		EasyMock.verify(statusbarRidget);
		EasyMock.reset(statusbarRidget);

		EasyMock.expect(statusbarRidget.getMessage()).andReturn(testErrorMessage);
		statusbarRidget.setMessage(testMessageBySomebodyElse);
		EasyMock.replay(statusbarRidget);

		ridget1.removeMarker(errorMessageMarker1);

		EasyMock.verify(statusbarRidget);
		EasyMock.reset(statusbarRidget);

		String anotherTestErrorMessage = "Another Test Error in Adapter 1";
		EasyMock.expect(statusbarRidget.getMessage()).andReturn(testMessageBySomebodyElse);
		statusbarRidget.setMessage(anotherTestErrorMessage);
		EasyMock.replay(statusbarRidget);

		ridget1.addMarker(new ErrorMessageMarker(anotherTestErrorMessage));

		EasyMock.verify(statusbarRidget);
		EasyMock.reset(statusbarRidget);

		EasyMock.expect(statusbarRidget.getMessage()).andReturn(anotherTestErrorMessage);
		statusbarRidget.setMessage(testMessageBySomebodyElse);
		EasyMock.replay(statusbarRidget);

		text2.setFocus();

		EasyMock.verify(statusbarRidget);
	}

	public void testRemoveRidget() throws Exception {

		String testErrorMessage = "Test Error in Adapter 1";
		ErrorMessageMarker errorMessageMarker1 = new ErrorMessageMarker(testErrorMessage);
		EasyMock.expect(statusbarRidget.getMessage()).andReturn(EMPTY_STATUSBAR_MESSAGE);
		statusbarRidget.setMessage(testErrorMessage);
		EasyMock.replay(statusbarRidget);
		ridget1.addMarker(errorMessageMarker1);
		text1.setFocus();

		EasyMock.reset(statusbarRidget);
		EasyMock.replay(statusbarRidget);

		statusbarMessageMarkerViewer.removeRidget(ridget2);

		EasyMock.verify(statusbarRidget);
		EasyMock.reset(statusbarRidget);

		EasyMock.expect(statusbarRidget.getMessage()).andReturn(testErrorMessage);
		statusbarRidget.setMessage(EMPTY_STATUSBAR_MESSAGE);
		EasyMock.replay(statusbarRidget);

		statusbarMessageMarkerViewer.removeRidget(ridget1);

		EasyMock.verify(statusbarRidget);
	}

	public void testAddAndRemoveMarkerType() throws Exception {

		EasyMock.replay(statusbarRidget);

		String messageDifferentType = "TestDifferentMarkerType";
		ridget2.addMarker(new MessageMarker(messageDifferentType));

		statusbarMessageMarkerViewer.addMarkerType(MessageMarker.class);

		EasyMock.verify(statusbarRidget);
		EasyMock.reset(statusbarRidget);

		EasyMock.expect(statusbarRidget.getMessage()).andReturn(EMPTY_STATUSBAR_MESSAGE);
		statusbarRidget.setMessage(messageDifferentType);
		EasyMock.replay(statusbarRidget);

		text2.setFocus();

		EasyMock.verify(statusbarRidget);
		EasyMock.reset(statusbarRidget);

		EasyMock.expect(statusbarRidget.getMessage()).andReturn(messageDifferentType);
		statusbarRidget.setMessage(EMPTY_STATUSBAR_MESSAGE);
		EasyMock.replay(statusbarRidget);

		statusbarMessageMarkerViewer.removeMarkerType(MessageMarker.class);

		EasyMock.verify(statusbarRidget);
		EasyMock.reset(statusbarRidget);

		EasyMock.expect(statusbarRidget.getMessage()).andReturn(EMPTY_STATUSBAR_MESSAGE);
		statusbarRidget.setMessage(messageDifferentType);
		EasyMock.replay(statusbarRidget);

		statusbarMessageMarkerViewer.addMarkerType(MessageMarker.class);

		EasyMock.verify(statusbarRidget);
	}

	public void testSetVisible() throws Exception {

		String testErrorMessage = "Test Error in Adapter 1";

		statusbarMessageMarkerViewer.setVisible(false);
		ridget1.addMarker(new ErrorMessageMarker(testErrorMessage));

		EasyMock.expect(statusbarRidget.getMessage()).andReturn(EMPTY_STATUSBAR_MESSAGE);
		statusbarRidget.setMessage(testErrorMessage);
		EasyMock.replay(statusbarRidget);

		statusbarMessageMarkerViewer.setVisible(true);

		EasyMock.verify(statusbarRidget);
		EasyMock.reset(statusbarRidget);

		EasyMock.expect(statusbarRidget.getMessage()).andReturn(testErrorMessage);
		statusbarRidget.setMessage(EMPTY_STATUSBAR_MESSAGE);
		EasyMock.replay(statusbarRidget);

		statusbarMessageMarkerViewer.setVisible(false);

		EasyMock.verify(statusbarRidget);
		EasyMock.reset(statusbarRidget);

		EasyMock.replay(statusbarRidget);

		text2.setFocus();
		text1.setFocus();

		EasyMock.verify(statusbarRidget);
	}

	public void testTwoMarkers() throws Exception {

		String testErrorMessage1 = "Test Error 1 in Adapter 1";
		ErrorMessageMarker marker1 = new ErrorMessageMarker(testErrorMessage1);
		statusbarMessageMarkerViewer.addMarkerType(MessageMarker.class);
		String testErrorMessage2 = "Test Error 2 in Adapter 1";
		MessageMarker marker2 = new MessageMarker(testErrorMessage2);

		EasyMock.expect(statusbarRidget.getMessage()).andReturn(EMPTY_STATUSBAR_MESSAGE);
		statusbarRidget.setMessage(testErrorMessage1);
		EasyMock.replay(statusbarRidget);

		ridget1.addMarker(marker1);

		EasyMock.verify(statusbarRidget);
		EasyMock.reset(statusbarRidget);

		statusbarRidget.setMessage(testErrorMessage1 + " " + testErrorMessage2);
		EasyMock.replay(statusbarRidget);

		ridget1.addMarker(marker2);

		EasyMock.verify(statusbarRidget);
		EasyMock.reset(statusbarRidget);

		statusbarRidget.setMessage(testErrorMessage2);
		EasyMock.replay(statusbarRidget);

		ridget1.removeMarker(marker1);

		EasyMock.verify(statusbarRidget);
		EasyMock.reset(statusbarRidget);

		EasyMock.expect(statusbarRidget.getMessage()).andReturn(testErrorMessage2);
		statusbarRidget.setMessage(EMPTY_STATUSBAR_MESSAGE);
		EasyMock.replay(statusbarRidget);

		ridget1.removeMarker(marker2);

		EasyMock.verify(statusbarRidget);
	}

}
