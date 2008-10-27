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
package org.eclipse.riena.example.client.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * AkteEnum
 * 
 * 
 */
public class AkteEnum extends Enumeration {
	private int sortOrder = -1;
	private String altCode;

	/**
	 * 
	 * @param pCode
	 * @param pValue
	 */
	public AkteEnum(String pCode, String pValue) {
		super(pCode, pValue);
	}

	/**
	 * 
	 * @param pCode
	 * @param pValue
	 * @param alt
	 */
	public AkteEnum(String pCode, String pValue, String alt) {
		super(pCode, pValue, alt);
		setAltCode(alt);
	}

	/**
	 * 
	 * @see de.compeople.xxx.commons.base.enumeration.Enumeration#asSortedList(java.lang.Class)
	 */
	public static List asSortedList(Class pClass) {
		return Enumeration.asSortedList(pClass, new Comparator() {
			public int compare(Object o1, Object o2) {
				if (((AkteEnum) o1).getSortOrder() < ((AkteEnum) o2).getSortOrder()) {
					return -1;
				}
				if (((AkteEnum) o1).getSortOrder() > ((AkteEnum) o2).getSortOrder()) {
					return 1;
				}
				return 0;
			}

		});
	}

	/**
	 * 
	 * @return int
	 */
	public int getSortOrder() {
		return sortOrder;
	}

	/**
	 * 
	 * @param sortOrder
	 */
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	/**
	 * @return Returns the altCode.
	 */
	public String getAltCode() {
		return altCode;
	}

	/**
	 * @param altCode
	 *            The altCode to set.
	 */
	public void setAltCode(String altCode) {
		this.altCode = altCode;
	}

	/*
	 * Beispiel zur Initialisierung eines Enums
	 * 
	 * public static void init{ Iterator<List<String>> it =
	 * AkteEnum.loadEnumList("enumxy.csv").iterator(); int i=0;
	 * while(it.hasNext()){ Iterator<String> it2 = it.next().iterator(); String
	 * key = it2.next(); String value = it2.next(); new EnumXY(key, value, i);
	 * i++; } }
	 */

	/**
	 * 
	 * @param fileName
	 * @return List<List<String>>
	 */
	public static List<List<String>> loadEnumList(String fileName) {
		List<List<String>> result = new ArrayList<List<String>>();
		String baseName = "de/compeople/xxx/ki/core/base/model/";
		InputStream input = null;
		BufferedReader reader = null;
		try {
			input = Thread.currentThread().getContextClassLoader().getResourceAsStream(baseName + fileName);
			reader = new BufferedReader(new InputStreamReader(input));
			String currentLine = reader.readLine();

			while (currentLine != null) {
				List<String> lineResult = new ArrayList<String>();
				StringBuffer b = new StringBuffer();
				int pointer = 0;
				boolean isInLiteral = false;
				while (pointer < currentLine.length()) {
					char currentChar = currentLine.charAt(pointer);
					if (isInLiteral) {
						if (currentChar == '"') {
							isInLiteral = false;
						} else {
							b.append(currentChar);
						}
					} else {
						if (currentChar == ',') {
							lineResult.add(b.toString().trim());
							b.setLength(0);
						} else {
							if (currentChar == '"') {
								isInLiteral = true;
							} else {
								b.append(currentChar);
							}
						}
					}
					pointer++;
				}

				lineResult.add(b.toString().trim());

				result.add(lineResult);
				currentLine = reader.readLine();
			}
		} catch (IOException ex) {
			//
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ex) {
					//
				}
			}
		}
		return result;
	}
}
