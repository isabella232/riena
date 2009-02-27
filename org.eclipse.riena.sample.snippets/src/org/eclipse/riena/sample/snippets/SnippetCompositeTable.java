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

import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.beans.common.PersonFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.nebula.widgets.compositetable.AbstractNativeHeader;
import org.eclipse.swt.nebula.widgets.compositetable.CompositeTable;
import org.eclipse.swt.nebula.widgets.compositetable.GridRowLayout;
import org.eclipse.swt.nebula.widgets.compositetable.IRowContentProvider;
import org.eclipse.swt.nebula.widgets.compositetable.RowConstructionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * TODO [ev] remove later
 */
public class SnippetCompositeTable {

	private static class Header extends AbstractNativeHeader {
		public Header(Composite parent, int style) {
			super(parent, style);
			setWeights(new int[] { 100, 100, 100 });
			setColumnText(new String[] { "Column 1", "Column 2", "Column 3" });
		}
	}

	public static class Row extends Composite {
		private Text txtFirst;
		private Text txtLast;

		public Row(Composite parent, int style) {
			super(parent, style);
			setLayout(new GridRowLayout(new int[] { 100, 100, 100 }, true));
			new Text(this, SWT.BORDER);
			createColumnTwo();
			createColumnThree();
		}

		private final void createColumnTwo() {
			Composite cell = createCell();
			txtFirst = new Text(cell, SWT.BORDER);
			txtLast = new Text(cell, SWT.BORDER);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(txtFirst);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(txtLast);
			new Link(cell, SWT.NONE).setText("<a>This is a Link</a>");
		}

		private final void createColumnThree() {
			Composite cell = createCell();
			new Button(cell, SWT.RADIO).setText("Choice 1");
			new Button(cell, SWT.RADIO).setText("Choice 2");
			new Button(cell, SWT.RADIO).setText("Choice 3");
		}

		private Composite createCell() {
			Composite cell = new Composite(this, SWT.NONE);
			GridLayoutFactory.fillDefaults().numColumns(1).applyTo(cell);
			cell.setBackground(cell.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
			return cell;
		}
	}

	private static final List<Person> persons = PersonFactory.createPersonList();

	public static void main(String[] args) {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		CompositeTable table = new CompositeTable(shell, SWT.NONE);
		new Header(table, SWT.NONE);
		new Row(table, SWT.NONE);

		table.setRunTime(true);
		table.setNumRowsInCollection(persons.size());

		table.addRowConstructionListener(new RowConstructionListener() {
			@Override
			public void headerConstructed(Control newHeader) {
				// unused
			}

			@Override
			public void rowConstructed(Control newRow) {
				System.out.println("row cons: " + newRow + ":" + newRow.hashCode());
			}
		});

		table.addRowContentProvider(new IRowContentProvider() {
			public void refresh(CompositeTable sender, int currentObjectOffset, Control rowControl) {
				Row row = (Row) rowControl;
				Person person = persons.get(currentObjectOffset);
				row.txtFirst.setText(person.getFirstname());
				row.txtLast.setText(person.getLastname());
			}
		});

		shell.setSize(400, 400);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
