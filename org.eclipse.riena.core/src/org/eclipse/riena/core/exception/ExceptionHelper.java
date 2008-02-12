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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.AssertionFailedException;

/**
 * This class provides several static methods needed by the exception handling
 * classes.
 * 
 */
public final class ExceptionHelper {

	/*
	 * Note: The usage of the LOGGER has been completely removed, because
	 * creating the logger via hivemind caused in some situations throwing an
	 * ExceptionInInitializerError!!!
	 */

	private static final String DOT_STR = ".";

	/**
	 * version ID (controlled by CVS).
	 */
	public static final String VERSION_ID = "$Id$";

	private static final String EMPTY_STR = "";
	private static final String LINE_SEPARATOR = "\n";
	private static final String ITEM_SEPARATOR = ",";
	private static final String UNKNOWN = "<unknown>";
	private static final String CALLER_CLASS_NOT_FOUND = "<caller class not found>";
	private static final String DEFAULT_CLIENT_MSG = "Es ist ein Systemfehler aufgetreten - Referenz-ID: {ID}";

	private final static StackTraceElement[] EMPTY_STACKTRACE_ELEMENTS = new StackTraceElement[] {};

	private static String javaVersion;
	private static long lastId;

	/**
	 * used to replace the failure id.
	 */
	public static final String PLACEHOLDER_FAILURE_ID_REGEX = "\\{ID\\}";

	/**
	 * the attribute name to be used inside application classes. To determinate
	 * the version of a class this attribute name will be looked for.
	 */
	public static final String VERSION_ID_ATTR_NAME = "VERSION_ID";

	static {
		javaVersion = System.getProperty("java.version");
	}

	private ExceptionHelper() { /* utitlity class */
	}

	/**
	 * Get the caller class name. Use the current stacktrace to determine it.
	 * 
	 * @param obj
	 *            the object to look for.
	 * @return the name of the object class of the caller.
	 */
	public static String getCallerByRef(Object obj) {
		return getCallerByName(obj.getClass().getName());
	}

	/**
	 * Get the caller class name. Use the current stacktrace to determine it.
	 * 
	 * @param className
	 *            the classname to look for.
	 * @return the name of the object class of the caller.
	 */
	public static String getCallerByName(String className) {
		boolean stackFlag = false;
		StackTraceElement[] trace = new Throwable().getStackTrace();

		for (StackTraceElement ste : trace) {
			String steClassName = ste.getClassName();
			if (steClassName.equals(className)) {
				stackFlag = true;
			} else {
				if (stackFlag && !steClassName.startsWith("sun.") && !steClassName.startsWith("java.lang.reflect")) {
					return steClassName;
				}
			}
		}
		return CALLER_CLASS_NOT_FOUND;
	}

	/**
	 * Get the method name of the caller class. Use the current stacktrace to
	 * determine it.
	 * 
	 * @param obj
	 *            the object to look for.
	 * @return the method name of the calling class.
	 */
	public static String getCallerMethod(Object obj) {
		return getCallerMethod(getCallerByRef(obj));
	}

	/**
	 * Get the method name of the caller class. Use the current stacktrace to
	 * determine it.
	 * 
	 * @param className
	 *            the class name of the object to look for.
	 * @return the method name of the calling class.
	 */
	public static String getCallerMethod(String className) {
		StackTraceElement[] trace = new Throwable().getStackTrace();

		for (StackTraceElement ste : trace) {
			if (ste.getClassName().equals(className)) {
				return ste.getMethodName();
			}
		}

		return null;
	}

	/**
	 * Get the version id of the class specified.
	 * 
	 * @param c
	 *            class reference.
	 * @return the version id of the requested class
	 */
	public static String getVersionId(Class<?> c) {
		try {
			Field f = c.getField(VERSION_ID_ATTR_NAME);
			return EMPTY_STR + f.get(null);
		} catch (Throwable ex) {
			return UNKNOWN;
		}
	}

	/**
	 * Get the version id of the class specified.
	 * 
	 * @param className
	 *            class name.
	 * @return the version id of the requested class
	 */
	public static String getVersionId(String className) {
		try {
			if (className == null) {
				return UNKNOWN;
			} else {
				return getVersionId(Class.forName(className));
			}
		} catch (Throwable ex) {
			return UNKNOWN;
		}
	}

	/**
	 * Get the recursive resolved stacktrace (cause chain) of the Failure passed
	 * as argument as string.
	 * 
	 * @param ex
	 *            throwable object reference.
	 * @return stacktrace as string.
	 */
	public static String getRecursiveStacktrace(Throwable ex) {

		List<Throwable> throwables = new ArrayList<Throwable>();
		Throwable throwable = ex;

		// collect all throwables avoiding cycles in order to reverse the
		// sequence
		while (throwable != null && !throwables.contains(throwable)) {
			throwables.add(throwable);
			throwable = throwable.getCause();
		}

		StringBuilder buffer = new StringBuilder();
		for (int i = throwables.size() - 1; i > -1; i--) {
			throwable = throwables.get(i);
			buffer.append("Exception: ").append(throwable.getClass().getName()).append(": ").append(
					throwable.getMessage());

			StackTraceElement[] trace = throwable.getStackTrace();

			if (trace.length < 1 || trace[0].getClassName() == null) {
				buffer.append("No Stacktrace available.");
			} else {
				for (StackTraceElement ste : trace) {
					String className = EMPTY_STR;
					String pkgName = EMPTY_STR;
					String steClassName = ste.getClassName();
					if (steClassName != null) {
						if (steClassName.lastIndexOf(DOT_STR) > 0) {
							int locIx = steClassName.lastIndexOf(DOT_STR);
							className = steClassName.substring(locIx + 1);
							pkgName = ":" + steClassName.substring(0, locIx);
						} else {
							className = steClassName;
						}
					}
					buffer.append(LINE_SEPARATOR).append(className).append(DOT_STR).append(ste.getMethodName()).append(
							"()").append(",").append(ste.getLineNumber()).append(pkgName);
				}
			}
			buffer.append(LINE_SEPARATOR);

			if (i != 0) {
				buffer.append(LINE_SEPARATOR).append("caused:").append(LINE_SEPARATOR);
			}
		}

		return buffer.toString();
	}

	/**
	 * Get the resolved stacktrace of the Failure passed as argument as string.
	 * 
	 * @param throwable
	 *            the throwable to get the stacktrace for.
	 * @return stacktrace as string.
	 */
	public static String getStacktraceAsString(Throwable throwable) {
		StringBuilder buffer = new StringBuilder();
		StackTraceElement[] trace = throwable.getStackTrace();
		for (StackTraceElement ste : trace) {
			buffer.append(ste.getClassName()).append(ITEM_SEPARATOR);
			buffer.append(ste.getMethodName()).append(ITEM_SEPARATOR);
			buffer.append(ste.getFileName()).append(ITEM_SEPARATOR);
			buffer.append(ste.getLineNumber()).append(LINE_SEPARATOR);
		}
		return buffer.toString();
	}

	/**
	 * Get the stacktrace of the Throwable passed as argument as string in its
	 * original format.
	 * 
	 * @param throwable
	 *            the throwable to get the stacktrace for.
	 * @return stacktrace as string.
	 */
	public static String getStacktraceAsOriginalString(Throwable throwable) {
		StringWriter stringWriter = new StringWriter();
		throwable.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}

	/**
	 * Warning: This method is dodgy! <br>
	 * <p>
	 * It is a hack and a workaround. <br>
	 * The method deserializes StackTraceInformation to build up an exception.
	 * <br>
	 * Within JDK1.4 <code>stackTraceElement</code> does not have an public
	 * constructor <br>
	 * or set-methods. <br>
	 * However, there is a way by accessing the private fields of the
	 * StackTraceElement. <br>
	 * This does only work when no or a very untight security Manager is used.
	 * <br>
	 * <p>
	 * The structure of Fields in the StackTraceElement is (in correct order
	 * starting with 0): <br>
	 * <code> <br>
	 * private java.lang.String java.lang.StackTraceElement.declaringClass<br>
	 * private java.lang.String java.lang.StackTraceElement.methodName<br>
	 * private java.lang.String java.lang.StackTraceElement.fileName<br>
	 * private int java.lang.StackTraceElement.lineNumber<br>
	 * </code><br>
	 * <p>
	 * The stacktrace string has the following format: <br>
	 * sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
	 * <br>
	 * which is <code>declaringClass.methodName(fileName:lineNumber)</code>.<br>
	 * 
	 * @param stacktrace
	 *            the stacktrace as concatenated string.
	 * @return StacktraceElement[] the deserialized Stacktrace.
	 * @throws ExceptionFailure
	 *             wrappes checked exceptions from java reflection.
	 */
	public static StackTraceElement[] parseStackTrace(String stacktrace) throws ExceptionFailure {

		// if we do not have a stacktrace (empty string) we don't need to do
		// anything.
		if (stacktrace == null || stacktrace.length() == 0) {
			return EMPTY_STACKTRACE_ELEMENTS;
		}

		String[] stackTraceElements = stacktrace.split(LINE_SEPARATOR);

		// if there are no stacktrace elements ..
		if (stackTraceElements.length == 0) {
			return EMPTY_STACKTRACE_ELEMENTS;
		}

		Constructor<?> constructor;

		try {
			constructor = StackTraceElement.class.getDeclaredConstructor(new Class[] { String.class, String.class,
					String.class, int.class });
		} catch (NoSuchMethodException e) {
			throw new ExceptionFailure("Could not get the constructor of StackTraceElement.", e);
		}

		try {

			// initialize the return vector
			StackTraceElement[] resultStackTrace = (StackTraceElement[]) Array.newInstance(StackTraceElement.class,
					stackTraceElements.length);

			// fill stacktrace for each available line
			for (int i = 0; i < stackTraceElements.length; i++) {

				String[] items = stackTraceElements[i].split(ITEM_SEPARATOR);
				assert items.length == 4;

				String declaringClass = items[0];
				String methodName = items[1];
				String fileName;
				if (items[2].equals("null")) {
					fileName = null;
				} else {
					fileName = items[2];
				}
				int lineNumber = Integer.parseInt(items[3]);

				// create a new instance for the array
				resultStackTrace[i] = (StackTraceElement) constructor.newInstance(new Object[] { declaringClass,
						methodName, fileName, lineNumber });
			}

			return resultStackTrace;

		} catch (Throwable e) {
			throw new ExceptionFailure("Parsing of exception failed.", e);
		}
	}

	/**
	 * @param map
	 *            the to get the values from.
	 * @return array of the map values. ordered by failure msg part names.
	 * @pre map != null
	 */
	public static Object[] createArrayFromMap(Map<String, ? extends Object> map) {
		Assert.isNotNull(map, "map should not be null");

		String[] messagePartNames = Failure.getMessagePartNames();
		Object[] args = new Object[messagePartNames.length];
		for (int i = 0; i < messagePartNames.length; i++) {
			args[i] = map.get(messagePartNames[i]);
		}
		return args;
	}

	/**
	 * Check the throwable passed and embed it into an exception failure if
	 * necessary.
	 * 
	 * @param t
	 *            the throwable
	 * @return failure instance
	 */
	public static Failure toFailure(Throwable t) {
		if (!(t instanceof Failure)) {
			String msgText = null;
			if (t instanceof AssertionFailedException) {
				msgText = "Assertion violated";
			} else {
				msgText = "Unexpected exception occurred";
			}
			return new ExceptionFailure(msgText + ": " + t, t);
		}

		return (Failure) t;
	}

	/**
	 * Check and rethrow the throwable passed. Imbed it into an exception
	 * failure if neccassary.
	 * 
	 * @param t
	 *            the throwable
	 */
	public static void toFailureAndThrow(Throwable t) {
		throw toFailure(t);
	}

	/**
	 * Get the java version running inside the environmenal JVM.
	 * 
	 * @return java version string
	 */
	public static String getJavaVersion() {
		return javaVersion;
	}

	/**
	 * Generate a unique ID satifying the requirements of a Failure instance.
	 * 
	 * @return a unique long value
	 */
	public static long generateFailureID() {
		synchronized (ExceptionHelper.class) {
			long newId = new Date().getTime(); // return with milliseconds of
			// current time
			while (newId <= lastId) {
				newId++;
			}
			lastId = newId;
			return newId;
		}
	}

	/**
	 * Get the default client message used for unexpected failure situations.
	 * 
	 * @return the client message string.
	 */
	public static String getDefaultClientMsg() {
		return DEFAULT_CLIENT_MSG;
	}

	/**
	 * get the primary cause of an exception (nested causes).
	 * 
	 * @param ex
	 * @return the primary cause exception or the exception itself if no cause
	 *         has been specified
	 */
	public static Throwable getPrimaryCause(Throwable ex) {
		if (ex.getCause() != null) {
			return getPrimaryCause(ex.getCause());
		} else {
			return ex;
		}
	}
}