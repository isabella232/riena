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
package org.eclipse.riena.demo.client.customer.controllers;

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.wire.AbstractWiring;
import org.eclipse.riena.demo.customer.common.ICustomerDemoService;
import org.osgi.framework.BundleContext;

/**
 * Wire the {@code CustomerSearchController}.
 */
public class CustomerSearchControllerWiring extends AbstractWiring {

	public void wire(Object bean, BundleContext context) {
		Inject.service(ICustomerDemoService.class).into(bean).andStart(context);
	}

}
