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
package org.eclipse.riena.monitor.client;

import java.io.Serializable;

import org.eclipse.riena.monitor.common.Collectible;

/**
 * Abstract {@code ICollector} performing common stuff.
 */
public abstract class AbstractCollector implements ICollector {

	private ICollectingAggregator aggregator;
	private Category category;
	private IClientInfoProvider clientInfoProvider;
	private boolean started;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.monitor.client.ICollector#getCategory()
	 */
	public Category getCategory() {
		return category;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.monitor.client.ICollector#setAggregator(org.eclipse
	 * .riena.monitor.client.ICollectingAggregator)
	 */
	public void setAggregator(ICollectingAggregator aggregator) {
		this.aggregator = aggregator;
	}

	protected ICollectingAggregator getAggregator() {
		return aggregator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.monitor.client.ICollector#setCategory(org.eclipse.riena
	 * .monitor.client.Category)
	 */
	public void setCategory(Category category) {
		this.category = category;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.monitor.client.ICollector#setClientInfoProvider(org
	 * .eclipse.riena.monitor.client.IClientInfoProvider)
	 */
	public void setClientInfoProvider(IClientInfoProvider clientInfoProvider) {
		this.clientInfoProvider = clientInfoProvider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.monitor.client.ICollector#start()
	 */
	public void start() {
		if (started) {
			return;
		}
		doStart();
		started = true;
	}

	/**
	 * Hook method for start actions.
	 */
	protected void doStart() {
	}

	protected boolean isReady() {
		return aggregator != null && category != null && started;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.monitor.client.ICollector#stop()
	 */
	public void stop() {
		if (!started) {
			return;
		}
		doStop();
		started = false;
	}

	/**
	 * Hook method for stop actions.
	 */
	protected void doStop() {
	}

	/**
	 * Collect the payload.
	 * 
	 * @param <T>
	 * @param payload
	 * @return
	 */
	protected <T extends Serializable> void collect(T payload) {
		if (!isReady()) {
			return;
		}
		aggregator.collect(new Collectible<T>(clientInfoProvider == null ? null : clientInfoProvider.getClientInfo(),
				category.getName(), payload));
	}

	/**
	 * Trigger transfer.
	 */
	protected void triggerTransfer() {
		aggregator.triggerTransfer(category.getName());
	}
}
