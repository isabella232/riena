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
package org.eclipse.riena.internal.example.client.beans;

import org.eclipse.riena.example.client.model.Person;

/**
 * This beans allows editing of a {@link Person} without immediately changing
 * it. Modifications to the first and last name are stored in the bean and are
 * only applied to the Person after invoking {@link #update()}.
 */
public final class PersonModificationBean {

	private Person person;
	private String first;
	private String last;

	public String getFirstName() {
		return person == null ? "" : person.getFirstname(); //$NON-NLS-1$
	}

	public void setFirstName(String first) {
		this.first = first;
	}

	public String getLastName() {
		return person == null ? "" : person.getLastname(); //$NON-NLS-1$
	}

	public void setLastName(String last) {
		this.last = last;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
		first = getFirstName();
		last = getLastName();
	}

	public void update() {
		if (person != null) {
			person.setFirstname(first);
			person.setLastname(last);
		}
	}

}
