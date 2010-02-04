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
package org.eclipse.riena.communication.core.hooks;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.riena.communication.core.RemoteServiceDescription;

/**
 * CallContext that is available in beforeCall and afterCall to all CallHooks
 */
public class CallContext {

	private RemoteServiceDescription rsd;
	private String methodName;
	private HashMap<String, String> properties;
	private ICallMessageContext messageContext;

	public CallContext(RemoteServiceDescription rsd, String methodName, ICallMessageContext messageContext) {
		this.rsd = rsd;
		this.methodName = methodName;
		this.messageContext = messageContext;
	}

	/**
	 * The name as string of the remote service interface
	 * 
	 * @return
	 */
	public String getInterfaceName() {
		return rsd.getServiceInterfaceClassName();
	}

	/**
	 * The name as class object of the remote service interface
	 * 
	 * @return
	 */
	public Class<?> getInterfaceClass() {
		return rsd.getServiceInterfaceClass();
	}

	/**
	 * The method that is called as string.
	 * 
	 * @return
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * The method that is called as Method object.
	 * 
	 * @return
	 * @throws UnsupportedOperationException
	 *             if more than one method with the method name are found. That
	 *             is currently not supported.
	 */
	public Method getMethod() {
		Class<?> interf = getInterfaceClass();
		Method[] methods = interf.getMethods();
		Method foundMethod = null;
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				if (foundMethod == null) {
					foundMethod = method;
				} else {
					throw new UnsupportedOperationException("More than one method with the same name '" + methodName //$NON-NLS-1$
							+ "' found."); //$NON-NLS-1$
				}
			}
		}
		return foundMethod;
	}

	/**
	 * Arbitrary property that can be associated with this call. Accessible in
	 * beforeService and afterService
	 * 
	 * @param name
	 *            key
	 * @param value
	 *            value for that key
	 */
	public void setProperty(String name, String value) {
		if (properties == null) {
			properties = new HashMap<String, String>();
		}
		properties.put(name, value);
		return;
	}

	/**
	 * Getter access to key/value property store in setProperty
	 * 
	 * @param name
	 * @return
	 */
	public String getProperty(String name) {
		if (properties == null) {
			return null;
		}
		return properties.get(name);
	}

	/**
	 * Access to the ServiceMessageContext that has more lowlevel access to
	 * properties
	 * 
	 * @return
	 */
	public ICallMessageContext getMessageContext() {
		return messageContext;
	}

	/**
	 * Sets a new Set-Cookie on the service side, which is sent back to the
	 * client when the remote service call returns
	 * 
	 * @param cookie
	 */
	public void setCookie(String name, String value) {
		messageContext.addRequestHeader("Cookie", name + "=" + value); //$NON-NLS-1$ //$NON-NLS-2$
		return;
	}

	/**
	 * Returns a Map of key, value pairs of Cookies that were set on the server
	 * in the last remote service call. This only returns interesting
	 * information in the afterCall method.
	 * 
	 * @return
	 */
	public Map<String, String> getSetCookies() {
		Map<String, List<String>> respHeaders = messageContext.listResponseHeaders();
		if (respHeaders == null) {
			return null;
		}
		List<String> listSetCookies = respHeaders.get("Set-Cookie"); //$NON-NLS-1$
		if (listSetCookies == null) {
			return null;
		}
		Map<String, String> setCookies = new HashMap<String, String>();
		for (String temp : listSetCookies) {
			setCookies.put(temp.substring(0, temp.indexOf("=")), temp.substring(temp.indexOf("=") + 1)); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return setCookies;
	}
}
