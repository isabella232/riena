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
package org.eclipse.riena.monitor.common;

import java.io.Serializable;
import java.util.UUID;

/**
 * A {@code Collectible} is the generic transportation element.
 */
public class Collectible<T extends Serializable> implements Serializable {

	private final String category;
	private final UUID uuid;
	private final long collectionTime;
	private final T payload;

	private static final long serialVersionUID = 1218813380792765109L;

	/**
	 * Create a {@code Collectible} for a given category.
	 * 
	 * @param category
	 *            the category for this collectible
	 * @param payload
	 *            the payload of this collectible
	 */
	public Collectible(final String category, final T payload) {
		this.category = category;
		this.uuid = UUID.randomUUID();
		this.collectionTime = System.currentTimeMillis();
		this.payload = payload;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	public UUID getUUID() {
		return uuid;
	}

	public long getCollectionTime() {
		return collectionTime;
	}

	/**
	 * @return the payload
	 */
	public T getPayload() {
		return payload;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Collectible))
			return false;
		return uuid.equals(((Collectible<?>) obj).uuid);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return uuid.hashCode();
	}

	@Override
	public String toString() {
		return "Collectible(" + category + ": " + payload + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

}
