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
package org.eclipse.riena.navigation.ui.swt.component;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.riena.navigation.IApplicationModel;
import org.eclipse.riena.navigation.ISubApplication;
import org.eclipse.riena.navigation.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.navigation.ui.swt.lnf.LnfManager;
import org.eclipse.riena.navigation.ui.swt.lnf.rienadefault.SubApplicationSwitcherRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

public class SubApplicationSwitcherWidget extends Canvas {

	private List<SubApplicationItem> items;
	private IApplicationModel applicationModel;

	public SubApplicationSwitcherWidget(Composite parent, int style, IApplicationModel applicationModel) {

		super(parent, style | SWT.DOUBLE_BUFFERED);
		this.applicationModel = applicationModel;
		items = new ArrayList<SubApplicationItem>();
		registerItems();

		addMouseListener(new TabSelector());
		addPaintListener(new PaintListener() {

			/**
			 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
			 */
			public void paintControl(PaintEvent e) {
				GC gc = e.gc;
				getRenderer().setBounds(getParent().getBounds());
				getRenderer().setItems(getItems());
				getRenderer().paint(gc, null);
			}

		});
	}

	/**
	 * Prototype for riena subApplicationTabs
	 */
	private class TabSelector extends MouseAdapter {

		/**
		 * @see org.eclipse.swt.events.MouseAdapter#mouseDown(org.eclipse.swt.events.MouseEvent)
		 */
		@Override
		public void mouseDown(MouseEvent e) {

			SubApplicationItem item = getItem(new Point(e.x, e.y));
			if (item != null) {
				item.getSubApplicationNode().activate();
				redraw();
			}

		}

	}

	/**
	 * Returns the sub-application at the given point.
	 * 
	 * @param point -
	 *            point over sub-application item
	 * @return module item; or null, if not item was found
	 */
	private SubApplicationItem getItem(Point point) {

		for (SubApplicationItem item : getItems()) {
			if (item.getBounds().contains(point)) {
				return item;
			}
		}

		return null;

	}

	private List<SubApplicationItem> getItems() {
		return items;
	}

	private void registerItems() {

		List<ISubApplication> subApps = getApplicationModel().getChildren();
		for (ISubApplication subApp : subApps) {
			SubApplicationItem item = new SubApplicationItem(this, subApp);
			getItems().add(item);
		}

	}

	private IApplicationModel getApplicationModel() {
		return applicationModel;
	}

	private SubApplicationSwitcherRenderer getRenderer() {
		return (SubApplicationSwitcherRenderer) LnfManager.getLnf().getRenderer(
				ILnfKeyConstants.SUB_APPLICATION_SWITCHER_RENDERER);
	}

}