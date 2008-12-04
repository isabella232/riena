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
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.util.Literal;
import org.eclipse.riena.core.util.PropertiesUtils;
import org.eclipse.riena.monitor.client.ISender;
import org.eclipse.riena.monitor.client.IStore;
import org.eclipse.riena.monitor.common.Collectible;
import org.eclipse.riena.monitor.common.IReceiver;

/**
 * This simple sender implements {@code ISender} that uses riena´s (remote)
 * services to communicate with the ´server´.
 * <p>
 * The simple sender expects the following configuration that can be passed with
 * its definition in an extension:
 * <ul>
 * <li>retryTime - defines the time in minutes that will be waited for a retry
 * when a send has failed. (default value is 10 minutes if not defined)</li>
 * </ul>
 * Example extension:
 * 
 * <pre>
 * &lt;extension point=&quot;org.eclipse.riena.monitor.sender&quot;&gt;
 *       &lt;sender
 *             name=&quot;SimpleSender&quot;
 *             class=&quot;org.eclipse.riena.internal.monitor.client.SimpleSender:retryTime=20&quot;&gt;
 *       &lt;/sender&gt;
 * &lt;/extension&gt;
 * </pre>
 */
public class SimpleSender implements ISender, IExecutableExtension {

	private IStore store;
	private IReceiver receiver;
	private final ScheduledExecutorService senderExecutor;
	private boolean stopped;
	private int retryTime = 10;
	private List<String> categories = new ArrayList<String>();
	private static final String RETRY_TIME = "retryTime"; //$NON-NLS-1$

	/**
	 * Creates the {@code SimpleSender}.
	 * 
	 * @throws CoreException
	 */
	public SimpleSender() throws CoreException {
		this(true);
		// perform default initialization - maybe redone by the {@code IConfigurationElement.createExecutableExtension(String)}
		setInitializationData(null, null, null);
	}

	/**
	 * Constructor that should only be used while testing
	 * 
	 * @param autoConfig
	 *            true perform configuration; otherwise do not configure
	 */
	public SimpleSender(boolean autoConfig) {
		senderExecutor = Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory());
		if (autoConfig) {
			Inject.service(IReceiver.class).useRanking().into(this).andStart(Activator.getDefault().getContext());
		}
	}

	public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
			throws CoreException {

		Map<String, String> properties = null;
		try {
			properties = PropertiesUtils.asMap(data, Literal.map(RETRY_TIME, "10")); //$NON-NLS-1$
		} catch (IllegalArgumentException e) {
			throw configurationException("Bad configuration.", e); //$NON-NLS-1$
		}
		try {
			retryTime = Integer.valueOf(properties.get(RETRY_TIME));
			Assert.isLegal(retryTime > 0, "retryTime must be greater than 0."); //$NON-NLS-1$
		} catch (IllegalArgumentException e) {
			throw configurationException("Bad configuration. Parsing retry time failed.", e); //$NON-NLS-1$
		}
	}

	private CoreException configurationException(String message, Exception e) {
		return new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, message, e));
	}

	public void bind(IReceiver receiver) {
		this.receiver = receiver;
	}

	public void unbind(IReceiver receiver) {
		this.receiver = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.monitor.client.ISender#configureStore(org
	 * .eclipse.riena.monitor.client.ICollectibleStore)
	 */
	public void configureStore(IStore store) {
		Assert.isNotNull(store, "store must not be null");
		this.store = store;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.monitor.client.ISender#addCategory(java.
	 * lang.String )
	 */
	public void addCategory(String category) {
		Assert.isNotNull(category, "category must not be null");
		this.categories.add(category);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.monitor.client.ISender#removeCategory(java .lang.
	 * String)
	 */
	public void removeCategory(String category) {
		Assert.isNotNull(category, "category must not be null");
		this.categories.remove(category);
	}

	public void start() {
		stopped = false;

		// check if there are remaining collectibles, 
		// if so trigger transfer in the background 
		// with a slight delay
		senderExecutor.schedule(new Runnable() {
			public void run() {
				for (String category : categories) {
					triggerTransfer(category);
				}
			}
		}, 5, TimeUnit.SECONDS);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.monitor.client.ISender#stop()
	 */
	public void stop() {
		stopped = true;
		senderExecutor.shutdown();
	}

	public void triggerTransfer(String category) {
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
			List<Collectible<?>> transferables = store.retrieveTransferables(category);
			if (transferables.size() == 0) {
				System.out.println("sender ended(nothing to send) - " + category);
				return;
			}
			transfer(transferables);
			System.out.println("sender ended - " + category);
		}

		/**
		 * 
		 */
		private void transfer(List<Collectible<?>> transferables) {
			System.out.println("sender transfer " + transferables.size() + " transferables:");
			for (Collectible<?> transferable : transferables) {
				System.out.println(" - " + transferable);
			}
			try {
				if (receiver.take(System.currentTimeMillis(), transferables)) {
					store.commitTransferred(transferables);
				}
			} catch (Throwable t) {
				System.out.println("sending failed with: " + t);
				System.out.println("retrying in " + retryTime + " minutes");
				senderExecutor.schedule(new Runnable() {
					public void run() {
						triggerTransfer(category);
					}
				}, retryTime * 60, TimeUnit.SECONDS);
			}
		}
	}

}
