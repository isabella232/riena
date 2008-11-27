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
package org.eclipse.riena.monitor.client;

import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.log.ExtendedLogEntry;
import org.eclipse.equinox.log.ExtendedLogReaderService;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.util.PropertiesUtils;
import org.eclipse.riena.internal.monitor.client.Activator;
import org.eclipse.riena.monitor.client.Range.ParseException;
import org.eclipse.riena.monitor.common.Collectible;
import org.eclipse.riena.monitor.common.LogEntryTransferObject;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;

/**
 * Collects logs that are delivered by the {@code
 * org.osgi.service.log.LogListener}.
 * <p>
 * TODO Configuration - threshold, ..
 */
public class LogServiceCollector implements ICollector, LogListener, IExecutableExtension {

	private ICollectingAggregator aggregator;
	private boolean active;
	private String category;
	private Range collectRange;
	private Range triggerRange;

	private static final String TRIGGER_RANGE = "triggerRange"; //$NON-NLS-1$
	private static final String COLLECT_RANGE = "collectRange"; //$NON-NLS-1$

	/**
	 * Default/Standard constructor
	 */
	public LogServiceCollector() {
		this(true);
	}

	/**
	 * Constructor that should only be used while testing
	 * 
	 * @param autoConfig
	 *            true perform configuration; otherwise do not configure
	 */
	protected LogServiceCollector(boolean autoConfig) {
		if (autoConfig) {
			Inject.service(ExtendedLogReaderService.class).useRanking().into(this).andStart(
					Activator.getDefault().getContext());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org
	 * .eclipse.core.runtime.IConfigurationElement, java.lang.String,
	 * java.lang.Object)
	 */
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data)
			throws CoreException {
		if (!(data instanceof String)) {
			throw configurationException("Bad configuration data type. Expecting a String.", null); //$NON-NLS-1$
		}
		Map<String, String> properties = null;
		try {
			properties = PropertiesUtils.asMap((String) data, COLLECT_RANGE, TRIGGER_RANGE);
		} catch (IllegalArgumentException e) {
			throw configurationException("Bad configuration.", e); //$NON-NLS-1$
		}
		try {
			collectRange = new Range(properties.get(COLLECT_RANGE));
		} catch (ParseException e) {
			throw configurationException("Bad configuration. Parsing collect range fails.", e); //$NON-NLS-1$
		}
		try {
			triggerRange = new Range(properties.get(TRIGGER_RANGE));
		} catch (ParseException e) {
			throw configurationException("Bad configuration. Parsing trigger range fails.", e); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.monitor.client.ICollector#configureAggregator(org.eclipse
	 * .riena.monitor.client.IAggregator)
	 */
	public void configureAggregator(ICollectingAggregator aggregator) {
		this.aggregator = aggregator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.monitor.client.ICollector#configureCategory(java.lang
	 * .String)
	 */
	public void configureCategory(String category) {
		this.category = category;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.monitor.client.ICollector#startCollecting
	 * (org.eclipse.riena.monitor.client.IAggregator)
	 */
	public void start() {
		active = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.monitor.client.ICollector#stopCollecting()
	 */
	public void stop() {
		active = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.monitor.client.ICollector#getCategory()
	 */
	public String getCategory() {
		return category;
	}

	public void bind(ExtendedLogReaderService extendedLogReaderService) {
		extendedLogReaderService.addLogListener(this);
	}

	public void unbind(ExtendedLogReaderService extendedLogReaderService) {
		extendedLogReaderService.removeLogListener(this);
	}

	public void bind(ICollectingAggregator aggregator) {
		this.aggregator = aggregator;
	}

	public void unbind(IAggregator aggregator) {
		this.aggregator = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.service.log.LogListener#logged(org.osgi.service.log.LogEntry)
	 */
	public void logged(LogEntry entry) {
		if (aggregator == null || !active || !collectRange.matches(entry.getLevel())) {
			return;
		}
		aggregator.collect(new Collectible<LogEntryTransferObject>(category, new LogEntryTransferObject(
				(ExtendedLogEntry) entry)));
		if (triggerRange.matches(entry.getLevel())) {
			aggregator.triggerTransfer(category);
		}
	}

	private CoreException configurationException(String message, Exception e) {
		return new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, message, e));
	}

}
