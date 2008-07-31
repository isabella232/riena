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
import org.eclipse.riena.ui.ridgets.IStatuslineNumberRidget;
import org.eclipse.riena.ui.ridgets.IStatuslineProcessRidget;
import org.eclipse.riena.ui.ridgets.IStatuslineRidget;
import org.eclipse.riena.ui.swt.Statusline;
import org.eclipse.riena.ui.swt.StatuslineMessage;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.swt.graphics.Image;

/**
 * Ridget for the {@link Statusline}.
 */
public class StatuslineRidget extends AbstractCompositeRidget implements IStatuslineRidget {

	private final static String LONG_EMPTY_STRING = "            "; //$NON-NLS-1$

	private String message;
	private IStatuslineNumberRidget statuslineNumberRidget;

	/**
	 * Creates a new instance of {@code StatuslineRidget}.
	 */
	public StatuslineRidget() {
		super();
		message = LONG_EMPTY_STRING;
	}

	/**
	 * Returns the composite that displays the message in the status line.
	 * 
	 * @return message composite
	 */
	public final StatuslineMessage getStatuslineMessage() {
		return ((Statusline) getUIControl()).getMessageComposite();
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IStatuslineRidget#clear()
	 */
	public void clear() {
		// TODO icon
		setMessage(LONG_EMPTY_STRING);
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IStatuslineRidget#error(java.lang.String)
	 */
	public void error(String message) {
		setImage(LnfManager.getLnf().getImage(ILnfKeyConstants.STATUSLINE_ERROR_ICON));
		setMessage(message);
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IStatuslineRidget#getIcon()
	 */
	public String getIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IStatuslineRidget#getMessage()
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IStatuslineRidget#getStatuslineNumberRidget()
	 */
	public IStatuslineNumberRidget getStatuslineNumberRidget() {
		return statuslineNumberRidget;
	}

	/**
	 * @param statuslineNumberRidget
	 *            the statuslineNumberRidget to set
	 */
	public void setStatuslineNumberRidget(IStatuslineNumberRidget statuslineNumberRidget) {
		this.statuslineNumberRidget = statuslineNumberRidget;
		addRidget("statuslineNumberRidget", statuslineNumberRidget); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.ui.ridgets.IStatuslineRidget#getStatuslineProcessRidget
	 * ()
	 */
	public IStatuslineProcessRidget getStatuslineProcessRidget() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IStatuslineRidget#hidePopups()
	 */
	public void hidePopups() {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IStatuslineRidget#info(java.lang.String)
	 */
	public void info(String message) {
		setImage(LnfManager.getLnf().getImage(ILnfKeyConstants.STATUSLINE_INFO_ICON));
		setMessage(message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.ui.ridgets.IStatuslineRidget#setIcon(java.lang.String)
	 */
	public void setIcon(String icon) {
		// TODO Auto-generated method stub

	}

	private void setImage(Image image) {
		getStatuslineMessage().setImage(image);
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IStatuslineRidget#setMessage(java.lang.String
	 *      )
	 */
	public void setMessage(String message) {
		if (message != null && !message.equals(this.message)) {
			this.message = message;
			getStatuslineMessage().setMessage(this.message);
		}
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IStatuslineRidget#warning(java.lang.String)
	 */
	public void warning(String message) {
		setImage(LnfManager.getLnf().getImage(ILnfKeyConstants.STATUSLINE_WARNING_ICON));
		setMessage(message);
	}

}
