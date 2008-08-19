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
package org.eclipse.riena.communication.core.progressmonitor;

/**
 * Interface that must be implemented by any class that wants to monitor the
 * progress of transporting data in webservices
 */
public interface IProgressMonitor {

	/**
	 * called before the first byte is send on the line
	 */
	void start();

	/**
	 * called after the last byte was read from the line
	 */
	void end();

	/**
	 * Called in arbitrary (currently not configurable intervals) when data
	 * chunks are sent on the line. The last call for one successful request
	 * will always have bytes == totalBytes.
	 * 
	 * @param bytes
	 *            bytes already sent
	 * @param totalBytes
	 *            totalBytes that need to be sent
	 */
	void request(ProgressMonitorEvent event);

	/**
	 * Called in arbitrary (currently not configurable intervals) when data
	 * chunks are received from the line. the last call for a sucessful response
	 * will always have bytes == totalBytes
	 * 
	 * @param bytes
	 * @param totalBytes
	 */
	void response(ProgressMonitorEvent event);
}
