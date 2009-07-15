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
package org.eclipse.riena.security.common;

import javax.security.auth.Subject;

import org.eclipse.riena.core.service.Service;
import org.eclipse.riena.internal.security.common.Activator;

/**
 * A convenient class to access the current Subject from the
 * SubjectHolderService
 */
public final class SubjectAccessor {

	private SubjectAccessor() {
		// utility
	}

	public static Subject getSubject() {
		ISubjectHolderService holder = get();
		if (holder == null) {
			return null;
		}
		return holder.fetchSubjectHolder().getSubject();
	}

	public static void setSubject(Subject subject) {
		ISubjectHolderService holder = get();
		if (holder == null) {
			return;
		}
		holder.fetchSubjectHolder().setSubject(subject);
	}

	private static ISubjectHolderService get() {
		return Service.get(Activator.getDefault().getContext(), ISubjectHolderService.class);
	}
}
