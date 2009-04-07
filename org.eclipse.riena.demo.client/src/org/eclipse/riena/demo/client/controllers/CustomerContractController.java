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
package org.eclipse.riena.demo.client.controllers;

import org.eclipse.core.databinding.observable.list.WritableList;

import org.eclipse.riena.demo.common.Contract;
import org.eclipse.riena.demo.common.Customer;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.listener.SubModuleNodeListener;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IMasterDetailsDelegate;
import org.eclipse.riena.ui.ridgets.IMasterDetailsRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IRidgetContainer;
import org.eclipse.riena.ui.ridgets.ITextRidget;

/**
 *
 */
public class CustomerContractController extends SubModuleController {

	@Override
	public void configureRidgets() {

		Customer customer = (Customer) getNavigationNode().getParent().getContext(
				NavigationArgument.CONTEXT_KEY_PARAMETER);

		ITextRidget firstName = (ITextRidget) getRidget("firstname");
		firstName.bindToModel(customer, "firstName");
		firstName.setMandatory(true);
		ITextRidget lastName = (ITextRidget) getRidget("lastname");
		lastName.setMandatory(true);
		lastName.bindToModel(customer, "lastName");

		IMasterDetailsRidget master = (IMasterDetailsRidget) getRidget("contracts");
		master.setDelegate(new ContractDelegate());
		String[] properties = new String[] { "contractNo", "contractValue", "status" };
		String[] headers = new String[] { "contract#", "value", "status" };
		master.bindToModel(new WritableList(customer.getContracts(), Contract.class), Contract.class, properties,
				headers);
		// master.updateFromModel();

		updateAllRidgetsFromModel();

		getNavigationNode().addListener(new SubModuleNodeListener() {
			@Override
			public void activated(ISubModuleNode source) {
				updateAllRidgetsFromModel();
			}
		});
	}

	public static final class ContractDelegate implements IMasterDetailsDelegate {

		private final Contract workingCopy = createWorkingCopy();

		// configure ridgets for the details
		public void configureRidgets(IRidgetContainer container) {

			ITextRidget txtcontractNo = (ITextRidget) container.getRidget("contractno"); //$NON-NLS-1$
			txtcontractNo.setMandatory(true);
			txtcontractNo.bindToModel(workingCopy, "contractNo");
			txtcontractNo.updateFromModel();

			ITextRidget txtLast = (ITextRidget) container.getRidget("description"); //$NON-NLS-1$
			// txtLast.setMandatory(true);
			txtLast.bindToModel(workingCopy, "description");
			txtLast.updateFromModel();

			ITextRidget txtValue = (ITextRidget) container.getRidget("value"); //$NON-NLS-1$
			txtValue.setMandatory(true);
			txtValue.bindToModel(workingCopy, "contractValue");
			txtValue.updateFromModel();

			ITextRidget txtStatus = (ITextRidget) container.getRidget("status"); //$NON-NLS-1$
			// txtStatus.setMandatory(true);
			txtStatus.bindToModel(workingCopy, "status");
			txtStatus.updateFromModel();

		}

		public Contract createWorkingCopy() {
			return new Contract();
		}

		public Contract copyBean(final Object source, final Object target) {
			Contract from = source != null ? (Contract) source : createWorkingCopy();
			Contract to = target != null ? (Contract) target : createWorkingCopy();
			to.setContractNo(from.getContractNo());
			to.setDescription(from.getDescription());
			to.setContractValue(from.getContractValue());
			to.setStatus(from.getStatus());
			return to;
		}

		public Object getWorkingCopy() {
			return workingCopy;
		}

		public void updateDetails(IRidgetContainer container) {
			for (IRidget ridget : container.getRidgets()) {
				ridget.updateFromModel();
			}
		}

		public boolean isChanged(Object source, Object target) {
			Contract source2 = (Contract) source;
			Contract target2 = (Contract) target;
			return (!source2.getContractNo().equals(target2.getContractNo())
					|| !(source2.getContractValue() != target2.getContractValue())
					|| !source2.getDescription().equals(target2.getDescription()) || !source2.getStatus().equals(
					target2.getStatus()));
		}

		public String isValid(IRidgetContainer container) {
			return null;
		}

	}

}
