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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.riena.example.client.views.TreeSubModuleView;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.ISelectableRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.util.beans.WordNode;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;

/**
 * Controller for the {@link TreeSubModuleView} example.
 */
public class TableSubModuleController extends SubModuleController {

	private ITableRidget table;
	private IActionRidget buttonAddSibling;
	private IActionRidget buttonRename;
	private IActionRidget buttonDelete;

	public ITableRidget getTable() {
		return table;
	}

	public void setTable(ITableRidget table) {
		this.table = table;
	}

	public IActionRidget getButtonAddSibling() {
		return buttonAddSibling;
	}

	public void setButtonAddSibling(IActionRidget buttonAddSibling) {
		this.buttonAddSibling = buttonAddSibling;
	}

	public IActionRidget getButtonRename() {
		return buttonRename;
	}

	public void setButtonRename(IActionRidget buttonRename) {
		this.buttonRename = buttonRename;
	}

	public IActionRidget getButtonDelete() {
		return buttonDelete;
	}

	public void setButtonDelete(IActionRidget buttonDelete) {
		this.buttonDelete = buttonDelete;
	}

	public TableSubModuleController() {
		this(null);
	}

	public TableSubModuleController(ISubModuleNode navigationNode) {
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
		final List<WordNode> input = createInput();
		String[] columnPropertyNames = { "word", "upperCase", "ACount" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		String[] columnHeaders = { "Word", "Uppercase", "A Count" };
		table.bindToModel(new WritableList(input, WordNode.class), WordNode.class, columnPropertyNames, columnHeaders);
		table.setComparator(0, new StringComparator());
		table.setComparator(1, new StringComparator());
		table.setColumnSortable(2, false);
		table.setSelectionType(ISelectableRidget.SelectionType.SINGLE);
		table.setSelection(0);

		table.addDoubleClickListener(new IActionListener() {
			public void callback() {
				WordNode node = (WordNode) table.getSingleSelectionObservable().getValue();
				if (node != null) {
					boolean isUpperCase = !node.isUpperCase();
					node.setUpperCase(isUpperCase);
				}
			}
		});

		buttonAddSibling.setText("&Add");
		buttonAddSibling.addListener(new IActionListener() {
			public void callback() {
				WordNode newNode = new WordNode("A_NEW_SIBLING");
				input.add(newNode);
				table.updateFromModel();
				table.setSelection(newNode);
			}
		});

		buttonRename.setText("&Modify");
		buttonRename.addListener(new IActionListener() {
			public void callback() {
				WordNode node = (WordNode) table.getSingleSelectionObservable().getValue();
				if (node != null) {
					String newValue = getNewValue(node.getWordIgnoreUppercase());
					if (newValue != null) {
						node.setWord(newValue);
					}
				}
			}
		});

		buttonDelete.setText("&Delete");
		buttonDelete.addListener(new IActionListener() {
			public void callback() {
				WordNode node = (WordNode) table.getSingleSelectionObservable().getValue();
				input.remove(node);
				table.updateFromModel();
			}
		});

		final IObservableValue viewerSelection = table.getSingleSelectionObservable();
		IObservableValue hasSelection = new ComputedValue(Boolean.TYPE) {
			protected Object calculate() {
				return Boolean.valueOf(viewerSelection.getValue() != null);
			}
		};
		DataBindingContext dbc = new DataBindingContext();
		bindEnablementToValue(dbc, buttonDelete, hasSelection);
		bindEnablementToValue(dbc, buttonRename, hasSelection);
	}

	private void bindEnablementToValue(DataBindingContext dbc, IMarkableRidget ridget, IObservableValue value) {
		dbc.bindValue(BeansObservables.observeValue(ridget, IMarkableRidget.PROPERTY_ENABLED), value, null, null);
	}

	private String getNewValue(Object oldValue) {
		String newValue = null;
		if (oldValue != null) {
			Shell shell = ((Button) buttonRename.getUIControl()).getShell();
			IInputValidator validator = new IInputValidator() {
				public String isValid(String newText) {
					boolean isValid = newText.trim().length() > 0;
					return isValid ? null : "Word cannot be empty!";
				}
			};
			InputDialog dialog = new InputDialog(shell, "Modify", "Enter a new word:", String.valueOf(oldValue),
					validator);
			int result = dialog.open();
			if (result == Window.OK) {
				newValue = dialog.getValue();
			}
		}
		return newValue;
	}

	private List<WordNode> createInput() {
		String[] words = { "Adventure", "Acclimatisation", "Aardwark", "Binoculars", "Beverage", "Boredom",
				"Ballistics", "Calculation", "Coexistence", "Cinnamon", "Celebration", "Disney", "Dictionary", "Delta",
				"Desperate", "Elf", "Electronics", "Elwood", "Enemy" };
		ArrayList<WordNode> result = new ArrayList<WordNode>(words.length);
		for (int i = 0; i < words.length; i++) {
			WordNode node = new WordNode(words[i]);
			result.add(node);
		}
		result.get(0).setUpperCase(true);
		result.get(1).setUpperCase(true);
		return result;
	}

	// helping classes
	// ////////////////

	/**
	 * Compares two strings.
	 */
	private static final class StringComparator implements Comparator<Object> {
		public int compare(Object o1, Object o2) {
			String s1 = (String) o1;
			String s2 = (String) o2;
			return s1.compareTo(s2);
		}
	}
}