/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.util.beans;


/**
 * List element of the list held by the <code>PersonManager</code>
 * 
 * @author Frank Schepp
 */
public class Person extends AbstractBean {
	/**
	 * Comment for <code>MALE</code>
	 */
	public final static String MALE = "male";
	/**
	 * Comment for <code>FEMALE</code>
	 */
	public final static String FEMALE = "female";

	private String lastname;
	private String firstname;
	private String gender;
	// private boolean vip;
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
		birthday = "";
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

		return new Integer(eyeColor);
	}

	/**
	 * @param lastname
	 */
	public void setLastname(String lastname) {

		String oldLastname = this.lastname;
		this.lastname = lastname;

		firePropertyChanged("lastname", oldLastname, lastname);
	}

	/**
	 * @param firstname
	 */
	public void setFirstname(String firstname) {

		String oldFirstname = this.firstname;
		this.firstname = firstname;

		firePropertyChanged("firstname", oldFirstname, firstname);
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

		firePropertyChanged("eyeColor", new Integer(oldEyeColor), new Integer(eyeColor));
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
	 * @param hasDaughter
	 *            The hasDaughter to set.
	 */
	public void setHasDog(boolean hasDaughter) {
		this.hasDog = hasDaughter;
	}

	/**
	 * @return Returns the hasSon.
	 */
	public boolean isHasCat() {
		return this.hasCat;
	}

	/**
	 * @param hasSon
	 *            The hasSon to set.
	 */
	public void setHasCat(boolean hasSon) {
		this.hasCat = hasSon;
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
	 * @param value
	 */
	public void setEyeColorBrown(boolean value) {

		if (value) {
			setEyeColor(3);
		}
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

		return lastname + ", " + firstname;
	}

	/**
	 * Return object for presentation in a list.
	 * 
	 * @return a string representing object as list entry.
	 */
	public String getListEntry() {
		return lastname + " - " + firstname;
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