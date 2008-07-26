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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.riena.navigation.ISubApplication;
import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubApplicationViewController;
import org.eclipse.riena.navigation.ui.swt.binding.DefaultSwtControlRidgetMapper;
import org.eclipse.riena.navigation.ui.swt.lnf.renderer.ModuleGroupRenderer;
import org.eclipse.riena.navigation.ui.swt.lnf.renderer.SubModuleViewRenderer;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManagerAccessor;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtViewId;
import org.eclipse.riena.navigation.ui.swt.views.GrabCorner;
import org.eclipse.riena.ui.ridgets.uibinding.DefaultBindingManager;
import org.eclipse.riena.ui.swt.Statusbar;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
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
import org.eclipse.ui.internal.presentations.PresentablePart;
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
 * 
 * @deprecated use {@link TitlelessStackPresentation}
 */
// TODO [ev] delete later
public class TitlelessStackPresentationOrg extends StackPresentation {

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
	public static final int PADDING_TOP = 10;
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
	 * The height of the status bar
	 */
	private static final int STATUSLINE_HIGHT = 22;

	/**
	 * Property to distinguish the view of the navigation.
	 */
	public static final String PROPERTY_NAVIGATION = "navigation"; //$NON-NLS-1$
	/**
	 * Property to distinguish the view of the status line.
	 */
	public static final String PROPERTY_STATUSLINE = "statusLine"; //$NON-NLS-1$

	private IPresentablePart currentPP;
	private Control current;
	private Control navigation;
	private Control statusLine;
	private Composite parent;
	private SubModuleViewRenderer renderer;

	private Map<Control, SwtViewId> parts = new HashMap<Control, SwtViewId>();

	public TitlelessStackPresentationOrg(Composite parent, IStackPresentationSite stackSite) {
		super(stackSite);
		this.parent = parent;
		createSubModuleViewArea();
	}

	/**
	 * Creates the area within witch the view of the current active sub-module
	 * is displayed.
	 */
	private void createSubModuleViewArea() {

		parent.setBackground(LnfManager.getLnf().getColor(ILnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.addPaintListener(new PaintListener() {

			/**
			 * Paints the border and the title bar of the current active
			 * sub-module.
			 */
			public void paintControl(PaintEvent e) {

				if (!isCurrentDisposed() && current.isVisible()) {
					if (getRenderer() != null) {
						getRenderer().setBounds(calcSubModuleOuterBounds());
						SwtViewId viewId = parts.get(current);
						// boolean db = (current.getStyle() &
						// SWT.DOUBLE_BUFFERED) == SWT.DOUBLE_BUFFERED;
						// if (!db) {
						// System.out.println(".paintControl() " +
						// parent.getBounds() + " / " + parent.hashCode());
						// System.out.println(".paintControl() " +
						// current.getBounds() + " / " + current.hashCode());
						// }
						current.setBackground(LnfManager.getLnf().getColor(ILnfKeyConstants.SUB_MODULE_BACKGROUND));
						SubModuleNode node = SwtPresentationManagerAccessor.getManager().getNavigationNode(
								viewId.getId(), viewId.getSecondary(), SubModuleNode.class);
						getRenderer().paint(e.gc, node);
					}
				}

			}
		});

	}

	/**
	 * @see org.eclipse.ui.presentations.StackPresentation#addPart(org.eclipse.ui.presentations.IPresentablePart,
	 *      java.lang.Object)
	 */
	@Override
	public void addPart(IPresentablePart newPart, Object cookie) {
	}

	/**
	 * @see org.eclipse.ui.presentations.StackPresentation#removePart(org.eclipse.ui.presentations.IPresentablePart)
	 */
	@Override
	public void removePart(IPresentablePart oldPart) {
	}

	/**
	 * @see org.eclipse.ui.presentations.StackPresentation#dispose()
	 */
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

	/**
	 * @see org.eclipse.ui.presentations.StackPresentation#selectPart(org.eclipse.ui.presentations.IPresentablePart)
	 */
	@Override
	public void selectPart(IPresentablePart toSelect) {
		if (toSelect.getControl() == current) {
			return;
		}
		if (toSelect.getPartProperty(PROPERTY_NAVIGATION) != null) {
			// show navigation tree
			navigation = toSelect.getControl();
		} else if (toSelect.getPartProperty(PROPERTY_STATUSLINE) != null) {
			// show status line
			statusLine = toSelect.getControl();
			// TODO - is this the correct place ???
			if (getActivePage() != null) {
				String perspectiveId = getActivePage().getPerspective().getId();
				ISubApplication node = SwtPresentationManagerAccessor.getManager().getNavigationNode(perspectiveId,
						ISubApplication.class);
				SubApplicationViewController controller = (SubApplicationViewController) node.getPresentation();
				DefaultBindingManager defaultBindingManager = createBindingManager();
				List<Object> uiControls = new ArrayList<Object>(1);
				uiControls.add(getStatusLineWidget(statusLine));
				defaultBindingManager.injectRidgets(controller, uiControls);
				defaultBindingManager.bind(controller, uiControls);
			}
		} else {
			if (!isCurrentDisposed()) {
				current.setVisible(false);
			}
			currentPP = toSelect;
			current = toSelect.getControl();
			parts.put(current, getViewId((PresentablePart) toSelect));
		}
		updateBounds();
	}

	private Statusbar getStatusLineWidget(Control parent) {

		if (parent instanceof Statusbar) {
			return (Statusbar) parent;
		} else {
			if (parent instanceof Composite) {
				Control[] children = ((Composite) parent).getChildren();
				for (int i = 0; i < children.length; i++) {
					return getStatusLineWidget(children[i]);
				}
			}
		}

		return null;

	}

	/**
	 * Creates the <code>SwtViewId</code> for the given part.
	 * 
	 * @param part
	 * @return ID of a SWT view.
	 */
	private SwtViewId getViewId(PresentablePart part) {

		String compoundId = part.getPane().getCompoundId();
		return new SwtViewId(compoundId);

	}

	/**
	 * Updates the bounds of all three parts (controls) of this navigation.
	 */
	private void updateBounds() {

		if (!isCurrentDisposed()) {
			GC gc = new GC(current);
			Rectangle innerBounds = getRenderer().computeInnerBounds(gc, calcSubModuleOuterBounds());
			gc.dispose();
			updateControl(current, innerBounds);
		}
		updateControl(navigation, calcNavigationBounds());
		updateControl(statusLine, calcStatusLineBounds());

	}

	/**
	 * Calculates the (outer) bounds of the sub-module view.
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
		Point size = getModuleGroupRenderer().computeSize(gc, SWT.DEFAULT, SWT.DEFAULT);
		gc.dispose();

		int x = PADDING_LEFT;
		int y = PADDING_TOP;
		int width = size.x;
		int height = parent.getBounds().height - PADDING_BOTTOM - PADDING_TOP;
		height -= STATUSLINE_HIGHT;

		return new Rectangle(x, y, width, height);

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
		Rectangle bounds = new Rectangle(x, y, width, height);

		return bounds;

	}

	/**
	 * @see org.eclipse.ui.presentations.StackPresentation#setActive(int)
	 */
	@Override
	public void setActive(int newState) {
		new Exception().printStackTrace();
		if (newState != AS_INACTIVE) {
			parent.setVisible(true);
			updateBounds();
		} else {
			hideAll();
		}

	}

	/**
	 * Hides all controls of this presentation.
	 */
	private void hideAll() {

		if (!isCurrentDisposed()) {
			current.setVisible(false);
		}
		if (navigation != null) {
			navigation.setVisible(false);
		}
		if (statusLine != null) {
			statusLine.setVisible(false);
		}
		parent.setVisible(false);

	}

	/**
	 * Updates the bounds of the given control.<br>
	 * 
	 * @param ctrl
	 *            - control
	 * @param bounds
	 *            - new bounds
	 */
	private void updateControl(Control ctrl, Rectangle bounds) {

		if (ctrl == null) {
			return;
		}

		if (!ctrl.getBounds().equals(bounds)) {
			ctrl.setBounds(bounds);
			// ctrl.setRedraw(false);
			// ctrl.setRedraw(true);
		}
		if (!ctrl.isVisible()) {
			ctrl.setVisible(true);
		}

	}

	/**
	 * @see org.eclipse.ui.presentations.StackPresentation#setBounds(org.eclipse.swt.graphics.Rectangle)
	 */
	@Override
	public void setBounds(Rectangle bounds) {
		parent.setBounds(bounds);
		updateBounds();
	}

	private DefaultBindingManager createBindingManager() {
		return new DefaultBindingManager(new SWTBindingPropertyLocator(), new DefaultSwtControlRidgetMapper());
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

	/**
	 * Returns the renderer of the sub-module view.<br>
	 * Renderer renders the border and the title of the sub-module view and not
	 * the content of the view.
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
	 * 
	 * @return renderer of module group
	 */
	private ModuleGroupRenderer getModuleGroupRenderer() {
		return (ModuleGroupRenderer) LnfManager.getLnf().getRenderer(ILnfKeyConstants.MODULE_GROUP_RENDERER);
	}

	private boolean isCurrentDisposed() {
		if (current != null) {
			return current.isDisposed();
		} else {
			return true;
		}
	}

	/**
	 * Returns the currently active page.
	 * 
	 * @return active page
	 */
	private IWorkbenchPage getActivePage() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	}

}
