package org.eclipse.riena.objecttransaction.list.value;

import java.security.SecureRandom;

import org.eclipse.riena.objecttransaction.AbstractTransactedObject;
import org.eclipse.riena.objecttransaction.ITransactedObject;
import org.eclipse.riena.objecttransaction.InvalidTransactionFailure;
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
		if ( ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().isCleanModus() ) {
			ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().register( this );
		} else {
			throw new InvalidTransactionFailure( "cannot instantiate Adresse with private method if not in clean state" );
		}
	}

	/**
	 * @param dummy
	 */
	public Addresse( boolean dummy ) {
		super( new GenericOID( "addresse", "primkey", Integer.valueOf( nextRandomInt() ) ), "1" );
		if ( ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().isCleanModus() ) {
			ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().register( this );
		} else {
			ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().registerNew( this );
		}
	}

	/**
	 * @param primKey
	 */
	public Addresse( Integer primKey ) {
		super( new GenericOID( "addresse", "primkey", primKey ), "1" );
		if ( ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().isCleanModus() ) {
			ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().register( this );
		} else {
			ObjectTransactionManagerAccessor.fetchObjectTransactionManager().getCurrent().registerNew( this );
		}
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