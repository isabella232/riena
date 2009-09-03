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
package org.eclipse.riena.ui.ridgets.uibinding;

import org.eclipse.riena.core.injector.extension.ExtensionInterface;

/**
 * @since 1.2
 *
 */
@ExtensionInterface
public interface ICorrespondingLabelConfigExtension {

	//String EXTENSION_POINT_ID = Activator.PLUGIN_ID + ".correspondingridget.config"; //$NON-NLS-1$ // FIXME

	String EXTENSION_POINT_ID = "org.eclipse.riena.ui.ridgets.correspondinglabel.config"; //$NON-NLS-1$

	String getLabelPrefix();
}
