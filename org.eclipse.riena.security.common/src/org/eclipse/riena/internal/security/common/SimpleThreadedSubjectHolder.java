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
package org.eclipse.riena.internal.security.common;

import javax.security.auth.Subject;

import org.eclipse.riena.security.common.ISubjectChangeListener;
import org.eclipse.riena.security.common.ISubjectHolder;

/**
 * The <code>SimpleTrheadedSubjectHolder</code> is a very simple implementation
 * of the <code>ISubjectHolder</code> interface which should be sufficient for
 * the server side.
 */
public class SimpleThreadedSubjectHolder implements ISubjectHolder {

	private final ThreadLocal<SimpleSubjectHolder> subjectHolders = new ThreadLocal<SimpleSubjectHolder>() {
		@Override
		protected SimpleSubjectHolder initialValue() {
			return new SimpleSubjectHolder();
		}
	};

	/**
	 * Creates a SimpleThreadedSubjectHolder
	 */
	public SimpleThreadedSubjectHolder() {
	}

	public Subject getSubject() {
		return subjectHolders.get().getSubject();
	}

	public void setSubject(Subject subject) {
		subjectHolders.get().setSubject(subject);
	}

	public void addSubjectChangeListener(ISubjectChangeListener listener) {
		subjectHolders.get().addSubjectChangeListener(listener);
	}

	public void removeSubjectChangeListener(ISubjectChangeListener listener) {
		subjectHolders.get().removeSubjectChangeListener(listener);
	}

}
