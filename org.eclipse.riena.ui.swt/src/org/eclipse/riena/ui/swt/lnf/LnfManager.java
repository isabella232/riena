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
package org.eclipse.riena.ui.swt.lnf;

import org.osgi.framework.Bundle;

import org.eclipse.core.runtime.Platform;

import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;
import org.eclipse.riena.ui.swt.utils.BundleUtil;

/**
 * <code>LnfManager</code> manages the current look and feel of the riena
 * (navigation) widgets.
 */
public final class LnfManager {

	private static RienaDefaultLnf defaultLnfClass = new RienaDefaultLnf();
	private static String lnfClassName;

	private static RienaDefaultLnf lnf;

	/**
	 * @since 1.2
	 */
	public static void setDefaultLnf(RienaDefaultLnf parmDefaultLnfClazz) {
		defaultLnfClass = parmDefaultLnfClazz;
	}

	private LnfManager() {
		// cannot instantiated, because all method are static
	}

	/**
	 * Returns the current look and feel. If no look and feel is set, the
	 * default look and feel is returned.
	 * 
	 * @return current look and feel
	 */
	public static RienaDefaultLnf getLnf() {

		if (lnf == null) {
			String className = getLnfClassName();
			if (className != null) {
				try {
					RienaDefaultLnf myLnf = createLnf(className);
					if (myLnf == null) {
						throw new Error("can't load LnfClass '" + className + "' from the list of installed bundles."); //$NON-NLS-1$ //$NON-NLS-2$
					}
					setLnf(myLnf);
				} catch (Exception e) {
					e.printStackTrace();
					throw new Error(
							"can't load " + className + " If you use -Driena.lnf, try prefixing the classname with the bundleId i.e. org.eclipse.riena.demo.client:org.eclipse.riena.demo.client.lnf.EclipseLnf"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			} else {
				setLnf(defaultLnfClass);
			}
		}
		lnf.initialize();

		return lnf;

	}

	/**
	 * Loads the look and feel specified by the given class name and passes it
	 * to <code>setLnf(RienaDefaultLnf)</code>.
	 * 
	 * @param lnfClassName
	 *            - a string specifying the name of the class that implements
	 *            the look and feel
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static void setLnf(String newLnfClassName) {
		dispose();
		lnfClassName = newLnfClassName;
	}

	private static RienaDefaultLnf createLnf(String lnfClassName) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {

		Class<?> lnfClass = null;
		if (lnfClassName.contains(":")) { //$NON-NLS-1$
			String[] parts = lnfClassName.split(":"); //$NON-NLS-1$
			String pluginID = parts[0];
			String classPath = parts[1];
			Bundle bundle = Platform.getBundle(pluginID);
			if (!BundleUtil.isReady(bundle)) {
				return null;
			}
			lnfClass = bundle.loadClass(classPath);

		} else {

			ClassLoader classLoader = LnfManager.class.getClassLoader();
			lnfClass = classLoader.loadClass(lnfClassName);
		}
		return (RienaDefaultLnf) lnfClass.newInstance();

	}

	/**
	 * Sets the current look and feel. New look and feel will be initialized and
	 * the old one will be uninitialized.
	 * 
	 * @param newLnf
	 *            - new look and feel to install.
	 */
	public static void setLnf(RienaDefaultLnf newLnf) {
		lnf = newLnf;
	}

	/**
	 * @return the lnfClassName
	 */
	public static String getLnfClassName() {
		String className = lnfClassName;
		if (className == null) {
			className = System.getProperty("riena.lnf"); //$NON-NLS-1$
		}
		return className;
	}

	/**
	 * Disposes (uninitializes) the current look and feel.
	 */
	public static void dispose() {
		if (lnf != null) {
			lnf.uninitialize();
			lnf = null;
		}
	}

}
