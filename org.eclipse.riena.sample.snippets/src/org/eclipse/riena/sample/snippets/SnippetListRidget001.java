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
package org.eclipse.riena.sample.snippets;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.riena.beans.common.AbstractBean;
import org.eclipse.riena.beans.common.TypedBean;
import org.eclipse.riena.ui.ridgets.ISelectableRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Demonstrates listening to selection changes on a list ridget.
 */
public class SnippetListRidget001 {

	public SnippetListRidget001(Shell shell) {
		shell.setLayout(new FillLayout());
		org.eclipse.swt.widgets.List list = UIControlsFactory.createList(shell, false, true);

		ITableRidget listRidget = (ITableRidget) SwtRidgetFactory.createRidget(list);
		String[] columnPropertyNames = { "english" }; //$NON-NLS-1$
		List<MyNode> input = createInput();
		listRidget.setSelectionType(ISelectableRidget.SelectionType.SINGLE);
		listRidget.bindToModel(new WritableList(input, MyNode.class), MyNode.class, columnPropertyNames, null);
		listRidget.updateFromModel();

		final TypedBean<MyNode> selection = new TypedBean<MyNode>(null);
		selection.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				MyNode node = selection.getValue();
				System.out.println("Selection: " + node.getEnglish()); //$NON-NLS-1$
			}
		});
		listRidget.bindSingleSelectionToModel(selection, "value"); //$NON-NLS-1$
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = Display.getDefault();
		try {
			final Shell shell = new Shell();
			new SnippetListRidget001(shell);
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

	private List<MyNode> createInput() {

		List<MyNode> nodes = new ArrayList<MyNode>(7);
		nodes.add(new MyNode("Monday", "Montag", "lundi", "lunes", "lunedì")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		nodes.add(new MyNode("Tuesday", "Dienstag", "mardi", "martes", "martedì")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		nodes.add(new MyNode("Wednesday", "Mittwoch", "mercredi", "miércoles", "mercoledì")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		nodes.add(new MyNode("Thursday", "Donnerstag", "jeudi", "jueves", "giovedì")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		nodes.add(new MyNode("Friday", "Freitag", "vendredi", "viernes", "venerdì")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		nodes.add(new MyNode("Saturday", "Samstag", "samedi", "sábado", "sabato")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		nodes.add(new MyNode("Sunday", "Sonntag", "dimanche", "domingo", "domenica")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

		return nodes;

	}

	private static class MyNode extends AbstractBean {

		private String english;
		private String german;
		private String french;
		private String spain;
		private String italian;

		public MyNode(String english, String german, String french, String spain, String italian) {
			this.english = english;
			this.german = german;
			this.french = french;
			this.spain = spain;
			this.italian = italian;
		}

		public String getEnglish() {
			return english;
		}

		public String getGerman() {
			return german;
		}

		public String getFrench() {
			return french;
		}

		public String getSpain() {
			return spain;
		}

		public String getItalian() {
			return italian;
		}

	}

}
