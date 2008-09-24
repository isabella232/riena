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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.ridgets.util.beans.AbstractBean;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * A table ridget with sorting.
 */
public class SnippetTableRidget002 {

	public SnippetTableRidget002(Shell shell) {

		Table table = new Table(shell, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		TableColumn[] columns = new TableColumn[3];
		for (int i = 0; i < 3; i++) {
			columns[i] = new TableColumn(table, SWT.LEFT);
		}
		TableColumnLayout layout = new TableColumnLayout();
		layout.setColumnData(columns[0], new ColumnWeightData(125, 125));
		layout.setColumnData(columns[1], new ColumnWeightData(125, 125));
		layout.setColumnData(columns[2], new ColumnWeightData(100, 100));
		shell.setLayout(layout);

		ITableRidget tableRidget = (ITableRidget) SwtRidgetFactory.createRidget(table);
		String[] columnPropertyNames = { "pseudonym", "name", "appearance" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
		String[] columnHeaders = { "Pseudonym", "Name", "First appearance" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ 
		List<MyNode> input = createInput();
		tableRidget
				.bindToModel(new WritableList(input, MyNode.class), MyNode.class, columnPropertyNames, columnHeaders);
		tableRidget.setComparator(0, new StringComparator());
		tableRidget.setColumnSortable(1, false);
		tableRidget.setComparator(2, new IntegerComparator());
		tableRidget.updateFromModel();

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = Display.getDefault();
		try {
			final Shell shell = new Shell();
			new SnippetTableRidget002(shell);
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
		nodes.add(new MyNode("Superman", "Clark Kent", 1938)); //$NON-NLS-1$ //$NON-NLS-2$
		nodes.add(new MyNode("Batman", "Bruce Wayne", 1939)); //$NON-NLS-1$ //$NON-NLS-2$
		nodes.add(new MyNode("Zorro", "Don Diego de la Vega", 1919)); //$NON-NLS-1$ //$NON-NLS-2$
		nodes.add(new MyNode("Flash Gordon", "Gordon Ferrao", 1934)); //$NON-NLS-1$ //$NON-NLS-2$
		nodes.add(new MyNode("Hulk", "Bruce Banner", 1962)); //$NON-NLS-1$ //$NON-NLS-2$
		nodes.add(new MyNode("Spider-Man", "Peter Parker", 1962)); //$NON-NLS-1$ //$NON-NLS-2$
		nodes.add(new MyNode("Silver Surfer", "Norrin Radd", 1966)); //$NON-NLS-1$ //$NON-NLS-2$

		return nodes;

	}

	private static class MyNode extends AbstractBean {

		private String pseudonym;
		private String name;
		private Integer appearance;

		public MyNode(String pseudonym, String name, int appearance) {
			this.pseudonym = pseudonym;
			this.name = name;
			this.appearance = appearance;
		}

		public String getPseudonym() {
			return pseudonym;
		}

		public String getName() {
			return name;
		}

		public Integer getAppearance() {
			return appearance;
		}

	}

	private static final class StringComparator implements Comparator<Object> {
		public int compare(Object o1, Object o2) {
			String s1 = (String) o1;
			String s2 = (String) o2;
			return s1.compareTo(s2);
		}
	}

	private static final class IntegerComparator implements Comparator<Object> {
		public int compare(Object o1, Object o2) {
			Integer i1 = (Integer) o1;
			Integer i2 = (Integer) o2;
			return i1.compareTo(i2);
		}
	}

}
