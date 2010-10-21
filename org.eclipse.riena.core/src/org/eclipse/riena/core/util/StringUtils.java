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
package org.eclipse.riena.core.util;

import java.util.Iterator;

/**
 * Collection of String/CharSequence utilities.
 */
public final class StringUtils {

	private StringUtils() {
		// Utility class
	}

	/**
	 * Return the input string with the first character converted to upper case.
	 * 
	 * @param input
	 *            the input; may be null or empty
	 * @return the input with the first character converted to upper case, or
	 *         null (if input is null), or "" if input is ""
	 * 
	 */
	public static String capitalize(final String input) {
		String result = input;
		if (input != null && input.length() > 0) {
			result = input.substring(0, 1).toUpperCase();
			if (input.length() > 1) {
				result += input.substring(1);
			}
		}
		return result;
	}

	/**
	 * Returns the number of occurrences of ch in string.
	 * 
	 * @param string
	 *            the string to process; may be null
	 * @param ch
	 *            the character to look for
	 */
	public static int count(final String string, final char ch) {
		int result = 0;
		if (string != null) {
			for (int i = 0; i < string.length(); i++) {
				if (string.charAt(i) == ch) {
					result++;
				}
			}
		}
		return result;
	}

	/**
	 * Tests equality of the given strings.
	 * 
	 * @param sequence1
	 *            candidate 1, may be null
	 * @param sequence2
	 *            candidate 2, may be null
	 * @return
	 */
	public static boolean equals(final CharSequence sequence1, final CharSequence sequence2) {
		if (sequence1 == sequence2) {
			return true;
		}
		if (sequence1 == null || sequence2 == null) {
			return false;
		}
		return sequence1.equals(sequence2);
	}

	/**
	 * Tests ´deep´ emptiness. A string is empty if it is either null or its
	 * trimmed version has a zero length.
	 * 
	 * @param string
	 * @return
	 */
	public static boolean isDeepEmpty(final String string) {
		return string == null || string.trim().length() == 0;
	}

	/**
	 * Tests ´simple´ emptiness. A character sequence is empty either if it is
	 * null or it has a zero length.
	 * 
	 * @param sequence
	 * @return
	 */
	public static boolean isEmpty(final CharSequence sequence) {
		return sequence == null || sequence.length() == 0;
	}

	/**
	 * Tests whether the char sequence has content. A character sequence is
	 * given either if it is not null and it has a none zero length.
	 * 
	 * @param sequence
	 * @return
	 */
	public static boolean isGiven(final CharSequence sequence) {
		return sequence != null && sequence.length() != 0;
	}

	/**
	 * Joins the string representations of an {@link Iterable} collection of
	 * objects to one continues string while separating the entities with the
	 * provided separator.
	 * <p>
	 * The string representation will be retrieved by String.valueOf()
	 * 
	 * @param iterable
	 *            The {@link Iterable} number of objects
	 * @param separator
	 * @return The joined String separated by the given separator
	 */
	public static String join(final Iterable<? extends Object> iterable, final CharSequence separator) {
		Iterator<? extends Object> oIt;
		if (iterable == null || (!(oIt = iterable.iterator()).hasNext())) {
			return ""; //$NON-NLS-1$
		}
		final StringBuilder sb = new StringBuilder(String.valueOf(oIt.next()));
		while (oIt.hasNext()) {
			sb.append(separator).append(oIt.next());
		}
		return sb.toString();
	}
}
