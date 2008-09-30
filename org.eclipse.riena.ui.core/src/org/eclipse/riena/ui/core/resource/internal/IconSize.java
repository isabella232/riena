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
package org.eclipse.riena.ui.core.resource.internal;

/**
 * Constants for icon sizes.
 */
public final class IconSize {

	/** <code>Size a (16x16 at standard scaling 00) */
	public static final IconSize A = new IconSize('a');

	/** <code>Size b (22x22 at standard scaling 00) */
	public static final IconSize B = new IconSize('b');

	/** <code>Size c (32x32 at standard scaling 00) */
	public static final IconSize C = new IconSize('c');

	/** <code>Size d (48x48 at standard scaling 00) */
	public static final IconSize D = new IconSize('d');

	/** <code>Size e (64x64 at standard scaling 00) */
	public static final IconSize E = new IconSize('e');

	/** <code>Size f (128x128 at standard scaling 00) */
	public static final IconSize F = new IconSize('f');

	/** <code>Size s (15x15 at standard scaling 00) */
	public static final IconSize S = new IconSize('s');

	/** <code>Size u (14x14 at standard scaling 00) */
	public static final IconSize U = new IconSize('u');

	/** <code>Size v (22x27 at standard scaling 00) */
	public static final IconSize V = new IconSize('v');

	/** <code>Size w (18x18 at standard scaling 00) */
	public static final IconSize W = new IconSize('w');

	/** <code>Size x (13x13 at standard scaling 00) */
	public static final IconSize X = new IconSize('x');

	/** <code>Size y (12x12 at standard scaling 00) */
	public static final IconSize Y = new IconSize('y');

	/** <code>Size z (9x9 at standard scaling 00) */
	public static final IconSize Z = new IconSize('z');

	/** <code>Size _ (irregular size) */
	public static final IconSize SPECIAL = new IconSize('_');

	private char defaultMapping;

	private IconSize(char defaultMapping) {
		this.defaultMapping = defaultMapping;
	}

	/**
	 * @return The filename character the icon size is mapped to.
	 */
	public char getDefaultMapping() {
		return defaultMapping;
	}
}
