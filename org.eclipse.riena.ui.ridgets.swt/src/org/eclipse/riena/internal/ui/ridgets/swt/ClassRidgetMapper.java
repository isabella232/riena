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
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.osgi.service.log.LogService;

import org.eclipse.core.databinding.BindingException;
import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.ui.ridgets.IRidget;

/**
 * A mapper that maps ridget interfaces to concrete ridget implementations.
 * 
 * @since 2.0
 */
public final class ClassRidgetMapper {

	private static ClassRidgetMapper instance = new ClassRidgetMapper();

	private Map<Class<? extends IRidget>, Class<? extends IRidget>> mappings;

	private static final Logger LOGGER = Log4r.getLogger(Activator.getDefault(), ClassRidgetMapper.class);

	/**
	 * Answer the singleton <code>SwtControlRidgetMapper</code>
	 * 
	 * @return the SwtControlRidgetMapper singleton
	 */
	public static ClassRidgetMapper getInstance() {
		return instance;
	}

	private ClassRidgetMapper() {
		mappings = new HashMap<Class<? extends IRidget>, Class<? extends IRidget>>();
		initDefaultMappings();
	}

	private void initDefaultMappings() {
		//		addMapping(IComboRidget.class, ComboRidget.class);
		//		addMapping(IDecimalTextRidget.class, DecimalTextRidget.class);
		//		addMapping(INumericTextRidget.class, NumericTextRidget.class);
		//		addMapping(IDateTextRidget.class, DateTextRidget.class);
		//		addMapping(IDateTimeRidget.class, DateTimeRidget.class);
		//		addMapping(ITextRidget.class, TextRidget.class);
		//		addMapping(IActionRidget.class, ActionRidget.class);
		//		addMapping(IToggleButtonRidget.class, ToggleButtonRidget.class);
		//		addMapping(ISingleChoiceRidget.class, SingleChoiceRidget.class);
		//		addMapping(IMultipleChoiceRidget.class, MultipleChoiceRidget.class);
		//		addMapping(ITableRidget.class, TableRidget.class);
		//		addMapping(IListRidget.class, ListRidget.class);
		//		addMapping(ICompositeTableRidget.class, CompositeTableRidget.class);
		//		addMapping(ITreeRidget.class, TreeRidget.class);
		//		addMapping(IGroupedTreeTableRidget.class, TreeTableRidget.class);
	}

	/**
	 * Adds an Interface Class pair to the mapper. If the Interface is already
	 * existent, the previous value will be overwritten.
	 * 
	 * @param ridgetInterface
	 *            an interface extending <code>IRidget</code>
	 * @param ridgetClazz
	 *            a concrete class implementing <code>IRidget</code>. If
	 *            ridgetClazz is abstract or an interface it will not be added
	 *            and warning is logged.
	 */
	public void addMapping(Class<? extends IRidget> ridgetInterface, Class<? extends IRidget> ridgetClazz) {
		if (ridgetInterface == null || ridgetClazz == null) {
			return;
		}

		if (!ridgetClazz.isInterface() && !Modifier.isAbstract(ridgetClazz.getModifiers())) {
			mappings.put(ridgetInterface, ridgetClazz);
		} else {
			LOGGER.log(LogService.LOG_WARNING, "The interface " + ridgetInterface.getName() + " and the class " //$NON-NLS-1$//$NON-NLS-2$
					+ ridgetClazz.getName() + " could not be added to the map, because " + ridgetClazz.getName() //$NON-NLS-1$
					+ " is an abstract class or an interface"); //$NON-NLS-1$
		}
	}

	/**
	 * Returns the ridget class that belongs to the ridgetInterface in the
	 * mapper.
	 * 
	 * @param ridgetInterface
	 *            the key to search for
	 * @return the ridget class that belongs to the ridgetInterface in the
	 *         mapper
	 * @throws throws a <code>BindingException</code> if the ridgetInterface
	 *         cannot be found in the mapper.
	 */
	public Class<? extends IRidget> getRidgetClass(Class<? extends IRidget> ridgetInterface) {
		Class<? extends IRidget> ridgetClass = mappings.get(ridgetInterface);
		if (ridgetClass != null) {
			return ridgetClass;
		}

		throw new BindingException("No Ridget class defined for interface " + ridgetInterface.getName()); //$NON-NLS-1$
	}

	/**
	 * This method tries to guess which interface is implemented directly by
	 * ridgetClazz. If the class has directly implemented only one interface,
	 * that one is returned. If it has implemented more than one,
	 * <code>null</code> is returned. If no interface was implemented, but the
	 * class has a superclass the process described above is repeated on that
	 * class. <br>
	 * If an interface was found, the interface-ridgetClazz-pair is added to the
	 * map. However, the interface is only added if it is not already existent
	 * in the map. <br>
	 * Logs a warning, if the ridgetClazz is an abstract class or an interface.
	 * 
	 * @param ridgetClazz
	 *            the class to add to the map
	 */
	public void addMapping(Class<? extends IRidget> ridgetClazz) {
		if (!ridgetClazz.isInterface() && !Modifier.isAbstract(ridgetClazz.getModifiers())) {
			Class<? extends IRidget> ridgetInterface = getImplementedInterfaceFromClass(ridgetClazz);
			if (mappings.containsKey(ridgetInterface)) {
				return;
			}
			if (ridgetInterface != null) {
				addMapping(ridgetInterface, ridgetClazz);
			} else {
				LOGGER
						.log(
								LogService.LOG_WARNING,
								"The class " + ridgetClazz.getName() + " could not be added to the map, because it is an abstract class, an interface or has implemented more than one interface"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

	}

	/**
	 * Tries to search for an interface that ridgetClazz implemented. If the
	 * class has directly implemented only one interface, that one is returned.
	 * If it has implemented more than one <code>null</code> is returned. If no
	 * interface was implemented, but the class has a superclass the process is
	 * repeated on that class.
	 * 
	 * @param ridgetClazz
	 * @return the interface that ridgetClazz has implemented, <code>null</code>
	 *         if nothing was found
	 */
	@SuppressWarnings("unchecked")
	private Class<? extends IRidget> getImplementedInterfaceFromClass(Class<? extends IRidget> ridgetClazz) {
		Class<? extends IRidget> ridgetInterface = null;
		while (ridgetInterface == null && ridgetClazz != null) {
			ridgetInterface = getSingleInterfaceFromClass(ridgetClazz.getInterfaces());
			if (ridgetInterface != null) {
				break;
			}
			if (ridgetClazz.getInterfaces().length == 0) {
				ridgetClazz = (Class<? extends IRidget>) ridgetClazz.getSuperclass();
				if (!IRidget.class.isAssignableFrom(ridgetClazz)) {
					break;
				}
			} else {
				break;
			}
		}

		return ridgetInterface;
	}

	@SuppressWarnings("unchecked")
	private Class<? extends IRidget> getSingleInterfaceFromClass(Class<?>[] interfaces) {
		if (interfaces != null && interfaces.length == 1) {
			return (Class<? extends IRidget>) interfaces[0];
		}
		return null;
	}
}
