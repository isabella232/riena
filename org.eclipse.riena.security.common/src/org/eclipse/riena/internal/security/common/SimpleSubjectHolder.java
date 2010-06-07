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
package org.eclipse.riena.internal.security.common;

import javax.security.auth.Subject;

import org.eclipse.riena.core.util.ListenerList;
import org.eclipse.riena.security.common.ISubjectChangeListener;
import org.eclipse.riena.security.common.ISubjectHolder;
import org.eclipse.riena.security.common.SubjectChangeEvent;

/**
 * The <code>SimpleSubjectHolder</code> is a very simple implementation of the
 * <code>ISubjectHolder</code> interface which should be sufficient for at least
 * the client side.
 * 
 */
public class SimpleSubjectHolder implements ISubjectHolder {

	private Subject subject;
	private final ListenerList<ISubjectChangeListener> subjectChangeListeners = new ListenerList<ISubjectChangeListener>(
			ISubjectChangeListener.class);

	/**
	 * Creates a SimpleSubjectHolder
	 */
	public SimpleSubjectHolder() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.security.common.ISubjectHolder#getSubject()
	 */
	public Subject getSubject() {
		return subject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.security.common.ISubjectHolder#setSubject(javax.security
	 * .auth.Subject)
	 */
	public void setSubject(Subject subject) {
		Subject old = this.subject;
		this.subject = subject;
		notifySubjectChange(this.subject, old);
	}

	private void notifySubjectChange(Subject newSubject, Subject oldSubject) {
		// check avoids SubjectChangeEvent object if there is no listener
		if (subjectChangeListeners.size() == 0) {
			return;
		}
		SubjectChangeEvent event = new SubjectChangeEvent(newSubject, oldSubject);
		for (ISubjectChangeListener listener : subjectChangeListeners.getListeners()) {
			listener.changed(event);
		}
	}

	public void addSubjectChangeListener(ISubjectChangeListener listener) {
		if (listener != null) {
			subjectChangeListeners.add(listener);
		}
	}

	public void removeSubjectChangeListener(ISubjectChangeListener listener) {
		subjectChangeListeners.remove(listener);
	}

}
