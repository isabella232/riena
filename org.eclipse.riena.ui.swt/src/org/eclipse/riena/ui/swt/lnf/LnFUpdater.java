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
package org.eclipse.riena.ui.swt.lnf;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.util.ReflectionFailure;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.internal.ui.swt.Activator;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * This class updates the properties of the UI controls according the settings
 * of the current Look&Feel.
 */
public class LnFUpdater {

	private final static Logger LOGGER = Log4r.getLogger(Activator.getDefault(), LnFUpdater.class);

	/**
	 * System property defining if properties of views are updated.
	 */
	private static final String PROPERTY_RIENA_LNF_UPDATE_VIEW = "riena.lnf.update.view"; //$NON-NLS-1$

	private final static Map<Class<? extends Control>, PropertyDescriptor[]> CONTROL_PROPERTIES = new Hashtable<Class<? extends Control>, PropertyDescriptor[]>();
	private final static Map<Class<? extends Control>, Map<String, Object>> DEFAULT_PROPERTY_VALUES = new Hashtable<Class<? extends Control>, Map<String, Object>>();
	private final static List<Class<? extends Control>> CONTROLS_AFTER_BIND = new ArrayList<Class<? extends Control>>();

	/**
	 * @since 1.2
	 */
	public static void addControlsAfterBind(Class<? extends Control> controlClass) {

		if (!CONTROLS_AFTER_BIND.contains(controlClass)) {
			CONTROLS_AFTER_BIND.add(controlClass);
		}

	}

	/**
	 * Updates the properties of all children of the given composite.
	 * 
	 * @param parent
	 *            composite which children are updated.
	 */
	public void updateUIControls(Composite parent) {

		if (!checkPropertyUpdateView()) {
			return;
		}

		Control[] controls = parent.getChildren();
		for (Control uiControl : controls) {

			updateUIControl(uiControl);

			if (uiControl instanceof Composite) {
				updateUIControls((Composite) uiControl);
			}

		}

	}

	/**
	 * Updates the properties of all children of the given composite and updates
	 * the layout of the given parent.
	 * 
	 * @param parent
	 *            composite which children are updated.
	 */
	public void updateUIControlsAfterBind(Composite parent) {

		updateAfterBind(parent);
		parent.layout();

	}

	/**
	 * Updates the properties of all children of the given composite.
	 * 
	 * @param parent
	 *            composite which children are updated.
	 */
	private void updateAfterBind(Composite parent) {

		if (!checkPropertyUpdateView()) {
			return;
		}

		Control[] controls = parent.getChildren();
		for (Control uiControl : controls) {

			if (checkUpdateAfterBind(uiControl)) {
				updateUIControl(uiControl);
			}

			if (uiControl instanceof Composite) {
				updateAfterBind((Composite) uiControl);
			}

		}

	}

	/**
	 * Checks is the given UI control must be updated after bind.
	 * 
	 * @param uiControl
	 *            UI control
	 * @return {@code true} if the control must be updated; otherwise {@code
	 *         false}
	 */
	private boolean checkUpdateAfterBind(Control uiControl) {

		if (uiControl == null) {
			return false;
		}

		if (CONTROLS_AFTER_BIND.contains(uiControl.getClass())) {
			return true;
		}

		if (uiControl.getParent() != null) {
			return checkUpdateAfterBind(uiControl.getParent());
		}

		return false;

	}

	/**
	 * Checks the value of the system property "riena.lnf.update.view".
	 * 
	 * @return value of system property
	 */
	private boolean checkPropertyUpdateView() {
		return Boolean.getBoolean(PROPERTY_RIENA_LNF_UPDATE_VIEW);
	}

	/**
	 * Updates the properties of the UI control according to the values of the
	 * LnF.
	 * 
	 * @param control
	 *            UI control
	 */
	private void updateUIControl(Control control) {

		if (!checkLnfKeys(control)) {
			return;
		}
		int classModifiers = control.getClass().getModifiers();
		if (!Modifier.isPublic(classModifiers)) {
			return;
		}
		PropertyDescriptor[] properties = getProperties(control);
		for (PropertyDescriptor property : properties) {
			Object newValue = getLnfValue(control, property);
			if (newValue == null) {
				continue;
			}
			Method setter = property.getWriteMethod();
			if (setter == null) {
				continue;
			}
			int setterModifiers = setter.getModifiers();
			if (!Modifier.isPublic(setterModifiers)) {
				continue;
			}
			if (hasNoDefaultValue(control, property)) {
				continue;
			}
			try {
				setter.invoke(control, newValue);
			} catch (IllegalArgumentException e) {
				LOGGER.log(LogService.LOG_WARNING, getErrorMessage(control, property), e);
			} catch (IllegalAccessException e) {
				LOGGER.log(LogService.LOG_WARNING, getErrorMessage(control, property), e);
			} catch (InvocationTargetException e) {
				LOGGER.log(LogService.LOG_WARNING, getErrorMessage(control, property), e);
			}
		}

	}

	/**
	 * Checks if a key for the given control exists in the current Look&Feel. If
	 * no key for the control exits, checks keys for the (optional) style exits.
	 * 
	 * @param control
	 *            UI control
	 * @return {@code true} if latest one key exists; otherwise {@code false}
	 */
	private boolean checkLnfKeys(Control control) {

		Class<? extends Control> controlClass = control.getClass();
		if (checkLnfClassKeys(controlClass)) {
			return true;
		}

		RienaDefaultLnf lnf = LnfManager.getLnf();
		String style = (String) control.getData(UIControlsFactory.KEY_LNF_STYLE);
		if (!StringUtils.isEmpty(style)) {
			style += "."; //$NON-NLS-1$
			Set<String> keys = lnf.getResourceTable().keySet();
			for (String key : keys) {
				if (key.startsWith(style)) {
					return true;
				}
			}
		}

		return false;

	}

	/**
	 * Checks if a key for the given control class exists in the current
	 * Look&Feel.
	 * 
	 * @param controlClass
	 *            class of the UI control
	 * @return {@code true} if latest one key exists; otherwise {@code false}
	 */
	@SuppressWarnings("unchecked")
	private boolean checkLnfClassKeys(Class<? extends Control> controlClass) {

		RienaDefaultLnf lnf = LnfManager.getLnf();

		String className = getSimpleClassName(controlClass);
		if (!StringUtils.isEmpty(className)) {
			className += "."; //$NON-NLS-1$
			Set<String> keys = lnf.getResourceTable().keySet();
			for (String key : keys) {
				if (key.startsWith(className)) {
					return true;
				}
			}
		}

		if (Control.class.isAssignableFrom(controlClass.getSuperclass())) {
			return checkLnfClassKeys((Class<? extends Control>) controlClass.getSuperclass());
		}

		return false;

	}

	/**
	 * Returns the simple name of a class.<br>
	 * For anonymous classes the name of the super class is returned.
	 * 
	 * @param controlClass
	 *            class of the UI control
	 * @return simple name of the class
	 */
	@SuppressWarnings("unchecked")
	private String getSimpleClassName(Class<? extends Control> controlClass) {

		if (StringUtils.isEmpty(controlClass.getSimpleName())) {
			if (Control.class.isAssignableFrom(controlClass.getSuperclass())) {
				Class<? extends Control> superClass = (Class<? extends Control>) controlClass.getSuperclass();
				return getSimpleClassName(superClass);
			} else {
				return null;
			}
		}

		return controlClass.getSimpleName();

	}

	/**
	 * Compares the default value of the UI control and the current value of the
	 * given property.
	 * 
	 * @param control
	 *            UI control
	 * @param property
	 *            property
	 * @return {@code true} if the current value of the property isn't equals
	 *         the default value; otherwise {@code false}.
	 */
	private boolean hasNoDefaultValue(Control control, PropertyDescriptor property) {

		Method getter = property.getReadMethod();
		if (getter != null) {
			Object defaultValue = getDefaultPropertyValue(control, property);
			Object currentValue = getPropertyValue(control, property);
			if (defaultValue != null) {
				if (!defaultValue.equals(currentValue)) {
					return true;
				}
			}
		}

		return false;

	}

	/**
	 * Returns the default value of the given property of the given UI control.
	 * 
	 * @param control
	 *            UI control
	 * @param property
	 *            property
	 * @return default value
	 */
	private Object getDefaultPropertyValue(Control control, PropertyDescriptor property) {

		Class<? extends Control> controlClass = control.getClass();
		if (!DEFAULT_PROPERTY_VALUES.containsKey(controlClass)) {
			Control defaultControl = null;
			try {
				defaultControl = ReflectionUtils.newInstanceHidden(controlClass, control.getParent(), control
						.getStyle());
			} catch (ReflectionFailure failure) {
				LOGGER.log(LogService.LOG_ERROR, "Cannot create an instance of \"" + controlClass.getName() + "\"", //$NON-NLS-1$ //$NON-NLS-2$
						failure);
				defaultControl = null;
			}
			if (defaultControl != null) {
				PropertyDescriptor[] properties = getProperties(control);
				Map<String, Object> defaults = new Hashtable<String, Object>(properties.length);
				for (PropertyDescriptor defaultProperty : properties) {
					Object value = getPropertyValue(defaultControl, defaultProperty);
					if (value != null) {
						defaults.put(defaultProperty.getName(), value);
					}
				}
				defaultControl.dispose();
				DEFAULT_PROPERTY_VALUES.put(controlClass, defaults);
			}
		}

		Map<String, Object> defaultValues = DEFAULT_PROPERTY_VALUES.get(controlClass);
		if (defaultValues == null) {
			return null;
		}
		return defaultValues.get(property.getName());

	}

	/**
	 * Returns the value of the given property of the given UI control.
	 * 
	 * @param control
	 *            UI control
	 * @param property
	 *            property
	 * @return value of the property or {@code null} if the property cannot
	 *         read.
	 */
	private Object getPropertyValue(Control control, PropertyDescriptor property) {

		Method getter = property.getReadMethod();
		if (getter != null) {
			try {
				return ReflectionUtils.invokeHidden(control, property.getReadMethod().getName());
			} catch (ReflectionFailure failure) {
				// TODO This is a workaround of a nebula "bug"
				if (control.getClass().getName().equals("org.eclipse.swt.nebula.widgets.compositetable.CompositeTable")) { //$NON-NLS-1$
					return null;
				}
				String message = "Cannot get the value of the property \"" + property.getName() + "\" of the class \"" //$NON-NLS-1$ //$NON-NLS-2$
						+ control.getClass().getName() + "\"."; //$NON-NLS-1$
				LOGGER.log(LogService.LOG_ERROR, message, failure);
				return null;
			}
		}

		return null;

	}

	/**
	 * Creates the error message for a given class and a given property.
	 * 
	 * @param control
	 *            the control
	 * @param property
	 *            property
	 * @return error message
	 */
	private String getErrorMessage(Control control, PropertyDescriptor property) {

		Class<? extends Control> controlClass = control.getClass();
		StringBuilder sb = new StringBuilder("Cannot update property "); //$NON-NLS-1$
		sb.append("\"" + property.getName() + "\""); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append(" of the class "); //$NON-NLS-1$
		sb.append("\"" + controlClass.getName() + "\""); //$NON-NLS-1$ //$NON-NLS-2$

		return sb.toString();

	}

	/**
	 * Returns the properties of the class of the given control.<br>
	 * The properties of the classes are cached. So introspection is only
	 * necessary for new classes.
	 * 
	 * @param control
	 *            the control
	 * @return properties
	 */
	private PropertyDescriptor[] getProperties(Control control) {

		Class<? extends Control> controlClass = control.getClass();
		if (!CONTROL_PROPERTIES.containsKey(controlClass)) {
			BeanInfo beanInfo;
			try {
				beanInfo = Introspector.getBeanInfo(controlClass);
				PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();
				CONTROL_PROPERTIES.put(controlClass, properties);
			} catch (IntrospectionException e) {
				CONTROL_PROPERTIES.put(controlClass, new PropertyDescriptor[] {});
			}
		}

		return CONTROL_PROPERTIES.get(controlClass);

	}

	/**
	 * Returns for the given control and the given property the corresponding
	 * value of the LnF.
	 * 
	 * @param control
	 *            the control
	 * @param property
	 *            description of one property
	 * @return value of LnF
	 */
	private Object getLnfValue(Control control, PropertyDescriptor property) {

		Object lnfValue = getLnfStyleValue(control, property);
		if (lnfValue == null) {
			Class<? extends Control> controlClass = control.getClass();
			lnfValue = getLnfValue(controlClass, property);
		}
		return lnfValue;

	}

	/**
	 * Returns for th given control class and the given property the
	 * corresponding value of the LnF.
	 * 
	 * @param controlClass
	 *            class of the control
	 * @param property
	 *            description of one property
	 * @return value of LnF
	 */
	@SuppressWarnings("unchecked")
	private Object getLnfValue(Class<? extends Control> controlClass, PropertyDescriptor property) {

		RienaDefaultLnf lnf = LnfManager.getLnf();
		String lnfKey = generateLnfKey(controlClass, property);
		Object lnfValue = lnf.getResource(lnfKey.toString());

		if (lnfValue == null) {
			if (Control.class.isAssignableFrom(controlClass.getSuperclass())) {
				lnfValue = getLnfValue((Class<? extends Control>) controlClass.getSuperclass(), property);
			}
		}

		return lnfValue;

	}

	/**
	 * Generates the LnF key with the given parameters.
	 * 
	 * @param controlClass
	 *            class of the control
	 * @param property
	 *            description of one property
	 * @return LnF key
	 */
	private String generateLnfKey(Class<? extends Control> controlClass, PropertyDescriptor property) {

		String controlName = getSimpleClassName(controlClass);
		StringBuilder lnfKey = new StringBuilder(controlName);
		lnfKey.append("."); //$NON-NLS-1$
		lnfKey.append(property.getName());

		return lnfKey.toString();

	}

	/**
	 * Returns for the given control and the given property the corresponding
	 * value of the LnF style.
	 * 
	 * @param control
	 *            the control with style "attribute"
	 * @param property
	 *            property
	 * @return value of Lnf or {@code null} if not style exists
	 */
	private Object getLnfStyleValue(Control control, PropertyDescriptor property) {

		String style = (String) control.getData(UIControlsFactory.KEY_LNF_STYLE);
		if (StringUtils.isEmpty(style)) {
			return null;
		}

		RienaDefaultLnf lnf = LnfManager.getLnf();
		String lnfKey = style + "." + property.getName(); //$NON-NLS-1$
		return lnf.getResource(lnfKey);

	}

}
