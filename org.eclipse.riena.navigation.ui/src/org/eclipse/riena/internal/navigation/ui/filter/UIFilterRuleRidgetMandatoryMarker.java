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
package org.eclipse.riena.internal.navigation.ui.filter;

import org.eclipse.riena.ui.core.marker.MandatoryMarker;

/**
 * Filter rule to provide a mandatory marker for a ridget.
 */
public class UIFilterRuleRidgetMandatoryMarker extends AbstractUIFilterRuleRidgetMarker {

	/**
	 * Creates a new instance of {@code UIFilterRuleRidgetMandatoryMarker}.
	 * 
	 * @param id
	 *            - ID
	 */
	public UIFilterRuleRidgetMandatoryMarker() {
		super(null, new MandatoryMarker(false));
	}

	/**
	 * Creates a new instance of {@code UIFilterRuleRidgetMandatoryMarker}.
	 * 
	 * @param ridgetIdPattern
	 *            - ID
	 */
	public UIFilterRuleRidgetMandatoryMarker(String ridgetIdPattern) {
		super(ridgetIdPattern, new MandatoryMarker(false));
	}

}