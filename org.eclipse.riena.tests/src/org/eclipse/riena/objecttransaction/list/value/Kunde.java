/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.objecttransaction.list.value;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.riena.objecttransaction.AbstractTransactedObject;
import org.eclipse.riena.objecttransaction.ITransactedObject;
import org.eclipse.riena.objecttransaction.InvalidTransactionFailure;
import org.eclipse.riena.objecttransaction.ObjectTransactionManagerAccessor;

/**
 * Sample class for "Kunde"
 * 
 * @author Christian Campo
 */
public class Kunde extends AbstractTransactedObject implements ITransactedObject {

	private String vorname;
	private String nachname;
	private String kundennr;
	private Addresse addresse;
	private List<Vertrag> vertraege;

	/** constructor called by webservices only for clean objects * */
	@SuppressWarnings("unused")
	private Kunde() {
		super();
		if (ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().isCleanModus()) {
			ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().register(this);
		} else {
			throw new InvalidTransactionFailure("cannot instantiate Kunde with private method if not in clean state");
		}
	}

	/**
	 * @param kundennr
	 */
	public Kunde(String kundennr) {
		super(new GenericOID("kunde", "kundennrpk", kundennr), "1");
		if (ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().isCleanModus()) {
			ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().register(this);
		} else {
			ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().registerNew(this);
		}
		setKundennr(kundennr);
		vertraege = new ArrayList<Vertrag>();
	}

	/**
	 * @return Returns the kundennr.
	 */
	public String getKundennr() {
		return (String) ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().getReference(
				this, "kundennr", kundennr);
	}

	/**
	 * @param kundennr
	 *            The kundennr to set.
	 */
	public void setKundennr(String kundennr) {
		if (((GenericOID) getObjectId()).getProperties().get("primkey") != null) {
			throw new UnsupportedOperationException("cannot change kundennr (once it is set)");
		}
		// changeEvent
		if (ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().isCleanModus()) {
			this.kundennr = kundennr;
		} else {
			ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().setReference(this,
					"kundennr", kundennr);
		}
	}

	/**
	 * @return Returns the nachname.
	 */
	public String getNachname() {
		return (String) ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().getReference(
				this, "nachname", nachname);
	}

	/**
	 * @param nachname
	 *            The nachname to set.
	 */
	public void setNachname(String nachname) {
		// changeEvent
		if (ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().isCleanModus()) {
			this.nachname = nachname;
		} else {
			ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().setReference(this,
					"nachname", nachname);
		}
	}

	/**
	 * @return Returns the vorname.
	 */
	public String getVorname() {
		return (String) ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().getReference(
				this, "vorname", vorname);
	}

	/**
	 * @param vorname
	 *            The vorname to set.
	 */
	public void setVorname(String vorname) {
		// changeEvent
		if (ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().isCleanModus()) {
			this.vorname = vorname;
		} else {
			ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().setReference(this, "vorname",
					vorname);
		}
	}

	/**
	 * @return Returns the addresse.
	 */
	public Addresse getAddresse() {
		return (Addresse) ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().getReference(
				this, "addresse", addresse);
	}

	/**
	 * @param addresse
	 *            The addresse to set.
	 */
	public void setAddresse(Addresse addresse) {
		// changeEvent
		if (ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().isCleanModus()) {
			this.addresse = addresse;
		} else {
			ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().setReference(this,
					"addresse", addresse);
		}
	}

	/**
	 * @param vertrag
	 */
	public void addVertrag(Vertrag vertrag) {
		if (ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().isCleanModus()) {
			vertraege.add(vertrag);
		} else {
			ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().addReference(this, "vertrag",
					vertrag);
		}
	}

	/**
	 * @param vertragsNummer
	 */
	public void removeVertrag(String vertragsNummer) {
		Vertrag tempVertrag = getVertrag(vertragsNummer);
		// changeEvent
		if (ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().isCleanModus()) {
			vertraege.remove(getVertrag(vertragsNummer));
		} else {
			ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().removeReference(this,
					"vertrag", tempVertrag);
		}
	}

	/**
	 * @param vertrag
	 */
	public void removeVertrag(Vertrag vertrag) {
		// changeEvent
		if (ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().isCleanModus()) {
			vertraege.remove(vertrag);
		} else {
			ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().removeReference(this,
					"vertrag", vertrag);
		}
	}

	/**
	 * @param vertragsNummer
	 * @return
	 */
	public Vertrag getVertrag(String vertragsNummer) {
		Vertrag[] tempVertraege = listVertrag();
		for (int i = 0; i < tempVertraege.length; i++) {
			if (tempVertraege[i].getVertragsNummer().equals(vertragsNummer)) {
				return tempVertraege[i];
			}
		}
		return null;
	}

	/**
	 * @return
	 */
	public Vertrag[] listVertrag() {
		List<Vertrag> vertraegeSet = ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent()
				.listReference(this, "vertrag", vertraege);
		if (vertraegeSet.size() == 0) {
			return new Vertrag[0];
		}
		return vertraegeSet.toArray(new Vertrag[vertraegeSet.size()]);
	}
}