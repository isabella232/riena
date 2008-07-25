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
package org.eclipse.riena.example.client.application;

import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.example.client.views.ComboView;
import org.eclipse.riena.example.client.views.FocusableView;
import org.eclipse.riena.example.client.views.ListView;
import org.eclipse.riena.example.client.views.MarkerView;
import org.eclipse.riena.example.client.views.NavigationSubModuleView;
import org.eclipse.riena.example.client.views.RidgetsSubModuleView;
import org.eclipse.riena.example.client.views.SharedViewDemoView;
import org.eclipse.riena.example.client.views.StatuslineSubModuleView;
import org.eclipse.riena.example.client.views.SystemPropertiesView;
import org.eclipse.riena.example.client.views.TableView;
import org.eclipse.riena.example.client.views.TextView;
import org.eclipse.riena.example.client.views.TreeTableView;
import org.eclipse.riena.example.client.views.TreeView;
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
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManager;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManagerAccessor;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;
import org.osgi.framework.Bundle;

/**
 * Define the model of the application
 */
public class SwtExampleApplication extends SwtApplication {

	/**
	 * Creates a new instance of <code>SwtExampleApplication</code> and set the
	 * look and feel, if a class for the look and feel is given.
	 */
	@SuppressWarnings("unchecked")
	public SwtExampleApplication() {

		super();

		String lnfClassName = System.getProperty("riena.lnf", ""); //$NON-NLS-1$ //$NON-NLS-2$
		if (!StringUtils.isEmpty(lnfClassName)) {
			try {
				Class lnfClass = this.getBundle().loadClass(lnfClassName);
				RienaDefaultLnf lnf;
				lnf = (RienaDefaultLnf) lnfClass.newInstance();
				LnfManager.setLnf(lnf);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

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

		final IApplicationModel applicationModel = new ApplicationModel("Riena Navigation Example"); //$NON-NLS-1$
		applicationModel.setIcon(createIconPath(IExampleIcons.ICON_APPLICATION));
		// sub application 1
		subApplication = new SubApplication("Navigation"); //$NON-NLS-1$
		subApplication.setIcon(createIconPath(IExampleIcons.ICON_APPLICATION));
		presentation.present(subApplication, "subapplication.1"); //$NON-NLS-1$
		applicationModel.addChild(subApplication);
		subApplication.setSelected(true);

		moduleGroup = new ModuleGroupNode("Group 1.1"); //$NON-NLS-1$
		subApplication.addChild(moduleGroup);
		module = new ModuleNode("Module 1.1.1"); //$NON-NLS-1$
		module.setIcon(createIconPath(IExampleIcons.ICON_APPLICATION));
		moduleGroup.addChild(module);
		subModule = new SubModuleNode("SubModule 1.1.1.1"); //$NON-NLS-1$
		subModule.setIcon(createIconPath(IExampleIcons.ICON_FILE));
		presentation.present(subModule, "customerDetailView"); //$NON-NLS-1$
		module.addChild(subModule);

		SubModuleNode subModule2 = new SubModuleNode("SubModule 1.1.1.1"); //$NON-NLS-1$
		presentation.present(subModule2, "customerDetailView"); //$NON-NLS-1$
		subModule.addChild(subModule2);

		subModule = new SubModuleNode("SubModule 1.1.1.2"); //$NON-NLS-1$
		presentation.present(subModule, "customerDetailView"); //$NON-NLS-1$
		module.addChild(subModule);
		module = new ModuleNode("Module 1.1.2 (closeable)"); //$NON-NLS-1$
		module.setIcon(createIconPath(IExampleIcons.ICON_HOMEFOLDER));
		moduleGroup.addChild(module);
		subModule = new SubModuleNode("SubModule 1.1.2.1"); //$NON-NLS-1$
		presentation.present(subModule, "customerDetailView"); //$NON-NLS-1$
		module.addChild(subModule);
		/* NEW */
		subModule = new SubModuleNode("Navigation"); //$NON-NLS-1$
		presentation.registerView(NavigationSubModuleView.ID, false);
		presentation.present(subModule, NavigationSubModuleView.ID);
		module.addChild(subModule);

		moduleGroup = new ModuleGroupNode("Group 1.2"); //$NON-NLS-1$
		moduleGroup.setPresentWithSingleModule(false);
		subApplication.addChild(moduleGroup);
		module = new ModuleNode("Module 1.2.1 (not closeable)"); //$NON-NLS-1$
		module.setCloseable(false);
		module.setIcon(createIconPath(IExampleIcons.ICON_RED_LED));
		moduleGroup.addChild(module);
		subModule = new SubModuleNode("SubModule 1.2.1.1"); //$NON-NLS-1$
		presentation.present(subModule, "customerDetailView"); //$NON-NLS-1$
		module.addChild(subModule);
		subModule = new SubModuleNode("SubModule 1.2.1.2"); //$NON-NLS-1$
		presentation.present(subModule, "customerDetailView"); //$NON-NLS-1$
		module.addChild(subModule);

		// Playground
		subApplication = new SubApplication("Playground"); //$NON-NLS-1$
		subApplication.setIcon(createIconPath(IExampleIcons.ICON_SAMPLE));
		presentation.present(subApplication, "subapplication.2"); //$NON-NLS-1$
		applicationModel.addChild(subApplication);

		moduleGroup = new ModuleGroupNode("Group 2.2"); //$NON-NLS-1$
		subApplication.addChild(moduleGroup);

		// shared view demo
		presentation.registerView(SharedViewDemoView.ID, true);
		IModuleNode sharedViewModule = new ModuleNode("Shared View Demo"); //$NON-NLS-1$
		module.setIcon(createIconPath(IExampleIcons.ICON_SAMPLE));
		moduleGroup.addChild(sharedViewModule);

		ISubModuleNode sharedViewSm1 = new SubModuleNode("Node 1"); //$NON-NLS-1$
		presentation.present(sharedViewSm1, SharedViewDemoView.ID);
		sharedViewModule.addChild(sharedViewSm1);

		ISubModuleNode sharedViewSm2 = new SubModuleNode("Node 2"); //$NON-NLS-1$
		presentation.present(sharedViewSm2, SharedViewDemoView.ID);
		sharedViewModule.addChild(sharedViewSm2);

		// uiProcess demo
		IModuleNode uiProcessModule = new ModuleNode("UIProcess"); //$NON-NLS-1$
		moduleGroup.addChild(uiProcessModule);

		ISubModuleNode uiPSubModule = new SubModuleNode("Demo1"); //$NON-NLS-1$
		presentation.registerView(UiProcessDemoView.ID, false);
		presentation.present(uiPSubModule, UiProcessDemoView.ID);
		uiProcessModule.addChild(uiPSubModule);

		uiPSubModule = new SubModuleNode("Demo2"); //$NON-NLS-1$
		presentation.registerView(UiProcessDemoView.ID, false);
		presentation.present(uiPSubModule, UiProcessDemoView.ID);
		uiProcessModule.addChild(uiPSubModule);

		// playground
		IModuleNode playgroundModule = new ModuleNode("Playground"); //$NON-NLS-1$
		moduleGroup.addChild(playgroundModule);

		ISubModuleNode buttonsSubModule = new SubModuleNode("Buttons"); //$NON-NLS-1$
		presentation.registerView(RidgetsSubModuleView.ID, false);
		presentation.present(buttonsSubModule, RidgetsSubModuleView.ID);
		playgroundModule.addChild(buttonsSubModule);

		ISubModuleNode comboSubModule = new SubModuleNode("Combo"); //$NON-NLS-1$
		presentation.registerView(ComboView.ID, false);
		presentation.present(comboSubModule, ComboView.ID);
		playgroundModule.addChild(comboSubModule);

		ISubModuleNode listSubModule = new SubModuleNode("List"); //$NON-NLS-1$
		presentation.registerView(ListView.ID, false);
		presentation.present(listSubModule, ListView.ID);
		playgroundModule.addChild(listSubModule);

		ISubModuleNode textSubModule = new SubModuleNode("Text"); //$NON-NLS-1$
		presentation.registerView(TextView.ID, false);
		presentation.present(textSubModule, TextView.ID);
		playgroundModule.addChild(textSubModule);

		ISubModuleNode markerSubModule = new SubModuleNode("Marker"); //$NON-NLS-1$
		presentation.registerView(MarkerView.ID, false);
		presentation.present(markerSubModule, MarkerView.ID);
		playgroundModule.addChild(markerSubModule);

		ISubModuleNode focusableSubModule = new SubModuleNode("Focusable"); //$NON-NLS-1$
		presentation.registerView(FocusableView.ID, false);
		presentation.present(focusableSubModule, FocusableView.ID);
		playgroundModule.addChild(focusableSubModule);

		ISubModuleNode validationSubModule = new SubModuleNode("Validation"); //$NON-NLS-1$
		presentation.registerView(ValidationView.ID, false);
		presentation.present(validationSubModule, ValidationView.ID);
		playgroundModule.addChild(validationSubModule);

		ISubModuleNode treeSubModule = new SubModuleNode("Tree"); //$NON-NLS-1$
		presentation.registerView(TreeView.ID, false);
		presentation.present(treeSubModule, TreeView.ID);
		playgroundModule.addChild(treeSubModule);

		ISubModuleNode treeTableSubModule = new SubModuleNode("Tree Table"); //$NON-NLS-1$
		presentation.registerView(TreeTableView.ID, false);
		presentation.present(treeTableSubModule, TreeTableView.ID);
		playgroundModule.addChild(treeTableSubModule);

		ISubModuleNode tableSubModule = new SubModuleNode("Table");
		presentation.registerView(TableView.ID, false);
		presentation.present(tableSubModule, TableView.ID);
		playgroundModule.addChild(tableSubModule);

		ISubModuleNode systemPropertiesSubModule = new SubModuleNode("System Properties"); //$NON-NLS-1$
		presentation.registerView(SystemPropertiesView.ID, false);
		presentation.present(systemPropertiesSubModule, SystemPropertiesView.ID);
		playgroundModule.addChild(systemPropertiesSubModule);

		ISubModuleNode statusLineSubModule = new SubModuleNode("Statusline"); //$NON-NLS-1$
		presentation.registerView(StatuslineSubModuleView.ID, false);
		presentation.present(statusLineSubModule, StatuslineSubModuleView.ID);
		playgroundModule.addChild(statusLineSubModule);

		return applicationModel;
	}

	@Override
	protected Bundle getBundle() {
		return Activator.getDefault().getBundle();
	}

}
