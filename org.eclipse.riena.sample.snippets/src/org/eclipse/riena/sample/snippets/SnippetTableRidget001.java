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

import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import org.eclipse.riena.beans.common.AbstractBean;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;

/**
 * Demonstrates binding a table ridget to a model.
 */
public class SnippetTableRidget001 {

	public SnippetTableRidget001(Shell shell) {

		Table table = new Table(shell, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		TableColumn[] columns = new TableColumn[5];
		for (int i = 0; i < 5; i++) {
			columns[i] = new TableColumn(table, SWT.LEFT);
		}
		TableColumnLayout layout = new TableColumnLayout();
		for (int i = 0; i < 5; i++) {
			layout.setColumnData(columns[i], new ColumnWeightData(120, 120));
		}
		shell.setLayout(layout);

		ITableRidget tableRidget = (ITableRidget) SwtRidgetFactory.createRidget(table);
		String[] columnPropertyNames = { "english", "german", "french", "spain", "italian" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		String[] columnHeaders = { "English", "German", "French", "Spain", "Italian" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		List<MyNode> input = createInput();
		tableRidget
				.bindToModel(new WritableList(input, MyNode.class), MyNode.class, columnPropertyNames, columnHeaders);
		tableRidget.updateFromModel();

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = Display.getDefault();
		try {
			final Shell shell = new Shell();
			new SnippetTableRidget001(shell);
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

		@SuppressWarnings("unused")
		public String getEnglish() {
			return english;
		}

		@SuppressWarnings("unused")
		public String getGerman() {
			return german;
		}

		@SuppressWarnings("unused")
		public String getFrench() {
			return french;
		}

		@SuppressWarnings("unused")
		public String getSpain() {
			return spain;
		}

		@SuppressWarnings("unused")
		public String getItalian() {
			return italian;
		}

	}

}
