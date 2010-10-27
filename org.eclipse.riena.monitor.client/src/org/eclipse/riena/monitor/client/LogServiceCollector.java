/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
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

import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.log.ExtendedLogEntry;
import org.eclipse.equinox.log.ExtendedLogReaderService;

import org.eclipse.riena.core.util.PropertiesUtils;
import org.eclipse.riena.core.wire.InjectService;
import org.eclipse.riena.internal.monitor.client.Activator;
import org.eclipse.riena.monitor.common.LogEntryTransferObject;

/**
 * Collects logs that are delivered by the
 * {@code org.osgi.service.log.LogListener}.
 * <p>
 */
public class LogServiceCollector extends AbstractCollector implements IExecutableExtension {

	private Range collectRange;
	private Range triggerRange;
	private ExtendedLogReaderService extendedLogReaderService;
	private final LogListener logListener;

	private static final String TRIGGER_RANGE = "triggerRange"; //$NON-NLS-1$
	private static final String COLLECT_RANGE = "collectRange"; //$NON-NLS-1$

	/**
	 * Default/Standard constructor
	 */
	public LogServiceCollector() {
		logListener = newLogListener();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org
	 * .eclipse.core.runtime.IConfigurationElement, java.lang.String,
	 * java.lang.Object)
	 */
	public void setInitializationData(final IConfigurationElement config, final String propertyName, final Object data)
			throws CoreException {
		Map<String, String> properties = null;
		try {
			properties = PropertiesUtils.asMap(data, COLLECT_RANGE, TRIGGER_RANGE);
			collectRange = new Range(properties.get(COLLECT_RANGE));
			triggerRange = new Range(properties.get(TRIGGER_RANGE));
		} catch (final IllegalArgumentException e) {
			throw configurationException("Bad configuration.", e); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.monitor.client.AbstractCollector#doStart()
	 */
	@Override
	protected void doStart() {
		if (extendedLogReaderService == null) {
			return;
		}
		extendedLogReaderService.addLogListener(logListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.monitor.client.AbstractCollector#doStop()
	 */
	@Override
	protected void doStop() {
		if (extendedLogReaderService == null) {
			return;
		}
		extendedLogReaderService.removeLogListener(logListener);
	}

	@InjectService(useRanking = true)
	public void bind(final ExtendedLogReaderService extendedLogReaderService) {
		this.extendedLogReaderService = extendedLogReaderService;
	}

	public void unbind(final ExtendedLogReaderService extendedLogReaderService) {
		extendedLogReaderService.removeLogListener(logListener);
		this.extendedLogReaderService = null;
	}

	private LogListener newLogListener() {
		return new LogListener() {
			public void logged(final LogEntry entry) {
				if (!collectRange.matches(entry.getLevel())) {
					return;
				}
				collect(new LogEntryTransferObject((ExtendedLogEntry) entry));
				if (triggerRange.matches(entry.getLevel())) {
					triggerTransfer();
				}
			}
		};
	}

	private CoreException configurationException(final String message, final Exception e) {
		return new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, message, e));
	}

}
