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

import org.eclipse.riena.ui.core.uiprocess.UIProcess;

/**
 * 
 */
public abstract class SwtUIProcess extends UIProcess {

	public SwtUIProcess(String name) {
		super(name, new DialogProgressProvider(new SwtUISynchronizer()));
	}

	@Override
	public void finalUpdateUI() {
		// TODO Auto-generated method stub

	}

	public void initialUpdateUI() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateProgress(int progress) {
		// TODO Auto-generated method stub

	}

}