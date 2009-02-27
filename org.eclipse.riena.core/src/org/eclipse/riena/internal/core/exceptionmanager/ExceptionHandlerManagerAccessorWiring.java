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
package org.eclipse.riena.internal.core.exceptionmanager;

import org.eclipse.riena.core.exception.IExceptionHandlerManager;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.wire.AbstractWiring;
import org.osgi.framework.BundleContext;

/**
 * Wire the {@code ExceptionHandlerManagerAccessor}.
 */
public class ExceptionHandlerManagerAccessorWiring extends AbstractWiring {

	public void wire(Object bean, BundleContext context) {
		Inject.service(IExceptionHandlerManager.class).useRanking().into(bean).andStart(context);
	}

}
