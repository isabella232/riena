package org.eclipse.riena.demo.client.customer.controllers;

public class CustomerLoader {

	private static String firstName;
	private static String lastName;

	public static String getFirstName() {
		return firstName;
	}

	public static void setFirstName(String firstName) {
		CustomerLoader.firstName = firstName;
	}

	public static String getLastName() {
		return lastName;
	}

	public static void setLastName(String lastName) {
		CustomerLoader.lastName = lastName;
	}

}
