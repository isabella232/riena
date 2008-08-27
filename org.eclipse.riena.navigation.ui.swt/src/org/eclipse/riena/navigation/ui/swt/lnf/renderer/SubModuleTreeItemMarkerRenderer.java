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
import org.eclipse.riena.ui.core.marker.ErrorMarker;
import org.eclipse.riena.ui.core.marker.IIconizableMarker;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.swt.lnf.AbstractLnfRenderer;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Renderer of the markers (e.g. error or mandatory marker) of a item in the
 * navigation tree.
 */
public class SubModuleTreeItemMarkerRenderer extends AbstractLnfRenderer {

	/**
	 * @see org.eclipse.riena.ui.swt.lnf.AbstractLnfRenderer#paint(org.eclipse.swt.graphics.GC,
	 *      java.lang.Object)
	 */
	@Override
	public void paint(GC gc, Object value) {

		Assert.isNotNull(gc);
		Assert.isNotNull(value);
		Assert.isTrue(value instanceof TreeItem);

		TreeItem item = (TreeItem) value;
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
	 * @param markers
	 *            - collection of markers
	 * @param item
	 *            - tree item
	 */
	protected void paintMarkers(GC gc, Collection<IIconizableMarker> markers, TreeItem item) {

		Image itemImage = item.getImage();
		if (itemImage == null) {
			return;
		}

		for (IIconizableMarker iconizableMarker : markers) {

			if (iconizableMarker instanceof MandatoryMarker) {
				MandatoryMarker mandatoryMarker = (MandatoryMarker) iconizableMarker;
				if (mandatoryMarker.isDisabled()) {
					continue;
				}
			}

			String key = iconizableMarker.getIconConfiguationKey();
			Image markerImage = LnfManager.getLnf().getImage(key);
			if (markerImage != null) {
				int x = getBounds().x;
				x += itemImage.getImageData().x;
				if (iconizableMarker instanceof ErrorMarker) {
					x += itemImage.getImageData().width - markerImage.getImageData().width;
				}
				int y = getBounds().y;
				y += itemImage.getImageData().y;
				y += itemImage.getImageData().height - markerImage.getImageData().height;
				gc.drawImage(markerImage, x, y);
			}

		}

	}

	/**
	 * @see org.eclipse.riena.ui.swt.lnf.ILnfRenderer#dispose()
	 */
	public void dispose() {
		// nothing to do
	}

}
