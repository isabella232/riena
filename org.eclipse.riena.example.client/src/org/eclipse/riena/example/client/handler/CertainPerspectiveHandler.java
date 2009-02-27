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
package org.eclipse.riena.example.client.handler;

/**
 * 
 */
public class CertainPerspectiveHandler extends DummyHandler {

	/**
	 * @see org.eclipse.riena.example.client.handler.DummyHandler#getTitle()
	 */
	@Override
	protected String getTitle() {
		return "Certain view"; //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.riena.example.client.handler.DummyHandler#getMessage()
	 */
	@Override
	protected String getMessage() {
		return "This command is only enabled for a certain perspective (Playground)!\n"; //$NON-NLS-1$
	}

}
