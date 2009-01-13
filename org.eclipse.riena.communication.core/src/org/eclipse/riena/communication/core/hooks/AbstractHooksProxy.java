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
package org.eclipse.riena.communication.core.hooks;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.security.auth.Subject;

public abstract class AbstractHooksProxy implements InvocationHandler {

	private Object proxiedInstance;
	private HashMap<String, List<Method>> methodTable = new HashMap<String, List<Method>>();

	public AbstractHooksProxy(Object proxiedInstance) {
		this.proxiedInstance = proxiedInstance;

		Method[] methods = proxiedInstance.getClass().getMethods();

		for (Method method : methods) {

			Class<?>[] param = method.getParameterTypes();
			String computedMethodName = computeMethodName(method, param.length);
			List<Method> mList = methodTable.get(computedMethodName);
			if (mList == null) {
				mList = new ArrayList<Method>();
				methodTable.put(computedMethodName, mList);
			}
			mList.add(method);
		}
	}

	private String computeMethodName(Method method, int paramSize) {
		return method.getName() + "__" + paramSize; //$NON-NLS-1$
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// Class<?>[] clazzParm;
		// if (args == null) {
		// clazzParm = new Class[0];
		// } else {
		// clazzParm = new Class[args.length];
		// for (int i = 0; i < args.length; i++) {
		// clazzParm[i] = args[i].getClass();
		// }
		// }

		String computedMethodName;
		if (args != null) {
			computedMethodName = computeMethodName(method, args.length);
		} else {
			computedMethodName = computeMethodName(method, 0);
		}
		List<Method> mList = methodTable.get(computedMethodName);
		Method proxyMethod = null;
		if (mList.size() == 1) {
			proxyMethod = mList.get(0);
		} else {
			for (Method tmpMethod : mList) {
				Class<?>[] paramTypes = tmpMethod.getParameterTypes();
				int i = 0;
				boolean found = true;
				for (Class<?> param : paramTypes) {
					if (!param.isAssignableFrom(args[i].getClass())) {
						found = false;
						break;
					}
				}
				if (found) {
					proxyMethod = tmpMethod;
					break;
				}
			}
		}
		if (proxyMethod == null) {
			throw new NoSuchMethodException(proxiedInstance + " " + method.getName() + "," + Arrays.toString(args)); //$NON-NLS-1$ //$NON-NLS-2$
		}
		Subject subject = getSubject();
		if (subject == null) {
			return proxyMethod.invoke(proxiedInstance, args);
		} else {
			MySecurityAction myAction = new MySecurityAction(proxyMethod, proxiedInstance, args);
			try {
				Subject.doAsPrivileged(subject, myAction, null);
			} catch (PrivilegedActionException pae) {
				Throwable cause = pae.getCause();
				if (cause instanceof InvocationTargetException) {
					cause = ((InvocationTargetException) cause).getTargetException();
				}
				throw cause;
			}
			return myAction.getResult();
		}
	}

	protected Object getProxiedInstance() {
		return proxiedInstance;
	}

	public abstract Subject getSubject();

}

class MySecurityAction implements PrivilegedExceptionAction<Object> {

	private Method proxyMethod;
	private Object proxiedInstance;
	private Object[] args;
	private Object result;

	MySecurityAction(Method proxyMethod, Object proxiedInstance, Object[] args) {
		this.proxyMethod = proxyMethod;
		this.proxiedInstance = proxiedInstance;
		this.args = args;
	}

	public Object run() throws Exception {
		result = proxyMethod.invoke(proxiedInstance, args);
		return result;
	}

	public Object getResult() {
		return result;
	}
}
