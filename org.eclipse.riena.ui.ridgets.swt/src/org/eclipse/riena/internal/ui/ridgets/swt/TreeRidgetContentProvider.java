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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.jface.databinding.viewers.ObservableListTreeContentProvider;
import org.eclipse.jface.databinding.viewers.TreeStructureAdvisor;
import org.eclipse.jface.viewers.Viewer;

import org.eclipse.riena.ui.ridgets.ITreeRidget;

/**
 * Extends a standard observable tree content provider with support for:
 * <ul>
 * <li>handling Object[] input</li>
 * <li>knowing when we have valid input</li>
 * <li>showing / hiding the roots of the tree - see
 * {@link ITreeRidget#setRootsVisible(boolean)}</li>
 * </ul>
 * 
 * @deprecated TODO [ev] remove after updating to 3.5 M7, see 266042, 266038
 */
public final class TreeRidgetContentProvider extends ObservableListTreeContentProvider {

	private boolean hasInput = false;

	public TreeRidgetContentProvider(IObservableFactory listFactory, TreeStructureAdvisor structureAdvisor) {
		super(listFactory, structureAdvisor);
	}

	@Override
	public synchronized void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		/*
		 * this is a workaround to allow our set change listener, which is in
		 * charge triggering an update of the tree icons, to skip the update
		 * when the viewer is in the process of disposing itself (newInput ==
		 * null)
		 */
		hasInput = (newInput != null);
		super.inputChanged(viewer, oldInput, newInput);
	}

	/** Returns true if we have a valid (i.e. non-null) input. */
	public boolean hasInput() {
		return hasInput;
	}
}