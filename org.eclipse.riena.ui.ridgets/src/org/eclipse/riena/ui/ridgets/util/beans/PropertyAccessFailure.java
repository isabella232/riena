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
	public static PropertyAccessFailure createReadAccessException(Object bean, PropertyDescriptor propertyDescriptor,
			Throwable cause) {

		String beanType;
		if (bean == null) {
			beanType = null;
		} else {
			beanType = bean.getClass().getName();
		}
		String message = "Failed to read an adapted Java Bean property." + "\nbean     =" + bean + "\nbean type=" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ beanType + "\nproperty name  =" + propertyDescriptor.getName() + "\nproperty type  =" //$NON-NLS-1$ //$NON-NLS-2$
				+ propertyDescriptor.getPropertyType().getName() + "\nproperty reader=" //$NON-NLS-1$
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
	public static PropertyAccessFailure createWriteAccessException(Object bean, Object value,
			PropertyDescriptor propertyDescriptor, Throwable cause) {

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

		String message = "Failed to set an adapted Java Bean property." + "\nbean      =" + bean + "\nbean type =" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ beanType + "\nvalue     =" + value + "\nvalue type=" + valueType + "\nproperty name  =" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ propertyDescriptor.getName() + "\nproperty type  =" + propertyDescriptor.getPropertyType().getName() //$NON-NLS-1$
				+ "\nproperty setter=" + propertyDescriptor.getWriteMethod(); //$NON-NLS-1$

		return new PropertyAccessFailure(message, cause);
	}

}