/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.ridgets.menu;

import java.util.List;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IRadioButtonRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;

/**
 * 
 */
public interface IMenuFactory {

	List<IActionRidget> createPopupMenu();

	//
	// --- basic menu button
	//
	IActionRidget createButton(String label, IActionListener listener);

	IActionRidget createButton(String label, String target, String action);

	IActionRidget createButton(String label, String iconName, IActionListener listener);

	IActionRidget createButton(String label, String iconName, String target, String action);

	//
	// --- menu radio button
	//	
	IRadioButtonRidget createRadioButton(String label, IObservableValue observableValue, IActionListener listener);

	IRadioButtonRidget createRadioButton(String label, IObservableValue observableValue, String target, String action);

	IRadioButtonRidget createRadioButton(String label, String iconName, IObservableValue observableValue,
			IActionListener listener);

	IRadioButtonRidget createRadioButton(String label, String iconName, IObservableValue observableValue,
			String target, String action);

	//
	// --- menu toggle button (aka checkbox)
	//	
	IToggleButtonRidget createToggleButton(String label, IObservableValue observableValue, IActionListener listener);

	IToggleButtonRidget createToggleButton(String label, IObservableValue observableValue, String target, String action);

	IToggleButtonRidget createToggleButton(String label, String iconName, IObservableValue observableValue,
			IActionListener listener);

	IToggleButtonRidget createToggleButton(String label, String iconName, IObservableValue observableValue,
			String target, String action);

	//
	// --- menu separator
	//	
	IMenuSeparator createSeparator();
}
