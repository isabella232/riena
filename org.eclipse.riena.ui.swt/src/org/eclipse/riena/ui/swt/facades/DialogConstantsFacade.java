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
package org.eclipse.riena.ui.swt.facades;

/**
 * TODO [ev] javadoc
 * 
 * @since 2.0
 */
public abstract class DialogConstantsFacade {

	private static final DialogConstantsFacade INSTANCE = (DialogConstantsFacade) FacadeFactory
			.newFacade(DialogConstantsFacade.class);

	public static final DialogConstantsFacade getDefault() {
		return INSTANCE;
	}

	public abstract String getOkLabel();

	public abstract String getCancelLabel();

	public abstract String getYesLabel();

	public abstract String getNoLabel();
}
