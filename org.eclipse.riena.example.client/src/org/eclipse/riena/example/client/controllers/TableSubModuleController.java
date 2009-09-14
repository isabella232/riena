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
package org.eclipse.riena.example.client.controllers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.observable.list.WritableList;

import org.eclipse.riena.beans.common.WordNode;
import org.eclipse.riena.example.client.views.TableSubModuleView;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ISelectableRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;

/**
 * Controller for the {@link TableSubModuleView} example.
 */
public class TableSubModuleController extends SubModuleController {

	private ITableRidget table;
	private List<WordNode> input;
	private Selection selection;

	public TableSubModuleController() {
		this(null);
	}

	public TableSubModuleController(ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.controllers.SubModuleController#afterBind()
	 */
	@Override
	public void afterBind() {
		super.afterBind();
		bindModel();
	}

	private void bindModel() {
		input = createInput();
		String[] columnPropertyNames = { "word", "upperCase", "ACount" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		String[] columnHeaders = { "Word", "Uppercase", "A Count" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		table.bindToModel(new WritableList(input, WordNode.class), WordNode.class, columnPropertyNames, columnHeaders);
		selection = new Selection();
		table.bindMultiSelectionToModel(selection, "selections");
		table.updateFromModel();
		table.setSelectionType(ISelectableRidget.SelectionType.MULTI);
		table.setSelection(0);
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#configureRidgets()
	 */
	@Override
	public void configureRidgets() {

		table = (ITableRidget) getRidget("table"); //$NON-NLS-1$
		final IToggleButtonRidget buttonPrintSelection = (IToggleButtonRidget) getRidget("buttonPrintSelection"); //$NON-NLS-1$
		IActionRidget buttonAddSibling = (IActionRidget) getRidget("buttonAddSibling"); //$NON-NLS-1$

		buttonPrintSelection.setText("&Echo Selection"); //$NON-NLS-1$
		buttonPrintSelection.setSelected(true);

		buttonAddSibling.setText("&Add"); //$NON-NLS-1$
		buttonAddSibling.addListener(new IActionListener() {
			public void callback() {
				System.out.println("selection: " + selection.getSelections());
			}
		});

	}

	private List<WordNode> createInput() {
		String[] words = { "Adventure", "Acclimatisation", "Aardwark", "Binoculars", "Beverage", "Boredom", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
				"Ballistics", "Calculation", "Coexistence", "Cinnamon", "Celebration", "Disney", "Dictionary", "Delta", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
				"Desperate", "Elf", "Electronics", "Elwood", "Enemy" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		ArrayList<WordNode> result = new ArrayList<WordNode>(words.length);
		for (int i = 0; i < words.length; i++) {
			WordNode node = new WordNode(words[i]);
			result.add(node);
		}
		result.get(0).setUpperCase(true);
		result.get(1).setUpperCase(true);
		return result;
	}

	private class Selection {

		private List<Object> selections;

		public Selection() {
			selections = new ArrayList<Object>();
		}

		/**
		 * @param selections
		 *            the selections to set
		 */
		public void setSelections(List<Object> selections) {
			this.selections = selections;
		}

		/**
		 * @return the selections
		 */
		public List<Object> getSelections() {
			return selections;
		}

	}

}
