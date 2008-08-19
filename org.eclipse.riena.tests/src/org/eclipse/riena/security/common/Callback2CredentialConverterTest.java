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

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextInputCallback;

import junit.framework.TestCase;

import org.eclipse.riena.security.common.authentication.Callback2CredentialConverter;
import org.eclipse.riena.security.common.authentication.credentials.AbstractCredential;
import org.eclipse.riena.security.common.authentication.credentials.CustomCredential;
import org.eclipse.riena.security.common.authentication.credentials.NameCredential;
import org.eclipse.riena.security.common.authentication.credentials.PasswordCredential;
import org.eclipse.riena.security.common.authentication.credentials.TextInputCredential;

/**
 * 
 */
public class Callback2CredentialConverterTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testSimpleName1() {
		Callback[] callbacks = new Callback[] { new NameCallback("userid", "cca") };

		AbstractCredential[] credentials = Callback2CredentialConverter.callbacks2Credentials(callbacks);
		assertTrue(credentials.length == 1);
		assertTrue(credentials[0] instanceof NameCredential);
		NameCredential nCred = (NameCredential) credentials[0];
		assertTrue(nCred.getPrompt().equals("userid"));
		assertTrue(nCred.getDefaultName().equals("cca"));
		assertTrue(nCred.getName() == null);

		Callback[] callbacksReturned = Callback2CredentialConverter.credentials2Callbacks(credentials);
		assertTrue(callbacksReturned.length == 1);
		assertTrue(callbacksReturned[0] instanceof NameCallback);
		NameCallback ncb = (NameCallback) callbacksReturned[0];
		assertTrue(ncb.getPrompt().equals("userid"));
		assertTrue(ncb.getDefaultName().equals("cca"));
		assertTrue(ncb.getName() == null);
	}

	public void testSimpleName2() {
		NameCallback temp = new NameCallback("userid", "cca");
		temp.setName("christian");
		Callback[] callbacks = new Callback[] { temp };

		AbstractCredential[] credentials = Callback2CredentialConverter.callbacks2Credentials(callbacks);
		assertTrue(credentials.length == 1);
		assertTrue(credentials[0] instanceof NameCredential);
		NameCredential nCred = (NameCredential) credentials[0];
		assertTrue(nCred.getPrompt().equals("userid"));
		assertTrue(nCred.getDefaultName().equals("cca"));
		assertTrue(nCred.getName().equals("christian"));

		Callback[] callbacksReturned = Callback2CredentialConverter.credentials2Callbacks(credentials);
		assertTrue(callbacksReturned.length == 1);
		assertTrue(callbacksReturned[0] instanceof NameCallback);
		NameCallback ncb = (NameCallback) callbacksReturned[0];
		assertTrue(ncb.getPrompt().equals("userid"));
		assertTrue(ncb.getDefaultName().equals("cca"));
		assertTrue(ncb.getName().equals("christian"));
	}

	public void testMultipleCredentials() {
		Callback[] callbacks = new Callback[] { new PasswordCallback("password", false),
				new TextInputCallback("textinp", "default-text") };

		AbstractCredential[] credentials = Callback2CredentialConverter.callbacks2Credentials(callbacks);
		assertTrue(credentials.length == 2);
		assertTrue(credentials[0] instanceof PasswordCredential);
		assertTrue(credentials[1] instanceof TextInputCredential);
		PasswordCredential pc = (PasswordCredential) credentials[0];
		TextInputCredential tic = (TextInputCredential) credentials[1];
		assertTrue(pc.getPrompt().equals("password"));
		assertFalse(pc.isEchoOn());
		assertTrue(tic.getPrompt().equals("textinp"));
		assertTrue(tic.getDefaultText().equals("default-text"));
		assertTrue(tic.getText() == null);

		Callback[] callbacksReturned = Callback2CredentialConverter.credentials2Callbacks(credentials);
		assertTrue(callbacksReturned.length == 2);
		assertTrue(callbacksReturned[0] instanceof PasswordCallback);
		assertTrue(callbacksReturned[1] instanceof TextInputCallback);
		PasswordCallback pcb = (PasswordCallback) callbacksReturned[0];
		TextInputCallback ticb = (TextInputCallback) callbacksReturned[1];
		assertTrue(pcb.getPrompt().equals("password"));
		assertFalse(pcb.isEchoOn());
		assertTrue(ticb.getPrompt().equals("textinp"));
		assertTrue(ticb.getDefaultText().equals("default-text"));
		assertTrue(ticb.getText() == null);
	}

	public void testCustomCredentials() {
		Callback[] callbacks = new Callback[] { new MyCallback("valueOne", "valueTwo") };

		AbstractCredential[] credentials = Callback2CredentialConverter.callbacks2Credentials(callbacks);

		assertTrue(credentials.length == 1);
		assertTrue(credentials[0] instanceof CustomCredential);

		Callback[] callbacksReturned = Callback2CredentialConverter.credentials2Callbacks(credentials);
		assertTrue(callbacksReturned.length == 1);
		assertTrue(callbacksReturned[0] instanceof MyCallback);
		MyCallback mycb = (MyCallback) callbacksReturned[0];
		assertTrue(mycb.getValue1().equals("valueOne"));
		assertTrue(mycb.getValue2().equals("valueTwo"));
	}

	public static class MyCallback implements Callback {
		private String value1;
		private String value2;

		public MyCallback(String value1, String value2) {
			this.value1 = value1;
			this.value2 = value2;
		}

		public String getValue1() {
			return value1;
		}

		public String getValue2() {
			return value2;
		}

	}

}
