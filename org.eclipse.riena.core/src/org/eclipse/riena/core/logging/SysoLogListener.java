/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.core.logging;

import java.util.Date;

import org.eclipse.equinox.log.ExtendedLogEntry;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;

public class SysoLogListener implements LogListener {

	public void logged(LogEntry entry) {
		ExtendedLogEntry eEntry = (ExtendedLogEntry) entry;
		StringBuffer buffer = new StringBuffer();
		buffer.append(new Date(eEntry.getTime()).toString()).append(' ');
		buffer.append(eEntry.getLevel()).append(' ');
		buffer.append(eEntry.getLoggerName()).append(' ');
		buffer.append(eEntry.getContext()).append(' ');
		buffer.append(entry.getMessage());
		System.out.println(buffer.toString());
		if (eEntry.getException() != null) {
			eEntry.getException().printStackTrace(System.out);
		}
	}
}
