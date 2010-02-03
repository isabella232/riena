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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.Cookie;

import org.eclipse.riena.communication.core.RemoteServiceDescription;

/**
 * Allows a ServiceHook to access information associated with the current remote
 * service call
 */
public class ServiceContext {

	private RemoteServiceDescription rsd;
	private String method;
	private HashMap<String, Object> properties;
	private IServiceMessageContext messageContext;

	public ServiceContext(RemoteServiceDescription rsd, String method, IServiceMessageContext messageContext) {
		this.rsd = rsd;
		this.method = method;
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
		return method;
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
		String methodName = getMethodName();
		Method[] methods = interf.getMethods();
		Method foundMethod = null;
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				if (foundMethod == null) {
					foundMethod = method;
				} else {
					throw new UnsupportedOperationException("More than one method with the same name '" + methodName
							+ "' found.");
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
	public void setProperty(String name, Object value) {
		if (properties == null) {
			properties = new HashMap<String, Object>();
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
	public Object getProperty(String name) {
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
	public IServiceMessageContext getMessageContext() {
		return messageContext;
	}

	/**
	 * Accessor to read cookies that where transferred as part of the remote
	 * service call. Currently used for session information.
	 * 
	 * @return
	 */
	public Cookie[] getCookies() {
		List<String> cookieValues = messageContext.getRequestHeaderValue("Cookie"); //$NON-NLS-1$
		if (cookieValues == null || cookieValues.size() == 0) {
			return null;
		}
		List<Cookie> cookies = new ArrayList<Cookie>();
		for (String temp : cookieValues) {
			cookies.add(new Cookie(temp.substring(0, temp.indexOf("=")), temp.substring(temp.indexOf("=") + 1))); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return cookies.toArray(new Cookie[cookies.size()]);
	}

	/**
	 * Adds a new Set-Cookie on the service side, which is sent back to the
	 * client when the remote service call returns
	 * 
	 * @param cookie
	 */
	public void addCookie(Cookie cookie) {
		messageContext.addResponseHeader("Set-Cookie", cookie.getName() + "=" + cookie.getValue()); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
