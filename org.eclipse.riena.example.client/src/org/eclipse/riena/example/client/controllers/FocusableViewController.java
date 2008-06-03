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
package org.eclipse.riena.example.client.controllers;

import org.eclipse.core.runtime.Assert;
import org.eclipse.riena.example.client.views.SystemPropertiesView;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleNodeViewController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;

/**
 * Controller for the {@link SystemPropertiesView} example.
 */
public class FocusableViewController extends SubModuleNodeViewController {

	private IToggleButtonRidget checkVisible;
	private IToggleButtonRidget buttonA0;
	private IToggleButtonRidget buttonA1;
	private IToggleButtonRidget buttonA2;
	private IToggleButtonRidget buttonA3;
	private IToggleButtonRidget buttonA4;
	private IToggleButtonRidget buttonB0;
	private IToggleButtonRidget buttonC0;
	private IToggleButtonRidget buttonC1;
	private IToggleButtonRidget buttonD0;
	private IToggleButtonRidget buttonD1;
	private IToggleButtonRidget buttonD2;

	private ITextFieldRidget textA0;
	private ITextFieldRidget textA1;
	private ITextFieldRidget textA2;
	private ITextFieldRidget textA3;
	private ITextFieldRidget textA4;
	private ITextFieldRidget textB0;
	private ITextFieldRidget textC0;
	private ITextFieldRidget textC1;
	private ITextFieldRidget textD0;
	private ITextFieldRidget textD1;
	private ITextFieldRidget textD2;

	private IToggleButtonRidget[] checkButtons;
	private ITextFieldRidget[] textRidgets;

	public IToggleButtonRidget getCheckVisible() {
		return checkVisible;
	}

	public void setCheckVisible(IToggleButtonRidget checkVisible) {
		this.checkVisible = checkVisible;
	}

	public IToggleButtonRidget getButtonA0() {
		return buttonA0;
	}

	public void setButtonA0(IToggleButtonRidget buttonA0) {
		this.buttonA0 = buttonA0;
	}

	public IToggleButtonRidget getButtonA1() {
		return buttonA1;
	}

	public void setButtonA1(IToggleButtonRidget buttonA1) {
		this.buttonA1 = buttonA1;
	}

	public IToggleButtonRidget getButtonA2() {
		return buttonA2;
	}

	public void setButtonA2(IToggleButtonRidget buttonA2) {
		this.buttonA2 = buttonA2;
	}

	public IToggleButtonRidget getButtonA3() {
		return buttonA3;
	}

	public void setButtonA3(IToggleButtonRidget buttonA3) {
		this.buttonA3 = buttonA3;
	}

	public IToggleButtonRidget getButtonA4() {
		return buttonA4;
	}

	public void setButtonA4(IToggleButtonRidget buttonA4) {
		this.buttonA4 = buttonA4;
	}

	public IToggleButtonRidget getButtonB0() {
		return buttonB0;
	}

	public void setButtonB0(IToggleButtonRidget buttonB0) {
		this.buttonB0 = buttonB0;
	}

	public IToggleButtonRidget getButtonC0() {
		return buttonC0;
	}

	public void setButtonC0(IToggleButtonRidget buttonC0) {
		this.buttonC0 = buttonC0;
	}

	public IToggleButtonRidget getButtonC1() {
		return buttonC1;
	}

	public void setButtonC1(IToggleButtonRidget buttonC1) {
		this.buttonC1 = buttonC1;
	}

	public IToggleButtonRidget getButtonD0() {
		return buttonD0;
	}

	public void setButtonD0(IToggleButtonRidget buttonD0) {
		this.buttonD0 = buttonD0;
	}

	public IToggleButtonRidget getButtonD1() {
		return buttonD1;
	}

	public void setButtonD1(IToggleButtonRidget buttonD1) {
		this.buttonD1 = buttonD1;
	}

	public IToggleButtonRidget getButtonD2() {
		return buttonD2;
	}

	public void setButtonD2(IToggleButtonRidget buttonD2) {
		this.buttonD2 = buttonD2;
	}

	public ITextFieldRidget getTextA0() {
		return textA0;
	}

	public void setTextA0(ITextFieldRidget textA0) {
		this.textA0 = textA0;
	}

	public ITextFieldRidget getTextA1() {
		return textA1;
	}

	public void setTextA1(ITextFieldRidget textA1) {
		this.textA1 = textA1;
	}

	public ITextFieldRidget getTextA2() {
		return textA2;
	}

	public void setTextA2(ITextFieldRidget textA2) {
		this.textA2 = textA2;
	}

	public ITextFieldRidget getTextA3() {
		return textA3;
	}

	public void setTextA3(ITextFieldRidget textA3) {
		this.textA3 = textA3;
	}

	public ITextFieldRidget getTextA4() {
		return textA4;
	}

	public void setTextA4(ITextFieldRidget textA4) {
		this.textA4 = textA4;
	}

	public ITextFieldRidget getTextB0() {
		return textB0;
	}

	public void setTextB0(ITextFieldRidget textB0) {
		this.textB0 = textB0;
	}

	public ITextFieldRidget getTextC0() {
		return textC0;
	}

	public void setTextC0(ITextFieldRidget textC0) {
		this.textC0 = textC0;
	}

	public ITextFieldRidget getTextC1() {
		return textC1;
	}

	public void setTextC1(ITextFieldRidget textC1) {
		this.textC1 = textC1;
	}

	public ITextFieldRidget getTextD0() {
		return textD0;
	}

	public void setTextD0(ITextFieldRidget textD0) {
		this.textD0 = textD0;
	}

	public ITextFieldRidget getTextD1() {
		return textD1;
	}

	public void setTextD1(ITextFieldRidget textD1) {
		this.textD1 = textD1;
	}

	public ITextFieldRidget getTextD2() {
		return textD2;
	}

	public void setTextD2(ITextFieldRidget textD2) {
		this.textD2 = textD2;
	}

	public FocusableViewController(ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	public void afterBind() {
		super.afterBind();
		initRidgets();
	}

	/**
	 * Binds and updates the ridgets.
	 */
	private void initRidgets() {
		checkButtons = new IToggleButtonRidget[] { buttonA0, buttonA1, buttonA2, buttonA3, buttonA4, buttonB0 };
		textRidgets = new ITextFieldRidget[] { textA0, textA1, textA2, textA3, textA4, textB0 };
		// checkButtons = new IToggleButtonRidget[] { buttonA0, buttonA1,
		// buttonA2, buttonA3, buttonA4, buttonB0,
		// buttonC0, buttonC1, buttonD0, buttonD1, buttonD2, };
		// textRidgets = new ITextFieldRidget[] { textA0, textA1, textA2, textA3
		// , textA4, textB0, textC0, textC1, textD0,
		// textD1, textD2, };

		checkVisible.setText("show checkboxes");
		checkVisible.setSelected(true);
		checkVisible.addListener(new IActionListener() {
			public void callback() {
				boolean show = checkVisible.isSelected();
				for (IToggleButtonRidget check : checkButtons) {
					check.setVisible(show);
				}
			}
		});

		Assert.isLegal(checkButtons.length == textRidgets.length);
		for (int i = 0; i < checkButtons.length; i++) {
			IToggleButtonRidget check = checkButtons[i];
			check.setText("make focusable");
			check.setSelected(true);
			IActionListener listener = new ChangeFocusableCallback(check, textRidgets[i]);
			check.addListener(listener);
		}

		for (int i = 0; i < textRidgets.length; i++) {
			textRidgets[i].setText("Text Field #" + i);
		}
	}

	// helping classes
	// ////////////////

	private static class ChangeFocusableCallback implements IActionListener {
		private final IToggleButtonRidget buttonCheck;
		private final ITextFieldRidget textRidget;

		private ChangeFocusableCallback(IToggleButtonRidget buttonCheck, ITextFieldRidget textRidget) {
			this.buttonCheck = buttonCheck;
			this.textRidget = textRidget;
		}

		public void callback() {
			boolean isSelected = buttonCheck.isSelected();
			textRidget.setFocusable(isSelected);
		}
	}

}
