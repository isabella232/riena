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
package org.eclipse.riena.example.client.controllers;

import org.eclipse.riena.example.client.views.TreeView;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleNodeViewController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ISelectableRidget;
import org.eclipse.riena.ui.ridgets.ITreeRidget;
import org.eclipse.riena.ui.ridgets.tree.DefaultObservableTreeModel;
import org.eclipse.riena.ui.ridgets.tree.DefaultObservableTreeNode;
import org.eclipse.riena.ui.ridgets.tree.IObservableTreeModel;

/**
 * Controller for the {@link TreeView} example.
 */
public class TreeViewController extends SubModuleNodeViewController {

	private ITreeRidget tree;
	private IActionRidget buttonAddSibling;
	private IActionRidget buttonAddChild;
	private IActionRidget buttonRename;
	private IActionRidget buttonDelete;

	public ITreeRidget getTree() {
		return tree;
	}

	public void setTree(ITreeRidget tree) {
		this.tree = tree;
	}

	public IActionRidget getButtonAddSibling() {
		return buttonAddSibling;
	}

	public void setButtonAddSibling(IActionRidget buttonAddSibling) {
		this.buttonAddSibling = buttonAddSibling;
	}

	public IActionRidget getButtonAddChild() {
		return buttonAddChild;
	}

	public void setButtonAddChild(IActionRidget buttonAddChild) {
		this.buttonAddChild = buttonAddChild;
	}

	public IActionRidget getButtonRename() {
		return buttonRename;
	}

	public void setButtonRename(IActionRidget buttonRename) {
		this.buttonRename = buttonRename;
	}

	public IActionRidget getButtonDelete() {
		return buttonDelete;
	}

	public void setButtonDelete(IActionRidget buttonDelete) {
		this.buttonDelete = buttonDelete;
	}

	public TreeViewController(ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	public void afterBind() {
		super.afterBind();
		initRidgets();
	}

	/**
	 * Binds and updates the ridgets.
	 */
	private void initRidgets() {
		tree.setSelectionType(ISelectableRidget.SelectionType.SINGLE);
		tree.bindToModel(createTreeModel());
		tree.updateFromModel();
		// TODO [ev] tree.setSelection(0);

		buttonAddSibling.setText("Add &Sibling");
		buttonAddSibling.addListener(new IActionListener() {
			public void callback() {
				System.out.println("addSibling");
			}
		});

		buttonAddChild.setText("Add &Child");
		buttonAddChild.addListener(new IActionListener() {
			public void callback() {
				System.out.println("addChild");
			}
		});

		buttonRename.setText("&Rename");
		buttonRename.addListener(new IActionListener() {
			public void callback() {
				System.out.println("rename");
			}
		});

		buttonDelete.setText("&Delete");
		buttonDelete.addListener(new IActionListener() {
			public void callback() {
				System.out.println("delete");
			}
		});
	}

	private IObservableTreeModel createTreeModel() {
		DefaultObservableTreeNode root = new DefaultObservableTreeNode("root");

		DefaultObservableTreeNode groupA = new DefaultObservableTreeNode("group a");
		groupA.addChild(new DefaultObservableTreeNode("a_child_1"));
		groupA.addChild(new DefaultObservableTreeNode("a_child_2"));
		groupA.addChild(new DefaultObservableTreeNode("a_child_3"));
		root.addChild(groupA);

		DefaultObservableTreeNode groupB = new DefaultObservableTreeNode("group b");
		groupB.addChild(new DefaultObservableTreeNode("b_child_1"));
		groupB.addChild(new DefaultObservableTreeNode("b_child_2"));
		groupB.addChild(new DefaultObservableTreeNode("b_child_3"));
		root.addChild(groupB);

		DefaultObservableTreeNode groupC = new DefaultObservableTreeNode("group c");
		groupC.addChild(new DefaultObservableTreeNode("c_child_1"));
		groupC.addChild(new DefaultObservableTreeNode("c_child_2"));
		groupC.addChild(new DefaultObservableTreeNode("c_child_3"));
		root.addChild(groupC);

		return new DefaultObservableTreeModel(root);
	}
}