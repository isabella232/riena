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
package org.eclipse.riena.core.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * The Iter class provides conversions from classes only implementing Iterator
 * or Enumeration such that they can be used within the extended for()-loop
 * (foreach).<br>
 * However, all the <code>able()</code> methods can be used with a
 * <code>null</code> parameter. Most important, checking for <code>null</code>
 * is not necessary!<br>
 * <b>Note: </b>All these methods induce a small performance penalty.
 */
public final class Iter {

	private Iter() {
		// locked
	}

	/**
	 * Create an Iterable for the given iterator.
	 * 
	 * @param <T>
	 *            generic type for the Iterable
	 * @param iterator
	 *            the iterator (might be null!)
	 * 
	 * @return the Iterable
	 */
	public static <T> Iterable<T> able(Iterator<T> iterator) {
		if (iterator == null) {
			return Collections.emptyList();
		} else {
			return new IteratorIterable<T>(iterator);
		}
	}

	/**
	 * Create an Iterable for the given iterator.
	 * 
	 * @param <T>
	 *            generic type for the Iterable
	 * @param enumeration
	 *            the enumeration (might be null!)
	 * 
	 * @return the Iterable
	 */
	public static <T> Iterable<T> able(Enumeration<T> enumeration) {
		if (enumeration == null) {
			return Collections.emptyList();
		} else {
			return new IteratorIterable<T>(new EnumerationIterator<T>(enumeration));
		}
	}

	/**
	 * Create an Iterable for the given array.
	 * 
	 * @param <T>
	 *            generic type for the Iterable
	 * @param array
	 *            the array (might be null!)
	 * 
	 * @return the Iterable
	 */
	public static <T> Iterable<T> able(T[] array) {
		if (array == null) {
			return Collections.emptyList();
		} else {
			return Arrays.asList(array);
		}
	}

	/**
	 * Create an Iterable for the given iterator. This method´s intended use is
	 * for ´pre generic´ apis.
	 * 
	 * @param <T>
	 *            generic type for the Iterable
	 * @param iterator
	 *            the iterator (might be null!)
	 * @param t
	 *            the type of the iterator
	 * 
	 * @return the Iterable
	 */
	@SuppressWarnings("unchecked")
	public static <T> Iterable<T> able(Iterator iterator, Class<T> t) {
		return able(iterator);
	}

	/**
	 * Create an Iterable for the given iterator. This method´s intended use is
	 * for ´pre generic´ apis.
	 * 
	 * @param <T>
	 *            generic type for the Iterable
	 * @param enumeration
	 *            the enumeration (might be null!)
	 * @param t
	 *            the type of the iterator
	 * 
	 * @return the Iterable
	 */
	@SuppressWarnings("unchecked")
	public static <T> Iterable<T> able(Enumeration enumeration, Class<T> t) {
		return able(enumeration);
	}

	private static final class IteratorIterable<T> implements Iterable<T> {

		private Iterator<T> iterator;

		private IteratorIterable(Iterator<T> iterator) {
			this.iterator = iterator;
		}

		public Iterator<T> iterator() {
			return iterator;
		}

	}

	private static final class EnumerationIterator<T> implements Iterator<T> {

		private Enumeration<T> enumeration;

		private EnumerationIterator(Enumeration<T> enumeration) {
			this.enumeration = enumeration;
		}

		public boolean hasNext() {
			return enumeration.hasMoreElements();
		}

		public T next() {
			return enumeration.nextElement();
		}

		public void remove() {
			throw new NoSuchMethodError("No remove() on an wrapped Enumeration!");
		}

	}
}
