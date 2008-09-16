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
package org.eclipse.riena.navigation.ui.swt.presentation.stack;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.riena.navigation.ISubApplicationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.listener.NavigationTreeObserver;
import org.eclipse.riena.navigation.listener.SubModuleNodeListener;
import org.eclipse.riena.navigation.ui.controllers.SubApplicationController;
import org.eclipse.riena.navigation.ui.swt.binding.InjectSwtViewBindingDelegate;
import org.eclipse.riena.navigation.ui.swt.lnf.renderer.ModuleGroupRenderer;
import org.eclipse.riena.navigation.ui.swt.lnf.renderer.SubModuleViewRenderer;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtViewProviderAccessor;
import org.eclipse.riena.navigation.ui.swt.views.GrabCorner;
import org.eclipse.riena.ui.ridgets.swt.uibinding.AbstractViewBindingDelegate;
import org.eclipse.riena.ui.swt.Statusline;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.presentations.IPresentablePart;
import org.eclipse.ui.presentations.IStackPresentationSite;
import org.eclipse.ui.presentations.StackDropResult;
import org.eclipse.ui.presentations.StackPresentation;

/**
 * <pre>
 * +-----------------------------------------------------------------+
 * |                                                                 |
 * |                  +---------+---------+                          |
 * |                  | SubApp1 | SubApp2 |                          |
 * +-----------------------------------------------------------------+
 * | *1           *2                                                 |
 * | +---------+  +------------------------------------------------+ |
 * | | Module1 |  | Module1 - Sub1                                 | |
 * | +---------+  +------------------------------------------------+ |
 * | | o Sub1  |  |                                                | |
 * | | o Sub2  |  | *3                                             | |
 * | +---------+  |                                                | |
 * | | Module2 |  |                                                | |
 * | +---------+  |                                                | |
 * | +---------+  |                                                | |
 * | | Module2 |  |                                                | |
 * | +---------+  |                                                | |
 * |              |                                                | |
 * |              |                                                | |
 * |              +------------------------------------------------+ |
 * | Status Line                                                     |
 * +-----------------------------------------------------------------+
 * 
 * legend: *1 - navigation
 *         *2 - sub-module view (with title bar and border)
 *         *3 - content area of the sub-module view
 * </pre>
 */
public class TitlelessStackPresentation extends StackPresentation {

	/**
	 * Property to distinguish the view of the navigation.
	 */
	public static final String PROPERTY_NAVIGATION = "navigation"; //$NON-NLS-1$
	/**
	 * Property to distinguish the view of the status line.
	 */
	public static final String PROPERTY_STATUSLINE = "statusLine"; //$NON-NLS-1$

	/**
	 * Left padding of the navigation.<br>
	 * Gap between left shell border and navigation.
	 */
	private static final int PADDING_LEFT = 10;
	/**
	 * Right padding of the sub-module view.<br>
	 * Gap between right shell border and sub-module view.
	 */
	private static final int PADDING_RIGHT = 2;
	/**
	 * Top padding of the sub-module view.<br>
	 * Gap between application switcher and sub-module view.
	 */
	private static final int PADDING_TOP = 10;
	/**
	 * Bottom padding of the navigation/the sub-module view.<br>
	 * Gap between bottom shell border and sub-module view.
	 */
	private static final int PADDING_BOTTOM = 2;
	/**
	 * Gap between navigation and sub-module view
	 */
	private static final int NAVIGATION_SUB_MODULE_GAP = 10;
	/**
	 * The height of the status line
	 */
	private static final int STATUSLINE_HIGHT = 22;

	private Set<IPresentablePart> knownParts = new HashSet<IPresentablePart>();
	private IPresentablePart current;
	private IPresentablePart navigation;
	private IPresentablePart statusLine;
	private Composite parent;
	private SubModuleViewRenderer renderer;
	private boolean hasListener;
	private AbstractViewBindingDelegate binding;

	public TitlelessStackPresentation(Composite parent, IStackPresentationSite stackSite) {
		super(stackSite);
		this.parent = new Composite(parent, SWT.DOUBLE_BUFFERED);
		binding = createBinding();
		createSubModuleViewArea();
	}

	@Override
	public void addPart(IPresentablePart newPart, Object cookie) {
		initializeSubModuleChangeListener();
		if (isNavigation(newPart)) {
			navigation = newPart;
		} else if (isStatusLine(newPart)) {
			statusLine = newPart;
			binding.addUIControl(getStatuslineWidget(statusLine.getControl()));
			SubApplicationController controller = getSubApplicationController();
			if (controller != null) {
				binding.injectAndBind(controller);
			}

		}
		knownParts.add(newPart);
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
	 * @see org.eclipse.ui.presentations.StackPresentation#selectPart(org.eclipse
	 *      .ui.presentations.IPresentablePart)
	 */
	@Override
	public void selectPart(IPresentablePart toSelect) {
		if (current == toSelect) {
			return;
		}
		if (isNavigation(toSelect)) {
			Rectangle navi = calcNavigationBounds();
			toSelect.setBounds(navi);
			redrawSubModuleTitle();
		} else if (isStatusLine(toSelect)) {
			Rectangle line = calcStatusLineBounds();
			toSelect.setBounds(line);
		} else if (toSelect != null) {
			Rectangle inner = calcSubModuleInnerBounds();
			toSelect.setBounds(inner);
			if (current != null) {
				current.setVisible(false);
			}
			redrawSubModuleTitle();
			current = toSelect;
		}
		if (toSelect != null) {
			toSelect.setVisible(true);
		}
	}

	@Override
	public void setBounds(Rectangle bounds) {
		parent.setBounds(bounds);
		if (navigation != null) {
			Rectangle navi = calcNavigationBounds();
			navigation.setBounds(navi);
		}
		if (statusLine != null) {
			Rectangle line = calcStatusLineBounds();
			statusLine.setBounds(line);
		}
		if (current != null) {
			Rectangle inner = calcSubModuleInnerBounds();
			current.setBounds(inner);
		}
		parent.setVisible(true);
	}

	@Override
	public void removePart(IPresentablePart oldPart) {
		if (isNavigation(oldPart)) {
			navigation = null;
		} else if (isStatusLine(oldPart)) {
			statusLine = null;
		} else if (oldPart == current) {
			current = null;
		}
		knownParts.remove(oldPart);
	}

	@Override
	public void dispose() {
		if (getRenderer() != null) {
			getRenderer().dispose();
		}
	}

	/**
	 * This presentation does not support drag and drop.
	 * 
	 * @see org.eclipse.ui.presentations.StackPresentation#dragOver(org.eclipse.swt.widgets.Control,
	 *      org.eclipse.swt.graphics.Point)
	 */
	@Override
	public StackDropResult dragOver(Control currentControl, Point location) {
		return null;
	}

	/**
	 * @see org.eclipse.ui.presentations.StackPresentation#getControl()
	 */
	@Override
	public Control getControl() {
		return parent;
	}

	/**
	 * This presentation has no tabs.
	 * 
	 * @see org.eclipse.ui.presentations.StackPresentation#getTabList(org.eclipse.ui.presentations.IPresentablePart)
	 */
	@Override
	public Control[] getTabList(IPresentablePart part) {
		return new Control[] {};
	}

	@Override
	public void setActive(int newState) {
		/*
		 * Be very care careful what you do here, to avoid causing flicker. This
		 * method may be called with AS_INACTIVE and AS_ACTIVE states repeatedly
		 * for the same part.
		 */
	}

	/**
	 * The state (minimized, maximized or restored) is not relevant for the
	 * <code>TitlelessStackPresentation</code>.
	 * 
	 * @see org.eclipse.ui.presentations.StackPresentation#setState(int)
	 */
	@Override
	public void setState(int state) {
		// nothing to do
	}

	@Override
	public void setVisible(boolean isVisible) {
		parent.setVisible(isVisible);
	}

	@Override
	public void showPaneMenu() {
		// nothing to do
	}

	@Override
	public void showSystemMenu() {
		// nothing to do
	}

	// helping methods
	// ////////////////

	/**
	 * Calculates the inner (i.e. usable) bounds of the sub-module view.
	 * 
	 * @param part
	 * 
	 * @return inner bounds sub-module view
	 */
	private Rectangle calcSubModuleInnerBounds() {
		return getSubModuleViewRenderer().computeInnerBounds(calcSubModuleOuterBounds());
	}

	/**
	 * Calculates the bounds of the sub-module view.
	 * 
	 * @return outer bounds sub-module view
	 */
	private Rectangle calcSubModuleOuterBounds() {

		Rectangle naviBounds = calcNavigationBounds();

		int x = naviBounds.x + naviBounds.width + NAVIGATION_SUB_MODULE_GAP;
		int y = naviBounds.y;
		int width = parent.getBounds().width - x - PADDING_RIGHT;
		int height = naviBounds.height;
		Rectangle outerBounds = new Rectangle(x, y, width, height);

		return outerBounds;
	}

	/**
	 * Calculates the bounds of the navigation on the left side.
	 * 
	 * @return bounds of navigation
	 */
	private Rectangle calcNavigationBounds() {

		GC gc = new GC(parent);
		try {
			Point size = getModuleGroupRenderer().computeSize(gc, SWT.DEFAULT, SWT.DEFAULT);

			int x = PADDING_LEFT;
			int y = PADDING_TOP;
			int width = size.x;
			int height = parent.getBounds().height - PADDING_BOTTOM - PADDING_TOP - STATUSLINE_HIGHT;

			return new Rectangle(x, y, width, height);
		} finally {
			gc.dispose();
		}
	}

	/**
	 * Calculates the bounds of the status line.
	 * 
	 * @return bounds of status line
	 */
	private Rectangle calcStatusLineBounds() {

		Rectangle naviBounds = calcNavigationBounds();

		int x = naviBounds.x;
		int y = naviBounds.y + naviBounds.height;
		int width = parent.getBounds().width - x - GrabCorner.getGrabCornerSize().x;
		int height = STATUSLINE_HIGHT;

		return new Rectangle(x, y, width, height);
	}

	/**
	 * Creates the area within witch the view of the current active sub-module
	 * is displayed.
	 */
	private void createSubModuleViewArea() {

		parent.setBackground(LnfManager.getLnf().getColor(ILnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.addPaintListener(new PaintListener() {

			/**
			 * Paints the border of the current active sub-module.
			 */
			public void paintControl(PaintEvent e) {

				if (current != null) {
					SubModuleViewRenderer viewRenderer = getRenderer();
					if (viewRenderer != null) {
						Rectangle bounds = calcSubModuleOuterBounds();
						viewRenderer.setBounds(bounds);
						viewRenderer.paint(e.gc, null);
					}
				}
			}
		});

	}

	/**
	 * Returns the currently active page.
	 */
	private IWorkbenchPage getActivePage() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	}

	/**
	 * Returns the renderer of the sub-module view.<br>
	 * Renderer renders the border of the sub-module view and not the content of
	 * the view.
	 * 
	 * @return renderer of sub-module view
	 */
	private SubModuleViewRenderer getRenderer() {
		if (renderer == null) {
			renderer = (SubModuleViewRenderer) LnfManager.getLnf().getRenderer("SubModuleView.renderer"); //$NON-NLS-1$
		}
		return renderer;
	}

	/**
	 * Returns the renderer of a module group.
	 */
	private ModuleGroupRenderer getModuleGroupRenderer() {
		return (ModuleGroupRenderer) LnfManager.getLnf().getRenderer(ILnfKeyConstants.MODULE_GROUP_RENDERER);
	}

	/**
	 * Returns the renderer of the sub module view
	 */
	private SubModuleViewRenderer getSubModuleViewRenderer() {
		return (SubModuleViewRenderer) LnfManager.getLnf().getRenderer(ILnfKeyConstants.SUB_MODULE_VIEW_RENDERER);
	}

	/**
	 * Make a depth-first-search starting with parent and return the first
	 * Status line widget found.
	 * 
	 * @throws RuntimeException
	 *             if no Status line was found
	 */
	private Statusline getStatuslineWidget(Control parent) {

		if (parent instanceof Statusline) {
			return (Statusline) parent;
		} else if (parent instanceof Composite) {
			Control[] children = ((Composite) parent).getChildren();
			for (int i = 0; i < children.length; i++) {
				return getStatuslineWidget(children[i]);
			}
		}
		throw new IllegalStateException("could not find Status line widget"); //$NON-NLS-1$
	}

	/**
	 * Install a sub module change listener with the navigation tree, that will
	 * repaint the sub module title when the node has changed.
	 * <p>
	 * This is necessary because when "shared" nodes (i.e. parts) are selected,
	 * we do <b>not</b> receive a {@link #selectPart(IPresentablePart)}
	 * notification if the part is <b>already</b> selected. However the shared
	 * parts may still have different titles.
	 */
	private synchronized void initializeSubModuleChangeListener() {
		if (hasListener) {
			return;
		}
		SubApplicationController controller = getSubApplicationController();
		if (controller != null) {
			NavigationTreeObserver navigationTreeObserver = new NavigationTreeObserver();
			navigationTreeObserver.addListener(new SubModuleNodeListener() {
				@Override
				public void activated(ISubModuleNode source) {
					redrawSubModuleTitle();
				}
			});
			navigationTreeObserver.addListenerTo(controller.getNavigationNode());
			hasListener = true;
		}
	}

	/**
	 * Return the SubApplicationController instance for this presentation.
	 * 
	 * @return a SubApplicationController instance or null
	 */
	private SubApplicationController getSubApplicationController() {
		SubApplicationController result = null;
		IWorkbenchPage page = getActivePage();
		if (page != null) {
			String id = page.getPerspective().getId();
			ISubApplicationNode subApplication = SwtViewProviderAccessor.getViewProvider().getNavigationNode(id,
					ISubApplicationNode.class);
			result = (SubApplicationController) subApplication.getNavigationNodeController();
		}
		return result;
	}

	/**
	 * Returns true if the given part is the navigation tree.
	 */
	private boolean isNavigation(IPresentablePart part) {
		return part.getPartProperty(PROPERTY_NAVIGATION) != null;
	}

	/**
	 * Returns true if the given part is the status line.
	 */
	private boolean isStatusLine(IPresentablePart part) {
		return part.getPartProperty(PROPERTY_STATUSLINE) != null;
	}

	/**
	 * Redraws the custom sub module title contained in the parent
	 */
	private void redrawSubModuleTitle() {
		if (parent != null && !parent.isDisposed()) {
			parent.redraw();
		}
	}

}
