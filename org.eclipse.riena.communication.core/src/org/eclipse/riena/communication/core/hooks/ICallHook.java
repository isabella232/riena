/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.communication.core.hooks;

/**
 * ICallHook is an interface that needs to be implemented by a component that
 * wants to plug into the remote service call process. A component needs to
 * register as OSGi Service to activate itself as such an hook i.e.
 * context.registerService(ICallHook.class.getName(), new YouCallHook(),null);
 * 
 * Then the beforeCall method is called (on the client) before EACH and EVERY
 * remote service call together with a CallContext instance. The afterCall
 * method is called after the remote service calls return and before control is
 * returned to the application code with the same CallContext instance.
 * 
 * A new CallContext instance is created for every remote service call. (they
 * are never reused)
 */
public interface ICallHook {

	/**
	 * Is called before the remote service call.
	 * 
	 * @param context
	 *            CallContext instance that contains metainformation about the
	 *            call and contains a properties hashmap that can be used to
	 *            store information for the afterCall method.
	 */
	void beforeCall(CallContext context);

	/**
	 * @param context
	 *            CallContext instance (same as beforeCall) that contains
	 *            metainformation and a generice properties hashpamp.
	 */
	void afterCall(CallContext context);

}
