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
package org.eclipse.riena.security.sessionservice;

import java.security.Principal;

import org.eclipse.riena.security.common.session.Session;


/**
 * This Provider allows to supply your own implementation to construct the <code>Session</code> object. See comment
 * below. You always have to construct the <code>Session</code> object, however you can bring in your own algorithm
 * for supplying the ID.
 * 
 */
public interface ISessionProvider {

    String ID = ISessionProvider.class.getName();

    /**
     * The interface should allow to return any instance of the interface <code>ISession</code>. However other parts
     * of the Framework construct explicit the Session from the id. Hence only the original Session Implementation is
     * currently allowed.
     * 
     * @param principals
     * @param principalLocation
     * @return session
     */
    Session createSession(Principal[] principals);

}