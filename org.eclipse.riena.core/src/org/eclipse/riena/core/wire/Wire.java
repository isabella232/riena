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
package org.eclipse.riena.core.wire;

/**
 * The {@code Wire} class starts a sentence (fluid interface) for a simple
 * approach of wiring components, i.e. injecting services and extensions.<br>
 * The main goal of using {@code Wire} is to remove the usage of {@code Inject}
 * from within the classes that need wiring (injecting).
 */
public final class Wire {

	private Wire() {
		// Utility class
	}

	/**
	 * Wire the given {@code beanClass} class. The effect of wiring a class is,
	 * that an instance of this class will be created upon the wiring will be
	 * done.
	 * 
	 * @param beanClass
	 * @return
	 */
	public static WirePuller type(Class<?> beanClass) {
		return new WirePuller(beanClass);
	}

	/**
	 * Wire the given {@code beanClassNam} class given as a string. The effect
	 * of wiring a class is, that an instance of this class will be created upon
	 * the wiring will be done.
	 * 
	 * @param beanClassName
	 * @return
	 */
	public static WirePuller type(String beanClassName) {
		return new WirePuller(beanClassName);
	}

	/**
	 * Wire the given {@code bean} instance.
	 * 
	 * @param test
	 * @return
	 */
	public static WirePuller instance(Object bean) {
		return new WirePuller(bean);
	}

}
