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
package org.eclipse.riena.ui.ridgets.tree;

import org.eclipse.core.databinding.observable.AbstractObservable;
import org.eclipse.core.databinding.observable.Realm;

/**
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractObservableTreeNode extends AbstractObservable implements ITreeNode {

	private boolean stale = false;

	/**
	 * 
	 */
	public AbstractObservableTreeNode() {

		this(Realm.getDefault());
	}

	/**
	 * @param parent
	 */
	public AbstractObservableTreeNode(Realm realm) {

		super(realm);
	}

	/*
	 * @see org.eclipse.riena.ui.internal.ridgets.tree.ITreeNode#getLevel()
	 */
	public int getLevel() {

		if (getParent() == null) {
			return 0;
		} else {
			return getParent().getLevel() + 1;
		}
	}

	/*
	 * @see org.eclipse.riena.ui.internal.ridgets.tree.ITreeNode#isLeaf()
	 */
	public boolean isLeaf() {
		return getChildCount() == 0;
	}

	/*
	 * @see org.eclipse.core.databinding.observable.IObservable#isStale()
	 */
	public boolean isStale() {
		return stale;
	}

	public void setStale(boolean value) {
		stale = value;
	}
}
