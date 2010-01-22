/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation.ui.controllers;

import java.util.Iterator;

import org.osgi.service.log.LogService;

import org.eclipse.core.databinding.BindingException;
import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.RienaStatus;
import org.eclipse.riena.internal.navigation.ui.Activator;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.listener.SubModuleNodeListener;
import org.eclipse.riena.navigation.model.SimpleNavigationNodeAdapter;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IWindowRidget;

/**
 * Default implementation for a SubModuleController.
 */
public class SubModuleController extends NavigationNodeController<ISubModuleNode> {

	/**
	 * The ID of the window ridget in this controller ("windowRidget").
	 */
	public static final String WINDOW_RIDGET = "windowRidget"; //$NON-NLS-1$

	private static final String TITLE_SEPARATOR = " - "; //$NON-NLS-1$

	private static final Logger LOGGER = Log4r.getLogger(Activator.getDefault(), SubModuleController.class);

	public SubModuleController() {
		this(null);
	}

	public SubModuleController(ISubModuleNode navigationNode) {
		super(navigationNode);
	}

	@Override
	public void afterBind() {
		super.afterBind();

		updateLabel();
		updateIcon();
		updateCloseable();
		updateActive();
	}

	/**
	 * Subclasses should override to configure their ridget.
	 * <p>
	 * {@inheritDoc}
	 */
	public void configureRidgets() {
		// unused
	}

	// TODO [ev] remove
	//	public Object getDefaultButton() {
	//		IWindowRidget windowRidget = getWindowRidget();
	//		if (windowRidget != null) {
	//			return windowRidget.getDefaultButton();
	//		}
	//		return null;
	//	}

	/**
	 * Returns the controller of the parent module.
	 * 
	 * @return module controller or {@code null} if not parent module controller
	 *         exits.
	 */
	public ModuleController getModuleController() {
		IModuleNode moduleNode = getNavigationNode().getParentOfType(IModuleNode.class);
		if (moduleNode != null) {
			return (ModuleController) moduleNode.getNavigationNodeController();
		}
		return null;
	}

	/**
	 * @return the windowRidget
	 */
	public IWindowRidget getWindowRidget() {
		return (IWindowRidget) getRidget(WINDOW_RIDGET);
	}

	// TODO [ev] remove
	//	public void setDefaultButton(IActionRidget actionRidget) {
	//		IWindowRidget windowRidget = getWindowRidget();
	//		if (windowRidget != null) {
	//			windowRidget.setDefaultButton(actionRidget.getUIControl());
	//		}
	//	}

	@Override
	public void setNavigationNode(ISubModuleNode navigationNode) {
		super.setNavigationNode(navigationNode);

		if (getModuleController() != null) {
			getNavigationNode().getParent().addSimpleListener(new SimpleNavigationNodeAdapter() {
				@Override
				public void labelChanged(INavigationNode<?> parent) {
					super.labelChanged(parent);
					updateLabel();
				}

			});
		}
		getNavigationNode().addListener(new SubModuleNodeListener() {
			@Override
			public void iconChanged(ISubModuleNode source) {
				updateIcon();
			}

			@Override
			public void labelChanged(ISubModuleNode subModuleNode) {
				updateLabel();
			}
		});
	}

	/**
	 * @param windowRidget
	 *            the windowRidget to set
	 */
	public void setWindowRidget(IWindowRidget windowRidget) {
		if (getRidget(WINDOW_RIDGET) != windowRidget) {
			addRidget(WINDOW_RIDGET, windowRidget);
		}
	}

	/**
	 * calls updateFromModel for all registered ridgets in this controller
	 */
	public void updateAllRidgetsFromModel() {
		Iterator<? extends IRidget> iter = getRidgets().iterator();
		while (iter.hasNext()) {
			IRidget ridget = iter.next();
			if (RienaStatus.isDevelopment()) {
				try {
					ridget.updateFromModel();
				} catch (BindingException ex) {
					LOGGER
							.log(
									LogService.LOG_WARNING,
									"Update from the model was unsuccessful for the ridget: " + ridget + ", with id: " + ridget.getID()); //$NON-NLS-1$ //$NON-NLS-2$
				}
			} else {
				ridget.updateFromModel();
			}
		}
	}

	/**
	 * Returns the full title of this sub-module. The title exits of the label
	 * of parent module node and all parent sub-module nodes. One exception
	 * exits: If the module has one one sub-module (and this is not visible in
	 * the tree), the label of the module is returned.
	 * 
	 * @return full title of the sub-module
	 */
	protected String getFullTitle() {
		String title = getNavigationNode().getLabel();

		if (isInvisibleInTree()) {
			IModuleNode moduleNode = getModuleController().getNavigationNode();
			title = moduleNode.getLabel();
		} else {
			INavigationNode<?> parent = getNavigationNode().getParent();
			if (parent != null) {
				while (parent != null && !(parent instanceof IModuleNode)) {
					title = parent.getLabel() + TITLE_SEPARATOR + title;
					parent = parent.getParent();
				}
				title = parent.getLabel() + TITLE_SEPARATOR + title;
			}
		}
		return title;
	}

	/**
	 * Re-layout all contents.
	 * 
	 * @since 1.2
	 */
	protected void layout() {
		IWindowRidget ridget = (IWindowRidget) getRidget(WINDOW_RIDGET);
		ridget.layout();
	}

	@Override
	protected void updateIcon(IWindowRidget windowRidget) {
		if (isInvisibleInTree()) {
			if (windowRidget == null) {
				return;
			}
			IModuleNode moduleNode = getModuleController().getNavigationNode();
			String nodeIcon = moduleNode.getIcon();
			windowRidget.setIcon(nodeIcon);
		} else {
			super.updateIcon(windowRidget);
		}
	}

	/**
	 * Returns whether the sub-module is hidden in the navigation tree. A
	 * sub-module is hidden if it is the only child of a module unless its
	 * parent is configured to show a single child.
	 * 
	 * @return {@code true} if there is a navigation tree but the sub-module is
	 *         not shown; otherwise {@code false}
	 */
	private boolean isInvisibleInTree() {
		if (getModuleController() != null && getModuleController().hasSingleLeafChild()) {
			return !getModuleController().getNavigationNode().isPresentSingleSubModule();
		}
		return false;
	}

	private void updateActive() {
		IWindowRidget windowRidget = getWindowRidget();
		if (windowRidget != null) {
			windowRidget.setActive(getNavigationNode().isActivated());
		}
	}

	private void updateCloseable() {
		IWindowRidget windowRidget = getWindowRidget();
		if (windowRidget != null) {
			windowRidget.setCloseable(false);
		}
	}

	private void updateIcon() {
		updateIcon(getWindowRidget());
	}

	private void updateLabel() {
		IWindowRidget windowRidget = getWindowRidget();
		if (windowRidget != null) {
			windowRidget.setTitle(getFullTitle());
		}
	}
}
