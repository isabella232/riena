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
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.riena.example.client.views.TreeView;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleNodeViewController;
import org.eclipse.riena.ui.ridgets.ISelectableRidget;
import org.eclipse.riena.ui.ridgets.ITreeTableRidget;
import org.eclipse.riena.ui.ridgets.util.beans.AbstractBean;

/**
 * Controller for the {@link TreeView} example.
 */
public class TreeTableViewController extends SubModuleNodeViewController {

	private ITreeTableRidget tree;

	public ITreeTableRidget getTree() {
		return tree;
	}

	public void setTree(ITreeTableRidget tree) {
		this.tree = tree;
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
		if (tree != null) {
			tree.setSelectionType(ISelectableRidget.SelectionType.SINGLE);
			Object[] roots = createTreeInput();
			String[] columnPropertyNames = { "word", "upperCase", "ACount" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			String[] columnHeaders = { "Word", "Uppercase", "A Count" };
			tree.bindToModel(roots, WordNode.class, "children", "parent", columnPropertyNames, columnHeaders); //$NON-NLS-1$ //$NON-NLS-2$
			tree.updateFromModel();
			tree.expand(roots[0]);
		}
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

	private static class WordNode extends AbstractBean {

		private final WordNode parent;
		private final String word;
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
			return Collections.unmodifiableList(children);
		}

		public WordNode getParent() {
			return parent;
		}

		public String getWord() {
			return word;
		}

		public boolean isUpperCase() {
			return isUpperCase;
		}

		/**
		 * @param isUppercase
		 *            the isUppercase to set
		 */
		public void setUpperCase(boolean isUppercase) {
			boolean oldValue = this.isUpperCase;
			this.isUpperCase = isUppercase;
			firePropertyChanged("upperCase", oldValue, this.isUpperCase); //$NON-NLS-1$
		}

		@Override
		public String toString() {
			return word;
		}

		private void addChild(WordNode child) {
			Assert.isNotNull(child);
			children.add(child);
		}

	}
}