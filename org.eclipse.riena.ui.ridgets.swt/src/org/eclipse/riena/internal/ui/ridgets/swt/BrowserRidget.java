/*******************************************************************************
 * Copyright © 2009 Florian Pirchner.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Florian Pirchner – initial API and implementation (based on other ridgets of
 *                    compeople AG)
 * compeople AG     - adjustments for Riena v1.2
 *******************************************************************************/
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;

import org.eclipse.riena.ui.ridgets.AbstractMarkerSupport;
import org.eclipse.riena.ui.ridgets.IBrowserRidget;
import org.eclipse.riena.ui.ridgets.swt.AbstractSWTRidget;
import org.eclipse.riena.ui.ridgets.swt.AbstractValueRidget;

/**
 * Ridget for an SWT {@link Browser} widget.
 * <p>
 * Implementation note: because of SWT <a
 * href="http://bugs.eclipse.org/84532">Bug 84532</a> the
 * {@link #setFocusable(boolean)} methods has no effect.
 * 
 * @since 1.2
 */
public class BrowserRidget extends AbstractValueRidget implements IBrowserRidget {

	private final BrowserUrlListener locationListener;
	private String url;

	public BrowserRidget() {
		locationListener = new BrowserUrlListener();
	}

	@Override
	protected void checkUIControl(Object uiControl) {
		AbstractSWTRidget.assertType(uiControl, Browser.class);
	}

	@Override
	protected void bindUIControl() {
		Browser control = getUIControl();
		if (control != null) {
			updateURL();
			control.addLocationListener(locationListener);
		}
	}

	@Override
	protected void unbindUIControl() {
		Browser control = getUIControl();
		if (control != null) {
			control.removeLocationListener(locationListener);
		}
		super.unbindUIControl();
	}

	@Override
	protected AbstractMarkerSupport createMarkerSupport() {
		return new BasicMarkerSupport(this, propertyChangeSupport);
	}

	@Override
	protected IObservableValue getRidgetObservable() {
		return BeansObservables.observeValue(this, IBrowserRidget.PROPERTY_URL);
	}

	@Override
	public Browser getUIControl() {
		return (Browser) super.getUIControl();
	}

	public String getUrl() {
		return url;
	}

	/**
	 * Always returns true because mandatory markers do not make sense for this
	 * ridget.
	 */
	@Override
	public boolean isDisableMandatoryMarker() {
		return true;
	}

	public void setUrl(String url) {
		String oldValue = this.getUrl();
		this.url = url;
		updateURL();
		firePropertyChange(IBrowserRidget.PROPERTY_URL, oldValue, this.url);
	}

	// helping methods
	//////////////////

	private void updateURL() {
		Browser control = getUIControl();
		if (control != null) {
			String theUrl = getUrl() != null ? getUrl() : ""; //$NON-NLS-1$
			if (!control.getUrl().equals(theUrl)) {
				locationListener.setBlock(false);
				try {
					control.setUrl(theUrl);
				} finally {
					locationListener.setBlock(true);
				}
			}
		}
	}

	// helping classes
	//////////////////

	/**
	 * Listens to location changes in the Browser widget and update's the
	 * Ridget's URL if necessary.
	 */
	private final class BrowserUrlListener implements LocationListener {

		private boolean canBlock = true;

		public void setBlock(boolean canBlock) {
			this.canBlock = canBlock;
		}

		public void changing(LocationEvent event) {
			if (isOutputOnly() && canBlock) {
				// this will prevent the browser from following the link
				event.doit = false;
			}
		}

		public void changed(LocationEvent event) {
			if (event.top) {
				setUrl(event.location);
			}
		}

	}

}