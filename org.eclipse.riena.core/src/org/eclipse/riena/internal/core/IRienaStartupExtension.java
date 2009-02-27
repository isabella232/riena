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
package org.eclipse.riena.internal.core;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;
import org.eclipse.riena.core.injector.extension.MapName;
import org.osgi.framework.Bundle;

/**
 * This extension interface defines all startup actions should be performed
 * after {@code org.eclipse.riena.core} has been started.
 */
@ExtensionInterface
public interface IRienaStartupExtension {

	Bundle getContributingBundle();

	@MapName("runClass")
	String getRunClassName();

	@MapName("runClass")
	Runnable createRunner();

	String getRequiredBundles();

	boolean isActivateSelf();

}
