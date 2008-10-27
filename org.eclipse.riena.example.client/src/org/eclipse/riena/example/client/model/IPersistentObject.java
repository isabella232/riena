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

/**
 * Gemeinsames Interface für die Persistenten Objekte
 * 
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