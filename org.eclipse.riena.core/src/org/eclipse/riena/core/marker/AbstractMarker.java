/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.core.marker;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Assert;

/**
 * An abstract implementation of the {@link IMarker IMarker}interface which
 * implements managing the attributes.
 * 
 */
public abstract class AbstractMarker implements IMarker {

	private Map<String, Object> attributeMap = new HashMap<String, Object>(0, 1.0f);
	private Set<IMarkerAttributeChangeListener> attributeChangeListeners = new HashSet<IMarkerAttributeChangeListener>(
			1, 1.0f);

	/**
	 * @see IMarker#getAttribute(String)
	 */
	public Object getAttribute(String name) {
		Assert.isNotNull(name, "name must not be null");
		return attributeMap.get(name);
	}

	/**
	 * @see IMarker#getAttribute(String, Object)
	 */
	public Object getAttribute(String name, Object defaultValue) {
		Object value = getAttribute(name);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}

	/**
	 * @see IMarker#getAttributes()
	 */
	public Map<String, Object> getAttributes() {
		return new HashMap<String, Object>(attributeMap);
	}

	/**
	 * @see IMarker#setAttribute(String, Object)
	 */
	public void setAttribute(String name, Object value) {
		Assert.isNotNull(name, "name must not be null");
		Object oldValue = attributeMap.get(name);
		if ((value != null && !value.equals(oldValue)) || (value == null && oldValue != null)) {
			attributeMap.put(name, value);
			fireAttributesChanged();
		}
	}

	private void fireAttributesChanged() {
		Iterator<IMarkerAttributeChangeListener> listenerIterator = attributeChangeListeners.iterator();
		while (listenerIterator.hasNext()) {
			IMarkerAttributeChangeListener listener = (IMarkerAttributeChangeListener) listenerIterator.next();
			listener.attributesChanged();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.core.marker.IMarker#addAttributeChangeListener(org.
	 * eclipse.riena.core.marker.IMarkerAttributeChangeListener)
	 */
	public void addAttributeChangeListener(IMarkerAttributeChangeListener listener) {
		attributeChangeListeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.core.marker.IMarker#removeAttributeChangeListener(org
	 * .eclipse.riena.core.marker.IMarkerAttributeChangeListener)
	 */
	public void removeAttributeChangeListener(IMarkerAttributeChangeListener listener) {
		attributeChangeListeners.remove(listener);
	}

	/**
	 * @see Object#equals(Object)
	 */
	public boolean equals(Object other) {
		if (!(other instanceof AbstractMarker)) {
			return false;
		}
		if (!getClass().equals(other.getClass())) {
			return false;
		}
		AbstractMarker otherMarker = (AbstractMarker) other;
		return getAttributes().equals(otherMarker.getAttributes());
	}

	/**
	 * @see Object#hashCode()
	 */
	public int hashCode() {
		return getClass().hashCode();
	}

}