/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.example.client.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ModelTools
 * 
 * 
 */
public final class ModelTools {
	/**
	 *  
	 */
	public static final String DATUMSFORMAT = "dd.MM.yyyy";

	private ModelTools() {
		// 	
	}

	/**
	 * 
	 * @param date
	 * @return String
	 */
	public static String formatDateToString(Date date) {
		String result = null;
		result = (new SimpleDateFormat(DATUMSFORMAT)).format(date);
		return result;
	}

	/**
	 * 
	 * @param dateString
	 * @return String
	 */
	public static Date getDateFromString(String dateString) {
		Date result = null;
		try {
			result = (new SimpleDateFormat(DATUMSFORMAT)).parse(dateString);
		} catch (ParseException ex) {
			// do nothing
		}
		return result;
	}
}
