package org.eclipse.riena.beans.common;

/**
 * A simple bean.
 */
public class TestBean {

	/**
	 * The name of the 'property' property of the bean.
	 */
	public static final String PROPERTY = "property"; //$NON-NLS-1$

	private Object property;

	/**
	 * @return The property.
	 */
	public Object getProperty() {
		return property;
	}

	/**
	 * Sets the property.
	 * 
	 * @param property
	 *            The new property value.
	 */
	public void setProperty(Object property) {
		this.property = property;
	}
}
