/****************************************************************
 *                                                              *
 * Copyright (c) 2005 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/

package org.eclipse.riena.ui.core.resource.internal;

/**
 * Constants for icon sizes.
 *
 * @author Michaela Behling
 * @author Carsten Drossel
 */
public final class IconSize {

	/** <code>Size a (16x16 at standard scaling 00) */
	public static final IconSize A = new IconSize( 'a' );

	/** <code>Size b (22x22 at standard scaling 00) */
	public static final IconSize B = new IconSize( 'b' );

	/** <code>Size c (32x32 at standard scaling 00) */
	public static final IconSize C = new IconSize( 'c' );

	/** <code>Size d (48x48 at standard scaling 00) */
	public static final IconSize D = new IconSize( 'd' );

	/** <code>Size e (64x64 at standard scaling 00) */
	public static final IconSize E = new IconSize( 'e' );

	/** <code>Size f (128x128 at standard scaling 00) */
	public static final IconSize F = new IconSize( 'f' );

	/** <code>Size u (14x14 at standard scaling 00) */
	public static final IconSize U = new IconSize( 'u' );

	/** <code>Size v (22x27 at standard scaling 00) */
	public static final IconSize V = new IconSize( 'v' );

	/** <code>Size w (18x18 at standard scaling 00) */
	public static final IconSize W = new IconSize( 'w' );

	/** <code>Size x (13x13 at standard scaling 00) */
	public static final IconSize X = new IconSize( 'x' );

	/** <code>Size y (12x12 at standard scaling 00) */
	public static final IconSize Y = new IconSize( 'y' );

	/** <code>Size z (9x9 at standard scaling 00) */
	public static final IconSize Z = new IconSize( 'z' );

	/** <code>Size _ (irregular size) */
	public static final IconSize SPECIAL = new IconSize( '_' );

	private char defaultMapping;

	private IconSize( char defaultMapping ) {
		this.defaultMapping = defaultMapping;
	}

	/**
	 * @return The filename character the icon size is mapped to.
	 */
	public char getDefaultMapping() {
		return defaultMapping;
	}
}
