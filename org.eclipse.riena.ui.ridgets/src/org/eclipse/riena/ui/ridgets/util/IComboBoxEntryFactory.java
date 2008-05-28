/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.util;

/**
 * Interface to be implemented by classes, which may be used by a
 * <code>ListAdapter</code> to create list entries from a string.
 * 
 * @author Erich Achilles
 */
public interface IComboBoxEntryFactory {

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