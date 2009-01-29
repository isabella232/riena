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
package org.eclipse.riena.core.wire;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.core.runtime.Assert;
import org.osgi.framework.BundleContext;

/**
 * Marks a class that needs <i>wiring</i>.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface WireWrap {

	/**
	 * Attach the wire-wrap class. If not specified a special marker class will
	 * indicate to use the convention rule for finding the wire-wrap class. The
	 * rule is to post-fix the <i>bean</i> class with <code>WireWrap</code>.
	 * 
	 * @return
	 */
	Class<? extends IWireWrap> value() default UseDefaultWiring.class;

	/**
	 * Marker wire-wrap class.
	 */
	class UseDefaultWiring implements IWireWrap {

		public void wire(Object bean, BundleContext context) {
			Assert.isLegal(false, "Must never be used for anything else than marking!"); //$NON-NLS-1$
		}

	}
}
