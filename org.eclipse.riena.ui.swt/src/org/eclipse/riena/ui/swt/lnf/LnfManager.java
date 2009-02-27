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

import org.eclipse.core.runtime.Platform;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;
import org.eclipse.riena.ui.swt.utils.BundleUtil;
import org.osgi.framework.Bundle;

/**
 * <code>LnfManager</code> manages the current look and feel of the riena
 * (navigation) widgets.
 */
public final class LnfManager {

	private final static String DEFAULT_LNF_CLASSNAME = RienaDefaultLnf.class.getName();
	private static String lnfClassName;

	private static RienaDefaultLnf lnf;

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
			try {
				RienaDefaultLnf myLnf = createLnf(className);
				setLnf(myLnf);
			} catch (Exception e) {
				e.printStackTrace();
				throw new Error("can't load " + className); //$NON-NLS-1$
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
			className = DEFAULT_LNF_CLASSNAME;
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
