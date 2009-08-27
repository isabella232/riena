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
package org.eclipse.riena.ui.ridgets.swt;

import java.net.URL;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;

import org.eclipse.riena.internal.ui.ridgets.swt.BasicMarkerSupport;
import org.eclipse.riena.ui.ridgets.AbstractMarkerSupport;
import org.eclipse.riena.ui.ridgets.ILabelRidget;

/**
 * Superclass of LabelRidget that does not depend on the Label SWT control. May
 * be reused for custom Label controls.
 */
public abstract class AbstractLabelRidget extends AbstractValueRidget implements ILabelRidget {

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$
	private String text;
	private String icon;
	private URL iconLocation;
	private boolean textAlreadyInitialized;
	private boolean useRidgetIcon;

	public AbstractLabelRidget() {
		textAlreadyInitialized = false;
		useRidgetIcon = false;
	}

	@Override
	protected AbstractMarkerSupport createMarkerSupport() {
		return new BasicMarkerSupport(this, propertyChangeSupport);
	}

	@Override
	protected IObservableValue getRidgetObservable() {
		return BeansObservables.observeValue(this, ILabelRidget.PROPERTY_TEXT);
	}

	@Override
	protected void bindUIControl() {
		initText();
		updateUIText();
		updateUIIcon();
	}

	/**
	 * If the text of the ridget has no value, initialize it with the text of
	 * the UI control.
	 */
	private void initText() {
		if (text == null && !textAlreadyInitialized) {
			Control control = getUIControl();
			if (control != null && !control.isDisposed()) {
				text = getUIControlText();
				if (text == null) {
					text = EMPTY_STRING;
				}
				textAlreadyInitialized = true;
			}
		}
	}

	public String getIcon() {
		return icon;
	}

	public URL getIconLocation() {
		return iconLocation;
	}

	public String getText() {
		return text;
	}

	/**
	 * Always returns true because mandatory markers do not make sense for this
	 * ridget.
	 */
	@Override
	public boolean isDisableMandatoryMarker() {
		return true;
	}

	public void setIcon(String icon) {
		boolean oldUseRidgetIcon = useRidgetIcon;
		useRidgetIcon = true;
		String oldIcon = this.icon;
		this.icon = icon;
		if (hasChanged(oldIcon, icon) || !oldUseRidgetIcon) {
			updateUIIcon();
		}
	}

	public void setIconLocation(URL location) {
		useRidgetIcon = true;
		URL oldUrl = this.iconLocation;
		this.iconLocation = location;
		if (hasChanged(oldUrl, location)) {
			updateUIIcon();
		}
	}

	public void setText(String text) {
		String oldValue = this.text;
		this.text = text;
		updateUIText();
		firePropertyChange(ILabelRidget.PROPERTY_TEXT, oldValue, this.text);
	}

	// helping methods
	// ////////////////

	/**
	 * @return The controls text.
	 */
	protected abstract String getUIControlText();

	private void updateUIText() {
		if (getUIControl() != null) {
			setUIControlText(text);
		}
	}

	/**
	 * Sets the controls text.
	 */
	protected abstract void setUIControlText(String text);

	private void updateUIIcon() {
		if (getUIControl() != null) {
			Image image = null;
			if (icon != null) {
				image = getManagedImage(icon);
			} else if (iconLocation != null) {
				String key = iconLocation.toExternalForm();
				image = getManagedImage(key);
			}
			if ((image != null) || useRidgetIcon) {
				setUIControlImage(image);
			}
		}
	}

	/**
	 * Sets the controls image.
	 */
	protected abstract void setUIControlImage(Image image);

	private boolean hasChanged(URL oldValue, URL newValue) {
		if (oldValue == null && newValue == null) {
			return false;
		}
		if (oldValue == null || newValue == null) {
			return true;
		}

		// avoid URL.equals(...) since it opens a network connection :(
		String str1 = oldValue.toExternalForm();
		String str2 = newValue.toExternalForm();
		return !str1.equals(str2);
	}

}
