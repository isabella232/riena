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
	// private final ModifyListener modifyListener;
	private final KeyListener keyListener;

	private String pattern;
	private ValidDate validDateRule;
	private ValidIntermediateDate validIntermediateDateRule;
	private StringToDateConverter uiControlToModelconverter;
	private DateToStringConverter modelToUIControlConverter;

	public DateTextRidget() {
		verifyListener = new DateVerifyListener();
		// modifyListener = new DateModifyListener();
		keyListener = new DateKeyListener();
		setFormat(IDateTextFieldRidget.FORMAT_DDMMYYYY);
	}

	protected final synchronized void addListeners(Text control) {
		control.addVerifyListener(verifyListener);
		// control.addModifyListener(modifyListener);
		control.addKeyListener(keyListener);
		super.addListeners(control);
	}

	@Override
	protected final synchronized void removeListeners(Text control) {
		control.removeKeyListener(keyListener);
		// control.removeModifyListener(modifyListener);
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
	}

	// helping classes
	//////////////////

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
	 * <li>Left & Right arrow - will jump over separators and spaces</li>
	 * <li>Shift - ddisables jumping over grouping separators when pressed down</li>
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
			String text = control.getText();
			if (131072 == e.keyCode) {
				shiftDown = true;
			} else if (16777219 == e.keyCode && control.getSelectionCount() == 0 && !shiftDown) {// left arrow
				e.doit = false;
				SegmentedString ss = new SegmentedString(pattern, text);
				int index = ss.findNewCursorPosition(control.getCaretPosition(), -1);
				control.setSelection(index);
			} else if (16777220 == e.keyCode && control.getSelectionCount() == 0 && !shiftDown) { //right arrow
				e.doit = false;
				SegmentedString ss = new SegmentedString(pattern, text);
				int index = ss.findNewCursorPosition(control.getCaretPosition(), 1);
				control.setSelection(index);
			}
		}
	}

}
