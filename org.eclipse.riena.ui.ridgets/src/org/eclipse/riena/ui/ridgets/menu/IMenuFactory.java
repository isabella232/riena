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
package org.eclipse.riena.ui.ridgets.menu;

import java.util.List;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.riena.ui.ridgets.IActionListener;

/**
 * 
 */
public interface IMenuFactory {

	List<IMenuItem> createPopupMenu();

	//
	// --- basic menu button
	//
	IMenuButton createButton(String label, IActionListener listener);

	IMenuButton createButton(String label, String target, String action);

	IMenuButton createButton(String label, String iconName, IActionListener listener);

	IMenuButton createButton(String label, String iconName, String target, String action);

	//
	// --- menu radio button
	//	
	IMenuRadioButton createRadioButton(String label, IObservableValue observableValue, IActionListener listener);

	IMenuRadioButton createRadioButton(String label, IObservableValue observableValue, String target, String action);

	IMenuRadioButton createRadioButton(String label, String iconName, IObservableValue observableValue,
			IActionListener listener);

	IMenuRadioButton createRadioButton(String label, String iconName, IObservableValue observableValue, String target,
			String action);

	//
	// --- menu toggle button (aka checkbox)
	//	
	IMenuToggleButton createToggleButton(String label, IObservableValue observableValue, IActionListener listener);

	IMenuToggleButton createToggleButton(String label, IObservableValue observableValue, String target, String action);

	IMenuToggleButton createToggleButton(String label, String iconName, IObservableValue observableValue,
			IActionListener listener);

	IMenuToggleButton createToggleButton(String label, String iconName, IObservableValue observableValue,
			String target, String action);

	//
	// --- menu separator
	//	
	IMenuSeparator createSeparator();
}
