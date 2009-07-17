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

import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.core.marker.NegativeMarker;
import org.eclipse.riena.ui.ridgets.IBasicMarkableRidget;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.swt.AbstractActionRidget;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;

/**
 * Helper class for SWT Ridgets to delegate their marker issues to.
 */
public class MarkerSupport extends BasicMarkerSupport {

	private static final Logger LOGGER = Log4r.getLogger(Activator.getDefault(), MarkerSupport.class);
	private static final String PRE_MANDATORY_BACKGROUND_KEY = "org.eclipse.riena.MarkerSupport.preMandatoryBackground"; //$NON-NLS-1$
	private static final String PRE_OUTPUT_BACKGROUND_KEY = "org.eclipse.riena.MarkerSupport.preOutputBackground"; //$NON-NLS-1$
	private static final String PRE_NEGATIVE_FOREGROUND_KEY = "org.eclipse.riena.MarkerSupport.preNegativeForeground"; //$NON-NLS-1$

	/**
	 * This flag controls whether disabled ridget do hide their content. The
	 * default value is {@code true}. It can be overridden by setting the system
	 * property {@code 'HIDE_DISABLED_RIDGET_CONTENT'} to {@code false}.
	 * <p>
	 * Note: this field was made public for testing purposes. It should not be
	 * referenced outside this package.
	 */
	public static final boolean HIDE_DISABLED_RIDGET_CONTENT = Boolean.parseBoolean(System.getProperty(
			"HIDE_DISABLED_RIDGET_CONTENT", Boolean.TRUE.toString())); //$NON-NLS-1$

	private ControlDecoration errorDecoration;

	public MarkerSupport(IBasicMarkableRidget ridget, PropertyChangeSupport propertyChangeSupport) {
		super(ridget, propertyChangeSupport);
	}

	@Override
	protected IMarkableRidget getRidget() {
		return (IMarkableRidget) super.getRidget();
	}

	protected void addError(Control control) {
		if (errorDecoration == null) {
			errorDecoration = createErrorDecoration(control);
			control.addDisposeListener(new DisposeListener() {
				public void widgetDisposed(DisposeEvent e) {
					errorDecoration.dispose();
				}
			});
		}
		errorDecoration.show();
	}

	/**
	 * Creates a decoration with an error marker for the given control.
	 * 
	 * @param control
	 *            - the control to be decorated with an error marker
	 * @return decoration of the given control
	 */
	private ControlDecoration createErrorDecoration(Control control) {

		RienaDefaultLnf lnf = LnfManager.getLnf();

		int position = 0;
		int hPos = lnf.getIntegerSetting(LnfKeyConstants.ERROR_MARKER_HORIZONTAL_POSITION, SWT.LEFT);
		if (hPos == SWT.RIGHT || hPos == SWT.LEFT) {
			position |= hPos;
		} else {
			LOGGER.log(LogService.LOG_WARNING, "Invalid horizonal error marker position!"); //$NON-NLS-1$
			position |= SWT.LEFT;
		}
		int vPos = lnf.getIntegerSetting(LnfKeyConstants.ERROR_MARKER_VERTICAL_POSITION, SWT.TOP);
		if (vPos == SWT.TOP || vPos == SWT.CENTER || vPos == SWT.BOTTOM) {
			position |= vPos;
		} else {
			LOGGER.log(LogService.LOG_WARNING, "Invalid vertical error marker position!"); //$NON-NLS-1$
			position |= SWT.TOP;
		}
		ControlDecoration ctrlDecoration = new ControlDecoration(control, position);

		// setMargin has to be before setImage!
		int margin = lnf.getIntegerSetting(LnfKeyConstants.ERROR_MARKER_MARGIN, 1);
		ctrlDecoration.setMarginWidth(margin);
		Image image = lnf.getImage(LnfKeyConstants.ERROR_MARKER_ICON);
		if (image == null) {
			image = Activator.getSharedImage(SharedImages.IMG_ERROR_DECO);
		}
		ctrlDecoration.setImage(image);

		return ctrlDecoration;

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
			RienaDefaultLnf lnf = LnfManager.getLnf();
			Color color = lnf.getColor(LnfKeyConstants.MANDATORY_MARKER_BACKGROUND);
			if (color == null) {
				color = Activator.getSharedColor(control.getDisplay(), SharedColors.COLOR_MANDATORY);
			}
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
	}

	private boolean isMandatory(IMarkableRidget ridget) {
		boolean result = false;
		Iterator<MandatoryMarker> iter = getRidget().getMarkersOfType(MandatoryMarker.class).iterator();
		while (!result && iter.hasNext()) {
			result = !iter.next().isDisabled();
		}
		return result;
	}

	private void updateError(Control control) {
		if (getRidget().isErrorMarked() && getRidget().isEnabled() && getRidget().isVisible()) {
			if (!(isButton(control) && getRidget().isOutputOnly())) {
				addError(control);
			} else {
				clearError(control);
			}
		} else {
			clearError(control);
		}
	}

	private void updateMandatory(Control control) {
		if (isMandatory(getRidget()) && !getRidget().isOutputOnly() && getRidget().isEnabled()) {
			addMandatory(control);
		} else {
			clearMandatory(control);
		}
	}

	private void updateNegative(Control control) {
		if (!getRidget().getMarkersOfType(NegativeMarker.class).isEmpty() && getRidget().isEnabled()) {
			addNegative(control);
		} else {
			clearNegative(control);
		}
	}

	private void updateOutput(Control control) {
		if (getRidget().isOutputOnly() && getRidget().isEnabled()) {
			clearMandatory(control);
			clearOutput(control);
			if (isMandatory(getRidget())) {
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
	@Override
	protected void updateUIControl(Control control) {
		updateVisible(control);
		updateDisabled(control);
		updateOutput(control);
		updateMandatory(control);
		updateError(control);
		updateNegative(control);
	}

	private boolean isButton(Control control) {
		return control instanceof Button || getRidget() instanceof AbstractActionRidget;
	}

}
