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
package org.eclipse.riena.ui.ridgets.databinding;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.runtime.Assert;

/**
 * TODO [ev] javadoc
 * 
 * @since 1.2
 */
public final class ConverterFactory<K, L> {

	private final Class<K> fromType;
	private final Class<L> toType;
	private final Map<K, L> modelToUI;

	public ConverterFactory(Class<K> fromType, Class<L> toType) {
		Assert.isLegal(fromType != toType);
		this.fromType = fromType;
		this.toType = toType;
		modelToUI = new HashMap<K, L>();
	}

	public ConverterFactory<K, L> add(K fromValue, L toValue) {
		modelToUI.put(fromValue, toValue);
		return this;
	}

	public IConverter createFromToConverter() {
		return new Converter(fromType, toType) {
			public Object convert(Object fromObject) {
				return modelToUI.get(fromObject);
			}
		};
	}

	public IConverter createToFromConverter() {
		return new Converter(toType, fromType) {
			public Object convert(Object fromObject) {
				K result = null;
				Iterator<Entry<K, L>> iter = modelToUI.entrySet().iterator();
				while (result == null && iter.hasNext()) {
					Entry<K, L> entry = iter.next();
					if (entry.getValue().equals(fromObject)) {
						result = entry.getKey();
					}
				}
				return result;
			}
		};
	}

}
