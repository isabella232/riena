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

import java.beans.PropertyChangeSupport;
import java.util.Iterator;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.core.marker.NegativeMarker;
import org.eclipse.riena.ui.ridgets.AbstractMarkerSupport;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;

/**
 * Helper class for SWT Ridgets to delegate their marker issues to.
 */
public final class MarkerSupport extends AbstractMarkerSupport {

	static {
		String value = System.getProperty("HIDE_DISABLED_RIDGET_CONTENT"); //$NON-NLS-1$
		HIDE_DISABLED_RIDGET_CONTENT = value == null ? true : Boolean.parseBoolean(value);
	}

	/**
	 * TODO [ev] docs
	 */
	static final boolean HIDE_DISABLED_RIDGET_CONTENT;

	private Color preOutputBg;
	private Color preMandatoryBg;
	private Color preNegativeFg;
	private ControlDecoration errorDecoration;

	public MarkerSupport(IMarkableRidget ridget, PropertyChangeSupport propertyChangeSupport) {
		super(ridget, propertyChangeSupport);
	}

	@Override
	public void updateMarkers() {
		updateUIControl();
	}

	@Override
	protected void handleMarkerAttributesChanged() {
		updateUIControl();
		super.handleMarkerAttributesChanged();
	}

	// helping methods
	// ////////////////

	private void addError(Control control) {
		if (errorDecoration == null) {
			errorDecoration = new ControlDecoration(control, SWT.LEFT | SWT.TOP);
			// setMargin has to be before setImage!
			errorDecoration.setMarginWidth(1);
			errorDecoration.setImage(Activator.getSharedImage(SharedImages.IMG_ERROR_DECO));
			control.addDisposeListener(new DisposeListener() {
				public void widgetDisposed(DisposeEvent e) {
					errorDecoration.dispose();
				}
			});
		}
		errorDecoration.show();
	}

	private void addMandatory(Control control) {
		if (preMandatoryBg == null) {
			preMandatoryBg = control.getBackground();
			Color color = Activator.getSharedColor(SharedColors.COLOR_MANDATORY);
			control.setBackground(color);
		}
	}

	private void addNegative(Control control) {
		if (preNegativeFg == null) {
			preNegativeFg = control.getForeground();
			control.setForeground(control.getDisplay().getSystemColor(SWT.COLOR_RED));
		}
	}

	private void addOutput(Control control, Color color) {
		if (preOutputBg == null) {
			preOutputBg = control.getBackground();
			control.setBackground(color);
		}
		if (control instanceof Button) {
			control.setVisible(false);
		}
	}

	private void clearError() {
		if (errorDecoration != null) {
			errorDecoration.hide();
		}
	}

	private void clearMandatory(Control control) {
		if (preMandatoryBg != null) {
			control.setBackground(preMandatoryBg);
			preMandatoryBg = null;
		}
	}

	private void clearNegative(Control control) {
		if (preNegativeFg != null) {
			control.setForeground(preNegativeFg);
			preNegativeFg = null;
		}
	}

	private void clearOutput(Control control) {
		if (preOutputBg != null) {
			control.setBackground(preOutputBg);
			preOutputBg = null;
		}
		if (control instanceof Button) {
			control.setVisible(ridget.isVisible());
		}
	}

	private boolean isMandatory(IMarkableRidget ridget) {
		boolean result = false;
		Iterator<MandatoryMarker> iter = ridget.getMarkersOfType(MandatoryMarker.class).iterator();
		while (!result && iter.hasNext()) {
			result = !iter.next().isDisabled();
		}
		return result;
	}

	private void updateVisible(Control control) {
		control.setVisible(ridget.isVisible());
	}

	private void updateDisabled(Control control) {
		control.setEnabled(ridget.isEnabled());
	}

	private void updateError(Control control) {
		if (ridget.isErrorMarked() && ridget.isEnabled() && ridget.isVisible()) {
			if (!(control instanceof Button && ridget.isOutputOnly())) {
				addError(control);
			} else {
				clearError();
			}
		} else {
			clearError();
		}
	}

	private void updateMandatory(Control control) {
		if (isMandatory(ridget) && !ridget.isOutputOnly() && ridget.isEnabled()) {
			addMandatory(control);
		} else {
			clearMandatory(control);
		}
	}

	private void updateNegative(Control control) {
		if (!ridget.getMarkersOfType(NegativeMarker.class).isEmpty() && ridget.isEnabled()) {
			addNegative(control);
		} else {
			clearNegative(control);
		}
	}

	private void updateOutput(Control control) {
		if (ridget.isOutputOnly() && ridget.isEnabled()) {
			clearMandatory(control);
			clearOutput(control);
			if (isMandatory(ridget)) {
				addOutput(control, Activator.getSharedColor(SharedColors.COLOR_MANDATORY_OUTPUT));
			} else {
				addOutput(control, Activator.getSharedColor(SharedColors.COLOR_OUTPUT));
			}
		} else {
			clearOutput(control);
		}
	}

	/**
	 * Precedence of visibility and marker states for a ridget:
	 * <ol>
	 * <li>ridget is hidden - no decorations are not shown</li>
	 * <li>disabled on - all other states not shown on the ridget</li>
	 * <li>output on - output decoration is shown</li>
	 * <li>mandatory on - mandatory decoration is shown</li>
	 * <li>error on - error decoration is shown</li>
	 * <li>negative on - negative decoration is shown</li>
	 * <ol>
	 */
	private void updateUIControl() {
		Control control = (Control) ridget.getUIControl();
		if (control != null) {
			control.setRedraw(false); // prevent flicker
			try {
				//				if (control instanceof Combo) {
				//					System.out.println("MarkerSupport.updateUIControl() " + ridget.isVisible());
				//				}
				updateVisible(control);
				updateDisabled(control);
				updateOutput(control);
				updateMandatory(control);
				updateError(control);
				updateNegative(control);
			} finally {
				control.setRedraw(true);
			}
			//			if (control instanceof Combo) {
			//				System.out.println("End " + control.isVisible());
			//			}
		}
	}

}
