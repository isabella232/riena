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
package org.eclipse.riena.example.client.views;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.riena.example.client.controllers.TreeTableViewController;
import org.eclipse.riena.internal.example.client.utils.UIControlsFactory;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleNodeView;
import org.eclipse.riena.ui.ridgets.ITreeRidget;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

/**
 * SWT {@link ITreeRidget} sample.
 */
public class TreeTableView extends SubModuleNodeView<TreeTableViewController> {

	public static final String ID = TreeTableView.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
		parent.setLayout(new GridLayout(1, false));

		Group group1 = createTreeTableGroup(parent);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(group1);
	}

	@Override
	protected TreeTableViewController createController(ISubModuleNode subModuleNode) {
		return new TreeTableViewController(subModuleNode);
	}

	// helping methods
	// ////////////////

	private Group createTreeTableGroup(Composite parent) {
		Group group = UIControlsFactory.createGroup(parent, "&Tree Table:");
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(1).applyTo(group);

		Composite treeComposite = new Composite(group, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(treeComposite);

		Tree tree = new Tree(treeComposite, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		addUIControl(tree, "tree"); //$NON-NLS-1$

		TreeColumn columnWord = new TreeColumn(tree, SWT.LEFT);
		TreeColumn columnUppercase = new TreeColumn(tree, SWT.LEFT);
		TreeColumn columnACount = new TreeColumn(tree, SWT.LEFT);

		TreeColumnLayout layout = new TreeColumnLayout();
		layout.setColumnData(columnWord, new ColumnWeightData(30));
		layout.setColumnData(columnUppercase, new ColumnWeightData(30));
		layout.setColumnData(columnACount, new ColumnWeightData(30));
		treeComposite.setLayout(layout);

		return group;
	}
}
