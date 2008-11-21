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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.Assert;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.monitor.client.ICollectibleSender;
import org.eclipse.riena.monitor.client.ICollectibleStore;
import org.eclipse.riena.monitor.common.Collectible;
import org.eclipse.riena.monitor.common.ICollectibleReceiver;

/**
 *
 */
public class DefaultCollectibleSender implements ICollectibleSender {

	private ICollectibleStore store;
	private ICollectibleReceiver receiver;
	private final ScheduledExecutorService senderExecutor;
	private boolean stopped;
	private List<String> categories = new ArrayList<String>();

	/**
	 * Creates the {@code DefaultCollectibleSender}.
	 */
	public DefaultCollectibleSender() {
		this(true);
	}

	/**
	 * Constructor that should only be used while testing
	 * 
	 * @param autoConfig
	 *            true perform configuration; otherwise do not configure
	 */
	public DefaultCollectibleSender(boolean autoConfig) {
		senderExecutor = Executors.newSingleThreadScheduledExecutor();
		if (autoConfig) {
			Inject.service(ICollectibleReceiver.class).useRanking().into(this).andStart(
					Activator.getDefault().getContext());
		} else {
			// Test stuff
			this.receiver = new ICollectibleReceiver() {
				public boolean take(long senderTime, List<Collectible<?>> collectibles) {
					for (Collectible<?> collectible : collectibles) {
						System.out.println("Got(" + senderTime + " ms): " + collectible);
					}
					return true;
				}
			};
		}
	}

	public void bind(ICollectibleReceiver receiver) {
		this.receiver = receiver;
	}

	public void unbind(ICollectibleReceiver receiver) {
		this.receiver = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.monitor.client.ICollectibleSender#setStoreProperties
	 * (java.io .File, java.io.FilenameFilter)
	 */
	public void setStore(ICollectibleStore store) {
		Assert.isNotNull(store, "store must not be null");
		this.store = store;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.monitor.client.ICollectibleSender#addCategory(java.
	 * lang.String )
	 */
	public void addCategory(String category) {
		Assert.isNotNull(category, "category must not be null");
		this.categories.add(category);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.monitor.client.ICollectibleSender#removeCategory(java
	 * .lang. String)
	 */
	public void removeCategory(String category) {
		Assert.isNotNull(category, "category must not be null");
		this.categories.remove(category);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.monitor.client.ICollectibleSender#setReceiver(org.eclipse
	 * .riena .monitor.core.ICollectibleReceiver)
	 */
	public void setReceiver(ICollectibleReceiver receiver) {
		Assert.isNotNull(receiver, "receiver must not be null");
		this.receiver = receiver;
	}

	public void start() {
		stopped = false;

		// check if there are remaining collectibles, 
		// if so trigger transfer in the background 
		// with a slight delay
		senderExecutor.schedule(new Runnable() {
			public void run() {
				for (String category : categories) {
					trigger(category);
				}
			}
		}, 5, TimeUnit.SECONDS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.monitor.client.ICollectibleSender#stop()
	 */
	public void stop() {
		stopped = true;
		senderExecutor.shutdown();
	}

	public void trigger(String category) {
		if (stopped) {
			return;
		}
		Sender sender = new Sender(category);
		senderExecutor.execute(sender);
	}

	private class Sender implements Runnable {

		private final String category;

		private Sender(String category) {
			this.category = category;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			System.out.println("sender start - " + category);
			if (receiver == null) {
				System.out.println("sender ended(no receiver) - " + category);
				return;
			}
			List<Collectible<?>> transferables = store.getTransferables(category);
			if (transferables.size() == 0) {
				System.out.println("sender ended(nothing to do) - " + category);
				return;
			}
			transfer(transferables);
			System.out.println("sender ended - " + category);
		}

		/**
		 * 
		 */
		private void transfer(List<Collectible<?>> transferables) {
			for (Collectible<?> transferable : transferables) {
				System.out.println("sender transfer:" + transferable);
			}
			try {
				if (receiver.take(System.currentTimeMillis(), transferables)) {
					store.commitTransferred(transferables);
				}
			} catch (Throwable t) {
				System.out.println("sending failed with: " + t);
				System.out.println("retrying in 10 minutes");
				senderExecutor.schedule(new Runnable() {
					public void run() {
						trigger(category);
					}
				}, 10 * 60, TimeUnit.SECONDS);
			}
		}
	}

}
