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
package org.eclipse.riena.navigation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.riena.navigation.ISimpleNavigationNodeListener;

/**
 * This annotation is used to mark a method as target for an automatically
 * generated listener in {@link INavigattionNodeController} controllers like:
 * 
 * <pre>
 * INavigationNode.addSimpleListener({@link ISimpleNavigationNodeListener})
 * </pre>
 * 
 * The {@code event} parameter denotes the corresponding
 * {@link ISimpleNavigationNodeListener} event method.
 * 
 * @since 3.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnNavigationNodeEvent {

	enum Event {
		BEFORE_ACTIVATED, AFTER_ACTIVATED, LABEL_CHANGED, ICON_CHANGED, SELECTED_CHANGED, CHILD_ADDED, CHILD_REMOVED, PRESENTATION_CHANGED, PARENT_CHANGED, EXPANDED_CHANGED, MARKER_CHANGED, ACTIVATED, DEACTIVATED, BEFORE_DECTIVATED, AFTER_DECTIVATED, DISPOSED, BEFORE_DISPOSED, AFTER_DISPOSED, STATE_CHANGED, BLOCK, FILTER_ADDED, FILTER_REMOVED, PREPARED, NODE_ID_CHANGED
	};

	/**
	 * The type of event.
	 * 
	 * @return the type of event
	 */
	Event event();
}
