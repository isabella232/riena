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
package org.eclipse.riena.example.client.controllers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.core.marker.OutputMarker;
import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.ridgets.IDateTextRidget;
import org.eclipse.riena.ui.ridgets.INumericTextRidget;
import org.eclipse.riena.ui.ridgets.ISingleChoiceRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.validation.MaxLength;

/**
 * This controller displays the data of one person.
 */
public class OnePersonSubModuleController extends SubModuleController {

	private static List<String> countries;
	private Person person;
	private INumericTextRidget customerNumber;
	private ITextRidget lastName;
	private ITextRidget firstName;
	private IDateTextRidget birthday;
	private ITextRidget birthplace;
	private ITextRidget street;
	private IComboRidget country;
	private INumericTextRidget postalcode;
	private ITextRidget town;
	private ISingleChoiceRidget gender;

	public OnePersonSubModuleController() {
		this(null);
	}

	public OnePersonSubModuleController(ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	/**
	 * Creates and initializes one person.
	 * 
	 * @return person
	 */
	private void createPerson() {
		person = new Person("Mustermann", "Erika"); //$NON-NLS-1$ //$NON-NLS-2$
		person.setNumber(4711);
		person.setBirthday("12.08.1964"); //$NON-NLS-1$
		person.setBirthplace("Berlin"); //$NON-NLS-1$
		person.setGender(Person.FEMALE);
		person.getAddress().setStreetAndNumber("Heidestraße 17"); //$NON-NLS-1$
		person.getAddress().setCountry(Locale.GERMANY.getCountry());
		person.getAddress().setPostalCode(81739);
		person.getAddress().setTown("München"); //$NON-NLS-1$
	}

	@Override
	public void afterBind() {

		super.afterBind();

		updateAllRidgetsFromModel();
		updateTitle();

	}

	/**
	 * Returns a list of countries.
	 * 
	 * @return countries
	 */
	public List<String> getCountries() {

		if (countries == null) {
			countries = new ArrayList<String>();
			countries.add(Locale.FRANCE.getCountry());
			countries.add(Locale.CANADA.getCountry());
			countries.add(Locale.GERMANY.getCountry());
			countries.add(Locale.ITALY.getCountry());
			countries.add(Locale.UK.getCountry());
			countries.add(Locale.US.getCountry());
			String defCountry = Locale.getDefault().getCountry();
			if (!countries.contains(defCountry)) {
				countries.add(defCountry);
			}
			// countries = Arrays.asList(Locale.getISOCountries());
		}

		return countries;

	}

	private void updateTitle() {
		getNavigationNode().setLabel(person.toString());
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#configureRidgets()
	 */
	@Override
	public void configureRidgets() {

		customerNumber = (INumericTextRidget) getRidget("customerNumber"); //$NON-NLS-1$
		customerNumber.addMarker(new OutputMarker());
		customerNumber.setGrouping(false);
		lastName = (ITextRidget) getRidget("lastName"); //$NON-NLS-1$
		lastName.setMandatory(true);
		firstName = (ITextRidget) getRidget("firstName"); //$NON-NLS-1$
		firstName.setMandatory(true);
		birthday = (IDateTextRidget) getRidget("birthday"); //$NON-NLS-1$
		birthday.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);
		birthplace = (ITextRidget) getRidget("birthplace"); //$NON-NLS-1$
		gender = (ISingleChoiceRidget) getRidget("gender"); //$NON-NLS-1$
		street = (ITextRidget) getRidget("street"); //$NON-NLS-1$
		country = (IComboRidget) getRidget("country"); //$NON-NLS-1$
		postalcode = (INumericTextRidget) getRidget("postalCode"); //$NON-NLS-1$
		postalcode.setGrouping(false);
		postalcode.addValidationRule(new MaxLength(5), ValidationTime.ON_UI_CONTROL_EDIT);
		town = (ITextRidget) getRidget("town"); //$NON-NLS-1$
		IActionRidget show = (IActionRidget) getRidget("show"); //$NON-NLS-1$

		createPerson();
		person.addPropertyChangeListener(new NameChangeListener());

		customerNumber.bindToModel(person, "number"); //$NON-NLS-1$
		lastName.bindToModel(person, "lastname"); //$NON-NLS-1$
		firstName.bindToModel(person, "firstname"); //$NON-NLS-1$
		birthday.bindToModel(person, "birthday"); //$NON-NLS-1$
		birthplace.bindToModel(person, "birthplace"); //$NON-NLS-1$
		List<String> genders = new ArrayList<String>(2);
		genders.add(Person.FEMALE);
		genders.add(Person.MALE);
		gender.bindToModel(genders, genders, person, "gender"); //$NON-NLS-1$
		street.bindToModel(person.getAddress(), "streetAndNumber"); //$NON-NLS-1$
		country.bindToModel(this, "countries", String.class, null, person.getAddress(), "country"); //$NON-NLS-1$ //$NON-NLS-2$
		postalcode.bindToModel(person.getAddress(), "postalCode"); //$NON-NLS-1$
		town.bindToModel(person.getAddress(), "town"); //$NON-NLS-1$
		show.addListener(new ShowActionListener());

	}

	/**
	 * Displays the properties of the person.
	 */
	private class ShowActionListener implements IActionListener {

		public void callback() {

			System.out.println("lastname: " + person.getLastname()); //$NON-NLS-1$
			System.out.println("firstname: " + person.getFirstname()); //$NON-NLS-1$
			System.out.println("birthday: " + person.getBirthday()); //$NON-NLS-1$
			System.out.println("birthplace: " + person.getBirthplace()); //$NON-NLS-1$
			System.out.println("gender: " + person.getGender()); //$NON-NLS-1$
			System.out.println("streetAndNumber: " + person.getAddress().getStreetAndNumber()); //$NON-NLS-1$
			System.out.println("country: " + person.getAddress().getCountry()); //$NON-NLS-1$
			System.out.println("postalCode: " + person.getAddress().getPostalCode()); //$NON-NLS-1$
			System.out.println("town: " + person.getAddress().getTown()); //$NON-NLS-1$

		}

	}

	private class NameChangeListener implements PropertyChangeListener {

		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(Person.PROPERTY_LASTNAME)
					|| evt.getPropertyName().equals(Person.PROPERTY_FIRSTNAME)) {
				updateTitle();
			}
		}

	}

}
