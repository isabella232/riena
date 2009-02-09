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
package org.eclipse.riena.ui.swt.utils;

/**
 *
 */
public enum ImageState {

	NORMAL(""), //$NON-NLS-1$
	HOVER("_h_"), //$NON-NLS-1$
	PRESSED("_p_"), //$NON-NLS-1$
	DISABLED("_d_"), //$NON-NLS-1$
	SELECTED("_a_"), //$NON-NLS-1$
	SELECTED_HOVER("_ah_"), //$NON-NLS-1$
	SELECTED_DISABLED("_ad_"), //$NON-NLS-1$
	DEFAULT("_s_"), //$NON-NLS-1$
	HAS_FOCUS("_f_"), //$NON-NLS-1$
	HOVER_HAS_FOCUS("_hf_"); //$NON-NLS-1$

	private String stateNameExtension;

	private ImageState(String stateNameExtension) {
		this.stateNameExtension = stateNameExtension;
	}

	String getStateNameExtension() {
		return stateNameExtension;
	}

}
