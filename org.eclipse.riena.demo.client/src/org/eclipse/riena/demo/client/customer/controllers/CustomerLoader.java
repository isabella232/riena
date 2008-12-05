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
