/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.beans.common;

import java.beans.PropertyDescriptor;

/**
 * Utility class for getting and setting properties according to the
 * PropertyDescriptor
 * 
 * @deprecated please use instead {@code BeanPropertyUtils}.
 */
@Deprecated
public final class BeanPropertyAccessor {

	private BeanPropertyAccessor() {
		super();
	}

	/**
	 * Gets a propertyValue as described in the PropertyDescriptor for the
	 * passed bean parameter
	 * 
	 * @param bean
	 * @param descriptor
	 * @return
	 * @deprecated please use instead
	 *             {@code BeanPropertyUtils.getPropertyValue(bean, descriptor)}.
	 */
	@Deprecated
	public static Object getPropertyValue(final Object bean, final PropertyDescriptor descriptor) {
		return BeanPropertyUtils.getPropertyValue(bean, descriptor);
	}

	/**
	 * @param bean
	 * @param descriptor
	 * @param value
	 * @deprecated please use instead
	 *             {@code BeanPropertyUtils.setPropertyValue(bean, descriptor, value)}
	 *             .
	 */
	@Deprecated
	public static void setPropertyValue(final Object bean, final PropertyDescriptor descriptor, final Object value) {
		BeanPropertyUtils.setPropertyValue(bean, descriptor, value);
	}

}
