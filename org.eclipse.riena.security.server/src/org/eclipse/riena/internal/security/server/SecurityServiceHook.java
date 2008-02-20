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
package org.eclipse.riena.internal.security.server;

import java.security.Principal;
import java.util.Arrays;

import javax.security.auth.Subject;
import javax.servlet.http.Cookie;

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.communication.core.hooks.IServiceHook;
import org.eclipse.riena.communication.core.hooks.ServiceContext;
import org.eclipse.riena.core.cache.IGenericObjectCache;
import org.eclipse.riena.core.service.ServiceId;
import org.eclipse.riena.security.common.ISubjectHolderService;
import org.eclipse.riena.security.common.NotAuthorizedFailure;
import org.eclipse.riena.security.common.session.ISessionHolderService;
import org.eclipse.riena.security.common.session.Session;
import org.eclipse.riena.security.server.session.ISessionService;
import org.osgi.service.log.LogService;

/**
 * This Service Hook deals with security issues of a webservice invocation. It
 * reads the cookies and set SessionHolder, PrincipalLocationHolder and
 * PrincipalHolder. It also sets "Set-Cookie" on return, when the session has
 * changed.
 * 
 */
public class SecurityServiceHook implements IServiceHook {

	/** <code>SESSIONID</code> */
	public static final String SESSIONID = "ssoid";
	/** <code>SSOID</code> used as Cookie name for the ssoid */
	public final static String SSOID = "x-compeople-ssoid";
	/**
	 * <code>PRINCIPAL</code> the name of the id, under which the principal is
	 * stored in the current messagecontext *
	 */
	public static final String PRINCIPAL = "principal";
	/** <code>SET_SESSION</code> */
	public static final String SET_SESSION = "set-ssoid";

	// private static final String UNSECURE_WEBSERVICES_ID =
	// "spirit.security.server.UnsecureWebservices";

	private IGenericObjectCache principalCache;
	private ISessionService sessionService;
	private ISubjectHolderService subjectHolderService;
	private ISessionHolderService sessionHolderService;

	// private HashMap<String, Boolean> freeHivemindWebservices = new
	// HashMap<String, Boolean>();
	private boolean requiresSSOIDbyDefault = false;

	private static final Logger LOGGER = Activator.getDefault().getLogger(SecurityServiceHook.class.getName());

	/**
	 * 
	 */
	public SecurityServiceHook() {
		super();
		new ServiceId(IGenericObjectCache.ID).useFilter("(cache.type=PrincipalCache)").injectInto(this).andStart(
				Activator.getContext());
		new ServiceId(ISessionService.ID).injectInto(this).andStart(Activator.getContext());
		new ServiceId(ISubjectHolderService.ID).injectInto(this).andStart(Activator.getContext());
		new ServiceId(ISessionHolderService.ID).injectInto(this).andStart(Activator.getContext());

		// List<UnsecureWebservice> tempList =
		// RegistryAccessor.fetchRegistry().getConfiguration(UNSECURE_WEBSERVICES_ID);
		String appName = "???appname??????";// RuntimeInfo.getApplicationName();
		if (appName == null) {
			appName = "<unknown>";
		}
		// if (tempList.size() == 0) {
		// LOGGER.log(LogService.LOG_INFO, appName + ": no unsecureWebservices
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
		// + ": defining ALL WEBSERVICES in this Webapp as unsecure (SSOID is
		// not required). definition * for
		// UnsecureServices found.");
		// } else {
		// LOGGER.log(LogService.LOG_INFO, appName + ": defining a Webservice "
		// + freeWS.getServiceId() + " as unsecure
		// (SSOID not required).");
		// }
		// }

		// String preferenceValue =
		// PreferencesAccessor.fetchPreferences().getSystemPreference("spirit.security.server.UnsecureWebservices").getString("All");
		// if (preferenceValue.equalsIgnoreCase("true")) {
		// requiresSSOIDbyDefault = false;
		// LOGGER.log(LogService.LOG_INFO, "ALL WEBSERVICES are defined as
		// unsecure in webapp:" + appName
		// + " using SystemPreference
		// spirit.security.server.UnsecureWebservices.");
		// }

		if (!requiresSSOIDbyDefault) {
			LOGGER.log(LogService.LOG_INFO, appName
					+ ": defining ALL WEBSERVICES in this Webapp as unsecure (SSOID is not required).");
		}
	}

	public void bind(IGenericObjectCache principalCache) {
		this.principalCache = principalCache;
	}

	public void unbind(IGenericObjectCache principalCache) {
		if (this.principalCache == principalCache) {
			this.principalCache = null;
		}
	}

	public void bind(ISessionService sessionService) {
		this.sessionService = sessionService;
	}

	public void unbind(ISessionService sessionService) {
		if (this.sessionService == sessionService) {
			this.sessionService = null;
		}
	}

	public void bind(ISubjectHolderService subjectHolderService) {
		this.subjectHolderService = subjectHolderService;
	}

	public void unbind(ISubjectHolderService subjectHolderService) {
		if (this.subjectHolderService == subjectHolderService) {
			this.subjectHolderService = null;
		}
	}

	public void bind(ISessionHolderService ISessionHolderService) {
		this.sessionHolderService = ISessionHolderService;
	}

	public void unbind(ISessionHolderService ISessionHolderService) {
		if (this.sessionHolderService == ISessionHolderService) {
			this.sessionHolderService = null;
		}
	}

	/**
	 * @see de.compeople.spirit.communication.server.hook.IServiceHook#beforeService(de.compeople.spirit.communication.server.hook.IServiceContext)
	 */
	public void beforeService(ServiceContext callback) {
		boolean requiresSSOID = requiresSSOIDbyDefault;
		// if (freeHivemindWebservices.get(callback.getComponentId()) != null) {
		// requiresSSOID = false;
		// } else {
		// if (callback.isUnsecure()) {
		// requiresSSOID = false;
		// }
		// }

		// first extract the cookies
		Cookie[] cookies = callback.getCookies();
		String ssoid = null;
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals(SSOID)) {
					ssoid = cookies[i].getValue();
				}
			}
		}

		if (ssoid != null && ssoid.length() == 0) {
			ssoid = null;
		}

		LOGGER.log(LogService.LOG_DEBUG, "before Service ssoid = " + ssoid);

		if (ssoid == null && requiresSSOID) {
			LOGGER.log(LogService.LOG_ERROR, "error in call to webservice {" + callback.getInterfaceName()
					+ "} since it is not in the list of webservices that do not require a session but SSOID=null !!!");
			if (System.getProperty("spirit.secure.webservices") == null
					|| System.getProperty("spirit.secure.webservices").equals("true")) {
				throw new NotAuthorizedFailure("call to webservice " + callback.getInterfaceName()
						+ " failed, no valid session was given but is required.");
			}
		}

		// check the ssoid in the session service potentially with a webservice
		// call
		// note: ssoid and plid are not set
		if (ssoid != null) {
			Principal[] principals = (Principal[]) principalCache.get(ssoid, SecurityServiceHook.class);
			if (principals == null) {
				principals = sessionService.findPrincipals(new Session(ssoid));
				LOGGER.log(LogService.LOG_DEBUG, "sessionService found principal = " + Arrays.toString(principals));
				if (principals == null && requiresSSOID) {
					LOGGER.log(LogService.LOG_ERROR, "ssoid {" + ssoid
							+ "} found in request but SessionService could not find a Principal.");
					throw new NotAuthorizedFailure("call to webservice with invalid ssoid");
				}
				if (principals != null) {
					principalCache.put(ssoid, principals);
				}
			} else {
				LOGGER.log(LogService.LOG_DEBUG, "found principal in cache = " + Arrays.toString(principals));
			}
			if (principals != null) {
				Subject subject = new Subject();
				for (Principal p : principals) {
					subject.getPrincipals().add(p);
				}
				subjectHolderService.fetchSubjectHolder().setSubject(subject);
				callback.setProperty("riena.subject", subject);
			}
		}

		// set ssoid and plid in the sessionholder and the ssoid as attribute
		if (ssoid != null) {
			Session beforeSession = new Session(ssoid);
			sessionHolderService.fetchSessionHolder().setSession(beforeSession);
			callback.setProperty("de.compeople.ssoid", beforeSession);
		}

	}

	/**
	 * @see de.compeople.spirit.communication.server.hook.IServiceHook#afterService(de.compeople.spirit.communication.server.hook.IServiceContext)
	 */
	public void afterService(ServiceContext context) {
		Session afterSession = sessionHolderService.fetchSessionHolder().getSession();
		Session beforeSession = (Session) context.getProperty("de.compeople.ssoid");
		String ssoid = null;
		if (afterSession != null) {
			ssoid = afterSession.getSessionId();
		}
		if (beforeSession != null) {
			LOGGER.log(LogService.LOG_DEBUG, "afterService after_ssoid=" + ssoid + " before_ssoid="
					+ beforeSession.getSessionId());
		}
		LOGGER.log(LogService.LOG_DEBUG, "afterService compare session instance before=" + beforeSession + " after="
				+ afterSession);
		if (beforeSession != afterSession
				|| (beforeSession != null && afterSession != null && !(beforeSession.getSessionId().equals(ssoid)))) {
			if (ssoid == null || ssoid.equals("0")) {
				// delete cookie
				Cookie cookie = new Cookie(SSOID, "");
				cookie.setPath("/");
				context.addCookie(cookie);
				LOGGER.log(LogService.LOG_DEBUG, "setting cookie to '0'");
			} else {
				Cookie cookie = new Cookie(SSOID, ssoid);
				cookie.setPath("/");
				context.addCookie(cookie);
				if (beforeSession != null && !(beforeSession.getSessionId().equals("0"))) {
					LOGGER.log(LogService.LOG_WARNING, "CHANGING cookie setting from '" + beforeSession.getSessionId()
							+ "' to '" + ssoid + "'");
				} else {
					LOGGER.log(LogService.LOG_DEBUG, "setting cookie to '" + ssoid + "'");
				}
			}
		} else {
			LOGGER.log(LogService.LOG_DEBUG, "doing nothing in afterService");
		}

		sessionHolderService.fetchSessionHolder().setSession(null);
		subjectHolderService.fetchSubjectHolder().setSubject(null);
	}
}