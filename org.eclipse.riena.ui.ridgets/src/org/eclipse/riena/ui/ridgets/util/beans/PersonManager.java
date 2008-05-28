/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.util.beans;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.StringTokenizer;

import org.eclipse.riena.ui.ridgets.util.IComboBoxEntryFactory;

/**
 * @author Frank Schepp
 */
public class PersonManager implements IComboBoxEntryFactory {

	/**
	 * Property name of the selected person property.
	 */
	public static final String PROPERTY_SELECTED_PERSON = "selectedPerson"; //$NON-NLS-1$

	private PropertyChangeSupport propertyChangeSupport;
	private Collection<Person> persons;
	private Person selectedPerson;

	/**
	 * constructor.
	 * 
	 * @param persons
	 */
	public PersonManager(Collection<Person> persons) {

		super();
		propertyChangeSupport = new PropertyChangeSupport(this);
		setPersons(persons);
	}

	public void addPropertyChangeListener(String p, PropertyChangeListener l) {

		propertyChangeSupport.addPropertyChangeListener(p, l);
	}

	public void addPropertyChangeListener(PropertyChangeListener l) {

		propertyChangeSupport.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(String p, PropertyChangeListener l) {

		propertyChangeSupport.removePropertyChangeListener(p, l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {

		propertyChangeSupport.removePropertyChangeListener(l);
	}

	/**
	 * @return persons
	 */
	public Collection<Person> getPersons() {

		return persons;
	}

	/**
	 * @param persons
	 */
	public void setPersons(Collection<Person> persons) {

		this.persons = persons;
	}

	/**
	 * @see de.compeople.spirit.core.client.uibinding.adapter.util.IComboBoxEntryFactory#createNewEntry(java.lang.Object)
	 */
	public Person createNewEntry(Object listEntryString) {
		if (listEntryString instanceof String) {
			StringTokenizer t = new StringTokenizer((String) listEntryString, ","); //$NON-NLS-1$

			Person person;
			switch (t.countTokens()) {
			case 0:
				person = new Person("", ""); //$NON-NLS-1$//$NON-NLS-2$
				break;
			case 1:
				person = new Person(t.nextToken().trim(), ""); //$NON-NLS-1$
				break;
			case 2:
				person = new Person(t.nextToken().trim(), t.nextToken().trim());
				break;
			default:
				person = new Person("", ""); //$NON-NLS-1$ //$NON-NLS-2$
				break;
			}
			return person;
		}
		return null;
	}

	/**
	 * @return Returns the selectedPerson.
	 */
	public Person getSelectedPerson() {
		return selectedPerson;
	}

	/**
	 * @param newSelection
	 *            The selectedPerson to set.
	 */
	public void setSelectedPerson(Person newSelection) {
		Person oldSelection = this.selectedPerson;
		this.selectedPerson = newSelection;
		propertyChangeSupport.firePropertyChange(PROPERTY_SELECTED_PERSON, oldSelection, newSelection);
	}
}