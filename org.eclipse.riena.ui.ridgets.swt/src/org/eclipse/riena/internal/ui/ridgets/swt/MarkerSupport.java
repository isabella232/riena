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

import java.beans.PropertyChangeSupport;
import java.util.Iterator;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Table;

import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.core.marker.NegativeMarker;
import org.eclipse.riena.ui.ridgets.AbstractMarkerSupport;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.swt.AbstractActionRidget;

/**
 * Helper class for SWT Ridgets to delegate their marker issues to.
 */
public class MarkerSupport extends AbstractMarkerSupport {

	private static final String PRE_MANDATORY_BACKGROUND_KEY = "org.eclipse.riena.MarkerSupport.preMandatoryBackground"; //$NON-NLS-1$
	private static final String PRE_OUTPUT_BACKGROUND_KEY = "org.eclipse.riena.MarkerSupport.preOutputBackground"; //$NON-NLS-1$
	private static final String PRE_NEGATIVE_FOREGROUND_KEY = "org.eclipse.riena.MarkerSupport.preNegativeForeground"; //$NON-NLS-1$

	static {
		// avoid inlining HIDE_DISABLED_RIDGET_CONTENT
		String value = System.getProperty("HIDE_DISABLED_RIDGET_CONTENT"); //$NON-NLS-1$
		HIDE_DISABLED_RIDGET_CONTENT = value == null ? true : Boolean.parseBoolean(value);
	}

	/**
	 * This flag controls wether disabled ridget do hide their content. The
	 * default value is {@code true}. It can be overriden by setting the system
	 * property {@code 'HIDE_DISABLED_RIDGET_CONTENT'} to {@code false}.
	 * <p>
	 * Note: this field was made public for testing purposes. It should not be
	 * referenced outside this package.
	 */
	public static final boolean HIDE_DISABLED_RIDGET_CONTENT;

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

	protected void addError(Control control) {
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

	protected void clearError(Control control) {
		if (errorDecoration != null) {
			errorDecoration.hide();
		}
	}

	// helping methods
	// ////////////////

	private void addMandatory(Control control) {
		if (control.getData(PRE_MANDATORY_BACKGROUND_KEY) == null) {
			control.setData(PRE_MANDATORY_BACKGROUND_KEY, control.getBackground());
			Color color = Activator.getSharedColor(control.getDisplay(), SharedColors.COLOR_MANDATORY);
			control.setBackground(color);
		}
	}

	private void addNegative(Control control) {
		if (control.getData(PRE_NEGATIVE_FOREGROUND_KEY) == null) {
			control.setData(PRE_NEGATIVE_FOREGROUND_KEY, control.getForeground());
			control.setForeground(control.getDisplay().getSystemColor(SWT.COLOR_RED));
		}
	}

	private void addOutput(Control control, Color color) {
		if (control.getData(PRE_OUTPUT_BACKGROUND_KEY) == null) {
			control.setData(PRE_OUTPUT_BACKGROUND_KEY, control.getBackground());
			control.setBackground(color);
		}
		if (isButton(control)) {
			control.setVisible(false);
		}
	}

	private void clearMandatory(Control control) {
		if (control.getData(PRE_MANDATORY_BACKGROUND_KEY) != null) {
			control.setBackground((Color) control.getData(PRE_MANDATORY_BACKGROUND_KEY));
			control.setData(PRE_MANDATORY_BACKGROUND_KEY, null);
		}
	}

	private void clearNegative(Control control) {
		if (control.getData(PRE_NEGATIVE_FOREGROUND_KEY) != null) {
			control.setForeground((Color) control.getData(PRE_NEGATIVE_FOREGROUND_KEY));
			control.setData(PRE_NEGATIVE_FOREGROUND_KEY, null);
		}
	}

	private void clearOutput(Control control) {
		if (control.getData(PRE_OUTPUT_BACKGROUND_KEY) != null) {
			control.setBackground((Color) control.getData(PRE_OUTPUT_BACKGROUND_KEY));
			control.setData(PRE_OUTPUT_BACKGROUND_KEY, null);
		}
		if (isButton(control)) {
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
			if (!(isButton(control) && ridget.isOutputOnly())) {
				addError(control);
			} else {
				clearError(control);
			}
		} else {
			clearError(control);
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
				Color color = Activator.getSharedColor(control.getDisplay(), SharedColors.COLOR_MANDATORY_OUTPUT);
				addOutput(control, color);
			} else {
				Color color = Activator.getSharedColor(control.getDisplay(), SharedColors.COLOR_OUTPUT);
				addOutput(control, color);
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
			stopRedraw(control);
			try {
				updateVisible(control);
				updateDisabled(control);
				updateOutput(control);
				updateMandatory(control);
				updateError(control);
				updateNegative(control);
			} finally {
				startRedraw(control);
			}
		}
	}

	private void startRedraw(Control control) {
		if (!skipRedrawForBug258176(control)) {
			control.setRedraw(true);
			control.redraw();
		}
	}

	private void stopRedraw(Control control) {
		if (!skipRedrawForBug258176(control)) {
			control.setRedraw(false);
		}
	}

	/**
	 * These controls are affected by bug 258176 in SWT.
	 */
	private boolean skipRedrawForBug258176(Control control) {
		return (control instanceof Combo) || (control instanceof Table) || (control instanceof List);
	}

	private boolean isButton(Control control) {
		return control instanceof Button || ridget instanceof AbstractActionRidget;
	}

}
