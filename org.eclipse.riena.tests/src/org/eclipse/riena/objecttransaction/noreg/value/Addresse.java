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
package org.eclipse.riena.objecttransaction.noreg.value;

import java.security.SecureRandom;

import org.eclipse.riena.objecttransaction.AbstractTransactedObject;
import org.eclipse.riena.objecttransaction.ITransactedObject;
import org.eclipse.riena.objecttransaction.ObjectTransactionManagerAccessor;

/**
 * Sample class for "UTAddresse"
 * 
 * @author Christian Campo
 */
public class Addresse extends AbstractTransactedObject implements ITransactedObject {

	private String plz;
	private String ort;
	private String strasse;
	private static SecureRandom random = new SecureRandom();

	@SuppressWarnings("unused")
	private Addresse() {
		super();
	}

	/**
	 *  
	 */
	/**
	 * @param dummy
	 */
	public Addresse( boolean dummy ) {
		super( new GenericOID( "addresse", "primkey", Integer.valueOf( nextRandomInt() ) ), "1" );
	}

	/**
	 * @param primKey
	 */
	public Addresse( Integer primKey ) {
		super( new GenericOID( "addresse", "primkey", primKey ), "1" );
	}

	/**
	 * @return Returns the ort.
	 */
	public String getOrt() {
		return (String) ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().getReference( this, "ort", ort );
	}

	/**
	 * @param ort The ort to set.
	 */
	public void setOrt( String ort ) {
		// changeEvent
		if ( ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().isCleanModus() ) {
			this.ort = ort;
		}
		ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().setReference( this, "ort", ort );
	}

	/**
	 * @return Returns the plz.
	 */
	public String getPlz() {
		return (String) ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().getReference( this, "plz", plz );
	}

	/**
	 * @param plz The plz to set.
	 */
	public void setPlz( String plz ) {
		// changeEvent
		if ( ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().isCleanModus() ) {
			this.plz = plz;
		}
		ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().setReference( this, "plz", plz );
	}

	/**
	 * @return Returns the strasse.
	 */
	public String getStrasse() {
		return (String) ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().getReference( this, "strasse", strasse );
	}

	/**
	 * @param strasse The strasse to set.
	 */
	public void setStrasse( String strasse ) {
		// changeEvent
		if ( ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().isCleanModus() ) {
			this.strasse = strasse;
		}
		ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().setReference( this, "strasse", strasse );
	}
	
	private static int nextRandomInt() {
		return random.nextInt();
	}
}