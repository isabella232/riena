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
package org.eclipse.riena.core.exception;

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.exception.IExceptionHandlerManager.Action;


/**
 * ExceptionHandler can handle exceptions
 */
public interface IExceptionHandler {
    /**
     * Service ID
     */
    String ID = IExceptionHandler.class.getName();

    /**
     * Indicate that this handler will be invoked before the named handler <code>before</code> If <code>before</code>
     * is <code>null</code> or the named handler does not exists and there are more than one handlers then the time of
     * invocation for this handler will be indeterminate.
     * 
     * @return a handler name
     */
    String getBefore();

    /**
     * Anwers this handler name
     * 
     * @pre name != null
     * 
     * @return this handler name
     */
    String getName();

    /**
     * Check if the exception passed can be handled and return an {@link Action} how might to process. This method
     * should only be called in top level catch blocks like main, ui thread etc... From other locations inside a catch
     * block use handleCaught(..)
     * 
     * @pre t != null
     * @post result != null
     * 
     * @param t
     *            exception to be handled
     * @param msg
     *            an optional message
     * @param logger
     *            to be used for logging
     * 
     * @return the Action how to process
     */
    Action handleUncaught(Throwable t, String msg, Logger logger);

    /**
     * Check if the exception passed can be handled and return an {@link Action} how might to process. This method
     * should be used anywhere where an exceptions is catched directly.
     * 
     * @pre t != null
     * @post result != null
     * 
     * @param t
     *            exception to be handled
     * @param msg
     *            an optional message
     * @param logger
     *            to be used for logging
     * 
     * @return the Action how to process
     * 
     */
    Action handleCaught(Throwable t, String msg, Logger logger);
}
