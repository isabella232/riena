/*******************************************************************************
 * Copyright (c) 2004, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     compeople AG - modified for generics, added enums, ..
 *                   (otherwise a true copy of org.eclipse.core.runtime.ListenerList)
 *******************************************************************************/
package org.eclipse.riena.core.util;

import java.lang.reflect.Array;

/**
 * This class is a thread safe list that is designed for storing lists of
 * listeners. The implementation is optimized for minimal memory footprint,
 * frequent reads and infrequent writes. Modification of the list is
 * synchronized and relatively expensive, while accessing the listeners is very
 * fast. Readers are given access to the underlying array data structure for
 * reading, with the trust that they will not modify the underlying array.
 * <p>
 * <a name="same">A listener list handles the <i>same</i> listener being added
 * multiple times, and tolerates removal of listeners that are the same as other
 * listeners in the list. For this purpose, listeners can be compared with each
 * other using either equality or identity, as specified in the list
 * constructor.
 * </p>
 * <p>
 * Use the <code>getListeners</code> method when notifying listeners. The
 * recommended code sequence for notifying all registered listeners of say,
 * <code>FooListener.eventHappened</code>, is:
 * 
 * <pre>
 * ListenerList&lt;FooListener&gt; myListenerList = new ListenerList&lt;FooListener&gt;(FooListener.class);
 * ..
 * for (L listener : myListenerList.getListeners()) {
 * 	listener.eventHappened(event);
 * }
 * </pre>
 * 
 * </p>
 * <p>
 * This class can be used without OSGi running.
 * </p>
 * 
 */
public class ListenerList<L> {

	public enum Mode {

		/**
		 * Mode constant indicating that listeners should be considered the <a
		 * href="#same">same</a> if they are equal.
		 */
		EQUALITY,

		/**
		 * Mode constant indicating that listeners should be considered the <a
		 * href="#same">same</a> if they are identical.
		 */
		IDENTITY
	};

	/**
	 * Indicates the comparison mode used to determine if two listeners are
	 * equivalent
	 */
	private final boolean identity;

	/**
	 * The list of listeners. Initially empty but initialized to an array of
	 * size capacity the first time a listener is added. Maintains invariant:
	 * listeners != null
	 */
	private volatile L[] listeners;

	/**
	 * The empty array singleton instance.
	 */
	private final Object emptyArray;

	/**
     * The listener class (used internally for creating arrays of this type).
     */
	private final Class<?> listenerClass;

	@SuppressWarnings("unused")
	private ListenerList() {
		this(null);
	}

	/**
	 * Creates a listener list in which listeners are compared using equality.
	 * 
	 * @param listenerClass
	 *            the listener class (used internally for creating arrays)
	 */
	public ListenerList(Class<?> listenerClass) {
		this(Mode.EQUALITY, listenerClass);
	}

	/**
	 * Creates a listener list using the provided comparison mode.
	 * 
	 * @param mode
	 *            The mode used to determine if listeners are the <a
	 *            href="#same">same</a>.
	 * @param listenerClass
	 *            the listener class (used internally for creating arrays)
	 */
	@SuppressWarnings("unchecked")
	public ListenerList(Mode mode, Class<?> listenerClass) {
		this.identity = mode == Mode.IDENTITY;
		this.listenerClass = listenerClass;
		this.emptyArray = Array.newInstance(listenerClass, 0);
		this.listeners = (L[]) emptyArray;
	}

	/**
	 * Adds a listener to this list. This method has no effect if the <a
	 * href="#same">same</a> listener is already registered.
	 * 
	 * @param listener
	 *            the non-<code>null</code> listener to add
	 */
	@SuppressWarnings("unchecked")
	public synchronized void add(L listener) {
		// This method is synchronized to protect against multiple threads
		// adding or removing listeners concurrently. This does not block
		// concurrent readers.
		if (listener == null) {
			throw new IllegalArgumentException();
		}
		// check for duplicates
		for (L each : listeners) {
			if (identity ? listener == each : listener.equals(each)) {
				return;
			}
		}

		// Thread safety: create new array to avoid affecting concurrent readers
		final int oldSize = listeners.length;
		L[] newListeners = (L[]) Array.newInstance(listenerClass, oldSize + 1);
		System.arraycopy(listeners, 0, newListeners, 0, oldSize);
		newListeners[oldSize] = listener;
		// atomic assignment
		this.listeners = newListeners;
	}

	/**
	 * Returns an array containing all the registered listeners. The resulting
	 * array is unaffected by subsequent adds or removes. If there are no
	 * listeners registered, the result is an empty array. Use this method when
	 * notifying listeners, so that any modifications to the listener list
	 * during the notification will have no effect on the notification itself.
	 * <p>
	 * Note: Callers of this method <b>must not</b> modify the returned array.
	 * 
	 * @return the list of registered listeners
	 */
	public L[] getListeners() {
		return listeners;
	}

	/**
	 * Returns whether this listener list is empty.
	 * 
	 * @return <code>true</code> if there are no registered listeners, and
	 *         <code>false</code> otherwise
	 */
	public boolean isEmpty() {
		return listeners.length == 0;
	}

	/**
	 * Removes a listener from this list. Has no effect if the <a
	 * href="#same">same</a> listener was not already registered.
	 * 
	 * @param listener
	 *            the non-<code>null</code> listener to remove
	 */
	@SuppressWarnings("unchecked")
	public synchronized void remove(L listener) {
		// This method is synchronized to protect against multiple threads
		// adding or removing listeners concurrently. This does not block
		// concurrent readers.
		if (listener == null) {
			throw new IllegalArgumentException();
		}
		final int oldSize = listeners.length;
		for (int i = 0; i < oldSize; ++i) {
			L each = listeners[i];
			if (identity ? listener == each : listener.equals(each)) {
				if (oldSize == 1) {
					listeners = (L[]) emptyArray;
				} else {
					// Thread safety: create new array to avoid affecting
					// concurrent readers
					L[] newListeners = (L[]) Array.newInstance(listenerClass, oldSize - 1);
					System.arraycopy(listeners, 0, newListeners, 0, i);
					System.arraycopy(listeners, i + 1, newListeners, i, oldSize - i - 1);
					// atomic assignment to field
					this.listeners = newListeners;
				}
				return;
			}
		}
	}

	/**
	 * Returns the number of registered listeners.
	 * 
	 * @return the number of registered listeners
	 */
	public int size() {
		return listeners.length;
	}

	/**
	 * Removes all listeners from this list.
	 */
	@SuppressWarnings("unchecked")
	public synchronized void clear() {
		listeners = (L[]) emptyArray;
	}

}
