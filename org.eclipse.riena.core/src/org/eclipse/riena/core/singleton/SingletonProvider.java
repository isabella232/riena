/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
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
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.service.packageadmin.ExportedPackage;
import org.osgi.service.packageadmin.PackageAdmin;

import org.eclipse.core.runtime.Platform;

import org.eclipse.riena.core.exception.Failure;
import org.eclipse.riena.core.service.Service;
import org.eclipse.riena.core.util.Nop;
import org.eclipse.riena.core.wire.Wire;

/**
 * A generic (RCP/RAP) singleton provider.
 * <p>
 * The {@code SingletonProvider} creates singletons based on their type
 * {@code S}. It <i>automagically</i> detects whether it runs in a RAP/RWT
 * environment. In this case it delegates singleton creation/management to the
 * RAP {@code SessionSingletonBase} which takes care of session managed
 * singletons.<br>
 * In a <i>standard RCP</i> environment it handles the singleton
 * creation/management itself.<br>
 * However, in both cases it wires the singletons.
 * 
 * @param <S>
 * 
 * @since 3.0
 */
public class SingletonProvider<S> {

	private final Class<S> singletonClass;
	private volatile S rcpSingleton;

	private static Class<?> sessionSingletonBase;
	private static Method getInstance;
	private static final boolean IS_RAP;
	private static final Set<Object> RAP_SINGLETONS = new HashSet<Object>();

	private static final String RWT_PACKAGE = "org.eclipse.rwt"; //$NON-NLS-1$
	private static final String SESSION_SINGLETON_BASE = RWT_PACKAGE + ".SessionSingletonBase"; //$NON-NLS-1$
	private static final String GET_INSTANCE = "getInstance"; //$NON-NLS-1$
	private static final String RWT_BUNDLE = "org.eclipse.rap.rwt"; //$NON-NLS-1$

	static {
		IS_RAP = isRAP();
	}

	public SingletonProvider(final Class<S> singletonClass) {
		this.singletonClass = singletonClass;
	}

	/**
	 * Is RAP available and if if so set a few class fields as a side effect.
	 * 
	 * @return {@code true} if RAP available; otherwise false
	 */
	private static boolean isRAP() {
		final PackageAdmin packageAdmin = Service.get(PackageAdmin.class);
		Bundle rapBundle = null;
		if (packageAdmin != null) {
			final ExportedPackage rwtPackage = packageAdmin.getExportedPackage(RWT_PACKAGE);
			if (rwtPackage != null) {
				rapBundle = rwtPackage.getExportingBundle();
			}
		}
		if (rapBundle == null) {
			rapBundle = Platform.getBundle(RWT_BUNDLE);
		}
		if (rapBundle == null) {
			return false;
		}
		try {
			sessionSingletonBase = rapBundle.loadClass(SESSION_SINGLETON_BASE);
			getInstance = sessionSingletonBase.getMethod(GET_INSTANCE, Class.class);
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
	public S getInstance() {
		return IS_RAP ? getRAPInstance() : getRCPInstance();
	}

	@SuppressWarnings("unchecked")
	private S getRAPInstance() {
		try {
			final S rapSingleton = (S) getInstance.invoke(sessionSingletonBase, singletonClass);
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

	// Double-check idiom for lazy initialization of instance fields (Joshua Bloch, Effective Java, Item 71)
	private S getRCPInstance() {
		S result = rcpSingleton;
		if (result == null) { // First check (no locking)
			synchronized (this) {
				result = rcpSingleton;
				if (result == null) {
					rcpSingleton = result = newWiredInstance();
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

	public static class SingletonFailure extends Failure {

		private static final long serialVersionUID = 1L;

		public SingletonFailure(final String msg, final Throwable cause) {
			super(msg, cause);
		}

	}
}
