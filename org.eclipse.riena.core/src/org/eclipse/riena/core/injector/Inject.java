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
package org.eclipse.riena.core.injector;

import org.eclipse.riena.core.extension.ExtensionDescriptor;
import org.eclipse.riena.core.service.ServiceDescriptor;

/**
 * This <i>helper</i> class is the entry point to the injector <i>micro
 * framework</i> of riena.
 */
public final class Inject {

	/**
	 * @see ServiceDescriptor
	 * 
	 * @param clazz
	 * @return
	 */
	public static ServiceDescriptor service(String clazz) {
		return new ServiceDescriptor(clazz);
	}

	/**
	 * @see ExtensionDescriptor
	 * 
	 * @param extensionId
	 * @return
	 */
	public static ExtensionDescriptor extension(String extensionId) {
		return new ExtensionDescriptor(extensionId);
	}
}
