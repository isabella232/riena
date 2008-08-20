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
package org.eclipse.riena.internal.communication.publisher.hessian;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.riena.communication.core.hooks.IServiceMessageContext;

public class MessageContext implements IServiceMessageContext {

	private HttpServletRequest httpReq;
	private HttpServletResponse httpRes;

	public MessageContext(HttpServletRequest httpReq, HttpServletResponse httpRes) {
		this.httpReq = httpReq;
		this.httpRes = httpRes;
	}

	public List<String> getRequestHeaderValue(String name) {
		Enumeration<String> enumeration = httpReq.getHeaders(name);
		ArrayList<String> hValues = new ArrayList<String>();
		while (enumeration.hasMoreElements()) {
			String value = enumeration.nextElement();
			hValues.add(value);
		}
		return hValues;
	}

	public Map<String, List<String>> listRequestHeaders() {
		Map<String, List<String>> headers = new HashMap<String, List<String>>();
		Enumeration<String> enumHeaders = httpReq.getHeaderNames();
		while (enumHeaders.hasMoreElements()) {
			String name = enumHeaders.nextElement();
			Enumeration<String> enumeration = httpReq.getHeaders(name);
			ArrayList<String> hValues = new ArrayList<String>();
			headers.put(name, hValues);
			while (enumeration.hasMoreElements()) {
				String value = enumeration.nextElement();
				hValues.add(value);
			}
		}
		return headers;
	}

	public void addResponseHeader(String name, String value) {
		httpRes.addHeader(name, value);
	}

}
