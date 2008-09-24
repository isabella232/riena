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
package org.eclipse.riena.internal.communication.factory.hessian;

import java.security.Principal;

import com.caucho.hessian.io.AbstractSerializerFactory;
import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.JavaDeserializer;
import com.caucho.hessian.io.Serializer;

public class PrincipalSerializerFactory extends AbstractSerializerFactory {

	@Override
	public Deserializer getDeserializer(Class cl) throws HessianProtocolException {
		if (cl.isInterface() && (!cl.getPackage().getName().startsWith("java") || cl == Principal.class)) { //$NON-NLS-1$
			return new JavaDeserializer(cl);
		}
		return null;
	}

	@Override
	public Serializer getSerializer(Class cl) throws HessianProtocolException {
		return null;
	}

}
