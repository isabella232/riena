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
 * List object for a list of progress monitors. convient class so that the class
 * sending events to a list of progressmonitors for remote services can send
 * this event to this class and it dispatches the event to all registered
 * progressmonitors
 */
public interface IRemoteProgressMonitorList {

	int BYTE_COUNT_INCR = 512;

	void fireStartEvent();

	void fireEndEvent(int totalBytes);

	void fireWriteEvent(int totalBytes, int bytesSent);

	void fireReadEvent(int totalBytes, int bytesRead);

}
