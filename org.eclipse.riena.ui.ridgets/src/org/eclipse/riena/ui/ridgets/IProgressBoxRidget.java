/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2008 compeople AG                         *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.ui.ridgets;

import org.eclipse.riena.ui.core.uiprocess.IProgressVisualizerObserver;

/**
 * 
 */
public interface IProgressBoxRidget extends IProgressVisualizerObserver, IRidget {

	public void activate();

	public void deactivate();

}