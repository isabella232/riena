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
package org.eclipse.riena.ui.swt;

import org.eclipse.swt.widgets.Widget;

/**
 * Builds the content of the {@link Statusline} by including {@link Widget}s
 * like {@link StatuslineNumber} and {@link StatuslineUIProcess} into the
 * {@link Statusline}. It is also possible to add additional Widgets to the
 * {@link Statusline} at this place. Be sure to use {@link Statusline}
 * #addUIControl to add the additional Widgets for later binding with Ridgets.
 * In it´s initial state the {@link Statusline} holds a {@link StatuslineNumber}
 * and a {@link StatuslineUIProcess} which need to be layouted.
 */
public interface IStatusLineContentFactory {

	/**
	 * layouts(Includes) Widgets in(to) the {@link Statusline}. Can add
	 * additional Widgets.
	 * 
	 * @param statusline
	 *            - the {@link Statusline}
	 */
	void createContent(Statusline statusline);

}
