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
package org.eclipse.riena.navigation.ui.swt.lnf;

import org.eclipse.riena.navigation.ui.swt.lnf.rienadefault.RienaDefaultLnf;

/**
 * <code>LnfManager</code> manages the current look and feel of the riena
 * (navigation) widgets.
 */
public class LnfManager {

	private final static String DEFAULT_LNF_CLASSNAME = RienaDefaultLnf.class.getName();

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
			try {
				setLnf(DEFAULT_LNF_CLASSNAME);
			} catch (Exception e) {
				e.printStackTrace();
				throw new Error("can't load " + DEFAULT_LNF_CLASSNAME); //$NON-NLS-1$
			}
		}

		return lnf;

	}

	/**
	 * Loads the look and feel specified by the given class name and passes it
	 * to <code>setLnf(RienaDefaultLnf)<code>.
	 * 
	 * @param lnfClassName - a string specifying the name of the class that implements
	 *        the look and feel
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static void setLnf(String lnfClassName) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		setLnf(createLnf(lnfClassName));
	}

	private static RienaDefaultLnf createLnf(String lnfClassName) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		ClassLoader classLoader = LnfManager.class.getClassLoader();
		Class<?> lnfClass = classLoader.loadClass(lnfClassName);
		return (RienaDefaultLnf) lnfClass.newInstance();
	}

	/**
	 * Sets the current look and feel. New look and feel will be initialized and
	 * the old one will be uninitialized.
	 * 
	 * @param newLnf -
	 *            new look and feel to install.
	 */
	public static void setLnf(RienaDefaultLnf newLnf) {

		RienaDefaultLnf oldLnf = lnf;

		lnf = newLnf;

		if (newLnf != null) {
			newLnf.initialize();
		}
		if (oldLnf != null) {
			oldLnf.uninitialize();
		}

	}

	/**
	 * Disposes (uninitializes) the current look and feel.
	 */
	public static void dispose() {
		setLnf((RienaDefaultLnf) null);
	}

}
