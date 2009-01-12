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

import java.util.List;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.internal.databinding.viewers.SelectionProviderMultipleSelectionObservableList;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;

/**
 * Creates an observable list based on the current selection of a given
 * selection provider. This observable will discard updates when the ridget is
 * set to "output only". This prevents the user from modifying the multiple
 * selection observable (at the other end of the binding) when the ridget is in
 * "output only" mode.
 */
@SuppressWarnings("restriction")
public final class OutputAwareMultipleSelectionObservableList extends SelectionProviderMultipleSelectionObservableList {

	private final IMarkableRidget ridget;

	public OutputAwareMultipleSelectionObservableList(Realm realm, ISelectionProvider selectionProvider,
			Object elementType, IMarkableRidget ridget) {
		super(realm, selectionProvider, elementType);
		Assert.isNotNull(ridget);
		this.ridget = ridget;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void updateWrappedList(List newList) {
		if (!ridget.isOutputOnly()) {
			super.updateWrappedList(newList);
		}
	}
};
