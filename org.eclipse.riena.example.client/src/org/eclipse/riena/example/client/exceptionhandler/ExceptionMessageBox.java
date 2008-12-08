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
package org.eclipse.riena.example.client.exceptionhandler;

import org.eclipse.riena.core.exception.IExceptionHandler;
import org.eclipse.riena.core.exception.IExceptionHandlerManager.Action;
import org.eclipse.riena.ui.ridgets.IMessageBoxRidget;
import org.eclipse.riena.ui.ridgets.IMessageBoxRidget.MessageBoxOption;
import org.eclipse.riena.ui.ridgets.IMessageBoxRidget.Type;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.MessageBox;

import org.eclipse.equinox.log.Logger;
import org.eclipse.swt.widgets.Display;

public class ExceptionMessageBox implements IExceptionHandler {

	public Action handleException(Throwable t, String msg, Logger logger) {
		MessageBox messageBox = new MessageBox(Display.getCurrent().getActiveShell());
		final IMessageBoxRidget messageBoxRidget = (IMessageBoxRidget) SwtRidgetFactory.createRidget(messageBox);

		messageBoxRidget.setTitle("Exception at Runtime");
		messageBoxRidget.setType(Type.ERROR);
		messageBoxRidget.setText(t.getMessage());
		IMessageBoxRidget.MessageBoxOption ok = new IMessageBoxRidget.MessageBoxOption("OK");
		IMessageBoxRidget.MessageBoxOption ignore = new IMessageBoxRidget.MessageBoxOption("Ignore");
		IMessageBoxRidget.MessageBoxOption printstack = new IMessageBoxRidget.MessageBoxOption("Print stacktrace and OK");
		messageBoxRidget.setOptions(new IMessageBoxRidget.MessageBoxOption[] { ok, ignore, printstack });
		MessageBoxOption show = messageBoxRidget.show();
		if (show.equals(printstack)) {
			t.printStackTrace();
		}
		if (show.equals(ignore)) {
			return Action.NotHandled;
		}
		return Action.Ok;
	}

	public Action handleUncaught(Throwable t, String msg, Logger logger) {
		return Action.NotHandled;
	}
}
