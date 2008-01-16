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
package org.eclipse.riena.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * This class offers methods for generic clone operations.
 * 
 */
public final class CloneHelper {

	/**
	 * Private default constructor.
	 */
	private CloneHelper() {
		super();
	}

	/**
	 * try to (deep) clone the object passed.
	 * 
	 * @param obj
	 *            the object to clone.
	 * @return the cloned object.
	 * @throws CloneNotSupportedException
	 */
	public static Object cloneObject(Object obj) throws CloneNotSupportedException {
		Object clonedObject;

		if (obj instanceof java.lang.String) {
			clonedObject = obj; // Strings are immutable, so they can be shared.
		} else if (obj instanceof Serializable) {
			try { // serialize+deserialize the object to get a deep copy
				byte[] array = objectToByteArray(obj);
				clonedObject = byteArrayToObject(array);
			} catch (Throwable ex) {
				throw new CloneNotSupportedException();
			}
		} else if (obj instanceof Cloneable) {
			try {
				Method method = obj.getClass().getMethod("clone", new Class[0]);
				clonedObject = method.invoke(obj, new Object[0]);
			} catch (Throwable ex) {
				throw new CloneNotSupportedException();
			}
		} else {
			throw new CloneNotSupportedException();
		}

		return clonedObject;
	}

	/**
	 * convert any serializable object to a byte array.
	 * 
	 * @param obj
	 *            the object reference.
	 * @return the result byte array.
	 * @throws IOException
	 *             thrown in any case of errors.
	 */
	private static byte[] objectToByteArray(Object obj) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(obj);
		oos.flush();
		byte[] array = bos.toByteArray();
		bos.close();
		return array;
	}

	/**
	 * convert a byte array which represents a serializable object to the object
	 * itself.
	 * 
	 * @param array
	 *            the byte array.
	 * @return the object.
	 * @throws ClassNotFoundException -
	 *             Classloader problem.
	 * @throws IOException -
	 *             all other problems.
	 */
	private static Object byteArrayToObject(byte[] array) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bais = new ByteArrayInputStream(array);
		ObjectInputStream ois = new ObjectInputStream(bais);
		return ois.readObject();
	}
}