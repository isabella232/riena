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

import org.osgi.framework.Constants;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

/**
 * This class makes configuration a regular Java POJO easier. Its used by ConfigUtility and is instantiated for each
 * Java POJO instance that should be wrapped. It translates configuration properties from ConfigAdmin into setter calls
 * of the underlying Java POJO.
 * 
 */
public class ConfigProxy implements ManagedService {

    private Object bObject;
    private HashMap<String, Method> methods;

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
                    System.out.println("skipping " + key);
                    continue;
                }
                Object oValue = properties.get(key);
                String methodName = "set" + key.substring(0, 1).toUpperCase() + key.substring(1);
                Method m = methods.get(methodName);
                System.out.println(methodName + " :" + oValue + " ... " + System.currentTimeMillis());
                try {
                    if (m != null) {
                        m.invoke(bObject, oValue);
                    } else {
                        System.out.println("method does not exist");
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