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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.riena.ui.ridgets.AbstractCompositeRidget;
import org.eclipse.riena.ui.ridgets.IStatusbarNumberRidget;
import org.eclipse.riena.ui.ridgets.IStatusbarProcessRidget;
import org.eclipse.riena.ui.ridgets.IStatusbarRidget;
import org.eclipse.riena.ui.swt.Statusbar;
import org.eclipse.riena.ui.swt.StatusbarMessage;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.swt.graphics.Image;

/**
 * Ridget for the {@link Statusbar}.
 */
public class StatusbarRidget extends AbstractCompositeRidget implements IStatusbarRidget {

	private final static String LONG_EMPTY_STRING = "            "; //$NON-NLS-1$

	private String message;
	private IStatusbarNumberRidget statusbarNumberRidget;

	/**
	 * Creates a new instance of {@code Statusbar}.
	 */
	public StatusbarRidget() {
		super();
		message = LONG_EMPTY_STRING;
	}

	/**
	 * Returns the composite that displays the message in the status bar.
	 * 
	 * @return message composite
	 */
	public final StatusbarMessage getStatusbarMessage() {
		return ((Statusbar) getUIControl()).getMessageComposite();
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IStatusbarRidget#clear()
	 */
	public void clear() {
		// TODO icon
		setMessage(LONG_EMPTY_STRING);
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IStatusbarRidget#error(java.lang.String)
	 */
	public void error(String message) {
		setImage(LnfManager.getLnf().getImage(ILnfKeyConstants.STATUSBAR_ERROR_ICON));
		setMessage(message);
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IStatusbarRidget#getIcon()
	 */
	public String getIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IStatusbarRidget#getMessage()
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IStatusbarRidget#getStatusBarNumberRidget()
	 */
	public IStatusbarNumberRidget getStatusBarNumberRidget() {
		return statusbarNumberRidget;
	}

	/**
	 * @param statusBarNumberRidget
	 *            the statusBarNumberNumber to set
	 */
	public void setStatusBarNumberRidget(IStatusbarNumberRidget statusBarNumberRidget) {
		this.statusbarNumberRidget = statusBarNumberRidget;
		addRidget("statusBarNumberRidget", statusBarNumberRidget); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.ui.ridgets.IStatusbarRidget#getStatusBarProcessRidget()
	 */
	public IStatusbarProcessRidget getStatusBarProcessRidget() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IStatusbarRidget#hidePopups()
	 */
	public void hidePopups() {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IStatusbarRidget#info(java.lang.String)
	 */
	public void info(String message) {
		setImage(LnfManager.getLnf().getImage(ILnfKeyConstants.STATUSBAR_INFO_ICON));
		setMessage(message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.ui.ridgets.IStatusbarRidget#setIcon(java.lang.String)
	 */
	public void setIcon(String icon) {
		// TODO Auto-generated method stub

	}

	private void setImage(Image image) {
		getStatusbarMessage().setImage(image);
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IStatusbarRidget#setMessage(java.lang.String
	 *      )
	 */
	public void setMessage(String message) {
		if (message != null && !message.equals(this.message)) {
			this.message = message;
			getStatusbarMessage().setMessage(this.message);
		}
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IStatusbarRidget#warning(java.lang.String)
	 */
	public void warning(String message) {
		setImage(LnfManager.getLnf().getImage(ILnfKeyConstants.STATUSBAR_WARNING_ICON));
		setMessage(message);
	}

}
