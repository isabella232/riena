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
package org.eclipse.riena.objecttransaction.simple.value;

import org.eclipse.riena.objecttransaction.AbstractTransactedObject;
import org.eclipse.riena.objecttransaction.ITransactedObject;
import org.eclipse.riena.objecttransaction.InvalidTransactionFailure;
import org.eclipse.riena.objecttransaction.ObjectTransactionManagerAccessor;

/**
 * TODO Fehlender Klassen-Kommentar
 * 
 * @author Christian Campo
 */
public class Vertrag extends AbstractTransactedObject implements ITransactedObject {

	private String vertragsNummer;
	private String vertragsBeschreibung;
	private Long vertragsSumme;

	@SuppressWarnings("unused")
	private Vertrag() {
		super();
		if ( ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().isCleanModus() ) {
			ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().register( this );
		} else {
			throw new InvalidTransactionFailure( "cannot instantiate Vertrag with private method if not in clean state" );
		}
	}

	/**
	 * @param vertragsnummer
	 */
	public Vertrag( String vertragsnummer ) {
		super( new GenericOID( "vertrag", "vertragsnr", vertragsnummer ), "1" );
		if ( ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().isCleanModus() ) {
			ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().register( this );
		} else {
			ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().registerNew( this );
		}
		setVertragsNummer( vertragsnummer );
	}

	/**
	 * @return Returns the vertragsNummer.
	 */
	public String getVertragsNummer() {
		return (String) ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().getReference( this, "vertragsNummer", vertragsNummer );
	}

	/**
	 * @param vertragsNummer The vertragsNummer to set.
	 */
	public void setVertragsNummer( String vertragsNummer ) {
		if ( ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().isCleanModus() ) {
			this.vertragsNummer = vertragsNummer;
		}
		ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().setReference( this, "vertragsNummer", vertragsNummer );
	}

	/**
	 * @return Returns the vertragsBeschreibung.
	 */
	public String getVertragsBeschreibung() {
		return (String) ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().getReference( this, "vertragsBeschreibung",
			vertragsBeschreibung );
	}

	/**
	 * @param vertragsBeschreibung The vertragsBeschreibung to set.
	 */
	public void setVertragsBeschreibung( String vertragsBeschreibung ) {
		if ( ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().isCleanModus() ) {
			this.vertragsBeschreibung = vertragsBeschreibung;
		}
		ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().setReference( this, "vertragsBeschreibung", vertragsBeschreibung );
	}

	/**
	 * @return Returns the vertragsSumme.
	 */
	public Long getVertragsSumme() {
		return (Long) ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().getReference( this, "vertragsSumme", vertragsSumme );
	}

	/**
	 * @param vertragsSumme The vertragsSumme to set.
	 */
	public void setVertragsSumme( Long vertragsSumme ) {
		if ( ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().isCleanModus() ) {
			this.vertragsSumme = vertragsSumme;
		}
		ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().setReference( this, "vertragsSumme", vertragsSumme );

	}
}