/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2005 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.tree;

import java.util.Collection;
import java.util.Iterator;

/**
 * A helper class wrapping an Iterator for the children of an IUserTreeElement that will
 * not create the actual iterator until the first element is accessed using next(). Before
 * that hasNext() is determined otherwise.
 *
 * @author Carsten Drossel
 */
public abstract class AbstractLazyUserTreeElementIterator implements Iterator<Collection<IUserTreeElement>> {

	private Iterator<Collection<IUserTreeElement>> wrappee;

	/**
	 * Indicates whether the element has children. Used as the return value for hasNext()
	 * until the first call of next(). May return true even though the actual iterator is
	 * empty.
	 * 
	 * @return true if the element has children, false otherwise.
	 */
	protected abstract boolean hasChildren();

	/**
	 * Creates the actual iterator. Invoked when next() is called for the first time.
	 * 
	 * @return The actual iterator of the elements children.
	 */
	protected abstract Iterator<Collection<IUserTreeElement>> getChildIterator();

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		if ( wrappee != null ) {
			return wrappee.hasNext();
		} else {
			return hasChildren();
		}
	}

	/**
	 * @see java.util.Iterator#next()
	 */
	public Collection<IUserTreeElement> next() {
		if ( wrappee == null ) {
			wrappee = getChildIterator();
			if ( wrappee == null || !wrappee.hasNext() ) {
				return null;
			}
		}
		return wrappee.next();
	}

	/**
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		throw new UnsupportedOperationException();
	}

}
