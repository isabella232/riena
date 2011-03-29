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
package org.eclipse.riena.internal.core.exceptionmanager;

import org.eclipse.core.runtime.Assert;

/**
 * 
 */
public class TopologicalNode<T> {
	private final String name;
	private final String before;
	private final T element;
	private int pointToMe;

	public TopologicalNode(final String name, final String before, final T element) {
		Assert.isLegal(name != null && name.length() > 0, "name is required"); //$NON-NLS-1$
		this.name = name;
		this.before = before == null ? "" : before; //$NON-NLS-1$
		this.element = element;
	}

	public String getName() {
		return name;
	}

	public String getBefore() {
		return before;
	}

	public int getPointToMe() {
		return pointToMe;
	}

	public T getElement() {
		return element;
	}

	public void increase() {
		pointToMe++;
	}

	public void decrease() {
		pointToMe--;
	}

	@Override
	public String toString() {
		return "[" + name + ",<-" + pointToMe + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}
