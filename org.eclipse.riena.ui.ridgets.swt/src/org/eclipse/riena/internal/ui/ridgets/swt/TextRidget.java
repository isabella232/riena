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
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Text;

/**
 * Ridget for an SWT <code>Text</code> widget.
 */
public class TextRidget extends AbstractEditableRidget implements ITextFieldRidget {

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private final FocusListener lostFocusListener;
	private final KeyListener crKeyListener;
	private final ModifyListener modifyListener;
	private String textValue = EMPTY_STRING;
	private boolean isDirectWriting;

	public TextRidget() {
		crKeyListener = new CRKeyListener();
		lostFocusListener = new LostFocusListener();
		modifyListener = new SyncModifyListener();
		isDirectWriting = false;
	}

	/**
	 * @deprecated use BeansObservables.observeValue(ridget instance,
	 *             ITextFieldRidget.PROPERTY_TEXT);
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
			control.addFocusListener(lostFocusListener);
			control.addModifyListener(modifyListener);
		}
	}

	@Override
	protected void unbindUIControl() {
		Text control = getUIControl();
		if (control != null) {
			control.removeKeyListener(crKeyListener);
			control.removeFocusListener(lostFocusListener);
			control.removeModifyListener(modifyListener);
		}
	}

	@Override
	public Text getUIControl() {
		return (Text) super.getUIControl();
	}

	public String getText() {
		return textValue;
	}

	public void setText(String text) {
		setText(text, true);
	}

	public boolean isDirectWriting() {
		return isDirectWriting;
	}

	public void setDirectWriting(boolean directWriting) {
		if (this.isDirectWriting != directWriting) {
			this.isDirectWriting = directWriting;
		}
	}

	@Override
	public void updateFromModel() {
		super.updateFromModel();
	}

	// helping methods
	// ////////////////

	private void setText(String newValue, boolean updateWidget) {
		String oldValue = textValue;
		textValue = newValue;
		if (updateWidget) {
			setTextToControl(textValue);
		}
		getValueBindingSupport().updateFromTarget();
		firePropertyChange(ITextFieldRidget.PROPERTY_TEXT, oldValue, textValue);
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

	private final class CRKeyListener extends KeyAdapter implements KeyListener {
		@Override
		public void keyReleased(KeyEvent e) {
			if (e.character == '\r') {
				updateTextValue();
			}
		}
	}

	private final class LostFocusListener extends FocusAdapter implements FocusListener {
		@Override
		public void focusLost(FocusEvent e) {
			updateTextValue();
		}
	}

	private final class SyncModifyListener implements ModifyListener {
		public void modifyText(ModifyEvent e) {
			if (isDirectWriting) {
				updateTextValue();
			}
		}
	}

}
