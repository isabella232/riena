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
package org.eclipse.riena.ui.ridgets.swt;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.viewcontroller.IController;

/**
 * TODO [ev] docs
 */
public abstract class AbstractRidgetController implements IController {

	private Map<String, IRidget> map;
	private boolean isBlocked;

	public AbstractRidgetController() {
		map = new HashMap<String, IRidget>();
	}

	public final void addRidget(String id, IRidget ridget) {
		map.put(id, ridget);
	}

	public final IRidget getRidget(String id) {
		return map.get(id);
	}

	public final Collection<? extends IRidget> getRidgets() {
		return Collections.unmodifiableCollection(map.values());
	}
	
	public void configureRidgets() {
		// does nothing, children may overide
	}
	
	public boolean isBlocked() {
		return isBlocked;
	}
	
	public void setBlocked(boolean blocked) {
		isBlocked = blocked;
	}

}
