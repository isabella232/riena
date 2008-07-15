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

	public SubModuleNodeViewController(ISubModuleNode navigationNode) {
		super(navigationNode);

		if (isActivated()) {
			registerDispatcherBuilder();
		}

	}

	public SubModuleNodeViewController() {
		this(null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.riena.navigation.ui.controllers.NavigationNodeViewController
	 * #setNavigationNode(org.eclipse.riena.navigation.INavigationNode)
	 */
	@Override
	public void setNavigationNode(ISubModuleNode navigationNode) {
		super.setNavigationNode(navigationNode);

		getNavigationNode().addListener(new SubModuleNodeAdapter() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.riena.navigation.model.NavigationNodeAdapter#labelChanged
			 * (org.eclipse.riena.navigation.INavigationNode)
			 */
			@Override
			public void labelChanged(ISubModuleNode subModuleNode) {
				updateLabel();
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.riena.navigation.model.NavigationNodeAdapter#iconChanged
			 * (org.eclipse.riena.navigation.INavigationNode)
			 */
			@Override
			public void iconChanged(ISubModuleNode source) {
				updateIcon();
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.riena.navigation.model.NavigationNodeAdapter#activated
			 * (org.eclipse.riena.navigation.INavigationNode)
			 */
			@Override
			public void activated(ISubModuleNode source) {
				super.activated(source);
				registerDispatcherBuilder();
			}
		});
	}

	public UICallbackDispatcher createCallbackDispatcher() {
		// UICallbackDispatcher dispatcher = new UICallbackDispatcher(n);
		// dispatcher.addUIMonitor(getProgressVisualizer());
		// return dispatcher;
		return null;
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
	 * @see
	 * org.eclipse.riena.navigation.ui.controllers.NavigationNodeViewController
	 * #afterBind()
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

	@Override
	public IProgressVisualizer getProgressVisualizer() {
		INavigationNode<?> aNode = getSubApplication(getNavigationNode());
		if (aNode != null) {
			IProgressVisualizer aVisualizer = new ProgressVisualizer();
			aVisualizer.addObserver(((SubApplicationViewController) aNode.getPresentation()).getProgressBoxRidget());
			for (INavigationNode<?> aSubApplicationNode : getSubApplications()) {
				if (aSubApplicationNode.getPresentation() != null
						&& ((SubApplicationViewController) aSubApplicationNode.getPresentation()).getStatusbarRidget() != null) {
					aVisualizer.addObserver(((SubApplicationViewController) aSubApplicationNode.getPresentation())
							.getStatusbarRidget().getStatusBarProcessRidget());
				}
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
