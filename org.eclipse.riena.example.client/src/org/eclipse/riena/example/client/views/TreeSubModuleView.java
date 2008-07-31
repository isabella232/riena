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
import org.eclipse.riena.example.client.controllers.TreeSubModuleController;
import org.eclipse.riena.internal.example.client.utils.UIControlsFactory;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleNodeView;
import org.eclipse.riena.ui.ridgets.ITreeRidget;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Tree;

/**
 * SWT {@link ITreeRidget} sample.
 */
public class TreeSubModuleView extends SubModuleNodeView<TreeSubModuleController> {

	public static final String ID = TreeSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(ILnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(1, false));

		Group group1 = createTreeGroup(parent);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(group1);
	}

	@Override
	protected TreeSubModuleController createController(ISubModuleNode subModuleNode) {
		return new TreeSubModuleController(subModuleNode);
	}

	// helping methods
	// ////////////////

	private Group createTreeGroup(Composite parent) {
		Group group = UIControlsFactory.createGroup(parent, "&Tree:");
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(1).applyTo(group);

		Tree tree = new Tree(group, SWT.FULL_SELECTION | SWT.MULTI);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(tree);
		addUIControl(tree, "tree"); //$NON-NLS-1$

		Composite buttonComposite = createButtonComposite(group);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(buttonComposite);

		return group;
	}

	private Composite createButtonComposite(Group group) {
		Composite buttonComposite = UIControlsFactory.createComposite(group);
		GridLayoutFactory.fillDefaults().numColumns(6).equalWidth(false).applyTo(buttonComposite);

		Button buttonAddSibling = UIControlsFactory.createButton(buttonComposite);
		int widthHint = UIControlsFactory.getWidthHint(buttonAddSibling);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.END, SWT.BEGINNING).hint(widthHint, SWT.DEFAULT)
				.applyTo(buttonAddSibling);
		addUIControl(buttonAddSibling, "buttonAddSibling"); //$NON-NLS-1$

		Button buttonAddChild = UIControlsFactory.createButton(buttonComposite);
		addUIControl(buttonAddChild, "buttonAddChild"); //$NON-NLS-1$

		Button buttonRename = UIControlsFactory.createButton(buttonComposite);
		addUIControl(buttonRename, "buttonRename"); //$NON-NLS-1$

		Button buttonDelete = UIControlsFactory.createButton(buttonComposite);
		addUIControl(buttonDelete, "buttonDelete"); //$NON-NLS-1$

		Button buttonExpand = UIControlsFactory.createButton(buttonComposite);
		addUIControl(buttonExpand, "buttonExpand"); //$NON-NLS-1$

		Button buttonCollapse = UIControlsFactory.createButton(buttonComposite);
		addUIControl(buttonCollapse, "buttonCollapse"); //$NON-NLS-1$

		return buttonComposite;
	}

}
