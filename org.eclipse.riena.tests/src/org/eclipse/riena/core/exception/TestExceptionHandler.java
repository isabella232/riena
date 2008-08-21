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
package org.eclipse.riena.core.exception;

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.exception.IExceptionHandlerManager.Action;


/**
 * Test class for ExceptionHandler
 */
public class TestExceptionHandler implements IExceptionHandler {

    String name;
    String before;
    Throwable throwable;
    Action action = IExceptionHandlerManager.Action.NotHandled;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.riena.core.exception.IExceptionHandler#getBefore()
     */
    public String getBefore() {
        // TODO Auto-generated method stub
        return before;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.riena.core.exception.IExceptionHandler#getName()
     */
    public String getName() {
        // TODO Auto-generated method stub
        return name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.riena.core.exception.IExceptionHandler#handleCaught(java.lang.Throwable, java.lang.Object,
     *      org.eclipse.equinox.log.Logger)
     */
    public Action handleCaught(Throwable t, String msg, Logger logger) {
        throwable = t;
        return action;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.riena.core.exception.IExceptionHandler#handleUncaught(java.lang.Throwable,
     *      java.lang.Object, org.eclipse.equinox.log.Logger)
     */
    public Action handleUncaught(Throwable t, String msg, Logger logger) {
        throwable = t;
        return action;
    }

}
