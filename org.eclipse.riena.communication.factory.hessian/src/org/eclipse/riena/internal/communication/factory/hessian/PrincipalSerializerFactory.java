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
package org.eclipse.riena.internal.communication.factory.hessian;

import java.lang.reflect.Constructor;
import java.security.Principal;

import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.JavaDeserializer;
import com.caucho.hessian.io.Serializer;

import org.eclipse.riena.internal.core.ignore.Nop;

/**
 * An {@code AbstractSerializerFactory} for the Hessian protocol that supports
 * serializing {@code Principal} objects if they have a constructor with a
 * {@code String} parameter.
 */
public class PrincipalSerializerFactory extends AbstractRienaSerializerFactory {

	@Override
	public Deserializer getDeserializer(Class cl) throws HessianProtocolException {
		final Constructor<?> constructor;
		if (Principal.class.isAssignableFrom(cl) && (constructor = getStringConstructor(cl)) != null) {
			return new JavaDeserializer(cl) {
				@Override
				protected Object instantiate() throws Exception {
					return constructor.newInstance("o@o"); //$NON-NLS-1$
				}
			};
		}
		return null;
	}

	/**
	 * @param cl
	 * @return
	 */
	private Constructor<?> getStringConstructor(Class<?> cl) {
		try {
			return cl.getConstructor(String.class);
		} catch (SecurityException e) {
			Nop.reason("Fall through"); //$NON-NLS-1$
		} catch (NoSuchMethodException e) {
			Nop.reason("Fall through"); //$NON-NLS-1$
		}
		return null;
	}

	@Override
	public Serializer getSerializer(Class cl) throws HessianProtocolException {
		return null;
	}

	@Override
	public int getSalience() {
		return GENERIC;
	}

}
