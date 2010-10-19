package org.eclipse.riena.example.client.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import org.eclipse.riena.navigation.ApplicationNodeManager;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;

public class OpenPlaygroundHandler extends AbstractHandler {

	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final ISubModuleNode activeSubModuleNode = ApplicationNodeManager.locateActiveSubModuleNode();
		ApplicationNodeManager.getDefaultNavigationProcessor().navigate(activeSubModuleNode,
				new NavigationNodeId("playgroundModule"), null); //$NON-NLS-1$
		return null;
	}
}
