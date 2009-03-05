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

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.riena.beans.common.WordNode;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IGroupedTreeTableRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

/**
 * Demonstrates a tree table ridget with grouping (toggling on and off).
 */
public class SnippetTreeTableRidget004 {

	public SnippetTreeTableRidget004(final Shell shell) {
		Tree tree = new Tree(shell, SWT.FULL_SELECTION | SWT.MULTI);
		for (int i = 0; i < 2; i++) {
			TreeColumn tc = new TreeColumn(tree, SWT.DEFAULT);
			tc.setWidth(200);
		}
		GridDataFactory.fillDefaults().grab(true, true).applyTo(tree);

		Button button = new Button(shell, SWT.PUSH);
		button.setText("Toggle &Grouping"); //$NON-NLS-1$
		GridDataFactory.swtDefaults().align(SWT.END, SWT.BEGINNING).applyTo(button);

		final IGroupedTreeTableRidget treeTableRidget = (IGroupedTreeTableRidget) SwtRidgetFactory.createRidget(tree);
		WordNode[] roots = createTreeInput();
		String[] columnValues = new String[] { "word", "upperCase" }; //$NON-NLS-1$//$NON-NLS-2$
		String[] columnHeaders = new String[] { "Word", "Uppercase" }; //$NON-NLS-1$//$NON-NLS-2$
		treeTableRidget.bindToModel(roots, WordNode.class, "children", "parent", //$NON-NLS-1$//$NON-NLS-2$
				columnValues, columnHeaders);
		treeTableRidget.expandAll();

		IActionRidget actionRidget = (IActionRidget) SwtRidgetFactory.createRidget(button);
		actionRidget.addListener(new IActionListener() {
			public void callback() {
				boolean isGrouping = !treeTableRidget.isGroupingEnabled();
				treeTableRidget.setGroupingEnabled(isGrouping);
				System.out.println("Grouping enabled? " + isGrouping); //$NON-NLS-1$
			}
		});
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = Display.getDefault();
		try {
			Shell shell = UIControlsFactory.createShell(display);
			GridLayoutFactory.swtDefaults().applyTo(shell);
			new SnippetTreeTableRidget004(shell);
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
}
