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
package org.eclipse.riena.security.common;


import javax.security.auth.Subject;

/**
 * A <code>ISubjectHolder</code> implementation retrieves the current principal, i.e. on the client the currently
 * logged on user and on the server the user bound to the current session.
 * 
 */
public interface ISubjectHolder {

    /**
     * Get the current principal
     * 
     * @return current principal
     */
    Subject getSubject();

    /**
     * Set the current principal
     * 
     * @param subject
     *            current subject
     */
    void setSubject(Subject subject);

    /**
     * Adds the given principal change listener
     * 
     * @param listener
     */
    void addSubjectChangeListener(ISubjectChangeListener listener);

    /**
     * Removes the given principal change listener
     * 
     * @param listener
     */
    void removeSubjectChangeListener(ISubjectChangeListener listener);

}