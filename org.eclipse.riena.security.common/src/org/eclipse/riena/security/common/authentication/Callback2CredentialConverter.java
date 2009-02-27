/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.security.common.authentication;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.ChoiceCallback;
import javax.security.auth.callback.ConfirmationCallback;
import javax.security.auth.callback.LanguageCallback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.TextInputCallback;
import javax.security.auth.callback.TextOutputCallback;

import org.eclipse.riena.security.common.authentication.credentials.AbstractCredential;
import org.eclipse.riena.security.common.authentication.credentials.ChoiceCredential;
import org.eclipse.riena.security.common.authentication.credentials.ConfirmationCredential;
import org.eclipse.riena.security.common.authentication.credentials.CustomCredential;
import org.eclipse.riena.security.common.authentication.credentials.LanguageCredential;
import org.eclipse.riena.security.common.authentication.credentials.NameCredential;
import org.eclipse.riena.security.common.authentication.credentials.PasswordCredential;
import org.eclipse.riena.security.common.authentication.credentials.TextInputCredential;
import org.eclipse.riena.security.common.authentication.credentials.TextOutputCredential;

/**
 * this class converts between JAAS callbacks and credential object so that they
 * can be easily transported to a remote service
 */
public final class Callback2CredentialConverter {

	private Callback2CredentialConverter() {
		// utility
	}

	/**
	 * Convert Callback objects to AbstractCredential objects
	 * 
	 * @param callbacks
	 * @return
	 */
	public static AbstractCredential[] callbacks2Credentials(Callback[] callbacks) {
		AbstractCredential[] creds = new AbstractCredential[callbacks.length];
		int i = 0;
		for (Callback cb : callbacks) {
			if (cb instanceof NameCallback) {
				NameCallback ncb = (NameCallback) cb;
				NameCredential nc = new NameCredential(ncb.getPrompt(), ncb.getDefaultName());
				nc.setName(ncb.getName());
				creds[i++] = nc;
			} else if (cb instanceof PasswordCallback) {
				PasswordCallback pcb = (PasswordCallback) cb;
				PasswordCredential pc = new PasswordCredential(pcb.getPrompt(), pcb.isEchoOn());
				pc.setPassword(pcb.getPassword());
				creds[i++] = pc;
			} else if (cb instanceof ConfirmationCallback) {
				ConfirmationCallback ccb = (ConfirmationCallback) cb;
				ConfirmationCredential ccc = new ConfirmationCredential(ccb.getMessageType(), ccb.getOptionType(), ccb
						.getDefaultOption());
				ccc.setSelectedIndex(ccb.getSelectedIndex());
				creds[i++] = ccc;
			} else if (cb instanceof TextInputCallback) {
				TextInputCallback ticb = (TextInputCallback) cb;
				TextInputCredential tic = new TextInputCredential(ticb.getPrompt(), ticb.getDefaultText());
				tic.setText(ticb.getText());
				creds[i++] = tic;
			} else if (cb instanceof TextOutputCallback) {
				TextOutputCallback tocb = (TextOutputCallback) cb;
				TextOutputCredential toc = new TextOutputCredential(tocb.getMessageType(), tocb.getMessage());
				creds[i++] = toc;
			} else if (cb instanceof LanguageCallback) {
				LanguageCallback lcb = (LanguageCallback) cb;
				LanguageCredential lc = new LanguageCredential(lcb.getLocale());
				creds[i++] = lc;
			} else if (cb instanceof ChoiceCallback) {
				ChoiceCallback ccb = (ChoiceCallback) cb;
				ChoiceCredential cc = new ChoiceCredential(ccb.getPrompt(), ccb.getChoices(), ccb.getDefaultChoice(),
						ccb.allowMultipleSelections());
				cc.setSelections(ccb.getSelectedIndexes());
				creds[i++] = cc;
			} else {
				CustomCredential cc = new CustomCredential(cb);
				creds[i++] = cc;
			}

		}

		return creds;
	}

	public static Callback[] credentials2Callbacks(AbstractCredential[] credentials) {
		Callback[] callbacks = new Callback[credentials.length];
		int i = 0;
		for (AbstractCredential cred : credentials) {
			if (cred instanceof NameCredential) {
				NameCredential nc = (NameCredential) cred;
				NameCallback ncb;
				if (nc.getDefaultName() == null) {
					ncb = new NameCallback(nc.getPrompt());
				} else {
					ncb = new NameCallback(nc.getPrompt(), nc.getDefaultName());
				}
				ncb.setName(nc.getName());
				callbacks[i++] = ncb;
			} else if (cred instanceof PasswordCredential) {
				PasswordCredential pc = (PasswordCredential) cred;
				PasswordCallback pcb = new PasswordCallback(pc.getPrompt(), pc.isEchoOn());
				pcb.setPassword(pc.getPassword());
				callbacks[i++] = pcb;
			} else if (cred instanceof ConfirmationCredential) {
				ConfirmationCredential cc = (ConfirmationCredential) cred;
				ConfirmationCallback ccb = new ConfirmationCallback(cc.getMessageType(), cc.getOptionType(), cc
						.getDefaultOption());
				ccb.setSelectedIndex(cc.getSelectedIndex());
				callbacks[i++] = ccb;
			} else if (cred instanceof TextInputCredential) {
				TextInputCredential tic = (TextInputCredential) cred;
				TextInputCallback ticb = new TextInputCallback(tic.getPrompt(), tic.getDefaultText());
				ticb.setText(tic.getText());
				callbacks[i++] = ticb;
			} else if (cred instanceof TextOutputCredential) {
				TextOutputCredential toc = (TextOutputCredential) cred;
				TextOutputCallback tocb = new TextOutputCallback(toc.getMessageType(), toc.getMessage());
				callbacks[i++] = tocb;
			} else if (cred instanceof LanguageCredential) {
				LanguageCredential lc = (LanguageCredential) cred;
				LanguageCallback lcb = new LanguageCallback();
				lcb.setLocale(lc.getLocale());
				callbacks[i++] = lcb;
			} else if (cred instanceof ChoiceCredential) {
				ChoiceCredential cc = (ChoiceCredential) cred;
				ChoiceCallback ccb = new ChoiceCallback(cc.getPrompt(), cc.getChoices(), cc.getDefaultChoice(), cc
						.isMultipleSelectionsAllowed());
				int[] selections = cc.getSelections();
				if (selections.length == 1) {
					ccb.setSelectedIndex(selections[0]);
				} else {
					ccb.setSelectedIndexes(cc.getSelections());
				}
				callbacks[i++] = ccb;
			} else if (cred instanceof CustomCredential) {
				callbacks[i++] = ((CustomCredential) cred).getCallback();
			}
		}

		return callbacks;
	}
}
