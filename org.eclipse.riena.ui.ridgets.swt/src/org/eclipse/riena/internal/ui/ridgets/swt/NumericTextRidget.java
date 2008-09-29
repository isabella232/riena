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

import java.util.Locale;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.nebula.widgets.formattedtext.FormattedText;
import org.eclipse.nebula.widgets.formattedtext.NumberFormatter;
import org.eclipse.riena.ui.ridgets.INumericValueTextFieldRidget;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;
import org.eclipse.riena.ui.ridgets.ValueBindingSupport;
import org.eclipse.riena.ui.ridgets.validation.ValidationRuleStatus;
import org.eclipse.riena.ui.ridgets.validation.ValidatorCollection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * TODO [ev] docs
 */
public class NumericTextRidget extends AbstractEditableRidget implements INumericValueTextFieldRidget {

	private final ModifyListener modifyListener = new ModifyListener() {
		public void modifyText(ModifyEvent e) {
			String text = formatter.getDisplayString();
			firePropertyChange(ITextFieldRidget.PROPERTY_TEXT, "", text); //$NON-NLS-1$
		}
	};
	private final WritableValue value;
	private final Formatter formatter;

	private DisposableFormattedText formattedText;
	private boolean isSigned;
	private boolean isGrouping;

	public NumericTextRidget() {
		isSigned = true;
		isGrouping = true;
		value = new WritableValue(Long.valueOf(0), Long.class);
		value.addValueChangeListener(new IValueChangeListener() {
			public void handleValueChange(ValueChangeEvent event) {
				Object value = event.getObservableValue().getValue();
				if (formattedText != null) {
					formattedText.setValue(value);
				} else {
					formatter.setValue(value);
				}
			}
		});
		formatter = new Formatter(isGrouping);
		getValueBindingSupport().bindToTarget(value);
	}

	@Override
	protected void checkUIControl(Object uiControl) {
		AbstractSWTRidget.assertType(uiControl, Text.class);
	}

	@Override
	protected void bindUIControl() {
		Text control = getUIControl();
		if (control != null) {
			formattedText = new DisposableFormattedText(control);
			formattedText.setFormatter(formatter);
			formattedText.setValue(value.getValue());
			control.addModifyListener(modifyListener);
		}
	}

	@Override
	protected void unbindUIControl() {
		if (formattedText != null) {
			formattedText.dispose();
			formattedText = null;
		}
		Text control = getUIControl();
		if (control != null) {
			control.removeModifyListener(modifyListener);
		}
	}

	@Override
	public Text getUIControl() {
		return (Text) super.getUIControl();
	}

	@Override
	public boolean isDisableMandatoryMarker() {
		return true;
	}

	public boolean isGrouping() {
		return isGrouping;
	}

	public boolean isSigned() {
		return isSigned;
	}

	public void setGrouping(boolean useGrouping) {
		if (isGrouping != useGrouping) {
			isGrouping = useGrouping;
			if (formatter != null) {
				Object ftValue = formattedText.getValue();
				formatter.setGrouping(isGrouping);
				formattedText.setValue(ftValue);
			}
		}
	}

	public void setSigned(boolean signed) {
		// TODO Auto-generated method stub

	}

	public String getText() {
		return formatter.getDisplayString();
	}

	public boolean isDirectWriting() {
		return true;
	}

	public int getAlignment() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("not implemented"); //$NON-NLS-1$
	}

	public void setAlignment(int alignment) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("not implemented"); //$NON-NLS-1$
	}

	public void setDirectWriting(boolean directWriting) {
		// TODO Auto-generated method stub
	}

	/**
	 * TODO [ev] docs
	 */
	public void setText(String text) {
		throw new UnsupportedOperationException("setText() is not supported"); //$NON-NLS-1$
	}

	public boolean revalidate() {
		return true;
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
		String textValue = formatter.getDisplayString();
		IStatus onEdit = checkOnEditRules(textValue);
		IStatus onUpdate = checkOnUpdateRules(textValue);
		IStatus joinedStatus = ValidationRuleStatus.join(new IStatus[] { onEdit, onUpdate });
		validationRulesChecked(joinedStatus);
	}

	public IObservableValue getRidgetObservable() {
		return value;
	}

	// helping methods
	// ////////////////

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

	// helping classes
	//////////////////

	/**
	 * TODO [ev] docs
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
					if (c == '-') {
						continue;
					} else if (c >= '0' && c <= '9') {
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
	 * TODO [ev] docs
	 */
	private final class DisposableFormattedText extends FormattedText implements KeyListener {

		private final int[] VALID_KEYCODES = { 16777224 /* end */, 16777223 /* home */, 16777220 /* right */,
				16777219 /* left */, 127 /* del */, 8 /* backspace */, 97 /* 'a' */, 262144 /* ctrl */, 44 /* ',' */, 46 /* '.' */};

		DisposableFormattedText(Text control) {
			super(control);
			control.addKeyListener(this);
		}

		public void dispose() {
			if (text != null && !text.isDisposed()) {
				// TODO [ev] should make sure we don't remove wrong listener
				Listener[] listeners = text.getListeners(SWT.FocusIn);
				for (Listener listener : listeners) {
					if (listener instanceof FocusListener) {
						System.out.println("Removing listener: " + listener); // TODO [ev] ex //$NON-NLS-1$
						text.removeFocusListener((FocusListener) listener);
					}
				}
				if (getFormatter() != null) {
					text.removeVerifyListener(getFormatter());
				}
				text.removeKeyListener(this);
			}
		}

		public void keyPressed(KeyEvent e) {
			System.out.println(e);
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
