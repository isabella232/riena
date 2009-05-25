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
package org.eclipse.riena.navigation.ui.swt.lnf.renderer;

import java.util.Collection;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.TreeItem;

import org.eclipse.riena.ui.core.marker.IIconizableMarker;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.core.marker.UIProcessFinishedMarker;
import org.eclipse.riena.ui.swt.lnf.AbstractLnfRenderer;
import org.eclipse.riena.ui.swt.lnf.FlasherSupportForRenderer;
import org.eclipse.riena.ui.swt.lnf.LnfManager;

/**
 * Renderer of the markers (e.g. error or mandatory marker) of a item in the
 * navigation tree.
 */
public class SubModuleTreeItemMarkerRenderer extends AbstractLnfRenderer {

	private TreeItem item;
	private FlasherSupportForRenderer flasherSupport;

	/**
	 * Creates a new instance of the renderer for the markers of sub-modules in
	 * a tree.
	 */
	public SubModuleTreeItemMarkerRenderer() {
		super();
		flasherSupport = new FlasherSupportForRenderer(this, new MarkerUpdater());
	}

	/**
	 * @see org.eclipse.riena.ui.swt.lnf.AbstractLnfRenderer#paint(org.eclipse.swt.graphics.GC,
	 *      java.lang.Object)
	 */
	@Override
	public void paint(GC gc, Object value) {

		Assert.isNotNull(gc);
		Assert.isNotNull(value);
		Assert.isTrue(value instanceof TreeItem);

		item = (TreeItem) value;

		flasherSupport.startFlasher();

		Collection<IIconizableMarker> markers = getMarkersOfType(IIconizableMarker.class);
		if (!markers.isEmpty()) {
			paintMarkers(gc, markers, item);
		}

	}

	/**
	 * Paints all the given markers (icons).
	 * 
	 * @param gc
	 *            - graphics context
	 * @param item
	 *            - tree item
	 */
	protected void paintMarkers(final GC gc, final Collection<IIconizableMarker> markers, final TreeItem item) {

		for (IIconizableMarker iconizableMarker : markers) {

			if (iconizableMarker instanceof MandatoryMarker) {
				MandatoryMarker mandatoryMarker = (MandatoryMarker) iconizableMarker;
				if (mandatoryMarker.isDisabled()) {
					continue;
				}
			}
			if (iconizableMarker instanceof UIProcessFinishedMarker) {
				if (!flasherSupport.isProcessMarkerVisible()) {
					return;
				}
			}

			Image itemImage = item.getImage();
			String key = iconizableMarker.getIconConfigurationKey();
			Image markerImage = LnfManager.getLnf().getImage(key);
			Point pos = calcMarkerCoordinates(itemImage, markerImage, iconizableMarker.getPositionOfMarker());
			gc.drawImage(markerImage, pos.x, pos.y);

		}

	}

	/**
	 * Calculates the x- and y-coordinates of the marker image.
	 * 
	 * @param itemImage
	 *            - image of the item
	 * @param markerImage
	 *            - image of the marker
	 * @param position
	 *            - position of the marker
	 * @return x- and y-coordinates
	 */
	private Point calcMarkerCoordinates(Image itemImage, Image markerImage,
			final IIconizableMarker.MarkerPosition position) {

		Rectangle itemImageBounds = new Rectangle(0, 0, 0, 0);
		if (itemImage != null) {
			Rectangle imageBounds = itemImage.getBounds();
			itemImageBounds = new Rectangle(imageBounds.x, imageBounds.y, imageBounds.width, imageBounds.height);
		}
		Rectangle itemMarkerBounds = new Rectangle(0, 0, 0, 0);
		if (markerImage != null) {
			Rectangle imageBounds = markerImage.getBounds();
			itemMarkerBounds = new Rectangle(imageBounds.x, imageBounds.y, imageBounds.width, imageBounds.height);
		}

		int x = getBounds().x;
		x += itemImageBounds.x;
		int y = getBounds().y;
		y += itemImageBounds.y;

		switch (position) {
		case TOP_RIGHT:
			x += itemImageBounds.width - itemMarkerBounds.width;
			break;
		case BOTTOM_LEFT:
			y += itemImageBounds.height - itemMarkerBounds.height;
			break;
		case BOTTOM_RIGHT:
			x += itemImageBounds.width - itemMarkerBounds.width;
			y += itemImageBounds.height - itemMarkerBounds.height;
			break;
		default:
			break;
		}

		return new Point(x, y);

	}

	/**
	 * @see org.eclipse.riena.ui.swt.lnf.ILnfRenderer#dispose()
	 */
	public void dispose() {
		item = null;
		flasherSupport = null;
	}

	/**
	 * This class updates (redraws) the tree, so that the marker are also
	 * updated (redrawn).
	 */
	private class MarkerUpdater implements Runnable {

		/**
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			item.getParent().redraw();
		}
	}

}
