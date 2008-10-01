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
import java.util.Locale;

import org.eclipse.core.databinding.BindingException;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.nebula.widgets.formattedtext.FormattedText;
import org.eclipse.nebula.widgets.formattedtext.NumberFormatter;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.INumericValueTextFieldRidget;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;
import org.eclipse.riena.ui.ridgets.ValueBindingSupport;
import org.eclipse.riena.ui.ridgets.validation.IValidationRuleStatus;
import org.eclipse.riena.ui.ridgets.validation.ValidationRuleStatus;
import org.eclipse.riena.ui.ridgets.validation.ValidatorCollection;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * Ridget for a 'numeric' SWT <code>Text</code> widget.
 * 
 * @see UIControlsFactory#createTextNumeric(org.eclipse.swt.widgets.Composite)
 */
public class NumericTextRidget extends AbstractEditableRidget implements INumericValueTextFieldRidget {

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private final FocusListener focusListener;
	private final KeyListener crKeyListener;
	private final ModifyListener modifyListener;
	private final Formatter formatter;

	private WritableValue valueExternal = new WritableValue(Long.valueOf(0), Long.class);
	private WritableValue valueInternal = new WritableValue(Long.valueOf(0), Long.class);

	private boolean isDirectWriting;
	private boolean isSigned;
	private boolean isGrouping;
	private DisposableFormattedText formattedText;

	public NumericTextRidget() {
		crKeyListener = new CRKeyListener();
		focusListener = new FocusManager();
		modifyListener = new SyncModifyListener();
		isDirectWriting = false;
		isSigned = true;
		isGrouping = true;
		formatter = new Formatter(isGrouping);
		addPropertyChangeListener(IMarkableRidget.PROPERTY_ENABLED, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				forceTextToControl(getText());
			}
		});
		addPropertyChangeListener(IMarkableRidget.PROPERTY_OUTPUT_ONLY, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				Text control = getUIControl();
				if (control != null && !control.isDisposed()) {
					control.setEditable(isOutputOnly() ? false : true);
				}
			}
		});
		getValueBindingSupport().bindToTarget(valueExternal);
	}

	/**
	 * @deprecated use BeansObservables.observeValue(ridget instance,
	 *             ITextFieldRidget.PROPERTY_TEXT);
	 */
	public IObservableValue getRidgetObservable() {
		return valueExternal;
	}

	@Override
	protected void checkUIControl(Object uiControl) {
		AbstractSWTRidget.assertType(uiControl, Text.class);
		if (uiControl != null) {
			int style = ((Text) uiControl).getStyle();
			if ((style & SWT.SINGLE) == 0) {
				throw new BindingException("Text widget must be SWT.SINGLE"); //$NON-NLS-1$
			}
		}
	}

	@Override
	protected synchronized void bindUIControl() {
		Text control = getUIControl();
		if (control != null) {
			formattedText = new DisposableFormattedText(control);
			formattedText.setFormatter(formatter);
			valueInternal.setValue(valueExternal.getValue());
			formattedText.setValue(valueInternal.getValue());
			control.setSelection(0, 0); // move cursor to 0
			control.setEditable(isOutputOnly() ? false : true);
			control.addKeyListener(crKeyListener);
			control.addFocusListener(focusListener);
			control.addModifyListener(modifyListener);
		}
	}

	@Override
	protected synchronized void unbindUIControl() {
		Text control = getUIControl();
		if (control != null) {
			control.removeKeyListener(crKeyListener);
			control.removeFocusListener(focusListener);
			control.removeModifyListener(modifyListener);
		}
		if (formattedText != null) {
			formattedText.dispose();
			formattedText = null;
		}
	}

	@Override
	public Text getUIControl() {
		return (Text) super.getUIControl();
	}

	public synchronized String getText() {
		return formatter.getDisplayString(((Long) valueInternal.getValue()).longValue());
	}

	/**
	 * TODO [ev] docs
	 */
	public synchronized void setText(String text) {
		throw new UnsupportedOperationException("setText(String) is not supported"); //$NON-NLS-1$
	}

	public synchronized boolean revalidate() {
		String oldValue = formatter.getDisplayString(((Long) valueExternal.getValue()).longValue());
		String newValue = formatter.getDisplayString();
		boolean blockEdit = checkOnEditRules(newValue).getCode() == IValidationRuleStatus.ERROR_BLOCK_WITH_FLASH;
		if (!blockEdit) {
			valueInternal.setValue(formattedText.getValue());
			firePropertyChange(ITextFieldRidget.PROPERTY_TEXT, oldValue, newValue);
			boolean blockUpdate = checkOnUpdateRules(newValue).getCode() == IValidationRuleStatus.ERROR_BLOCK_WITH_FLASH;
			if (!blockUpdate) {
				valueExternal.setValue(formattedText.getValue());
			}
		}
		checkRules();
		return !isErrorMarked();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Invoking this method will copy the model value into the ridget and the
	 * widget regardless of the validation outcome. If the model value does not
	 * pass validation, the error marker will be set.
	 */
	@Override
	public synchronized void updateFromModel() {
		super.updateFromModel();
		valueInternal.setValue(valueExternal.getValue());
		if (isEnabled()) {
			if (formattedText != null) {
				formattedText.setValue(valueInternal.getValue());
			}
			checkRules();
		}
	}

	public synchronized boolean isDirectWriting() {
		return isDirectWriting;
	}

	public synchronized void setDirectWriting(boolean directWriting) {
		if (this.isDirectWriting != directWriting) {
			this.isDirectWriting = directWriting;
		}
	}

	@Override
	public boolean isDisableMandatoryMarker() {
		return formatter.getDisplayString().length() > 0;
	}

	public int getAlignment() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("not implemented"); //$NON-NLS-1$
	}

	public void setAlignment(int alignment) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("not implemented"); //$NON-NLS-1$
	}

	public boolean isGrouping() {
		return isGrouping;
	}

	public boolean isMarkNegative() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("not implemented"); //$NON-NLS-1$
	}

	public boolean isSigned() {
		return isSigned;
	}

	public synchronized void setGrouping(boolean useGrouping) {
		if (isGrouping != useGrouping) {
			isGrouping = useGrouping;
			Object ftValue = formattedText == null ? null : formattedText.getValue();
			formatter.setGrouping(isGrouping);
			if (formattedText != null) {
				formattedText.setValue(ftValue);
			}
		}
	}

	public void setMarkNegative(boolean mustBeMarked) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("not implemented"); //$NON-NLS-1$
	}

	public void setSigned(boolean signed) {
		if (isSigned != signed) {
			boolean oldValue = isSigned;
			isSigned = signed;
			firePropertyChange(PROPERTY_SIGNED, oldValue, isSigned);
		}
	}

	// helping methods
	// ////////////////

	private void checkRules() {
		String textValue = formatter.getDisplayString();
		IStatus onEdit = checkOnEditRules(textValue);
		IStatus onUpdate = checkOnUpdateRules(textValue);
		IStatus joinedStatus = ValidationRuleStatus.join(new IStatus[] { onEdit, onUpdate });
		validationRulesChecked(joinedStatus);
	}

	private IStatus checkOnEditRules(String newValue) {
		ValueBindingSupport vbs = getValueBindingSupport();
		ValidatorCollection onEditValidators = vbs.getOnEditValidators();
		IStatus result = onEditValidators.validate(newValue);
		return result;
	}

	private IStatus checkOnUpdateRules(String newValue) {
		ValueBindingSupport vbs = getValueBindingSupport();
		ValidatorCollection afterGetValidators = vbs.getAfterGetValidators();
		IStatus result = afterGetValidators.validate(newValue);
		return result;
	}

	private synchronized void forceTextToControl(String newValue) {
		Text control = getUIControl();
		if (control != null) {
			boolean hideValue = !isEnabled() && MarkerSupport.HIDE_DISABLED_RIDGET_CONTENT;
			formatter.setText(hideValue ? EMPTY_STRING : newValue);
		}
	}

	private synchronized void updateTextValue() {
		String oldValue = formatter.getDisplayString(((Long) valueExternal.getValue()).longValue());
		String newValue = formatter.getDisplayString();
		if (!oldValue.equals(newValue)) {
			IStatus editStatus = checkOnEditRules(newValue);
			boolean blockEdit = editStatus.getCode() == IValidationRuleStatus.ERROR_BLOCK_WITH_FLASH;
			if (!blockEdit) {
				valueInternal.setValue(formattedText.getValue());
				firePropertyChange(ITextFieldRidget.PROPERTY_TEXT, oldValue, newValue);
				if (editStatus.isOK()) {
					boolean blockUpdate = checkOnUpdateRules(newValue).getCode() == IValidationRuleStatus.ERROR_BLOCK_WITH_FLASH;
					if (!blockUpdate) {
						valueExternal.setValue(formattedText.getValue());
					}
				}
			}
			checkRules();
		}
	}

	private synchronized void updateTextValueWhenDirectWriting() {
		if (isDirectWriting) {
			updateTextValue();
		}
	}

	// helping classes
	// ////////////////

	/**
	 * Update text value in ridget when ENTER is pressed
	 */
	private final class CRKeyListener extends KeyAdapter implements KeyListener {
		@Override
		public void keyReleased(KeyEvent e) {
			if (e.character == '\r') {
				updateTextValue();
			}
		}
	}

	/**
	 * Manages activities trigger by focus changed:
	 * <ol>
	 * <li>select single line text fields, when focus is gained by keyboard</li>
	 * <li>update text value in ridget, when focus is lost</li>
	 * <ol>
	 */
	private final class FocusManager implements FocusListener {
		public void focusGained(FocusEvent e) {
			if (isFocusable()) {
				Text text = (Text) e.getSource();
				// if not multi line text field
				if ((text.getStyle() & SWT.MULTI) == 0) {
					text.selectAll();
				}
			}
		}

		public void focusLost(FocusEvent e) {
			updateTextValue();
		}
	}

	/**
	 * Updates the text value in the ridget, if direct writing is enabled.
	 */
	private final class SyncModifyListener implements ModifyListener {
		public void modifyText(ModifyEvent e) {
			updateTextValueWhenDirectWriting();
			boolean hasText = ((Text) e.widget).getText().length() > 0;
			disableMandatoryMarkers(hasText);
		}
	}

	/**
	 * Specialized NumberFormatter class, that can turn 'grouping' on (i.e.
	 * ###,###) and off (i.e. #######).
	 * 
	 * @see #setGrouping(boolean)
	 */
	private static final class Formatter extends NumberFormatter {
		private static final String PATTERN_GROUPED = "-#,##0"; //$NON-NLS-1$
		private static final String PATTERN_UNGROUPED = "-###0"; //$NON-NLS-1$

		public Formatter(boolean isGrouping) {
			super(isGrouping ? PATTERN_GROUPED : PATTERN_UNGROUPED);
			setDecimalSeparatorAlwaysShown(false);
			setFixedLengths(false, true);
		}

		public synchronized void setGrouping(boolean isGrouping) {
			String pattern = isGrouping ? PATTERN_GROUPED : PATTERN_UNGROUPED;
			setPatterns(pattern, null, Locale.getDefault());
		}

		public String getDisplayString(long value) {
			return nfDisplay.format(value);
		}

		public void setText(String text) {
			super.updateText(text, text.length());
		}

		/**
		 * Copy and override original for bugfix reg. the '-' symbol
		 */
		@Override
		protected int format(int curseur) {
			int i = prefixLen + (negative ? 1 : 0);
			char c;

			// Inserts zeros in the int part
			while (intCount < zeroIntLen) {
				editValue.insert(i, '0');
				intCount++;
				curseur++;
			}
			while (intCount > zeroIntLen) {
				if (editValue.charAt(i) == '0') {
					intCount--;
				} else if (editValue.charAt(i) != symbols.getGroupingSeparator()) {
					break;
				}
				editValue.deleteCharAt(i);
				if (curseur > i)
					curseur--;
			}

			// Recreates the groups in the int part
			if (groupLen > 0) {
				int n = intCount > groupLen ? groupLen - intCount % groupLen : 0;
				if (n == groupLen) {
					n = 0;
				}
				for (; i < editValue.length() - suffixLen; i++) {
					c = editValue.charAt(i);
					// -- bugfix begin: do not abort on '-' ---
					if (c == '-') {
						continue;
					}
					// -- bugfix end ---
					else if (c >= '0' && c <= '9') {
						if (n == groupLen) {
							editValue.insert(i, symbols.getGroupingSeparator());
							if (curseur >= i) {
								curseur++;
							}
							n = 0;
						} else {
							n++;
						}
					} else if (c == symbols.getGroupingSeparator()) {
						if (n != groupLen) {
							editValue.deleteCharAt(i);
							if (curseur >= i) {
								curseur--;
							}
							i--;
						} else {
							n = 0;
						}
					} else if (c == symbols.getDecimalSeparator()) {
						if (i > 0 && editValue.charAt(i - 1) == symbols.getGroupingSeparator()) {
							editValue.deleteCharAt(i - 1);
							if (curseur >= i) {
								curseur--;
							}
							i--;
						}
						break;
					} else {
						break;
					}
				}
			}

			// Truncates / completes by zeros the decimal part
			i = editValue.indexOf(EMPTY + symbols.getDecimalSeparator());
			if (i < 0 && (zeroDecimalLen > 0 || alwaysShowDec)) {
				i = editValue.length() - suffixLen;
				editValue.insert(i, symbols.getDecimalSeparator());
			}
			if (i >= 0) {
				int j;
				for (j = i + 1; j < editValue.length() - suffixLen;) {
					c = editValue.charAt(j);
					if (c == symbols.getGroupingSeparator()) {
						editValue.deleteCharAt(j);
					} else if (c < '0' || c > '9') {
						break;
					} else {
						j++;
					}
				}
				if (fixedDec && (j - i - 1) > decimalLen) {
					editValue.delete(i + decimalLen + 1, j);
					if (curseur > i + decimalLen) {
						curseur = i + decimalLen;
					}
				} else {
					while ((j - i - 1) < zeroDecimalLen) {
						editValue.insert(j++, '0');
					}
				}
			}

			return curseur;
		}
	}

	/**
	 * Specialized FormattedText class, that can detach itself cleanly from the
	 * given Text control. This is necessary because the same text control might
	 * be introduced to the ridget several times.
	 * <p>
	 * This class also:
	 * <ul>
	 * <li>ensures that non-supported key strokes are blocked</li> <li>negates a
	 * positive ridget value, when '-' is pressed</li>
	 * </ul>
	 * 
	 * @see #dispose();
	 */
	private final class DisposableFormattedText extends FormattedText implements KeyListener {

		private final int[] VALID_KEYCODES = { 16777224 /* end */, 16777223 /* home */, 16777220 /* right */,
				16777219 /* left */, 127 /* del */, 8 /* backspace */, 97 /* 'a' */, 262144 /* ctrl */, 44 /* ',' */, 46 /* '.' */};

		DisposableFormattedText(Text control) {
			super(control);
			control.addKeyListener(this);
		}

		/**
		 * Remove all listeners added by this class from the text control
		 */
		public void dispose() {
			if (text != null && !text.isDisposed()) {
				Listener[] listeners = text.getListeners(SWT.FocusIn);
				for (Listener listener : listeners) {
					text.removeListener(SWT.FocusIn, listener);
					text.removeListener(SWT.FocusOut, listener);
				}
				if (getFormatter() != null) {
					text.removeVerifyListener(getFormatter());
				}
				text.removeKeyListener(this);
			}
		}

		public void keyPressed(KeyEvent e) {
			char ch = e.character;
			if (!(Character.isDigit(ch) || isValid(e.keyCode))) {
				e.doit = false;
			}
			if (e.character == '-' && isSigned) {
				Number value = (Number) getValue();
				if (value.longValue() > 0) {
					Number newValue = Long.valueOf(value.longValue() * -1);
					int caretPos = text.getCaretPosition() + 1;
					setValue(newValue);
					text.setSelection(caretPos, caretPos);
				}
			}
		}

		public void keyReleased(KeyEvent e) {
			// unused
		}

		// helping methods
		//////////////////

		private boolean isValid(int keyCode) {
			boolean result = false;
			for (int i = 0; !result && i < VALID_KEYCODES.length; i++) {
				result = VALID_KEYCODES[i] == keyCode;
			}
			return result;
		}

	}

}
