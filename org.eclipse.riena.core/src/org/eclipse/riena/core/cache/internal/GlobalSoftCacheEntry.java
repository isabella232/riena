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
package org.eclipse.riena.core.cache.internal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.ArrayList;

import org.eclipse.riena.core.cache.CacheFailure;

/**
 * CacheEntry for global use
 * 
 * @author Christian Campo
 */
public class GlobalSoftCacheEntry extends SoftCacheEntry implements ICacheEntry {

	private ArrayList<SoftReference<?>> values;
	private SoftReference<?> serializedValue;
	private ReferenceQueue<?> queue;

	/**
	 * @param value
	 * @param key
	 * @param queue
	 */
	@SuppressWarnings("unchecked")
	public GlobalSoftCacheEntry(Object value, Object key, ReferenceQueue<?> queue) {
		// do not store the value
		super(key);
		this.queue = queue;
		values = new ArrayList<SoftReference<?>>();
		values.add(new SoftReference(value, queue));
	}

	/**
	 * @see org.eclipse.riena.core.cache.internal.ICacheEntry#getValue()
	 */
	public Object getValue() {
		return getValue(this.getClass().getClassLoader());
	}

	/**
	 * @param classLoader
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Object getValue(ClassLoader classLoader) {
		for (SoftReference<?> softRef : values) {
			Object obj = softRef.get();
			if (obj == null) {
				return null;
			}
			if (obj.getClass().getClassLoader() == null) {
				return obj;
			}
			ClassLoader parmCurrentCL = classLoader;
			while (parmCurrentCL != null) {
				if (obj.getClass().getClassLoader() == parmCurrentCL) {
					return obj;
				}
				parmCurrentCL = parmCurrentCL.getParent();
			}
		}
		byte[] byteArray;
		// if we need the value and put value was never serialized
		if (serializedValue == null) {
			// why on earth would the first value be NULL ??
			if (values.get(0) == null) {
				return null;
			}
			SoftReference<?> ref = values.get(0);
			Object value = ref.get();
			// if the garbage collector has removed the value in the meantime,
			// we don't check the others but
			// rather decide that the whole value is gone (highly unlikly, since
			// this was checked above, but you never
			// know)
			if (value == null) {
				return null;
			}
			try {
				// serialize the value
				ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
				new ObjectOutputStream(byteStream).writeObject(value);
				byteArray = byteStream.toByteArray();
				serializedValue = new SoftReference(new SerializedValue(byteArray), queue);
			} catch (IOException e) {
				throw new CacheFailure("error creating cache value", e);
			}
		} else {
			SerializedValue value = (SerializedValue) serializedValue.get();
			if (value == null) {
				return null;
			}
			byteArray = value.getSerializedContent();
		}

		try {
			Object obj = new MyObjectInputStream(classLoader, new ByteArrayInputStream(byteArray)).readObject();
			values.add(new SoftReference(obj, queue));
			return obj;
		} catch (Exception e) {
			throw new CacheFailure("error creating cache value", e);
		}
	}

	/**
	 * Object for the SerializedValue
	 */
	static class SerializedValue {

		private byte[] serializedContent;

		/**
		 * Constructor for serializedValue
		 * 
		 * @param serializedContent
		 */
		public SerializedValue(byte[] serializedContent) {
			super();
			this.serializedContent = serializedContent;
		}

		/**
		 * Return the byte[] of the serialized content
		 * 
		 * @return byte[] of serialized content
		 */
		public byte[] getSerializedContent() {
			return serializedContent;
		}
	}

	static class MyObjectInputStream extends java.io.ObjectInputStream {
		private ClassLoader myLoader = null;

		/**
		 * @param newLoader
		 * @param theStream
		 * @throws IOException
		 */
		public MyObjectInputStream(ClassLoader newLoader, InputStream theStream) throws IOException {
			super(theStream);
			myLoader = newLoader;
		}

		protected Class<?> resolveClass(ObjectStreamClass osc) throws IOException, ClassNotFoundException {

			return Class.forName(osc.getName(), true, myLoader);
		}
	}

}