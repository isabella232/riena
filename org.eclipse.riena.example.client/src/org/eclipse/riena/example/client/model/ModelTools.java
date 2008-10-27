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
