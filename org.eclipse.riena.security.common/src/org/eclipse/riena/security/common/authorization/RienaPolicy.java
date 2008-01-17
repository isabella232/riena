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
package org.eclipse.riena.security.common.authorization;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.Principal;
import java.security.ProtectionDomain;

import org.eclipse.riena.core.service.ServiceInjector;
import org.eclipse.riena.internal.security.common.Activator;

import sun.security.provider.PolicyFile;

/**
 * 
 */
public class RienaPolicy extends Policy {

	private static Policy defaultPolicy;
	private IPermissionCache permCache;

	public RienaPolicy() {
		super();
		new ServiceInjector(Activator.getContext(), IPermissionCache.ID, this, "bindPermCache", "unbindPermCache").start();
	}

	public void bindPermCache(IPermissionCache permCache) {
		this.permCache = permCache;
	}

	public void unbindPermCache(IPermissionCache permCache) {
		if (permCache == this.permCache) {
			this.permCache = null;
		}
	}

	/**
	 * 
	 */
	public static void init() {
		RienaPolicy rp = new RienaPolicy();
		Policy.setPolicy(rp);
		defaultPolicy = new PolicyFile();
		if (true)
			return;
		try {
			Class<?> clz = RienaPolicy.class.getClassLoader().getSystemClassLoader().loadClass("org.eclipse.riena.security.common.policyproxy.PolicyProxy");
			Method method = clz.getMethod("setRealPolicy", Policy.class);
			method.invoke(clz, rp);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// PolicyProxy.setRealPolicy(rp);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.security.Policy#getPermissions(java.security.CodeSource)
	 */
	@Override
	public PermissionCollection getPermissions(CodeSource codesource) {
		System.out.println("rienapolicy: codesource: getPermissions codesource=" + codesource.getLocation());
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.security.Policy#refresh()
	 */
	@Override
	public void refresh() {
		System.out.println("rienapolicy: refresh");
	}

	@Override
	public PermissionCollection getPermissions(ProtectionDomain domain) {
		System.out.println("rienapolicy: domain: getPermissions domain=" + domain.getCodeSource().getLocation());
		return super.getPermissions(domain);
	}

	@Override
	public boolean implies(ProtectionDomain domain, Permission permission) {
		System.out.print("(Y)");
		if (/* permission instanceof AuthPermission && */domain.getCodeSource().getLocation().toString().contains("/org.eclipse.riena.security.common/")) {
			return true;
		}

		// this branch is entered if there is no principal set
		if (domain.getPrincipals() == null || domain.getPrincipals().length == 0) {
			boolean result = defaultPolicy.implies(domain, permission);
			if (!result) {
				System.err.println("no right to do " + permission + " for " + domain.getCodeSource().getLocation() + " no principal");
			}
			return result;
		}

		// this branch is entered if there is at least one principal
		System.out.print("rienapolicy: implies ");
		for (Principal p : domain.getPrincipals()) {
			System.out.print(p);
		}
		System.out.println(" " + permission);
		boolean result;
		if (permCache == null) {
			result = defaultPolicy.implies(domain, permission);
		} else {
			Permissions perms = permCache.getPermissions(domain.getPrincipals());
			if (perms != null) {
				result = perms.implies(permission);
			} else {
				result = false;
			}
		}
		if (!result) {
			System.err.println("no right to do " + permission + " for " + domain.getCodeSource().getLocation() + " with principal");
		}
		return result;
	}
}
