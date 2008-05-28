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

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.riena.ui.ridgets.util.beans.Person;

/**
 * Creates a collection of persons.
 */
public class PersonFactory {

	private PersonFactory() {
		// prevent instantation
	}

	/**
	 * Create a collection of persons.
	 */
	public static Collection<Person> createPersonList() {
		Collection<Person> newList = new ArrayList<Person>();

		Person person = new Person("Doe", "John");
		person.setEyeColor(1);
		newList.add(person);

		person = new Person("Jackson", "Janet");
		person.setEyeColor(1);
		newList.add(person);

		person = new Person("Jackson", "Jermaine");
		person.setEyeColor(1);
		newList.add(person);

		person = new Person("Jackson", "John");
		person.setEyeColor(3);
		newList.add(person);

		person = new Person("JJ Jr. Shabadoo", "Joey");
		person.setEyeColor(3);
		newList.add(person);

		person = new Person("Johnson", "Jack");
		person.setEyeColor(2);
		newList.add(person);

		person = new Person("Johnson", "Jane");
		person.setEyeColor(3);
		newList.add(person);

		person = new Person("Zappa", "Frank");
		person.setEyeColor(2);
		newList.add(person);

		return newList;
	}
}
