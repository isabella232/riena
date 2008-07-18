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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.Assert;
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
import org.eclipse.riena.ui.ridgets.ITreeTableRidget;
import org.eclipse.riena.ui.ridgets.util.beans.AbstractBean;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;

/**
 * Controller for the {@link TreeView} example.
 */
public class TreeTableViewController extends SubModuleNodeViewController {

	private ITreeTableRidget tree;
	private IActionRidget buttonAddSibling;
	private IActionRidget buttonAddChild;
	private IActionRidget buttonRename;
	private IActionRidget buttonDelete;
	private IActionRidget buttonExpand;
	private IActionRidget buttonCollapse;

	public ITreeTableRidget getTree() {
		return tree;
	}

	public void setTree(ITreeTableRidget tree) {
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

	public TreeTableViewController(ISubModuleNode navigationNode) {
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
		Object[] roots = createTreeInput();
		String[] columnPropertyNames = { "word", "upperCase", "ACount" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		String[] columnHeaders = { "Word", "Uppercase", "A Count" };
		tree.bindToModel(roots, WordNode.class, "children", "parent", columnPropertyNames, columnHeaders); //$NON-NLS-1$ //$NON-NLS-2$
		tree.updateFromModel();
		tree.expand(roots[0]);
		tree.setSelectionType(ISelectableRidget.SelectionType.SINGLE);
		tree.setComparator(0, new StringComparator());
		tree.setComparator(1, new StringComparator());
		tree.setColumnSortable(2, false);

		tree.addDoubleClickListener(new IActionListener() {
			public void callback() {
				WordNode node = (WordNode) tree.getSingleSelectionObservable().getValue();
				if (node != null) {
					boolean isUpperCase = !node.isUpperCase();
					node.setUpperCase(isUpperCase);
				}
			}
		});

		buttonAddSibling.setText("Add &Sibling");
		buttonAddSibling.addListener(new IActionListener() {
			public void callback() {
				WordNode node = (WordNode) tree.getSingleSelectionObservable().getValue();
				WordNode parent = (node != null) ? node.getParent() : null;
				if (parent != null) {
					new WordNode(parent, "A_NEW_SIBLING");
				}
			}
		});

		buttonAddChild.setText("Add &Child");
		buttonAddChild.addListener(new IActionListener() {
			public void callback() {
				WordNode node = (WordNode) tree.getSingleSelectionObservable().getValue();
				if (node != null) {
					new WordNode(node, "ANOTHER_CHILD");
				}
			}
		});

		buttonRename.setText("&Modify");
		buttonRename.addListener(new IActionListener() {
			public void callback() {
				WordNode node = (WordNode) tree.getSingleSelectionObservable().getValue();
				if (node != null) {
					String newValue = getNewValue(node.getWordIgnoreUppercase());
					if (newValue != null) {
						node.setWord(newValue);
					}
				}
			}
		});

		buttonDelete.setText("&Delete");
		buttonDelete.addListener(new IActionListener() {
			public void callback() {
				WordNode node = (WordNode) tree.getSingleSelectionObservable().getValue();
				WordNode parent = (node != null) ? node.getParent() : null;
				if (parent != null) {
					List<WordNode> children = parent.getChildren();
					children.remove(node);
					parent.setChildren(children);
				}
			}
		});

		buttonExpand.setText("E&xpand");
		buttonExpand.addListener(new IActionListener() {
			public void callback() {
				WordNode node = (WordNode) tree.getSingleSelectionObservable().getValue();
				if (node != null) {
					tree.expand(node);
				}
			}
		});

		buttonCollapse.setText("&Collapse");
		buttonCollapse.addListener(new IActionListener() {
			public void callback() {
				WordNode node = (WordNode) tree.getSingleSelectionObservable().getValue();
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
				if (node instanceof WordNode) {
					result = ((WordNode) node).getParent() != null;
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

	private String getNewValue(Object oldValue) {
		String newValue = null;
		if (oldValue != null) {
			Shell shell = ((Button) buttonRename.getUIControl()).getShell();
			IInputValidator validator = new IInputValidator() {
				public String isValid(String newText) {
					boolean isValid = newText.trim().length() > 0;
					return isValid ? null : "Word cannot be empty!";
				}
			};
			InputDialog dialog = new InputDialog(shell, "Modify", "Enter a new word:", String.valueOf(oldValue),
					validator);
			int result = dialog.open();
			if (result == Window.OK) {
				newValue = dialog.getValue();
			}
		}
		return newValue;
	}

	private WordNode[] createTreeInput() {
		WordNode groupA = new WordNode("A");
		WordNode groupB = new WordNode("B");
		WordNode groupC = new WordNode("C");
		WordNode groupD = new WordNode("D");
		WordNode groupE = new WordNode("E");

		WordNode node1 = new WordNode(groupA, "Abandonment");
		node1.setUpperCase(true);
		new WordNode(groupA, "Adventure");
		new WordNode(groupA, "Acclimatisation");
		new WordNode(groupA, "Aardwark");
		new WordNode(groupB, "Binoculars");
		new WordNode(groupB, "Beverage");
		new WordNode(groupB, "Boredom");
		new WordNode(groupB, "Ballistics");
		new WordNode(groupC, "Calculation");
		new WordNode(groupC, "Coexistence");
		new WordNode(groupC, "Cinnamon");
		new WordNode(groupC, "Celebration");
		new WordNode(groupD, "Disney");
		new WordNode(groupD, "Dictionary");
		new WordNode(groupD, "Delta");
		new WordNode(groupD, "Desperate");
		new WordNode(groupE, "Elf");
		new WordNode(groupE, "Electronics");
		new WordNode(groupE, "Elwood");
		new WordNode(groupE, "Enemy");

		return new WordNode[] { groupA, groupB, groupC, groupD, groupE };
	}

	// helping classes
	// ////////////////

	/**
	 * This bean stores information about a word (String) and can be used with
	 * {@link ITreeRidget}s and {@link ITreeTableRidget}s.
	 */
	private static class WordNode extends AbstractBean {

		private final WordNode parent;

		private String word;
		private boolean isUpperCase;
		private List<WordNode> children;

		WordNode(String word) {
			this(null, word);
		}

		WordNode(WordNode parent, String word) {
			Assert.isNotNull(word);
			this.parent = parent;
			this.word = word;
			this.children = new ArrayList<WordNode>();
			if (parent != null) {
				parent.addChild(this);
			}
		}

		public int getACount() {
			int result = 0;
			for (char c : word.toCharArray()) {
				if (c == 'a' || c == 'A') {
					result++;
				}
			}
			return result;
		}

		public List<WordNode> getChildren() {
			return new ArrayList<WordNode>(children);
		}

		public WordNode getParent() {
			return parent;
		}

		public String getWord() {
			return isUpperCase ? word.toUpperCase() : word;
		}

		public String getWordIgnoreUppercase() {
			return word;
		}

		public boolean isUpperCase() {
			return isUpperCase;
		}

		public void setChildren(List<WordNode> children) {
			List<WordNode> oldChildren = this.children;
			this.children = new ArrayList<WordNode>(children);
			firePropertyChanged("children", oldChildren, this.children); //$NON-NLS-1$
		}

		public void setUpperCase(boolean isUppercase) {
			boolean oldValue = this.isUpperCase;
			this.isUpperCase = isUppercase;
			firePropertyChanged("upperCase", oldValue, this.isUpperCase); //$NON-NLS-1$
		}

		public void setWord(String word) {
			String oldWord = word;
			this.word = word;
			firePropertyChanged("word", oldWord, this.word); //$NON-NLS-1$
		}

		@Override
		public String toString() {
			return word;
		}

		private void addChild(WordNode child) {
			Assert.isNotNull(child);
			List<WordNode> newChildren = getChildren();
			newChildren.add(child);
			setChildren(newChildren);
		}
	}

	/**
	 * Compares two strings.
	 */
	private static final class StringComparator implements Comparator<Object> {
		public int compare(Object o1, Object o2) {
			String s1 = (String) o1;
			String s2 = (String) o2;
			return s1.compareTo(s2);
		}
	}
}