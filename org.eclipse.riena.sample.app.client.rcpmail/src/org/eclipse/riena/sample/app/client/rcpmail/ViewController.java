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
 * TODO [ev] docs
 */
public class ViewController extends AbstractRidgetController {

	private static class MailMessage {
		private String subject = "This a message about the cool Eclipse RCP!";
		private String from = "nicole@mail.org";
		private String date = "10:34 am";
		private String message = "This RCP Application was generated from the PDE Plug-in Project wizard. This sample shows how to:\n"+
		"- add a top-level menu and toolbar with actions\n"+
		"- add keybindings to actions\n" +
		"- create views that can't be closed and\n"+
		"  multiple instances of the same view\n"+
		"- perspectives with placeholders for new views\n"+
		"- use the default about dialog\n"+
		"- create a product definition\n";
		
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
	
	public void afterBind() {
		ILabelRidget lblSubject = (ILabelRidget) getRidget("subject");
		lblSubject.bindToModel(PojoObservables.observeValue(message, "subject"));
		lblSubject.updateFromModel();
		
		ILabelRidget lblFrom = (ILabelRidget) getRidget("from");
		lblFrom.bindToModel(PojoObservables.observeValue(message, "from"));
		lblFrom.updateFromModel();
		
		ILabelRidget lblDate = (ILabelRidget) getRidget("date");
		lblDate.bindToModel(PojoObservables.observeValue(message, "date"));
		lblDate.updateFromModel();
		
		ITextFieldRidget txtMessage = (ITextFieldRidget) getRidget("message");
		txtMessage.bindToModel(PojoObservables.observeValue(message, "message"));
		txtMessage.setOutputOnly(true);
		txtMessage.updateFromModel();
	}

}
