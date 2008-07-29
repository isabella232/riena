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

import java.text.MessageFormat;
import java.util.Date;

/**
 * Exception for handling of non-recoverable errors/problems (resource
 * unavailability, runtime exceptions and other system errors). This class
 * extends <code>java.lang.RuntimeException</code> and is therefore an unchecked
 * exception.
 * 
 * @see java.lang.RuntimeException
 */
public abstract class Failure extends RuntimeException {

	private static final String UNKNOWN = "<unknown>";
	private static final String NONE = "<none>";
	private static final String NO_DETAILS = "No details.";

	/**
	 * version ID (controlled by CVS).
	 */
	public static final String VERSION_ID = "$Id$";

	private String javaVersion = UNKNOWN;

	private String callerClassName = UNKNOWN;
	private String callerClassVersion = UNKNOWN;
	private String callerMethodName = UNKNOWN;

	private String id = "";
	private Date timestamp;

	private String clientMsg = "";
	private String serverMsg = "";

	private String nativeErrorCode = NONE;

	private static final int HEX_BASE = 16;

	// package protected for unit test!!
	static final String[] MESSAGE_PART_NAMES = { "serverMsg", "cause", "id", "timestamp", "callerClassName",
			"callerClassVersion", "callerMethodName", "nativeErrorCode", "clientMsg", "javaVersion", "stacktrace" };

	/**
	 * constructor.
	 * 
	 * @param cause
	 *            exception which has caused this Failure.
	 * @param msg
	 *            message text or message code.
	 * @param args
	 *            message parameters.
	 */
	public Failure(String msg, Object[] args, Throwable cause) {
		super(msg, cause);
		setDetails(msg, args);
	}

	/**
	 * constructor.
	 * 
	 * @param msg
	 *            message text or message code.
	 */
	public Failure(String msg) {
		// Note: In the case that no cause is given it neccesary to call the
		// super constructor
		// with the same signature. Otherwise it would not be possible to call
		// the
		// initCause() method without getting an IllegalStateException!!!!
		// Every setting of cause (also with null) in Throwable will lock the
		// cause field of Throwable!
		super(msg);
		setDetails(msg, null);
	}

	/**
	 * constructor.
	 * 
	 * @param cause
	 *            exception which has caused this Failure.
	 * @param msg
	 *            message text or message code.
	 */
	public Failure(String msg, Throwable cause) {
		this(msg, null, cause);
	}

	/**
	 * constructor.
	 * 
	 * @param cause
	 *            exception which has caused this Failure.
	 * @param msg
	 *            message text or message code.
	 * @param arg1
	 *            single message parameter.
	 */
	public Failure(String msg, Object arg1, Throwable cause) {
		this(msg, new Object[] { arg1 }, cause);
	}

	/**
	 * constructor.
	 * 
	 * @param cause
	 *            exception which has caused this Failure.
	 * @param msg
	 *            message text or message code.
	 * @param arg1
	 *            message parameter 1.
	 * @param arg2
	 *            message parameter 2.
	 */
	public Failure(String msg, Object arg1, Object arg2, Throwable cause) {
		this(msg, new Object[] { arg1, arg2 }, cause);
	}

	/**
	 * determine and set the exception details.
	 */
	private void setDetails(String msg, Object[] args) {
		setTimestamp(new Date());
		// setId(Long.toString(ExceptionHelper.generateFailureID(),
		// HEX_BASE).toUpperCase(Locale.getDefault()));
		// setJavaVersion(ExceptionHelper.getJavaVersion());
		// setServerMsg(getFormattedMsgText(msg, args));
		// setClientMsg(ExceptionHelper.getDefaultClientMsg().replaceAll(
		// ExceptionHelper.PLACEHOLDER_FAILURE_ID_REGEX,
		// getId()));
		// setCallerClassName(ExceptionHelper.getCallerByRef(this));
		//setCallerMethodName(ExceptionHelper.getCallerMethod(getCallerClassName
		// ()));
		//setCallerClassVersion(ExceptionHelper.getVersionId(getCallerClassName(
		// )));
	}

	/**
	 * Format Messagetext.
	 * 
	 * @param msg
	 *            message text or code.
	 * @return formatted message text.
	 */
	private String getFormattedMsgText(String msg, Object[] args) {
		if (msg == null || args == null) {
			return msg;
		} else {
			return new MessageFormat(msg).format(args);
		}
	}

	/**
	 * Get value of attribute <code>javaVersion</code>.
	 * 
	 * @return value of attribute javaVersion.
	 */
	public String getJavaVersion() {
		return javaVersion;
	}

	/**
	 * Set value of attribute <code>javaVersion</code>.
	 * 
	 * @param javaVersion
	 *            the java version.
	 */
	private void setJavaVersion(String javaVersion) {
		this.javaVersion = javaVersion;
	}

	/**
	 * Set value of attribute <code>callerClassName</code>.
	 * 
	 * @param callerClassName
	 *            the parameter value of caller class name.
	 */
	private void setCallerClassName(String callerClassName) {
		this.callerClassName = callerClassName;
	}

	/**
	 * Get value of attribute <code>callerClassName</code>.
	 * 
	 * @return value of attribute caller class name.
	 */
	public String getCallerClassName() {
		return this.callerClassName;
	}

	/**
	 * Set value of attribute <code>callerClassVersion</code>.
	 * 
	 * @param callerClassVersion
	 *            parameter value of caller class version.
	 */
	private void setCallerClassVersion(String callerClassVersion) {
		this.callerClassVersion = callerClassVersion;
	}

	/**
	 * Get value of attribute <code>callerClassVersion</code>.
	 * 
	 * @return value of attribute caller class version.
	 */
	public String getCallerClassVersion() {
		return this.callerClassVersion;
	}

	/**
	 * Set value of attribute <code>callerMethodName</code>.
	 * 
	 * @param callerMethodName
	 *            parameter value of caller method name.
	 */
	private void setCallerMethodName(String callerMethodName) {
		this.callerMethodName = callerMethodName;
	}

	/**
	 * Get value of attribute caller method name.
	 * 
	 * @return value of attribute caller method name.
	 */
	public String getCallerMethodName() {
		return this.callerMethodName;
	}

	/**
	 * Set value of attribute <code>id</code>.
	 * 
	 * @param id
	 *            parameter value of id.
	 */
	private void setId(String id) {
		this.id = id;
	}

	/**
	 * Get value of attribute <code>id</code>. The <code>id</code> is an unique
	 * identifier for this Failure instance. The <code>id</code> is embedded
	 * inside the server and client message text and can be used as a reference
	 * (i.e. inside the logging file) to address the problem.
	 * 
	 * @return value of attribute id.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Set value of attribute <code>timestamp</code>.
	 * 
	 * @param timestamp
	 *            parameter value of timestamp.
	 */
	private void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Get value of attribute <code>timestamp</code>.
	 * 
	 * @return value of attribute timestamp.
	 */
	public Date getTimestamp() {
		return new Date(this.timestamp.getTime());
	}

	/**
	 * Set value of attribute <code>clientMsg</code>.
	 * 
	 * @param clientMsg
	 *            parameter value of clientMsg.
	 */
	public void setClientMsg(String clientMsg) {
		this.clientMsg = clientMsg;
	}

	/**
	 * Get value of attribute <code>clientMsg</code>.
	 * 
	 * @return value of attribute clientMsg.
	 */
	public String getClientMsg() {
		return this.clientMsg;
	}

	/**
	 * Set value of attribute <code>serverMsg</code>.
	 * 
	 * @param serverMsg
	 *            parameter value of serverMsg.
	 */
	public void setServerMsg(String serverMsg) {
		this.serverMsg = serverMsg;
	}

	/**
	 * Get value of attribute <code>serverMsg</code>.
	 * 
	 * @return value of attribute serverMsg.
	 */
	public String getServerMsg() {
		if (this.serverMsg == null) {
			return "";
		} else {
			return this.serverMsg;
		}
	}

	/**
	 * Set value of attribute <code>nativeErrorCode</code>.
	 * 
	 * @param nativeErrorCode
	 *            parameter value of native error code.
	 */
	public void setNativeErrorCode(String nativeErrorCode) {
		this.nativeErrorCode = nativeErrorCode;
	}

	/**
	 * Get value of attribute <code>nativeErrorCode</code>.
	 * 
	 * @return value of attribute native error code.
	 */
	public String getNativeErrorCode() {
		return this.nativeErrorCode;
	}

	/**
	 * Get the message part names. Note: This method makes a copy of an internal
	 * string array, to protect it from malicious changes!
	 * 
	 * @return the message part names.
	 */
	public static String[] getMessagePartNames() {
		String[] copy = new String[MESSAGE_PART_NAMES.length];
		System.arraycopy(MESSAGE_PART_NAMES, 0, copy, 0, MESSAGE_PART_NAMES.length);
		return copy;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	// commented out so that we see the toString from Throwable until we have a
	// decent implementation TODO
	// public String toString() {
	//
	// StringBuilder failureText = new StringBuilder(this.getClass().getName());
	// failureText.append(":\n - serverMsg \t\t\t= ");
	// failureText.append(getServerMsg());
	// failureText.append("\n - cause \t\t\t\t= ");
	// failureText.append(getCauseClassName());
	// failureText.append("\n - id \t\t\t\t\t= ");
	// failureText.append(getId());
	// failureText.append("\n - timestamp \t\t\t= ");
	// failureText.append(DateFormat.getInstance().format(timestamp));
	// failureText.append("\n - callerClassName \t= ");
	// failureText.append(getCallerClassName());
	// failureText.append("\n - callerClassVersion \t= ");
	// failureText.append(getCallerClassVersion());
	// failureText.append("\n - callerMethodName \t= ");
	// failureText.append(getCallerMethodName());
	// failureText.append("\n - nativeErrorCode \t= ");
	// failureText.append(getNativeErrorCode());
	// failureText.append("\n - clientMsg \t\t\t= ");
	// failureText.append(getClientMsg());
	// failureText.append("\n - javaVersion \t\t= ");
	// failureText.append(getJavaVersion());
	// return failureText.toString();
	// }
	private String getCauseClassName() {
		if (getCause() != null) {
			return getCause().getClass().getName();
		} else {
			return null;
		}

	}

	/**
	 * Get the full stacktrace of all nested exceptions chained to this Failure
	 * instance.
	 * 
	 * @return the full stacktrace in string representation.
	 */
	// public String getRecursiveStacktrace() {
	// return ExceptionHelper.getRecursiveStacktrace(this);
	// }
	/**
	 * This method is called to clear details that were automatically set by the
	 * constructor but are in fact unknown.
	 */
	public void clearDetails() {
		callerClassName = UNKNOWN;
		callerClassVersion = UNKNOWN;
		callerMethodName = UNKNOWN;
		nativeErrorCode = UNKNOWN;
		clientMsg = NO_DETAILS;
		javaVersion = UNKNOWN;
	}

	/**
	 * @param args
	 * @return
	 */
	private static Object nullify(Object[] args) {
		if (args == null || args.length == 0) {
			return null;
		} else {
			return args[0];
		}
	}
}