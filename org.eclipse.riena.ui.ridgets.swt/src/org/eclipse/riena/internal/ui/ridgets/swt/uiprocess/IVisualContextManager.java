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

import java.util.List;

public interface IVisualContextManager {

	List<Object> getActiveContexts(List<Object> contexts);

	void addContextUpdateListener(IContextUpdateListener listener, Object context);

}
