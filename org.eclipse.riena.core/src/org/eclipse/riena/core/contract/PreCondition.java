/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.core.contract;

/**
 * Collection of static methods to perform precondition check due to design by
 * contract conventions.
 * 
 */
@Deprecated
public final class PreCondition {

	/**
	 * version ID (controlled by CVS).
	 */
	public static final String VERSION_ID = "$Id$";

	private PreCondition() {
		// utility class
	}

	/**
	 * check a given boolean condition and throw an exception if the condition
	 * is false.
	 * 
	 * Example: assertTrue("Age is lower than 18",age >= 18);
	 * 
	 * @param message
	 *            the message text to be inserted into the exception.
	 * @param condition
	 *            the condition to check.
	 * 
	 * @throws PreConditionException
	 *             thrown if condition is false.
	 * 
	 * @see de.compeople.spirit.core.base.contract.PreConditionException
	 */
	@Deprecated
	public static void assertTrue(String message, boolean condition) {
		if (!condition) {
			throw new PreConditionException(message);
		}
	}

	/**
	 * check an object reference for not null and throw an exception if it is
	 * null.
	 * 
	 * Example: assertNotNull("Customer",customer);
	 * 
	 * @param objectName
	 *            the parameter name to be used in the error message.
	 * @param objectToCheck
	 *            the object to check.
	 * 
	 * @throws PreConditionException
	 *             thrown if condition is false.
	 * 
	 * @see de.compeople.spirit.core.base.contract.PreConditionException
	 */
	@Deprecated
	public static void assertNotNull(String objectName, Object objectToCheck) {
		// Optimized in order to concatenate Strings only if necessary.
		// See problem #278
		if (objectToCheck == null) {
			throw new PreConditionException("Object reference is null: " + objectName);
		}
	}

	/**
	 * Check an string reference for not null and not zero length. throw an
	 * exception if the string reference doesn't fulfill this requirements.
	 * 
	 * Example: assertNotNullAndNotZeroLength("Customer name",custName);
	 * 
	 * @param objectName
	 *            the parameter name to be used in the error message.
	 * @param objectToCheck
	 *            the string to check.
	 * 
	 * @throws PreConditionException
	 *             thrown if condition is false.
	 * 
	 * @see de.compeople.spirit.core.base.contract.PreConditionException
	 */
	@Deprecated
	public static void assertNotNullAndNotZeroLength(String objectName, String objectToCheck) {
		// Optimized in order to concatenate Strings only if necessary.
		// See problem #278
		if ((objectToCheck == null) || (objectToCheck.trim().length() == 0)) {
			throw new PreConditionException("String argument is null or has zero-length: " + objectName);
		}
	}
}
