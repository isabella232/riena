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
package org.eclipse.riena.internal.monitor.client;

import org.eclipse.riena.core.injector.extension.DoNotWireExecutable;
import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;
import org.eclipse.riena.monitor.client.ICollector;

/**
 * Extension interface for {@code ICollector} definitions.
 */
@ExtensionInterface
public interface ICollectorExtension {

	String ID = "org.eclipse.riena.monitor.collectors"; //$NON-NLS-1$

	String getCategory();

	@MapName("class")
	@DoNotWireExecutable
	ICollector createCollector();

	int getMaxItems();
}
