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
package org.eclipse.riena.ui.ridgets.databinding;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.runtime.Assert;
import org.eclipse.riena.beans.common.BeanPropertyAccessor;
import org.eclipse.riena.ui.ridgets.UIBindingFailure;

public class UnboundPropertyWritableList extends WritableList implements IUnboundPropertyObservable {

	private final Object bean;
	private final PropertyDescriptor propertyDescriptor;

	public UnboundPropertyWritableList(Object listBean, String listPropertyName) {
		Assert.isNotNull(listBean, "bound property for " + listPropertyName + " cannot be null."); //$NON-NLS-1$ //$NON-NLS-2$
		try {
			propertyDescriptor = PropertyUtils.getPropertyDescriptor(listBean, listPropertyName);
			if (propertyDescriptor == null) {
				throw new UIBindingFailure("Could not read propertyDescriptor " + listPropertyName + " in " + listBean); //$NON-NLS-1$//$NON-NLS-2$
			}
			bean = listBean;
		} catch (IllegalAccessException e) {
			throw new UIBindingFailure("Could not read property '" + listPropertyName + "' of bean " + listBean + ".", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					e);
		} catch (InvocationTargetException e) {
			throw new UIBindingFailure("Could not read property '" + listPropertyName + "' of bean " + listBean + ".", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					e);
		} catch (NoSuchMethodException e) {
			throw new UIBindingFailure("Could not read property '" + listPropertyName + "' of bean " + listBean + ".", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					e);
		}
		updateFromBean();
	}

	public void updateFromBean() {
		Object value = BeanPropertyAccessor.getPropertyValue(bean, propertyDescriptor);
		if (value == null) {
			clear();
		} else if (value instanceof Collection) {
			updateWrappedList(new ArrayList<Object>((Collection<?>) value));
		} else {
			throw new UIBindingFailure("The property '" + propertyDescriptor.getName() //$NON-NLS-1$
					+ "'is not a java.util.Collection."); //$NON-NLS-1$
		}
	}

	@SuppressWarnings("unchecked")
	public void updateToBean() {
		List<Object> newValue = new ArrayList<Object>(this);
		BeanPropertyAccessor.setPropertyValue(bean, propertyDescriptor, newValue);
	}

	@Override
	public void add(int index, Object element) {
		super.add(index, element);
		updateToBean();
	}

	@Override
	public Object set(int index, Object element) {
		Object returnValue = super.set(index, element);
		updateToBean();
		return returnValue;
	}

	@Override
	public Object remove(int index) {
		Object returnValue = super.remove(index);
		updateToBean();
		return returnValue;
	}

}
