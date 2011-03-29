/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.core.singleton;

import java.lang.reflect.Constructor;

import org.eclipse.riena.core.wire.Wire;

/**
 * A singleton provider that creates <i>true</i> singletons.
 * <p>
 * The {@code SingletonProvider} creates singletons based on their type
 * {@code S}. A <i>true</i> singleton is a singleton that exists only once.<br>
 * The {@code SingletonProvider} also wires the singletons.
 * 
 * @param <S>
 *            the type of the singleton
 * 
 * @since 3.0
 */
public class SingletonProvider<S> {

	protected final Class<S> singletonClass;
	private volatile S singleton;

	/**
	 * Create {@code SingletonProvider} that creates a singleton for the
	 * specified {@code singletonClass}. This constructor creates a <i>true</i>
	 * singleton, i.e. it will occur only once.
	 * 
	 * @param singletonClass
	 *            the class of the requested singleton
	 */
	public SingletonProvider(final Class<S> singletonClass) {
		this.singletonClass = singletonClass;
	}

	/**
	 * Return the requested singleton.
	 * 
	 * @return the singleton
	 */
	public S getInstance() {
		// Double-check idiom for lazy initialization of instance fields (Joshua Bloch, Effective Java, Item 71)
		S result = singleton;
		if (result == null) { // First check (no locking)
			synchronized (this) {
				result = singleton;
				if (result == null) {
					singleton = result = newWiredInstance();
				}
			}
		}
		return result;
	}

	private S newWiredInstance() {
		boolean isAccessible = true;
		Constructor<S> constructor = null;
		try {
			constructor = singletonClass.getDeclaredConstructor();
			isAccessible = constructor.isAccessible();
			if (!isAccessible) {
				constructor.setAccessible(true);
			}
			final S result = constructor.newInstance();
			Wire.instance(result).andStart();
			return result;
		} catch (final Exception e) {
			throw new SingletonFailure("Could not instantiate RCP controlled singleton.", e); //$NON-NLS-1$
		} finally {
			if (!isAccessible && constructor != null) {
				constructor.setAccessible(false);
			}
		}
	}
}
