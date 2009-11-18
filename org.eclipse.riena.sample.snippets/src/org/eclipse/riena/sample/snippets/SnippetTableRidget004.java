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
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import org.eclipse.riena.beans.common.AbstractBean;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;

/**
 * Demonstrates automatic table column creation and setting the column widths.
 */
public class SnippetTableRidget004 {

	public SnippetTableRidget004(Shell shell) {
		Table table = new Table(shell, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		// TableColumns will be created as needed when 'bind' is invoked.
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		ITableRidget tableRidget = (ITableRidget) SwtRidgetFactory.createRidget(table);

		// set the widths
		ColumnLayoutData[] widths = { new ColumnPixelData(100, false), new ColumnWeightData(2, false),
				new ColumnWeightData(2, false), new ColumnWeightData(1, false), new ColumnWeightData(1, false), };
		tableRidget.setColumnWidths(widths);

		String[] columnPropertyNames = { "english", "german", "french", "spain", "italian" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		String[] columnHeaders = { "English", "German", "French", "Spain", "Italian" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		tableRidget.bindToModel(createInput(), MyNode.class, columnPropertyNames, columnHeaders);
		tableRidget.updateFromModel();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = Display.getDefault();
		try {
			final Shell shell = new Shell();
			new SnippetTableRidget004(shell);
			shell.setSize(500, 300);
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

	private WritableList createInput() {

		List<MyNode> nodes = new ArrayList<MyNode>(7);
		nodes.add(new MyNode("Monday", "Montag", "lundi", "lunes", "lunedì")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		nodes.add(new MyNode("Tuesday", "Dienstag", "mardi", "martes", "martedì")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		nodes.add(new MyNode("Wednesday", "Mittwoch", "mercredi", "miércoles", "mercoledì")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		nodes.add(new MyNode("Thursday", "Donnerstag", "jeudi", "jueves", "giovedì")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		nodes.add(new MyNode("Friday", "Freitag", "vendredi", "viernes", "venerdì")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		nodes.add(new MyNode("Saturday", "Samstag", "samedi", "sábado", "sabato")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
		nodes.add(new MyNode("Sunday", "Sonntag", "dimanche", "domingo", "domenica")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

		return new WritableList(nodes, MyNode.class);
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
