package org.eclipse.riena.example.client.controllers;

import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;

public class SharedViewDemoSubModuleController extends SubModuleController {

	public SharedViewDemoSubModuleController() {
		this(null);
	}

	public SharedViewDemoSubModuleController(ISubModuleNode navigationNode) {
		super(navigationNode);
		bean = new TxtBean();
		bean.setTxt("Shared View Demo");
		bean.setName("");
	}

	private ILabelRidget labelFacade;

	private ITextFieldRidget textFacade;

	private TxtBean bean;

	public void setLabelFacade(ILabelRidget labelFacade) {
		this.labelFacade = labelFacade;
	}

	public void setTextFacade(ITextFieldRidget textFacade) {
		this.textFacade = textFacade;
	}

	public ILabelRidget getLabelFacade() {
		return labelFacade;
	}

	public ITextFieldRidget getTextFacade() {
		return textFacade;
	}

	@Override
	public void afterBind() {
		super.afterBind();
		initLabelFacade();
	}

	private void initLabelFacade() {
		if (labelFacade != null) {
			labelFacade.bindToModel(bean, "txt");
			labelFacade.updateFromModel();
			textFacade.bindToModel(bean, "name");
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
