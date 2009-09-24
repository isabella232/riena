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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Demonstrates converting an enum to String and back while using a ComboRidget.
 */
public class SnippetComboRidget002 {

	private enum Answer {
		YES, NO, UNKNOWN
	}

	public static void main(String[] args) {
		Display display = Display.getDefault();

		try {
			Shell shell = UIControlsFactory.createShell(display);
			shell.setLayout(new GridLayout(2, false));
			shell.setText(SnippetComboRidget002.class.getSimpleName());

			UIControlsFactory.createLabel(shell, "ComboRidget:"); //$NON-NLS-1$
			Combo combo = UIControlsFactory.createCombo(shell);

			UIControlsFactory.createLabel(shell, "Selection:"); //$NON-NLS-1$
			Label label = UIControlsFactory.createLabel(shell, ""); //$NON-NLS-1$
			GridDataFactory.fillDefaults().grab(true, false).applyTo(label);

			IComboRidget comboRidget = (IComboRidget) SwtRidgetFactory.createRidget(combo);
			WritableList values = new WritableList(Arrays.asList(Answer.YES, Answer.NO, Answer.UNKNOWN), Answer.class);
			WritableValue selection = new WritableValue(Answer.YES, Answer.class);
			comboRidget.setModelToUIControlConverter(new AnswerToStringConverter());
			comboRidget.setUIControlToModelConverter(new StringToAnswerConverter());
			comboRidget.bindToModel(values, Answer.class, null, selection);
			comboRidget.updateFromModel();

			DataBindingContext dbc = new DataBindingContext();
			dbc.bindValue(SWTObservables.observeText(label), selection);

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

	// helping classes
	//////////////////

	private static abstract class AbstractAnswerConverter extends Converter {

		private final Map<Answer, String> E2S;

		AbstractAnswerConverter(Object from, Object to) {
			super(from, to);
			E2S = new HashMap<Answer, String>();
			E2S.put(Answer.YES, "Yes"); //$NON-NLS-1$
			E2S.put(Answer.NO, "No"); //$NON-NLS-1$
			E2S.put(Answer.UNKNOWN, "Unknown"); //$NON-NLS-1$
			E2S.put(null, ""); //$NON-NLS-1$
		}

		public Object convert(Object fromObject) {
			Object result = null;
			if (getFromType() == Answer.class) {
				result = E2S.get(fromObject);
			} else {
				Iterator<Entry<Answer, String>> iter = E2S.entrySet().iterator();
				while (result == null && iter.hasNext()) {
					Entry<Answer, String> entry = iter.next();
					if (entry.getValue().equals(fromObject)) {
						result = entry.getKey();
					}
				}
			}
			Assert.isNotNull(result, "Conversion failed for: " + fromObject); //$NON-NLS-1$
			return result;
		}
	}

	private static class AnswerToStringConverter extends AbstractAnswerConverter {
		AnswerToStringConverter() {
			super(Answer.class, String.class);
		}
	}

	private static class StringToAnswerConverter extends AbstractAnswerConverter {
		StringToAnswerConverter() {
			super(String.class, Answer.class);
		}
	}

}
