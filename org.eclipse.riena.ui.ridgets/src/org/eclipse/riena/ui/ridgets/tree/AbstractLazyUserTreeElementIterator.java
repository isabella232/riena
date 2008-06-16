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
package org.eclipse.riena.ui.ridgets.tree;

import java.util.Collection;
import java.util.Iterator;

/**
 * A helper class wrapping an Iterator for the children of an IUserTreeElement
 * that will not create the actual iterator until the first element is accessed
 * using next(). Before that hasNext() is determined otherwise.
 */
public abstract class AbstractLazyUserTreeElementIterator implements Iterator<Collection<IUserTreeElement>> {

	private Iterator<Collection<IUserTreeElement>> wrappee;

	/**
	 * Indicates whether the element has children. Used as the return value for
	 * hasNext() until the first call of next(). May return true even though the
	 * actual iterator is empty.
	 * 
	 * @return true if the element has children, false otherwise.
	 */
	protected abstract boolean hasChildren();

	/**
	 * Creates the actual iterator. Invoked when next() is called for the first
	 * time.
	 * 
	 * @return The actual iterator of the elements children.
	 */
	protected abstract Iterator<Collection<IUserTreeElement>> getChildIterator();

	/**
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext() {
		if (wrappee != null) {
			return wrappee.hasNext();
		} else {
			return hasChildren();
		}
	}

	/**
	 * @see java.util.Iterator#next()
	 */
	public Collection<IUserTreeElement> next() {
		if (wrappee == null) {
			wrappee = getChildIterator();
			if (wrappee == null || !wrappee.hasNext()) {
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
