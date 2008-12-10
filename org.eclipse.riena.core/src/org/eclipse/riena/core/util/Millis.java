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
package org.eclipse.riena.core.util;

/**
 * {@code Millis} is a little helper class that provides methods to convert from
 * seconds, minutes, hours and days to milli seconds.
 */
public final class Millis {

	private Millis() {
		// utility class
	}

	/**
	 * Return the given seconds in milli seconds.
	 * 
	 * @param seconds
	 * @return
	 */
	public static long seconds(final int seconds) {
		return 1000L * seconds;
	}

	/**
	 * Return the given minutes in milli seconds.
	 * 
	 * @param minutes
	 * @return
	 */
	public static long minutes(final int minutes) {
		return seconds(60 * minutes);
	}

	/**
	 * Return the given hours in milli seconds.
	 * 
	 * @param hours
	 * @return
	 */
	public static long hours(final int hours) {
		return minutes(60 * hours);
	}

	/**
	 * Return the given days in milli seconds.
	 * 
	 * @param days
	 * @return
	 */
	public static long days(final int days) {
		return hours(24 * days);
	}
}
