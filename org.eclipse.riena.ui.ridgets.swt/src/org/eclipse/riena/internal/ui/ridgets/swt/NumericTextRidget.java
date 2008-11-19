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
import org.eclipse.riena.ui.ridgets.INumericTextRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
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
public class NumericTextRidget extends TextRidget implements INumericTextRidget {

	/**
	 * This is not API and should not be called by clients. Public for testing
	 * only.
	 */
	public static String group(String input, boolean isGrouping, boolean isDecimal) {
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

	/**
	 * This is not API and should not be called by clients. Public for testing
	 * only.
	 */
	public static String ungroup(String input) {
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

	/**
	 * This is not API and should not be called by clients. Public for testing
	 * only.
	 */
	public static String removeLeadingZeroes(String input) {
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

	protected static final char DECIMAL_SEPARATOR = new DecimalFormatSymbols().getDecimalSeparator();
	protected static final char GROUPING_SEPARATOR = new DecimalFormatSymbols().getGroupingSeparator();
	private static final char MINUS_SIGN = new DecimalFormatSymbols().getMinusSign();
	private static final char ZERO = '0';
	private static final String MINUS_ZERO = String.valueOf(MINUS_SIGN) + ZERO;
	private static final String MINUS_DEC = String.valueOf(MINUS_SIGN) + DECIMAL_SEPARATOR;

	private final NumericVerifyListener verifyListener;
	private final NumericModifyListener modifyListener;
	private final KeyListener keyListener;
	private final FocusListener focusListener;

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
		focusListener = new NumericFocusListener();
		isSigned = true;
		isGrouping = true;
		isMarkNegative = true;
		maxLength = -1;
		precision = -1;
		addPropertyChangeListener(ITextRidget.PROPERTY_TEXT, new PropertyChangeListener() {
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
	protected final synchronized void addListeners(Text control) {
		control.addVerifyListener(verifyListener);
		control.addModifyListener(modifyListener);
		control.addKeyListener(keyListener);
		control.addFocusListener(focusListener);
		super.addListeners(control);
	}

	protected synchronized int getPrecision() {
		return precision;
	}

	protected synchronized int getMaxLength() {
		return maxLength;
	}

	protected boolean isNegative(String text) {
		BigInteger value = new BigInteger(text);
		return (value.compareTo(BigInteger.ZERO) < 0);
	}

	@Override
	protected final synchronized void removeListeners(Text control) {
		control.removeFocusListener(focusListener);
		control.removeKeyListener(keyListener);
		control.removeModifyListener(modifyListener);
		control.removeVerifyListener(verifyListener);
		super.removeListeners(control);
	}

	protected synchronized void setPrecision(int precision) {
		this.precision = precision;
		String oldText = getText();
		String newText = formatFraction(oldText);
		if (!oldText.equals(newText)) {
			setText(newText);
		}
	}

	protected synchronized void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	// API methods
	//////////////

	public synchronized boolean isGrouping() {
		return isGrouping;
	}

	public synchronized boolean isMarkNegative() {
		return isMarkNegative;
	}

	public synchronized boolean isSigned() {
		return isSigned;
	}

	public synchronized void setGrouping(boolean useGrouping) {
		if (isGrouping != useGrouping) {
			isGrouping = useGrouping;
			updateGrouping();
		}
	}

	public synchronized void setMarkNegative(boolean mustBeMarked) {
		if (isMarkNegative != mustBeMarked) {
			isMarkNegative = mustBeMarked;
			updateMarkNegative();
		}
	}

	public final synchronized void setSigned(boolean signed) {
		if (isSigned != signed) {
			boolean oldValue = isSigned;
			isSigned = signed;
			firePropertyChange(PROPERTY_SIGNED, oldValue, isSigned);
		}
	}

	/**
	 * TODO [ev] docs - document that text should be in 'local' format
	 */
	@Override
	public final synchronized void setText(String text) {
		checkNumber(text);
		super.setText(group(ungroup(text), isGrouping, isDecimal()));
	}

	@Override
	public synchronized String getText() {
		String result = super.getText();
		if (isDecimal()) {
			result = removeTrailingPadding(result);
		}
		return result;
	}

	@Override
	public synchronized void updateFromModel() {
		super.updateFromModel();
		if (isDecimal()) {
			beautifyText(getUIControl());
		}

	}

	// helping methods
	//////////////////

	private void beautifyText(Text control) {
		if (control != null) {
			String text = control.getText();
			String newText = formatFraction(text);
			if (newText.length() > 0 && newText.charAt(0) == DECIMAL_SEPARATOR) {
				newText = "0" + newText; //$NON-NLS-1$
			} else if (newText.startsWith(MINUS_DEC)) {
				boolean hasValue = false;
				for (int i = 0; !hasValue && i < newText.length(); i++) {
					char ch = newText.charAt(i);
					hasValue = Character.isDigit(ch) && ch != '0';
				}
				if (hasValue) {
					newText = newText.substring(0, 1) + "0" + newText.substring(1); //$NON-NLS-1$
				} else {
					newText = "0" + newText.substring(1); //$NON-NLS-1$
				}
			}
			if (!newText.equals(text)) {
				stopVerifyListener();
				stopModifyListener();
				control.setText(newText);
				control.setSelection(newText.length());
				startModifyListener();
				startVerifyListener();
			}
		}
	}

	private void startModifyListener() {
		modifyListener.setEnabled(true);
	}

	private void startVerifyListener() {
		verifyListener.setEnabled(true);
	}

	private synchronized String createPattern(String input) {
		String result;
		if (isDecimal()) {
			String decSep = DECIMAL_SEPARATOR == '.' ? "\\." : String.valueOf(DECIMAL_SEPARATOR); //$NON-NLS-1$
			result = String.format("\\d{0,%d}%s\\d{0,%d}", getMaxLength(), decSep, getPrecision()); //$NON-NLS-1$
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

	private String formatFraction(String text) {
		String result = text;
		int decSep = text.indexOf(DECIMAL_SEPARATOR);
		if (decSep != -1) {
			int fractionDigits = text.substring(decSep).length() - 1;
			int prec = getPrecision();
			if (fractionDigits < prec) {
				int pad = Math.max(0, getPrecision() - fractionDigits);
				if (pad > 0) {
					char[] zeroes = new char[pad];
					Arrays.fill(zeroes, '0');
					result = text + String.valueOf(zeroes);
				}
			} else if (fractionDigits > prec) {
				int diff = fractionDigits - prec;
				result = text.substring(0, text.length() - diff);
			}
		}
		return result;
	}

	private boolean isDecimal() {
		return getPrecision() != -1;
	}

	private String removeTrailingPadding(String text) {
		String result = text;
		int decSep = text.indexOf(DECIMAL_SEPARATOR);
		if (decSep != -1) {
			int index = text.length() - 1;
			char ch = result.charAt(index);
			while (index >= decSep && (ch == DECIMAL_SEPARATOR | ch == '0')) {
				result = result.substring(0, index);
				index--;
				if (index >= decSep) {
					ch = result.charAt(index);
				}
			}
		}
		return result;
	}

	private void stopModifyListener() {
		modifyListener.setEnabled(false);
	}

	private void stopVerifyListener() {
		verifyListener.setEnabled(false);
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

	// helping classes
	//////////////////

	/**
	 * This listener handles addition, deletion and replacement of text in the
	 * Text control. When the text in the control is modified, it will compute
	 * the new value and match it against a pattern. The pattern is computed
	 * based on the maxLength, precision and signed settings of this ridget. If
	 * the new value does not match against the pattern, e.doit is set to false
	 * and the modification is cancelled.
	 */
	private final class NumericVerifyListener implements VerifyListener {

		private boolean isEnabled = true;

		public synchronized void setEnabled(boolean isEnabled) {
			this.isEnabled = isEnabled;
		}

		public synchronized void verifyText(VerifyEvent e) {
			if (!e.doit || !isEnabled) {
				return;
			}
			// System.out.println(e);
			final Text control = (Text) e.widget;
			final String oldText = control.getText();
			String newText = null;
			int newCursorPos = -1;
			boolean applyText = false;
			if (Character.isDigit(e.character) || MINUS_SIGN == e.character) { // insert / replace
				boolean preserveDecSep = oldText.substring(e.start, e.end).indexOf(DECIMAL_SEPARATOR) != -1;
				if (preserveDecSep) {
					if (oldText.charAt(e.start) == DECIMAL_SEPARATOR && oldText.length() > e.end - e.start) {
						// #(.#)# -> #.C# or (.#)# -> .C#
						newText = oldText.substring(0, e.start) + DECIMAL_SEPARATOR + e.character
								+ oldText.substring(e.end);
					} else {
						// (#.#) -> C. or #(#.#)# -> #C.# or #(#.)# -> #C.#
						newText = oldText.substring(0, e.start) + e.character + DECIMAL_SEPARATOR
								+ oldText.substring(e.end);
					}
					applyText = true;
				} else {
					newText = oldText.substring(0, e.start) + e.character + oldText.substring(e.end);
				}
			} else if ('\b' == e.character || 127 == e.keyCode) { // delete
				NumericString ns = new NumericString(oldText, isGrouping());
				newCursorPos = ns.delete(e.start, e.end, e.character);
				newText = ns.toString();
				applyText = true;
			}
			boolean doFlash = true;
			if (newText != null) {
				String newTextNoGroup = ungroup(newText);
				String regex = createPattern(newTextNoGroup);
				e.doit = Pattern.matches(regex, newTextNoGroup);
				// System.out.println("nt:" + newTextNoGroup + " p: " + regex);
				doFlash = !e.doit;
				if (e.doit && applyText) {
					e.doit = false;
					stopVerifyListener();
					setTextAndCursor(control, newTextNoGroup, newCursorPos, e);
					startVerifyListener();
				}
			} else {
				e.doit = false;
			}
			if (doFlash) {
				flash();
			}
		}

		private void setTextAndCursor(Text control, String text, int cursorPos, VerifyEvent e) {
			final String oldText = control.getText();
			int oldCursorPos = control.getCaretPosition();
			control.setText(text);
			if (oldCursorPos != 0 && text.length() == 1 && text.charAt(0) == DECIMAL_SEPARATOR) {
				control.setSelection(0);
			} else if (text.length() == 2 && text.charAt(1) == DECIMAL_SEPARATOR) {
				control.setSelection(1);
			} else if (cursorPos > -1) {
				control.setSelection(cursorPos);
			} else {
				int posFromRight = oldText.length() - e.end;
				control.setSelection(control.getText().length() - posFromRight);
			}
		}
	}

	/**
	 * When the user types a key in the text control that modifies it's content
	 * this listener is invoked. It will replace the string in the text control,
	 * by the a formatted equivalent, according to the settings in this ridget
	 * (precision, maxlength, etc.).
	 */
	private final class NumericModifyListener implements ModifyListener {

		private boolean isEnabled = true;

		public synchronized void setEnabled(boolean isEnabled) {
			this.isEnabled = isEnabled;
		}

		public synchronized void modifyText(ModifyEvent e) {
			if (!isEnabled || !isEnabled()) {
				return;
			}
			// System.out.println(e);
			Text control = (Text) e.widget;
			String oldText = control.getText();
			String newText = group(removeLeadingZeroes(ungroup(oldText)), isGrouping(), isDecimal());
			if (!oldText.equals(newText)) {
				stopVerifyListener();
				int posFromRight = oldText.length() - control.getCaretPosition();
				control.setText(newText);
				int caretPos = newText.length() - posFromRight;
				control.setSelection(caretPos);
				// System.out.println("newText= " + newText + " @ " + caretPos);
				startVerifyListener();
			}
		}
	}

	/**
	 * This listener controls which key strokes are allowed by the text control.
	 * Additionally some keystrokes replaced with special behavior. Currently
	 * those key strokes are:
	 * <ol>
	 * <ol>
	 * <li>Left & Right arrow - will jump over grouping separators</li>
	 * <li>Shift - ddisables jumping over grouping separators when pressed down</li>
	 * <li>Decimal separator - will cause the cursor to jump over the decimal
	 * separator if directly to the right of it. Otherwise ignored</li>
	 * <li>minus ('-') - for signed widgets, it adds the '-' character to the
	 * left of the widget. Otherwise ignored</li>
	 * <li>CR ('\r') - for decimal ridgets, it will pad the fractional digits by
	 * adding '0's until the maximum number of fractional digits is reached (as
	 * specified by the ridgets precision value)
	 * </ol>
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
			} else if (16777219 == e.keyCode && control.getSelectionCount() == 0) {// left arrow
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
				} else {
					flash();
				}
			} else if (MINUS_SIGN == e.character) {
				e.doit = false;
				if (isSigned()) {
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
						stopVerifyListener();
						control.setText(MINUS_SIGN + text);
						control.setSelection(caret);
						startVerifyListener();
					} else {
						flash();
					}
				} else {
					flash();
				}
			} else if ('\r' == e.character) {
				if (isDecimal()) {
					String newText = formatFraction(text);
					if (!newText.equals(text)) {
						stopVerifyListener();
						control.setText(newText);
						control.setSelection(newText.length());
						startVerifyListener();
					}
				}
			}
		}
	}

	/**
	 * For decimal ridgets: this focus listener will pad the fractional digits
	 * by adding '0's until the maximum number of fractional digits is reached
	 * (as specified by the ridgets precision value)
	 */
	private final class NumericFocusListener extends FocusAdapter {
		@Override
		public void focusLost(FocusEvent e) {
			if (isDecimal()) {
				Text control = (Text) e.widget;
				beautifyText(control);
			}
		}
	}

}
