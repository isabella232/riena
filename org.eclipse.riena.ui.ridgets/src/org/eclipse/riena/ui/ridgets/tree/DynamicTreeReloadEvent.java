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
package org.eclipse.riena.ui.ridgets.tree;

import java.util.EventObject;

import org.eclipse.core.runtime.Assert;

/**
 * <code>DynamicTreeReloadEvent</code> is used to notify listeners that reload
 * of tree elements has started or has stopped.
 */
public class DynamicTreeReloadEvent extends EventObject {

	/** Identifies the begin of reload. */
	public static final int BEGIN = 1;
	/** Identifies the end of reload. */
	public static final int END = 0;

	private static final String TXT_PRECONDITION_TYPE = "Type must be one of DynamicTreeReloadEvent.BEGIN or DynamicTreeReloadEvent.END"; //$NON-NLS-1$

	private int type;

	/**
	 * Constructor. Create a new instance of <code>DynamicTreeReloadEvent</code>
	 * 
	 * @param source -
	 *            the event source.
	 * @param type -
	 *            type of the event - one of:
	 *            <code>DynamicTreeReloadEvent.BEGIN</code> or
	 *            <code>DynamicTreeReloadEvent.END</code>.
	 * 
	 * @post (type == DynamicTreeReloadEvent.END) || (type ==
	 *       DynamicTreeReloadEvent.BEGIN)
	 */
	public DynamicTreeReloadEvent(Object source, int type) {

		super(source);

		Assert.isTrue((type == END) || (type == BEGIN), TXT_PRECONDITION_TYPE);

		this.type = type;
	}

	/**
	 * Returns the type of the event.
	 * 
	 * @return type of the event - one of: <code>BEGIN</code> and
	 *         <code>END</code>.
	 */
	public int getType() {

		return type;

	}

}
