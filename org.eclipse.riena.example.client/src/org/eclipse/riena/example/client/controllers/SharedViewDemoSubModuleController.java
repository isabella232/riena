package org.eclipse.riena.example.client.controllers;

import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;

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
		ITextFieldRidget textFacade = (ITextFieldRidget) getRidget("textFacade"); //$NON-NLS-1$

		if (labelFacade != null) {
			labelFacade.bindToModel(bean, "txt"); //$NON-NLS-1$
			labelFacade.updateFromModel();
			textFacade.bindToModel(bean, "name"); //$NON-NLS-1$
			textFacade.updateFromModel();
		}
	}

	private class TxtBean {
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
