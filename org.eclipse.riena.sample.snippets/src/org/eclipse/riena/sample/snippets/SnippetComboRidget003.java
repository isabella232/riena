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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.beans.common.ListBean;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IComboRidget;
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
			shell.setLayout(new GridLayout(2, false));
			shell.setText(SnippetComboRidget003.class.getSimpleName());

			UIControlsFactory.createLabel(shell, "Combo:"); //$NON-NLS-1$
			CCombo combo = UIControlsFactory.createCCombo(shell);

			UIControlsFactory.createLabel(shell, "Selection:"); //$NON-NLS-1$
			Label label = UIControlsFactory.createLabel(shell, "Monday"); //$NON-NLS-1$
			GridDataFactory.fillDefaults().grab(true, false).applyTo(label);

			Button button = UIControlsFactory.createButton(shell, "&Remove Selection"); //$NON-NLS-1$
			GridDataFactory.fillDefaults().span(2, 1).applyTo(button);

			final IComboRidget comboRidget = (IComboRidget) SwtRidgetFactory.createRidget(combo);
			final ListBean input = createInput();
			comboRidget.bindToModel(input, ListBean.PROPERTY_VALUES, String.class, null, label, "text"); //$NON-NLS-1$
			comboRidget.updateFromModel();
			comboRidget.setMandatory(true);

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
		for (String day : new String[] { "<Empty>", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
				"Sunday" }) { //$NON-NLS-1$
			days.add(day);
		}
		return new ListBean(days);
	}

}
