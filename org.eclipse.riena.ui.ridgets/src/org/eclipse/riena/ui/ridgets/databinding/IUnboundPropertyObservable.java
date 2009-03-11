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
package org.eclipse.riena.ui.ridgets.databinding;

import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.list.IObservableList;

/**
 * Extends IObservable to add some support for objects that do not conform to
 * the JavaBean specification for bound properties. Changes of the properties of
 * such objects cannot be detected by adding PropertyChangeListeners.
 * 
 * @deprecated use {@link IObservableList} or {@link IObservable}
 */
public interface IUnboundPropertyObservable extends IObservable {

	/**
	 * Updates the value of the observable from the underlying bean and fires
	 * change events.
	 */
	void updateFromBean();

}
