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
package org.eclipse.riena.ui.filter.impl;

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.ui.filter.IUIFilterRuleMarkerNavigation;
import org.eclipse.riena.ui.filter.IUIFilterRuleMarkerRidget;
import org.eclipse.riena.ui.filter.IUIFilterRuleValidatorRidget;
import org.eclipse.riena.ui.filter.extension.IRuleMapperExtension;
import org.eclipse.riena.ui.filter.extension.IRuleMarkerNavigationMapper;
import org.eclipse.riena.ui.filter.extension.IRuleMarkerRidgetMapper;
import org.eclipse.riena.ui.filter.extension.IRuleValidatorRidgetMapper;
import org.eclipse.riena.ui.internal.Activator;

/**
 * This class provides the correct rule for a given marker type.
 */
public class RulesProvider {

	private static final String EP_RULEMAPPER = "org.eclipse.riena.filter.rulemapper"; //$NON-NLS-1$
	private IRuleMapperExtension[] data;

	/**
	 * Creates a new instance of {@code RulesProvider} and perform
	 * configuration.
	 */
	public RulesProvider() {
		this(true);
	}

	/**
	 * Constructor that should only be used while testing.
	 * 
	 * @param autoConfig
	 *            - true perform configuration; otherwise do not configure
	 */
	public RulesProvider(boolean autoConfig) {
		if (autoConfig) {
			Inject.extension(EP_RULEMAPPER).into(this).andStart(Activator.getDefault().getContext());
		}
	}

	/**
	 * Returns the rule to set a marker for a ridget.
	 * 
	 * @param type
	 *            - type of marker (hidden, disabled, output or mandatory)
	 * @return rule or {@code null} if no rule was found.
	 */
	public IUIFilterRuleMarkerRidget getRuleMarkerRidget(String type) {

		IUIFilterRuleMarkerRidget rule = null;
		if (type == null) {
			return rule;
		}

		for (IRuleMapperExtension ruleMapperExtension : getData()) {

			IRuleMarkerRidgetMapper mapper = null;
			if (type.equals("hidden")) { //$NON-NLS-1$
				mapper = ruleMapperExtension.getRidgetHiddenMarker();
			} else if (type.equals("disabled")) { //$NON-NLS-1$
				mapper = ruleMapperExtension.getRidgetDisabledMarker();
			} else if (type.equals("output")) { //$NON-NLS-1$
				mapper = ruleMapperExtension.getRidgetOutputMarker();
			} else if (type.equals("mandatory")) { //$NON-NLS-1$
				mapper = ruleMapperExtension.getRidgetMandatoryMarker();
			}
			if (mapper != null) {
				rule = mapper.getRuleClass();
				if (rule != null) {
					return rule;
				}
			}
		}

		return rule;

	}

	/**
	 * Returns the rule to set a marker for a menu-/toolItem.
	 * 
	 * @param type
	 *            - type of marker (hidden or disabled)
	 * @return rule or {@code null} if no rule was found.
	 */
	public IUIFilterRuleMarkerRidget getRuleMarkerMenuItem(String type) {

		IUIFilterRuleMarkerRidget rule = null;
		if (type == null) {
			return rule;
		}

		for (IRuleMapperExtension ruleMapperExtension : getData()) {

			IRuleMarkerRidgetMapper mapper = null;
			if (type.equals("hidden")) { //$NON-NLS-1$
				mapper = ruleMapperExtension.getMenuItemHiddenMarker();
			} else if (type.equals("disabled")) { //$NON-NLS-1$
				mapper = ruleMapperExtension.getMenuItemDisabledMarker();
			}
			if (mapper != null) {
				rule = mapper.getRuleClass();
				if (rule != null) {
					return rule;
				}
			}
		}

		return rule;

	}

	/**
	 * Returns the rule to set a marker for a navigation node.
	 * 
	 * @param type
	 *            - type of marker (hidden or disabled)
	 * @return rule or {@code null} if no rule was found.
	 */
	public IUIFilterRuleMarkerNavigation getRuleMarkerNavigation(String type) {

		IUIFilterRuleMarkerNavigation rule = null;
		if (type == null) {
			return rule;
		}

		for (IRuleMapperExtension ruleMapperExtension : getData()) {

			IRuleMarkerNavigationMapper mapper = null;
			if (type.equals("hidden")) { //$NON-NLS-1$
				mapper = ruleMapperExtension.getNavigationHiddenMarker();
			} else if (type.equals("disabled")) { //$NON-NLS-1$
				mapper = ruleMapperExtension.getNavigationDisabledMarker();
			}
			if (mapper != null) {
				rule = mapper.getRuleClass();
				if (rule != null) {
					return rule;
				}
			}
		}

		return rule;

	}

	/**
	 * Returns the rule to add a validator to a ridget.
	 * 
	 * @return rule or {@code null} if no rule was found.
	 */
	public IUIFilterRuleValidatorRidget getRuleValidatorRidget() {

		IUIFilterRuleValidatorRidget rule = null;

		for (IRuleMapperExtension ruleMapperExtension : getData()) {
			IRuleValidatorRidgetMapper mapper = ruleMapperExtension.getRidgetValidator();
			if (mapper != null) {
				rule = mapper.getRuleClass();
				if (rule != null) {
					return rule;
				}
			}
		}

		return rule;

	}

	/**
	 * This is the callback method for the extension injector.
	 * 
	 * @param data
	 */
	public void update(IRuleMapperExtension[] data) {
		this.data = data;
	}

	private IRuleMapperExtension[] getData() {
		return data;
	}

}
