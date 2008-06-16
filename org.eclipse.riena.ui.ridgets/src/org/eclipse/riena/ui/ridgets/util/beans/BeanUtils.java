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
package org.eclipse.riena.ui.ridgets.util.beans;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.logging.ConsoleLogger;
import org.eclipse.riena.internal.ui.ridgets.Activator;
import org.osgi.service.log.LogService;

/**
 * Consists exclusively of static methods that provide convenience behavior for
 * working with Java Bean properties.
 * 
 * @see Introspector
 * @see BeanInfo
 * @see PropertyDescriptor
 */

public final class BeanUtils {

	private final static boolean PROPERTY_DESCRIPTOR_CACHE_ACTIVE = "true".equalsIgnoreCase(System.getProperty(
			"propertyDescriptorCacheActive", "true"));
	private final static Map<Class, Map<String, PropertyDescriptor>> DESCRIPTOR_CACHE = new HashMap<Class, Map<String, PropertyDescriptor>>();

	private static final Logger LOGGER;

	static {
		Activator activator = Activator.getDefault();
		if (activator != null) {
			LOGGER = activator.getLogger(BeanUtils.class.getName());
		} else {
			LOGGER = new ConsoleLogger(BeanUtils.class.getName());
		}
	}

	private BeanUtils() {
		// Override default constructor; prevents instantiation.
	}

	private static PropertyDescriptor getCachedPropertyDescritor(Class pClass, String pProperty) {
		if (PROPERTY_DESCRIPTOR_CACHE_ACTIVE) {
			synchronized (DESCRIPTOR_CACHE) {
				Map<String, PropertyDescriptor> classDescriptors = DESCRIPTOR_CACHE.get(pClass);
				if (classDescriptors != null) {
					return classDescriptors.get(pProperty);
				} else {
					return null;
				}
			}
		} else {
			return null;
		}

	}

	private static void putCachedPropertyDescriptor(Class pClass, String pProperty, PropertyDescriptor pDescriptor) {
		if (PROPERTY_DESCRIPTOR_CACHE_ACTIVE) {
			synchronized (DESCRIPTOR_CACHE) {
				Map<String, PropertyDescriptor> classDescriptors = DESCRIPTOR_CACHE.get(pClass);
				if (classDescriptors == null) {
					classDescriptors = new HashMap<String, PropertyDescriptor>();
					DESCRIPTOR_CACHE.put(pClass, classDescriptors);
				}
				classDescriptors.put(pProperty, pDescriptor);
			}
		}
	}

	/**
	 * Checks and answers whether the given class supports bound properties,
	 * i.e. it provides a pair of multicast event listener registration methods
	 * for <code>PropertyChangeListener</code>s:
	 * 
	 * <pre>
	 * public void addPropertyChangeListener(PropertyChangeListener x);
	 * 
	 * public void removePropertyChangeListener(PropertyChangeListener x);
	 * </pre>
	 * 
	 * @param clazz
	 *            the class to test
	 * @return true if the class supports bound properties, false otherwise
	 */
	public static boolean supportsBoundProperties(Class clazz) {
		return (getPCLAdder(clazz) != null) && (getPCLRemover(clazz) != null);
	}

	/**
	 * Looks up and returns a <code>PropertyDescriptor</code> for the given
	 * Java Bean and property name using the standard Java Bean introspection
	 * behavior.
	 * 
	 * @param bean
	 *            the bean that holds the property
	 * @param propertyName
	 *            the name of the Bean property
	 * @return the <code>PropertyDescriptor</code>
	 * @throws IntrospectionException
	 *             if an exception occurs during introspection.
	 */
	public static PropertyDescriptor getPropertyDescriptor(Object bean, String propertyName)
			throws IntrospectionException {
		PropertyDescriptor result = getCachedPropertyDescritor(bean.getClass(), propertyName);
		if (result == null) {
			try {// try to get the descriptor directly with default getter
				// and setter
				result = new PropertyDescriptor(propertyName, bean.getClass());
			} catch (IntrospectionException ie) {
				try {// try to get the descriptor directly for properties
					// having a only getter
					result = new PropertyDescriptor(propertyName, bean.getClass(), "get"
							+ BeanUtils.capitalize(propertyName), null);
				} catch (IntrospectionException ie2) {
					// try to get the descriptor using introspection
					LOGGER.log(LogService.LOG_WARNING, "A PropertyDescriptor for the property: "
							+ bean.getClass().getName() + "." + propertyName
							+ " could not be created directly! Create get-ter and set-ter for the property!");
					BeanInfo info = Introspector.getBeanInfo(bean.getClass());
					Introspector.flushFromCaches(bean.getClass());
					PropertyDescriptor[] descriptors = info.getPropertyDescriptors();
					for (PropertyDescriptor element : descriptors) {
						if (propertyName.equals(element.getName())) {
							result = element;
						}
					}
					if (result == null) {
						throw new IntrospectionException("Property '" + propertyName + "' not found in bean " + bean);
					}
				}
			}
			putCachedPropertyDescriptor(bean.getClass(), propertyName, result);
		}
		return result;
	}

	/**
	 * Returns a String which capitalizes the first letter of the string.
	 * 
	 * @param name
	 *            The string to capitalize.
	 * @return The capitalized string.
	 */
	public static String capitalize(String name) {
		if (name == null || name.length() == 0) {
			return name;
		}
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	/**
	 * Looks up and returns a <code>PropertyDescriptor</code> for the given
	 * bean and property name. If a getter name or setter name is available,
	 * these are used to create a PropertyDescriptor. Otherwise, the standard
	 * Java Bean introspection is used to determine the property descriptor.
	 * 
	 * @param bean
	 *            the bean that holds the property
	 * @param propertyName
	 *            the name of the property to be accessed
	 * @param getterName
	 *            the optional name of the property's getter
	 * @param setterName
	 *            the optional name of the property's setter
	 * @return the <code>PropertyDescriptor</code>
	 * @throws PropertyNotFoundFailure
	 *             if the property could not be found
	 */
	public static PropertyDescriptor getPropertyDescriptor(Object bean, String propertyName, String getterName,
			String setterName) {
		try {
			if (getterName != null || setterName != null) {
				return new PropertyDescriptor(propertyName, bean.getClass(), getterName, setterName);
			} else {
				return getPropertyDescriptor(bean, propertyName);
			}
		} catch (IntrospectionException e) {
			throw new PropertyNotFoundFailure(propertyName, bean, e);
		}
	}

	/**
	 * Holds the class parameter list that is used to lookup the adder and
	 * remover methods for PropertyChangeListeners.
	 */
	private static final Class[] PCL_PARAMS = new Class[] { PropertyChangeListener.class };

	/**
	 * Holds the class parameter list that is used to lookup the adder and
	 * remover methods for PropertyChangeListeners.
	 */
	private static final Class[] NAMED_PCL_PARAMS = new Class[] { String.class, PropertyChangeListener.class };

	/**
	 * Looks up and returns the method that adds a multicast
	 * PropertyChangeListener to instances of the given class.
	 * 
	 * @param clazz
	 *            the class that provides the adder method
	 * @return the method that adds multicast PropertyChangeListeners
	 */
	public static Method getPCLAdder(Class clazz) {
		try {
			return clazz.getMethod("addPropertyChangeListener", PCL_PARAMS);
		} catch (NoSuchMethodException e) {
			return null;
		}
	}

	/**
	 * Looks up and returns the method that removes a multicast
	 * PropertyChangeListener from instances of the given class.
	 * 
	 * @param clazz
	 *            the class that provides the remover method
	 * @return the method that removes multicast PropertyChangeListeners
	 */
	public static Method getPCLRemover(Class clazz) {
		try {
			return clazz.getMethod("removePropertyChangeListener", PCL_PARAMS);
		} catch (NoSuchMethodException e) {
			return null;
		}
	}

	/**
	 * Looks up and returns the method that adds a PropertyChangeListener for a
	 * specified property name to instances of the given class.
	 * 
	 * @param clazz
	 *            the class that provides the adder method
	 * @return the method that adds the PropertyChangeListeners
	 */
	public static Method getNamedPCLAdder(Class clazz) {
		try {
			return clazz.getMethod("addPropertyChangeListener", NAMED_PCL_PARAMS);
		} catch (NoSuchMethodException e) {
			return null;
		}
	}

	/**
	 * Looks up and returns the method that removes a PropertyChangeListener for
	 * a specified property name from instances of the given class.
	 * 
	 * @param clazz
	 *            the class that provides the remover method
	 * @return the method that removes the PropertyChangeListeners
	 */
	public static Method getNamedPCLRemover(Class clazz) {
		try {
			return clazz.getMethod("removePropertyChangeListener", NAMED_PCL_PARAMS);
		} catch (NoSuchMethodException e) {
			return null;
		}
	}

	/**
	 * Adds a property change listener to the given bean . First checks whether
	 * the bean supports <em>bound properties</em>, i.e. it provides a pair
	 * of methods to register multicast property change event listeners; see
	 * section 7.4.1 of the Java Beans specification for details.
	 * 
	 * @param bean
	 *            the bean to add the property change listener from
	 * @param listener
	 *            the listener to remove
	 * @throws NullPointerException
	 *             if the bean or listener is null
	 * @throws PropertyNotBindableFailure
	 *             if the property change handler cannot be added successfully
	 */
	public static void addPropertyChangeListener(Object bean, PropertyChangeListener listener) {
		if (listener == null) {
			throw new NullPointerException("The listener must not be null.");
		}
		Class beanClass = bean.getClass();

		// Check whether the bean supports bound properties.
		if (!BeanUtils.supportsBoundProperties(beanClass)) {
			throw new PropertyUnboundFailure("Bound properties unsupported by bean class=" + beanClass
					+ "\nThe Bean class must provide a pair of methods:"
					+ "\npublic void addPropertyChangeListener(PropertyChangeListener x);"
					+ "\npublic void removePropertyChangeListener(PropertyChangeListener x);");
		}
		try {
			Method multicastPCLAdder = getPCLAdder(beanClass);
			if (multicastPCLAdder == null) {
				throw new PropertyNotBindableFailure(
						"Due to an missing method addPropertyChangeListener() we failed to add "
								+ "a multicast PropertyChangeListener to bean: " + bean);
			}
			multicastPCLAdder.invoke(bean, new Object[] { listener });
		} catch (InvocationTargetException e) {
			throw new PropertyNotBindableFailure("Due to an InvocationTargetException we failed to add "
					+ "a multicast PropertyChangeListener to bean: " + bean, e);
		} catch (IllegalAccessException e) {
			throw new PropertyNotBindableFailure("Due to an IllegalAccessException we failed to add "
					+ "a multicast PropertyChangeListener to bean: " + bean, e);
		}
	}

	/**
	 * Adds a named property change listener to the given. The bean must provide
	 * the optional support for listening on named properties as described in
	 * section 7.4.5 of the <a
	 * href="http://java.sun.com/products/javabeans/docs/spec.html">Java Bean
	 * Secification </a>. The bean class must provide the method:
	 * 
	 * <pre>
	 * public void addPropertyChangeHandler(String name, PropertyChangeListener l);
	 * </pre>
	 * 
	 * @param bean
	 *            the bean to add a property change handler
	 * @param propertyName
	 *            the name of the property to be observed
	 * @param listener
	 *            the listener to add
	 * @throws NullPointerException
	 *             if the bean, propertyName or listener is null
	 * @throws PropertyNotBindableFailure
	 *             if the property change handler cannot be added successfully
	 */
	public static void addPropertyChangeListener(Object bean, String propertyName, PropertyChangeListener listener) {

		if (propertyName == null) {
			throw new NullPointerException("The property name must not be null.");
		}
		if (listener == null) {
			throw new NullPointerException("The listener must not be null.");
		}
		Class beanClass = bean.getClass();
		Method namedPCLAdder = getNamedPCLAdder(beanClass);
		if (namedPCLAdder == null) {
			throw new PropertyNotBindableFailure("Could not find the bean method"
					+ "/npublic void addPropertyChangeListener(String, PropertyChangeListener);" + "/nin bean:" + bean);
		}
		try {
			namedPCLAdder.invoke(bean, new Object[] { propertyName, listener });
		} catch (InvocationTargetException e) {
			throw new PropertyNotBindableFailure("Due to an InvocationTargetException we failed to add "
					+ "a named PropertyChangeListener to bean: " + bean, e);
		} catch (IllegalAccessException e) {
			throw new PropertyNotBindableFailure("Due to an IllegalAccessException we failed to add "
					+ "a named PropertyChangeListener to bean: " + bean, e);
		}
	}

	/**
	 * Removes a property change listener from the given bean.
	 * 
	 * @param bean
	 *            the bean to remove the property change listener from
	 * @param listener
	 *            the listener to remove
	 * @throws NullPointerException
	 *             if the bean or listener is null
	 * @throws PropertyUnboundFailure
	 *             if the bean does not support bound properties
	 * @throws PropertyNotBindableFailure
	 *             if the property change handler cannot be removed successfully
	 */
	public static void removePropertyChangeListener(Object bean, PropertyChangeListener listener) {
		Class beanClass = bean.getClass();

		Method multicastPCLRemover = getPCLRemover(beanClass);
		if (multicastPCLRemover == null) {
			throw new PropertyUnboundFailure("Could not find the method:"
					+ "\npublic void removePropertyChangeListener(String, PropertyChangeListener x);" + "\nfor bean:"
					+ bean);
		}
		try {
			multicastPCLRemover.invoke(bean, new Object[] { listener });
		} catch (InvocationTargetException e) {
			throw new PropertyNotBindableFailure("Due to an InvocationTargetException we failed to remove "
					+ "a multicast PropertyChangeListener from bean: " + bean, e);
		} catch (IllegalAccessException e) {
			throw new PropertyNotBindableFailure("Due to an IllegalAccessException we failed to remove "
					+ "a multicast PropertyChangeListener from bean: " + bean, e);
		}
	}

	/**
	 * Removes a named property change listener to the given. The bean must
	 * provide the optional support for listening on named properties as
	 * described in section 7.4.5 of the <a
	 * href="http://java.sun.com/products/javabeans/docs/spec.html">Java Bean
	 * Secification </a>. The bean class must provide the method:
	 * 
	 * <pre>
	 * public void removePropertyChangeHandler(String name, PropertyChangeListener l);
	 * </pre>
	 * 
	 * @param bean
	 *            the bean to remove the property change listener from
	 * @param propertyName
	 *            the name of the observed property
	 * @param listener
	 *            the listener to remove
	 * @throws NullPointerException
	 *             if the bean, propertyName or listener is null
	 * @throws PropertyNotBindableFailure
	 *             if the property change handler cannot be removed successfully
	 */
	public static void removePropertyChangeListener(Object bean, String propertyName, PropertyChangeListener listener) {

		if (propertyName == null) {
			throw new NullPointerException("The property name must not be null.");
		}
		if (listener == null) {
			throw new NullPointerException("The listener must not be null.");
		}
		Class beanClass = bean.getClass();
		Method namedPCLRemover = getNamedPCLRemover(beanClass);
		if (namedPCLRemover == null) {
			throw new PropertyNotBindableFailure("Could not find the bean method"
					+ "/npublic void removePropertyChangeListener(String, PropertyChangeListener);" + "/nin bean:"
					+ bean);
		}
		try {
			namedPCLRemover.invoke(bean, new Object[] { propertyName, listener });
		} catch (InvocationTargetException e) {
			throw new PropertyNotBindableFailure("Due to an InvocationTargetException we failed to remove "
					+ "a named PropertyChangeListener from bean: " + bean, e);
		} catch (IllegalAccessException e) {
			throw new PropertyNotBindableFailure("Due to an IllegalAccessException we failed to remove "
					+ "a named PropertyChangeListener from bean: " + bean, e);
		}
	}

	/**
	 * Returns the value of the specified property of the given non-null bean.
	 * 
	 * @param bean
	 *            the bean to read the value from
	 * @param propertyDescriptor
	 *            describes the property to be read
	 * @return the bean's property value
	 * @throws NullPointerException
	 *             if the bean is null
	 */
	public static Object getValue(Object bean, PropertyDescriptor propertyDescriptor) {
		if (bean == null) {
			throw new NullPointerException("The bean must not be null.");
		}
		Method getter = propertyDescriptor.getReadMethod();
		if (getter == null) {
			throw new UnsupportedOperationException("The property '" + propertyDescriptor.getName()
					+ "' is write-only.");
		}

		try {
			return getter.invoke(bean);
		} catch (InvocationTargetException e) {
			throw PropertyAccessFailure.createReadAccessException(bean, propertyDescriptor, e);
		} catch (IllegalAccessException e) {
			throw PropertyAccessFailure.createReadAccessException(bean, propertyDescriptor, e);
		}
	}

	/**
	 * Sets the given object as new value of the specified property of the given
	 * non-null bean. This operation is unsupported if the bean property is
	 * read-only.
	 * 
	 * @param bean
	 *            the bean that holds the adapted property
	 * @param propertyDescriptor
	 *            describes the property to be set
	 * @param newValue
	 *            the property value to be set
	 * @throws NullPointerException
	 *             if the bean is null
	 */
	public static void setValue(Object bean, PropertyDescriptor propertyDescriptor, Object newValue) {
		if (bean == null) {
			throw new NullPointerException("The bean must not be null.");
		}
		if (propertyDescriptor == null) {
			throw new NullPointerException("The propertyDescriptor must not be null");
		}
		Method setter = propertyDescriptor.getWriteMethod();
		if (setter == null) {
			throw new UnsupportedOperationException("The property '" + propertyDescriptor.getName() + "' is read-only.");
		}
		try {
			setter.invoke(bean, new Object[] { newValue });
		} catch (InvocationTargetException e) {
			throw PropertyAccessFailure.createWriteAccessException(bean, newValue, propertyDescriptor, e);
		} catch (IllegalAccessException e) {
			throw PropertyAccessFailure.createWriteAccessException(bean, newValue, propertyDescriptor, e);
		} catch (IllegalArgumentException e) {
			throw PropertyAccessFailure.createWriteAccessException(bean, newValue, propertyDescriptor, e);
		}
	}

	private final static Class[] NO_PARAMS = {};

	/**
	 * calls the given method of the object
	 * 
	 * @param bean
	 *            the bean that holds the adapted property
	 * @param methodName
	 *            the method name to call
	 */
	public static void callVoidMethod(Object bean, String methodName) {
		try {
			Method method = bean.getClass().getMethod(methodName, NO_PARAMS);
			method.invoke(bean);
		} catch (Exception e) {
			throw new BeanUtilFailure("unable to call method [" + methodName + "] of object [" + bean + "]", e);
		}
	}

	private final static Class[] ONE_PARAM = { Object.class };

	/**
	 * calls the given method of the object
	 * 
	 * @param bean
	 *            the bean that holds the adapted property
	 * @param methodName
	 *            the method name to call
	 * @param param
	 *            the param to pass
	 */
	public static void callSingleParamMethod(Object bean, String methodName, Object param) {
		try {
			Method method = bean.getClass().getMethod(methodName, ONE_PARAM);
			method.invoke(bean, new Object[] { param });
		} catch (NoSuchMethodException e) {
			throw new BeanUtilFailure("unable to call method [" + methodName + "] of object [" + bean + "]", e);
		} catch (IllegalAccessException e) {
			throw new BeanUtilFailure("unable to call method [" + methodName + "] of object [" + bean + "]", e);
		} catch (InvocationTargetException e) {
			throw new BeanUtilFailure("unable to call method [" + methodName + "] of object [" + bean + "]", e);
		}
	}

	/**
	 * Calls the given method of the object
	 * 
	 * @param bean -
	 *            the bean that holds the adapted property
	 * @param methodName -
	 *            the method name to call
	 * @param paramClass -
	 *            the the class of the param
	 * @param param -
	 *            the param to pass
	 */
	public static void callSingleParamMethod(Object bean, String methodName, Class paramClass, Object param) {

		try {
			Method method = bean.getClass().getMethod(methodName, new Class[] { paramClass });
			method.invoke(bean, new Object[] { param });
		} catch (NoSuchMethodException e) {
			throw new BeanUtilFailure("unable to call method [" + methodName + "] of object [" + bean + "]", e);
		} catch (IllegalAccessException e) {
			throw new BeanUtilFailure("unable to call method [" + methodName + "] of object [" + bean + "]", e);
		} catch (InvocationTargetException e) {
			throw new BeanUtilFailure("unable to call method [" + methodName + "] of object [" + bean + "]", e);
		} catch (IllegalArgumentException e) {
			throw new BeanUtilFailure("unable to call method [" + methodName + "] of object [" + bean + "]", e);
		} // end try

	} // end method

}