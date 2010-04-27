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
package org.eclipse.riena.example.client.views;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.FilteredList;

import org.eclipse.riena.core.util.ReflectionUtils;

/**
 * TODO [ev] move to appropriate plugin / package
 */
class CompletionCombo extends Composite {

	private Text filterText;
	private FilteredList filteredList;
	private LabelProvider labelProvider;

	public CompletionCombo(Composite parent, int style) {
		super(parent, style);
		GridLayoutFactory.fillDefaults().numColumns(1).spacing(0, 0).applyTo(this);

		filterText = new Text(this, SWT.BORDER | SWT.SINGLE);
		GridDataFactory.fillDefaults().applyTo(filterText);

		int flags = SWT.V_SCROLL | SWT.H_SCROLL | SWT.SINGLE;
		labelProvider = new LabelProvider(); // WorkbenchLabelProvider(); //  new LabelProvider(); 
		filteredList = new FilteredList(this, flags, labelProvider, true, true, true);
		filteredList.setFilter(""); //$NON-NLS-1$
		GridDataFactory.fillDefaults().grab(true, true).applyTo(filteredList);

		addListeners();
	}

	public void setElements(Object[] elements) {
		filteredList.setElements(elements);
	}

	// helping methods
	//////////////////

	private void addListeners() {
		CompletionListener listener = new CompletionListener();
		filterText.addKeyListener(listener);
		filterText.addModifyListener(listener);

	}

	private void completeText(String prefix) {
		if (getItemCount() == 1) {
			Object[] selection = filteredList.getSelection();
			if (selection.length == 1) {
				String proposal = labelProvider.getText(selection[0]);
				if (proposal != null && prefix.length() < proposal.length()) {
					// System.out.println("complete: " + prefix + " -> " + proposal);
					filterText.setText(proposal);
					filterText.setSelection(prefix.length(), proposal.length());
				}
			}
		}
	}

	private int getItemCount() {
		return ReflectionUtils.getHidden(filteredList, "fFilteredCount"); //$NON-NLS-1$
	}

	// helping classes
	//////////////////

	private final class CompletionListener extends KeyAdapter implements ModifyListener {

		private boolean ignore;

		public void keyPressed(KeyEvent e) {
			ignore = !Character.isLetterOrDigit(e.character);
			if (e.keyCode == SWT.ARROW_DOWN) {
				filteredList.setFocus();
			} else if (e.keyCode == 13) {
				int end = filterText.getText().length();
				filterText.setSelection(end, end);
			}
		}

		public void modifyText(ModifyEvent e) {
			String text = filterText.getText();
			filteredList.setFilter(text);
			if (!ignore) {
				completeText(text);
			}
		}
	}

}