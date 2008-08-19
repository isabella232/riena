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
package org.eclipse.riena.ui.ridgets.util.beans;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.ITreeRidget;

import org.eclipse.core.runtime.Assert;

/**
 * Bean that stores information about a single word (Strin). It is used in
 * examples that demo the {@link ITreeRidget} and {@link ITableRidget}.
 */
public final class WordNode extends AbstractBean {

	private final WordNode parent;

	private String word;
	private boolean isUpperCase;
	private List<WordNode> children;

	public WordNode(String word) {
		this(null, word);
	}

	public WordNode(WordNode parent, String word) {
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
