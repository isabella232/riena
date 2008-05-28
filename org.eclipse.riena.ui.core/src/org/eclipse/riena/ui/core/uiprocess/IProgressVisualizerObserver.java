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

import java.util.List;

/**
 * 
 */
public interface IProgressVisualizerObserver {

	public List<IProgressVisualizer> getProgressVisualizers();

	void addProgressVisualizer(IProgressVisualizer aVisualizer);

	void removeProgressVisualizer(IProgressVisualizer aVisualizer);

	void setActiveProgressVisualizer(IProgressVisualizer aVisualizer);

	void updateProgress(IProgressVisualizer aVisualizer, int progress);

	void initialUpdateUI(IProgressVisualizer aVisualizer, int totalWork);

	void finalUpdateUI(IProgressVisualizer aVisualizer);
}
