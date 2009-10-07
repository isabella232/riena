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
package org.eclipse.riena.ui.swt.uiprocess;

import org.eclipse.swt.widgets.Display;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.core.uiprocess.IUISynchronizer;

/**
 * serializes a runnable to the SWT-Thread - if possible
 */
public class SwtUISynchronizer implements IUISynchronizer {

	public void synchronize(Runnable r) {
		if (hasDisplay()) {
			Display.getDefault().syncExec(r);
		} else {
			r.run();
		}
	}

	private static Display display;

	private boolean hasDisplay() {
		if (display != null) {
			return true;
		}
		synchronized (SwtUISynchronizer.class) {
			if (display == null) {
				// TODO [sli] Another hack - How can I find out whether the Display has been set by the correct thread?
				display = ReflectionUtils.getHidden(Display.class, "Default"); //$NON-NLS-1$
			}
		}
		return display != null;
	}
}
