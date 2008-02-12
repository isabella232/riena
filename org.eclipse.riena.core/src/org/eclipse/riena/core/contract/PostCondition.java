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
 * Collection of static methods to perform postcondition checks due to design by
 * contract conventions.
 * 
 */
@Deprecated
public final class PostCondition {

	/**
	 * version ID (controlled by CVS).
	 */
	public static final String VERSION_ID = "$Id$";

	private PostCondition() {
		// Utility
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
	 *            the condition.
	 * 
	 * @throws PostConditionException
	 *             thrown if condition is false.
	 * 
	 * @see de.compeople.spirit.core.base.contract.PostConditionException
	 */
	@Deprecated
	public static void assertTrue(String message, boolean condition) {
		if (!condition) {
			throw new PostConditionException(message);
		}
	}

	/**
	 * check a given boolean condition and throw an exception if the condition
	 * is false. If the condition is true return with the object reference
	 * passed.
	 * 
	 * Example: return assertTrueAndReturn("Age is lower than
	 * 18",person.getAge() >= 18,person);
	 * 
	 * @param message
	 *            the message text to be inserted into the exception.
	 * @param condition
	 *            the condition.
	 * @param returnValue
	 *            any return value.
	 * 
	 * @return return with the <code>retObj</code> passed.
	 * 
	 * @throws PostConditionException
	 *             thrown if condition is false.
	 * 
	 * @see de.compeople.spirit.core.base.contract.PostConditionException
	 */
	@Deprecated
	public static <T> T assertTrueAndReturn(String message, boolean condition, T returnValue) {
		assertTrue(message, condition);
		return returnValue;
	}

	// /**
	// * check a given boolean condition and throw an exception if the condition
	// is false. If the condition is true return with the object reference
	// passed.
	// *
	// * Example: return assertTrueAndReturn("Age is lower than 18",age >=
	// 18,age);
	// *
	// * @param message the message text to be inserted into the exception.
	// * @param condition the condition.
	// * @param returnValue any return value.
	// *
	// * @return return with the <code>retVal</code> passed.
	// *
	// * @throws PostConditionException thrown if condition is false.
	// *
	// * @see de.compeople.spirit.core.base.contract.PostConditionException
	// */
	// public static int assertTrueAndReturn( String message, boolean condition,
	// int returnValue ) {
	// assertTrue( message, condition );
	// return returnValue;
	// }
	//
	// /**
	// * check a given boolean condition and throw an exception if the condition
	// is false. If the condition is true return with the object reference
	// passed.
	// *
	// * Example: return assertTrueAndReturn("Age is lower than 18",age >=
	// 18,age);
	// *
	// * @param message the message text to be inserted into the exception.
	// * @param condition the condition.
	// * @param returnValue any return value.
	// *
	// * @return return with the <code>retVal</code> passed.
	// *
	// * @throws PostConditionException thrown if condition is false.
	// *
	// * @see de.compeople.spirit.core.base.contract.PostConditionException
	// */
	// public static long assertTrueAndReturn( String message, boolean
	// condition, long returnValue ) {
	// assertTrue( message, condition );
	// return returnValue;
	// }

	/**
	 * check an object reference for not null and throw an exception if it is
	 * null.
	 * 
	 * Example: assertNotNull("Customer", customer);
	 * 
	 * @param objectName
	 *            the parameter name to be used in the error message.
	 * @param objectToCheck
	 *            the object to check.
	 * 
	 * @throws PostConditionException
	 *             thrown if condition is false.
	 * 
	 * @see de.compeople.spirit.core.base.contract.PostConditionException
	 */
	@Deprecated
	public static void assertNotNull(String objectName, Object objectToCheck) {
		// Optimized in order to concatenate Strings only if necessary.
		// See problem #278
		if (objectToCheck == null) {
			throw new PostConditionException("Object reference is null: " + objectName);
		}
	}

	/**
	 * check an object reference for not null and throw an exception if it is
	 * null. If the object isn't null return with the object reference.
	 * 
	 * Example: return assertNotNullAndReturn("Customer",customer);
	 * 
	 * @param objectName
	 *            the parameter name to be used in the error message.
	 * @param objectToCheck
	 *            the object to check.
	 * 
	 * @return return with the <code>objectToCheck</code> passed and checked.
	 * 
	 * @throws PostConditionException
	 *             thrown if condition is false.
	 * 
	 * @see de.compeople.spirit.core.base.contract.PostConditionException
	 */
	@Deprecated
	public static <T> T assertNotNullAndReturn(String objectName, T objectToCheck) {
		assertNotNull(objectName, objectToCheck);
		return objectToCheck;
	}

	/**
	 * Check a string reference for not null and not zero length. throw an
	 * exception if the string reference doesn't fulfill this requirements.
	 * 
	 * Example: assertNotNullAndNotZeroLength("Customer name", custName);
	 * 
	 * @param objectName
	 *            the parameter name to be used in the error message.
	 * @param objectToCheck
	 *            the object to check.
	 * 
	 * @throws PostConditionException
	 *             thrown if condition is false.
	 * 
	 * @see de.compeople.spirit.core.base.contract.PostConditionException
	 */
	@Deprecated
	public static void assertNotNullAndNotZeroLength(String objectName, String objectToCheck) {
		// Optimized in order to concatenate Strings only if necessary.
		// See problem #278
		if ((objectToCheck == null) || (objectToCheck.trim().length() == 0)) {
			throw new PostConditionException("String is null or has zero-length: " + objectName);
		}
	}

	/**
	 * Check a string reference for not null and not zero length. throw an
	 * exception if the string reference doesn't fulfill this requirements. If
	 * the String has been checked successful it's reference will be returned.
	 * 
	 * Example: return assertNotNullAndNotZeroLengthAndReturn("Customer
	 * name",custName);
	 * 
	 * @param objectName
	 *            the parameter name to be used in the error message.
	 * @param objectToCheck
	 *            the object to check.
	 * 
	 * @return the reference of the string just checked.
	 * 
	 * @throws PostConditionException
	 *             thrown if condition is false.
	 * 
	 * @see de.compeople.spirit.core.base.contract.PostConditionException
	 */
	@Deprecated
	public static String assertNotNullAndNotZeroLengthAndReturn(String objectName, String objectToCheck) {
		assertNotNullAndNotZeroLength(objectName, objectToCheck);
		return objectToCheck;
	}
}