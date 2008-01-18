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
package org.eclipse.riena.security.services.itest;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;

public class MyCallbackHandler implements CallbackHandler {

	private String name;
	private String password;

	public MyCallbackHandler(String name, String password) {
		this.name = name;
		this.password = password;
	}

	public void handle(Callback[] callbacks) {
		for (int i = 0; i < callbacks.length; i++) {
			if (callbacks[i] instanceof NameCallback) {
				NameCallback nc = (NameCallback) callbacks[i];
				nc.setName(name);
			} else {
				if (callbacks[i] instanceof PasswordCallback) {
					PasswordCallback pc = (PasswordCallback) callbacks[i];
					pc.setPassword(password.toCharArray());
				} else {
					if (callbacks[i] instanceof TextOutputCallback) {
						TextOutputCallback toc = (TextOutputCallback) callbacks[i];
						String typeAsString;
						// detect text output message type and translate it
						// to string
						switch (toc.getMessageType()) {
						case TextOutputCallback.INFORMATION:
							typeAsString = "information";
							break;
						case TextOutputCallback.ERROR:
							typeAsString = "error";
							break;
						case TextOutputCallback.WARNING:
							typeAsString = "warning";
							break;
						default:
							typeAsString = "unknown";
							break;
						}
						System.out.println(typeAsString + " " + toc.getMessage());
					}
				}
			}
		}
	}
}