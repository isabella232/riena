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
package org.eclipse.riena.security.common;

import java.security.Principal;

import javax.security.auth.Subject;

import org.eclipse.riena.communication.core.hooks.CallContext;
import org.eclipse.riena.communication.core.hooks.ICallHook;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.security.common.Activator;

import sun.misc.BASE64Encoder;

/**
 * This optional hook implements authentication by
 */
public class BasicAuthenticationCallHook implements ICallHook {

	private ISubjectHolderService subjectHolderService = null;

	public BasicAuthenticationCallHook() {
		super();
		Inject.service(ISubjectHolderService.class.getName()).useRanking().into(this).andStart(
				Activator.getDefault().getContext());
	}

	public void bind(ISubjectHolderService subjectHolderService) {
		this.subjectHolderService = subjectHolderService;
	}

	public void unbind(ISubjectHolderService subjectHolderService) {
		this.subjectHolderService = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.hooks.ICallHook#afterCall(org.eclipse
	 * .riena.communication.core.hooks.CallContext)
	 */
	public void afterCall(CallContext context) {
		// no action necessary
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.hooks.ICallHook#beforeCall(org.eclipse
	 * .riena.communication.core.hooks.CallContext)
	 */
	public void beforeCall(CallContext context) {
		ISubjectHolder subjectHolder = subjectHolderService.fetchSubjectHolder();
		if (subjectHolder != null) {
			Subject subject = subjectHolder.getSubject();
			if (subject != null) {
				Object psw = subject.getPrivateCredentials().iterator().next();
				String password;
				if (psw != null && psw instanceof String) {
					password = (String) psw;
				} else {
					password = ""; //$NON-NLS-1$
				}
				for (Principal principal : subject.getPrincipals()) {
					String useridPlusPassword = principal.getName() + ":" + password; //$NON-NLS-1$
					String authorizationInBase64 = new BASE64Encoder().encode(useridPlusPassword.getBytes());
					context.getMessageContext().addRequestHeader("Authorization", "Basic " + authorizationInBase64); //$NON-NLS-1$//$NON-NLS-2$
				}
			}
		}
	}

}
