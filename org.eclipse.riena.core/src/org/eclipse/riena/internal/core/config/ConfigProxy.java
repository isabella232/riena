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
package org.eclipse.riena.internal.core.config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.internal.core.Activator;
import org.osgi.framework.Constants;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.log.LogService;

/**
 * This class makes configuration a regular Java POJO easier. Its used by
 * ConfigUtility and is instantiated for each Java POJO instance that should be
 * wrapped. It translates configuration properties from ConfigAdmin into setter
 * calls of the underlying Java POJO.
 * 
 */
public class ConfigProxy implements ManagedService {

	private Object bObject;
	private HashMap<String, Method> methods;
	private Logger LOGGER = Activator.getDefault().getLogger(ConfigProxy.class.getName());

	/**
	 * standard constructor that takes the object to wrap as parameter
	 * 
	 * @param bObject
	 */
	public ConfigProxy(Object bObject) {
		this.bObject = bObject;
		methods = new HashMap<String, Method>();
		for (Method method : bObject.getClass().getMethods()) {
			if (method.getName().startsWith("set")) {
				methods.put(method.getName(), method);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.service.cm.ManagedService#updated(java.util.Dictionary)
	 */
	public void updated(Dictionary properties) throws ConfigurationException {
		if (properties == null) {
			return;
		}
		Enumeration<?> eKeys = properties.keys();
		while (eKeys.hasMoreElements()) {
			Object oKey = eKeys.nextElement();
			if (oKey instanceof String) {
				String key = (String) oKey;
				if (key.equals(Constants.SERVICE_PID)) {
					continue;
				}
				if (key.contains(".")) {
					LOGGER.log(LogService.LOG_WARNING, "skipping " + key);
					continue;
				}
				Object oValue = properties.get(key);
				String methodName = "set" + key.substring(0, 1).toUpperCase() + key.substring(1);
				Method m = methods.get(methodName);
				LOGGER.log(LogService.LOG_DEBUG, methodName + " :" + oValue + " ... " + System.currentTimeMillis());
				try {
					if (m != null) {
						m.invoke(bObject, oValue);
					} else {
						LOGGER.log(LogService.LOG_WARNING, "method" + m.getName() + "does not exist");
					}
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}