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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.riena.navigation.model.SubModuleNode;
import org.eclipse.riena.navigation.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.navigation.ui.swt.lnf.LnfManager;
import org.eclipse.riena.navigation.ui.swt.lnf.renderer.ModuleGroupRenderer;
import org.eclipse.riena.navigation.ui.swt.lnf.renderer.SubApplicationTabRenderer;
import org.eclipse.riena.navigation.ui.swt.lnf.renderer.SubModuleViewRenderer;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManagerAccessor;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtViewId;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.internal.presentations.PresentablePart;
import org.eclipse.ui.presentations.IPresentablePart;
import org.eclipse.ui.presentations.IStackPresentationSite;
import org.eclipse.ui.presentations.StackDropResult;
import org.eclipse.ui.presentations.StackPresentation;

/**
 * <pre>
 * +-----------------------------------------------------------------+
 * |                                                                 |
 * |               *1 +---------+---------+                          |
 * |                  | SubApp1 | SubApp2 |                          |
 * +-----------------------------------------------------------------+
 * | *2           *3                                                 |
 * | +---------+  +------------------------------------------------+ |
 * | | Module1 |  | Module1 - Sub1                                 | |
 * | +---------+  +------------------------------------------------+ |
 * | | o Sub1  |  |                                                | |
 * | | o Sub2  |  | *4                                             | |
 * | +---------+  |                                                | |
 * | | Module2 |  |                                                | |
 * | +---------+  |                                                | |
 * | +---------+  |                                                | |
 * | | Module2 |  |                                                | |
 * | +---------+  |                                                | |
 * |              |                                                | |
 * |              |                                                | |
 * |              +------------------------------------------------+ |
 * +-----------------------------------------------------------------+
 * 
 * legend: *1 - sub-application switcher
 *         *2 - navigation
 *         *3 - sub-module view (with title bar and border)
 *         *4 - content area of the sub-module view
 * </pre>
 */
public class TitlelessStackPresentation extends StackPresentation {

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
	 * Height of the sub-application switcher
	 */
	private static final int SUB_APPLICATION_SWITCHER_HEIGHT = 65;

	/**
	 * Property to distinguish the view of the navigation.
	 */
	public static final String PROPERTY_NAVIGATION = "navigation"; //$NON-NLS-1$
	/**
	 * Property to distinguish the view of the sub-application switcher.
	 */
	public static final String PROPERTY_SUB_APPLICATION_SWITCHER = "subApplicationSwitcher"; //$NON-NLS-1$

	private Control current;
	private Control navigation;
	private Control subApplicationSwitcher;
	private Composite parent;
	private SubModuleViewRenderer renderer;

	private Map<Control, SwtViewId> parts = new HashMap<Control, SwtViewId>();

	public TitlelessStackPresentation(Composite parent, IStackPresentationSite stackSite) {
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
		} else if (toSelect.getPartProperty(PROPERTY_SUB_APPLICATION_SWITCHER) != null) {
			// show applications
			subApplicationSwitcher = toSelect.getControl();
		} else {
			if (!isCurrentDisposed()) {
				current.setVisible(false);
			}
			current = toSelect.getControl();
			parts.put(current, getViewId((PresentablePart) toSelect));
		}
		updateBounds();
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
		if (navigation != null) {
			updateControl(navigation, calcNavigationBounds());
		}
		if (subApplicationSwitcher != null) {
			updateControl(subApplicationSwitcher, calcSubApplicationSwitcherBounds());
		}

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
		int tabHeight = calcTabHeight();

		int x = PADDING_LEFT;
		int y = tabHeight + PADDING_TOP;
		int width = size.x;
		int height = parent.getBounds().height - tabHeight - PADDING_BOTTOM - PADDING_TOP;

		return new Rectangle(x, y, width, height);

	}

	/**
	 * Returns the height of a tab.
	 * 
	 * @return tab height.
	 */
	public static int calcTabHeight() {

		GC gc = new GC(Display.getCurrent());

		// Get the renderer of tab
		SubApplicationTabRenderer renderer = (SubApplicationTabRenderer) LnfManager.getLnf().getRenderer(
				ILnfKeyConstants.SUB_APPLICATION_TAB_RENDERER);

		renderer.setActivated(true);
		int tabHeight = renderer.computeSize(gc, null).y;
		tabHeight += SubApplicationTabRenderer.ACTIVE_Y_OFFSET;
		gc.dispose();

		return tabHeight;

	}

	/**
	 * Calculates the bounds of the switcher of the sub-applications.
	 * 
	 * @return bounds of switcher of sub-applications
	 */
	private Rectangle calcSubApplicationSwitcherBounds() {

		int x = 0;
		int y = 0;
		int width = parent.getBounds().width;
		int height = calcTabHeight();

		return new Rectangle(x, y, width, height);

	}

	/**
	 * @see org.eclipse.ui.presentations.StackPresentation#setActive(int)
	 */
	@Override
	public void setActive(int newState) {

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
		if (subApplicationSwitcher != null) {
			subApplicationSwitcher.setVisible(false);
		}
		parent.setVisible(false);

	}

	/**
	 * Updates the bounds of the given control.<br>
	 * 
	 * @param ctrl -
	 *            control
	 * @param bounds -
	 *            new bounds
	 */
	private void updateControl(Control ctrl, Rectangle bounds) {

		if (!ctrl.getBounds().equals(bounds)) {
			ctrl.setVisible(false);
			ctrl.setBounds(bounds);
		}
		ctrl.setVisible(true);

	}

	/**
	 * @see org.eclipse.ui.presentations.StackPresentation#setBounds(org.eclipse.swt.graphics.Rectangle)
	 */
	@Override
	public void setBounds(Rectangle bounds) {
		parent.setBounds(bounds);
		updateBounds();
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

}
