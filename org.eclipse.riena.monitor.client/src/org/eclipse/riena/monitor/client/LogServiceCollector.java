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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.log.ExtendedLogEntry;
import org.eclipse.equinox.log.ExtendedLogReaderService;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.monitor.client.Activator;
import org.eclipse.riena.monitor.client.Range.ParseException;
import org.eclipse.riena.monitor.common.Collectible;
import org.eclipse.riena.monitor.common.LogEntryTransferObject;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;

/**
 * Collect logs. TODO Configuration - threshold, ..
 */
public class LogServiceCollector implements ICollector, LogListener, IExecutableExtension {

	private IAggregator aggregator;
	private boolean active;
	private String category;
	private Range collectRange;
	private Range triggerRange;

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
		String[] ranges = ((String) data).split(","); //$NON-NLS-1$
		if (ranges.length != 2) {
			throw configurationException(
					"Bad configuration. Expecting two string parts, i.e. collect range and trigger range separated by a comma ','.", //$NON-NLS-1$
					null);
		}
		try {
			collectRange = new Range(ranges[0]);
		} catch (ParseException e) {
			throw configurationException("Bad configuration. Parsing collect range fails.", e); //$NON-NLS-1$
		}
		try {
			triggerRange = new Range(ranges[1]);
		} catch (ParseException e) {
			throw configurationException("Bad configuration. Parsing trigger range fails.", e); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.monitor.client.ICollector#setMonitor(org.eclipse.riena
	 * .monitor.core.IMonitor)
	 */
	public void configureAggregator(IAggregator aggregator) {
		this.aggregator = aggregator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.monitor.client.ICollector#setCategory(java.lang.String)
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

	public void bind(IAggregator aggregator) {
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
				(ExtendedLogEntry) entry), triggerTransmission(entry)));
	}

	/**
	 * @param entry
	 * @return
	 */
	protected boolean triggerTransmission(LogEntry entry) {
		return triggerRange.matches(entry.getLevel());
	}

	private CoreException configurationException(String message, ParseException e) {
		return new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, message, e));
	}

}
