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
package org.eclipse.riena.internal.communication.core.factory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;

import javax.security.auth.Subject;

import com.caucho.hessian.client.HessianRuntimeException;
import com.caucho.hessian.io.HessianProtocolException;

import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.communication.core.RemoteFailure;
import org.eclipse.riena.communication.core.RemoteServiceDescription;
import org.eclipse.riena.communication.core.hooks.AbstractHooksProxy;
import org.eclipse.riena.communication.core.hooks.CallContext;
import org.eclipse.riena.communication.core.hooks.ICallHook;
import org.eclipse.riena.communication.core.hooks.ICallMessageContext;
import org.eclipse.riena.communication.core.hooks.ICallMessageContextAccessor;
import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.wire.InjectService;
import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.internal.communication.core.Activator;

public class CallHooksProxy extends AbstractHooksProxy {

	private final HashSet<ICallHook> callHooks = new HashSet<ICallHook>();
	private RemoteServiceDescription rsd;
	private ICallMessageContextAccessor mca;
	private final static Logger LOGGER = Log4r.getLogger(Activator.getDefault(), CallHooksProxy.class);

	public CallHooksProxy(final Object proxiedInstance) {
		super(proxiedInstance);
		Wire.instance(this).andStart(Activator.getDefault().getContext());
	}

	@Override
	public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
		ICallMessageContext mc = null;
		if (mca != null) {
			mc = mca.createMessageContext(getProxiedInstance(), method.getName(), null);
		}

		final CallContext context = new CallContext(rsd, method.getName(), mc);
		if (callHooks.size() > 0) {
			// call before service hook
			for (final ICallHook sHook : callHooks) {
				try {
					sHook.beforeCall(context);
				} catch (final Throwable e) {
					LOGGER.log(LogService.LOG_ERROR, "exception in beforeCall", e); //$NON-NLS-1$
				}
			}
		}

		try {
			return super.invoke(proxy, method, args);
		} catch (final InvocationTargetException e) {
			// first check for specific Hessian exceptions
			if (e.getTargetException() instanceof HessianRuntimeException
					|| e.getTargetException() instanceof HessianProtocolException) {
				Throwable cause = e.getTargetException();
				while (cause.getCause() != null) {
					if (cause.getCause() instanceof RemoteFailure) {
						context.setRemoteFailure(true);
						throw cause.getCause();
					}
					cause = cause.getCause();
				}
				context.setRemoteFailure(true);
				throw new RemoteFailure("Error while invoking remote service", e.getTargetException()); //$NON-NLS-1$
			}
			// if runtime exception throw it anyway
			if (e.getTargetException() instanceof RuntimeException) {
				throw e.getTargetException();
			}
			// throw exception if it is in the method signature
			for (final Class<?> exceptionType : method.getExceptionTypes()) {
				if (exceptionType.isAssignableFrom(e.getTargetException().getClass())) {
					throw e.getTargetException();
				}
			}
			// otherwise throw a remote failure
			context.setRemoteFailure(true);
			throw new RemoteFailure("Error while invoking remote service", e.getTargetException()); //$NON-NLS-1$
		} finally {
			context.getMessageContext().fireEndCall();
			// call hooks after the call
			if (callHooks.size() > 0) {
				for (final ICallHook sHook : callHooks) {
					try {
						sHook.afterCall(context);
					} catch (final Throwable e) {
						LOGGER.log(LogService.LOG_ERROR, "exception in afterCall", e); //$NON-NLS-1$
					}
				}
			}
		}
	}

	@InjectService
	public void bind(final ICallHook serviceHook) {
		callHooks.add(serviceHook);
	}

	public void unbind(final ICallHook serviceHook) {
		callHooks.remove(serviceHook);
	}

	public Object getCallProxy() {
		return getProxiedInstance();
	}

	public void setRemoteServiceDescription(final RemoteServiceDescription rsd) {
		this.rsd = rsd;
	}

	public void setMessageContextAccessor(final ICallMessageContextAccessor mca) {
		this.mca = mca;
	}

	@Override
	public Subject getSubject() {
		return null;
	}
}
