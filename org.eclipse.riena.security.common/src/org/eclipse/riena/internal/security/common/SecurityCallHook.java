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
package org.eclipse.riena.internal.security.common;

import java.util.Map;

import org.eclipse.riena.communication.core.hooks.CallContext;
import org.eclipse.riena.communication.core.hooks.ICallHook;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.security.common.session.ISessionHolder;
import org.eclipse.riena.security.common.session.ISessionHolderService;
import org.eclipse.riena.security.common.session.Session;

/**
 * This Call Hook deals with security issues of a webservice calls, it sets the
 * cookie of the session and principal location before the call and checks for
 * set-cookies after the call returns.
 * 
 */
public class SecurityCallHook implements ICallHook {

	private static final String SSOID = "x-compeople-ssoid";
	private ISessionHolderService shService;

	public SecurityCallHook() {
		super();
		Inject.service(ISessionHolderService.class.getName()).useRanking().into(this).andStart(Activator.getContext());
	}

	public void bind(ISessionHolderService shService) {
		this.shService = shService;
	}

	public void unbind(ISessionHolderService shService) {
		if (this.shService == shService) {
			this.shService = null;
		}
	}

	/**
	 * @see de.compeople.spirit.communication.base.hook.ICallHook#beforeCall(de.compeople.spirit.communication.base.hook.ICallContext)
	 */
	public void beforeCall(CallContext callback) {
		ISessionHolder sessionHolder = shService.fetchSessionHolder();
		Session session = sessionHolder.getSession();

		if (session != null) {
			callback.setCookie(SSOID, session.getSessionId());
		}
	}

	/**
	 * @see de.compeople.spirit.communication.base.hook.ICallHook#afterCall(de.compeople.spirit.communication.base.hook.ICallContext)
	 */
	public void afterCall(CallContext callback) {
		ISessionHolder sessionHolder = shService.fetchSessionHolder();
		Map<String, String> map = callback.getSetCookies();
		if (map == null) {
			return;
		}
		String temp = (String) map.get(SSOID);
		if (temp != null) {
			sessionHolder.setSession(new Session(temp));
		}
	}
}