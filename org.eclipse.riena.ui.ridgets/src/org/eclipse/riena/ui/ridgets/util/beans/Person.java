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
package org.eclipse.riena.ui.ridgets.util.beans;

/**
 * List element of the list held by the <code>PersonManager</code>
 */
public class Person extends AbstractBean {
	/**
	 * Property name of the first name property ("firstname").
	 */
	public static final String PROPERTY_FIRSTNAME = "firstname"; //$NON-NLS-1$
	/**
	 * Property name of the first name property ("lastname").
	 */
	public static final String PROPERTY_LASTNAME = "lastname"; //$NON-NLS-1$
	/**
	 * Property name of the eye color property ("eyeColor").
	 */
	public static final String PROPERTY_EYE_COLOR = "eyeColor"; //$NON-NLS-1$
	/**
	 * Constant for <code>MALE</code> gender value ("male").
	 */
	public final static String MALE = "male"; //$NON-NLS-1$
	/**
	 * Constant for <code>FEMALE</code> gender value ("female").
	 */
	public final static String FEMALE = "female"; //$NON-NLS-1$

	private String lastname;
	private String firstname;
	private String gender;
	private boolean hasDog;
	private boolean hasCat;
	private boolean hasFish;
	private String birthday;
	private int eyeColor;

	/**
	 * constructor.
	 * 
	 * @param lastname
	 * @param firstname
	 */
	public Person(String lastname, String firstname) {
		super();

		this.lastname = lastname;
		this.firstname = firstname;
		birthday = ""; //$NON-NLS-1$
		gender = MALE;
	}

	/**
	 * @return last name
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * @return first name
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @return eye color
	 */
	public Integer getEyeColor() {
		return Integer.valueOf(eyeColor);
	}

	/**
	 * @param lastname
	 */
	public void setLastname(String lastname) {
		String oldLastname = this.lastname;
		this.lastname = lastname;

		firePropertyChanged(PROPERTY_LASTNAME, oldLastname, lastname);
	}

	/**
	 * @param firstname
	 */
	public void setFirstname(String firstname) {
		String oldFirstname = this.firstname;
		this.firstname = firstname;

		firePropertyChanged(PROPERTY_FIRSTNAME, oldFirstname, firstname);
	}

	/**
	 * @param newEyeColor
	 */
	public void setEyeColor(Integer newEyeColor) {
		if (newEyeColor != null) {
			setEyeColor(newEyeColor.intValue());
		}
	}

	/**
	 * @param newEyeColor
	 */
	public void setEyeColor(int newEyeColor) {
		int oldEyeColor = this.eyeColor;
		this.eyeColor = newEyeColor;

		firePropertyChanged(PROPERTY_EYE_COLOR, Integer.valueOf(oldEyeColor), Integer.valueOf(eyeColor));
	}

	/**
	 * @param value
	 */
	public void setEyeColorGreen(boolean value) {
		if (value) {
			setEyeColor(0);
		}
	}

	/**
	 * @param value
	 */
	public void setEyeColorGray(boolean value) {
		if (value) {
			setEyeColor(1);
		}
	}

	/**
	 * @param value
	 */
	public void setEyeColorBlue(boolean value) {
		if (value) {
			setEyeColor(2);
		}
	}

	/**
	 * @param value
	 */
	public void setEyeColorBrown(boolean value) {
		if (value) {
			setEyeColor(3);
		}
	}

	/**
	 * @return gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return Returns the hasDaughter.
	 */
	public boolean isHasDog() {
		return this.hasDog;
	}

	/**
	 * @param hasDog
	 *            The hasDog to set.
	 */
	public void setHasDog(boolean hasDog) {
		this.hasDog = hasDog;
	}

	/**
	 * @return Returns the hasSon.
	 */
	public boolean isHasCat() {
		return this.hasCat;
	}

	/**
	 * @param hasCat
	 *            The hasCat to set.
	 */
	public void setHasCat(boolean hasCat) {
		this.hasCat = hasCat;
	}

	/**
	 * @return Returns the hasFish.
	 */
	public boolean isHasFish() {
		return this.hasFish;
	}

	/**
	 * @param hasFish
	 *            The hasFish to set.
	 */
	public void setHasFish(boolean hasFish) {
		this.hasFish = hasFish;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Person)) {
			return false;
		}

		Person p = (Person) obj;

		return equalsFirstname(p) && equalsLastname(p);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return toString().hashCode();
	}

	private boolean equalsFirstname(Person person) {
		if (firstname == null) {
			return person.firstname == firstname;
		} else {
			return firstname.equals(person.firstname);
		}
	}

	private boolean equalsLastname(Person person) {
		if (lastname == null) {
			return lastname == person.lastname;
		} else {
			return lastname.equals(person.lastname);
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return lastname + ", " + firstname; //$NON-NLS-1$
	}

	/**
	 * Return object for presentation in a list.
	 * 
	 * @return a string representing object as list entry.
	 */
	public String getListEntry() {
		return lastname + " - " + firstname; //$NON-NLS-1$
	}

	/**
	 * @return Returns the birthday.
	 */
	public String getBirthday() {
		return birthday;
	} // end method

	/**
	 * @param birthday
	 *            The birthday to set.
	 */
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	} // end method
}