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
package org.eclipse.riena.tests;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Utility class for tests.
 */
public class TestUtils {

	private TestUtils() {
		// utility class
	}

	public static boolean isArabLocaleAvailable() {
		Locale arabLocale = new Locale("ar", "AE");
		for (Locale availableLocale : Locale.getAvailableLocales()) {
			if (availableLocale.equals(arabLocale)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Return a localized version of a String representation of a number. The
	 * returned string will use the grouping separator and decimal separator for
	 * the current locale.
	 * <p>
	 * Examples:
	 * <ul>
	 * <li>DE - "1.234,56" -&gt; "1.234,56"</li>
	 * <li>US - "1.234,56" -&gt; "1,234.56"</li>
	 * </ul>
	 * 
	 * @param number
	 *            a String representation of a number, where '.' is used as the
	 *            grouping separator and ',' is used as the decimal separator.
	 * @return a localized String representation of a number
	 */
	public static String getLocalizedNumber(String number) {
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		char decimalSep = dfs.getDecimalSeparator();
		char groupingSep = dfs.getGroupingSeparator();
		String result = number.replace('.', '_').replace(',', decimalSep).replace('_', groupingSep);
		return result;
	}
}
