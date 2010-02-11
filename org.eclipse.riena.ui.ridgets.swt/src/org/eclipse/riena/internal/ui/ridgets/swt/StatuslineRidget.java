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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import org.eclipse.riena.ui.ridgets.AbstractCompositeRidget;
import org.eclipse.riena.ui.ridgets.IStatuslineNumberRidget;
import org.eclipse.riena.ui.ridgets.IStatuslineRidget;
import org.eclipse.riena.ui.ridgets.IStatuslineUIProcessRidget;
import org.eclipse.riena.ui.ridgets.uibinding.IBindingPropertyLocator;
import org.eclipse.riena.ui.swt.Statusline;
import org.eclipse.riena.ui.swt.StatuslineMessage;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;

/**
 * Ridget for the {@link Statusline}.
 */
public class StatuslineRidget extends AbstractCompositeRidget implements IStatuslineRidget {

	private final static String LONG_EMPTY_STRING = "            "; //$NON-NLS-1$

	private static Image missingImage;

	private String message;
	private Image image;
	private IStatuslineNumberRidget statuslineNumberRidget;

	private IStatuslineUIProcessRidget statuslineUIProcessRidget;

	/**
	 * Creates a new instance of {@code StatuslineRidget}.
	 */
	public StatuslineRidget() {
		super();
		message = LONG_EMPTY_STRING;
	}

	@Override
	public Statusline getUIControl() {
		return (Statusline) super.getUIControl();
	}

	@Override
	public void setUIControl(Object uiControl) {
		super.setUIControl(uiControl);
		bindUIControl();
	}

	protected void bindUIControl() {
		updateUIIcon();
	}

	/**
	 * Returns the composite that displays the message in the status line.
	 * 
	 * @return message composite
	 */
	public final StatuslineMessage getStatuslineMessage() {
		return getUIControl().getMessageComposite();
	}

	public void clear() {
		setMessage(LONG_EMPTY_STRING);
		this.image = null;
		updateUIIcon();
	}

	public void error(String message) {
		setImage(LnfManager.getLnf().getImage(LnfKeyConstants.STATUSLINE_ERROR_ICON));
		setMessage(message);
	}

	public String getMessage() {
		return message;
	}

	public IStatuslineNumberRidget getStatuslineNumberRidget() {
		return statuslineNumberRidget;
	}

	/**
	 * @param statuslineNumberRidget
	 *            the statuslineNumberRidget to set
	 */
	public void setStatuslineNumberRidget(IStatuslineNumberRidget statuslineNumberRidget) {
		this.statuslineNumberRidget = statuslineNumberRidget;
		addRidget(Statusline.SL_NUMBER_RIDGET_ID, statuslineNumberRidget);
	}

	public void setStatuslineUIProcessRidget(IStatuslineUIProcessRidget statuslineUIProcessRidget) {
		addRidget(Statusline.SL_UIPROCES_RIDGET_ID, statuslineUIProcessRidget);
		this.statuslineUIProcessRidget = statuslineUIProcessRidget;
	}

	public IStatuslineUIProcessRidget getStatuslineUIProcessRidget() {
		return statuslineUIProcessRidget;
	}

	public void hidePopups() {
		// FIXME remove empty method-block or mark as deprecated
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IStatuslineRidget#info(java.lang.String)
	 */
	public void info(String message) {
		setImage(LnfManager.getLnf().getImage(LnfKeyConstants.STATUSLINE_INFO_ICON));
		setMessage(message);
	}

	public void setMessage(String message) {
		if (message != null && !message.equals(this.message)) {
			this.message = message;
			getStatuslineMessage().setMessage(this.message);
		}
	}

	public void warning(String message) {
		setImage(LnfManager.getLnf().getImage(LnfKeyConstants.STATUSLINE_WARNING_ICON));
		setMessage(message);
	}

	public String getID() {
		IBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();
		return locator.locateBindingProperty(getUIControl());
	}

	public final synchronized Image getMissingImage() {
		if (missingImage == null) {
			missingImage = ImageDescriptor.getMissingImageDescriptor().createImage();
		}
		return missingImage;
	}

	// helping methods
	//////////////////

	private void setImage(Image image) {
		this.image = image;
		updateUIIcon();
	}

	private void updateUIIcon() {
		Statusline control = getUIControl();
		if (control != null) {
			getStatuslineMessage().setImage(image);
		}
	}

}
