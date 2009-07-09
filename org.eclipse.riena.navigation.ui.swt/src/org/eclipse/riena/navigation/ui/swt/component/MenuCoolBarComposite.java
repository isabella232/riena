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
package org.eclipse.riena.navigation.ui.swt.component;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import org.eclipse.riena.ui.ridgets.swt.MenuManagerHelper;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * This composites has a list of the top-level menus of the Riena menu bar (a
 * cool bar with an item for every top-level menu).
 */
public class MenuCoolBarComposite extends Composite {

	private CoolItem coolItem;
	private ToolBar toolBar;

	/**
	 * Restore focus to this control if ESC is pressed; could be null or
	 * disposed
	 */
	private Control savedFocusControl;

	/**
	 * Creates an new instance of {@code MenuCoolBarComposite} given its parent
	 * and a style value describing its behavior and appearance.
	 * 
	 * @param parent
	 *            - a composite which will be the parent of the new instance
	 *            (cannot be null)
	 * @param style
	 *            - the style of widget to construct
	 */
	public MenuCoolBarComposite(Composite parent, int style) {
		super(parent, style);
		create();
	}

	/**
	 * Initializes the given cool bar.<br>
	 * E.g. sets the background of the cool bar and if the cool bar is empty
	 * adds necessary cool item with the height 1.
	 * 
	 * @param coolBar
	 *            - cool bar
	 * @return the first cool item of the given cool bar
	 */
	public static CoolItem initCoolBar(CoolBar coolBar) {
		if (coolBar.getItemCount() == 0) {
			CoolItem coolItem = new CoolItem(coolBar, SWT.DROP_DOWN);
			ToolBar toolBar = new ToolBar(coolBar, SWT.FLAT);
			coolItem.setControl(toolBar);
			// sets the default size of an empty menu bar or tool bar
			coolItem.setSize(new Point(0, 1));
		}

		coolBar.setBackground(getCoolbarBackground());
		coolBar.setBackgroundMode(SWT.INHERIT_FORCE);
		coolBar.setLocked(true);

		return coolBar.getItem(0);
	}

	/**
	 * Creates a top-level menu and adds it to the Riena menu bar.
	 * 
	 * @param menuManager
	 */
	public ToolItem createAndAddMenu(MenuManager menuManager) {
		ToolItem toolItem = new ToolItem(toolBar, SWT.CHECK);
		SWTBindingPropertyLocator.getInstance().setBindingProperty(toolItem, menuManager.getId());
		toolItem.setText(menuManager.getMenuText());
		MenuManagerHelper helper = new MenuManagerHelper();
		helper.createMenu(toolBar, toolItem, menuManager);
		calcSize(coolItem);
		return toolItem;
	}

	public List<ToolItem> getTopLevelItems() {
		ToolItem[] toolItems = toolBar.getItems();
		return Arrays.asList(toolItems);
	}

	// helping methods
	//////////////////

	/**
	 * Calculates and sets the size of the given cool item.
	 * 
	 * @param item
	 *            - item of cool bar
	 */
	private void calcSize(CoolItem item) {
		Control control = item.getControl();
		Point pt = control.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		pt = item.computeSize(pt.x, pt.y);
		item.setSize(pt);
	}

	/**
	 * Creates the cool and tool bar. These build the menu bar of the Riena
	 * sub-application.
	 */
	private void create() {
		CoolBar coolBar = new CoolBar(this, SWT.FLAT);
		coolItem = initCoolBar(coolBar);

		toolBar = (ToolBar) coolItem.getControl();
		toolBar.addMouseMoveListener(new ToolBarMouseListener());
		toolBar.addKeyListener(new ToolBarKeyListener());
		new FocusListener(this);
	}

	/**
	 * Return the coolbar / menubar background color according to the
	 * look-and-feel.
	 */
	private static Color getCoolbarBackground() {
		return LnfManager.getLnf().getColor(LnfKeyConstants.COOLBAR_BACKGROUND);
	}

	// helping classes
	//////////////////

	/**
	 * If the mouse moves over an unselected item of the tool bar and another
	 * item was selected, deselect the other item and select the item below the
	 * mouse pointer.<br>
	 * <i>Does not work, if menu is visible.</i>
	 */
	private static final class ToolBarMouseListener implements MouseMoveListener {
		public void mouseMove(MouseEvent e) {
			if (e.getSource() instanceof ToolBar) {
				ToolBar toolBar = (ToolBar) e.getSource();

				ToolItem selectedItem = null;
				ToolItem[] items = toolBar.getItems();
				for (int i = 0; i < items.length; i++) {
					if (items[i].getSelection()) {
						selectedItem = items[i];
					}
				}

				ToolItem hoverItem = toolBar.getItem(new Point(e.x, e.y));
				if (hoverItem != null) {
					if (!hoverItem.getSelection() && (selectedItem != null)) {
						selectedItem.setSelection(false);
						hoverItem.setSelection(true);
					}
				}
			}
		}
	}

	/**
	 * When ESC is released, this listener restores the focus to the control
	 * that had it before the menu bar became focused.
	 */
	private final class ToolBarKeyListener extends KeyAdapter {
		@Override
		public void keyReleased(KeyEvent e) {
			if (e.keyCode == 27) { // 27 is ESC
				if (!SwtUtilities.isDisposed(savedFocusControl)) {
					savedFocusControl.setFocus();
				}
			}
		}
	}

	/**
	 * Keeps track of the last focused control within this window. Will irgonre
	 * the focused control if it is the menu toolbar.
	 */
	private final class FocusListener implements Listener {
		FocusListener(Control control) {
			control.getDisplay().addFilter(SWT.FocusIn, this);
			control.addDisposeListener(new DisposeListener() {
				public void widgetDisposed(DisposeEvent event) {
					event.display.removeFilter(SWT.FocusIn, FocusListener.this);
				}
			});
		}

		public void handleEvent(Event event) {
			if (event.widget != toolBar && event.widget instanceof Control) {
				Control control = (Control) event.widget;
				if (contains(toolBar.getShell(), control)) {
					savedFocusControl = control;
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
