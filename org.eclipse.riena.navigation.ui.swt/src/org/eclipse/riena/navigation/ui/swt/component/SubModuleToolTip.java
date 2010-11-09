/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/

// TODO [ev] class moved - document API change in wiki and remove

//package org.eclipse.riena.navigation.ui.swt.component;
//
//import org.eclipse.jface.window.DefaultToolTip;
//import org.eclipse.swt.custom.CLabel;
//import org.eclipse.swt.graphics.Color;
//import org.eclipse.swt.graphics.Font;
//import org.eclipse.swt.graphics.Point;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.swt.widgets.Event;
//import org.eclipse.swt.widgets.Tree;
//import org.eclipse.swt.widgets.TreeItem;
//
//import org.eclipse.riena.navigation.INavigationNode;
//import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
//import org.eclipse.riena.ui.swt.lnf.LnfManager;
//import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;
//
///**
// * ToolTip for the tree items in the tree of the sub-modules.<br>
// * ToolTip is only displayed if the text of the tree item was clipped.
// */
//public class SubModuleToolTip extends DefaultToolTip {
//
//	private Tree tree;
//
//	/**
//	 * Creates new instance which add TooltipSupport to the tree.
//	 * 
//	 * @param tree
//	 *            the tree on whose action the tooltip is shown
//	 */
//	public SubModuleToolTip(final Tree tree) {
//		super(tree);
//		setTree(tree);
//		setShift(new Point(0, 0));
//	}
//
//	/**
//	 * Initializes the look (color and font) and feel (popup delay) of the tool
//	 * tip. Uses the settings of the look and feel.
//	 */
//	private void initLookAndFeel() {
//
//		final RienaDefaultLnf lnf = LnfManager.getLnf();
//
//		final Integer delay = lnf.getIntegerSetting(LnfKeyConstants.SUB_MODULE_ITEM_TOOLTIP_POPUP_DELAY);
//		if (delay != null) {
//			setPopupDelay(delay);
//		}
//		Color color = lnf.getColor(LnfKeyConstants.SUB_MODULE_ITEM_TOOLTIP_FOREGROUND);
//		if (color != null) {
//			setForegroundColor(color);
//		}
//		color = lnf.getColor(LnfKeyConstants.SUB_MODULE_ITEM_TOOLTIP_BACKGROUND);
//		if (color != null) {
//			setBackgroundColor(color);
//		}
//		final Font font = lnf.getFont(LnfKeyConstants.SUB_MODULE_ITEM_TOOLTIP_FONT);
//		if (font != null) {
//			setFont(font);
//		}
//
//	}
//
//	/**
//	 * @see org.eclipse.jface.window.ToolTip#createToolTipContentArea(org.eclipse.swt.widgets.Event,
//	 *      org.eclipse.swt.widgets.Composite)
//	 */
//	@Override
//	protected Composite createToolTipContentArea(final Event event, final Composite parent) {
//
//		final CLabel label = new CLabel(parent, getStyle(event));
//
//		final Color fgColor = getForegroundColor(event);
//		final Color bgColor = getBackgroundColor(event);
//		final Font font = getFont(event);
//
//		if (fgColor != null) {
//			label.setForeground(fgColor);
//		}
//
//		if (bgColor != null) {
//			label.setBackground(bgColor);
//		}
//
//		if (font != null) {
//			label.setFont(font);
//		}
//
//		label.setText(getItemLongText(event));
//
//		return label;
//
//	}
//
//	/**
//	 * @see org.eclipse.jface.window.ToolTip#shouldCreateToolTip(org.eclipse.swt.widgets.Event)
//	 */
//	@Override
//	protected boolean shouldCreateToolTip(final Event event) {
//
//		boolean should = super.shouldCreateToolTip(event);
//
//		if (should) {
//			initLookAndFeel();
//			final String text = getItemText(event);
//			final String longText = getItemLongText(event);
//			should = !text.equals(longText);
//		}
//
//		return should;
//
//	}
//
//	/**
//	 * @see org.eclipse.jface.window.ToolTip#getLocation(org.eclipse.swt.graphics.Point,
//	 *      org.eclipse.swt.widgets.Event)
//	 */
//	@Override
//	public Point getLocation(final Point tipSize, final Event event) {
//
//		Point location = super.getLocation(tipSize, event);
//		final TreeItem item = getTreeItem(event);
//		if (item != null) {
//			location = getTree().toDisplay(item.getBounds().x, item.getBounds().y);
//		}
//
//		return location;
//
//	}
//
//	/**
//	 * @return the tree
//	 */
//	private Tree getTree() {
//		return tree;
//	}
//
//	/**
//	 * @param tree
//	 *            the tree to set
//	 */
//	private void setTree(final Tree tree) {
//		this.tree = tree;
//	}
//
//	/**
//	 * Returns original (not clipped) text of the item at the given point (
//	 * <code>event.x</code> and <code>event.x</code>) or null if no such item
//	 * exists.
//	 * 
//	 * @param event
//	 *            event with the x- and y-position of the mouse pointer
//	 * @return original (not clipped) item text
//	 */
//	protected String getItemLongText(final Event event) {
//
//		final TreeItem item = getTreeItem(event);
//		String longText = ""; //$NON-NLS-1$
//		if (item != null) {
//			final INavigationNode<?> subModule = (INavigationNode<?>) item.getData();
//			longText = subModule.getLabel();
//		}
//
//		return longText;
//
//	}
//
//	/**
//	 * Returns text of the item at the given point (<code>event.x</code> and
//	 * <code>event.x</code>) or null if no such item exists.
//	 * 
//	 * @param event
//	 *            event with the x- and y-position of the mouse pointer
//	 * @return item text
//	 */
//	protected String getItemText(final Event event) {
//
//		String text = ""; //$NON-NLS-1$
//		final TreeItem item = getTreeItem(event);
//		if (item != null) {
//			text = item.getText();
//		}
//		return text;
//
//	}
//
//	/**
//	 * Returns the item at the given point (<code>event.x</code> and
//	 * <code>event.x</code>) or null if no such item exists.
//	 * 
//	 * @param event
//	 *            event with the x- and y-position of the mouse pointer
//	 * @return the item at the given point, or null if the point is not in a
//	 *         selectable item
//	 */
//	protected TreeItem getTreeItem(final Event event) {
//
//		final Point point = new Point(event.x, event.y);
//		return getTree().getItem(point);
//
//	}
//
//}
