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

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.demo.client.customer.model.SearchResultContainer;
import org.eclipse.riena.demo.customer.common.CustomerRecordOverview;
import org.eclipse.riena.demo.customer.common.CustomerSearchBean;
import org.eclipse.riena.demo.customer.common.CustomerSearchResult;
import org.eclipse.riena.demo.customer.common.ICustomerDemoService;
import org.eclipse.riena.internal.demo.customer.client.Activator;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.ISelectableRidget.SelectionType;

/**
 * customer search view controller
 */
public class CustomerSearchController extends SubModuleController {
	private CustomerSearchBean customerSearchBean = new CustomerSearchBean();
	private CustomerSearchResult ergebnis;
	private ICustomerDemoService customerDemoService;

	public void bind(ICustomerDemoService customerDemoService) {
		this.customerDemoService = customerDemoService;
	}

	public void unbind(ICustomerDemoService customerDemoService) {
		this.customerDemoService = null;
	}

	@Override
	public void configureRidgets() {
		Inject.service(ICustomerDemoService.class).into(this).andStart(
				Activator.getDefault().getBundle().getBundleContext());

		ITextRidget suchName = (ITextRidget) getRidget("suchName"); //$NON-NLS-1$
		suchName.bindToModel(customerSearchBean, "lastName"); //$NON-NLS-1$
		suchName.setMandatory(true);

		((ITextRidget) getRidget("suchVorname")).bindToModel(customerSearchBean, "firstName"); //$NON-NLS-1$ //$NON-NLS-2$
		((ITextRidget) getRidget("suchPlz")).bindToModel(customerSearchBean, "zipcode"); //$NON-NLS-1$ //$NON-NLS-2$
		((ITextRidget) getRidget("suchOrt")).bindToModel(customerSearchBean, "city"); //$NON-NLS-1$ //$NON-NLS-2$
		((ITextRidget) getRidget("suchStrasse")).bindToModel(customerSearchBean, "street"); //$NON-NLS-1$ //$NON-NLS-2$
		// ((ILabelRidget) getRidget("treffer")).bindToModel(customerSearchBean,
		// "treffer");

		final ITableRidget kunden = ((ITableRidget) getRidget("ergebnis")); //$NON-NLS-1$
		String[] columnNames = {
				"lastname", "firstname", "custno.", "birthdate", "street", "zip", "city", "status", "salesrep", "phone" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
		String[] propertyNames = {
				"lastName", "firstName", "customerNumber", "birthdate", "street", "zipcode", "city", "status", "salesrepno", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
				"telefoneNumber" }; //$NON-NLS-1$
		final SearchResultContainer searchResultContainer = new SearchResultContainer();
		kunden.bindToModel(searchResultContainer,
				"customerList", CustomerRecordOverview.class, propertyNames, columnNames); //$NON-NLS-1$

		((IActionRidget) getRidget("openCustomer")).addListener(new IActionListener() { //$NON-NLS-1$

					public void callback() {
						int selectionIndex = kunden.getSelectionIndex();
						if (selectionIndex >= 0) {
							CustomerLoader.setFirstName(searchResultContainer.getCustomerList().get(selectionIndex)
									.getFirstName());
							CustomerLoader.setLastName(searchResultContainer.getCustomerList().get(selectionIndex)
									.getLastName());
							getNavigationNode()
									.navigate(
											new NavigationNodeId(
													"org.eclipse.riena.demo.client.module.CustomerRecord", Integer.valueOf(selectionIndex).toString()), //$NON-NLS-1$
											new NavigationArgument(searchResultContainer.getCustomerList().get(
													selectionIndex)));
						}
					}
				});

		((IActionRidget) getRidget("reset")).addListener(new IActionListener() { //$NON-NLS-1$

					public void callback() {
						searchResultContainer.setCustomerList(null);
						getRidget("ergebnis").updateFromModel(); //$NON-NLS-1$
					}
				});
		((IActionRidget) getRidget("search")).addListener(new IActionListener() { //$NON-NLS-1$

					public void callback() {
						searchResultContainer.setCustomerList(null);
						getRidget("ergebnis").updateFromModel(); //$NON-NLS-1$
						ergebnis = customerDemoService.suche(getSuchPerson());
						// List<PersonenSucheErgebnisBean> kundenRows = new
						// ArrayList<PersonenSucheErgebnisBean>();
						if (ergebnis == null || ergebnis.getFehler()) {
							((ILabelRidget) getRidget("treffer")).setText("Keine Treffer"); //$NON-NLS-1$ //$NON-NLS-2$
							getRidget("treffer").updateFromModel(); //$NON-NLS-1$
							return;
						}
						((ILabelRidget) getRidget("treffer")).setText(ergebnis.getErgebnismenge() + " Treffer"); //$NON-NLS-1$ //$NON-NLS-2$
						getRidget("treffer").updateFromModel(); //$NON-NLS-1$
						List<CustomerRecordOverview> result = new ArrayList<CustomerRecordOverview>();
						for (CustomerRecordOverview cust : ergebnis.getErgebnis()) {
							result.add(cust);
						}
						searchResultContainer.setCustomerList(result);
						getRidget("ergebnis").updateFromModel(); //$NON-NLS-1$
					}
				});
	}

	public CustomerSearchBean getSuchPerson() {
		return customerSearchBean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.navigation.ui.controllers.SubModuleController#afterBind
	 * ()
	 */
	@Override
	public void afterBind() {
		super.afterBind();

		ITableRidget kunden = ((ITableRidget) getRidget("ergebnis")); //$NON-NLS-1$
		kunden.setSelectionType(SelectionType.MULTI);
		for (int i = 0; i < 9; i++) {
			kunden.setColumnSortable(i, true);
		}
	}
}
