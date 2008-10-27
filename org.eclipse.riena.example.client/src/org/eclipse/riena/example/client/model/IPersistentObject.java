/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.example.client.model;

/**
 * Gemeinsames Interface für die Persistenten Objekte
 * 
 * @author SST
 */
public interface IPersistentObject {
	/**
	 * Gibt das Persistente Oid
	 * 
	 * @return
	 */
	IPersistentOid getPoid();

	/**
	 * Gibt das Persistente Oid
	 * 
	 * @param pPoid
	 */
	void setPoid(IPersistentOid pPoid);

	/**
	 * @return Returns the version.
	 */
	String getVersion();

	/**
	 * Setzt die Version des Objektes
	 * 
	 * @param pVersion
	 *            version zum Setzen
	 */
	void setVersion(String pVersion);

	/**
	 * Gibt die Klasse der Implementierung des Objektes, dieses muss
	 * reimplementiert werden, um das IOID der Transaction, die equals und
	 * HashCode der pojos zu bekommen, die mit Hibernate Proxys belegt werden.
	 * 
	 * @return
	 */
	Class getImplementationClass();

}