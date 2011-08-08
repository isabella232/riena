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

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.Bundle;

import org.eclipse.riena.core.util.Nop;
import org.eclipse.riena.core.util.RAPDetector;
import org.eclipse.riena.core.wire.Wire;

/**
 * A generic (RCP/RAP) singleton provider.
 * <p>
 * The {@code SessionSingletonProvider} creates singletons based on their type
 * {@code S}. It <i>automagically</i> detects whether it runs in a RAP/RWT
 * environment. In this case it delegates singleton creation/management to the
 * RAP {@code SessionSingletonBase} which takes care of session managed
 * singletons.<br>
 * In a <i>standard RCP</i> environment it handles the singleton
 * creation/management itself by delegating this to it's super class.<br>
 * However, in both cases it wires the singletons.
 * 
 * @param <S>
 *            the type of the singleton
 * 
 * @since 3.0
 */
public class SessionSingletonProvider<S> extends SingletonProvider<S> {

	private static final boolean IS_RAP_AVAILABLE;
	private static Class<?> sessionSingletonBaseClass;
	private static Method getInstanceMethod;
	private static final Set<Object> RAP_SINGLETONS = new HashSet<Object>();

	private static final String SESSION_SINGLETON_BASE = "org.eclipse.rwt.SessionSingletonBase"; //$NON-NLS-1$
	private static final String GET_INSTANCE = "getInstance"; //$NON-NLS-1$

	static {
		IS_RAP_AVAILABLE = RAPDetector.isRAPavailable() && loadSessionSingletonBase();
	}

	/**
	 * Create {@code SessionSingletonProvider} that creates (a) singleton(s) for
	 * the specified {@code singletonClass}.
	 * 
	 * @param singletonClass
	 *            the class of the requested singleton
	 */
	public SessionSingletonProvider(final Class<S> singletonClass) {
		super(singletonClass);
	}

	/**
	 * Load the RAP {@code SessionSingletonBase} class and the
	 * {@code getInstance} method as a side effect.
	 * 
	 * @return {@code true} if class and method have been found; otherwise
	 *         {@code false}
	 */
	private static boolean loadSessionSingletonBase() {
		final Bundle rapBundle = RAPDetector.getRWTBundle();
		try {
			sessionSingletonBaseClass = rapBundle.loadClass(SESSION_SINGLETON_BASE);
			getInstanceMethod = sessionSingletonBaseClass.getMethod(GET_INSTANCE, Class.class);
			return true;
		} catch (final Exception e) {
			Nop.reason("There seems to be no RAP available."); //$NON-NLS-1$
			return false;
		}
	}

	/**
	 * Return the requested singleton.
	 * 
	 * @return the singleton
	 */
	@Override
	public S getInstance() {
		return IS_RAP_AVAILABLE ? getRAPInstance() : super.getInstance();
	}

	private S getRAPInstance() {
		try {
			final S rapSingleton = (S) getInstanceMethod.invoke(sessionSingletonBaseClass, singletonClass);
			synchronized (RAP_SINGLETONS) {
				if (!RAP_SINGLETONS.contains(rapSingleton)) {
					RAP_SINGLETONS.add(rapSingleton);
					Wire.instance(rapSingleton).andStart();
				}
			}
			return rapSingleton;
		} catch (final Exception e) {
			throw new SingletonFailure("Could not instantiate RAP controlled singleton.", e); //$NON-NLS-1$
		}
	}

}
