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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.listener.ModuleNodeListener;
import org.eclipse.riena.navigation.listener.NavigationTreeObserver;
import org.eclipse.riena.navigation.listener.SubModuleNodeListener;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.ui.swt.binding.InjectSwtViewBindingDelegate;
import org.eclipse.riena.navigation.ui.swt.component.SubModuleToolTip;
import org.eclipse.riena.navigation.ui.swt.lnf.renderer.EmbeddedTitlebarRenderer;
import org.eclipse.riena.navigation.ui.views.AbstractViewBindingDelegate;
import org.eclipse.riena.ui.ridgets.viewcontroller.IViewController;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * View of a module.
 */
public class ModuleView implements INavigationNodeView<SWTModuleController, ModuleNode> {

	private AbstractViewBindingDelegate binding;
	private Composite parent;
	private Composite body;
	private Tree subModuleTree;
	private ModuleNode moduleNode;
	private boolean pressed;
	private boolean hover;
	private Rectangle bounds;

	private NavigationTreeObserver navigationTreeObserver;
	private List<IComponentUpdateListener> updateListeners;

	public ModuleView(Composite parent) {
		this.parent = parent;
		binding = createBinding();
		updateListeners = new ArrayList<IComponentUpdateListener>();
		buildView();
	}

	/**
	 * Creates a delegate for the binding of view and controller.
	 * 
	 * @return delegate for binding
	 */
	protected AbstractViewBindingDelegate createBinding() {
		return new InjectSwtViewBindingDelegate();
	}

	/**
	 * Builds the composite and the tree of the module view.
	 */
	private void buildView() {

		body = new Composite(getParent(), SWT.DOUBLE_BUFFERED);
		// body.setLayout(new FillLayout());
		body.setLayout(new FormLayout());
		createSubModuleTree();

	}

	public Composite getBody() {
		return body;
	}

	/**
	 * Creates the tree for the sub-modules.
	 */
	protected void createSubModuleTree() {

		subModuleTree = new Tree(getBody(), SWT.NO_SCROLL | SWT.DOUBLE_BUFFERED);
		subModuleTree.setLinesVisible(false);
		binding.addUIControl(subModuleTree, "tree"); //$NON-NLS-1$
		setLayoutData(subModuleTree);

		addListeners();

		new SubModuleToolTip(subModuleTree);
		setTreeBackGround();

	}

	/**
	 * Hides the body (that contains the tree) of the module.
	 */
	public void hideBody() {
		if (!getBody().isDisposed()) {
			getBody().setVisible(false);
		}
	}

	/**
	 * The top and left position of the tree is outside the bounds of the body.
	 * So <i>THE</i> root node of the tree is not visible. In SWT it is not
	 * possible to hide the root of a tree.
	 * 
	 * @param tree
	 */
	private void setLayoutData(Tree tree) {

		FormData formData = new FormData();
		int itemHeight = subModuleTree.getItemHeight();
		itemHeight = 0;
		formData.top = new FormAttachment(getBody(), -itemHeight, 0);
		int indentation = 20; // TODO don't use a fix value
		indentation = 0;
		formData.left = new FormAttachment(getBody(), -indentation, 0);
		formData.right = new FormAttachment(100, 0);
		formData.bottom = new FormAttachment(100, 0);
		subModuleTree.setLayoutData(formData);

	}

	/**
	 * Returns the renderer of the title bar.
	 * 
	 * @return
	 */
	public EmbeddedTitlebarRenderer getLnfTitlebarRenderer() {

		EmbeddedTitlebarRenderer renderer = (EmbeddedTitlebarRenderer) LnfManager.getLnf().getRenderer(
				ILnfKeyConstants.SUB_MODULE_VIEW_TITLEBAR_RENDERER);
		if (renderer == null) {
			renderer = new EmbeddedTitlebarRenderer();
		}
		return renderer;

	}

	/**
	 * Clips (if necessary) the text of the given tree item and all child items.
	 * 
	 * @param gc
	 * @param item
	 *            - tree item
	 * @return true: some text was clipped; false: no text was clipped
	 */
	private boolean clipSubModuleTexts(GC gc, TreeItem item) {

		boolean clipped = clipSubModuleText(gc, item);

		TreeItem[] items = item.getItems();
		for (TreeItem childItem : items) {
			if (clipSubModuleTexts(gc, childItem)) {
				clipped = true;
			}
		}

		return clipped;

	}

	/**
	 * Clips (if necessary) the text of the given tree item.
	 * 
	 * @param gc
	 * @param item
	 *            - tree item
	 * @return true: text was clipped; false: text was not clipped
	 */
	private boolean clipSubModuleText(GC gc, TreeItem item) {

		boolean clipped = false;

		Rectangle treeBounds = getTree().getBounds();
		Rectangle itemBounds = item.getBounds();
		int maxWidth = treeBounds.width - itemBounds.x - 5;
		INavigationNode<?> subModule = (INavigationNode<?>) item.getData();
		String longText = ""; //$NON-NLS-1$
		if (subModule != null) {
			longText = subModule.getLabel();
		} else {
			longText = item.getText();
		}
		String text = SwtUtilities.clipText(gc, longText, maxWidth);
		item.setText(text);

		clipped = !longText.equals(text);
		return clipped;

	}

	/**
	 * Clips (if necessary) the text of the tree items and hides the scroll
	 * bars.
	 * 
	 * @param gc
	 */
	private void onTreePaint(GC gc) {

		TreeItem[] items = getTree().getItems();
		for (TreeItem item : items) {
			clipSubModuleTexts(gc, item);
		}

	}

	protected void resize() {
		fireUpdated(null);
	}

	/**
	 * Adds listeners to the sub-module tree.
	 */
	private void addListeners() {

		getTree().addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				TreeItem[] selection = getTree().getSelection();
				if (selection[0].getData() instanceof ISubModuleNode) {
					ISubModuleNode activeSubModule = (ISubModuleNode) selection[0].getData();
					activeSubModule.activate();
				}
				resize();
			}

		});

		getTree().addListener(SWT.Paint, new Listener() {

			public void handleEvent(Event event) {
				onTreePaint(event.gc);
			}

		});

		getTree().addListener(SWT.Expand, new Listener() {

			public void handleEvent(Event event) {
				handleExpandCollapse(event, true);
			}

		});

		getTree().addListener(SWT.Collapse, new Listener() {

			public void handleEvent(Event event) {
				handleExpandCollapse(event, false);
			}

		});

	}

	/**
	 * After a node has been expanded or collapsed the size of the module must
	 * be updated.
	 * 
	 * @param event
	 *            - the event which occurred
	 * @param expand
	 */
	private void handleExpandCollapse(Event event, boolean expand) {
		if (event.item instanceof TreeItem) {
			TreeItem item = (TreeItem) event.item;
			INavigationNode<?> node = (INavigationNode<?>) item.getData();
			node.setExpanded(expand);
		}
		resize();
	}

	protected void setTreeBackGround() {
		subModuleTree.setBackground(LnfManager.getLnf().getColor("SubModuleTree.background")); //$NON-NLS-1$
	}

	protected Composite getParent() {
		return parent;
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.views.INavigationNodeView#getNavigationNode()
	 */
	public ModuleNode getNavigationNode() {
		return moduleNode;
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.views.INavigationNodeView#bind(org.eclipse.riena.navigation.INavigationNode)
	 */
	public void bind(ModuleNode node) {

		moduleNode = node;

		navigationTreeObserver = new NavigationTreeObserver();
		navigationTreeObserver.addListener(new SubModuleListener());
		navigationTreeObserver.addListener(new ModuleListener());
		navigationTreeObserver.addListenerTo(moduleNode);

		if (getNavigationNode().getPresentation() instanceof IViewController) {
			IViewController viewController = (IViewController) node.getPresentation();
			binding.injectRidgets(viewController);
			binding.bind(viewController);
			viewController.afterBind();
		}

	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.views.INavigationNodeView#unbind()
	 */
	public void unbind() {

		if (getNavigationNode().getPresentation() instanceof IViewController) {
			IViewController viewController = (IViewController) getNavigationNode().getPresentation();
			binding.unbind(viewController);
		}

		navigationTreeObserver.removeListenerFrom(moduleNode);
		moduleNode = null;

	}

	/**
	 * After adding of removing a sub-module from another sub-module, the module
	 * view must be resized.
	 */
	private class SubModuleListener extends SubModuleNodeListener {

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#childAdded(org.eclipse.riena.navigation.INavigationNode,
		 *      org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void childAdded(ISubModuleNode source, ISubModuleNode childAdded) {
			resize();
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#childRemoved(org.eclipse.riena.navigation.INavigationNode,
		 *      org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void childRemoved(ISubModuleNode source, ISubModuleNode childRemoved) {
			resize();
		}

		/**
		 * @see org.eclipse.riena.navigation.listener.NavigationNodeListener#activated
		 *      (org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void activated(ISubModuleNode source) {
			resize();
		}

	}

	/**
	 * After adding of removing a sub-module from this module, the module view
	 * must be resized.
	 */
	private class ModuleListener extends ModuleNodeListener {

		@Override
		public void childAdded(IModuleNode source, ISubModuleNode childAdded) {
			resize();
		}

		@Override
		public void childRemoved(IModuleNode source, ISubModuleNode childRemoved) {
			resize();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.riena.navigation.listener.NavigationNodeListener#disposed
		 * (org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void disposed(IModuleNode source) {
			super.disposed(source);
			dispose();
		}

	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.views.INavigationNodeView
	 *      #updateBounds(int)
	 */
	public int calculateBounds(int positionHint) {
		return -1;
	}

	/**
	 * Disposes this module item.
	 */
	public void dispose() {
		unbind();
		SwtUtilities.disposeWidget(getBody());
		SwtUtilities.disposeWidget(getTree());
	}

	/**
	 * Returns the tree with the sub-module items.
	 * 
	 * @return tree
	 */
	public Tree getTree() {
		return subModuleTree;
	}

	/**
	 * Returns the height of the open item.
	 * 
	 * @return height.
	 */
	public int getOpenHeight() {
		IModuleNode moduleNode = getNavigationNode();
		int depth = moduleNode.calcDepth();
		if (depth == 0) {
			return 0;
		} else {
			int itemHeight = getTree().getItemHeight();
			return depth * itemHeight + 1;
		}
	}

	/**
	 * Returns a rectangle describing the size and location of this module item.
	 * 
	 * @return the bounds
	 */
	public Rectangle getBounds() {
		return bounds;
	}

	/**
	 * Sets a rectangle describing the size and location of this module item.
	 * 
	 * @param bounds
	 *            the bounds to set
	 */
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	/**
	 * Returns if the module item is pressed or not.
	 * 
	 * @param pressed
	 *            - true, if mouse over the module and pressed; otherwise false.
	 */
	public boolean isPressed() {
		return pressed;
	}

	/**
	 * Sets if the module item is pressed or not.<br>
	 * If the given state differs from the current state, the parent of item is
	 * redrawn.
	 * 
	 * @param pressed
	 *            - true, if mouse over the module and pressed; otherwise false.
	 */
	public void setPressed(boolean pressed) {
		if (this.pressed != pressed) {
			this.pressed = pressed;
			if (!parent.isDisposed()) {
				parent.redraw();
			}
		}
	}

	/**
	 * Returns if the module item is highlighted, because the mouse hovers over
	 * the item.
	 * 
	 * @return true, if mouse over the module; otherwise false.
	 */
	public boolean isHover() {
		return hover;
	}

	/**
	 * Sets if the module item is highlighted, because the mouse hovers over the
	 * item.<br>
	 * If the given hover state differs from the current state, the parent of
	 * item is redrawn.
	 * 
	 * @param hover
	 *            - true, if mouse over the module; otherwise false.
	 */
	public void setHover(boolean hover) {
		if (this.hover != hover) {
			this.hover = hover;
			if (!parent.isDisposed()) {
				parent.redraw();
			}
		}
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return getNavigationNode().getIcon();
	}

	/**
	 * @return the activated
	 */
	public boolean isActivated() {
		return getNavigationNode().isActivated();
	}

	/**
	 * @return the closeable
	 */
	public boolean isCloseable() {
		return getNavigationNode().isCloseable();
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return getNavigationNode().getLabel();
	}

	protected void fireUpdated(INavigationNode<?> node) {
		for (IComponentUpdateListener listener : updateListeners) {
			listener.update(node);
		}
	}

	/**
	 * @see org.eclipse.riena.navigation.ui.swt.views.INavigationNodeView#addUpdateListener(org.eclipse.riena.navigation.ui.swt.views.IComponentUpdateListener)
	 */
	public void addUpdateListener(IComponentUpdateListener listener) {
		updateListeners.add(listener);
	}

}
