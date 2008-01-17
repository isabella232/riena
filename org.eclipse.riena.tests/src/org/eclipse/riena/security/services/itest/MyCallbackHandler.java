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

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextOutputCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

public class MyCallbackHandler implements CallbackHandler {

	private String name;
	private String password;

	public MyCallbackHandler(String name, String password) {
		this.name = name;
		this.password = password;
	}

	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		for (int i = 0; i < callbacks.length; i++) {
			if (callbacks[i] instanceof TextOutputCallback) {

				// display the message according to the specified type
				TextOutputCallback toc = (TextOutputCallback) callbacks[i];
				switch (toc.getMessageType()) {
				case TextOutputCallback.INFORMATION:
					System.out.println(toc.getMessage());
					break;
				case TextOutputCallback.ERROR:
					System.out.println("ERROR: " + toc.getMessage());
					break;
				case TextOutputCallback.WARNING:
					System.out.println("WARNING: " + toc.getMessage());
					break;
				default:
					throw new IOException("Unsupported message type: " + toc.getMessageType());
				}

			} else if (callbacks[i] instanceof NameCallback) {
				// prompt the user for a username
				NameCallback nc = (NameCallback) callbacks[i];

				// System.err.print(nc.getPrompt());
				// System.err.flush();
				// nc.setName((new BufferedReader(new
				// InputStreamReader(System.in))).readLine());
				nc.setName(name);

			} else if (callbacks[i] instanceof PasswordCallback) {

				// prompt the user for sensitive information
				PasswordCallback pc = (PasswordCallback) callbacks[i];
				// System.err.print(pc.getPrompt());
				// System.err.flush();
				// pc.setPassword((new BufferedReader(new
				// InputStreamReader(System.in))).readLine().toCharArray());
				pc.setPassword(password.toCharArray());
			}

		}
	}
}