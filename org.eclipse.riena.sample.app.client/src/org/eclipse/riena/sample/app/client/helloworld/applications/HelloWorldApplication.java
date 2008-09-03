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
package org.eclipse.riena.sample.app.client.helloworld.applications;

import org.eclipse.riena.internal.sample.app.client.Activator;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.model.ApplicationNode;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubApplicationNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.swt.application.SwtApplication;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtViewProvider;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtViewProviderAccessor;
import org.eclipse.riena.sample.app.client.helloworld.views.CustomerSearchSubModuleView;
import org.eclipse.riena.sample.app.client.helloworld.views.HelloServerSubModuleView;
import org.eclipse.riena.sample.app.client.helloworld.views.HelloWorldSubModuleView;

import org.osgi.framework.Bundle;

/**
 * Very simple application with only one sub application, one module group, one
 * module and one sub module.
 * 
 * @author schenkel
 */
public class HelloWorldApplication extends SwtApplication {

	private IApplicationNode application;

	/**
	 * Creates the model of the application "Hello world".
	 * 
	 * @see org.eclipse.riena.navigation.ui.application.AbstractApplication#createModel()
	 */
	@Override
	protected IApplicationNode createModel() {

		SwtViewProvider presentation = SwtViewProviderAccessor.getViewProvider();

		application = new ApplicationNode("Hello World Application");
		ISubApplicationNode subApplication = new SubApplicationNode("Riena Samples");
		presentation.present(subApplication, "helloWorldSubApplication");
		application.addChild(subApplication);

		IModuleGroupNode moduleGroup = new ModuleGroupNode();
		subApplication.addChild(moduleGroup);

		// simple hello world
		IModuleNode helloWorldModule = new ModuleNode("Hello World");
		moduleGroup.addChild(helloWorldModule);

		ISubModuleNode helloWorldSubModule = new SubModuleNode("Hello World");
		presentation.registerView(HelloWorldSubModuleView.ID, false);
		presentation.present(helloWorldSubModule, HelloWorldSubModuleView.ID);
		helloWorldModule.addChild(helloWorldSubModule);

		// hello server
		presentation.registerView(HelloServerSubModuleView.ID, false);
		IModuleNode helloServerModule = new ModuleNode("Hello Server");
		moduleGroup.addChild(helloServerModule);

		ISubModuleNode helloServerSubModule = new SubModuleNode("Hello Server");
		presentation.present(helloServerSubModule, HelloServerSubModuleView.ID);
		helloServerModule.addChild(helloServerSubModule);

		// customer search sample
		presentation.registerView(CustomerSearchSubModuleView.ID, false);
		IModuleNode cSearchModule = new ModuleNode("Customer Search");
		moduleGroup.addChild(cSearchModule);

		ISubModuleNode cSearchSubModule = new SubModuleNode("Customer Search");
		presentation.present(cSearchSubModule, CustomerSearchSubModuleView.ID);
		cSearchModule.addChild(cSearchSubModule);

		ISubApplicationNode subApplication2 = new SubApplicationNode("Simple Subapplication");
		presentation.present(subApplication2, "org.eclipse.riena.sample.app.client.second");
		application.addChild(subApplication2);
		moduleGroup = new ModuleGroupNode();
		subApplication2.addChild(moduleGroup);
		IModuleNode module = new ModuleNode("Module 1");
		moduleGroup.addChild(module);
		helloWorldSubModule = new SubModuleNode("Hello World");
		presentation.registerView(HelloWorldSubModuleView.ID, false);
		presentation.present(helloWorldSubModule, HelloWorldSubModuleView.ID);
		module.addChild(helloWorldSubModule);

		return application;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.navigation.ui.swt.application.SwtApplication#getBundle
	 * ()
	 */
	@Override
	protected Bundle getBundle() {
		return Activator.getDefault().getBundle();
	}
}
