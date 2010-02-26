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
package org.eclipse.riena.ui.core.uiprocess;

/**
 * Instances of this class usually are managed by instances of
 * {@link IProgressVisualizer} and get called as delegates for dispatching
 * {@link IUIMonitor} calls. This convenience interface adds the current
 * {@link IProgressVisualizer} as a parameter to the {@link IUIMonitor}
 * callbacks.
 */
public interface IProgressVisualizerObserver {

	/**
	 * adds the {@link IProgressVisualizer}
	 * 
	 * @param aVisualizer
	 */
	void addProgressVisualizer(IProgressVisualizer aVisualizer);

	/**
	 * removes the {@link IProgressVisualizer}
	 * 
	 * @param aVisualizer
	 */
	void removeProgressVisualizer(IProgressVisualizer aVisualizer);

	/**
	 * @see IUIMonitor#updateProgress(int)
	 * @see ProcessInfo#setProgresStartegy(org.eclipse.riena.ui.core.uiprocess.ProcessInfo.ProgresStrategy)
	 * @param aVisualizer
	 *            - the current visualizer
	 * @param progress
	 *            - the current progress. consider that the progress can grow in
	 *            2 different ways. (unit based and cumulative)
	 */
	void updateProgress(IProgressVisualizer aVisualizer, int progress);

	/**
	 * @see IUIMonitor#initialUpdateUI(int)
	 * @see ProcessInfo#setProgresStartegy(org.eclipse.riena.ui.core.uiprocess.ProcessInfo.ProgresStrategy)
	 * @param aVisualizer
	 *            - the current visualizer
	 * @param totalWork
	 *            - total number of work units
	 */
	void initialUpdateUI(IProgressVisualizer aVisualizer, int totalWork);

	/**
	 * @see IUIMonitor#finalUpdateUI()
	 * @param aVisualizer
	 *            - the current visualizer
	 * 
	 */
	void finalUpdateUI(IProgressVisualizer aVisualizer);
}
