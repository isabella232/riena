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
package org.eclipse.riena.security.authentication.module;

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
import org.eclipse.riena.internal.tests.Activator;
import org.eclipse.riena.security.common.authentication.SimplePrincipal;

/**
 * Test module that implements the JAAS LoginModule interface
 * 
 */
public class TestLocalLoginModule implements LoginModule {

	private Subject subject;
	private CallbackHandler callbackHandler;

	private static final Logger LOGGER = Log4r.getLogger(Activator.getDefault(), TestLocalLoginModule.class);

	private String username;
	private String password;

	private static String checkedUsername;
	private static String checkedPassword;

	/**
	 * @param string
	 * @param string2
	 */
	public static void setCredentials(final String usernameParm, final String passwordParm) {
		checkedUsername = usernameParm;
		checkedPassword = passwordParm;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.security.auth.spi.LoginModule#abort()
	 */
	public boolean abort() throws LoginException {
		LOGGER.log(LogService.LOG_DEBUG, "abort");
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.security.auth.spi.LoginModule#commit()
	 */
	public boolean commit() throws LoginException {
		LOGGER.log(LogService.LOG_DEBUG, "commit");
		subject.getPrincipals().add(new SimplePrincipal(username));
		return true;
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
			LOGGER.log(LogService.LOG_ERROR, "callbackhandler cant be null");
			throw new RuntimeException("callbackhandler cant be null");
		}
		LOGGER.log(LogService.LOG_DEBUG, "initialize");
		this.subject = subject;
		this.callbackHandler = callbackHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.security.auth.spi.LoginModule#login()
	 */
	public boolean login() throws LoginException {
		LOGGER.log(LogService.LOG_DEBUG, "login");
		final Callback[] callbacks = new Callback[2];
		callbacks[0] = new NameCallback("username: ");
		callbacks[1] = new PasswordCallback("password: ", false);
		if (callbackHandler == null) {
			System.out.println("callbackhandler cant be null");
			return false;
		}
		try {
			callbackHandler.handle(callbacks);
			username = ((NameCallback) callbacks[0]).getName();
			password = new String(((PasswordCallback) callbacks[1]).getPassword());
			if (username != null && password != null) {
				if (username.equals(checkedUsername) && password.equals(checkedPassword)) {
					return true;
				}
			}

			return false;
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
		LOGGER.log(LogService.LOG_DEBUG, "logout");
		return false;
	}

}
