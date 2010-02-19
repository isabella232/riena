package org.eclipse.riena.ui.swt.facades.internal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * Erase listener to paint all cells empty when this ridget is disabled.
 * <p>
 * Implementation note: this works by registering this class an an
 * EraseEListener and indicating we will be responsible from drawing the cells
 * content. We do not register a PaintListener, meaning that we do NOT paint
 * anything.
 * 
 * @see '<a href="http://www.eclipse.org/articles/article.php?file=Article-CustomDrawingTableAndTreeItems/index.html"
 *      >Custom Drawing Table and Tree Items</a>'
 */
public final class TableItemEraser implements Listener {

	/*
	 * Called EXTREMELY frequently. Must be as efficient as possible.
	 */
	public void handleEvent(Event event) {
		// indicate we are responsible for drawing the cell's content
		event.detail &= ~SWT.FOREGROUND;
	}
}