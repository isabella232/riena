/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2008 compeople AG                         *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.util.Arrays;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.riena.ui.ridgets.IMessageBoxRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IMessageBoxRidget.MessageBoxOption;
import org.eclipse.riena.ui.ridgets.swt.MessageBox;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Tests for the MessageBoxRidget.
 */
public class MessageBoxRidgetTest extends AbstractSWTRidgetNonWidgetTest {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.internal.ui.ridgets.swt.AbstractSWTRidgetTest#createWidget
	 * (org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected MessageBox createWidget(Composite parent) {
		return new MockMessageBox(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.internal.ui.ridgets.swt.AbstractSWTRidgetNonWidgetTest
	 * #createRidget()
	 */
	@Override
	protected IRidget createRidget() {
		return new MessageBoxRidget();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.internal.ui.ridgets.swt.AbstractSWTRidgetNonWidgetTest
	 * #getRidget()
	 */
	@Override
	protected IMessageBoxRidget getRidget() {
		return (IMessageBoxRidget) super.getRidget();
	}

	@Override
	protected MockMessageBox getWidget() {
		return (MockMessageBox) super.getWidget();
	}

	public void testSetText() throws Exception {

		// show message box to transfer values set
		getRidget().show();

		assertNull(getWidget().text);
		assertNull(getRidget().getText());

		getRidget().setText("TestMessageText");
		// show message box to transfer values set
		getRidget().show();

		assertEquals("TestMessageText", getWidget().text);
		assertEquals("TestMessageText", getRidget().getText());
	}

	public void testSetTitle() throws Exception {

		// show message box to transfer values set
		getRidget().show();

		assertNull(getWidget().title);
		assertNull(getRidget().getTitle());

		getRidget().setTitle("TestMessageTitle");
		// show message box to transfer values set
		getRidget().show();

		assertEquals("TestMessageTitle", getWidget().title);
		assertEquals("TestMessageTitle", getRidget().getTitle());
	}

	public void testSetType() throws Exception {

		// show message box to transfer values set
		getRidget().show();

		assertEquals(MessageDialog.NONE, getWidget().type);
		assertEquals(IMessageBoxRidget.Type.PLAIN, getRidget().getType());

		getRidget().setType(IMessageBoxRidget.Type.ERROR);
		// show message box to transfer values set
		getRidget().show();

		assertEquals(MessageDialog.ERROR, getWidget().type);
		assertEquals(IMessageBoxRidget.Type.ERROR, getRidget().getType());

		getRidget().setType(IMessageBoxRidget.Type.WARNING);
		// show message box to transfer values set
		getRidget().show();

		assertEquals(MessageDialog.WARNING, getWidget().type);
		assertEquals(IMessageBoxRidget.Type.WARNING, getRidget().getType());

		getRidget().setType(IMessageBoxRidget.Type.INFORMATION);
		// show message box to transfer values set
		getRidget().show();

		assertEquals(MessageDialog.INFORMATION, getWidget().type);
		assertEquals(IMessageBoxRidget.Type.INFORMATION, getRidget().getType());

		getRidget().setType(IMessageBoxRidget.Type.HELP);
		// show message box to transfer values set
		getRidget().show();

		assertEquals(MessageDialog.INFORMATION, getWidget().type);
		assertEquals(IMessageBoxRidget.Type.HELP, getRidget().getType());

		getRidget().setType(IMessageBoxRidget.Type.QUESTION);
		// show message box to transfer values set
		getRidget().show();

		assertEquals(MessageDialog.QUESTION, getWidget().type);
		assertEquals(IMessageBoxRidget.Type.QUESTION, getRidget().getType());

		getRidget().setType(null);
		// show message box to transfer values set
		getRidget().show();

		assertEquals(MessageDialog.NONE, getWidget().type);
		assertEquals(IMessageBoxRidget.Type.PLAIN, getRidget().getType());
	}

	public void testSetOptions() throws Exception {

		// show message box to transfer values set
		getRidget().show();

		assertEquals(1, getWidget().buttonLabels.length);
		assertEquals(IDialogConstants.OK_LABEL, getWidget().buttonLabels[0]);
		assertEquals(IMessageBoxRidget.OPTIONS_OK, getRidget().getOptions());

		getRidget().setOptions(IMessageBoxRidget.OPTIONS_YES_NO);
		// show message box to transfer values set
		getRidget().show();

		assertEquals(2, getWidget().buttonLabels.length);
		assertEquals(IDialogConstants.YES_LABEL, getWidget().buttonLabels[0]);
		assertEquals(IDialogConstants.NO_LABEL, getWidget().buttonLabels[1]);
		assertEquals(IMessageBoxRidget.OPTIONS_YES_NO, getRidget().getOptions());

		getRidget().setOptions(IMessageBoxRidget.OPTIONS_OK_CANCEL);
		// show message box to transfer values set
		getRidget().show();

		assertEquals(2, getWidget().buttonLabels.length);
		assertEquals(IDialogConstants.OK_LABEL, getWidget().buttonLabels[0]);
		assertEquals(IDialogConstants.CANCEL_LABEL, getWidget().buttonLabels[1]);
		assertEquals(IMessageBoxRidget.OPTIONS_OK_CANCEL, getRidget().getOptions());

		getRidget().setOptions(IMessageBoxRidget.OPTIONS_YES_NO_CANCEL);
		// show message box to transfer values set
		getRidget().show();

		assertEquals(3, getWidget().buttonLabels.length);
		assertEquals(IDialogConstants.YES_LABEL, getWidget().buttonLabels[0]);
		assertEquals(IDialogConstants.NO_LABEL, getWidget().buttonLabels[1]);
		assertEquals(IDialogConstants.CANCEL_LABEL, getWidget().buttonLabels[2]);
		assertEquals(IMessageBoxRidget.OPTIONS_YES_NO_CANCEL, getRidget().getOptions());

		MessageBoxOption[] semiCustomOptions = new IMessageBoxRidget.MessageBoxOption[] { IMessageBoxRidget.OK,
				new IMessageBoxRidget.MessageBoxOption("TestCustomOptionText") };
		getRidget().setOptions(semiCustomOptions);
		// show message box to transfer values set
		getRidget().show();

		assertEquals(2, getWidget().buttonLabels.length);
		assertEquals("OK", getWidget().buttonLabels[0]);
		assertEquals("TestCustomOptionText", getWidget().buttonLabels[1]);
		assertEquals(semiCustomOptions, getRidget().getOptions());

		MessageBoxOption[] customOptions = new IMessageBoxRidget.MessageBoxOption[] {
				new IMessageBoxRidget.MessageBoxOption("TestCustomOptionText1"),
				new IMessageBoxRidget.MessageBoxOption("TestCustomOptionText2"),
				new IMessageBoxRidget.MessageBoxOption("TestCustomOptionText3") };
		getRidget().setOptions(customOptions);
		// show message box to transfer values set
		getRidget().show();

		assertEquals(3, getWidget().buttonLabels.length);
		assertEquals("TestCustomOptionText1", getWidget().buttonLabels[0]);
		assertEquals("TestCustomOptionText2", getWidget().buttonLabels[1]);
		assertEquals("TestCustomOptionText3", getWidget().buttonLabels[2]);
		assertEquals(customOptions, getRidget().getOptions());

		getRidget().setOptions(IMessageBoxRidget.OPTIONS_OK);
		// show message box to transfer values set
		getRidget().show();

		assertEquals(1, getWidget().buttonLabels.length);
		assertEquals(IDialogConstants.OK_LABEL, getWidget().buttonLabels[0]);
		assertEquals(IMessageBoxRidget.OPTIONS_OK, getRidget().getOptions());
	}

	public void testUsedInSharedView() throws Exception {

		getRidget().setText("TestMessageText1");
		getRidget().setTitle("TestMessageTitle1");
		getRidget().setType(IMessageBoxRidget.Type.QUESTION);
		getRidget().setOptions(IMessageBoxRidget.OPTIONS_OK_CANCEL);
		// show message box to transfer values set
		getRidget().show();

		IMessageBoxRidget ridget2 = new MessageBoxRidget();
		ridget2.setText("TestMessageText2");
		ridget2.setTitle("TestMessageTitle2");
		ridget2.setType(IMessageBoxRidget.Type.WARNING);
		ridget2.setOptions(new IMessageBoxRidget.MessageBoxOption[] { new IMessageBoxRidget.MessageBoxOption(
				"TestCustomOption") });

		getRidget().setUIControl(null);
		ridget2.setUIControl(getWidget());
		// show message box to transfer values set
		ridget2.show();

		assertEquals("TestMessageText2", getWidget().text);
		assertEquals("TestMessageTitle2", getWidget().title);
		assertEquals(MessageDialog.WARNING, getWidget().type);
		assertEquals(1, getWidget().buttonLabels.length);
		assertEquals("TestCustomOption", getWidget().buttonLabels[0]);

		getRidget().setUIControl(getWidget());
		// show message box to transfer values set
		getRidget().show();
		ridget2.setUIControl(null);

		assertEquals("TestMessageText1", getWidget().text);
		assertEquals("TestMessageTitle1", getWidget().title);
		assertEquals(MessageDialog.QUESTION, getWidget().type);
		assertEquals(2, getWidget().buttonLabels.length);
		assertEquals(IDialogConstants.OK_LABEL, getWidget().buttonLabels[0]);
		assertEquals(IDialogConstants.CANCEL_LABEL, getWidget().buttonLabels[1]);
	}

	public void testShow() throws Exception {

		getRidget().setTitle("TestMessageTitle");
		getRidget().setText("TestMessageText");
		getRidget().setType(IMessageBoxRidget.Type.QUESTION);
		getRidget().setOptions(IMessageBoxRidget.OPTIONS_YES_NO);

		// simulate 'yes' button, i.e. button with id 0 is clicked.
		setMessageBoxReturnValue(0);

		assertEquals(IMessageBoxRidget.YES, getRidget().show());

		// simulate 'close' button, i.e. button with id SWT.DEFAULT is clicked.
		setMessageBoxReturnValue(SWT.DEFAULT);

		assertEquals(IMessageBoxRidget.CLOSED, getRidget().show());

		IMessageBoxRidget.MessageBoxOption option3 = new IMessageBoxRidget.MessageBoxOption("TestCustomOption3");
		getRidget().setOptions(
				new IMessageBoxRidget.MessageBoxOption[] { new IMessageBoxRidget.MessageBoxOption("TestCustomOption1"),
						new IMessageBoxRidget.MessageBoxOption("TestCustomOption2"), option3,
						new IMessageBoxRidget.MessageBoxOption("TestCustomOption4") });

		setMessageBoxReturnValue(2);

		assertEquals(option3, getRidget().show());

		// simulate 'close' button, i.e. button with id SWT.DEFAULT is clicked.
		setMessageBoxReturnValue(SWT.DEFAULT);

		assertEquals(IMessageBoxRidget.CLOSED, getRidget().show());

		getRidget().setUIControl(null);

		assertNull(getRidget().show());
	}

	private void setMessageBoxReturnValue(final int returnValue) {
		getRidget().setUIControl(new MessageBox(getShell()) {

			@Override
			public void show(String title, String text, int type, String[] buttonLabels) {
			}

			@Override
			public int getResult() {
				return returnValue;
			}

		});
	}

	private void assertEquals(MessageBoxOption[] expected, MessageBoxOption[] actual) {
		if (Arrays.equals(expected, actual)) {
			return;
		}
		failNotEquals(null, expected, actual);
	}

	private class MockMessageBox extends MessageBox {

		private String title;
		private String text;
		private int type;
		private String[] buttonLabels;

		private MockMessageBox(Control parent) {
			super(parent);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.riena.ui.ridgets.swt.MessageBox#show(java.lang.String,
		 * java.lang.String, int, java.lang.String[])
		 */
		@Override
		public void show(String title, String text, int type, String[] buttonLabels) {

			// save values her to check after showing that the call of show was performed with the correct arguments.
			this.title = title;
			this.text = text;
			this.type = type;
			this.buttonLabels = buttonLabels;
		}
	}
}
