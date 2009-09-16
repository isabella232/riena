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
package org.eclipse.riena.ui.ridgets.uibinding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.osgi.service.log.LogService;

import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.core.wire.InjectExtension;
import org.eclipse.riena.internal.ui.ridgets.Activator;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IRidgetContainer;
import org.eclipse.riena.ui.ridgets.ITextRidget;

/**
 * Connects a arbitrary {@link IRidget} with a {@link ILabelRidget} so the
 * enabled-State of the {@link ILabelRidget} reflects the enabled-State of the
 * {@link IRidget}. The default prefix for the Label is <code>"label"<code>.
 * 
 * @since 1.2
 * 
 */
public class CorrespondingLabelMapper {
	private String labelPrefix = "label"; //$NON-NLS-1$
	private IRidgetContainer ridgetContainer;
	private ILabelFinderStrategy labelFinderStrategy;
	private Logger logger = Log4r.getLogger(Activator.getDefault(), CorrespondingLabelMapper.class);

	public CorrespondingLabelMapper(IRidgetContainer ridgetContainer) {
		super();
		this.ridgetContainer = ridgetContainer;
		this.labelFinderStrategy = new DefaultLabelFinderStrategy();
	}

	/**
	 * Sets a custom labelPrefix by injecting a EclipseExtension
	 * 
	 * @param labelProperties
	 */
	@InjectExtension(id = ICorrespondingLabelConfigExtension.EXTENSION_POINT_ID, min = 0, max = 1)
	public void setCorrespondingLabelConfig(ICorrespondingLabelConfigExtension labelProperties) {
		if (null != labelProperties) {
			if (null != labelProperties.getLabelPrefix()) {
				labelPrefix = labelProperties.getLabelPrefix();
				// logger.log(LogService.LOG_INFO, "Using extensionpoint labelPrefix: " + labelPrefix); //$NON-NLS-1$
			}
		}
	}

	@InjectExtension(id = ILabelFinderStrategyExtension.EXTENSION_POINT_ID, min = 0, max = 1)
	public void setLabelFinderStrategy(ILabelFinderStrategyExtension strategyProperties) {
		logger.log(LogService.LOG_INFO, "Using extensionpoint labelFinderStrategy: " + strategyProperties); //$NON-NLS-1$

		if (null != strategyProperties) {
			if (StringUtils.isGiven(strategyProperties.getClassName())) {
				// logger.log(LogService.LOG_INFO,
				//		"Using extensionpoint labelFinderStrategy: " + strategyProperties.getClassName()); //$NON-NLS-1$
				ILabelFinderStrategy customLabelFinderStrategy = strategyProperties.createFinderStrategy();
				if (null != customLabelFinderStrategy) {
					this.labelFinderStrategy = customLabelFinderStrategy;
				}
			}
		}
	}

	/**
	 * Adds a {@link PropertyChangeListener} to the given ridget and tries to
	 * find a corresponding {@link ILabelRidget} by concatenating labelPrefix
	 * with the source ridgetId.
	 * 
	 * @param ridget
	 *            the source ridget
	 * @param ridgetId
	 * @return true if a {@link ILabelRidget} was found, otherwise false
	 */
	public boolean connectCorrespondingLabel(final IRidget ridget, final String ridgetId) {
		final ILabelRidget labelRidget = labelFinderStrategy.findLabelRidget(ridgetContainer, ridgetId);

		if (null == labelRidget) {
			return false;
		}

		ridget.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (ITextRidget.PROPERTY_ENABLED.equals(evt.getPropertyName())) {
					labelRidget.setEnabled(ridget.isEnabled());
				}
			}
		});
		return true;
	}

	private class DefaultLabelFinderStrategy implements ILabelFinderStrategy {
		public ILabelRidget findLabelRidget(final IRidgetContainer iridgetContainer, String ridgetID) {
			String labelID = labelPrefix + ridgetID;
			IRidget labelRidget = iridgetContainer.getRidget(labelID);
			if (null != labelRidget && labelRidget instanceof ILabelRidget) {
				return (ILabelRidget) labelRidget;
			}

			// logger.log(LogService.LOG_WARNING, String.format(
			//		"Corresponding Label with ridgetID: '%s' could not be found", labelID)); //$NON-NLS-1$
			return null;
		}
	}
}
