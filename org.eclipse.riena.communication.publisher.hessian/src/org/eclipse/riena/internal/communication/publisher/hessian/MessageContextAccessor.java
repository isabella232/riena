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
package org.eclipse.riena.internal.communication.publisher.hessian;

/**
 * @author [user:firstname] [user:lastname]
 * 
 */
public class MessageContextAccessor {

	private static ThreadLocal<MessageContext> messageContexts = new ThreadLocal<MessageContext>();

	public static MessageContext getMessageContext() {
		return messageContexts.get();
	}

	public static void setMessageContext(MessageContext msgCtx) {
		messageContexts.set(msgCtx);
	}

}
