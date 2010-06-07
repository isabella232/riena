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
package org.eclipse.riena.internal.sample.app.server;

import java.lang.reflect.Constructor;
import java.util.Calendar;

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.sample.app.common.exception.IExceptionService;
import org.osgi.service.log.LogService;

/**
 * This class implements the IExceptionService Interface and throws dedicated
 * failures / exceptions on client demand.
 * 
 * Some Examples: java.lang.ClassNotFoundException
 * java.lang.reflect.InvocationTargetException java.io.IOException
 * java.lang.RuntimeException java.lang.ClassCastException
 * java.lang.IndexOutOfBoundsException java.util.NoSuchElementException
 * java.lang.NullPointerException
 * 
 * 
 */
public class ExceptionService implements IExceptionService {
	private final static Logger LOGGER = Log4r.getLogger(Activator.getDefault(), ExceptionService.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.sample.app.common.exception.IExceptionService#getDate()
	 */
	public String getDate() {
		Calendar cld = Calendar.getInstance();

		int day = cld.get(Calendar.DAY_OF_MONTH);
		int month = cld.get(Calendar.MONTH);
		int year = cld.get(Calendar.YEAR);

		return day + "." + month + "." + year; //$NON-NLS-1$ //$NON-NLS-2$
	}

	// public String throwFailure(String name) throws Failure {
	// Failure failure = null;
	//
	// LOGGER.log(LogService.LOG_INFO, "The client requested a Failure '" + name
	// + "'");
	//
	// // let's try to assemble the requested Failure
	// try {
	// // load the class
	// Class failureClass = Class.forName(name);
	// Class[] params = { Class.forName("java.lang.String") };
	//
	// LOGGER.log(LogService.LOG_INFO, "throwFailure: Created a class: " +
	// failureClass);
	//
	// // create a new instance of the Failure
	// String cause = "ExceptionService: Here is your requested failure: " +
	// failureClass.getName() + "...";
	// Object[] args = { cause };
	// failure = (Failure)
	// failureClass.getConstructor(params).newInstance(args);
	// LOGGER.log(LogService.LOG_INFO,
	// "throwException: Created the requested exception " + failure
	// + " of type throwable");
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// // the requested class is not available
	// throw new RuntimeException("ExceptionService: requested Exception '" +
	// name + "' could not be found....");
	// }
	//
	// // thats the final result - throw it
	// if (failure != null) {
	// throw failure;
	// }
	//
	// return null;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.sample.app.common.exception.IExceptionService#
	 * throwException(java.lang.String)
	 */
	public String throwException(String name) throws Throwable {
		Throwable exception = null;

		LOGGER.log(LogService.LOG_INFO, "The client requested an Exception '" + name + "'"); //$NON-NLS-1$ //$NON-NLS-2$

		if (name == null) {
			LOGGER.log(LogService.LOG_ERROR, "The name of the requested Exception/Failure is null!"); //$NON-NLS-1$
			return null;
		}

		if (name.equals("default")) { //$NON-NLS-1$
			throw new Exception("ExceptionService: Here is your requested general default Exception..."); //$NON-NLS-1$
		}

		// let's try to assemble the requested Exception
		try {
			// load the class
			Class<?> exceptionClass = Class.forName(name);
			Class<?>[] params = { Class.forName("java.lang.String") }; //$NON-NLS-1$

			LOGGER.log(LogService.LOG_INFO, "throwException: Created a class: " + exceptionClass); //$NON-NLS-1$

			// create a new instance of the Exception
			String cause = "ExceptionService: Here is your requested " + exceptionClass.getName() + "..."; //$NON-NLS-1$ //$NON-NLS-2$
			Object[] args = { cause };

			// Some classes do not have an constructor with the parameter
			// string.
			Constructor<?> c = null;
			try {
				c = exceptionClass.getConstructor(params);
				exception = (Throwable) c.newInstance(args);
			} catch (NoSuchMethodException ex) {
				params = new Class[0];
				c = exceptionClass.getConstructor(params);
				exception = (Throwable) c.newInstance(new Object[0]);
				LOGGER.log(LogService.LOG_INFO, "Take default constructor."); //$NON-NLS-1$
			}

			LOGGER.log(LogService.LOG_INFO, "throwException: Created the requested exception " + exception //$NON-NLS-1$
					+ " of type throwable"); //$NON-NLS-1$

		} catch (Exception e) {
			e.printStackTrace();
			// the requested class is not available
			throw new Exception("ExceptionService: requested Exception '" + name + "' could not be found...."); //$NON-NLS-1$ //$NON-NLS-2$
		}

		// thats the final result - throw it
		if (exception != null) {
			throw exception;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.sample.app.common.exception.IExceptionService#
	 * throwNestedException()
	 */
	public String throwNestedException() throws Throwable {
		try {
			newNullPointerException();
		} catch (Exception e) {
			Exception x = new Exception(e);
			x.printStackTrace();
			throw new Exception(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.riena.sample.app.common.exception.IExceptionService#
	 * throwRuntimeException(java.lang.String)
	 */
	public String throwRuntimeException(String name) {
		try {
			this.throwException(name);
		} catch (Throwable e) {
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	private String newNullPointerException() {
		throw new NullPointerException("ExceptionService: Here is your requested nested Exception."); //$NON-NLS-1$
	}
}