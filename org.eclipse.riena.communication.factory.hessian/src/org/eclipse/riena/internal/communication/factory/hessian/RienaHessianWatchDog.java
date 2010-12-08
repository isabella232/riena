/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.communication.factory.hessian;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import com.caucho.hessian.client.HessianRuntimeException;

final class RienaHessianWatchDog extends Handler {

	@Override
	public void publish(final LogRecord record) {
		if (record.getLevel() != Level.WARNING) {
			return;
		}
		if (record.getMessage().contains("Hessian/Burlap: '") //$NON-NLS-1$
				&& record.getMessage().contains("' is an unknown class in ")) { //$NON-NLS-1$
			throw new HessianRuntimeException(record.getMessage());
		}
	}

	@Override
	public void flush() {
	}

	@Override
	public void close() throws SecurityException {
	}
}