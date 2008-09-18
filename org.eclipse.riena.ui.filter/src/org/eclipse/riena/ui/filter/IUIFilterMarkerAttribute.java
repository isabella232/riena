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
package org.eclipse.riena.ui.filter;

import org.eclipse.riena.core.marker.IMarker;

/**
 * An filter attributes can manipulate one attribute of an UI element (i.g.
 * marker of a ridget or visibility of a navigation node).
 */
public interface IUIFilterMarkerAttribute extends IUIFilterAttribute {

	/**
	 * Returns the marker of this IUIFilterMarkerAttribute .
	 */
	IMarker getMarker();

}
