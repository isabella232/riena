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

import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
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
import org.eclipse.riena.ui.ridgets.tree.ITreeNode;
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

		buttonAddSibling.setText("Add &Sibling");
		buttonAddSibling.setEnabled(false);
		buttonAddSibling.addListener(new IActionListener() {
			public void callback() {
				DefaultObservableTreeNode node = (DefaultObservableTreeNode) tree.getSingleSelectionObservable()
						.getValue();
				if (node != null) {
					DefaultObservableTreeNode parent = (DefaultObservableTreeNode) node.getParent();
					if (parent != null) { // make sure its not the root node
						parent.addChild(new DefaultObservableTreeNode("NEW_SIBLING"));
					}
				}
			}
		});

		buttonAddChild.setText("Add &Child");
		buttonAddChild.setEnabled(false);
		buttonAddChild.addListener(new IActionListener() {
			public void callback() {
				DefaultObservableTreeNode node = (DefaultObservableTreeNode) tree.getSingleSelectionObservable()
						.getValue();
				if (node != null) {
					node.addChild(new DefaultObservableTreeNode("NEW_CHILD"));
				}
			}
		});

		buttonRename.setText("&Rename");
		buttonRename.setEnabled(false);
		buttonRename.addListener(new IActionListener() {
			public void callback() {
				DefaultObservableTreeNode node = (DefaultObservableTreeNode) tree.getSingleSelectionObservable()
						.getValue();
				if (node != null) {
					String newValue = getNewValue(node.getUserObject());
					if (newValue != null) {
						node.getModel().setUserObject(node, newValue);
					}
				}
			}
		});

		buttonDelete.setText("&Delete");
		buttonDelete.setEnabled(false);
		buttonDelete.addListener(new IActionListener() {
			public void callback() {
				DefaultObservableTreeNode node = (DefaultObservableTreeNode) tree.getSingleSelectionObservable()
						.getValue();
				if (node != null) {
					DefaultObservableTreeNode parent = (DefaultObservableTreeNode) node.getParent();
					if (parent != null) { // make sure it's not the root node
						parent.removeChild(node);
					}
				}
			}
		});

		WritableValue singleSelection = new WritableValue();
		singleSelection.addValueChangeListener(new IValueChangeListener() {
			public void handleValueChange(ValueChangeEvent event) {
				Object selectedValue = event.getObservableValue().getValue();
				boolean isTreeNode = selectedValue instanceof ITreeNode;
				boolean hasParent = isTreeNode && ((ITreeNode) selectedValue).getParent() != null;
				buttonAddChild.setEnabled(isTreeNode);
				buttonAddSibling.setEnabled(hasParent);
				buttonDelete.setEnabled(hasParent);
				buttonRename.setEnabled(isTreeNode);
			}
		});
		tree.bindSingleSelectionToModel(singleSelection);
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