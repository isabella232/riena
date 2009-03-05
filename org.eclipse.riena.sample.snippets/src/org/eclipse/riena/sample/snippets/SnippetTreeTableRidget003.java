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

import org.eclipse.riena.beans.common.WordNode;
import org.eclipse.riena.ui.ridgets.IGroupedTreeTableRidget;
import org.eclipse.riena.ui.ridgets.swt.ColumnFormatter;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

/**
 * Demonstrates a tree table ridget with column formatters.
 */
public class SnippetTreeTableRidget003 {

	public SnippetTreeTableRidget003(final Shell shell) {
		Tree tree = new Tree(shell, SWT.FULL_SELECTION | SWT.MULTI);
		for (int i = 0; i < 2; i++) {
			TreeColumn tc = new TreeColumn(tree, SWT.DEFAULT);
			tc.setWidth(200);
		}

		IGroupedTreeTableRidget treeTableRidget = (IGroupedTreeTableRidget) SwtRidgetFactory.createRidget(tree);
		WordNode[] roots = createTreeInput();
		String[] columnValues = new String[] { "word", "upperCase" }; //$NON-NLS-1$//$NON-NLS-2$
		String[] columnHeaders = new String[] { "Word", "Uppercase" }; //$NON-NLS-1$//$NON-NLS-2$
		treeTableRidget.setGroupingEnabled(true);
		treeTableRidget.setColumnFormatter(0, new ColumnFormatter() {
			@Override
			public String getText(Object element) {
				String word = ((WordNode) element).getWord();
				if ("b".equalsIgnoreCase(word.substring(0, 1))) { //$NON-NLS-1$
					return reverse(word);
				}
				return null; // use default text
			}
		});
		treeTableRidget.setColumnFormatter(1, new ColumnFormatter() {
			@Override
			public Image getImage(Object element) {
				if (((WordNode) element).isUpperCase()) {
					return shell.getDisplay().getSystemImage(SWT.ICON_WARNING);
				}
				return null; // use default image
			}
		});
		treeTableRidget.bindToModel(roots, WordNode.class, "children", "parent", //$NON-NLS-1$//$NON-NLS-2$
				columnValues, columnHeaders);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = Display.getDefault();
		try {
			final Shell shell = new Shell();
			shell.setLayout(new FillLayout());
			new SnippetTreeTableRidget003(shell);
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

	private WordNode[] createTreeInput() {
		WordNode root = new WordNode("Words"); //$NON-NLS-1$

		WordNode bTowns = new WordNode(root, "B"); //$NON-NLS-1$
		new WordNode(bTowns, "Boring"); //$NON-NLS-1$
		new WordNode(bTowns, "Buchanan"); //$NON-NLS-1$
		new WordNode(bTowns, "Beaverton").setUpperCase(true); //$NON-NLS-1$
		new WordNode(bTowns, "Bend"); //$NON-NLS-1$
		new WordNode(bTowns, "Black Butte Ranch"); //$NON-NLS-1$
		new WordNode(bTowns, "Baker City"); //$NON-NLS-1$
		new WordNode(bTowns, "Bay City"); //$NON-NLS-1$
		new WordNode(bTowns, "Bridgeport"); //$NON-NLS-1$

		WordNode cTowns = new WordNode(root, "C"); //$NON-NLS-1$
		new WordNode(cTowns, "Cedar Mill"); //$NON-NLS-1$
		new WordNode(cTowns, "Crater Lake"); //$NON-NLS-1$
		new WordNode(cTowns, "Coos Bay"); //$NON-NLS-1$
		new WordNode(cTowns, "Corvallis"); //$NON-NLS-1$
		new WordNode(cTowns, "Cannon Beach"); //$NON-NLS-1$

		WordNode dTowns = new WordNode(root, "D"); //$NON-NLS-1$
		new WordNode(dTowns, "Dunes City"); //$NON-NLS-1$
		new WordNode(dTowns, "Damascus"); //$NON-NLS-1$
		new WordNode(dTowns, "Diamond Lake"); //$NON-NLS-1$
		new WordNode(dTowns, "Dallas"); //$NON-NLS-1$
		new WordNode(dTowns, "Depoe Bay"); //$NON-NLS-1$

		return new WordNode[] { root };
	}

	private String reverse(String s) {
		char[] chars = s.toCharArray();
		int length = chars.length;
		for (int i = 0, j = length / 2; i < j; i++) {
			char swap = chars[i];
			int endIndex = length - i - 1;
			chars[i] = chars[endIndex];
			chars[endIndex] = swap;
		}
		return String.valueOf(chars);
	}
}
