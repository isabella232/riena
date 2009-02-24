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
package org.eclipse.riena.navigation.ui.swt.views;

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

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.internal.navigation.ui.swt.Activator;
import org.eclipse.riena.ui.swt.ChoiceComposite;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.osgi.service.log.LogService;

/**
 * This class updates the properties of the UI controls according the settings
 * of the current Look&Feel.
 */
public class LnFUpdater {

	private final static Logger LOGGER = Activator.getDefault().getLogger(LnFUpdater.class.getName());

	/**
	 * System property defining if properties of views are update.
	 */
	private static final String PROPERTY_RIENA_LNF_UPDATE_VIEW = "riena.lnf.update.view"; //$NON-NLS-1$

	private final static Map<Class<? extends Control>, PropertyDescriptor[]> CONTROL_PROPERTIES = new Hashtable<Class<? extends Control>, PropertyDescriptor[]>();
	private final static List<Class<? extends Control>> CONTROLS_AFTER_BIND = new ArrayList<Class<? extends Control>>();

	/**
	 * Creates a new instance of {@code LnFUpdater}.
	 */
	public LnFUpdater() {
		initListOfControlsAfterBind();
	}

	/**
	 * Initializes the list of controls which (and their children) must be
	 * updated after bind.
	 */
	private void initListOfControlsAfterBind() {

		if (CONTROLS_AFTER_BIND.isEmpty()) {
			CONTROLS_AFTER_BIND.add(ChoiceComposite.class);
		}

	}

	/**
	 * Updates the properties of all children of the given composite.
	 * 
	 * @param parent
	 *            - composite which children are updated.
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
	 * Updates the properties of all children of the given composite.
	 * 
	 * @param parent
	 *            - composite which children are updated.
	 */
	public void updateUIControlsAfterBind(Composite parent) {

		if (!checkPropertyUpdateView()) {
			return;
		}

		Control[] controls = parent.getChildren();
		for (Control uiControl : controls) {

			if (checkUpDateAfterBind(uiControl)) {
				updateUIControl(uiControl);
			}

			if (uiControl instanceof Composite) {
				updateUIControlsAfterBind((Composite) uiControl);
			}

		}

	}

	/**
	 * Checks is the given UI control must be updated after bind.
	 * 
	 * @param uiControl
	 *            - UI control
	 * @return {@code true} if the control must be updated; otherwise {@code
	 *         false}
	 */
	private boolean checkUpDateAfterBind(Control uiControl) {

		if (uiControl == null) {
			return false;
		}

		if (CONTROLS_AFTER_BIND.contains(uiControl.getClass())) {
			return true;
		}

		if (uiControl.getParent() != null) {
			return checkUpDateAfterBind(uiControl.getParent());
		}

		return false;

	}

	/**
	 * Checks the value of the system property "riena.lnf.update.view".
	 * 
	 * @return value of system property
	 */
	private boolean checkPropertyUpdateView() {

		String updateStrg = System.getProperty(PROPERTY_RIENA_LNF_UPDATE_VIEW);
		return Boolean.parseBoolean(updateStrg);

	}

	/**
	 * Updates the properties of the UI control according to the values of the
	 * LnF.
	 * 
	 * @param control
	 *            - UI control
	 */
	private void updateUIControl(Control control) {

		Class<? extends Control> controlClass = control.getClass();
		PropertyDescriptor[] properties = getProperties(controlClass);
		for (PropertyDescriptor property : properties) {
			// System.out.println(controlClass.getSimpleName() + "." + property.getName());
			Object newValue = getLnfValue(controlClass, property);
			if (newValue == null) {
				continue;
			}
			Method setter = property.getWriteMethod();
			if (setter == null) {
				continue;
			}
			int modifiers = setter.getModifiers();
			if (!Modifier.isPublic(modifiers)) {
				continue;
			}
			try {
				setter.invoke(control, newValue);
			} catch (IllegalArgumentException e) {
				LOGGER.log(LogService.LOG_WARNING, getErrorMessage(controlClass, property), e);
			} catch (IllegalAccessException e) {
				LOGGER.log(LogService.LOG_WARNING, getErrorMessage(controlClass, property), e);
			} catch (InvocationTargetException e) {
				LOGGER.log(LogService.LOG_WARNING, getErrorMessage(controlClass, property), e);
			}
		}

	}

	/**
	 * Creates the error message for a given class and a given property.
	 * 
	 * @param controlClass
	 *            - class of UI control
	 * @param property
	 *            - property
	 * @return error message
	 */
	private String getErrorMessage(Class<? extends Control> controlClass, PropertyDescriptor property) {

		StringBuilder sb = new StringBuilder("Cannot update property "); //$NON-NLS-1$
		sb.append("\"" + property.getName() + "\""); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append(" of the class "); //$NON-NLS-1$
		sb.append("\"" + controlClass.getName() + "\""); //$NON-NLS-1$ //$NON-NLS-2$

		return sb.toString();

	}

	/**
	 * Returns the properties of the given class.<br>
	 * The properties of the classes are cached. So introspection is only
	 * nessecary for new classes.
	 * 
	 * @param controlClass
	 *            - class of the control
	 * @return properties
	 */
	private PropertyDescriptor[] getProperties(Class<? extends Control> controlClass) {

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
	 * Returns for the given class and the given property the corresponding
	 * value of the LnF.
	 * 
	 * @param controlClass
	 *            - class of the control
	 * @param property
	 *            - property
	 * @return value of Lnf
	 */
	private Object getLnfValue(Class<? extends Control> controlClass, PropertyDescriptor property) {

		RienaDefaultLnf lnf = LnfManager.getLnf();
		String controlName = controlClass.getSimpleName();
		String lnfKey = controlName + "." + property.getName(); //$NON-NLS-1$
		return lnf.getResource(lnfKey);

	}

}
