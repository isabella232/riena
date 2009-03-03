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
package org.eclipse.riena.internal.communication.core.proxyselector;

import java.net.ProxySelector;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;
import org.eclipse.riena.internal.communication.core.Activator;

/**
 * Extension interface for defining {@code ProxySelector}s.
 */
@ExtensionInterface
public interface IProxySelectorExtension {

	String EXTENSION_POINT_ID = Activator.PLUGIN_ID + ".proxyselector"; //$NON-NLS-1$

	String getName();

	int getOrder();

	@MapName("class")
	ProxySelector createProxySelector();
}
