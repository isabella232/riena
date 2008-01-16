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
package org.eclipse.riena.core.extension.util;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;

/**
 * ExtensionUtility maps Extensions to Interfaces. Extension properties can then be read by access the getters in the
 * interface definition. ExtensionUtility instantiates generic proxies to do this.
 * 
 * The ExtensionUtility does not know the schema so it can only trust that the extension and the interface match.
 * 
 */
public class ExtensionUtility {

    /**
     * Static method to read extensions
     * 
     * @param extensionPoint
     * @param interf
     * @return
     */
    public static <T> T[] readExtensions(String extensionPoint, Class<T> interf) {
        IExtensionRegistry registry = RegistryFactory.getRegistry();
        IExtensionPoint extPoint = registry.getExtensionPoint(extensionPoint);
        if (extPoint == null) {
            return null;
        }
        IExtension[] cfgExtensions = extPoint.getExtensions();
        if (cfgExtensions.length == 0) {
            return null;
        }
        T[] objects = (T[]) Array.newInstance(interf, cfgExtensions.length);
        int i = 0;
        for (IExtension cfgExt : cfgExtensions) {
            Array.set(objects, i++, Proxy.newProxyInstance(interf.getClassLoader(), new Class[] { interf }, new InterfaceInvocationHandler(cfgExt
                    .getConfigurationElements()[0], interf)));
        }
        return objects;
    }

    /**
     * InvocationHandler for managing proxies for the extension to interface proxy mapping
     * 
     */
    private static class InterfaceInvocationHandler implements InvocationHandler {

        private IConfigurationElement config;
        private Class<?> interf;

        InterfaceInvocationHandler(IConfigurationElement config, Class<?> interf) {
            this.config = config;
            this.interf = interf;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().startsWith("get")) {
                Method[] meths = interf.getMethods();
                for (Method meth : meths) {
                    if (meth.getName().equals(method.getName())) {
                        Class<?> retType = meth.getReturnType();
                        String name = method.getName().substring("get".length());
                        name = name.substring(0, 1).toLowerCase() + name.substring(1);
                        if (retType.equals(String.class)) {
                            return config.getAttribute(name);
                        } else if (retType.equals(Boolean.TYPE)) {
                            return new Boolean(config.getAttribute(name));
                        } else if (retType.isInterface()) {
                            IConfigurationElement cfgElement = config.getChildren(name)[0];
                            if (cfgElement == null) {
                                return null;
                            }
                            return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { retType }, new InterfaceInvocationHandler(cfgElement,
                                    retType));
                        }
                    }
                }
                throw new UnsupportedOperationException("property for method " + method.getName() + " not found.");
            }
            if (method.getName().startsWith("create")) {
                String name = method.getName().substring("create".length());
                name = name.substring(0, 1).toLowerCase() + name.substring(1);
                return config.createExecutableExtension(name);
            }
            throw new UnsupportedOperationException("only getXXX or createXXX is supported");
        }
    }

}
