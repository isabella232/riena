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

import org.eclipse.riena.internal.navigation.ui.marker.UIProcessFinsishedObserver;
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
	// observer of the uiProcess for marker support
	private UIProcessFinsishedObserver uiProcesFinishedObserver;

	public SubModuleNodeViewController(ISubModuleNode navigationNode) {
		super(navigationNode);

		if (isActivated()) {
			registerDispatcherBuilder();
		}

		getNavigationNode().addListener(new SubModuleNodeAdapter() {

			@Override
			public void labelChanged(ISubModuleNode subModuleNode) {
				updateLabel();
			}

			@Override
			public void iconChanged(ISubModuleNode source) {
				updateIcon();
			}

			@Override
			public void activated(ISubModuleNode source) {
				super.activated(source);
				registerDispatcherBuilder();
			}

		});

		createUIProcessObserver();
	}

	private void createUIProcessObserver() {
		uiProcesFinishedObserver = new UIProcessFinsishedObserver(getNavigationNode());
	}

	public UICallbackDispatcher createCallbackDispatcher() {
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
			// observe the uiProcess
			aVisualizer.addObserver(uiProcesFinishedObserver);
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
