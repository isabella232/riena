/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.communication.factory.hessian;

import java.util.Comparator;

import com.caucho.hessian.io.AbstractSerializerFactory;

/**
 * The {@code AbstractRienaSerializerFactory} adds the concept of a ´salience´
 * to a serializer factory. This means that each serializer factory can define
 * it´s own salience (represented as an integer value). The serializer factory
 * with the highest salience (asumed to be more specific) will be asked before
 * serializer factories with a lower value (assumed to be more generic).<br>
 * The more specific a serializer factory is the higher it´s salience has to be.
 * So the more specifc serializers will be given a chance before the more
 * general factories.<br>
 * The term ´salience´ is taken from:<br>
 * 
 * @see <a href="http://en.wikipedia.org/wiki/CLIPS">CLIPS</a>
 */
public abstract class AbstractRienaSerializerFactory extends AbstractSerializerFactory {

	protected static final int GENERIC = 0;
	protected static final int SPECIFIC = 1000;

	/**
	 * Return the salience of the serializer factory. The default value will be
	 * {@code SPECIFIC}.
	 * 
	 * @return
	 */
	public int getSalience() {
		return SPECIFIC;
	}

	/**
	 * Compares {@code AbstractRienaSerializerFactory} instances.
	 */
	public static class SalienceComparator implements Comparator<AbstractRienaSerializerFactory> {

		public int compare(final AbstractRienaSerializerFactory o1, final AbstractRienaSerializerFactory o2) {
			return o1.getSalience() < o2.getSalience() ? 1 : o1.getSalience() > o2.getSalience() ? -1 : 0;
		}

	}
}
