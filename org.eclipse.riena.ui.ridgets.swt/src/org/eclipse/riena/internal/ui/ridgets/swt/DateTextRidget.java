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

import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.ridgets.IDateTextFieldRidget;
import org.eclipse.riena.ui.ridgets.IDecimalValueTextFieldRidget;
import org.eclipse.riena.ui.ridgets.databinding.DateToStringConverter;
import org.eclipse.riena.ui.ridgets.databinding.StringToDateConverter;
import org.eclipse.riena.ui.ridgets.validation.ValidDate;
import org.eclipse.riena.ui.ridgets.validation.ValidIntermediateDate;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Text;

/**
 * Ridget for a 'date/time/date time' SWT <code>Text</code> widget. The desired
 * date/time/dat time pattern can be set via {@link #setFormat(String)}. See
 * {@link IDecimalValueTextFieldRidget} for supported patterns.
 * 
 * @see UIControlsFactory#createTextDate(org.eclipse.swt.widgets.Composite)
 */
public class DateTextRidget extends TextRidget implements IDateTextFieldRidget {

	private final VerifyListener verifyListener;
	private final KeyListener keyListener;

	private String pattern;
	private ValidDate validDateRule;
	private ValidIntermediateDate validIntermediateDateRule;
	private StringToDateConverter uiControlToModelconverter;
	private DateToStringConverter modelToUIControlConverter;

	public DateTextRidget() {
		verifyListener = new DateVerifyListener();
		keyListener = new DateKeyListener();
		setFormat(IDateTextFieldRidget.FORMAT_DDMMYYYY);
	}

	protected final synchronized void addListeners(Text control) {
		control.addVerifyListener(verifyListener);
		control.addKeyListener(keyListener);
		super.addListeners(control);
	}

	@Override
	protected final synchronized void removeListeners(Text control) {
		control.removeKeyListener(keyListener);
		control.removeVerifyListener(verifyListener);
		super.removeListeners(control);
	}

	public final void setFormat(String datePattern) {
		removeValidationRule(validDateRule);
		removeValidationRule(validIntermediateDateRule);

		pattern = datePattern;
		validDateRule = new ValidDate(pattern);
		validIntermediateDateRule = new ValidIntermediateDate(pattern);
		uiControlToModelconverter = new StringToDateConverter(pattern);
		modelToUIControlConverter = new DateToStringConverter(pattern);

		addValidationRule(validDateRule, ValidationTime.ON_UPDATE_TO_MODEL);
		addValidationRule(validIntermediateDateRule, ValidationTime.ON_UI_CONTROL_EDIT);
		setUIControlToModelConverter(uiControlToModelconverter);
		setModelToUIControlConverter(modelToUIControlConverter);

		setText(new SegmentedString(pattern).toString());
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * 
	 * @throws RuntimeException
	 *             - if {code text} does not (partially) match the specified
	 *             format pattern. A partial match is any string that has digits
	 *             and separators in the expected places - as defined by the
	 *             format pattern - regardless of limits for a certain group
	 *             (i.e. month <= 12 etc.). For example, assuming the format
	 *             pattern is 'dd.MM.yyyy', all of the following values are
	 *             valid: "", "12", "12.10", "47.11", "12.10.20", "12.10.2008",
	 *             "  .  .    ", "  .10". Invalid values would be: null, "abc",
	 *             "12.ab", "12122008", "12/12/2008"
	 */
	@Override
	public synchronized void setText(String text) {
		String newText = checkAndFormatValue(text);
		super.setText(newText);
	}

	// helping methods
	//////////////////

	private String checkAndFormatValue(String text) {
		SegmentedString ss = new SegmentedString(pattern);
		if (!ss.isValidPartialMatch(text)) {
			String msg = String.format("'%s' is no partial match for '%s'", text, pattern); //$NON-NLS-1$
			throw new IllegalArgumentException(msg);
		}
		ss.insert(0, text);
		return ss.toString();
	}

	// helping classes
	//////////////////

	/**
	 * This listener handles addition, deletion and replacement of text in the
	 * Text control. When the text in the control is modified, it will compute
	 * the new value. Unsupported modifications will be cancelled.
	 */
	private final class DateVerifyListener implements VerifyListener {

		private volatile boolean isEnabled = true;

		public void verifyText(VerifyEvent e) {
			if (e.doit == false || !isEnabled) {
				return;
			}
			// System.out.println(e);
			Text control = (Text) e.widget;
			String oldText = control.getText();
			int newPos = -1;
			SegmentedString ss = new SegmentedString(pattern, oldText);
			if (e.character == '\b' || e.keyCode == 127) {// backspace, del
				newPos = ss.delete(e.start, e.end - 1);
				if (newPos == -1) {
					newPos = e.character == '\b' ? e.start : e.end;
				}
			} else if (SegmentedString.isDigit(e.character)) {
				if (e.end - e.start > 0) {
					newPos = ss.replace(e.start, e.end - 1, e.text);
				} else {
					newPos = ss.insert(e.start, String.valueOf(e.character));
				}
			} else if (SegmentedString.isSeparator(e.character)) {
				if (e.end - e.start > 0) {
					newPos = ss.replace(e.start, e.end - 1, String.valueOf(e.character));
				} else {
					newPos = ss.insert(e.start, String.valueOf(e.character));
				}
			}
			e.doit = false;
			if (newPos != -1) {
				isEnabled = false;
				control.setText(ss.toString());
				// System.out.println("newPos: " + newPos);
				control.setSelection(newPos);
				isEnabled = true;
			}
		}
	}

	/**
	 * This listener controls which key strokes are allowed by the text control.
	 * Additionally some keystrokes replaced with special behavior. Currently
	 * those key strokes are:
	 * <ol>
	 * <ol>
	 * <li>Left & Right arrow - will jump over separators and spaces</li> <li>
	 * Delete / Backspace at a single separator - will jump to the next valid
	 * location in the same direction</li> <li>Shift - disables jumping over
	 * grouping separators when pressed down</li>
	 * </ol>
	 */
	private final class DateKeyListener extends KeyAdapter {

		private boolean shiftDown = false;

		@Override
		public void keyReleased(KeyEvent e) {
			if (131072 == e.keyCode) {
				shiftDown = false;
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
			Text control = (Text) e.widget;
			if (131072 == e.keyCode) {
				shiftDown = true;
			} else {
				final String text = control.getText();
				final int caret = control.getCaretPosition();
				final int selectionCount = control.getSelectionCount();
				if (16777219 == e.keyCode && selectionCount == 0 && !shiftDown) {// left arrow
					e.doit = false;
					SegmentedString ss = new SegmentedString(pattern, text);
					int index = ss.findNewCursorPosition(caret, -1);
					control.setSelection(index);
				} else if (16777220 == e.keyCode && selectionCount == 0 && !shiftDown) { //right arrow
					e.doit = false;
					SegmentedString ss = new SegmentedString(pattern, text);
					int index = ss.findNewCursorPosition(caret, 1);
					control.setSelection(index);
				} else if (127 == e.keyCode && selectionCount == 0 && !shiftDown) { // del
					if (caret < text.length() && SegmentedString.isSeparator(text.charAt(caret))) {
						e.doit = false;
						SegmentedString ss = new SegmentedString(pattern, text);
						int index = ss.findNewCursorPosition(caret, 1);
						control.setSelection(index);
					}
				} else if ('\b' == e.character && selectionCount == 0 && !shiftDown) {
					if (caret > 0 && SegmentedString.isSeparator(text.charAt(caret - 1))) {
						e.doit = false;
						SegmentedString ss = new SegmentedString(pattern, text);
						int index = ss.findNewCursorPosition(caret, -1);
						control.setSelection(index);
					}
				}
			}
		}
	}

}
