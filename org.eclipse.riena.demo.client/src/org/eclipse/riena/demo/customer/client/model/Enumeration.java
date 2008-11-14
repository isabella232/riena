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

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Basisklasse fuer alle Enumerationen.
 * 
 */
public class Enumeration implements Serializable, Cloneable, Comparable {
	/** Serialization ID */
	private static final long serialVersionUID = -4069970285757866138L;
	/** enthaelt wiederum HashMaps fuer jede Enumeration */
	private static transient Map enums = Collections.synchronizedMap(new HashMap());
	/**
	 * enthaelt wiederum HashMaps fuer jede Enumeration zum Zugriff ueber
	 * alternativen Code
	 */
	private static transient Map enumsAlt = Collections.synchronizedMap(new HashMap());

	/** Eindeutiger Code des Enums */
	private String code;
	/** Wert des Enums */
	private transient final String value;

	/**
	 * default constructor. do not use this. only for hibernate.
	 */
	protected Enumeration() {
		value = null;
	}

	/**
	 * Full constructor. Darf nur von abgeleiteten Klassen benutzt werden.
	 * 
	 * @param pCode
	 *            Code des Enums
	 * @param pValue
	 *            Wert des Enums
	 * @throws IllegalArgumentException
	 *             falls es schon ein Enum mit diesem Code gibt
	 */
	protected Enumeration(String pCode, String pValue) {
		code = pCode;
		value = pValue;

		// Hessian ruft den Konstruktor mit null-Parametern auf, null nehmen
		// wir nicht in die Map!
		if (pCode == null) {
			return;
		}

		Map lEnumClass = (Map) enums.get(getClass().getName());
		if (lEnumClass == null) {
			lEnumClass = new HashMap();
			enums.put(getClass().getName(), lEnumClass);
		}

		Object lOldObject = lEnumClass.put(code, this);
		if (lOldObject != null) {
			throw new IllegalArgumentException("Es existiert bereits ein Enum mit dem Code " + code
					+ " fuer die Klasse " + getClass().getName());
		}
	}

	/**
	 * Full constructor mit alternativem Code. Darf nur von abgeleiteten Klassen
	 * benutzt werden. Der alternative Code wird nicht in einem Attribut
	 * gehalten. Das ist Aufgabe der abgeleiteten Klasse.
	 * 
	 * @param pCode
	 *            Code des Enums
	 * @param pValue
	 *            Wert des Enums
	 * @param pAltCode
	 *            Alternativer Code des Enums
	 * @throws IllegalArgumentException
	 *             falls es schon ein Enum mit diesem alternativen Code gibt
	 */
	protected Enumeration(String pCode, String pValue, String pAltCode) {
		this(pCode, pValue);

		// Hessian ruft den Konstruktor mit null-Parametern auf, null nehmen
		// wir nicht in die Map!
		if (pCode == null || pAltCode == null) {
			return;
		}

		Map lEnumClass = (Map) enumsAlt.get(getClass().getName());
		if (lEnumClass == null) {
			lEnumClass = new HashMap();
			enumsAlt.put(getClass().getName(), lEnumClass);
		}

		Object lOldObject = lEnumClass.put(pAltCode, this);
		if (lOldObject != null) {
			throw new IllegalArgumentException("Es existiert bereits ein Enum mit dem alternativen Code " + pAltCode
					+ " fuer die Klasse " + getClass().getName());
		}
	}

	/**
	 * @return Returns the code.
	 */
	public final String getCode() {
		return code;
	}

	/**
	 * @return Returns the value.
	 */
	public final String getValue() {
		return value;
	}

	/**
	 * Von jedem Enum gibt es nur eine Instanz, deshalb kann die Referenz
	 * verglichen werden (eigentlich Default).
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public final boolean equals(Object pObj) {
		return this == pObj;
	}

	/**
	 * Der Hashcode des Enums entspricht dem Hashcode des Enumcodes.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public final int hashCode() {
		return toString().hashCode();
	}

	/**
	 * Liefert den Code des Enums als String. Diese Methode wird bei der
	 * Serialisierung ueber Axis benutzt, deshalb ist es wichtig, dass der
	 * eindeutige Code geliefert wird.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public final String toString() {
		return String.valueOf(code);
	}

	/**
	 * Um die Semantik des Enums zu bewahren, wird dasselbe Enum Objekt zurueck
	 * geliefert.
	 * 
	 * @return die urspruengliche Instanz
	 */
	public final Object clone() {
		return this;
	}

	/**
	 * Vergleich nach Code des Enums.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object pObj) {
		return code.compareTo(((Enumeration) pObj).code);
	}

	/**
	 * Diese Methode wird waehrend der Deserialisierung aufgerufen. Ersetzt das
	 * deserialisierte Objekt durch die definierten Enum-Instanzen.
	 * 
	 * @return die dem Code entsprechende definierte Instanz dieser Enum
	 * @throws ObjectStreamException
	 *             wenn keine Enum zu dem Objekt aus der Deserialisierung
	 *             gefunden wurde
	 */
	public final Object readResolve() throws ObjectStreamException {
		return fromString(getClass(), code);
	}

	/**
	 * Liefert HashMap mit den Enum-Objekten einer Klasse.
	 * 
	 * @param pClass
	 *            Typ des Enums
	 * @return HashMap mit Enum-Objekten
	 * @throws IllegalArgumentException
	 *             falls es keine Enums dieses Typs gibt
	 */
	private static Map getEnumClass(Class pClass) {
		Map lEnumClass = (Map) enums.get(pClass.getName());

		if (lEnumClass == null) {
			try {
				// Workaround Java 1.5: versuchen, Klasse explizit zu laden
				Class.forName(pClass.getName());
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException("Es existiert kein Enum der Klasse " + pClass.getName()
						+ ". Klasse kann nicht geladen werden.");
			}

			lEnumClass = (Map) enums.get(pClass.getName());

			if (lEnumClass == null) {
				throw new IllegalArgumentException("Es existiert kein Enum der Klasse " + pClass.getName());
			}
		}

		return lEnumClass;
	}

	/**
	 * Liefert Enum mit dem angegebenen Code.
	 * 
	 * @param pClass
	 *            Typ des Enums
	 * @param pCode
	 *            Code des Enums als String
	 * @return Enum
	 * @throws IllegalArgumentException
	 *             falls es kein Enum mit diesem Code gibt
	 */
	public static Object fromString(Class pClass, String pCode) {
		Map lEnumClass = getEnumClass(pClass);
		Enumeration lEnum = (Enumeration) lEnumClass.get(pCode);

		if (lEnum == null) {
			throw new IllegalArgumentException("Zu dem Code " + pCode + " gibt es kein Enum der Klasse "
					+ pClass.getName());
		}

		return lEnum;
	}

	/**
	 * Liefert Enum mit dem angegebenen alternativen Code.
	 * 
	 * @param pClass
	 *            Typ des Enums
	 * @param pAltCode
	 *            Alternativer Code des Enums
	 * @return Enum
	 * @throws IllegalArgumentException
	 *             falls es kein Enum mit diesem alternativen Code gibt
	 */
	public static Object fromAltCode(Class pClass, String pAltCode) {
		Map lEnumClass = (Map) enumsAlt.get(pClass.getName());

		if (lEnumClass == null) {
			throw new IllegalArgumentException("Es existiert kein Enum der Klasse " + pClass.getName());
		}

		Enumeration lEnum = (Enumeration) lEnumClass.get(pAltCode);

		if (lEnum == null) {
			throw new IllegalArgumentException("Zu dem alternativen Code " + pAltCode
					+ " gibt es kein Enum der Klasse " + pClass.getName());
		}

		return lEnum;
	}

	/**
	 * Liefert eine Liste der Enums einer Klasse.
	 * 
	 * @param pClass
	 *            Typ des Enums
	 * @return Liste der Enums
	 */
	public static List asList(Class pClass) {
		Map lEnumClass = getEnumClass(pClass);
		List lResult = new ArrayList(lEnumClass.size());

		lResult.addAll(lEnumClass.values());

		return Collections.unmodifiableList(lResult);
	}

	/**
	 * Liefert eine nach Code sortierte Liste der Enums einer Klasse.
	 * 
	 * @param pClass
	 *            Typ des Enums
	 * @return Liste der Enums
	 */
	public static List asSortedList(Class pClass) {
		Map lEnumClass = getEnumClass(pClass);
		List lResult = new ArrayList(lEnumClass.size());

		lResult.addAll(lEnumClass.values());
		Collections.sort(lResult);

		return Collections.unmodifiableList(lResult);
	}

	/**
	 * Liefert eine sortierte Liste der Enums einer Klasse.
	 * 
	 * @param pClass
	 *            Typ des Enums
	 * @param pComparator
	 *            dieser Comparator wird zur Sortierung verwendet
	 * @return Liste der Enums
	 */
	public static List asSortedList(Class pClass, Comparator pComparator) {
		Map lEnumClass = getEnumClass(pClass);
		List lResult = new ArrayList(lEnumClass.size());

		lResult.addAll(lEnumClass.values());
		Collections.sort(lResult, pComparator);

		return Collections.unmodifiableList(lResult);
	}

	/**
	 * Liefert eine Collection mit den Enums einer Klasse.
	 * 
	 * @param pClass
	 *            Typ des Enums
	 * @return Collection mit den Enums
	 */
	public static Collection asCollection(Class pClass) {
		Map lEnumClass = getEnumClass(pClass);

		return Collections.unmodifiableCollection(lEnumClass.values());
	}

	/**
	 * Liefert eine Map mit den Enums einer Klasse. Die Keys der Map sind die
	 * Codes der Enums.
	 * 
	 * @param pClass
	 *            Typ des Enums
	 * @return Map mit den Enums
	 */
	public static Map asMap(Class pClass) {
		Map lEnumClass = getEnumClass(pClass);

		return Collections.unmodifiableMap(lEnumClass);
	}

}