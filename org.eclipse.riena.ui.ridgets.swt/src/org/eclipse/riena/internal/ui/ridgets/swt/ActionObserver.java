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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**
 * This class notifies a collection of action {@link IActionListener} when a
 * widget is selected.
 */
class ActionObserver extends SelectionAdapter {

	private List<IActionListener> actionListeners;

	ActionObserver() {
		super();
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		fireAction();
	}

	void addListener(IActionListener listener) {
		Assert.isNotNull(listener, "listener is null"); //$NON-NLS-1$
		if (actionListeners == null) {
			actionListeners = new ArrayList<IActionListener>();
		}
		actionListeners.add(listener);
	}

	void removeListener(IActionListener listener) {
		if (actionListeners != null) {
			actionListeners.remove(listener);
		}
	}

	void fireAction() {
		if (actionListeners != null) {
			for (IActionListener listener : actionListeners) {
				listener.callback();
			}
		}
	}

}
