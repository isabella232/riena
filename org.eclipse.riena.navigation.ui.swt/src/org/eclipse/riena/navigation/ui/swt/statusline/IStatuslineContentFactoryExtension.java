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
package org.eclipse.riena.navigation.ui.swt.statusline;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.ui.swt.IStatusLineContentFactory;

/**
 *Interface for proxies for extensions to the extension point
 * "org.eclipse.riena.ui.swt.statusline"
 */
@ExtensionInterface
public interface IStatuslineContentFactoryExtension {

	String ID = "org.eclipse.riena.ui.swt.statusline"; //$NON-NLS-1$

	IStatusLineContentFactory createFactory();

}
