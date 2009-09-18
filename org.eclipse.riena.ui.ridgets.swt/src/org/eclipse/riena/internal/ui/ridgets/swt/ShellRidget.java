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
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.util.ListenerList;
import org.eclipse.riena.ui.core.marker.HiddenMarker;
import org.eclipse.riena.ui.ridgets.AbstractMarkerSupport;
import org.eclipse.riena.ui.ridgets.IWindowRidget;
import org.eclipse.riena.ui.ridgets.UIBindingFailure;
import org.eclipse.riena.ui.ridgets.listener.IWindowRidgetListener;
import org.eclipse.riena.ui.ridgets.swt.AbstractSWTWidgetRidget;
import org.eclipse.riena.ui.ridgets.uibinding.IBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.ImageStore;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;

/**
 * The ridget for a Shell control.
 */
public class ShellRidget extends AbstractSWTWidgetRidget implements IWindowRidget {

	private static Image missingImage;
	private boolean closeable;
	private boolean titleAlreadyInitialized;
	private String title;
	private String icon;
	private ListenerList<IWindowRidgetListener> windowRidgetListeners;
	private ShellListener shellListener;

	public ShellRidget() {

		super();
		titleAlreadyInitialized = false;
		title = ""; //$NON-NLS-1$
		closeable = true;
		windowRidgetListeners = new ListenerList<IWindowRidgetListener>(IWindowRidgetListener.class);
		shellListener = new RidgetShellListener();
	}

	public ShellRidget(Shell shell) {
		this();
		setUIControl(shell);
	}

	@Override
	public Shell getUIControl() {
		return (Shell) super.getUIControl();
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.swt.AbstractSWTWidgetRidget#checkUIControl(java.lang.Object)
	 */
	@Override
	protected void checkUIControl(Object uiControl) {
		if (uiControl != null && !(uiControl instanceof Shell)) {
			throw new UIBindingFailure("uiControl of a ShellRidget must be a Shell but was a " //$NON-NLS-1$
					+ uiControl.getClass().getSimpleName());
		}
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.swt.AbstractSWTWidgetRidget#unbindUIControl()
	 */
	@Override
	protected void unbindUIControl() {
		savedVisibleState = isVisible();
		removeShellListener();
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.swt.AbstractSWTWidgetRidget#bindUIControl()
	 */
	@Override
	protected void bindUIControl() {

		addShellListener();
		updateToolTip();
		updateCloseable();
		updateTitle();
		updateIcon();
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.swt.AbstractSWTWidgetRidget#createMarkerSupport()
	 */
	@Override
	protected AbstractMarkerSupport createMarkerSupport() {
		return new BasicMarkerSupport(this, propertyChangeSupport);
	}

	private void addShellListener() {
		if (getUIControl() != null) {
			getUIControl().addShellListener(shellListener);
		}
	}

	private void removeShellListener() {
		if (getUIControl() != null) {
			getUIControl().removeShellListener(shellListener);
		}
	}

	public void addWindowRidgetListener(IWindowRidgetListener listener) {
		windowRidgetListeners.add(listener);
	}

	public void removeWindowRidgetListener(IWindowRidgetListener listener) {
		windowRidgetListeners.remove(listener);
	}

	public void dispose() {
		getUIControl().dispose();
	}

	public void setTitle(String title) {
		titleAlreadyInitialized = true;
		if (title != null && !this.title.equals(title)) {
			this.title = title;
			updateTitle();
		}
	}

	private void updateTitle() {
		if (getUIControl() != null) {
			if (titleAlreadyInitialized) {
				getUIControl().setText(title);
			} else {
				titleAlreadyInitialized = true;
				title = getUIControl().getText();
			}
		}
	}

	public String getTitle() {
		return title;
	}

	public void setIcon(String icon) {
		String oldIcon = this.icon;
		this.icon = icon;
		if (hasChanged(oldIcon, icon)) {
			updateIcon();
		}
	}

	/**
	 * Updates the icon of the UI control.
	 */
	private void updateIcon() {
		Shell control = getUIControl();
		if (control != null) {
			Image image = null;
			if (icon != null) {
				image = getManagedImage(icon);
			}
			control.setImage(image);
		}
	}

	@Override
	protected Image getManagedImage(String key) {
		Image image = ImageStore.getInstance().getImage(key);
		if (image == null) {
			image = getMissingImage();
		}
		return image;
	}

	public final synchronized Image getMissingImage() {
		if (missingImage == null) {
			missingImage = ImageDescriptor.getMissingImageDescriptor().createImage();
		}
		return missingImage;
	}

	/**
	 * Compares the two given values.
	 * 
	 * @param oldValue
	 *            - old value
	 * 
	 * @param newValue
	 *            - new value
	 * 
	 * @return true, if value has changed; otherwise false
	 */
	@Override
	protected boolean hasChanged(Object oldValue, Object newValue) {

		if (oldValue == null && newValue == null) {
			return false;
		}
		if (oldValue == null || newValue == null) {
			return true;
		}
		return !oldValue.equals(newValue);
	}

	/*
	 * @see org.eclipse.riena.ui.ridgets.IWindowRidget#setDefaultButton(JButton)
	 */
	public void setDefaultButton(Object defaultButton) {
		if (defaultButton instanceof Button) {
			getUIControl().setDefaultButton((Button) defaultButton);
		}
	}

	/*
	 * @see org.eclipse.riena.ui.ridgets.IWindowRidget#getDefaultButton()
	 */
	public Object getDefaultButton() {
		return getUIControl().getDefaultButton();
	}

	/*
	 * @see org.eclipse.riena.ui.ridgets.IRidget#requestFocus()
	 */
	@Override
	public void requestFocus() {
		if (getUIControl() != null) {
			getUIControl().setFocus();
		}
	}

	/*
	 * @see org.eclipse.riena.ui.ridgets.IRidget#hasFocus()
	 */
	@Override
	public boolean hasFocus() {
		if (getUIControl() != null) {
			return getUIControl().isFocusControl();
		}
		return false;
	}

	@Override
	public boolean isFocusable() {
		return false;
	}

	@Override
	public void setFocusable(boolean focusable) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void updateEnabled() {
		if (getUIControl() != null) {
			getUIControl().setEnabled(isEnabled());
		}
	}

	@Override
	protected void updateToolTip() {
		if (getUIControl() != null) {
			getUIControl().setToolTipText(getToolTipText());
		}
	}

	/*
	 * @see org.eclipse.riena.ui.ridgets.IWindowRidget#setActive(boolean)
	 */
	public void setActive(boolean active) {
		setEnabled(active);
	}

	/*
	 * @see org.eclipse.riena.ui.ridgets.IWindowRidget#setCloseable(boolean)
	 */
	public void setCloseable(boolean closeable) {
		if (this.closeable != closeable) {
			this.closeable = closeable;
			updateCloseable();
		}
	}

	private void updateCloseable() {
		// TODO
	}

	@Override
	public void updateFromModel() {

		super.updateFromModel();

		if (getUIControl() != null) {
			updateTitle();
			updateIcon();
		}
	}

	@Override
	public String getID() {
		if (getUIControl() != null) {
			IBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();
			return locator.locateBindingProperty(getUIControl());
		}

		return null;
	}

	private class RidgetShellListener implements ShellListener {

		public void shellActivated(ShellEvent e) {
			for (IWindowRidgetListener l : windowRidgetListeners.getListeners()) {
				l.activated();
			}
		}

		public void shellClosed(ShellEvent e) {
			for (IWindowRidgetListener l : windowRidgetListeners.getListeners()) {
				l.closed();
			}
		}

		public void shellDeactivated(ShellEvent e) {
			// do nothing yet
		}

		public void shellDeiconified(ShellEvent e) {
			// do nothing yet
		}

		public void shellIconified(ShellEvent e) {
			// do nothing yet
		}
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.swt.AbstractSWTWidgetRidget#isDisableMandatoryMarker()
	 */
	@Override
	public boolean isDisableMandatoryMarker() {
		return false;
	}

	public boolean isVisible() {
		// check for "hidden.marker". This marker overrules any other visibility rule
		if (!getMarkersOfType(HiddenMarker.class).isEmpty()) {
			return false;
		}

		if (getUIControl() != null) {
			// the swt control is bound
			return getUIControl().isVisible();
		}
		// control is not bound
		return savedVisibleState;
	}

}
