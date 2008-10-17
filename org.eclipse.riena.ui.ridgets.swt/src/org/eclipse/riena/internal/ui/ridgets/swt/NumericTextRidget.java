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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigInteger;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.regex.Pattern;

import org.eclipse.core.databinding.BindingException;
import org.eclipse.riena.ui.core.marker.NegativeMarker;
import org.eclipse.riena.ui.ridgets.INumericValueTextFieldRidget;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;

/**
 * Ridget for a 'numeric' SWT <code>Text</code> widget.
 * 
 * @see UIControlsFactory#createTextNumeric(org.eclipse.swt.widgets.Composite)
 */
public class NumericTextRidget extends TextRidget implements INumericValueTextFieldRidget {

	private static final char DECIMAL_SEPARATOR = new DecimalFormatSymbols().getDecimalSeparator();
	private static final char GROUPING_SEPARATOR = new DecimalFormatSymbols().getGroupingSeparator();
	private static final char MINUS_SIGN = new DecimalFormatSymbols().getMinusSign();
	private static final char ZERO = '0';
	private static final String MINUS_ZERO = String.valueOf(MINUS_SIGN) + ZERO;

	private final VerifyListener verifyListener;
	private final ModifyListener modifyListener;
	private final KeyListener keyListener;

	private boolean isSigned;
	private boolean isGrouping;
	private boolean isMarkNegative;
	private NegativeMarker negativeMarker;
	private int maxLength;
	private int precision;

	public NumericTextRidget() {
		super("0"); //$NON-NLS-1$
		verifyListener = new NumericVerifyListener();
		modifyListener = new NumericModifyListener();
		keyListener = new NumericKeyListener();
		isSigned = true;
		isGrouping = true;
		isMarkNegative = true;
		maxLength = -1;
		precision = -1;
		addPropertyChangeListener(ITextFieldRidget.PROPERTY_TEXT, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				// System.out.println("updateMarkNeg: " + evt.getNewValue());
				updateMarkNegative();
			}
		});
	}

	protected void checkNumber(String number) {
		if (!"".equals(number)) { //$NON-NLS-1$
			try {
				new BigInteger(ungroup(number));
			} catch (NumberFormatException nfe) {
				throw new NumberFormatException("Not a valid number: " + number); //$NON-NLS-1$
			}
		}
	}

	@Override
	protected void checkUIControl(Object uiControl) {
		super.checkUIControl(uiControl);
		AbstractSWTRidget.assertType(uiControl, Text.class);
		if (uiControl != null) {
			int style = ((Text) uiControl).getStyle();
			if ((style & SWT.SINGLE) == 0) {
				throw new BindingException("Text widget must be SWT.SINGLE"); //$NON-NLS-1$
			}
		}
	}

	@Override
	protected synchronized void addListeners(Text control) {
		control.addVerifyListener(verifyListener);
		control.addModifyListener(modifyListener);
		control.addKeyListener(keyListener);
		super.addListeners(control);
	}

	@Override
	protected synchronized void removeListeners(Text control) {
		control.removeKeyListener(keyListener);
		control.removeModifyListener(modifyListener);
		control.removeVerifyListener(verifyListener);
		super.removeListeners(control);
	}

	public boolean isGrouping() {
		return isGrouping;
	}

	public boolean isMarkNegative() {
		return isMarkNegative;
	}

	public boolean isSigned() {
		return isSigned;
	}

	public void setGrouping(boolean useGrouping) {
		if (isGrouping != useGrouping) {
			isGrouping = useGrouping;
			updateGrouping();
		}
	}

	public void setMarkNegative(boolean mustBeMarked) {
		if (isMarkNegative != mustBeMarked) {
			isMarkNegative = mustBeMarked;
			updateMarkNegative();
		}
	}

	public void setSigned(boolean signed) {
		if (isSigned != signed) {
			boolean oldValue = isSigned;
			isSigned = signed;
			firePropertyChange(PROPERTY_SIGNED, oldValue, isSigned);
		}
	}

	@Override
	public synchronized void setText(String text) {
		checkNumber(text);
		super.setText(group(ungroup(text), isGrouping, isDecimal()));
	}

	// helping methods
	//////////////////

	private void addVerifyListener() {
		Text control = getUIControl();
		if (control != null) {
			// make sure the listener is only added once
			control.removeVerifyListener(verifyListener);
			control.addVerifyListener(verifyListener);
		}
	}

	// TODO [ev] test this?
	private synchronized String createPattern(String input) {
		String result;
		if (isDecimal()) {
			result = String.format("\\d{0,%d}\\.\\d{0,%d}", getMaxLength(), getPrecision()); //$NON-NLS-1$
			if (isSigned) {
				result = String.format("%c?", MINUS_SIGN) + result; //$NON-NLS-1$
			}
		} else {
			int length = input.length();
			if (isSigned) {
				int min = Math.max(0, length - 1);
				result = String.format("%c?\\d{%d,%d}", MINUS_SIGN, min, length); //$NON-NLS-1$
			} else {
				result = String.format("\\d{%d}", length); //$NON-NLS-1$
			}
		}
		// System.out.println("pattern: " + result);
		return result;
	}

	private boolean isDecimal() {
		return getPrecision() != -1;
	}

	protected static String group(String input, boolean isGrouping, boolean isDecimal) {
		String result = input;
		int decIndex = input.indexOf(DECIMAL_SEPARATOR);
		if (isGrouping) {
			String left = decIndex == -1 ? input : input.substring(0, decIndex);
			String right = decIndex == -1 ? "" : input.substring(decIndex); //$NON-NLS-1$
			result = NumericTextRidget.group(left) + right;
		}
		if (decIndex == -1 && isDecimal) {
			result += DECIMAL_SEPARATOR;
		}
		return result;
	}

	private static String group(String input) {
		final int numLength = input.length();
		boolean isNegative = input.indexOf(MINUS_SIGN) == 0;
		int groupSize = 3;
		int delta = isNegative ? -2 : -1;
		int groupCount = (numLength + delta) / groupSize;

		char[] result = new char[numLength + groupCount];
		Arrays.fill(result, '#');
		int availableChars = groupSize + 1;
		for (int i = result.length - 1; i > 0; i--) {
			availableChars--;
			if (availableChars == 0) {
				result[i] = GROUPING_SEPARATOR;
				availableChars = groupSize + 1;
			}
		}
		for (int i = 0, j = 0; i < result.length; i++) {
			if ('#' == result[i]) {
				result[i] = input.charAt(j);
				j++;
			}
		}
		// System.out.println("group: " + input + " >> " + String.valueOf(result));
		return String.valueOf(result);
	}

	// TODO [ev] move static methods
	protected static String ungroup(String input) {
		StringBuilder result = new StringBuilder(input.length());
		for (int i = 0; i < input.length(); i++) {
			char ch = input.charAt(i);
			if (ch != GROUPING_SEPARATOR) {
				result.append(ch);
			}
		}
		// System.out.println("ungroup: " + input + " >> " + result.toString());
		return result.toString();
	}

	private void updateGrouping() {
		setText(getText());
	}

	private void updateMarkNegative() {
		String text = ungroup(getText());
		boolean needMarker = false;
		if (isMarkNegative()) {
			try {
				needMarker = isNegative(text);
			} catch (NumberFormatException nfe) {
				needMarker = false;
			}
		}
		if (needMarker) {
			if (negativeMarker == null) {
				negativeMarker = new NegativeMarker();
			}
			addMarker(negativeMarker);
		} else {
			if (negativeMarker != null) {
				removeMarker(negativeMarker);
			}
		}
	}

	protected boolean isNegative(String text) {
		BigInteger value = new BigInteger(text);
		return (value.compareTo(BigInteger.ZERO) < 0);
	}

	// TODO [ev] test this?
	private String removeLeadingZeroes(String input) {
		if (String.valueOf(MINUS_SIGN).equals(input) || MINUS_ZERO.equals(input) || String.valueOf(ZERO).equals(input)) {
			return String.valueOf(ZERO);
		}
		StringBuilder result = new StringBuilder(input.length());
		int start = 0;
		if (input.indexOf(MINUS_SIGN) == 0) {
			result.append(MINUS_SIGN);
			start++;
		}
		if (start < input.length() && ZERO == input.charAt(start)) {
			int newStart = start + 1;
			while (newStart < input.length() && ZERO == input.charAt(newStart)) {
				newStart++;
			}
			if (newStart == input.length()) {
				result.append(ZERO);
				if (MINUS_ZERO.equals(result.toString())) {
					result.delete(0, 1);
				}
			} else {
				result.append(input.substring(newStart));
			}
		} else {
			result.append(input.substring(start));
		}
		return result.toString();
	}

	private void removeVerifyListener() {
		Text control = getUIControl();
		if (control != null) {
			control.removeVerifyListener(verifyListener);
		}
	}

	protected synchronized int getPrecision() {
		return precision;
	}

	protected synchronized int getMaxLength() {
		return maxLength;
	}

	protected synchronized void setPrecision(int precision) {
		this.precision = precision;
	}

	protected synchronized void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	// helping classes
	//////////////////

	/**
	 * TODO [ev] docs
	 */
	private final class NumericVerifyListener implements VerifyListener {
		public void verifyText(VerifyEvent e) {
			if (!e.doit) {
				return;
			}
			Text control = (Text) e.widget;
			final String oldText = control.getText();
			String newText = null;
			boolean preserveDecSep = oldText.substring(e.start, e.end).indexOf(DECIMAL_SEPARATOR) != -1;
			if (Character.isDigit(e.character) || MINUS_SIGN == e.character) { // insert / replace
				newText = oldText.substring(0, e.start);
				if (preserveDecSep) {
					if (oldText.charAt(e.start) == DECIMAL_SEPARATOR) {
						newText = newText + DECIMAL_SEPARATOR + e.character;
					} else {
						newText = newText + e.character + DECIMAL_SEPARATOR;
					}
				} else {
					newText += e.character;
				}
				newText += oldText.substring(e.end);
			} else if ('\b' == e.character || 127 == e.keyCode) { // delete
				char ch = oldText.charAt(e.start);
				if (e.end - e.start == 1 && (ch == GROUPING_SEPARATOR || ch == DECIMAL_SEPARATOR)) {
					// move cursor to left or right
					newText = null; // implies e.doit = false
					int delta = (127 == e.keyCode) ? 1 : 0;
					control.setSelection(e.start + delta);
				} else {
					// compute text after delete
					newText = oldText.substring(0, e.start);
					if (preserveDecSep) {
						newText += DECIMAL_SEPARATOR;
					}
					newText += oldText.substring(e.end);
				}
			}
			if (newText != null) {
				String newTextNoGroup = ungroup(newText);
				String regex = createPattern(newTextNoGroup);
				e.doit = Pattern.matches(regex, newTextNoGroup);
				// System.out.println("nt:" + newTextNoGroup + " p: " + regex);
				if (e.doit && preserveDecSep) {
					e.doit = false;
					int posFromRight = oldText.length() - e.end;
					removeVerifyListener();
					control.setText(newTextNoGroup);
					if (newTextNoGroup.equals(String.valueOf(DECIMAL_SEPARATOR))) {
						control.setSelection(0);
					} else {
						control.setSelection(control.getText().length() - posFromRight);
					}
					addVerifyListener();
				}
			} else {
				e.doit = false;
			}
		}
	}

	/**
	 * TODO [ev] docs
	 */
	private final class NumericModifyListener implements ModifyListener {

		public void modifyText(ModifyEvent e) {
			Text control = (Text) e.widget;
			String oldText = control.getText();
			String newText = group(removeLeadingZeroes(ungroup(oldText)), isGrouping, isDecimal());
			if (!oldText.equals(newText)) {
				removeVerifyListener();
				int posFromRight = oldText.length() - control.getCaretPosition();
				control.setText(newText);
				int caretPos = newText.length() - posFromRight;
				control.setSelection(caretPos);
				// System.out.println("newText= " + newText + " @ " + caretPos);
				addVerifyListener();
			}
		}
	}

	/**
	 * TODO [ev] docs
	 */
	private final class NumericKeyListener extends KeyAdapter {

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
			}
			if (16777219 == e.keyCode && control.getSelectionCount() == 0) {// left arrow
				int index = control.getCaretPosition() - 1;
				if (index > 1 && GROUPING_SEPARATOR == text.charAt(index) && !shiftDown) {
					e.doit = false;
					control.setSelection(index - 1);
				}
			} else if (16777220 == e.keyCode && control.getSelectionCount() == 0) { //right arrow
				int index = control.getCaretPosition() + 1;
				if (index < text.length() - 1 && GROUPING_SEPARATOR == text.charAt(index) && !shiftDown) {
					e.doit = false;
					control.setSelection(index + 1);
				}
			} else if (DECIMAL_SEPARATOR == e.character) {
				e.doit = false;
				int index = control.getCaretPosition();
				if (index < text.length() && text.charAt(index) == DECIMAL_SEPARATOR) {
					control.setSelection(index + 1);
				}
			} else if ('-' == e.character) {
				e.doit = false;
				if (isSigned) {
					Event event = new Event();
					event.type = SWT.Verify;
					event.character = MINUS_SIGN;
					event.start = 0;
					event.end = 0;
					event.widget = control;
					event.text = String.valueOf(MINUS_SIGN);
					control.notifyListeners(SWT.Verify, event);
					if (event.doit) {
						int caret = control.getCaretPosition() + 1;
						removeVerifyListener();
						control.setText(MINUS_SIGN + text);
						control.setSelection(caret);
						addVerifyListener();
					}
				}
			}
		}
	}

}
