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
package org.eclipse.riena.sample.snippets;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.beans.common.ListBean;
import org.eclipse.riena.beans.common.StringBean;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * TODO [ev] javadoc
 */
public class SnippetComboRidget003 {

	public static void main(String[] args) {
		Display display = Display.getDefault();

		try {
			Shell shell = UIControlsFactory.createShell(display);
			shell.setText(SnippetComboRidget003.class.getSimpleName());
			GridLayoutFactory.swtDefaults().numColumns(2).spacing(5, 5).applyTo(shell);

			UIControlsFactory.createLabel(shell, "Combo:"); //$NON-NLS-1$
			Combo combo = UIControlsFactory.createCombo(shell);

			UIControlsFactory.createLabel(shell, "Selection:"); //$NON-NLS-1$
			final Label lblSelection = UIControlsFactory.createLabel(shell, ""); //$NON-NLS-1$
			GridDataFactory.fillDefaults().grab(true, false).applyTo(lblSelection);

			Button button = UIControlsFactory.createButton(shell, "&Remove Selection"); //$NON-NLS-1$
			GridDataFactory.fillDefaults().span(2, 1).applyTo(button);

			// data model

			final ListBean input = createInput();
			final StringBean selection = new StringBean("Monday"); //$NON-NLS-1$

			// ridgets

			final IComboRidget comboRidget = (IComboRidget) SwtRidgetFactory.createRidget(combo);
			comboRidget.bindToModel(input, ListBean.PROPERTY_VALUES, String.class, null, selection,
					StringBean.PROP_VALUE);
			comboRidget.updateFromModel();
			comboRidget.setMandatory(true);

			final ILabelRidget labelRidget = (ILabelRidget) SwtRidgetFactory.createRidget(lblSelection);
			labelRidget.setModelToUIControlConverter(new IConverter() {
				public Object getToType() {
					return String.class;
				}

				public Object getFromType() {
					return String.class;
				}

				public Object convert(Object fromObject) {
					return String.valueOf(fromObject);
				}
			});
			labelRidget.bindToModel(selection, StringBean.PROP_VALUE);
			labelRidget.updateFromModel();
			selection.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					labelRidget.updateFromModel();
					comboRidget.setErrorMarked(selection.getValue() == null);
				}
			});

			IActionRidget actionRidget = (IActionRidget) SwtRidgetFactory.createRidget(button);
			actionRidget.addListener(new IActionListener() {
				public void callback() {
					input.getValues().remove(comboRidget.getSelection());
					comboRidget.updateFromModel();
				}
			});

			shell.pack();
			shell.open();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} finally {
			display.dispose();
		}
	}

	private static ListBean createInput() {
		List<String> days = new ArrayList<String>();
		for (String day : new String[] { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
				"Sunday" }) { //$NON-NLS-1$
			days.add(day);
		}
		return new ListBean(days);
	}

}
