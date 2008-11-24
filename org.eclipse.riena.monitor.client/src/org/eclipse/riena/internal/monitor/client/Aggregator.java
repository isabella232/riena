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
package org.eclipse.riena.internal.monitor.client;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.util.Iter;
import org.eclipse.riena.monitor.client.IAggregator;
import org.eclipse.riena.monitor.client.ICollectibleSender;
import org.eclipse.riena.monitor.client.ICollectibleStore;
import org.eclipse.riena.monitor.client.ICollector;
import org.eclipse.riena.monitor.common.Collectible;

/**
 * The {@code Aggregator} aggregates all collectibles from the collectors. Each
 * collectible may trigger the transmission of the collectibles.
 */
public class Aggregator implements IAggregator {

	private ICollectibleStore store;
	private ICollectibleSender collectibleSender;
	private ICollector[] collectors;

	public Aggregator() {
		this(true);
	}

	/**
	 * Constructor that should only be used while testing
	 * 
	 * @param autoConfig
	 *            true perform configuration; otherwise do not configure
	 */
	protected Aggregator(boolean autoConfig) {
		if (autoConfig) {
			Inject
					.extension("org.eclipse.riena.monitor.collector").useType(ICollectorExtension.class).into(this).andStart(Activator.getDefault().getContext()); //$NON-NLS-1$
			Inject
					.extension("org.eclipse.riena.monitor.store").expectingMinMax(0, 1).useType(ICollectibleStoreExtension.class).into(this).andStart(Activator.getDefault().getContext()); //$NON-NLS-1$
			Inject
					.extension("org.eclipse.riena.monitor.sender").expectingMinMax(0, 1).useType(ISenderExtension.class).into(this).andStart(Activator.getDefault().getContext()); //$NON-NLS-1$
		}
	}

	public void start() {
		for (ICollector collector : Iter.able(collectors)) {
			collectibleSender.addCategory(collector.getCategory());
			collector.start();
		}
		collectibleSender.start();
	}

	public void stop() {
		for (ICollector collector : Iter.able(collectors)) {
			collector.stop();
			collectibleSender.removeCategory(collector.getCategory());
		}
		collectibleSender.stop();
		store.flush();
	}

	public void update(ICollectorExtension[] collectorExtensions) {
		for (ICollector collector : Iter.able(this.collectors)) {
			collector.stop();
		}
		List<ICollector> list = new ArrayList<ICollector>(collectorExtensions.length);
		for (ICollectorExtension extension : collectorExtensions) {
			ICollector collector = extension.createCollector();
			collector.configureCategory(extension.getCategory());
			collector.configureAggregator(this);
			list.add(collector);
		}
		collectors = list.toArray(new ICollector[list.size()]);
		// TODO if we were really dynamic aware we should start them here
	}

	public void update(ISenderExtension senderExtension) {
		if (collectibleSender != null) {
			collectibleSender.stop();
		}
		if (senderExtension == null) {
			collectibleSender = new DefaultCollectibleSender();
		} else {
			collectibleSender = senderExtension.createSender();
		}
		collectibleSender.configureStore(store);
		// TODO if we were really dynamic aware we should start it here
	}

	public void update(ICollectibleStoreExtension storeExtension) {
		if (store != null) {
			store.flush();
		}
		if (storeExtension == null) {
			store = new DefaultCollectibleStore();
		} else {
			store = storeExtension.createCollectibleStore();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.monitor.client.IAggregator#collect(org.eclipse.riena
	 * .monitor .core.Collectible)
	 */
	public synchronized boolean collect(final Collectible<?> collectible) {
		store.collect(collectible);

		if (collectible.isTriggeringTransfer()) {
			collectibleSender.trigger(collectible.getCategory());
		}
		return true;
	}

}
