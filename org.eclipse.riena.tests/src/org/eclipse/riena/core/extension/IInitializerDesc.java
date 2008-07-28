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
package org.eclipse.riena.core.extension;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.riena.core.extension.MapContributor;
import org.eclipse.riena.core.extension.MapName;
import org.osgi.framework.Bundle;

/**
 *
 */
public interface IInitializerDesc {

	@MapName("class")
	AbstractPreferenceInitializer createInitializer();

	@MapName("class")
	String getInitializer();

	@MapContributor()
	Bundle getContributingBundle();
}
