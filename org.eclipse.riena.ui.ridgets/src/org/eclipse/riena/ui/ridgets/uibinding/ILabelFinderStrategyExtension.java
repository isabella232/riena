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
package org.eclipse.riena.ui.ridgets.uibinding;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;

/**
 * @since 1.2
 *
 */
@ExtensionInterface
public interface ILabelFinderStrategyExtension {

	String EXTENSION_POINT_ID = "org.eclipse.riena.ui.ridgets.labelfinderstrategy"; //$NON-NLS-1$

	@MapName("className")
	String getClassName();

	@MapName("className")
	ILabelFinderStrategy createFinderStrategy();
}
