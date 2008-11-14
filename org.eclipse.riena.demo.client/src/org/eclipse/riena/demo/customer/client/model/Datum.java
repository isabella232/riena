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
package org.eclipse.riena.demo.customer.client.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Datum
 * 
 * 
 */
public class Datum implements Serializable, Comparable<Datum> {
	private String datumAsString;
	private Boolean isValid = Boolean.FALSE;
	private Integer gespeicherterTag;
	private Integer gespeicherterMonat;
	private Integer gespeichertesJahr;
	private static SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

	/**
	 *
	 */
	public Datum() {
		super();
	}

	/**
	 * 
	 * @param tag
	 * @param monat
	 * @param jahr
	 */
	public Datum(int tag, int monat, int jahr) {
		setCalendar(tag, monat, jahr);
	}

	/**
	 * 
	 * @param date
	 */
	public Datum(GregorianCalendar date) {
		setCalendar(date.get(GregorianCalendar.DAY_OF_MONTH), date.get(GregorianCalendar.MONTH) + 1, date
				.get(GregorianCalendar.YEAR));
	}

	/**
	 * 
	 * @param datumAsString
	 */
	public Datum(String datumAsString) {
		setDatum(datumAsString);
	}

	/**
	 * @param date
	 */
	public Datum(Date date) {
		SimpleDateFormat currentFormat = new SimpleDateFormat("dd.MM.yyyy");
		setDatum(currentFormat.format(date));
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Datum) {
			String thisString = this.toString();
			String otherString = ((Datum) o).toString();
			if (thisString == null && otherString == null) {
				return true;
			}
			if (thisString != null && otherString == null) {
				return false;
			}
			if (thisString == null && otherString != null) {
				return false;
			}
			if (thisString.equals(otherString)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param d
	 * @return int
	 */
	public int compareTo(Datum d) {
		return toDate().compareTo(d.toDate());
	}

	/**
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		String aString = toString();
		if (aString == null) {
			aString = "";
		}
		return aString.hashCode();
	}

	private void setCalendar(int tag, int monat, int jahr) {
		if (tag <= 0 || tag > 31 || monat <= 0 || monat > 12 || jahr < 0) {
			isValid = false;
			throw new RuntimeException("Invalid Date");
		}
		GregorianCalendar tempCalendar = new GregorianCalendar();
		tempCalendar.clear();
		tempCalendar.set(GregorianCalendar.YEAR, jahr);
		tempCalendar.set(GregorianCalendar.MONTH, monat - 1);
		tempCalendar.set(GregorianCalendar.DAY_OF_MONTH, 1);
		if (tag > tempCalendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH)) {
			isValid = false;
			throw new RuntimeException("Invalid Date");
		}
		tempCalendar.set(GregorianCalendar.DAY_OF_MONTH, tag);

		isValid = true;
		gespeicherterTag = new Integer(tag);
		gespeicherterMonat = new Integer(monat);
		gespeichertesJahr = new Integer(jahr);
		datumAsString = ModelTools.formatDateToString(toDate());

	}

	/**
	 * @param newDatumAsString
	 */
	public void setDatum(String newDatumAsString) {
		if (newDatumAsString != null) {
			String tempTrimmed = newDatumAsString.trim();
			if (tempTrimmed.length() < 8) {

				if (tempTrimmed.length() == 4 && tempTrimmed.indexOf('.') == -1) {
					// 2007
					try {
						int tempJahr = Integer.parseInt(tempTrimmed);
						setCalendar(1, 1, tempJahr);
						isValid = true;
					} catch (NumberFormatException ex) {
						isValid = false;
					}
				} else {
					if (tempTrimmed.length() == 7 && tempTrimmed.indexOf('.') == 2) {
						// 01.2007
						try {
							int firstDot = tempTrimmed.indexOf('.');
							int tempMonat = Integer.parseInt(tempTrimmed.substring(0, firstDot));
							int tempJahr = Integer.parseInt(tempTrimmed.substring(firstDot + 1, tempTrimmed.length()));
							setCalendar(1, tempMonat, tempJahr);
							isValid = true;
						} catch (NumberFormatException ex) {
							isValid = false;
						}
					} else {
						isValid = false;
					}
				}
			} else {
				Date tempDate = ModelTools.getDateFromString(tempTrimmed);
				if (tempDate == null) {
					isValid = false;
				} else {
					try {
						int tempTag = -1;
						int tempMonat = -1;
						int tempJahr = -1;
						try {
							int firstDot = tempTrimmed.indexOf('.');
							int secondDot = tempTrimmed.indexOf('.', firstDot + 1);
							tempTag = Integer.parseInt(tempTrimmed.substring(0, firstDot));
							tempMonat = Integer.parseInt(tempTrimmed.substring(firstDot + 1, secondDot));
							tempJahr = Integer.parseInt(tempTrimmed.substring(secondDot + 1, tempTrimmed.length()));
							setCalendar(tempTag, tempMonat, tempJahr);
							isValid = true;
						} catch (NumberFormatException ex) {
							isValid = false;
						}

					} catch (RuntimeException ex) {
						isValid = false;
					}
				}
			}
		}
		if (!isValid) {
			this.datumAsString = newDatumAsString;
		}
	}

	/**
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		return datumAsString;
	}

	/**
	 * 
	 * @return boolean
	 */
	public boolean isValid() {
		return isValid;
	}

	/**
	 * 
	 * @return Date
	 */
	public Date toDate() {
		if (isValid) {
			Date result = null;
			try {
				SimpleDateFormat currentFormat = new SimpleDateFormat("dd.MM.yyyy");
				StringBuffer temp = new StringBuffer();
				temp.append(gespeicherterTag);
				temp.append(".");
				temp.append(gespeicherterMonat);
				temp.append(".");
				temp.append(gespeichertesJahr);
				result = currentFormat.parse(temp.toString());
			} catch (ParseException ex) {
				// nothing to do
			}
			return result;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @return Date
	 */
	public java.sql.Date toSQLDate() {
		if (isValid) {
			return new java.sql.Date(toDate().getTime());
		} else {
			return null;
		}
	}

}
