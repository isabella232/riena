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
package org.eclipse.riena.beans.common;

import java.util.Comparator;

/**
 * A comparator that works with any class that implements Comparable.
 * <p>
 * Examples:
 * 
 * <pre>
 * new TypedComparator&lt;String&gt;();
 * new TypedComparator&lt;Integer&gt;();
 * new TypedComparator&lt;Boolean&gt;();
 * </pre>
 */
public class TypedComparator<T extends Comparable<T>> implements Comparator<T> {

	private static final long serialVersionUID = 1L;

	public int compare(T o1, T o2) {
		return o1.compareTo(o2);
	}
}
