/*******************************************************************************
 * Copyright (c) 2008 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.objecttransaction.delta;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.riena.objecttransaction.IObjectId;
import org.eclipse.riena.objecttransaction.ObjectTransactionFailure;
import org.eclipse.riena.objecttransaction.state.State;

/**
 * this instance holds all changes for one transacted objects.
 * 
 */
public class TransactionDelta implements Cloneable {

	private IObjectId objectId;
	private State state; // (Clean, Created, Modified, Deleted)
	private String version; // version of TransactionDelta
	private Map<String, AbstractBaseChange> referenceChanges; // (refName à

	// newValue)

	/**
	 * @param objectId
	 * @param state
	 * @param version
	 */
	public TransactionDelta(IObjectId objectId, State state, String version) {
		super();
		setObjectId(objectId);
		setState(state);
		setVersion(version);
		this.referenceChanges = new HashMap<String, AbstractBaseChange>();
	}

	/**
	 * @return Returns the objectId.
	 */
	public IObjectId getObjectId() {
		return objectId;
	}

	/**
	 * @param objectId
	 *            The objectId to set.
	 */
	public void setObjectId(IObjectId objectId) {
		this.objectId = objectId;
	}

	/**
	 * Sets the change for a 1:1 relation overriding previous changes
	 * 
	 * @param refName
	 * @param childObject
	 */
	public void setSingleRefObject(String refName, Object childObject) {
		referenceChanges.put(refName, new SingleChange(refName, childObject));
	}

	/**
	 * @param refName
	 * @return
	 */
	public boolean hasSingleRefObject(String refName) {
		SingleChange sEntry = ((SingleChange) referenceChanges.get(refName));
		if (sEntry != null) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the change for a 1:1 relation, if there is no it returns null
	 * 
	 * @param refName
	 * @return
	 */
	public Object getSingleRefObject(String refName) {
		SingleChange sEntry = ((SingleChange) referenceChanges.get(refName));
		if (sEntry == null) {
			return null;
		}
		return sEntry.getChildObject();
	}

	/**
	 * Adds a change to a 1:N relation, creates a new 1:N relation if it did not
	 * exist
	 * 
	 * @param refName
	 * @param childObject
	 */
	public void addMultiRefObject(String refName, Object childObject) {
		AbstractBaseChange baseEntry = referenceChanges.get(refName);
		if (!(baseEntry instanceof MultipleChange) && baseEntry != null) {
			throw new ObjectTransactionFailure("impossible to add a reference to refName" + refName
					+ "as it was previously used with a set object, which creates a 1:1 reference.");
		}
		MultipleChange entry = (MultipleChange) baseEntry;
		if (entry == null) {
			entry = new MultipleChange(refName);
			referenceChanges.put(refName, entry);
		}
		entry.addEntry(childObject);
	}

	/**
	 * Removes a previously recorded change from a 1:N relation
	 * 
	 * @param refName
	 * @param childObject
	 */
	public void removeMultiRefObject(String refName, Object childObject) {
		AbstractBaseChange baseEntry = referenceChanges.get(refName);
		if (!(baseEntry instanceof MultipleChange) && baseEntry != null) {
			throw new ObjectTransactionFailure("impossible to add a reference to refName" + refName
					+ "as it was previously used with a set object, which creates a 1:1 reference.");
		}
		MultipleChange entry = (MultipleChange) baseEntry;
		if (entry == null) {
			entry = new MultipleChange(refName);
			referenceChanges.put(refName, entry);
		}
		entry.removeEntry(childObject);

	}

	protected Map<String, AbstractBaseChange> getAllChanges() {
		return referenceChanges;
	}

	/**
	 * @return Returns the state.
	 */
	public State getState() {
		return state;
	}

	/**
	 * @param state
	 *            The state to set.
	 */
	public void setState(State state) {
		this.state = state;
	}

	/**
	 * @return true if delta is clean
	 */
	public boolean isClean() {
		return state.equals(State.CLEAN);
	}

	/**
	 * @returntrue if delta is modified
	 */
	public boolean isModified() {
		return state.equals(State.MODIFIED);
	}

	/**
	 * @return true if delta is created
	 */
	public boolean isCreated() {
		return state.equals(State.CREATED);
	}

	/**
	 * @return true if delta is deleted
	 */
	public boolean isDeleted() {
		return state.equals(State.DELETED);
	}

	/**
	 * @return Returns the version.
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            The version to set.
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return
	 */
	public Map<String, AbstractBaseChange> getChanges() {
		return referenceChanges;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("##### objectId:" + objectId + " #####\n");
		sb.append("state:" + State.toString(state) + "\n");
		sb.append("reference Changes ---START---\n");
		for (Map.Entry<String, AbstractBaseChange> entry : referenceChanges.entrySet()) {
			sb.append(entry.toString() + "\n");
		}
		sb.append("reference Changes ---END---\n");
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	public Object clone() throws CloneNotSupportedException {
		TransactionDelta delta = (TransactionDelta) super.clone();
		delta.referenceChanges = (Map<String, AbstractBaseChange>) ((HashMap<String, AbstractBaseChange>) referenceChanges).clone();
		return delta;
	}

}