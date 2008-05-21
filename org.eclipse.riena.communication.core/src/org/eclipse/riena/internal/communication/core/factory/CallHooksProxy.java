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
package org.eclipse.riena.internal.communication.core.factory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;

import javax.security.auth.Subject;

import org.eclipse.riena.communication.core.RemoteFailure;
import org.eclipse.riena.communication.core.RemoteServiceDescription;
import org.eclipse.riena.communication.core.hooks.AbstractHooksProxy;
import org.eclipse.riena.communication.core.hooks.CallContext;
import org.eclipse.riena.communication.core.hooks.ICallHook;
import org.eclipse.riena.communication.core.hooks.ICallMessageContext;
import org.eclipse.riena.communication.core.hooks.ICallMessageContextAccessor;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.communication.core.Activator;

public class CallHooksProxy extends AbstractHooksProxy {

	private HashSet<ICallHook> callHooks = new HashSet<ICallHook>();
	private RemoteServiceDescription rsd;
	private ICallMessageContextAccessor mca;

	public CallHooksProxy(Object proxiedInstance) {
		super(proxiedInstance);
		Inject.service(ICallHook.class.getName()).into(this).andStart(Activator.getDefault().getContext());
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		ICallMessageContext mc = null;
		if (mca != null) {
			mc = mca.createMessageContext(getProxiedInstance());
		}

		CallContext context = null;
		// only create context (might be expensive), if you have callHooks
		if (callHooks.size() > 0) {
			context = new CallContext(rsd, method.getName(), mc);

			// call before service hook
			for (ICallHook sHook : callHooks) {
				sHook.beforeCall(context);
			}
		}

		try {
			return super.invoke(proxy, method, args);
		} catch (InvocationTargetException e) {
			throw new RemoteFailure("error while invoke remote service", e.getTargetException()); //$NON-NLS-1$
		} finally {
			// context might be null and callHooks were injected during invoke
			if (context != null) {
				for (ICallHook sHook : callHooks) {
					sHook.afterCall(context);
				}
			}
		}
	}

	public void bind(ICallHook serviceHook) {
		callHooks.add(serviceHook);
	}

	public void unbind(ICallHook serviceHook) {
		callHooks.remove(serviceHook);
	}

	public Object getCallProxy() {
		return getProxiedInstance();
	}

	public void setRemoteServiceDescription(RemoteServiceDescription rsd) {
		this.rsd = rsd;
	}

	public void setMessageContextAccessor(ICallMessageContextAccessor mca) {
		this.mca = mca;
	}

	public Subject getSubject() {
		return null;
	}
}
