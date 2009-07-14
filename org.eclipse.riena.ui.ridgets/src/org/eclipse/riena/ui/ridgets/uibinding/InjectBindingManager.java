/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.ridgets.uibinding;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.beans.common.BeanPropertyUtils;
import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.util.ReflectionFailure;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.internal.ui.ridgets.Activator;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IRidgetContainer;
import org.eclipse.riena.ui.ridgets.UIBindingFailure;

/**
 * This class manages the binding between UI-control and ridget.
 */
public class InjectBindingManager extends DefaultBindingManager {

	// cache for PropertyDescriptors
	private Map<String, PropertyDescriptor> binding2PropertyDesc;

	private static final Logger LOGGER = Log4r.getLogger(Activator.getDefault(), InjectBindingManager.class);

	/**
	 * Creates the managers of all bindings of a view.
	 * 
	 * @param propertyStrategy
	 *            - strategy to get the property for the binding from the
	 *            UI-control.
	 * @param mapper
	 *            - mapping for UI control-classes to ridget-classes
	 */
	public InjectBindingManager(IBindingPropertyLocator propertyStrategy, IControlRidgetMapper<Object> mapper) {
		super(propertyStrategy, mapper);
		binding2PropertyDesc = new HashMap<String, PropertyDescriptor>();
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.uibinding.DefaultBindingManager#injectRidget(org.eclipse.riena.ui.internal.ridgets.IRidgetContainer,
	 *      java.lang.String, org.eclipse.riena.ui.internal.ridgets.IRidget)
	 */
	@Override
	protected void injectRidget(IRidgetContainer ridgetContainer, String bindingProperty, IRidget ridget) {
		super.injectRidget(ridgetContainer, bindingProperty, ridget);
		try {
			injectIntoController(ridget, ridgetContainer, bindingProperty);
		} catch (ReflectionFailure e) {
			UIBindingFailure ee = new UIBindingFailure("Cannot create ridget for ridget property '" //$NON-NLS-1$
					+ bindingProperty + "' of ridget container " + ridgetContainer, e); //$NON-NLS-1$
			LOGGER.log(LogService.LOG_ERROR, ee.getMessage(), ee);
			throw ee;
		}
	}

	private void injectIntoController(IRidget ridget, IRidgetContainer controller, String bindingProperty) {
		bindingProperty = createMethodeNameProperty(bindingProperty);
		PropertyDescriptor desc = getPropertyDescriptor(bindingProperty, controller);
		if (desc == null) {
			throw new UnsupportedOperationException("Property '" + bindingProperty + "' unkown"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		BeanPropertyUtils.setPropertyValue(controller, desc, ridget);
	}

	private PropertyDescriptor getPropertyDescriptor(String bindingProperty, IRidgetContainer ridgetContainer) {
		PropertyDescriptor desc = binding2PropertyDesc.get(bindingProperty);
		if (desc != null) {
			return desc;
		}
		return createPropertyDescriptor(bindingProperty, ridgetContainer);
	}

	private PropertyDescriptor createPropertyDescriptor(String bindingProperty, IRidgetContainer ridgetContainer) {
		bindingProperty = createMethodeNameProperty(bindingProperty);
		try {
			PropertyDescriptor desc = PropertyUtils.getPropertyDescriptor(ridgetContainer, bindingProperty);
			binding2PropertyDesc.put(bindingProperty, desc);
			return desc;
		} catch (IllegalAccessException e) {
			UIBindingFailure bindingFailure = new UIBindingFailure("Cannot access ridget property '" + bindingProperty //$NON-NLS-1$
					+ "' of ridget container " + ridgetContainer, e); //$NON-NLS-1$
			LOGGER.log(LogService.LOG_ERROR, bindingFailure.getMessage(), bindingFailure);
		} catch (InvocationTargetException e) {
			UIBindingFailure bindingFailure = new UIBindingFailure("Cannot access ridget property '" + bindingProperty //$NON-NLS-1$
					+ "' of ridget container " + ridgetContainer, e); //$NON-NLS-1$
			LOGGER.log(LogService.LOG_ERROR, bindingFailure.getMessage(), bindingFailure);
		} catch (NoSuchMethodException e) {
			UIBindingFailure bindingFailure = new UIBindingFailure("Cannot access ridget property '" + bindingProperty //$NON-NLS-1$
					+ "' of ridget container " + ridgetContainer, e); //$NON-NLS-1$
			LOGGER.log(LogService.LOG_ERROR, bindingFailure.getMessage(), bindingFailure);
		}
		return null;
	}

	/**
	 * Creates from the given binding property a correct property of a
	 * (setter/getter) method name. Dots a removed and every letter after a dot
	 * is converted to uppercase.
	 * 
	 * @param bindingProperty
	 *            - binding property
	 * @return property of method name
	 */
	private String createMethodeNameProperty(String bindingProperty) {

		if (StringUtils.isEmpty(bindingProperty)) {
			return bindingProperty;
		}
		if (!bindingProperty.contains(IBindingPropertyLocator.SEPARATOR)) {
			return bindingProperty;
		}

		StringBuilder methodProperty = new StringBuilder();
		char[] propChars = bindingProperty.toCharArray();
		boolean pervWasDot = false;
		for (int i = 0; i < propChars.length; i++) {
			if (propChars[i] == '.') {
				pervWasDot = true;
				continue;
			}
			if (pervWasDot) {
				propChars[i] = Character.toUpperCase(propChars[i]);
			}
			methodProperty.append(propChars[i]);
			pervWasDot = false;
		}

		return methodProperty.toString();

	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.uibinding.DefaultBindingManager#getRidget(java.lang.String,
	 *      org.eclipse.riena.ui.internal.ridgets.IRidgetContainer)
	 */
	@Override
	protected IRidget getRidget(String bindingProperty, IRidgetContainer controller) {
		PropertyDescriptor desc = getPropertyDescriptor(bindingProperty, controller);
		return (IRidget) BeanPropertyUtils.getPropertyValue(controller, desc);
	}

}
