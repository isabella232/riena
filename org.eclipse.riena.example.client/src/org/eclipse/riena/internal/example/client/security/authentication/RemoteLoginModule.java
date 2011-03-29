/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.example.client.security.authentication;

import java.io.IOException;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.internal.example.client.Activator;
import org.eclipse.riena.security.common.authentication.RemoteLoginProxy;

/**
 * Test module that implements the JAAS LoginModule interface
 * 
 */
public class RemoteLoginModule implements LoginModule {

	private final static Logger LOGGER = Log4r.getLogger(Activator.getDefault(), RemoteLoginModule.class);

	private CallbackHandler callbackHandler;

	// AuthenticationTicket ticket;
	private RemoteLoginProxy remoteLoginProxy;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.security.auth.spi.LoginModule#abort()
	 */
	public boolean abort() throws LoginException {
		LOGGER.log(LogService.LOG_DEBUG, "abort"); //$NON-NLS-1$
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.security.auth.spi.LoginModule#commit()
	 */
	public boolean commit() throws LoginException {
		LOGGER.log(LogService.LOG_DEBUG, "commit"); //$NON-NLS-1$
		return remoteLoginProxy.commit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.security.auth.spi.LoginModule#initialize(javax.security.auth.Subject
	 * , javax.security.auth.callback.CallbackHandler, java.util.Map,
	 * java.util.Map)
	 */
	public void initialize(final Subject subject, final CallbackHandler callbackHandler,
			final Map<String, ?> sharedState, final Map<String, ?> options) {
		if (callbackHandler == null) {
			LOGGER.log(LogService.LOG_ERROR, "callbackhandler cant be null"); //$NON-NLS-1$
			throw new RuntimeException("callbackhandler cant be null"); //$NON-NLS-1$
		}
		LOGGER.log(LogService.LOG_DEBUG, "initialize"); //$NON-NLS-1$
		this.callbackHandler = callbackHandler;
		this.remoteLoginProxy = new RemoteLoginProxy("CentralSecurity", subject); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.security.auth.spi.LoginModule#login()
	 */
	public boolean login() throws LoginException {
		LOGGER.log(LogService.LOG_DEBUG, "login"); //$NON-NLS-1$
		final Callback[] callbacks = new Callback[2];
		callbacks[0] = new NameCallback("username: "); //$NON-NLS-1$
		callbacks[1] = new PasswordCallback("password: ", false); //$NON-NLS-1$
		try {
			callbackHandler.handle(callbacks);
			return remoteLoginProxy.login(callbacks);
		} catch (final IOException e) {
			e.printStackTrace();
			return false;
		} catch (final UnsupportedCallbackException e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.security.auth.spi.LoginModule#logout()
	 */
	public boolean logout() throws LoginException {
		LOGGER.log(LogService.LOG_DEBUG, "logout");//$NON-NLS-1$
		return remoteLoginProxy.logout();
	}
}
