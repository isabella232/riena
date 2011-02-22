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
package org.eclipse.riena.ui.ridgets.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.listener.IFocusListener;

/**
 * This annotation is used to mark a method as target for an automatically
 * generated listener ("focusLost" event)
 * 
 * <pre>
 * ridget.addFocusListener( {@link IFocusListener} )
 * </pre>
 * 
 * for the ridget with the given ridget id.
 * <p>
 * All {@link IRidget}s are supported.
 * 
 * @since 3.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HandlesFocusLost {

	/**
	 * The valid ridget id.
	 * 
	 * @return ridgetId
	 */
	String ridgetId();
}
