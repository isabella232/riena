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
package org.eclipse.riena.internal.ui.ridgets.swt.uiprocess;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.riena.ui.core.uiprocess.IProgressVisualizer;
import org.eclipse.swt.graphics.Rectangle;

@SuppressWarnings("serial")
class VisualizerContainer extends HashMap<IProgressVisualizer, Integer> {

	private Rectangle bounds;

	public IProgressVisualizer getCurrentVisualizer() {
		return getFreshestVisualizer();
	}

	private IProgressVisualizer getFreshestVisualizer() {
		List<IProgressVisualizer> visualizers = new LinkedList<IProgressVisualizer>(keySet());
		Collections.sort(visualizers, new VisualizerComparator());
		if (visualizers.size() > 0) {
			return visualizers.get(0);
		}
		return null;
	}

	/**
	 * @param bounds
	 *            the bounds to set
	 */
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	/**
	 * @return the bounds
	 */
	public Rectangle getBounds() {
		return bounds;
	}

	class VisualizerComparator implements Comparator<IProgressVisualizer> {

		public int compare(IProgressVisualizer o1, IProgressVisualizer o2) {
			Integer time1 = get(o1);
			if (time1 == null) {
				time1 = -1;
			}
			Integer time2 = get(o2);
			if (time2 == null) {
				time2 = -1;
			}
			if (time1 > time2) {
				return -1;
			}
			if (time1 == time2) {
				return 0;
			}

			return 1;
		}
	}
}