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
package org.eclipse.riena.navigation.ui.swt.views;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.riena.core.util.ListenerList;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ISubModuleViewBuilder;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.listener.NavigationTreeObserver;
import org.eclipse.riena.navigation.listener.SubModuleNodeListener;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.model.SubModuleViewBuilderAccessor;
import org.eclipse.riena.navigation.ui.controllers.ControllerUtils;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtViewId;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtViewProviderAccessor;
import org.eclipse.riena.ui.ridgets.swt.uibinding.AbstractViewBindingDelegate;
import org.eclipse.riena.ui.ridgets.swt.uibinding.DefaultSwtBindingDelegate;
import org.eclipse.riena.ui.swt.EmbeddedTitleBar;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.part.ViewPart;

/**
 * Abstract implementation for a sub module view
 */
public abstract class SubModuleView<C extends SubModuleController> extends ViewPart implements
		INavigationNodeView<SWTModuleController, SubModuleNode> {

	private Map<ISubModuleNode, C> node2Controler;
	private AbstractViewBindingDelegate binding;
	private C currentController;

	/**
	 * This node is used when creating this ViewPart inside an RCP application.
	 * It is created with information from the extension registry, instead being
	 * obtained from the navigation tree.
	 * 
	 * @see #getRCPSubModuleNode()
	 */
	private SubModuleNode rcpSubModuleNode;

	/** The title bar at the top of the view. May be null if running in RCP */
	private EmbeddedTitleBar title;
	private ListenerList<IComponentUpdateListener> updateListeners;

	private Composite parentComposite;
	private Composite contentComposite;
	/** Shared (system) cursor. No need to dispose it. */
	private Cursor cursorWait;
	/** Shared (system) cursor. No need to dispose it. */
	private Cursor cursorArrow;

	/**
	 * Creates a new instance of {@code SubModuleView}.
	 */
	public SubModuleView() {
		binding = createBinding();
		node2Controler = new HashMap<ISubModuleNode, C>();
		updateListeners = new ListenerList<IComponentUpdateListener>(IComponentUpdateListener.class);
	}

	/**
	 * Creates a delegate for the binding of view and controller.
	 * 
	 * @return delegate for binding
	 */
	protected AbstractViewBindingDelegate createBinding() {
		return new DefaultSwtBindingDelegate();
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
		return SwtViewProviderAccessor.getViewProvider().getNavigationNode(pId, pSecondary, ISubModuleNode.class);
	}

	/**
	 * @return the controller
	 */
	public C getController() {
		return node2Controler.get(getNavigationNode());
	}

	/**
	 * @param controller
	 *            the controller to set
	 */
	public void setController(C controller) {
		if (node2Controler.get(getNavigationNode()) == null) {
			node2Controler.put(getNavigationNode(), controller);
		}
	}

	@Override
	public void createPartControl(Composite parent) {
		this.parentComposite = parent;
		observeRoot();
		C controller = createController(getNavigationNode());
		setController(controller);
		if (controller != null) {
			setPartName(controller.getNavigationNode().getLabel());
		}
		Composite contentComposite = createContentComposite(parent);
		basicCreatePartControl(contentComposite);
		createViewFacade();
		doBinding();
	}

	protected Composite getParentComposite() {
		return parentComposite;
	}

	protected Composite getContentComposite() {
		return contentComposite;
	}

	/**
	 * Creates the composite for the content of the view. Its a container that
	 * holds the UI controls of the view.<br>
	 * Above this container the title bar of the view is located.
	 * 
	 * @param parent
	 * @return
	 */
	private Composite createContentComposite(Composite parent) {

		Color bgColor = LnfManager.getLnf().getColor(ILnfKeyConstants.SUB_MODULE_BACKGROUND);
		parent.setBackground(bgColor);

		parent.setLayout(new FormLayout());

		if (!isRCP()) {
			title = new EmbeddedTitleBar(parent, SWT.NONE);
			addUIControl(title, SubModuleController.WINDOW_RIDGET);
			title.setActive(true);
			FormData formData = new FormData();
			// don't show the top border of the title => -1
			formData.top = new FormAttachment(0, -1);
			// don't show the left border of the title => -1
			formData.left = new FormAttachment(0, -1);
			// don't show the top border of the title, but show the bottom
			// border => -1
			formData.bottom = new FormAttachment(0, title.getSize().y - 1);
			// don't show the right (and left) border of the title => 2
			formData.right = new FormAttachment(100, 2);
			title.setLayoutData(formData);
		}

		contentComposite = new Composite(parent, SWT.DOUBLE_BUFFERED);
		contentComposite.setBackground(bgColor);
		FormData formData = new FormData();
		if (title != null) {
			formData.top = new FormAttachment(title, 0, 0);
		} else {
			formData.top = new FormAttachment(0, -1);
		}
		formData.left = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100);
		formData.right = new FormAttachment(100);
		contentComposite.setLayoutData(formData);

		// initialize cursors
		cursorWait = createWaitCursor();
		cursorArrow = createArrowCursor();

		return contentComposite;
	}

	protected Cursor createArrowCursor() {
		return getSite().getShell().getDisplay().getSystemCursor(SWT.CURSOR_ARROW);
	}

	protected Cursor createWaitCursor() {
		return getSite().getShell().getDisplay().getSystemCursor(SWT.CURSOR_WAIT);
	}

	private void observeRoot() {
		INavigationNode<?> node = getNavigationNode();
		while (node.getParent() != null) {
			node = node.getParent();
		}
		NavigationTreeObserver navigationTreeObserver = new NavigationTreeObserver();
		navigationTreeObserver.addListener(new MySubModuleNodeListener());
		IApplicationNode appNode = node.getTypecastedAdapter(IApplicationNode.class);
		if (appNode != null) {
			navigationTreeObserver.addListenerTo(appNode);
		}
	}

	protected void activate(ISubModuleNode source) {
		SwtViewId id = SwtViewProviderAccessor.getViewProvider().getSwtViewId(source);
		if (getViewSite().getId().equals(id.getId())) {
			doBinding();
		}
	}

	private final class MySubModuleNodeListener extends SubModuleNodeListener {
		@Override
		public void activated(ISubModuleNode source) {
			super.activated(source);
			activate(source);
		}

		@Override
		public void block(ISubModuleNode source, boolean block) {
			super.block(source, block);
			if (source.equals(getNavigationNode())) {
				ControllerUtils.blockRidgets(getController().getRidgets(), block);
				blockView(block);
			}
		}

		@Override
		public void afterActivated(ISubModuleNode source) {

			super.afterActivated(source);

		}

	}

	protected void blockView(boolean block) {
		parentComposite.setCursor(block ? cursorWait : cursorArrow);
		contentComposite.setEnabled(!block);
	}

	/**
	 * Creates the content of the sub module view.
	 * 
	 * @param parent
	 *            - composite for the content of the sub module view
	 */
	protected abstract void basicCreatePartControl(Composite parent);

	@Override
	public void setFocus() {
		// doBinding();
	}

	protected void createViewFacade() {
		if (!node2Controler.containsKey(getNavigationNode())) {
			setController(createController(getNavigationNode()));
		}
		if (getController() != null) {
			binding.injectRidgets(getController());
		}
	}

	protected C createController(ISubModuleNode pSubModuleNode) {
		C controller = (C) getSubModuleViewBuilder().provideController(pSubModuleNode);
		if (controller != null) {
			controller.setNavigationNode(pSubModuleNode);
		}
		return controller;
	}

	/**
	 * @return
	 */
	protected ISubModuleViewBuilder getSubModuleViewBuilder() {
		// TODO: handling if no service found ???
		return SubModuleViewBuilderAccessor.current().getSubModuleViewBuilder();
	}

	private void doBinding() {
		bind(getNavigationNode());
	}

	public void addUpdateListener(IComponentUpdateListener listener) {
		updateListeners.add(listener);
	}

	public void bind(SubModuleNode node) {
		if (currentController != getController()) {
			if (currentController != null) {
				if (currentController.getNavigationNode().isDisposed()) {
					return;
				}
				binding.unbind(currentController);
			}
			if ((getNavigationNode() != null) && (node2Controler.get(getNavigationNode()) == null)) {
				createViewFacade();
			}
			if (getController() != null) {
				currentController = getController();
			}
			binding.bind(currentController);
			currentController.afterBind();
			if (title != null) {
				title.setActive(currentController.isActivated());
			}
		}
	}

	public SubModuleNode getNavigationNode() {
		String viewId = this.getViewSite().getId();
		String secondaryId = this.getViewSite().getSecondaryId();
		SubModuleNode result = (SubModuleNode) getSubModuleNode(viewId, secondaryId);
		if (result == null) {
			result = getRCPSubModuleNode();
		}
		return result;
	}

	public void unbind() {

		SubModuleNode node = getNavigationNode();
		if (node == null) {
			return;
		}

		C controller = node2Controler.get(node);
		if (controller != null) {
			binding.unbind(controller);
			node2Controler.remove(node);
		}

	}

	/**
	 * Returns true if we are running without the navigation tree
	 */
	private boolean isRCP() {
		getNavigationNode();
		return rcpSubModuleNode != null;
	}

	private SubModuleNode getRCPSubModuleNode() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IConfigurationElement[] elements = registry
				.getConfigurationElementsFor("org.eclipse.riena.navigation.subModule");
		String viewId = getViewSite().getId();
		for (int i = 0; rcpSubModuleNode == null && i < elements.length; i++) {
			IConfigurationElement element = elements[i];
			if ("subModule".equals(element.getName())) {
				String view = element.getAttribute("view");
				if (viewId.equals(view)) {
					String typeId = element.getAttribute("typeId");
					if (typeId != null) {
						rcpSubModuleNode = new SubModuleNode(new NavigationNodeId(typeId), getPartName());
					}
				}
			}
		}
		return rcpSubModuleNode;
	}

}
