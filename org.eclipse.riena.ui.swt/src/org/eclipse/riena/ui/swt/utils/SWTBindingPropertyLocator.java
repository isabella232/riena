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
package org.eclipse.riena.ui.swt.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.ui.common.IComplexComponent;
import org.eclipse.riena.ui.ridgets.uibinding.IBindingPropertyLocator;

/**
 * Helper class to get the ID of a SWT UI control used for binding.
 */
public final class SWTBindingPropertyLocator implements IBindingPropertyLocator {

	public final static String BINDING_PROPERTY = "binding_property"; //$NON-NLS-1$
	private static SWTBindingPropertyLocator locator;

	private SWTBindingPropertyLocator() {
		// prevent instantiation
	}

	/**
	 * Returns an instance of this class.
	 * 
	 * @return
	 */
	public static SWTBindingPropertyLocator getInstance() {
		if (locator == null) {
			locator = new SWTBindingPropertyLocator();
		}
		return locator;
	}

	/**
	 * Returns all bounds controls in the given composite and it's children.
	 * 
	 * @param composite
	 *            a Composite instance; never null
	 * @return a list of bounds Controls; never null; may be empty
	 * @since 1.2
	 */
	public static List<Object> getControlsWithBindingProperty(Composite composite) {
		Map<String, Control> map = new HashMap<String, Control>();
		collectControlsWithBindingProperty(composite, map);
		return new ArrayList<Object>(map.values());
	}

	public String locateBindingProperty(Object uiControl) {
		if (uiControl instanceof Widget) {
			Widget widget = (Widget) uiControl;
			if (widget.isDisposed()) {
				return null;
			}
			return locateBindingProperty(widget);
		}

		if (uiControl instanceof IPropertyNameProvider) {
			return ((IPropertyNameProvider) uiControl).getPropertyName();
		}

		return null;
	}

	public void setBindingProperty(Object uiControl, String id) {
		if (uiControl instanceof Widget) {
			Widget control = (Widget) uiControl;
			if (control.isDisposed()) {
				return;
			}
			control.setData(BINDING_PROPERTY, id);
		} else if (uiControl instanceof IPropertyNameProvider) {
			((IPropertyNameProvider) uiControl).setPropertyName(id);
		}
	}

	// helping methods
	//////////////////

	private static void collectControlsWithBindingProperty(Composite parent, Map<String, Control> result) {
		SWTBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();
		for (Control control : parent.getChildren()) {
			String bindingProperty = locator.locateBindingProperty(control);
			if (StringUtils.isGiven(bindingProperty)) {
				if (result.containsKey(bindingProperty)) {
					Control firstControl = result.get(bindingProperty);
					String msg = String.format("Binding property '%s' used by several widgets: %s and %s", //$NON-NLS-1$
							bindingProperty, firstControl, control);
					throw new RuntimeException(msg);
				} else {
					result.put(bindingProperty, control);
				}
			}
			if (control instanceof Composite) {
				collectControlsWithBindingProperty((Composite) control, result);
			}
		}
	}

	/**
	 * Returns the binding property of the given UI control. This method pays
	 * attention, if the widget is a child of a complex element. In this case
	 * the binding property of the complex element is added to the binding
	 * property (full property).
	 * 
	 * @param widget
	 *            UI control
	 * @return full binding property
	 */
	private String locateBindingProperty(Widget widget) {
		String fullProperty = (String) widget.getData(BINDING_PROPERTY);
		if (StringUtils.isEmpty(fullProperty)) {
			fullProperty = ""; //$NON-NLS-1$
		} else {
			if (widget instanceof Control) {
				Composite parent = ((Control) widget).getParent();
				if (parent != null) {
					String parentProperty = locateBindingProperty(parent);
					if (!StringUtils.isEmpty(parentProperty) && (parent instanceof IComplexComponent)) {
						fullProperty = parentProperty + SEPARATOR + fullProperty;
					}
				}
			}
		}
		return fullProperty;
	}
}
