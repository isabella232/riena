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
package org.eclipse.riena.example.client.controllers;

import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;

public class SharedViewDemoSubModuleController extends SubModuleController {

	private TxtBean bean;

	public SharedViewDemoSubModuleController() {
		this(null);
	}

	public SharedViewDemoSubModuleController(ISubModuleNode navigationNode) {
		super(navigationNode);
		bean = new TxtBean();
		bean.setTxt("Shared View Demo"); //$NON-NLS-1$
		bean.setName(""); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#configureRidgets()
	 */
	public void configureRidgets() {

		ILabelRidget labelFacade = (ILabelRidget) getRidget("labelFacade"); //$NON-NLS-1$
		ITextRidget textFacade = (ITextRidget) getRidget("textFacade"); //$NON-NLS-1$

		if (labelFacade != null) {
			labelFacade.bindToModel(bean, "txt"); //$NON-NLS-1$
			labelFacade.updateFromModel();
			textFacade.bindToModel(bean, "name"); //$NON-NLS-1$
			textFacade.updateFromModel();
		}
	}

	private static class TxtBean {
		private String txt;
		private String name;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getTxt() {
			return txt;
		}

		public void setTxt(String txt) {
			this.txt = txt;
		}

	}

}
