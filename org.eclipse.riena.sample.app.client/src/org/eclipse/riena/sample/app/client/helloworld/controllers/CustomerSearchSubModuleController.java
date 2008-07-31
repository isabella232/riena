package org.eclipse.riena.sample.app.client.helloworld.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.sample.app.client.Activator;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManager;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManagerAccessor;
import org.eclipse.riena.sample.app.client.helloworld.views.CustomerDetailsSubModuleView;
import org.eclipse.riena.sample.app.common.model.Customer;
import org.eclipse.riena.sample.app.common.model.ICustomerSearch;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;

public class CustomerSearchSubModuleController extends SubModuleController {

	private ICustomerSearch service;

	private ITableRidget tableRidget;
	private IActionRidget searchAction;
	private IActionRidget clearAction;
	private IActionRidget openAction;

	private ITextFieldRidget numberRidget;
	private ITextFieldRidget firstNameRidget;
	private ITextFieldRidget lastNameRidget;

	private ResultContainer searchResult;
	WritableValue tableSelection;

	private Customer sample;

	public CustomerSearchSubModuleController(ISubModuleNode navigationNode) {
		super(navigationNode);
		searchResult = new ResultContainer();
		sample = new Customer();
		Inject.service(ICustomerSearch.class.getName()).into(this).andStart(Activator.getContext());
	}

	public void bind(ICustomerSearch service) {
		this.service = service;
	}

	public void unbind(ICustomerSearch service) {
		if (this.service == service) {
			this.service = null;
		}
	}

	public void setTableRidget(ITableRidget tableFacade) {
		this.tableRidget = tableFacade;
	}

	public ITableRidget getTableRidget() {
		return tableRidget;
	}

	public void setSearchAction(IActionRidget searchAction) {
		this.searchAction = searchAction;
	}

	public IActionRidget getSearchAction() {
		return searchAction;
	}

	public void setClearAction(IActionRidget clearAction) {
		this.clearAction = clearAction;
	}

	public IActionRidget getClearAction() {
		return clearAction;
	}

	public void setOpenAction(IActionRidget openAction) {
		this.openAction = openAction;
	}

	public IActionRidget getOpenAction() {
		return openAction;
	}

	public void setFirstNameRidget(ITextFieldRidget firstNameRidget) {
		this.firstNameRidget = firstNameRidget;
	}

	public ITextFieldRidget getFirstNameRidget() {
		return firstNameRidget;
	}

	public void setLastNameRidget(ITextFieldRidget lastNameRidget) {
		this.lastNameRidget = lastNameRidget;
	}

	public ITextFieldRidget getLastNameRidget() {
		return lastNameRidget;
	}

	public void setNumberRidget(ITextFieldRidget numberRidget) {
		this.numberRidget = numberRidget;
	}

	public ITextFieldRidget getNumberRidget() {
		return numberRidget;
	}

	private class SearchCallback implements IActionListener {

		public void callback() {
			searchCustomers();
		}
	}

	protected void searchCustomers() {
		Customer[] result = service.findCustomer(sample);
		this.searchResult.setList(Arrays.asList(result));
		tableRidget.updateFromModel();
	}

	@Override
	public void afterBind() {
		super.afterBind();
		String[] columnProperties = new String[] { Customer.PROPERTY_CUSTOMER_NUMBER, Customer.PROPERTY_LAST_NAME,
				Customer.PROPERTY_FIRST_NAME, Customer.PROPERTY_PHONE_BUSINESS };
		tableRidget.bindToModel(searchResult, "list", Customer.class, columnProperties, new String[] { "Number",
				"Lastname", "Firstname", "phone" });

		tableSelection = new WritableValue();
		tableRidget.bindSingleSelectionToModel(tableSelection);
		searchAction.addListener(new SearchCallback());
		firstNameRidget.bindToModel(sample, "firstName");
		lastNameRidget.bindToModel(sample, "lastName");
		clearAction.addListener(new ClearCallback());
		openAction.addListener(new OpenCallback());
	}

	private class OpenCallback implements IActionListener {
		public void callback() {
			Object selectedValue = tableSelection.getValue();
			if (selectedValue == null) {
				return;
			}
			if (!(selectedValue instanceof Customer)) {
				throw new RuntimeException("invalid datatype for selected value");
			}
			Customer selected = (Customer) selectedValue;
			ISubModuleNode node = getNavigationNode();
			SwtPresentationManager presentation = SwtPresentationManagerAccessor.getManager();
			SubModuleNode cNode = new SubModuleNode(selected.getFirstName());
			cNode.setContext(selected);
			presentation.present(cNode, CustomerDetailsSubModuleView.ID);
			node.addChild(cNode);
			cNode.activate();
		}
	}

	private class ClearCallback implements IActionListener {

		public void callback() {
			sample.setCustomerNumber(null);
			sample.setFirstName("");
			sample.setLastName("");
			firstNameRidget.updateFromModel();
			lastNameRidget.updateFromModel();
		}
	}

	public class ResultContainer {

		private List<Customer> list = new ArrayList<Customer>();

		public List<Customer> getList() {
			return list;
		}

		public void setList(List<Customer> list) {
			this.list = list;
		}

	}
}
