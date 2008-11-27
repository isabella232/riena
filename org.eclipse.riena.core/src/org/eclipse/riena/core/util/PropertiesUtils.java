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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.Assert;

/**
 * PropertiesUtils class
 * 
 */
public final class PropertiesUtils {

	private PropertiesUtils() {
		// Utility
	}

	/**
	 * Answers the String value for the given propValue object. If the propValue
	 * not compatible to String or String[] answers the given
	 * <code>returnIfNoString</code> as default. If propValue is an instance of
	 * String[] then answers the item with index=0.
	 * 
	 * @param propValue
	 * @param returnIfNoString
	 * @return the String value for propValue or the default value
	 *         <code>returnIfNoString</code>
	 */
	public static String accessProperty(Object propValue, String returnIfNoString) {
		if (propValue instanceof String) { // if api programmed we receive
			return (String) propValue; // a String
		} else if (propValue instanceof String[]) { // for DS we receive a
			return ((String[]) propValue)[0]; // String array
		}

		return returnIfNoString;
	}

	/**
	 * Transform the string representation of a map into a map and optionally
	 * check the existence of the specified expected keys.
	 * 
	 * <pre>
	 * The format of the string is: 
	 * string := [ pair ] | pair { , pair }
	 * pair := key = value
	 * </pre>
	 * 
	 * @param stringified
	 * @param expectedKeys
	 * @return
	 */
	public static Map<String, String> asMap(String stringified, String... expectedKeys) {
		if (StringUtils.isEmpty(stringified)) {
			Assert.isLegal(expectedKeys.length == 0,
					"Excpeted keys " + Arrays.toString(expectedKeys) + " not found in empty string."); //$NON-NLS-1$ //$NON-NLS-2$
			return Collections.emptyMap();
		}
		Map<String, String> result = new HashMap<String, String>();
		StringTokenizer parts = new StringTokenizer(stringified, ","); //$NON-NLS-1$
		while (parts.hasMoreTokens()) {
			String part = parts.nextToken();
			int equal = part.indexOf('=');
			Assert.isLegal(equal > 0, "Error within definition. Expecting a string of the form: " //$NON-NLS-1$
					+ " [ <key> \"=\" <value> ] { [ \",\" <key> \"=\" <value> ] }"); //$NON-NLS-1$
			result.put(part.substring(0, equal).trim(), part.substring(equal + 1).trim());
		}

		for (String expectedKey : expectedKeys) {
			Assert.isLegal(result.containsKey(expectedKey), "Map " + stringified + "does not contain expected key " //$NON-NLS-1$ //$NON-NLS-2$
					+ expectedKey + "."); //$NON-NLS-1$
		}
		return Collections.unmodifiableMap(result);
	}

	private static final String[] EMPTY_STRING_ARRAY = new String[0];

	/**
	 * Transform the string representation of a list into a array.
	 * 
	 * <pre>
	 * The format of the string is: 
	 *  string = [ value ] | value { , value }
	 * </pre>
	 * 
	 * @param stringified
	 * @return
	 */
	public static String[] asArray(String stringified) {
		if (StringUtils.isEmpty(stringified)) {
			return EMPTY_STRING_ARRAY;
		}
		List<String> result = new ArrayList<String>();
		int comma;
		int fromIndex = 0;
		while ((comma = stringified.indexOf(',', fromIndex)) != -1) {
			result.add(stringified.substring(fromIndex, comma));
			fromIndex = comma + 1;
		}
		result.add(stringified.substring(fromIndex));
		return result.toArray(new String[result.size()]);
	}
}
