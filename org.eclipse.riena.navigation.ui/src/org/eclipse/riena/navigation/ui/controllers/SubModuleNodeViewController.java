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
package org.eclipse.riena.navigation.ui.controllers;

import java.util.Collection;

import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubApplication;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.model.SubModuleNodeAdapter;
import org.eclipse.riena.ui.core.uiprocess.IProgressVisualizer;
import org.eclipse.riena.ui.core.uiprocess.IUICallbackDispatcherFactory;
import org.eclipse.riena.ui.core.uiprocess.IUIMonitorContainer;
import org.eclipse.riena.ui.core.uiprocess.ProgressVisualizer;
import org.eclipse.riena.ui.core.uiprocess.UICallbackDispatcher;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IWindowRidget;

/**
 * Default implementation for a SubModuleNodeViewController
 */
/**
 * 
 */
public class SubModuleNodeViewController extends NavigationNodeViewController<ISubModuleNode> {

	private static final String TITLE_SEPARATOR = " - "; //$NON-NLS-1$

	private IWindowRidget windowRidget;

	private IUICallbackDispatcherFactory providerFactory;

	public SubModuleNodeViewController(ISubModuleNode navigationNode) {
		super(navigationNode);
		getNavigationNode().addListener(new SubModuleNodeAdapter() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.riena.navigation.model.NavigationNodeAdapter#labelChanged(org.eclipse.riena.navigation.INavigationNode)
			 */
			@Override
			public void labelChanged(ISubModuleNode subModuleNode) {
				updateLabel();
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.riena.navigation.model.NavigationNodeAdapter#iconChanged(org.eclipse.riena.navigation.INavigationNode)
			 */
			@Override
			public void iconChanged(ISubModuleNode source) {
				updateIcon();
			}
		});
	}

	protected IUIMonitorContainer createProgressProvider(IProgressVisualizer visualizer) {

		// UICallbackDispatcher provider =
		// providerFactory.createCallbackDispatcher();
		UICallbackDispatcher provider = createCallbackDispatcher();
		provider.addUIMonitor(visualizer);
		return provider;
	}

	/**
	 * @param windowRidget
	 *            the windowRidget to set
	 */
	public void setWindowRidget(IWindowRidget windowRidget) {
		this.windowRidget = windowRidget;
	}

	/**
	 * @return the windowRidget
	 */
	public IWindowRidget getWindowRidget() {
		return windowRidget;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.riena.navigation.ui.controllers.NavigationNodeViewController#afterBind()
	 */
	@Override
	public void afterBind() {
		super.afterBind();

		updateLabel();
		updateIcon();
	}

	/**
	 * @param actionRidget
	 */
	public void setDefaultButton(IActionRidget actionRidget) {
		if (windowRidget != null) {
			windowRidget.setDefaultButton(actionRidget.getUIControl());
		}
	}

	/**
	 * @return
	 */
	public Object getDefaultButton() {
		if (windowRidget != null) {
			return windowRidget.getDefaultButton();
		}
		return null;
	}

	private void updateLabel() {
		if (windowRidget != null) {
			windowRidget.setTitle(getFullTitle());
		}
	}

	protected String getFullTitle() {
		String title = getNavigationNode().getLabel();
		if (!((ModuleNodeViewController) getNavigationNode().getParentOfType(IModuleNode.class).getPresentation())
				.hasSingleLeafChild()) {
			INavigationNode<?> parent = getNavigationNode().getParent();
			while (!(parent instanceof IModuleNode)) {
				title = parent.getLabel() + TITLE_SEPARATOR + title;
				parent = parent.getParent();
			}
			title = parent.getLabel() + TITLE_SEPARATOR + title;
		}
		return title;
	}

	private void updateIcon() {
		updateIcon(windowRidget);
	}

	public UICallbackDispatcher createCallbackDispatcher() {
		UICallbackDispatcher dispatcher = getCallbackDispatcherfactory().createCallbackDispatcher();
		dispatcher.addUIMonitor(getProgressVisualizer());
		return dispatcher;
	}

	public IProgressVisualizer getProgressVisualizer() {
		INavigationNode<?> aNode = getSubApplication(getNavigationNode());
		if (aNode != null) {
			IProgressVisualizer aVisualizer = new ProgressVisualizer();
			aVisualizer.addObserver(((SubApplicationViewController) aNode.getPresentation()).getProgressBoxRidget());
			for (INavigationNode<?> aSubApplicationNode : getSubApplications()) {
				aVisualizer.addObserver(((SubApplicationViewController) aSubApplicationNode.getPresentation())
						.getStatusbarRidget().getStatusBarProcessRidget());
			}
			return aVisualizer;
		}
		return null;
	}

	private Collection<INavigationNode> getSubApplications() {
		INavigationNode topNode = getParentNavigationNode(getNavigationNode());
		return topNode.getChildren();
	}

	private INavigationNode getParentNavigationNode(INavigationNode aNode) {
		if (aNode.getParent() == null) {
			return aNode;
		} else {
			return getParentNavigationNode(aNode.getParent());
		}
	}

	private INavigationNode getSubApplication(INavigationNode aNode) {
		if (aNode instanceof ISubApplication || aNode == null) {
			return aNode;
		} else {
			return getSubApplication(aNode.getParent());
		}
	}
}
