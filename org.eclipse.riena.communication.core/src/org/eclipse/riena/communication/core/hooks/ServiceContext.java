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
package org.eclipse.riena.communication.core.hooks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.Cookie;

import org.eclipse.riena.communication.core.RemoteServiceDescription;

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

	public String getInterfaceName() {
		return rsd.getServiceInterfaceClassName();
	}

	public String getMethodName() {
		return method;
	}

	public void setProperty(String name, Object value) {
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		properties.put(name, value);
		return;
	}

	public Object getProperty(String name) {
		if (properties == null) {
			return null;
		}
		return properties.get(name);
	}

	public IServiceMessageContext getMessageContext() {
		return messageContext;
	}

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

	public void addCookie(Cookie cookie) {
		messageContext.addResponseHeader("Set-Cookie", cookie.getName() + "=" + cookie.getValue()); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
