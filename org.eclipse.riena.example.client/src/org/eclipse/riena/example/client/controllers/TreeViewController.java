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

import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.riena.example.client.views.TreeView;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleNodeViewController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.ISelectableRidget;
import org.eclipse.riena.ui.ridgets.ITreeRidget;
import org.eclipse.riena.ui.ridgets.tree2.ITreeNode;
import org.eclipse.riena.ui.ridgets.tree2.TreeNode;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;

/**
 * Controller for the {@link TreeView} example.
 */
public class TreeViewController extends SubModuleNodeViewController {

	private ITreeRidget tree;
	private IActionRidget buttonAddSibling;
	private IActionRidget buttonAddChild;
	private IActionRidget buttonRename;
	private IActionRidget buttonDelete;
	private IActionRidget buttonExpand;
	private IActionRidget buttonCollapse;

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

	public IActionRidget getButtonExpand() {
		return buttonExpand;
	}

	public void setButtonExpand(IActionRidget buttonExpand) {
		this.buttonExpand = buttonExpand;
	}

	public IActionRidget getButtonCollapse() {
		return buttonCollapse;
	}

	public void setButtonCollapse(IActionRidget buttonCollapse) {
		this.buttonCollapse = buttonCollapse;
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
		tree.bindToModel(createTreeInput(), ITreeNode.class, ITreeNode.PROP_CHILDREN, ITreeNode.PROP_VALUE);
		tree.updateFromModel();

		buttonAddSibling.setText("Add &Sibling");
		buttonAddSibling.addListener(new IActionListener() {
			public void callback() {
				ITreeNode node = (ITreeNode) tree.getSingleSelectionObservable().getValue();
				ITreeNode parent = (node != null) ? node.getParent() : null;
				if (parent != null) {
					new TreeNode(parent, "NEW_SIBLING");
				}
			}
		});

		buttonAddChild.setText("Add &Child");
		buttonAddChild.addListener(new IActionListener() {
			public void callback() {
				ITreeNode node = (ITreeNode) tree.getSingleSelectionObservable().getValue();
				if (node != null) {
					new TreeNode(node, "NEW_CHILD");
				}
			}
		});

		buttonRename.setText("&Rename");
		buttonRename.addListener(new IActionListener() {
			public void callback() {
				ITreeNode node = (ITreeNode) tree.getSingleSelectionObservable().getValue();
				if (node != null) {
					String newValue = getNewValue(node.getValue());
					if (newValue != null) {
						node.setValue(newValue);
					}
				}
			}
		});

		buttonDelete.setText("&Delete");
		buttonDelete.addListener(new IActionListener() {
			public void callback() {
				ITreeNode node = (ITreeNode) tree.getSingleSelectionObservable().getValue();
				ITreeNode parent = (node != null) ? node.getParent() : null;
				if (parent != null) {
					List<ITreeNode> children = parent.getChildren();
					children.remove(node);
					parent.setChildren(children);
				}
			}
		});

		buttonExpand.setText("E&xpand");
		buttonExpand.addListener(new IActionListener() {
			public void callback() {
				ITreeNode node = (ITreeNode) tree.getSingleSelectionObservable().getValue();
				if (node != null) {
					tree.expand(node);
				}
			}
		});

		buttonCollapse.setText("&Collapse");
		buttonCollapse.addListener(new IActionListener() {
			public void callback() {
				ITreeNode node = (ITreeNode) tree.getSingleSelectionObservable().getValue();
				if (node != null) {
					tree.collapse(node);
				}
			}
		});

		final IObservableValue viewerSelection = tree.getSingleSelectionObservable();
		IObservableValue hasSelection = new ComputedValue(Boolean.TYPE) {
			protected Object calculate() {
				return Boolean.valueOf(viewerSelection.getValue() != null);
			}
		};
		IObservableValue hasNonRootSelection = new ComputedValue(Boolean.TYPE) {
			protected Object calculate() {
				boolean result = false;
				Object node = viewerSelection.getValue();
				if (node instanceof ITreeNode) {
					result = ((ITreeNode) node).getParent() != null;
				}
				return Boolean.valueOf(result);
			}
		};
		DataBindingContext dbc = new DataBindingContext();
		bindEnablementToValue(dbc, buttonAddChild, hasSelection);
		bindEnablementToValue(dbc, buttonAddSibling, hasNonRootSelection);
		bindEnablementToValue(dbc, buttonDelete, hasNonRootSelection);
		bindEnablementToValue(dbc, buttonRename, hasSelection);
		bindEnablementToValue(dbc, buttonExpand, hasSelection);
		bindEnablementToValue(dbc, buttonCollapse, hasSelection);
	}

	private void bindEnablementToValue(DataBindingContext dbc, IMarkableRidget ridget, IObservableValue value) {
		dbc.bindValue(BeansObservables.observeValue(ridget, IMarkableRidget.PROPERTY_ENABLED), value, null, null);
	}

	private ITreeNode createTreeInput() {
		ITreeNode root = new TreeNode("root");

		ITreeNode groupA = new TreeNode(root, "group a");
		new TreeNode(groupA, "a_child_1");
		new TreeNode(groupA, "a_child_2");
		new TreeNode(groupA, "a_child_3");

		ITreeNode groupB = new TreeNode(root, "group b");
		new TreeNode(groupB, "b_child_1");
		new TreeNode(groupB, "b_child_2");
		new TreeNode(groupB, "b_child_3");

		ITreeNode groupC = new TreeNode(root, "group c");
		new TreeNode(groupC, "c_child_1");
		new TreeNode(groupC, "c_child_2");
		new TreeNode(groupC, "c_child_3");

		return root;
	}

	private String getNewValue(Object oldValue) {
		String newValue = null;
		if (oldValue != null) {
			Shell shell = ((Button) buttonRename.getUIControl()).getShell();
			IInputValidator validator = new IInputValidator() {
				public String isValid(String newText) {
					boolean isValid = newText.trim().length() > 0;
					return isValid ? null : "Name cannot be empty!";
				}
			};
			InputDialog dialog = new InputDialog(shell, "Rename", "Enter a new name:", String.valueOf(oldValue),
					validator);
			int result = dialog.open();
			if (result == Window.OK) {
				newValue = dialog.getValue();
			}
		}
		return newValue;
	}
}