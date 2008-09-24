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
package org.eclipse.riena.ui.ridgets.filter;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.ui.filter.AbstractUIFilterMarkerAttribute;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;

/**
 * Filter attribute to provide a marker for a ridget.
 */
public abstract class AbstractRidgetUIFilterMarkerAttribute extends AbstractUIFilterMarkerAttribute {

	private RidgetMatcher matcher;

	public AbstractRidgetUIFilterMarkerAttribute(String id, IMarker marker) {
		super(marker);
		matcher = new RidgetMatcher(id);
	}

	/**
	 * This method compares the ID of this attribute and the given ID of a
	 * ridget.
	 * 
	 * @see org.eclipse.riena.ui.filter.IUIFilterAttribute#matches(java.lang.Object)
	 */
	public boolean matches(Object object) {
		return matcher.matches(object);
	}

	/**
	 * Adds the marker of this attribute to the given object (if the object is
	 * an markable ridget).
	 * 
	 * @see org.eclipse.riena.ui.filter.IUIFilterAttribute#apply(java.lang.Object)
	 */
	public void apply(Object object) {
		if (object instanceof IMarkableRidget) {
			IMarkableRidget markableRidget = (IMarkableRidget) object;
			markableRidget.addMarker(getMarker());
		}
	}

	/**
	 * Removes the marker of this attribute from the given object (if the object
	 * is an markable ridget).
	 * 
	 * @see org.eclipse.riena.ui.filter.IUIFilterAttribute#remove(java.lang.Object)
	 */
	public void remove(Object object) {
		if (object instanceof IMarkableRidget) {
			IMarkableRidget markableRidget = (IMarkableRidget) object;
			markableRidget.removeMarker(getMarker());
		}
	}

}
