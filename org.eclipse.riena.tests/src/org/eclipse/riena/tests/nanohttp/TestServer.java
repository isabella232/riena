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
package org.eclipse.riena.tests.nanohttp;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Embedded simple web server for unit tests based on NanoHTTPD.
 */
public class TestServer extends NanoHTTPD {

	private File root;

	/**
	 * @param port
	 * @param root
	 * @throws IOException
	 */
	public TestServer(int port, File root) throws IOException {
		super(port);
		this.root = root;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.tests.nanohttp.NanoHTTPD#serveFile(java.lang.String,
	 *      java.util.Properties, java.io.File, boolean)
	 */
	@Override
	public Response serveFile(String uri, Properties header, File homeDir, boolean allowDirectoryListing) {
		return super.serveFile(uri, header, root, allowDirectoryListing);
	}

}
