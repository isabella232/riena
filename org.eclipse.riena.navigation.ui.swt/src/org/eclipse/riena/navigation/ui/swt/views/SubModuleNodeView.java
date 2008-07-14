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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.riena.navigation.IApplicationModel;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.model.NavigationTreeObserver;
import org.eclipse.riena.navigation.model.SubModuleNodeAdapter;
import org.eclipse.riena.navigation.ui.controllers.SubModuleNodeViewController;
import org.eclipse.riena.navigation.ui.swt.binding.DefaultSwtControlRidgetMapper;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManagerAccessor;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtViewId;
import org.eclipse.riena.ui.ridgets.uibinding.DefaultBindingManager;
import org.eclipse.riena.ui.ridgets.uibinding.IBindingManager;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.part.ViewPart;

/**
 * Abstract implementation for a sub module view
 */
public abstract class SubModuleNodeView<C extends SubModuleNodeViewController> extends ViewPart {

	private Map<ISubModuleNode, C> node2Controler;

	private List<Object> uiControls;

	private C currentController;

	private IBindingManager bindingManager;

	public SubModuleNodeView() {
		bindingManager = new DefaultBindingManager(new SWTBindingPropertyLocator(), new DefaultSwtControlRidgetMapper());
		node2Controler = new HashMap<ISubModuleNode, C>();
		uiControls = new ArrayList<Object>();
	}

	/**
	 * Adds the given control to the list of the controls that will be binded.
	 * 
	 * @param uiControl
	 *            - control to bind
	 */
	protected void addUIControl(Widget uiControl) {
		uiControls.add(uiControl);
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
		uiControl.setData(SWTBindingPropertyLocator.BINDING_PROPERTY, propertyName);
		uiControls.add(uiControl);
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
		navigationTreeObserver.addListener(new SubModuleNodeListener());
		navigationTreeObserver.addListenerTo(node.getTypecastedAdapter(IApplicationModel.class));
	}

	private final class SubModuleNodeListener extends SubModuleNodeAdapter {
		@Override
		public void activated(ISubModuleNode source) {
			SwtViewId id = SwtPresentationManagerAccessor.getManager().getSwtViewId(source);
			if (getViewSite().getId().equals(id.getId())) {
				activate();
			}
		}
	}

	protected abstract void basicCreatePartControl(Composite parent);

	protected abstract C createController(ISubModuleNode pSubModuleNode);

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
				bindingManager.unbind(currentController, uiControls);
			}
			if (node2Controler.get(getCurrentNode()) == null && getCurrentNode() != null) {
				createViewFacade();
			}
			bindingManager.bind(getController(), uiControls);
			currentController = getController();
			getController().afterBind();
		}
	}

	protected void createViewFacade() {
		if (!node2Controler.containsKey(getCurrentNode())) {
			setController(createController(getCurrentNode()));
		}
		bindingManager.injectRidgets(getController(), uiControls);
	}
}
