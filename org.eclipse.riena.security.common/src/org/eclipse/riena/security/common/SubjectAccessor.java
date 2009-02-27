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

import org.eclipse.riena.core.util.ServiceAccessor;
import org.eclipse.riena.core.wire.WireWith;
import org.eclipse.riena.internal.security.common.Activator;

/**
 * A convenient class to access the current Subject from the
 * SubjectHolderService
 */
@WireWith(SubjectAccessorWiring.class)
public final class SubjectAccessor extends ServiceAccessor<ISubjectHolderService> {

	private final static SubjectAccessor SUBJECT_ACCESSOR = new SubjectAccessor();

	private SubjectAccessor() {
		super(Activator.getDefault().getContext());
	}

	public static Subject getSubject() {
		return SUBJECT_ACCESSOR.getCurrentSubject();
	}

	public static void setSubject(Subject subject) {
		SUBJECT_ACCESSOR.setCurrentSubject(subject);
	}

	private Subject getCurrentSubject() {
		return getService().fetchSubjectHolder().getSubject();
	}

	private void setCurrentSubject(Subject subject) {
		getService().fetchSubjectHolder().setSubject(subject);
	}

}
