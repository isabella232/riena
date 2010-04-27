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

	private Text text;
	private FilteredList filteredList;
	private LabelProvider labelProvider;

	public CompletionCombo(Composite parent, int style) {
		super(parent, style);
		GridLayoutFactory.fillDefaults().numColumns(1).spacing(0, 0).applyTo(this);

		text = new Text(this, SWT.BORDER | SWT.SINGLE);
		GridDataFactory.fillDefaults().applyTo(text);

		int flags = SWT.V_SCROLL | SWT.H_SCROLL | SWT.SINGLE;
		labelProvider = new LabelProvider(); // new WorkbenchLabelProvider();
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
		text.addKeyListener(listener);
		text.addModifyListener(listener);

	}

	private void completeText(String prefix) {
		if (getItemCount() == 1) {
			Object[] selection = filteredList.getSelection();
			if (selection.length == 1) {
				String proposal = labelProvider.getText(selection[0]);
				if (proposal != null && prefix.length() < proposal.length()) {
					// System.out.println("complete: " + prefix + " -> " + proposal);
					text.setText(proposal);
					text.setSelection(prefix.length(), proposal.length());
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
			}
		}

		public void modifyText(ModifyEvent e) {
			if (ignore) {
				return;
			}
			String filterText = text.getText();
			filteredList.setFilter(filterText);
			completeText(filterText);
		}
	}

}