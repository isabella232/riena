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
package org.eclipse.riena.ui.ridgets.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.riena.internal.ui.ridgets.Activator;

/**
 * Result of a validation performed by an IValidationRule. Extends the IStatus
 * by adding some specific code for validation of Ridgets.
 */
public class ValidationRuleStatus extends Status implements IValidationRuleStatus {

	private ValidationRuleStatus(int severity, int code, String message) {
		super(severity, Activator.PLUGIN_ID, code, message, null);
	}

	/**
	 * Returns an OK status.
	 * 
	 * @return an OK status
	 */
	public static IStatus ok() {
		return Status.OK_STATUS;
	}

	/**
	 * Returns an ERROR status
	 * 
	 * @param blocker
	 *            Indicates whether the effects of the input that lead to the
	 *            error status must be undone i.e. whether the input must be
	 *            blocked.
	 * @param message
	 *            A message.
	 * @return An ERROR status.
	 */
	public static IStatus error(boolean blocker, String message) {
		int code = ERROR_ALLOW_WITH_MESSAGE;
		if (blocker) {
			code = ERROR_BLOCK_WITH_FLASH;
		}
		return new ValidationRuleStatus(IStatus.ERROR, code, message);
	}

	/**
	 * Returns a MultiStatus that joins multiple statuses. The code that holds
	 * the blocking information will be set to the most severe of the joined
	 * statuses.
	 * 
	 * @param statuses
	 *            The statuses to join.
	 * @return The joined status.
	 */
	public static IStatus join(IStatus[] statuses) {
		int code = IValidationRuleStatus.ERROR_ALLOW_WITH_MESSAGE;
		for (IStatus status : statuses) {
			if (status.getCode() == IValidationRuleStatus.ERROR_BLOCK_WITH_FLASH) {
				code = IValidationRuleStatus.ERROR_BLOCK_WITH_FLASH;
			}
		}
		return new MultiStatus(Activator.PLUGIN_ID, code, statuses, null, null);
	}

}
