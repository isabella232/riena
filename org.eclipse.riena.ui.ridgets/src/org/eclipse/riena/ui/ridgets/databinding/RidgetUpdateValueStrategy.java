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

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.IConverter;

/**
 * 
 */
public class RidgetUpdateValueStrategy extends UpdateValueStrategy {

	private static Map<TypePair, IConverter> converterMap;

	public RidgetUpdateValueStrategy() {
		super();
	}

	public RidgetUpdateValueStrategy(int updatePolicy) {
		super(updatePolicy);
	}

	public RidgetUpdateValueStrategy(boolean provideDefaults, int updatePolicy) {
		super(provideDefaults, updatePolicy);
	}

	/**
	 * @see org.eclipse.core.databinding.UpdateStrategy#createConverter(java.lang.Object,
	 *      java.lang.Object)
	 */
	@Override
	protected IConverter createConverter(Object fromType, Object toType) {

		if (fromType == String.class) {
			if (toType == Double.TYPE) {
				return StringToNumberAllowingNullConverter.toPrimitiveDouble();
			} else if (toType == Float.TYPE) {
				return StringToNumberAllowingNullConverter.toPrimitiveFloat();
			} else if (toType == Long.TYPE) {
				return StringToNumberAllowingNullConverter.toPrimitiveLong();
			} else if (toType == Integer.TYPE) {
				return StringToNumberAllowingNullConverter.toPrimitiveInteger();
			}
		}

		if ((fromType instanceof Class<?>) && (toType instanceof Class<?>)) {
			String fromTypeClassName = ((Class<?>) fromType).getName();
			String toTypeClassName = ((Class<?>) toType).getName();
			TypePair pair = new TypePair(fromTypeClassName, toTypeClassName);
			IConverter converter = getConverterMap().get(pair);
			if (converter != null) {
				return converter;
			}
		}

		return super.createConverter(fromType, toType);
	}

	private Map<TypePair, IConverter> getConverterMap() {

		if (converterMap == null) {
			converterMap = new HashMap<TypePair, IConverter>();
		}
		converterMap.put(new TypePair(String.class.getName(), GregorianCalendar.class.getName()),
				new StringToGregorianCalendarConverter());
		converterMap.put(new TypePair(GregorianCalendar.class.getName(), String.class.getName()),
				new GregorianCalendarToStringConverter());

		return converterMap;
	}

	/**
	 * A pair of to types.
	 */
	private static class TypePair {

		private final Object fromType;
		private final Object toType;

		/**
		 * Construct a pair with from- and to-type.
		 * 
		 * @param fromType
		 * @param toType
		 */
		public TypePair(Object fromType, Object toType) {
			this.fromType = fromType;
			this.toType = toType;
		}

		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (obj == null || obj.getClass() != this.getClass()) {
				return false;
			}
			TypePair other = (TypePair) obj;
			return fromType.equals(other.fromType) && toType.equals(other.toType);
		}

		/**
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			return fromType.hashCode() + toType.hashCode();
		}
	}

}
