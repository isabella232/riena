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
import java.util.HashMap;
import java.util.Map;

import org.osgi.service.log.LogService;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.log.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.part.ViewPart;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.core.util.InvocationTargetFailure;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.internal.navigation.ui.swt.Activator;
import org.eclipse.riena.internal.navigation.ui.swt.handlers.NavigationSourceProvider;
import org.eclipse.riena.navigation.IApplicationNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationNodeId;
import org.eclipse.riena.navigation.listener.NavigationTreeObserver;
import org.eclipse.riena.navigation.listener.SubModuleNodeListener;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtViewProvider;
import org.eclipse.riena.ui.ridgets.swt.uibinding.AbstractViewBindingDelegate;
import org.eclipse.riena.ui.ridgets.swt.uibinding.DefaultSwtBindingDelegate;
import org.eclipse.riena.ui.swt.EmbeddedTitleBar;
import org.eclipse.riena.ui.swt.lnf.LnFUpdater;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.SWTControlFinder;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;
import org.eclipse.riena.ui.swt.utils.WidgetIdentificationSupport;
import org.eclipse.riena.ui.workarea.IWorkareaDefinition;
import org.eclipse.riena.ui.workarea.WorkareaManager;

/**
 * Abstract implementation for a sub module view
 */
public abstract class SubModuleView extends ViewPart implements INavigationNodeView<SubModuleNode> {

	private final static Logger LOGGER = Log4r.getLogger(Activator.getDefault(), SubModuleView.class);
	private final static LnFUpdater LNF_UPDATER = new LnFUpdater();
	private final static Map<SubModuleView, SubModuleNode> FALLBACK_NODES = new HashMap<SubModuleView, SubModuleNode>();
	/**
	 * The key of the SWT data property that identifies the (top) composite of a
	 * sub-module view.
	 */
	private static final String IS_SUB_MODULE_VIEW_COMPOSITE = "isSubModuleViewComposite"; //$NON-NLS-1$

	private final AbstractViewBindingDelegate binding;
	private final FocusListener focusListener;

	private SubModuleController currentController;

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

	/**
	 * Keep a reference to the control that was last focused for a given
	 * controller id.
	 * 
	 * @see #getControllerId()
	 * @see #canRestoreFocus()
	 */
	private Map<Integer, Control> focusControlMap = new HashMap<Integer, Control>(1);
	private NavigationTreeObserver navigationTreeObserver;

	/**
	 * Creates a new instance of {@code SubModuleView}.
	 */
	public SubModuleView() {
		binding = createBinding();
		focusListener = new FocusListener();
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
	 *            control to bind
	 */
	protected void addUIControl(Object uiControl) {
		binding.addUIControl(uiControl);
	}

	/**
	 * Adds the given control to the list of the controls that will be binded.
	 * 
	 * @param uiControl
	 *            control to bind
	 * @param bindingId
	 *            ID for binding
	 */
	protected void addUIControl(Object uiControl, String bindingId) {
		binding.addUIControl(uiControl, bindingId);
	}

	/**
	 * Find the navigation node corresponding to the passed ids.
	 * 
	 * @param nodeId
	 *            the id of the node
	 * @param secondaryId
	 *            the secondary id
	 * @return the subModule node if found
	 */
	protected ISubModuleNode getSubModuleNode(String nodeId, String secondaryId) {
		return SwtViewProvider.getInstance().getNavigationNode(nodeId, secondaryId, ISubModuleNode.class);
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
		parent.setData(IS_SUB_MODULE_VIEW_COMPOSITE, Boolean.TRUE);
		if (!Beans.isDesignTime()) {
			observeRoot();
			SubModuleController controller = createController(getNavigationNode());
			if (controller != null) {
				setPartName(controller.getNavigationNode().getLabel());
			}
			contentComposite = createContentComposite(parent);
		} else {
			contentComposite = parent;
		}

		createWorkarea(contentComposite);

		if (Beans.isDesignTime()) {
			LNF_UPDATER.updateUIControls(getParentComposite(), true);
		} else {
			createViewFacade();
			doBinding();
		}

		if (getViewSite() != null) {
			if (getViewSite().getSecondaryId() != null) {
				WidgetIdentificationSupport.setIdentification(contentComposite,
						"subModuleView", getViewSite().getId(), getViewSite().getSecondaryId()); //$NON-NLS-1$
			}
		}

		contentComposite.getDisplay().addFilter(SWT.FocusIn, focusListener);
		contentComposite.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent event) {
				event.widget.getDisplay().removeFilter(SWT.FocusIn, focusListener);
			}
		});
	}

	/**
	 * Creates the workarea. Subclasses can override this method to get full
	 * control over the workarea layout.
	 * 
	 * @param parent
	 * @since 1.2
	 */
	protected void createWorkarea(Composite parent) {
		basicCreatePartControl(parent);
		afterBasicCreatePartControl(parent);
	}

	/**
	 * Is called by the SubModuleView after
	 * {@link #basicCreatePartControl(Composite)}
	 * 
	 * @param parent
	 * @since 1.2
	 */
	protected void afterBasicCreatePartControl(Composite parent) {

	}

	@Override
	public void dispose() {
		IApplicationNode appNode = getAppNode();
		if (navigationTreeObserver != null && appNode != null) {
			navigationTreeObserver.removeListenerFrom(appNode);
		}
		FALLBACK_NODES.remove(this);
		super.dispose();
	}

	/**
	 * This implementation will automatically focus on the control that had
	 * previously the focus, or, the first focusable control.
	 * <p>
	 * You may overwrite it, but it typically is not necessary to do so. If you
	 * still want to use the 'restore focus to last control' functionality,
	 * check {@link #canRestoreFocus()} and the invoke this method.
	 */
	@Override
	public void setFocus() {
		if (canRestoreFocus()) {
			Integer id = Integer.valueOf(getControllerId());
			Control lastFocusedControl = focusControlMap.get(id);
			lastFocusedControl.setFocus();
		} else {
			contentComposite.setFocus();
		}
	}

	protected Composite getParentComposite() {
		return parentComposite;
	}

	protected Composite getContentComposite() {
		return contentComposite;
	}

	protected void blockView(boolean block) {
		if (!parentComposite.isDisposed()) {
			parentComposite.setCursor(block ? getWaitCursor() : getArrowCursor());
			contentComposite.setEnabled(!block);
			if (!block) {
				contentComposite.setRedraw(false);
				contentComposite.setRedraw(true);
			}
		}
	}

	/**
	 * Creates the content of the sub module view.
	 * 
	 * @param parent
	 *            composite for the content of the sub module view
	 */
	protected abstract void basicCreatePartControl(Composite parent);

	/**
	 * Returns true if {@link #setFocus()} can restore the focus to the control
	 * that last had the focus in this view; false otherwise.
	 * 
	 * @since 1.2
	 */
	protected final boolean canRestoreFocus() {
		Integer id = Integer.valueOf(getControllerId());
		Control control = focusControlMap.get(id);
		return !SwtUtilities.isDisposed(control);
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

	protected SubModuleController createController(ISubModuleNode node) {

		// check node itself for controller definition first
		Assert.isNotNull(node, "navigation node must not be null"); //$NON-NLS-1$
		Assert.isNotNull(node.getNodeId(), "navigation node id must not be null"); //$NON-NLS-1$
		Assert.isNotNull(node.getNodeId().getTypeId(), "navigation node type id must not be null"); //$NON-NLS-1$

		// consult workarea manager
		SubModuleController controller = null;
		IWorkareaDefinition def = WorkareaManager.getInstance().getDefinition(node);
		if (def != null) {
			try {
				controller = (SubModuleController) def.createController();
			} catch (Exception ex) {
				String message = String.format("cannnot create controller for class %s", def.getControllerClass()); //$NON-NLS-1$ 
				LOGGER.log(LogService.LOG_ERROR, message, ex);
				throw new InvocationTargetFailure(message, ex);
			}
		}
		if (controller != null) {
			controller.setNavigationNode(node);
		}

		return controller;
	}

	public void addUpdateListener(IComponentUpdateListener listener) {
		throw new UnsupportedOperationException();
	}

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
			LNF_UPDATER.updateUIControls(getParentComposite(), true);
		} else {
			LNF_UPDATER.updateUIControlsAfterBind(getContentComposite());
		}

		NavigationSourceProvider.activeNodeChanged(getNavigationNode());
	}

	public SubModuleNode getNavigationNode() {

		if (getViewSite() == null) {
			return getFallbackNavigationNode();
		}

		String viewId = this.getViewSite().getId();
		String secondaryId = this.getViewSite().getSecondaryId();
		SubModuleNode result = (SubModuleNode) getSubModuleNode(viewId, secondaryId);
		if (result == null) {
			result = getRCPSubModuleNode();
		}
		if (result == null) {
			result = getFallbackNavigationNode();
		}
		return result;
	}

	public void unbind() {
		SubModuleController controller = getController();
		if (controller != null) {
			binding.unbind(controller);
		}
	}

	// helping methods
	//////////////////

	private void addUIControls(Composite composite) {
		SWTControlFinder finder = new SWTControlFinder(composite) {
			@Override
			public void handleBoundControl(Control control, String bindingProperty) {
				addUIControl(control);
			}

			@Override
			public void handleControl(Control control) {
				if (control.getMenu() != null) {
					addMenuControl(control.getMenu());
				}
				super.handleControl(control);
			}
		};
		finder.run();
	}

	private void addMenuControl(Menu menu) {
		SWTBindingPropertyLocator locator = SWTBindingPropertyLocator.getInstance();
		for (int i = 0; i < menu.getItemCount(); i++) {
			MenuItem item = menu.getItem(i);
			String bindingId = locator.locateBindingProperty(item);
			if (StringUtils.isGiven(bindingId)) {
				addUIControl(item, bindingId);
			}
			if (item.getMenu() != null) {
				addMenuControl(item.getMenu());
			}
		}
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
			// don't show the right border of the title => 1
			formData.right = new FormAttachment(100, 1);
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

		return composite;
	}

	private void doBinding() {
		bind(getNavigationNode());
		if (title != null) {
			title.setWindowActive(getNavigationNode().isActivated());
		}
	}

	private Cursor getArrowCursor() {
		return contentComposite.getDisplay().getSystemCursor(SWT.CURSOR_ARROW);
	}

	private IApplicationNode getAppNode() {
		INavigationNode<?> node = getNavigationNode();
		while (node.getParent() != null) {
			node = node.getParent();
		}
		IApplicationNode appNode = node.getTypecastedAdapter(IApplicationNode.class);
		return appNode;
	}

	/**
	 * Returns the id (hashcode) of the controller if available, or zero.
	 */
	private int getControllerId() {
		SubModuleController controller = getController();
		return controller == null ? 0 : controller.hashCode();
	}

	/**
	 * @return a fallback navigation node for views that are not associated with
	 *         a node in the navigation tree.
	 */
	private SubModuleNode getFallbackNavigationNode() {
		SubModuleNode fallbackNode = FALLBACK_NODES.get(this);
		if (fallbackNode == null) {
			fallbackNode = new SubModuleNode(new NavigationNodeId(getClass().getName() + FALLBACK_NODES.size()));
			FALLBACK_NODES.put(this, fallbackNode);
		}
		return fallbackNode;
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

	private Cursor getWaitCursor() {
		return contentComposite.getDisplay().getSystemCursor(SWT.CURSOR_WAIT);
	}

	/**
	 * Returns true if we are running without the navigation tree
	 */
	private boolean isRCP() {
		getNavigationNode();
		return rcpSubModuleNode != null;
	}

	private void observeRoot() {
		IApplicationNode appNode = getAppNode();
		if (appNode != null) {
			Assert.isLegal(navigationTreeObserver == null);
			navigationTreeObserver = new NavigationTreeObserver();
			navigationTreeObserver.addListener(new SubModuleNodesListener());
			navigationTreeObserver.addListenerTo(appNode);
		}
	}

	// helping classes
	//////////////////

	/**
	 * A listener for all submodules in the navigation tree! Needed i.e. to
	 * support shared views. When adding a method be sure to check the node.
	 */
	private final class SubModuleNodesListener extends SubModuleNodeListener {
		@Override
		public void activated(ISubModuleNode source) {
			if (source.equals(getNavigationNode())) {
				doBinding();
			}
		}

		@Override
		public void block(ISubModuleNode source, boolean block) {
			if (source.equals(getNavigationNode())) {
				blockView(block);
			}
		}

	}

	/**
	 * Keeps track of the last focused control within this view.
	 */
	private final class FocusListener implements Listener {

		public void handleEvent(Event event) {
			if (contentComposite.isVisible() && event.widget instanceof Control) {
				Control control = (Control) event.widget;
				if (contains(contentComposite, control)) {
					int id = getControllerId();
					if (id != 0) {
						focusControlMap.put(Integer.valueOf(id), control);
					}
				}
			}
		}

		private boolean contains(Composite container, Control control) {
			boolean result = false;
			Composite parent = control.getParent();
			while (!result && parent != null) {
				result = container == parent;
				parent = parent.getParent();
			}
			return result;
		}

	}

}
