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
import java.util.Collection;

import org.eclipse.core.runtime.PlatformObject;

public class ProgressVisualizer extends PlatformObject implements IProgressVisualizer {
	private Collection<IProgressVisualizerObserver> observers = new ArrayList<IProgressVisualizerObserver>();

	private ProcessInfo processInfo;
	private int currentProgress = -1;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.core.uiprocess.IUIProcess#finalUpdateUI()
	 */
	public void finalUpdateUI() {
		Collection<IProgressVisualizerObserver> currentObservers = new ArrayList<IProgressVisualizerObserver>(observers);
		if (currentObservers.size() > 0) {
			for (IProgressVisualizerObserver anObserver : currentObservers) {
				anObserver.finalUpdateUI(this);
				anObserver.removeProgressVisualizer(this);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.core.uiprocess.IUIProcess#initialUpdateUI(int)
	 */
	public void initialUpdateUI(int totalWork) {
		Collection<IProgressVisualizerObserver> currentObservers = new ArrayList<IProgressVisualizerObserver>(observers);
		for (IProgressVisualizerObserver anObserver : currentObservers) {
			anObserver.addProgressVisualizer(this);
			anObserver.setActiveProgressVisualizer(this);
			anObserver.initialUpdateUI(this, totalWork);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.core.uiprocess.IUIProcess#updateProgress(int)
	 */
	public void updateProgress(int progress) {
		setCurrentProgress(progress);
		Collection<IProgressVisualizerObserver> currentObservers = new ArrayList<IProgressVisualizerObserver>(observers);
		for (IProgressVisualizerObserver anObserver : currentObservers) {
			anObserver.updateProgress(this, progress);
		}
	}

	/**
	 * @return the processInfo
	 */
	public ProcessInfo getProcessInfo() {
		return processInfo;
	}

	/**
	 * @param processInfo
	 *            the processInfo to set
	 */
	public void setProcessInfo(ProcessInfo processInfo) {
		this.processInfo = processInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.ui.core.uiprocess.IUIProcess#initialUpdateUI(int)
	 */
	public void activate() {
		Collection<IProgressVisualizerObserver> currentObservers = new ArrayList<IProgressVisualizerObserver>(observers);
		for (IProgressVisualizerObserver anObserver : currentObservers) {
			anObserver.setActiveProgressVisualizer(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.ui.core.uiprocess.IProgressVisualizer#addObserver(org
	 * .eclipse.riena.ui.core.uiprocess.IProgressVisualizerObserver)
	 */
	public void addObserver(IProgressVisualizerObserver anObserver) {
		if (anObserver != null) {
			observers.add(anObserver);
		}

	}

	public void removeObserver(IProgressVisualizerObserver anObserver) {
		observers.remove(anObserver);
	}

	/**
	 * @return the currentProgress
	 */
	public int getCurrentProgress() {
		return currentProgress;
	}

	/**
	 * @param currentProgress
	 *            the currentProgress to set
	 */
	public void setCurrentProgress(int currentProgress) {
		this.currentProgress = currentProgress;
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == IProcessInfoAware.class || adapter == IProgressVisualizer.class) {
			return this;
		}
		return super.getAdapter(adapter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.ui.core.uiprocess.IUIMonitor#acceptState(org.eclipse
	 * .riena.ui.core.uiprocess.IUIMonitorContainer)
	 */
	public boolean isActive(IUIMonitorContainer container) {
		return container.isVisualizing();
	}
}
