/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.example.client.application;

import org.eclipse.riena.example.client.views.ComboView;
import org.eclipse.riena.example.client.views.FocusableView;
import org.eclipse.riena.example.client.views.ListView;
import org.eclipse.riena.example.client.views.MarkerView;
import org.eclipse.riena.example.client.views.RidgetsSubModuleView;
import org.eclipse.riena.example.client.views.SharedViewDemoView;
import org.eclipse.riena.example.client.views.SystemPropertiesView;
import org.eclipse.riena.example.client.views.TextView;
import org.eclipse.riena.example.client.views.UiProcessDemoView;
import org.eclipse.riena.example.client.views.ValidationView;
import org.eclipse.riena.internal.example.client.Activator;
import org.eclipse.riena.navigation.IApplicationModel;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.ISubApplication;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.model.ApplicationModel;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubApplication;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.ApplicationViewController;
import org.eclipse.riena.navigation.ui.swt.application.SwtApplication;
import org.eclipse.riena.navigation.ui.swt.lnf.LnfManager;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManager;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManagerAccessor;
import org.osgi.framework.Bundle;

/**
 * Define the model of the application
 */
public class SwtExampleApplication extends SwtApplication {

	public SwtExampleApplication() {
		super();
		LnfManager.setLnf(new ExampleLnf());
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.application.SwtApplication#
	 *      createApplicationViewController
	 *      (org.eclipse.riena.navigation.IApplicationModel)
	 */
	@Override
	protected ApplicationViewController createApplicationViewController(IApplicationModel model) {
		ApplicationViewController controller = super.createApplicationViewController(model);
		controller.setMenubarVisible(true);
		return controller;
	}

	@Override
	protected IApplicationModel createModel() {

		ISubApplication subApplication = null;
		IModuleGroupNode moduleGroup = null;
		IModuleNode module = null;
		ISubModuleNode subModule = null;

		SwtPresentationManager presentation = SwtPresentationManagerAccessor.getManager();

		final IApplicationModel applicationModel = new ApplicationModel("Riena Navigation Example");
		applicationModel.setIcon(createIconPath(IExampleIcons.ICON_APPLICATION));
		// sub application 1
		subApplication = new SubApplication("Navigation");
		subApplication.setIcon(createIconPath(IExampleIcons.ICON_APPLICATION));
		presentation.present(subApplication, "subapplication.1");
		applicationModel.addChild(subApplication);
		subApplication.setSelected(true);

		moduleGroup = new ModuleGroupNode("Group 1.1");
		subApplication.addChild(moduleGroup);
		module = new ModuleNode("Module 1.1.1");
		module.setIcon(createIconPath(IExampleIcons.ICON_APPLICATION));
		moduleGroup.addChild(module);
		subModule = new SubModuleNode("SubModule 1.1.1.1");
		subModule.setIcon(createIconPath(IExampleIcons.ICON_FILE));
		presentation.present(subModule, "customerDetailView");
		module.addChild(subModule);

		SubModuleNode subModule2 = new SubModuleNode("SubModule 1.1.1.1");
		presentation.present(subModule2, "customerDetailView");
		subModule.addChild(subModule2);

		subModule = new SubModuleNode("SubModule 1.1.1.2");
		presentation.present(subModule, "customerDetailView");
		module.addChild(subModule);
		module = new ModuleNode("Module 1.1.2 (closeable)");
		module.setIcon(createIconPath(IExampleIcons.ICON_HOMEFOLDER));
		moduleGroup.addChild(module);
		subModule = new SubModuleNode("SubModule 1.1.2.1");
		presentation.present(subModule, "customerDetailView");
		module.addChild(subModule);
		subModule = new SubModuleNode("SubModule 1.1.2.2");
		presentation.present(subModule, "customerDetailView");
		module.addChild(subModule);

		moduleGroup = new ModuleGroupNode("Group 1.2");
		moduleGroup.setPresentWithSingleModule(false);
		subApplication.addChild(moduleGroup);
		module = new ModuleNode("Module 1.2.1 (not closeable)");
		module.setCloseable(false);
		module.setIcon(createIconPath(IExampleIcons.ICON_RED_LED));
		moduleGroup.addChild(module);
		subModule = new SubModuleNode("SubModule 1.2.1.1");
		presentation.present(subModule, "customerDetailView");
		module.addChild(subModule);
		subModule = new SubModuleNode("SubModule 1.2.1.2");
		presentation.present(subModule, "customerDetailView");
		module.addChild(subModule);

		// Playground
		subApplication = new SubApplication("Playground");
		subApplication.setIcon(createIconPath(IExampleIcons.ICON_SAMPLE));
		presentation.present(subApplication, "subapplication.2");
		applicationModel.addChild(subApplication);

		moduleGroup = new ModuleGroupNode("Group 2.2");
		subApplication.addChild(moduleGroup);

		// shared view demo
		presentation.registerView(SharedViewDemoView.ID, true);
		IModuleNode sharedViewModule = new ModuleNode("Shared View Demo");
		module.setIcon(createIconPath(IExampleIcons.ICON_SAMPLE));
		moduleGroup.addChild(sharedViewModule);

		ISubModuleNode sharedViewSm1 = new SubModuleNode("Node 1");
		presentation.present(sharedViewSm1, SharedViewDemoView.ID);
		sharedViewModule.addChild(sharedViewSm1);

		ISubModuleNode sharedViewSm2 = new SubModuleNode("Node 2");
		presentation.present(sharedViewSm2, SharedViewDemoView.ID);
		sharedViewModule.addChild(sharedViewSm2);

		// ui process demo
		IModuleNode uiProcessModule = new ModuleNode("UI-Process");
		moduleGroup.addChild(uiProcessModule);

		ISubModuleNode uiPSubModule = new SubModuleNode("UI-Process");
		presentation.registerView(UiProcessDemoView.ID, false);
		presentation.present(uiPSubModule, UiProcessDemoView.ID);
		uiProcessModule.addChild(uiPSubModule);

		// playground
		IModuleNode playgroundModule = new ModuleNode("Playground");
		moduleGroup.addChild(playgroundModule);

		ISubModuleNode buttonsSubModule = new SubModuleNode("Buttons");
		presentation.registerView(RidgetsSubModuleView.ID, false);
		presentation.present(buttonsSubModule, RidgetsSubModuleView.ID);
		playgroundModule.addChild(buttonsSubModule);

		ISubModuleNode comboSubModule = new SubModuleNode("Combo");
		presentation.registerView(ComboView.ID, false);
		presentation.present(comboSubModule, ComboView.ID);
		playgroundModule.addChild(comboSubModule);

		ISubModuleNode listSubModule = new SubModuleNode("List");
		presentation.registerView(ListView.ID, false);
		presentation.present(listSubModule, ListView.ID);
		playgroundModule.addChild(listSubModule);

		ISubModuleNode textSubModule = new SubModuleNode("Text");
		presentation.registerView(TextView.ID, false);
		presentation.present(textSubModule, TextView.ID);
		playgroundModule.addChild(textSubModule);

		ISubModuleNode markerSubModule = new SubModuleNode("Marker");
		presentation.registerView(MarkerView.ID, false);
		presentation.present(markerSubModule, MarkerView.ID);
		playgroundModule.addChild(markerSubModule);

		ISubModuleNode focusableSubModule = new SubModuleNode("Focusable");
		presentation.registerView(FocusableView.ID, false);
		presentation.present(focusableSubModule, FocusableView.ID);
		playgroundModule.addChild(focusableSubModule);

		ISubModuleNode validationSubModule = new SubModuleNode("Validation");
		presentation.registerView(ValidationView.ID, false);
		presentation.present(validationSubModule, ValidationView.ID);
		playgroundModule.addChild(validationSubModule);

		ISubModuleNode systemPropertiesSubModule = new SubModuleNode("System Properties");
		presentation.registerView(SystemPropertiesView.ID, false);
		presentation.present(systemPropertiesSubModule, SystemPropertiesView.ID);
		playgroundModule.addChild(systemPropertiesSubModule);

		return applicationModel;
	}

	@Override
	protected Bundle getBundle() {
		return Activator.getDefault().getBundle();
	}

}
