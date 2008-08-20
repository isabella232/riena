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

import java.util.Collection;
import java.util.Collections;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.ui.core.marker.DisabledMarker;
import org.eclipse.riena.ui.core.marker.ErrorMarker;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.core.marker.OutputMarker;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;

/**
 * SWT Ridget that supports markers.
 */
public abstract class AbstractMarkableRidget extends AbstractSWTRidget implements IMarkableRidget {

	private MarkerSupport markerSupport;
	private ErrorMarker errorMarker;
	private DisabledMarker disabledMarker;
	private MandatoryMarker mandatoryMarker;
	private OutputMarker outputMarker;

	@Override
	public void setUIControl(Object uiControl) {
		super.setUIControl(uiControl);
		updateMarkers();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Implementation note: returns false always. Subclases should override, if
	 * necessary.
	 */
	public boolean isDisableMandatoryMarker() {
		// TODO [ev] remove or use template method
		return false;
	}

	public final boolean isErrorMarked() {
		return !getMarkersOfType(ErrorMarker.class).isEmpty();
	}

	public final void setErrorMarked(boolean errorMarked) {
		if (!errorMarked) {
			if (errorMarker != null) {
				removeMarker(errorMarker);
			}
		} else {
			if (errorMarker == null) {
				errorMarker = new ErrorMarker();
			}
			addMarker(errorMarker);
		}
	}

	public synchronized final void addMarker(IMarker marker) {
		if (markerSupport == null) {
			markerSupport = new MarkerSupport(this, propertyChangeSupport);
		}
		markerSupport.addMarker(marker);
	}

	public final Collection<IMarker> getMarkers() {
		if (markerSupport != null) {
			return markerSupport.getMarkers();
		}
		return Collections.emptySet();
	}

	public final <T extends IMarker> Collection<T> getMarkersOfType(Class<T> type) {
		if (markerSupport != null) {
			return markerSupport.getMarkersOfType(type);
		}
		return Collections.emptySet();
	}

	public final void removeAllMarkers() {
		if (markerSupport != null) {
			markerSupport.removeAllMarkers();
		}
	}

	public final void removeMarker(IMarker marker) {
		if (markerSupport != null) {
			markerSupport.removeMarker(marker);
		}
	}

	public final boolean isEnabled() {
		return getMarkersOfType(DisabledMarker.class).isEmpty();
	}

	public final synchronized void setEnabled(boolean enabled) {
		if (enabled) {
			if (disabledMarker != null) {
				removeMarker(disabledMarker);
			}
		} else {
			if (disabledMarker == null) {
				disabledMarker = new DisabledMarker();
			}
			addMarker(disabledMarker);
		}
	}

	public final boolean isOutputOnly() {
		return !getMarkersOfType(OutputMarker.class).isEmpty();
	}

	public final void setOutputOnly(boolean outputOnly) {
		if (!outputOnly) {
			if (outputMarker != null) {
				removeMarker(outputMarker);
			}
		} else {
			if (outputMarker == null) {
				outputMarker = new OutputMarker();
			}
			addMarker(outputMarker);
		}
	}

	public final boolean isMandatory() {
		return !getMarkersOfType(MandatoryMarker.class).isEmpty();
	}

	public final void setMandatory(boolean mandatory) {
		if (!mandatory) {
			if (mandatoryMarker != null) {
				removeMarker(mandatoryMarker);
			}
		} else {
			if (mandatoryMarker == null) {
				mandatoryMarker = new MandatoryMarker();
			}
			addMarker(mandatoryMarker);
		}
	}

	@Override
	public final void setVisible(boolean visible) {
		super.setVisible(visible);
		updateMarkers();
	}

	// protected methods
	// //////////////////

	/**
	 * TODO [ev] docs
	 */
	protected final void disableMandatoryMarkers(boolean disable) {
		for (IMarker marker : getMarkersOfType(MandatoryMarker.class)) {
			MandatoryMarker mMarker = (MandatoryMarker) marker;
			mMarker.setDisabled(disable);
		}
	}

	// helping methods
	// ////////////////

	private void updateMarkers() {
		if (markerSupport != null) {
			markerSupport.updateMarkers();
		}
	}

}
