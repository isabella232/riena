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
package org.eclipse.riena.ui.swt.uiprocess;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.riena.ui.core.uiprocess.UIProcess;

/**
 * Such a listener can be used to observe the {@link ApplicationWindow}´s cancel
 * state
 */
public interface ICancelListener {

	/**
	 * notifies that either the window is closed or the cancel button is
	 * pressed. Closing the window leads to all {@link UIProcess} being stopped.
	 * Cancel only stops the current visualized {@link UIProcess}.
	 * 
	 * @param windowClosing
	 *            - flag indicating if the whole window has been closed
	 * 
	 */
	void canceled(boolean windowClosing);

}
