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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.util.Iter;
import org.eclipse.riena.monitor.client.ICollectingAggregator;
import org.eclipse.riena.monitor.client.ICollector;
import org.eclipse.riena.monitor.client.ISender;
import org.eclipse.riena.monitor.client.IStore;
import org.eclipse.riena.monitor.common.Collectible;

/**
 * The {@code Aggregator} aggregates all collectibles from the collectors. Each
 * collectible may trigger the transmission of the collectibles.
 */
public class Aggregator implements ICollectingAggregator {

	private IStore store;
	private ISender sender;
	private ICollector[] collectors;
	private boolean started;

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
					.extension("org.eclipse.riena.monitor.store").expectingMinMax(0, 1).useType(IStoreExtension.class).into(this).andStart(Activator.getDefault().getContext()); //$NON-NLS-1$
			Inject
					.extension("org.eclipse.riena.monitor.sender").expectingMinMax(0, 1).useType(ISenderExtension.class).into(this).andStart(Activator.getDefault().getContext()); //$NON-NLS-1$
		}
	}

	public synchronized void start() {
		if (started) {
			return;
		}
		for (ICollector collector : Iter.able(collectors)) {
			sender.addCategory(collector.getCategory());
			collector.start();
		}
		store.open();
		sender.start();
		started = true;
	}

	public synchronized void stop() {
		if (!started) {
			return;
		}
		for (ICollector collector : Iter.able(collectors)) {
			collector.stop();
			sender.removeCategory(collector.getCategory());
		}
		sender.stop();
		store.close();
		started = false;
	}

	public void update(ICollectorExtension[] collectorExtensions) {
		for (ICollector collector : Iter.able(this.collectors)) {
			collector.stop();
		}
		Set<String> categories = new HashSet<String>(collectorExtensions.length);
		List<ICollector> list = new ArrayList<ICollector>(collectorExtensions.length);
		for (ICollectorExtension extension : collectorExtensions) {
			Assert.isLegal(!categories.contains(extension.getCategory()), "Category " + extension.getCategory() //$NON-NLS-1$
					+ " is defined twice. Categories must be unique."); //$NON-NLS-1$
			categories.add(extension.getCategory());
			ICollector collector = extension.createCollector();
			collector.configureCategory(extension.getCategory());
			collector.configureAggregator(this);
			list.add(collector);
		}
		collectors = list.toArray(new ICollector[list.size()]);
		// TODO if we were really dynamic aware we should start them here
	}

	public void update(ISenderExtension senderExtension) throws CoreException {
		if (sender != null) {
			sender.stop();
		}
		if (senderExtension == null) {
			sender = new SimpleSender();
		} else {
			sender = senderExtension.createSender();
		}
		sender.configureStore(store);
		// TODO if we were really dynamic aware we should start it here
	}

	public void update(IStoreExtension storeExtension) throws CoreException {
		if (store != null) {
			store.flush();
		}
		if (storeExtension == null) {
			store = new SimpleStore();
		} else {
			store = storeExtension.createStore();
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
		return store.collect(collectible);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.monitor.client.IAggregator#triggerTransfer(java.lang
	 * .String)
	 */
	public void triggerTransfer(String category) {
		store.prepareTransferables(category);
		sender.triggerTransfer(category);
	}

}
