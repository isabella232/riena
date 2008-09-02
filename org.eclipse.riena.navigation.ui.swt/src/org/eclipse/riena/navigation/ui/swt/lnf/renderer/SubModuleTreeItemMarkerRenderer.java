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
package org.eclipse.riena.navigation.ui.swt.lnf.renderer;

import java.util.Collection;

import org.eclipse.core.runtime.Assert;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.ui.core.marker.IIconizableMarker;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.core.marker.UIProcessFinishedMarker;
import org.eclipse.riena.ui.swt.lnf.AbstractLnfRenderer;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Renderer of the markers (e.g. error or mandatory marker) of a item in the
 * navigation tree.
 */
public class SubModuleTreeItemMarkerRenderer extends AbstractLnfRenderer {

	private TreeItem item;
	private Runnable updater;

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
		SubModuleNode node = (SubModuleNode) item.getData();
		if (node == null) {
			return;
		}

		Collection<IIconizableMarker> markers = node.getMarkersOfType(IIconizableMarker.class);
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
				UIProcessFinishedMarker processMarker = (UIProcessFinishedMarker) iconizableMarker;
				if (!processMarker.isActivated()) {
					// the flasher for the UI process finished marker was not
					// starter (activated) already
					startFlasher(processMarker);
					continue;
				} else if (!processMarker.isOn()) {
					// the marker (the image of the marker) is not visible at
					// the moment
					continue;
				}
			}

			Image itemImage = item.getImage();
			String key = iconizableMarker.getIconConfiguationKey();
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
			ImageData imageData = itemImage.getImageData();
			itemImageBounds = new Rectangle(imageData.x, imageData.y, imageData.width, imageData.height);
		}
		Rectangle itemMarkerBounds = new Rectangle(0, 0, 0, 0);
		if (markerImage != null) {
			ImageData imageData = markerImage.getImageData();
			itemMarkerBounds = new Rectangle(imageData.x, imageData.y, imageData.width, imageData.height);
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
	 * Creates and starts the flasher of a finished UI process.
	 * 
	 * @param processMarker
	 *            - marker of finished UI process.
	 */
	private synchronized void startFlasher(final UIProcessFinishedMarker processMarker) {

		if (updater == null) {
			updater = new MarkerUpdater();
		}

		UIProcessFinishedFlasher flasher = new UIProcessFinishedFlasher(processMarker, updater);
		processMarker.activate();
		flasher.start();

	}

	/**
	 * @see org.eclipse.riena.ui.swt.lnf.ILnfRenderer#dispose()
	 */
	public void dispose() {
		updater = null;
		item = null;
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
