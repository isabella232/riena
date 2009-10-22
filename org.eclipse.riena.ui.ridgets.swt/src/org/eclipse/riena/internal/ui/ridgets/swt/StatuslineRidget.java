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

	/**
	 * @see org.eclipse.riena.ui.ridgets.IStatuslineRidget#clear()
	 */
	public void clear() {
		setMessage(LONG_EMPTY_STRING);
		this.image = null;
		updateUIIcon();
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IStatuslineRidget#error(java.lang.String)
	 */
	public void error(String message) {
		setImage(LnfManager.getLnf().getImage(LnfKeyConstants.STATUSLINE_ERROR_ICON));
		setMessage(message);
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
		addRidget("numberRidget", statuslineNumberRidget); //$NON-NLS-1$
	}

	private IStatuslineUIProcessRidget statuslineUIProcessRidget;

	public void setStatuslineUIProcessRidget(IStatuslineUIProcessRidget statuslineUIProcessRidget) {
		addRidget("UIProcessRidget", statuslineUIProcessRidget); //$NON-NLS-1$
		this.statuslineUIProcessRidget = statuslineUIProcessRidget;
	}

	public IStatuslineUIProcessRidget getStatuslineUIProcessRidget() {
		return statuslineUIProcessRidget;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.ridgets.IStatuslineRidget#hidePopups()
	 */
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

	private void setImage(Image image) {
		this.image = image;
		updateUIIcon();
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
		setImage(LnfManager.getLnf().getImage(LnfKeyConstants.STATUSLINE_WARNING_ICON));
		setMessage(message);
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#configureRidgets()
	 */
	@Override
	public void configureRidgets() {
		// nothing to do
	}

	@Override
	public String getID() {
		if (getUIControl() != null) {
			IBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();
			return locator.locateBindingProperty(getUIControl());
		}

		return null;
	}

	public final synchronized Image getMissingImage() {
		if (missingImage == null) {
			missingImage = ImageDescriptor.getMissingImageDescriptor().createImage();
		}
		return missingImage;
	}

	private void updateUIIcon() {
		Statusline control = getUIControl();
		if (control != null) {
			getStatuslineMessage().setImage(image);
		}
	}

}
