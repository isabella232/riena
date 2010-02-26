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
 * A container managing instances of {@link IProgressVisualizerObserver}.
 * {@link IUIMonitor} callbacks usually get delegated here to the
 * {@link IProgressVisualizerObserver}s.
 */
public interface IProgressVisualizer extends IUIMonitor, IProcessInfoAware {

	/**
	 * adds the {@link IProgressVisualizerObserver} to the container
	 * 
	 * @param anObserver
	 */
	void addObserver(IProgressVisualizerObserver anObserver);

	/**
	 * removes the {@link IProgressVisualizerObserver} from the container
	 * 
	 * @param anObserver
	 */
	void removeObserver(IProgressVisualizerObserver anObserver);

}
