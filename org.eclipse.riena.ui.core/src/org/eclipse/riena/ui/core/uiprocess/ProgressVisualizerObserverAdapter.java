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
package org.eclipse.riena.ui.core.uiprocess;

import java.util.ArrayList;
import java.util.List;

public class ProgressVisualizerObserverAdapter implements IProgressVisualizerObserver {

	public void addProgressVisualizer(IProgressVisualizer visualizer) {
	}

	public void finalUpdateUI(IProgressVisualizer visualizer) {
	}

	public List<IProgressVisualizer> getProgressVisualizers() {
		return new ArrayList<IProgressVisualizer>();
	}

	public void initialUpdateUI(IProgressVisualizer visualizer, int totalWork) {
	}

	public void removeProgressVisualizer(IProgressVisualizer visualizer) {
	}

	public void setActiveProgressVisualizer(IProgressVisualizer visualizer) {
	}

	public void updateProgress(IProgressVisualizer visualizer, int progress) {
	}

}
