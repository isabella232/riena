/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2005 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.internal.sample.app.server;

import java.io.IOException;
import java.io.InputStream;

/**
 * nomen est omen
 * 
 * @author Christian Campo
 */
public class SampleInputStream extends InputStream {

	private int offset = 0;
	private int start = 0;

	/**
	 *
	 */
	public SampleInputStream() {
		super();
	}

	@Override
	public int read() throws IOException {
		if (offset++ < 100) {
			return 'x';
		} else {
			throw new IOException("could not read past 100 chars (by design)");
		}
	}

	@Override
	public boolean markSupported() {
		return false;
	}

	@Override
	public synchronized void reset() throws IOException {
		offset = start;
	}

}
