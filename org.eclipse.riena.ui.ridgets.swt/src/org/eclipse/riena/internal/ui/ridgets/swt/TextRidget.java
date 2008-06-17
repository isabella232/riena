/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;
import org.eclipse.riena.ui.ridgets.ValueBindingSupport;
import org.eclipse.riena.ui.ridgets.validation.ValidatorCollection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Text;

/**
 * Ridget for an SWT <code>Text</code> widget.
 */
public class TextRidget extends AbstractEditableRidget implements ITextFieldRidget {

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private final FocusListener focusListener;
	private final KeyListener crKeyListener;
	private final ModifyListener modifyListener;
	private final ValidationListener verifyListener;
	private String textValue = EMPTY_STRING;
	private boolean isDirectWriting;

	/* True indicates that an updateFromModel() is in progress */
	private boolean isUpdatingFromModel = false;
	/* True indicates that setText was aborted because the value was illegal */
	private boolean setTextAborted = false;

	public TextRidget() {
		crKeyListener = new CRKeyListener();
		focusListener = new FocusManager();
		modifyListener = new SyncModifyListener();
		verifyListener = new ValidationListener();
		isDirectWriting = false;
		isUpdatingFromModel = false;
		setTextAborted = false;
	}

	/**
	 * @deprecated use BeansObservables.observeValue(ridget instance,
	 * 	ITextFieldRidget.PROPERTY_TEXT);
	 */
	public final IObservableValue getRidgetObservable() {
		return BeansObservables.observeValue(this, ITextFieldRidget.PROPERTY_TEXT);
	}

	@Override
	protected void checkUIControl(Object uiControl) {
		AbstractSWTRidget.assertType(uiControl, Text.class);
	}

	@Override
	protected void bindUIControl() {
		Text control = getUIControl();
		if (control != null) {
			control.setText(textValue);
			control.setSelection(0, 0); // move cursor to 0
			control.addKeyListener(crKeyListener);
			control.addFocusListener(focusListener);
			control.addModifyListener(modifyListener);
			control.addVerifyListener(verifyListener);
		}
	}

	@Override
	protected void unbindUIControl() {
		Text control = getUIControl();
		if (control != null) {
			control.removeKeyListener(crKeyListener);
			control.removeFocusListener(focusListener);
			control.removeModifyListener(modifyListener);
			control.removeVerifyListener(verifyListener);
		}
	}

	@Override
	public Text getUIControl() {
		return (Text) super.getUIControl();
	}

	public String getText() {
		return textValue;
	}

	public synchronized void setText(String text) {
		if (hasErrors(text)) {
			if (isUpdatingFromModel) {
				setTextAborted = true;
			} else {
				throw new IllegalArgumentException("Invalid text:" + text); //$NON-NLS-1$
			}
		} else {
			String oldValue = textValue;
			textValue = text;
			setTextToControl(textValue);
			firePropertyChange(ITextFieldRidget.PROPERTY_TEXT, oldValue, textValue);
		}
	}

	@Override
	public synchronized void updateFromModel() {
		isUpdatingFromModel = true;
		try {
			super.updateFromModel();
			if (setTextAborted) {
				throw new IllegalArgumentException("Invalid text in model"); //$NON-NLS-1$
			}
		} finally {
			isUpdatingFromModel = false;
			setTextAborted = false;
		}
	}

	public boolean isDirectWriting() {
		return isDirectWriting;
	}

	public void setDirectWriting(boolean directWriting) {
		if (this.isDirectWriting != directWriting) {
			this.isDirectWriting = directWriting;
		}
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

	private boolean hasErrors(String newValue) {
		IStatus onUpdate = checkOnUpdateRules(newValue);
		IStatus onEdit = checkOnEditRules(newValue);
		boolean result = !onUpdate.isOK() || !onEdit.isOK();
		return result;
	}

	private void setTextToControl(String newValue) {
		Text control = getUIControl();
		if (control != null) {
			control.setText(newValue);
			control.setSelection(0, 0);
		}
	}

	private void updateTextValue() {
		String oldValue = textValue;
		String newValue = getUIControl().getText();
		if (!oldValue.equals(newValue)) {
			textValue = newValue;
			firePropertyChange(ITextFieldRidget.PROPERTY_TEXT, oldValue, newValue);
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
			if (isDirectWriting) {
				updateTextValue();
			}
		}
	}

	/**
	 * Validation listener that checks 'on edit' validation rules when the text
	 * widget's contents are modified by the user. If the next text value does
	 * not pass the test, the change will be rejected.
	 */
	private final class ValidationListener implements VerifyListener {

		public synchronized void verifyText(VerifyEvent e) {
			String newText = getText(e);
			IStatus status = checkOnEditRules(newText);
			validationRulesChecked(status);
			e.doit = status.isOK();
		}

		private String getText(VerifyEvent e) {
			Text widget = (Text) e.widget;
			String oldText = widget.getText();
			String newText;
			// deletion
			if (e.keyCode == 127 || e.keyCode == 8) {
				newText = oldText.substring(0, e.start) + oldText.substring(e.end);
			} else { // addition / replace
				newText = oldText.substring(0, e.start) + e.text + oldText.substring(e.end);
			}
			return newText;
		}
	}

}
