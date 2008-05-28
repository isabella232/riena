/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.util.beans;

import java.beans.PropertyDescriptor;

/**
 * A runtime exception that describes read and write access problems when
 * getting/setting a Java Bean property.
 */
public final class PropertyAccessFailure extends PropertyFailure {

	/**
	 * Constructs a new exception instance with the specified detail message and
	 * cause.
	 * 
	 * @param message
	 *            the detail message (which is saved for later retrieval by the
	 *            {@link #getMessage()}method).
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            {@link #getCause()}method). (A<code>null</code> value is
	 *            permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 */
	public PropertyAccessFailure(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Creates and returns a new PropertyAccessException instance for a failed
	 * read access for the specified bean, property descriptor and cause.
	 * 
	 * @param bean
	 *            the target bean
	 * @param propertyDescriptor
	 *            describes the bean's property
	 * @param cause
	 *            the Throwable that caused this exception
	 * @return an exception that describes a read access problem
	 */
	public static PropertyAccessFailure createReadAccessException(Object bean, PropertyDescriptor propertyDescriptor, Throwable cause) {

		String beanType;
		if (bean == null) {
			beanType = null;
		} else {
			beanType = bean.getClass().getName();
		}
		String message = "Failed to read an adapted Java Bean property." + "\nbean     =" + bean + "\nbean type=" + beanType + "\nproperty name  ="
				+ propertyDescriptor.getName() + "\nproperty type  =" + propertyDescriptor.getPropertyType().getName() + "\nproperty reader="
				+ propertyDescriptor.getReadMethod();

		return new PropertyAccessFailure(message, cause);
	}

	/**
	 * Creates and returns a new PropertyAccessException instance for a failed
	 * write access for the specified bean, value, property descriptor and
	 * cause.
	 * 
	 * @param bean
	 *            the target bean
	 * @param value
	 *            the value that could not be set
	 * @param propertyDescriptor
	 *            describes the bean's property
	 * @param cause
	 *            the Throwable that caused this exception
	 * @return an exception that describes a write access problem
	 */
	public static PropertyAccessFailure createWriteAccessException(Object bean, Object value, PropertyDescriptor propertyDescriptor, Throwable cause) {

		String beanType;
		if (bean == null) {
			beanType = null;
		} else {
			beanType = bean.getClass().getName();
		}
		String valueType;
		if (value == null) {
			valueType = null;
		} else {
			valueType = value.getClass().getName();
		}

		String message = "Failed to set an adapted Java Bean property." + "\nbean      =" + bean + "\nbean type =" + beanType + "\nvalue     =" + value
				+ "\nvalue type=" + valueType + "\nproperty name  =" + propertyDescriptor.getName() + "\nproperty type  ="
				+ propertyDescriptor.getPropertyType().getName() + "\nproperty setter=" + propertyDescriptor.getWriteMethod();

		return new PropertyAccessFailure(message, cause);
	}

}