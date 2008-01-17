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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.riena.communication.core.RemoteServiceDescription;

public class CallContext {

	private RemoteServiceDescription rsd;
	private String method;
	private HashMap<String, String> properties;
	private ICallMessageContext messageContext;

	public CallContext(RemoteServiceDescription rsd, String method, ICallMessageContext messageContext) {
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

	public void setProperty(String name, String value) {
		if (properties == null) {
			properties = new HashMap<String, String>();
		}
		properties.put(name, value);
		return;
	}

	public String getProperty(String name) {
		if (properties == null) {
			return null;
		}
		return properties.get(name);
	}

	public ICallMessageContext getMessageContext() {
		return messageContext;
	}

	public void setCookie(String name, String value) {
		messageContext.addRequestHeader("Cookie", name + "=" + value);
		return;
	}

	public Map<String, String> getSetCookies() {
		Map<String, List<String>> respHeaders = messageContext.listResponseHeaders();
		if (respHeaders == null) {
			return null;
		}
		List<String> listSetCookies = respHeaders.get("Set-Cookie");
		if (listSetCookies == null) {
			return null;
		}
		Map<String, String> setCookies = new HashMap<String, String>();
		for (String temp : listSetCookies) {
			setCookies.put(temp.substring(0, temp.indexOf("=")), temp.substring(temp.indexOf("=") + 1));
		}
		return setCookies;
	}
}