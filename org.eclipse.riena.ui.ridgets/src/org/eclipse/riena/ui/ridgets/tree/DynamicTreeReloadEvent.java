/****************************************************************
 *                                                              *
 * Copyright (c) 2004 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets.tree;

import java.util.EventObject;

import org.eclipse.core.runtime.Assert;

/**
 * <code>DynamicTreeReloadEvent</code> is used to notify listeners that reload
 * of tree elements has started or has stopped.
 * 
 * @author Thorsten Schenkel
 */
public class DynamicTreeReloadEvent extends EventObject {

	/** Identifies the begin of reload. */
	public static final int BEGIN = 1;
	/** Identifies the end of reload. */
	public static final int END = 0;

	private static final String TXT_PRECONDITION_TYPE = "Type must be one of DynamicTreeReloadEvent.BEGIN or DynamicTreeReloadEvent.END";

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
