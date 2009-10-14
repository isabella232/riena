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
		}

		if (widget instanceof Control) {
			Composite parent = ((Control) widget).getParent();
			if (parent != null) {
				String parentProperty = locateBindingProperty(parent);
				if (!StringUtils.isEmpty(parentProperty) && (parent instanceof IComplexComponent)) {
					fullProperty = parentProperty + SEPARATOR + fullProperty;
				}
			}
		}

		return fullProperty;

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
}
