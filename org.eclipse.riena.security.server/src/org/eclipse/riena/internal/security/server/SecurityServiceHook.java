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
package org.eclipse.riena.internal.security.server;

import java.security.Principal;
import java.util.Arrays;

import javax.security.auth.Subject;
import javax.servlet.http.Cookie;

import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.communication.core.hooks.IServiceHook;
import org.eclipse.riena.communication.core.hooks.ServiceContext;
import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.cache.IGenericObjectCache;
import org.eclipse.riena.security.common.ISubjectHolder;
import org.eclipse.riena.security.common.NotAuthorizedFailure;
import org.eclipse.riena.security.common.session.ISessionHolder;
import org.eclipse.riena.security.common.session.Session;
import org.eclipse.riena.security.server.session.ISessionService;

/**
 * This Service Hook deals with security issues of a web-service invocation. It
 * reads the cookies and set SessionHolder, PrincipalLocationHolder and
 * PrincipalHolder. It also sets "Set-Cookie" on return, when the session has
 * changed.
 * 
 */
public class SecurityServiceHook implements IServiceHook {

	/** <code>SESSIONID</code> */
	public static final String SESSIONID = "ssoid"; //$NON-NLS-1$
	/** <code>SSOID</code> used as Cookie name for the ssoid */
	public final static String SSOID = "x-compeople-ssoid"; //$NON-NLS-1$
	/**
	 * <code>PRINCIPAL</code> the name of the id, under which the principal is
	 * stored in the current messagecontext *
	 */
	public static final String PRINCIPAL = "principal"; //$NON-NLS-1$
	/** <code>SET_SESSION</code> */
	public static final String SET_SESSION = "set-ssoid"; //$NON-NLS-1$

	private static final String RIENA_SECURE_WEBSERVICES_PROPERTY = "riena.secure.webservices"; //$NON-NLS-1$
	// private static final String UNSECURE_WEBSERVICES_ID =
	// "riena.security.server.UnsecureWebservices";

	private IGenericObjectCache<String, Principal[]> principalCache;
	private ISessionService sessionService;
	private ISubjectHolder subjectHolder;
	private ISessionHolder sessionHolder;

	// private HashMap<String, Boolean> freeHivemindWebservices = new
	// HashMap<String, Boolean>();
	private final boolean requiresSSOIDbyDefault = false;

	private static final Logger LOGGER = Log4r.getLogger(Activator.getDefault(), SecurityServiceHook.class);

	/**
	 * 
	 */
	public SecurityServiceHook() {
		super();

		// List<UnsecureWebservice> tempList =
		// RegistryAccessor.fetchRegistry().getConfiguration(
		// UNSECURE_WEBSERVICES_ID);
		final String appName = "???appname??????";// RuntimeInfo.getApplicationName(); //$NON-NLS-1$
		//		if (appName == null) {
		//			appName = "<unknown>"; //$NON-NLS-1$
		//		}
		// if (tempList.size() == 0) {
		// LOGGER.log(LogService.LOG_INFO, appName +" : no unsecureWebservices
		// defined");
		// }
		// for (int i = 0; i < tempList.size(); i++) {
		// UnsecureWebservice freeWS = tempList.get(i);
		// freeHivemindWebservices.put(freeWS.getServiceId(), Boolean.TRUE);
		// if (freeWS.getServiceId().equals("*")) {
		// requiresSSOIDbyDefault = false;
		// }
		// if (freeWS.getServiceId().equals("*")) {
		// LOGGER.log(LogService.LOG_INFO, appName
		// +" : defining ALL WEBSERVICES in this Webapp as unsecure (SSOID is
		// not required). definition * for
		// UnsecureServices found.");
		// } else {
		// LOGGER.log(LogService.LOG_INFO, appName + ": defining a Webservice "
		// + freeWS.getServiceId() +" as unsecure
		// (SSOID not required).");
		// }
		// }

		// String preferenceValue =
		// PreferencesAccessor.fetchPreferences().getSystemPreference(
		// "spirit.security.server.UnsecureWebservices").getString("All");
		// if (preferenceValue.equalsIgnoreCase("true")) {
		// requiresSSOIDbyDefault = false;
		// LOGGER.log(LogService.LOG_INFO," ALL WEBSERVICES are defined as
		// unsecure in webapp:" + appName
		// +" using SystemPreference
		// spirit.security.server.UnsecureWebservices.");
		// }

		if (!requiresSSOIDbyDefault) {
			LOGGER.log(LogService.LOG_INFO, appName
					+ ": defining ALL WEBSERVICES in this Webapp as unsecure (SSOID is not required)."); //$NON-NLS-1$
		}
	}

	public void bind(final IGenericObjectCache<String, Principal[]> principalCache) {
		this.principalCache = principalCache;
	}

	public void unbind(final IGenericObjectCache<String, Principal[]> principalCache) {
		if (this.principalCache == principalCache) {
			this.principalCache = null;
		}
	}

	public void bind(final ISessionService sessionService) {
		this.sessionService = sessionService;
	}

	public void unbind(final ISessionService sessionService) {
		if (this.sessionService == sessionService) {
			this.sessionService = null;
		}
	}

	public void bind(final ISubjectHolder subjectHolder) {
		this.subjectHolder = subjectHolder;
	}

	public void unbind(final ISubjectHolder subjectHolder) {
		if (this.subjectHolder == subjectHolder) {
			this.subjectHolder = null;
		}
	}

	public void bind(final ISessionHolder sessionHolder) {
		this.sessionHolder = sessionHolder;
	}

	public void unbind(final ISessionHolder sessionHolder) {
		if (this.sessionHolder == sessionHolder) {
			this.sessionHolder = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.hooks.IServiceHook#beforeService
	 * (org.eclipse.riena.communication.core.hooks.ServiceContext)
	 */
	public void beforeService(final ServiceContext callback) {
		final boolean requiresSSOID = requiresSSOIDbyDefault;
		// if (freeHivemindWebservices.get(callback.getComponentId()) != null) {
		// requiresSSOID = false;
		// } else {
		// if (callback.isUnsecure()) {
		// requiresSSOID = false;
		// }
		// }

		// first extract the cookies
		final Cookie[] cookies = callback.getCookies();
		String ssoid = null;
		if (cookies != null) {
			for (final Cookie cookie : cookies) {
				if (cookie.getName().equals(SSOID)) {
					ssoid = cookie.getValue();
				}
			}
		}

		if (ssoid != null && ssoid.length() == 0) {
			ssoid = null;
		}

		LOGGER.log(LogService.LOG_DEBUG, "before Service ssoid = " + ssoid); //$NON-NLS-1$

		if (ssoid == null && requiresSSOID) {
			LOGGER.log(LogService.LOG_ERROR, "error in call to webservice {" + callback.getInterfaceName() //$NON-NLS-1$
					+ "} since it is not in the list of webservices that do not require a session but SSOID=null !!!"); //$NON-NLS-1$
			if (Boolean.valueOf(System.getProperty(RIENA_SECURE_WEBSERVICES_PROPERTY, Boolean.TRUE.toString()))) {
				throw new NotAuthorizedFailure("call to webservice " + callback.getInterfaceName() //$NON-NLS-1$
						+ " failed, no valid session was given but is required."); //$NON-NLS-1$
			}
		}

		// check the ssoid in the session service potentially with a webservice
		// call
		// note: ssoid and plid are not set
		if (ssoid != null) {
			Principal[] principals = principalCache.get(ssoid);
			if (principals == null) {
				principals = sessionService.findPrincipals(new Session(ssoid));
				LOGGER.log(LogService.LOG_DEBUG, "sessionService found principal = " + Arrays.toString(principals)); //$NON-NLS-1$
				if (principals == null && requiresSSOID) {
					LOGGER.log(LogService.LOG_ERROR, "ssoid {" + ssoid //$NON-NLS-1$
							+ "} found in request but SessionService could not find a Principal."); //$NON-NLS-1$
					throw new NotAuthorizedFailure("call to webservice with invalid ssoid"); //$NON-NLS-1$
				}
				if (principals != null) {
					principalCache.put(ssoid, principals);
				}
			} else {
				LOGGER.log(LogService.LOG_DEBUG, "found principal in cache = " + Arrays.toString(principals)); //$NON-NLS-1$
			}
			if (principals != null) {
				final Subject subject = new Subject();
				for (final Principal p : principals) {
					subject.getPrincipals().add(p);
				}
				subjectHolder.setSubject(subject);
				callback.setProperty("riena.subject", subject); //$NON-NLS-1$
			}
		}

		// set ssoid and plid in the sessionholder and the ssoid as attribute
		if (ssoid != null) {
			final Session beforeSession = new Session(ssoid);
			sessionHolder.setSession(beforeSession);
			callback.setProperty("de.compeople.ssoid", beforeSession); //$NON-NLS-1$
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.communication.core.hooks.IServiceHook#afterService(
	 * org.eclipse.riena.communication.core.hooks.ServiceContext)
	 */
	public void afterService(final ServiceContext context) {
		final Session afterSession = sessionHolder.getSession();
		final Session beforeSession = (Session) context.getProperty("de.compeople.ssoid"); //$NON-NLS-1$
		String ssoid = null;
		if (afterSession != null) {
			ssoid = afterSession.getSessionId();
		}
		if (beforeSession != null) {
			LOGGER.log(LogService.LOG_DEBUG, "afterService after_ssoid=" + ssoid + " before_ssoid=" //$NON-NLS-1$ //$NON-NLS-2$
					+ beforeSession.getSessionId());
		}
		LOGGER.log(LogService.LOG_DEBUG, "afterService compare session instance before=" + beforeSession + " after=" //$NON-NLS-1$ //$NON-NLS-2$
				+ afterSession);
		if (beforeSession != afterSession
				|| (beforeSession != null && afterSession != null && !(beforeSession.getSessionId().equals(ssoid)))) {
			if (ssoid == null || ssoid.equals("0")) { //$NON-NLS-1$
				// delete cookie
				final Cookie cookie = new Cookie(SSOID, ""); //$NON-NLS-1$
				cookie.setPath("/"); //$NON-NLS-1$
				context.addCookie(cookie);
				LOGGER.log(LogService.LOG_DEBUG, "setting cookie to '0'"); //$NON-NLS-1$
			} else {
				final Cookie cookie = new Cookie(SSOID, ssoid);
				cookie.setPath("/"); //$NON-NLS-1$
				context.addCookie(cookie);
				if (beforeSession != null && !(beforeSession.getSessionId().equals("0"))) { //$NON-NLS-1$
					LOGGER.log(LogService.LOG_WARNING, "CHANGING cookie setting from '" + beforeSession.getSessionId() //$NON-NLS-1$
							+ "' to '" + ssoid + "'"); //$NON-NLS-1$ //$NON-NLS-2$
				} else {
					LOGGER.log(LogService.LOG_DEBUG, "setting cookie to '" + ssoid + "'"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
		} else {
			LOGGER.log(LogService.LOG_DEBUG, "doing nothing in afterService"); //$NON-NLS-1$
		}

		sessionHolder.setSession(null);
		subjectHolder.setSubject(null);
	}
}
