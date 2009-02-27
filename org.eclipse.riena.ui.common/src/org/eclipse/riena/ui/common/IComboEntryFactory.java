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
package org.eclipse.riena.ui.common;

/**
 * Interface to be implemented by classes, which may be used by a
 * <code>ListAdapter</code> to create list entries from a string.
 */
public interface IComboEntryFactory {

	/**
	 * Implementing class should return the appropriate list entry object,
	 * constructed from string <code>value</code>. I.e. for example a Person
	 * object for a list of persons.
	 * 
	 * @param value
	 *            the object used to create the new entry.
	 * @return Object the created object.
	 */
	Object createNewEntry(Object value);
}
