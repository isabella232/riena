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

import java.beans.EventHandler;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IRidgetContainer;
import org.eclipse.riena.ui.ridgets.ITraverseRidget;
import org.eclipse.riena.ui.ridgets.annotation.HandlesActionCallback;

/**
 * Annotation handler for {@code @HandlesActionCallback}
 * 
 * @since 3.0
 */
public class ActionCallbackAnnotationHandler extends AbstractRidgetContainerAnnotationHandler {

	public void handleAnnotation(final Annotation annotation, final IRidgetContainer ridgetContainer,
			final Method method) {

		if (annotation instanceof HandlesActionCallback) {
			final IRidget ridget = getRidget(annotation, method, ridgetContainer,
					((HandlesActionCallback) annotation).ridgetId());
			final String eventPropertyName = method.getParameterTypes().length == 0 ? null : ""; //$NON-NLS-1$
			if (ridget instanceof IActionRidget) {
				((IActionRidget) ridget).addListener(EventHandler.create(IActionListener.class, ridgetContainer,
						method.getName(), eventPropertyName, null));
			} else if (ridget instanceof ITraverseRidget) {
				((ITraverseRidget) ridget).addListener(EventHandler.create(IActionListener.class, ridgetContainer,
						method.getName(), eventPropertyName, null));
			} else {
				errorUnsupportedRidgetType(annotation, ridget);
			}

		}
	}
}
