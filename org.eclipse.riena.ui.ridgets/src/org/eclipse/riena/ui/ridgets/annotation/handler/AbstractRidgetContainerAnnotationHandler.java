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
package org.eclipse.riena.ui.ridgets.annotation.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IRidgetContainer;

/**
 * "Helper" for concrete annotation handlers.
 * 
 * @since 3.0
 */
public abstract class AbstractRidgetContainerAnnotationHandler implements IRidgetContainerAnnotationHandler {

	protected IRidget getRidget(final Annotation annotation, final Method method,
			final IRidgetContainer ridgetContainer, final String ridgetId) {
		final IRidget ridget = ridgetContainer.getRidget(ridgetId);
		if (ridget != null) {
			return ridget;
		}
		throw new IllegalStateException(annotation + " defined unknown ridget id on method '" + method + "'."); //$NON-NLS-1$ //$NON-NLS-2$
	}

	protected void errorUnsupportedRidgetType(final Annotation annotation, final IRidget ridget) {
		throw new IllegalStateException(annotation + " defined for incompatible ridget type '" //$NON-NLS-1$
				+ ridget.getClass().getName() + "' with id '" + ridget.getID() + "'."); //$NON-NLS-1$ //$NON-NLS-2$
	}

}
