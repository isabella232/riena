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
package org.eclipse.riena.navigation.ui.swt.views;

import java.beans.Beans;

import org.osgi.service.log.LogService;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.log.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.part.ViewPart;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.util.InvocationTargetFailure;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.internal.navigation.ui.swt.Activator;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.listener.NavigationTreeObserver;
import org.eclipse.riena.navigation.listener.SubModuleNodeListener;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.ControllerUtils;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtViewProviderAccessor;
import org.eclipse.riena.ui.common.IComplexComponent;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.AbstractViewBindingDelegate;
import org.eclipse.riena.ui.ridgets.swt.uibinding.DefaultSwtBindingDelegate;
import org.eclipse.riena.ui.swt.EmbeddedTitleBar;
import org.eclipse.riena.ui.swt.lnf.LnFUpdater;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.workarea.IWorkareaDefinition;
import org.eclipse.riena.ui.workarea.WorkareaManager;

/**
 * Abstract implementation for a sub module view
 */
public abstract class SubModuleView<C extends SubModuleController> extends ViewPart implements
		INavigationNodeView<SWTModuleController, SubModuleNode> {

	private final static Logger LOGGER = Log4r.getLogger(Activator.getDefault(), SubModuleView.class);
	private final static LnFUpdater LNF_UPDATER = new LnFUpdater();

	private AbstractViewBindingDelegate binding;
	private SubModuleController currentController;

	private VisibilityListener visibilityListener;

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
		visibilityListener = new VisibilityListener();
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
	protected void addUIControl(Object uiControl) {
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
	protected void addUIControl(Object uiControl, String propertyName) {
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
	public SubModuleController getController() {

		if (getNavigationNode() != null
				&& getNavigationNode().getNavigationNodeController() instanceof SubModuleController) {
			return (SubModuleController) getNavigationNode().getNavigationNodeController();
		}
		return null;
	}

	@Override
	public void createPartControl(Composite parent) {
		this.parentComposite = parent;
		if (!Beans.isDesignTime()) {
			observeRoot();
			C controller = createController(getNavigationNode());
			if (controller != null) {
				setPartName(controller.getNavigationNode().getLabel());
			}
			contentComposite = createContentComposite(parent);
		} else {
			contentComposite = parent;
		}
		basicCreatePartControl(contentComposite);
		LNF_UPDATER.updateUIControls(getParentComposite());
		if (!Beans.isDesignTime()) {
			createViewFacade();
			doBinding();
			addVisibilityListener(visibilityListener);
		}
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

		Color bgColor = LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND);
		parent.setBackground(bgColor);

		parent.setLayout(new FormLayout());

		if (!isRCP()) {
			title = new EmbeddedTitleBar(parent, SWT.NONE);
			addUIControl(title, SubModuleController.WINDOW_RIDGET);
			title.setWindowActive(true);
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

		Composite composite = new Composite(parent, SWT.DOUBLE_BUFFERED);
		composite.setBackground(bgColor);
		FormData formData = new FormData();
		if (title != null) {
			formData.top = new FormAttachment(title, 0, 0);
		} else {
			formData.top = new FormAttachment(0, -1);
		}
		formData.left = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100);
		formData.right = new FormAttachment(100);
		composite.setLayoutData(formData);

		// initialize cursors
		cursorWait = createWaitCursor();
		cursorArrow = createArrowCursor();

		return composite;
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
		navigationTreeObserver.addListener(new SubModuleNodesListener());
		IApplicationNode appNode = node.getTypecastedAdapter(IApplicationNode.class);
		if (appNode != null) {
			navigationTreeObserver.addListenerTo(appNode);
		}
	}

	/**
	 * A listener for all submodules in the navigation tree! Needed i.e. to
	 * support shared views. When adding a method be sure to check the node.
	 */
	private final class SubModuleNodesListener extends SubModuleNodeListener {
		@Override
		public void activated(ISubModuleNode source) {
			if (source.equals(currentController.getNavigationNode())) {
				doBinding();
				addVisibilityListener(visibilityListener);
			}
		}

		@Override
		public void deactivated(ISubModuleNode source) {
			if (source.equals(currentController.getNavigationNode())) {
				removeVisibilityListener(visibilityListener);
			}
		}

		@Override
		public void block(ISubModuleNode source, boolean block) {
			if (source.equals(currentController.getNavigationNode())) {
				ControllerUtils.blockRidgets(getController().getRidgets(), block);
				blockView(block);
			}
		}
	}

	private void addVisibilityListener(VisibilityListener l) {
		getContentComposite().getDisplay().addFilter(SWT.Show, l);
		getContentComposite().getDisplay().addFilter(SWT.Hide, l);
	}

	private void removeVisibilityListener(VisibilityListener l) {
		getContentComposite().getDisplay().removeFilter(SWT.Show, l);
		getContentComposite().getDisplay().removeFilter(SWT.Hide, l);
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
		addUIControls(getParentComposite());
		if (getController() == null) {
			createController(getNavigationNode());
		}
		if (getController() != null) {
			binding.injectRidgets(getController());
		}
	}

	@SuppressWarnings("unchecked")
	protected C createController(ISubModuleNode node) {

		// check node itself for controller definition first
		Assert.isNotNull(node, "navigation node must not be null"); //$NON-NLS-1$
		Assert.isNotNull(node.getNodeId(), "navigation node id must not be null"); //$NON-NLS-1$
		Assert.isNotNull(node.getNodeId().getTypeId(), "navigation node type id must not be null"); //$NON-NLS-1$

		// consult workarea manager
		C controller = null;
		IWorkareaDefinition def = WorkareaManager.getInstance().getDefinition(node.getNodeId().getTypeId());
		if (def != null) {
			try {
				controller = (C) def.createController();
			} catch (Exception ex) {
				String message = String.format("cannot create controller for class %s", def.getControllerClass()); //$NON-NLS-1$ 
				LOGGER.log(LogService.LOG_ERROR, message, ex);
				throw new InvocationTargetFailure(message, ex);
			}
		}
		if (controller != null) {
			controller.setNavigationNode(node);
		}

		return controller;
	}

	private void doBinding() {
		bind(getNavigationNode());
	}

	public void addUpdateListener(IComponentUpdateListener listener) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.views.INavigationNodeView#bind(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void bind(SubModuleNode node) {

		if (currentController != getController()) {
			if (currentController != null) {
				if (currentController.getNavigationNode().isDisposed()) {
					return;
				}
				binding.unbind(currentController);
			}
			if ((getNavigationNode() != null) && (getController() == null)) {
				createViewFacade();
			}
			if (getController() != null) {
				currentController = getController();
			}
			binding.bind(currentController);
			currentController.afterBind();
			if (title != null) {
				title.setWindowActive(currentController.isActivated());
			}
		}

		LNF_UPDATER.updateUIControlsAfterBind(getParentComposite());

	}

	private class VisibilityListener implements Listener {
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets
		 * .Event)
		 */
		public void handleEvent(final Event event) {
			if (shouldBeHandled(event)) {
				getContentComposite().getDisplay().asyncExec(new Runnable() {
					public void run() {
						for (IRidget r : getController().getRidgets()) {
							if (r instanceof IMarkableRidget) {
								((IMarkableRidget) r).updateMarkers();
							}
						}
						getController().updateNavigationNodeMarkers();
					}
				});
			}
		}

		private boolean shouldBeHandled(Event event) {
			return getContentComposite().equals(event.widget)
					|| (event.widget instanceof Control && isDescendant(getContentComposite(), (Control) event.widget));
		}

		private boolean isDescendant(Control ancestor, Control descendant) {
			if (descendant == null) {
				return false;
			}

			if (descendant == ancestor) {
				return true;
			}

			return isDescendant(ancestor, descendant.getParent());
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

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.views.INavigationNodeView#unbind()
	 */
	public void unbind() {
		SubModuleController controller = getController();
		if (controller != null) {
			binding.unbind(controller);
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
				.getConfigurationElementsFor("org.eclipse.riena.navigation.assemblies"); //$NON-NLS-1$
		String viewId = getViewSite().getId();

		return getRCPSubModuleNode(viewId, elements);
	}

	private SubModuleNode getRCPSubModuleNode(String viewId, IConfigurationElement[] elements) {

		for (int i = 0; rcpSubModuleNode == null && i < elements.length; i++) {
			IConfigurationElement element = elements[i];
			if ("submodule".equals(element.getName())) { //$NON-NLS-1$
				String view = element.getAttribute("view"); //$NON-NLS-1$
				if (viewId.equals(view)) {
					String typeId = element.getAttribute("typeId"); //$NON-NLS-1$
					if (typeId != null) {
						rcpSubModuleNode = new SubModuleNode(new NavigationNodeId(typeId), getPartName());
					}
				}
			} else if (element.getChildren().length > 0) {
				rcpSubModuleNode = getRCPSubModuleNode(viewId, element.getChildren());
			}
		}
		return rcpSubModuleNode;
	}

	private void addUIControls(Composite composite) {

		Control[] controls = composite.getChildren();
		for (Control uiControl : controls) {

			String bindingProperty = SWTBindingPropertyLocator.getInstance().locateBindingProperty(uiControl);
			if (!StringUtils.isEmpty(bindingProperty)) {
				if (isChildOfComplexComponent(uiControl)) {
					continue;
				}
				addUIControl(uiControl);
			}
			if (uiControl instanceof Composite) {
				addUIControls((Composite) uiControl);
			}
		}
	}

	private boolean isChildOfComplexComponent(Control uiControl) {

		if (uiControl.getParent() == null) {
			return false;
		}
		if (uiControl.getParent() instanceof IComplexComponent) {
			return true;
		}
		return isChildOfComplexComponent(uiControl.getParent());
	}
}
