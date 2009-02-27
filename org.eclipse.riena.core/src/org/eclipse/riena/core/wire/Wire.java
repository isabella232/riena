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
	 * Wire the given {@code bean} instance.
	 * 
	 * @param test
	 * @return
	 */
	public static WirePuller instance(Object bean) {
		return new WirePuller(bean);
	}

}
