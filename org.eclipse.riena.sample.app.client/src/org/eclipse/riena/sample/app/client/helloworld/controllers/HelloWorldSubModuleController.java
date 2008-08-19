package org.eclipse.riena.sample.app.client.helloworld.controllers;

import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.ILabelRidget;

public class HelloWorldSubModuleController extends SubModuleController {

	private ILabelRidget labelRidget;
	private TxtBean bean;

	public HelloWorldSubModuleController(ISubModuleNode navigationNode) {
		super(navigationNode);
		bean = new TxtBean();
		bean.setTxt("Hello World"); //$NON-NLS-1$
		bean.setName(""); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.controllers.SubModuleController#afterBind()
	 */
	@Override
	public void afterBind() {
		super.afterBind();
		initLabelRidget();
	}

	/**
	 * Binds and updates the label.
	 */
	private void initLabelRidget() {
		if (labelRidget != null) {
			labelRidget.bindToModel(bean, "txt"); //$NON-NLS-1$
			labelRidget.updateFromModel();
		}
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IRidgetContainer#configureRidgets()
	 */
	public void configureRidgets() {
		labelRidget = (ILabelRidget) getRidget("labelRidget"); //$NON-NLS-1$
	}

	/**
	 * The model of this sub module controller.
	 */
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
