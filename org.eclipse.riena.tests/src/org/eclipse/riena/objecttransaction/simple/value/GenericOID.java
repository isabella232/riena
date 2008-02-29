package org.eclipse.riena.objecttransaction.simple.value;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.riena.objecttransaction.IObjectId;

/**
 * TODO Fehlender Klassen-Kommentar
 *
 * @author Christian Campo
 */
public class GenericOID implements IObjectId {
	private String type;
	private String primName;
	private Object primValue;

	/**
	 * @param type
	 * @param primName
	 * @param primValue
	 */
	public GenericOID( String type, String primName, Object primValue ) {
		super();
		this.type = type;
		this.primName = primName;
		this.primValue = primValue;
	}

	/**
	 * @see de.compeople.scp.objecttransaction.IObjectId#getType()
	 */
	public String getType() {
		return type;
	}

	/**
	 * @see de.compeople.scp.objecttransaction.IObjectId#getProperties()
	 */
	protected Map<String,Object> getProperties() {
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put( primName, primValue );
		return map;
	}

	/**
	 * @see de.compeople.scp.objecttransaction.IObjectId#equals(de.compeople.scp.objecttransaction.IObjectId)
	 */
	public boolean equals( Object oid ) {
		if ( oid instanceof GenericOID ) {
			GenericOID gOID = (GenericOID) oid;
			if ( gOID.type.equals( type ) && gOID.primName.equals( primName ) && gOID.primValue.equals( primValue ) ) {
				return true;
			}
		}
		return super.equals( oid );
	}

	public int hashCode() {
		try {
			if ( primValue instanceof Integer ) {
				return ( (Integer) primValue ).intValue();
			}
			return Integer.parseInt( (String) primValue );
		} catch ( NumberFormatException e ) {
			return super.hashCode();
		}
	}

	public String toString() {
		return "type:" + type + " primName:" + primName + " primValue:" + primValue;
	}

}