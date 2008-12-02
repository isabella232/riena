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
package org.eclipse.riena.demo.client.customer.controllers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.riena.demo.customer.common.CustomerRecordOverview;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.ITableRidget;

/**
 *
 */
public class CustomerOverviewController extends SubModuleController {

	private OverviewTable overview;

	@Override
	public void configureRidgets() {

		overview = new OverviewTable();
		CustomerRecordOverview cro = (CustomerRecordOverview)getNavigationNode().getContext(NavigationArgument.CONTEXT_KEY_PARAMETER);
		overview.addEntry(new OverviewEntry("Vorname", cro.getFirstName()));
		overview.addEntry(new OverviewEntry("Nachname", cro.getLastName()));

		//getNavigationNode().getParent().setLabel(CustomerLoader.getFirstName()
		// + " " + CustomerLoader.getLastName());
		final ITableRidget kunden = ((ITableRidget) getRidget("table1"));

		String[] propertyNames = { "key", "value" };
		kunden.bindToModel(overview, "entries", OverviewEntry.class, propertyNames, null);

	}

	@Override
	public void afterBind() {
		// ((Table) ((ITableRidget)
		// getRidget("table1")).getUIControl()).setHeaderVisible(false);
	}

	class Customer {

		String firstName;
		String lastName;

		Customer(String firstName, String lastName) {
			this.firstName = firstName;
			this.lastName = lastName;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

	}

	class OverviewTable {

		List<OverviewEntry> entries = new ArrayList<OverviewEntry>();

		public List<OverviewEntry> getEntries() {
			return entries;
		}

		public void setEntries(List<OverviewEntry> entries) {
			this.entries = entries;
		}

		public void addEntry(OverviewEntry entry) {
			this.entries.add(entry);
		}

	}

	class OverviewEntry {
		String key;
		String value;

		OverviewEntry(String key, String value) {
			super();
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}

}
