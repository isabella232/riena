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
package org.eclipse.riena.sample.app.client.rcpmail;

import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;
import org.eclipse.riena.ui.ridgets.swt.AbstractRidgetController;

/**
 * Ridget controller used by the {@link View}.
 * 
 * @see View
 */
public class ViewController extends AbstractRidgetController {

	/**
	 * Mock object holding the data of a single message.
	 */
	private static class MailMessage {
		private String subject = "This a message about the cool Eclipse RCP!"; //$NON-NLS-1$
		private String from = "nicole@mail.org"; //$NON-NLS-1$
		private String date = "10:34 am"; //$NON-NLS-1$
		private String message = "This RCP Application was generated from the PDE Plug-in Project wizard. This sample shows how to:\n" + //$NON-NLS-1$
				"- add a top-level menu and toolbar with actions\n" + //$NON-NLS-1$
				"- add keybindings to actions\n" + //$NON-NLS-1$
				"- create views that can't be closed and\n" + //$NON-NLS-1$
				"  multiple instances of the same view\n" + //$NON-NLS-1$
				"- perspectives with placeholders for new views\n" + //$NON-NLS-1$
				"- use the default about dialog\n" + //$NON-NLS-1$
				"- create a product definition\n"; //$NON-NLS-1$

		public String getDate() {
			return date;
		}

		public String getFrom() {
			return from;
		}

		public String getMessage() {
			return message;
		}

		public String getSubject() {
			return subject;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public void setFrom(String from) {
			this.from = from;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}
	}

	private MailMessage message = new MailMessage();

	public void configureRidgets() {
		ILabelRidget lblSubject = (ILabelRidget) getRidget("subject"); //$NON-NLS-1$
		lblSubject.bindToModel(PojoObservables.observeValue(message, "subject")); //$NON-NLS-1$
		lblSubject.updateFromModel();

		ILabelRidget lblFrom = (ILabelRidget) getRidget("from"); //$NON-NLS-1$
		lblFrom.bindToModel(PojoObservables.observeValue(message, "from")); //$NON-NLS-1$
		lblFrom.updateFromModel();

		ILabelRidget lblDate = (ILabelRidget) getRidget("date"); //$NON-NLS-1$
		lblDate.bindToModel(PojoObservables.observeValue(message, "date")); //$NON-NLS-1$
		lblDate.updateFromModel();

		ITextFieldRidget txtMessage = (ITextFieldRidget) getRidget("message"); //$NON-NLS-1$
		txtMessage.bindToModel(PojoObservables.observeValue(message, "message")); //$NON-NLS-1$
		txtMessage.setOutputOnly(true);
		txtMessage.updateFromModel();
	}

}
