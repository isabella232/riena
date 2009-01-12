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
package org.eclipse.riena.playground;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

import org.eclipse.equinox.log.internal.ExtendedLogEntryImpl;
import org.eclipse.riena.internal.tests.Activator;
import org.osgi.service.log.LogService;

import com.caucho.hessian.io.AbstractSerializerFactory;
import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.HessianSerializerInput;
import com.caucho.hessian.io.HessianSerializerOutput;
import com.caucho.hessian.io.JavaDeserializer;
import com.caucho.hessian.io.JavaSerializer;
import com.caucho.hessian.io.Serializer;

/**
 *
 */
public class Test extends TestCase {
	public void testObjectWithHessian() throws IOException {

		Object value = new ExtendedLogEntryImpl(Activator.getDefault().getBundle(), "LoggerName", this,
				LogService.LOG_ERROR, "Argh!", new IOException("IO"));
		System.out.println("In:");
		System.out.println(value);
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		HessianSerializerOutput output = new HessianSerializerOutput(bytesOut);
		output.findSerializerFactory().setAllowNonSerializable(true);
		output.findSerializerFactory().addFactory(new AbstractSerializerFactory() {

			@Override
			public Deserializer getDeserializer(Class cl) throws HessianProtocolException {
				if (cl.equals(Object.class))
					return new JavaDeserializer(cl) {
					};
				return null;
			}

			@Override
			public Serializer getSerializer(Class cl) throws HessianProtocolException {
				if (cl.equals(Object.class)) {
					return new JavaSerializer(cl);
				}
				return null;
			}
		});
		output.writeObject(value);

		byte[] b = bytesOut.toByteArray();
		System.out.println("Length: " + b.length);
		ByteArrayInputStream bytesIn = new ByteArrayInputStream(b);
		HessianSerializerInput input = new HessianSerializerInput(bytesIn);
		Object result = input.readObject();
		System.out.println("Out:");
		System.out.println(result);
	}
}
