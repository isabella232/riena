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
package org.eclipse.riena.communication.core.attachment;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * interface for encapsulating different types of attachments
 */
public interface IDataSource {

	/**
	 * Returns a inputStream to the content of this datasource. The inputStream
	 * is guranteed to be right at the beginning.
	 * 
	 * @return
	 * @throws IOException
	 */
	InputStream getInputStream() throws IOException;

	/**
	 * @return
	 * @throws IOException
	 */
	OutputStream getOutputStream() throws IOException;

	/**
	 * @return
	 */
	String getName();

	/**
	 * checks wether a datasource is valid (has an inputstream that can be read)
	 */
	void checkValid() throws IOException;

}
