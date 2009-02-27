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
package org.eclipse.riena.ui.ridgets.tree;

import org.eclipse.core.databinding.observable.AbstractObservable;
import org.eclipse.core.databinding.observable.Realm;

/**
 * 
 */
public abstract class AbstractObservableTreeModel extends AbstractObservable implements IObservableTreeModel {

	private boolean stale = false;

	public AbstractObservableTreeModel() {

		super(Realm.getDefault());
	}

	/*
	 * @see org.eclipse.riena.ui.internal.ridgets.tree.ITreeModel#addTreeModelListener(org.eclipse.riena.ui.internal.ridgets.tree.ITreeModelListener)
	 */
	public void addTreeModelListener(ITreeModelListener listener) {
		addListener(TreeModelEvent.TYPE, listener);
	}

	/*
	 * @see org.eclipse.riena.ui.internal.ridgets.tree.ITreeModel#removeTreeModelListener(org.eclipse.riena.ui.internal.ridgets.tree.ITreeModelListener)
	 */
	public void removeTreeModelListener(ITreeModelListener listener) {
		removeListener(TreeModelEvent.TYPE, listener);
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
