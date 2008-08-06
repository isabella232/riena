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
package org.eclipse.riena.navigation.ui.swt.views;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.riena.navigation.IApplicationModel;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.IPresentationProviderService;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.listener.NavigationTreeObserver;
import org.eclipse.riena.navigation.listener.SubModuleNodeListener;
import org.eclipse.riena.navigation.model.PresentationProviderServiceAccessor;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManagerAccessor;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtViewId;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.part.ViewPart;

/**
 * Abstract implementation for a sub module view
 */
public abstract class SubModuleView<C extends SubModuleController> extends ViewPart {

	private Map<ISubModuleNode, C> node2Controler;
	private SWTViewBindingDelegate binding;
	private C currentController;

	public SubModuleView() {
		binding = createBinding();
		node2Controler = new HashMap<ISubModuleNode, C>();
	}

	/**
	 * Creates a delegate for the binding of view and controller.
	 * 
	 * @return delegate for binding
	 */
	protected SWTViewBindingDelegate createBinding() {
		return new SWTViewBindingDelegate();
	}

	/**
	 * Adds the given control to the list of the controls that will be binded.
	 * 
	 * @param uiControl
	 *            - control to bind
	 */
	protected void addUIControl(Widget uiControl) {
		binding.addUIControl(uiControl);
	}

	/**
	 * Adds the given control to the list of the controls that will be binded.
	 * 
	 * @param uiControl
	 *            - control to bind
	 * @param propertyName
	 *            - name of the property...
	 */
	protected void addUIControl(Widget uiControl, String propertyName) {
		binding.addUIControl(uiControl, propertyName);
	}

	/**
	 * Find the navigation node corresponding to the passed id
	 * 
	 * @param pId
	 *            - the id to the node
	 * @return the subModule node if found
	 */
	protected ISubModuleNode getSubModuleNode(String pId, String pSecondary) {
		return SwtPresentationManagerAccessor.getManager().getNavigationNode(pId, pSecondary, ISubModuleNode.class);
	}

	/**
	 * @return the controller
	 */
	public C getController() {
		return node2Controler.get(getCurrentNode());
	}

	/**
	 * @param controller
	 *            the controller to set
	 */
	public void setController(C controller) {
		if (node2Controler.get(getCurrentNode()) == null) {
			node2Controler.put(getCurrentNode(), controller);

		}
	}

	protected ISubModuleNode getCurrentNode() {
		return getSubModuleNode(this.getViewSite().getId(), this.getViewSite().getSecondaryId());
	}

	/**
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		observeRoot();
		setController(createController(getCurrentNode()));
		setPartName(getController().getNavigationNode().getLabel());
		basicCreatePartControl(parent);
		createViewFacade();
		activate();
		observeRoot();
	}

	private void observeRoot() {
		INavigationNode<?> node = getCurrentNode();
		while (node.getParent() != null) {
			node = node.getParent();
		}
		NavigationTreeObserver navigationTreeObserver = new NavigationTreeObserver();
		navigationTreeObserver.addListener(new MySubModuleNodeListener());
		navigationTreeObserver.addListenerTo(node.getTypecastedAdapter(IApplicationModel.class));
	}

	private final class MySubModuleNodeListener extends SubModuleNodeListener {
		@Override
		public void activated(ISubModuleNode source) {
			SwtViewId id = SwtPresentationManagerAccessor.getManager().getSwtViewId(source);
			if (getViewSite().getId().equals(id.getId())) {
				activate();
			}
		}
	}

	protected abstract void basicCreatePartControl(Composite parent);

	protected C createController(ISubModuleNode pSubModuleNode) {
		C controller = (C) getPresentationDefinitionService().createViewController(pSubModuleNode);
		controller.setNavigationNode(pSubModuleNode);
		return controller;
	}

	/**
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		activate();
	}

	protected void activate() {
		if (currentController != getController()) {
			if (currentController != null) {
				binding.unbind(currentController);
			}
			if (node2Controler.get(getCurrentNode()) == null && getCurrentNode() != null) {
				createViewFacade();
			}
			binding.bind(getController());
			currentController = getController();
			getController().afterBind();
		}
	}

	protected void createViewFacade() {
		if (!node2Controler.containsKey(getCurrentNode())) {
			setController(createController(getCurrentNode()));
		}
		binding.injectRidgets(getController());
	}

	protected IPresentationProviderService getPresentationDefinitionService() {

		// TODO: handling if no service found ???
		return PresentationProviderServiceAccessor.current().getPresentationProviderService();

	}

}
