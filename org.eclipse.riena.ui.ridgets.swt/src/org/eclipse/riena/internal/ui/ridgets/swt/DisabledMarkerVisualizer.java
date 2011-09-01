/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.util.EventListener;

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.swt.MarkerSupport;
import org.eclipse.riena.ui.swt.ImageButton;
import org.eclipse.riena.ui.swt.facades.SWTFacade;

/**
 * Responsible for Rendering the disabled state of any control.
 */
public class DisabledMarkerVisualizer {

	// the painter
	private static final EventListener DISABLED_MARKER_PAINTER = SWTFacade.getDefault().createDisabledPainter();
	// true if running in RCP
	private static boolean isRCP = SWTFacade.isRCP();
	// the ridget
	private final IRidget ridget;

	public DisabledMarkerVisualizer(final IRidget ridget) {
		this.ridget = ridget;
	}

	/**
	 * This is the entry point for {@link MarkerSupport}
	 */
	public void updateDisabled() {
		final Control control = getControl();
		if (control == null) {
			return;
		}
		final boolean enabled = getRidget().isEnabled();
		updateDisabled(control, enabled);
	}

	/**
	 * Control connect and disconnect the Paintlistener.
	 */
	protected void removePaintlistener(final Control control) {
		final SWTFacade facade = SWTFacade.getDefault();
		facade.removePaintListener(control, DISABLED_MARKER_PAINTER);
	}

	protected void addPaintlistener(final Control control) {
		final SWTFacade facade = SWTFacade.getDefault();
		facade.addPaintListener(control, DISABLED_MARKER_PAINTER);
	}

	// helping methods
	//////////////////

	private Control getControl() {
		return (Control) getRidget().getUIControl();
	}

	private IRidget getRidget() {
		return ridget;
	}

	public void updateDisabled(final Control control, final boolean enabled) {
		// Bug 322030: only change the enablement of the control bound to the 
		// ridget. The enabled state of the controls within this control (if  
		// a composite) can depend on internal state not available here.
		// Examples: DatePickerComposite, CompletionCombo, CCombo
		control.setEnabled(enabled);
		updatePaintListener(control, enabled);
		control.redraw();
	}

	private void updatePaintListener(final Control control, final boolean enabled) {

		if (dontAddDisabledPainter(control)) {
			return;
		}

		removePaintlistener(control);

		if (!enabled) {
			addPaintlistener(control);
		}

		if (control instanceof Composite) {
			final Composite composite = (Composite) control;
			final Control[] children = getChildren(composite);
			for (final Control child : children) {
				updatePaintListener(child, enabled);
			}
		}
	}

	protected boolean dontAddDisabledPainter(final Control control) {
		if (control instanceof ImageButton) {
			return true;
		}
		if (control instanceof Button) {
			return true;
		}
		return false;
	}

	private Control[] getChildren(final Composite parent) {
		Control[] result = parent.getChildren();
		if (isRCP && parent instanceof CCombo) {
			// workaround for Bug 318301
			result = new Control[2];
			result[0] = ReflectionUtils.getHidden(parent, "text"); //$NON-NLS-1$
			result[1] = ReflectionUtils.getHidden(parent, "arrow"); //$NON-NLS-1$
		}
		return result;
	}

}
