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
package org.eclipse.riena.internal.ui.core.context;

/**
 * Interface representing the ability to hold key value pairs of context data.
 */
public interface IContext {

	/**
	 * @return the saved value for the given key
	 */
	Object getContext(String key);

	/**
	 * saves the value for the given key
	 */
	void setContext(String key, Object value);

}
