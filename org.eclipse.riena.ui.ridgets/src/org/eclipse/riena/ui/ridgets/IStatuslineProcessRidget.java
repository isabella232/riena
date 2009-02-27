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
package org.eclipse.riena.ui.ridgets;

import org.eclipse.riena.ui.core.uiprocess.IProgressVisualizerObserver;

/**
 * Interface for the Adapter of the process component of the status line
 */
public interface IStatuslineProcessRidget extends IStatuslineUIProcessRidget, IProgressVisualizerObserver, IRidget { // extends
	// IProcessInfoViewer,
	// ICompositePopupAdapter {
	//
	// /**
	// * Tells to show the progress static and not mooving If one of the
	// contained
	// * process infos is static, Then the progress is shown static
	// */
	// String MODE_STATIC = "ShowStaticProgress";
	//
	// /**
	// * The default name of this statusline component
	// */
	// String NAME = "process";
	//
	// /**
	// * <code>PROPERTY_PROCESSLIST</code>
	// */
	// String PROPERTY_PROCESSLIST = "processList";
	// /**
	// * <code>PROPERTY_PROCESSANIMATION</code>
	// */
	// String PROPERTY_PROCESSANIMATION = "processAnimation";
	// /**
	// * <code>PROPERTY_PROCESSBUTTONSTATE</code>
	// */
	// String PROPERTY_PROCESSBUTTONSTATE = "processButtonState";
	// /**
	// * <code>PROPERTY_PROCESSTOOLTIP</code>
	// */
	// String PROPERTY_PROCESSTOOLTIP = "processTolltip";
	// /**
	// * <code>PROPERTY_PROCESSMESSAGE</code>
	// */
	// String PROPERTY_PROCESSMESSAGE = "processMessage";
	// /**
	// * <code>PROPERTY_PROCESSLABEL</code>
	// */
	// String PROPERTY_PROCESSLABEL = "processLabel";
	//
	// /**
	// * @return Returns the processList.
	// */
	// Vector<UIProcess> getProcessList();

	/**
	 * @return the message to display instead of the list
	 */
	boolean getProcessMessage();

	// /**
	// * @return True if the animation should appear, else false
	// */
	// int getProcessAnimation();

	/**
	 * @return the processButtonState
	 */
	boolean getProcessButtonState();

	/**
	 * Called from the gui when the state of the Button changes
	 * 
	 * @param pProcessButtonState
	 */
	void setProcessButtonState(boolean pProcessButtonState);

	/**
	 * Called from the Gui,when a Process in the currently displayed list ist
	 * selected
	 * 
	 * @param pProcessSelection
	 */
	void setProcessSelection(int pProcessSelection);

	/**
	 * 
	 */
	void hidePopups();
}
