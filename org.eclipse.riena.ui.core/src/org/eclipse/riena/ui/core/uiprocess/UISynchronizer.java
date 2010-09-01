/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.core.uiprocess;

import org.eclipse.riena.core.wire.InjectExtension;
import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.internal.ui.core.Activator;

/**
 * The {@code UISynchronizer} creates {@code IUISynchronizer} instances which
 * can be used to perform execution of processes with in the UI.
 * 
 * <pre>
 * &lt;extension point=&quot;org.eclipse.riena.ui.core.uiSynchronizer&quot;&gt;
 *     &lt;uiSynchronizer class=&quot;org.eclipse.riena.ui.swt.uiprocess.SwtUISynchronizer&quot;/&gt;
 * &lt;/extension&gt;
 * </pre>
 * 
 * In case multiple extension points exist, which define an implementation class
 * for the {@code IUISynchronizer}, there is no guarantee which one will be
 * used.
 * 
 * @since 1.2
 */
public final class UISynchronizer {

	private static UISynchronizer singelton;

	static {
		singelton = new UISynchronizer();
		Wire.instance(singelton).andStart(Activator.getDefault().getContext());
	}

	private IUISynchronizerExtension synchronizerExtension;

	private UISynchronizer() {
		// utility
	}

	/**
	 * Create a new configured {@code IUISynchronizer}.
	 * 
	 * @return a {@code IUISynchronizer}
	 */
	public static IUISynchronizer createSynchronizer() {
		return singelton.create();
	}

	private IUISynchronizer create() {
		if (synchronizerExtension != null) {
			return synchronizerExtension.createUISynchronizer();
		}
		throw new IllegalStateException(
				"There is NO IUISynchronizer defined, but it must. Use extension point 'uiSynchronizer' to do this."); //$NON-NLS-1$
	}

	@InjectExtension(min = 1, max = 1)
	public void update(final IUISynchronizerExtension synchronizerExtension) {
		this.synchronizerExtension = synchronizerExtension;
	}

}
