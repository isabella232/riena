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
package org.eclipse.riena.navigation.ui.swt.binding;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.DefaultSwtControlRidgetMapper;
import org.eclipse.riena.ui.ridgets.uibinding.IControlRidgetMapper;
import org.eclipse.swt.widgets.Widget;

/**
 *
 */
public class DelegatingRidgetMapper implements IControlRidgetMapper<Object> {

	public DefaultSwtControlRidgetMapper delegate;

	private Map<Class<? extends Object>, Class<? extends IRidget>> mappings;

	public DelegatingRidgetMapper(DefaultSwtControlRidgetMapper delegate) {
		this.delegate = delegate;
		this.mappings = new HashMap<Class<? extends Object>, Class<? extends IRidget>>();
	}

	@SuppressWarnings("unchecked")
	public void addMapping(Class<? extends Object> controlClazz, Class<? extends IRidget> ridgetClazz) {
		if (Widget.class.isAssignableFrom(controlClazz)) {
			delegate.addMapping((Class<? extends Widget>) controlClazz, ridgetClazz);
			return;
		}
		mappings.put(controlClazz, ridgetClazz);

	}

	public void addSpecialMapping(String controlName, Class<? extends Object> ridgetClazz) {
		delegate.addSpecialMapping(controlName, ridgetClazz);

	}

	@SuppressWarnings("unchecked")
	public Class<? extends IRidget> getRidgetClass(Class<? extends Object> controlClazz) {
		if (Widget.class.isAssignableFrom(controlClazz)) {
			return delegate.getRidgetClass((Class<? extends Widget>) controlClazz);
		}
		return mappings.get(controlClazz);

	}

	public Class<? extends IRidget> getRidgetClass(Object control) {
		if (Widget.class.isAssignableFrom(control.getClass())) {
			return delegate.getRidgetClass((Widget) control);
		}

		return getRidgetClass(control.getClass());
	}

}
