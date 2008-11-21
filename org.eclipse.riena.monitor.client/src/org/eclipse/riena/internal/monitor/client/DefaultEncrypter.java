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
package org.eclipse.riena.internal.monitor.client;

import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.riena.monitor.client.IEncrypter;

/**
 *
 */
public class DefaultEncrypter implements IEncrypter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.monitor.client.IEncrypter#getDecrypter(java.io.InputStream
	 * )
	 */
	public InputStream getDecrypter(InputStream inputStream) {
		return inputStream;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.monitor.client.IEncrypter#getEncrypter(java.io.OutputStream
	 * )
	 */
	public OutputStream getEncrypter(OutputStream outputStream) {
		return outputStream;
	}

}
