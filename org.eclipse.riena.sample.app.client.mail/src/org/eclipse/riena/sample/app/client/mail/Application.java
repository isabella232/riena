package org.eclipse.riena.sample.app.client.mail;

import org.eclipse.riena.navigation.IApplicationModel;
import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.model.ApplicationModel;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.SubApplicationNode;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.swt.application.SwtApplication;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManager;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManagerAccessor;
import org.osgi.framework.Bundle;

/**
 * This class controls all aspects of the application's execution
 */
public class Application extends SwtApplication {

	@Override
	protected IApplicationModel createModel() {
		SwtPresentationManager presentation = SwtPresentationManagerAccessor.getManager();

		ApplicationModel app = new ApplicationModel(null, "Riena Mail");

		ISubApplicationNode subApp = new SubApplicationNode(null, "Your Mail");
		app.addChild(subApp);
		presentation.present(subApp, "rcp.mail.perspective");

		IModuleGroupNode groupMailboxes = new ModuleGroupNode(null);
		subApp.addChild(groupMailboxes);

		IModuleNode moduleAccount1 = createModule("me@this.com", groupMailboxes);
		presentation.registerView(View.ID, false);
		createSubMobule("Inbox", moduleAccount1, View.ID);
		createSubMobule("Drafts", moduleAccount1, View.ID);
		createSubMobule("Sent", moduleAccount1, View.ID);

		IModuleNode moduleAccount2 = createModule("other@aol.com", groupMailboxes);
		createSubMobule("Inbox", moduleAccount2, View.ID);

		return app;
	}

	private IModuleNode createModule(String caption, IModuleGroupNode parent) {
		IModuleNode module = new ModuleNode(null, caption);
		parent.addChild(module);
		return module;
	}

	private ISubModuleNode createSubMobule(String caption, IModuleNode parent, String viewId) {
		ISubModuleNode subModule = new SubModuleNode(null, caption);
		parent.addChild(subModule);
		SwtPresentationManagerAccessor.getManager().present(subModule, viewId);
		return subModule;
	}

	@Override
	protected Bundle getBundle() {
		return Activator.getDefault().getBundle();
	}
}
