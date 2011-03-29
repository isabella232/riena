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
package org.eclipse.riena.ui.swt.facades.internal;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;

import org.eclipse.riena.ui.swt.ChoiceComposite;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;

/**
 * The actual renderer of the DisabledMarker-State. Colors and Alpha values are
 * configurable. See {@link LnfManager} for more details on this.
 */
public final class DisabledPainter implements PaintListener {
	public void paintControl(final PaintEvent e) {
		final GC gc = e.gc;
		final Control control = (Control) e.widget;
		if (control instanceof Button) {
			return;
		}
		final int alpha = LnfManager.getLnf().getIntegerSetting(LnfKeyConstants.DISABLED_MARKER_STANDARD_ALPHA);
		gc.setAlpha(alpha);
		final Color color = LnfManager.getLnf().getColor(LnfKeyConstants.DISABLED_MARKER_BACKGROUND);
		gc.setBackground(color);
		if (control instanceof ChoiceComposite) {
			gc.setAlpha(255);
			gc.setBackground(control.getParent().getBackground());
		}
		// overdraws the content area
		final Rectangle bounds = control.getBounds();
		gc.fillRectangle(0, 0, bounds.width, bounds.height);
	}
}