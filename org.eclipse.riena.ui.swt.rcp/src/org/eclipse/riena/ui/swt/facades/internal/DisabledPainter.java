package org.eclipse.riena.ui.swt.facades.internal;

import org.eclipse.riena.ui.core.marker.DisabledMarker;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;

/**
 * The actual renderer of the {@link DisabledMarker}-State. Colors and Alpha
 * values are configurable. See {@link LnfManager} for more details on this.
 */
public final class DisabledPainter implements PaintListener {
	public void paintControl(PaintEvent e) {
		GC gc = e.gc;
		Control control = (Control) e.widget;
		int alpha = LnfManager.getLnf().getIntegerSetting(LnfKeyConstants.DISABLED_MARKER_STANDARD_ALPHA);
		gc.setAlpha(alpha);
		Color color = LnfManager.getLnf().getColor(LnfKeyConstants.DISABLED_MARKER_BACKGROUND);
		gc.setBackground(color);
		// overdraws the content area
		Rectangle bounds = control.getBounds();
		gc.fillRectangle(0, 0, bounds.width, bounds.height);
	}
}