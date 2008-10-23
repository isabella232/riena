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

import java.util.Collection;

/**
 * An UI Filter holds a collection of different attributes with witch the UI can
 * be used to parts of the UI.
 */
public interface IUIFilter {

	/**
	 * Returns the attributes of the filter
	 * 
	 * @return The attributes of the filter.
	 */
	Collection<? extends IUIFilterAttribute> getFilterAttributes();

	/**
	 * Returns the ID of the filter
	 * 
	 * @return filterID
	 */
	String getFilterID();

}
