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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.ITreeRidget;
import org.eclipse.riena.ui.ridgets.tree.IObservableTreeModel;
import org.eclipse.riena.ui.ridgets.tree.ITreeNode;

/**
 * TODO [ev] docs
 */
public class TreeRidget extends AbstractSelectableRidget implements ITreeRidget {

	@Override
	public int getSelectionIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int[] getSelectionIndices() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int indexOfOption(Object option) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void bindUIControl() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void checkUIControl(Object uiControl) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void unbindUIControl() {
		// TODO Auto-generated method stub

	}

	// ITreeRidget methods
	// ////////////////////

	public void addDoubleClickListener(IActionListener listener) {
		// TODO Auto-generated method stub

	}

	public void bindToModel(IObservableTreeModel observableTreeModel) {
		// TODO Auto-generated method stub

	}

	public void collapse(ITreeNode node) {
		// TODO Auto-generated method stub

	}

	public void collapseTree() {
		// TODO Auto-generated method stub

	}

	public void expand(ITreeNode node) {
		// TODO Auto-generated method stub

	}

	public void expandTree() {
		// TODO Auto-generated method stub

	}

	public IObservableTreeModel getRidgetObservable() {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeDoubleClickListener(IActionListener listener) {
		// TODO Auto-generated method stub

	}

}
