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

import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * This class provides all scrolling logic for the navigation
 */
public class ScrollingSupport {

	private static final int SCROLLING_STEP = 20;

	private IModuleNavigationComponentProvider navigationComponentProvider;
	// the SWT composite
	private ScrollControlComposite scrollControlComposite;
	// offset of the scrolled composite relative to the main navigation composite
	private int scrolledCompositeOffset = 0;

	// to detect changes..
	private int cachedScrolledCompositeHeight = 0;

	private IModuleGroupNode cachedModuleGroupNode;

	/**
	 * 
	 * @param parent
	 *            - the composite parent
	 * @param flags
	 *            - SWT style flags
	 * @param navigationComponentProvider
	 */
	public ScrollingSupport(Composite parent, int flags, IModuleNavigationComponentProvider navigationComponentProvider) {
		this.navigationComponentProvider = navigationComponentProvider;
		scrollControlComposite = new ScrollControlComposite(parent, flags);
		setBodyCompositeOffset(0);
		initMouseWheelObserver();
	}

	private void initMouseWheelObserver() {
		Display display = getNavigationComponent().getDisplay();
		display.addFilter(SWT.MouseWheel, new MouseWheelAdapter());
	}

	private class MouseWheelAdapter implements Listener {

		// for saving last event time
		private int lastEventTime = 0;

		public void handleEvent(Event event) {
			// only go further if the event has a new time stamp
			if (scrollingRequired() && event.time > lastEventTime) {
				lastEventTime = event.time;
				Rectangle navigationComponentBounds = getNavigationComponent().getBounds();

				// convert navigation bounds relative to display
				Point navigationPtAtDisplay = getNavigationComponent().toDisplay(0, 0);
				navigationComponentBounds.x = navigationPtAtDisplay.x;
				navigationComponentBounds.y = navigationPtAtDisplay.y;

				if (event.widget instanceof Control) {
					Control widget = (Control) event.widget;
					// convert widget event point relative to display
					Point evtPt = widget.toDisplay(event.getBounds().x, event.getBounds().y);
					// now check if inside navigation
					if (navigationComponentBounds.contains(evtPt.x, evtPt.y)) {
						if (event.count > 0) {
							scrollUp();
							return;
						}
						scrollDown();
					}

				}
			}
		}
	}

	/**
	 * 
	 * @return - the scrolled composite which is placed relative to the
	 *         navigation main composite
	 */
	public Composite getScrollComposite() {
		return scrollControlComposite;
	}

	/**
	 * scrolls up
	 */
	private void scrollUp() {
		int offset = Math.min(scrolledCompositeOffset + SCROLLING_STEP, 0);
		setBodyCompositeOffset(offset);
	}

	/**
	 * scroll down
	 */
	private void scrollDown() {
		int offset = Math.max(getNavigationComponentHeight() - getScrolledComponentHeight(), scrolledCompositeOffset
				- SCROLLING_STEP);
		setBodyCompositeOffset(offset);
	}

	/**
	 * scrolls to the active {@link ModuleGroupView}. Delegates to
	 * {@link #checkScrollToActiveModuleGroup()} for technical details
	 */
	public void autoScrolling() {
		scrollControlComposite.setVisible(scrollingRequired());
		//delegation
		checkScrollToActiveModuleGroup();
	}

	/**
	 * determines if scrolling is needed
	 * 
	 * @return - true if we need scrolling
	 */
	public boolean scrollingRequired() {
		return getScrolledComponentHeight() > getNavigationComponentHeight();
	}

	/**
	 * core implementation of scrolling to the active {@link ModuleGroupView}
	 * [part1]. should not be called directly. use {@link #autoScrolling()}
	 */
	private void checkScrollToActiveModuleGroup() {
		if (!scrollingRequired()) {
			resetScrolling();
			// nothing to be done
			return;
		}
		if (!isActiveModuleGroupViewVisible()) {
			//active not visible
			scrollToActiveModuleGroup();
		} else {
			ensureFilledToBottom();
		}
	}

	private void ensureFilledToBottom() {
		// use height
		int bodyBottomY = scrolledCompositeOffset + getScrolledComponentHeight();
		if (bodyBottomY < getNavigationComponentHeight()) {
			setBodyCompositeOffset(scrolledCompositeOffset + (getNavigationComponentHeight() - bodyBottomY));
		}
	}

	public void scroll() {
		IModuleGroupNode activeModuleGroupNode = navigationComponentProvider.getActiveModuleGroupNode();
		if (scrollingRequired()) {
			if (activeModuleGroupChanged(activeModuleGroupNode)) {
				autoScrolling();
				cachedModuleGroupNode = activeModuleGroupNode;
			} else {
				//height changed
				if (getScrolledComponentHeight() != cachedScrolledCompositeHeight) {
					scrollControlComposite.setVisible(true);
				}
				ensureFilledToBottom();
			}
		} else {
			if (activeModuleGroupChanged(activeModuleGroupNode)) {
				cachedModuleGroupNode = activeModuleGroupNode;
			}
			resetScrolling();
		}
		cachedScrolledCompositeHeight = getScrolledComponentHeight();
	}

	private boolean activeModuleGroupChanged(IModuleGroupNode activeModuleGroupNode) {
		return activeModuleGroupNode != null && cachedModuleGroupNode != activeModuleGroupNode;
	}

	public void resetScrolling() {
		setBodyCompositeOffset(0);
		scrollControlComposite.setVisible(false);
	}

	/**
	 * core implementation of scrolling to the active {@link ModuleGroupView}
	 * [part2]. should not be called directly. use {@link #autoScrolling()}
	 */
	private void scrollToActiveModuleGroup() {
		calculateActiveModuleGrouViewBounds();
		Point activeModuleGroupPosition = getActiveModuleGroupPosition();
		int offset;
		if (calculateActiveModuleGrouViewBounds().height < getNavigationComponentHeight()) {
			boolean scrollDownRequired = activeModuleGroupPosition.y < 0;
			if (scrollDownRequired) {
				offset = Math.max(0, scrolledCompositeOffset - activeModuleGroupPosition.y);
			} else {
				offset = scrolledCompositeOffset
						- (activeModuleGroupPosition.y + calculateActiveModuleGrouViewBounds().height - getNavigationComponentHeight());
			}
		} else {
			offset = -calculateActiveModuleGrouViewBounds().y;
		}
		setBodyCompositeOffset(offset);
	}

	//
	//// Convenience

	private Composite getNavigationComponent() {
		return navigationComponentProvider.getNavigationComponent();
	}

	private Composite getScrolledComponent() {
		return navigationComponentProvider.getScrolledComponent();
	}

	private int getNavigationComponentHeight() {
		return getNavigationComponent().getBounds().height;
	}

	private int getScrolledComponentHeight() {
		return getScrolledComponent().getBounds().height;
	}

	private Point getModuleGroupPosition(Rectangle bounds) {
		// the position is the position inside the body composite -> translate it to naviComposite
		Point position = new Point(bounds.x, bounds.y);
		// the position relative to main navigation composite
		position = getNavigationComponent().toControl(getScrolledComponent().toDisplay(position));
		return position;
	}

	private Rectangle calculateActiveModuleGrouViewBounds() {
		IModuleGroupNode activeModuleGroup = navigationComponentProvider.getActiveModuleGroupNode();
		if (activeModuleGroup != null) {
			return calculateModuleGroupViewBounds(activeModuleGroup);
		}
		return null;
	}

	private Rectangle calculateModuleGroupViewBounds(IModuleGroupNode moduleGroupNode) {
		ModuleGroupView activeModuleGroupView = navigationComponentProvider.getModuleGroupViewForNode(moduleGroupNode);
		return activeModuleGroupView.getBounds();
	}

	/**
	 * 
	 * @return - true if the active {@link ModuleGroupView} is visible inside
	 *         the navigation or false if it is hidden because of scrolling
	 */
	private boolean isActiveModuleGroupViewVisible() {
		Point activeModuleGroupPosition = getActiveModuleGroupPosition();
		return isModuleGroupViewVisible(activeModuleGroupPosition, calculateActiveModuleGrouViewBounds());
	}

	private boolean isModuleGroupViewVisible(Point moduleGroupViewPosition, Rectangle moduleGroupViewBounds) {
		return !(moduleGroupViewPosition.y < 0 || moduleGroupViewPosition.y + moduleGroupViewBounds.height > getNavigationComponentHeight());
	}

	/**
	 * calculates the position of the active {@link ModuleGroupView} relative to
	 * the main navigation component
	 * 
	 * @return - the point representing the position
	 */
	private Point getActiveModuleGroupPosition() {
		Rectangle activeMgVerticalBounds = calculateActiveModuleGrouViewBounds();
		return getModuleGroupPosition(activeMgVerticalBounds);
	}

	//
	//// End Convenience

	/**
	 * this method moves the scrolled component upwards and downwards for
	 * simulation of scrolling
	 * 
	 * @param yScrolledOffset
	 *            - the new vertical offset of the scrolled composite
	 */
	private void setBodyCompositeOffset(int yScrolledOffset) {
		scrolledCompositeOffset = yScrolledOffset;
		updateUI();
	}

	private void updateUI() {
		FormData formData = new FormData();
		formData.top = new FormAttachment(0, scrolledCompositeOffset);
		getScrolledComponent().setLayoutData(formData);
		updateLayout();
	}

	private void updateLayout() {
		//		getScrolledComponent().layout();
		getScrolledComponent().getParent().layout();
	}

	/**
	 * this composite contains the "up"- and "down" buttons. Manual scrolling is
	 * triggered here!
	 */
	private class ScrollControlComposite extends Composite {

		private Button upButton;
		private Button downButton;

		public ScrollControlComposite(Composite parent, int style) {
			super(parent, style);
			getNavigationComponent().addControlListener(new NavigationResizeListener());
			setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.NAVIGATION_BACKGROUND));
			initControlButtons();
		}

		/*
		 * defines the layout
		 */
		private void initControlButtons() {
			setLayout(new FormLayout());

			ScrollDelegation delegationAdaper = new ScrollDelegation();

			/*
			 * TODO The background of the buttons cannot be set on windows. See
			 * platform bug 64957. Awaiting Riena Button styling..
			 */
			upButton = new Button(this, SWT.NONE);
			upButton.setImage(LnfManager.getLnf().getImage(LnfKeyConstants.NAVIGATION_SCROLL_UP_ICON));
			upButton.addSelectionListener(delegationAdaper);
			layoutUpButton();

			downButton = new Button(this, SWT.NONE);
			downButton.setImage(LnfManager.getLnf().getImage(LnfKeyConstants.NAVIGATION_SCROLL_DOWN_ICON));
			downButton.addSelectionListener(delegationAdaper);
			layoutDownButton();
		}

		private void layoutDownButton() {
			downButton.setText(""); //$NON-NLS-1$

			FormData fd = new FormData();
			fd.left = new FormAttachment(upButton, 2);
			fd.height = 12;
			fd.width = 80;
			downButton.setLayoutData(fd);
		}

		private void layoutUpButton() {
			upButton.setText(""); //$NON-NLS-1$

			FormData fd = new FormData();
			fd.left = new FormAttachment(0, 4);
			fd.height = 12;
			fd.width = 80;
			upButton.setLayoutData(fd);
		}

		private class NavigationResizeListener extends ControlAdapter {

			@Override
			public void controlResized(ControlEvent e) {
				setVisible(scrollingRequired());
			}
		}

		/**
		 * action delegation for manual scrolling
		 */
		private class ScrollDelegation extends SelectionAdapter {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.getSource() == upButton) {
					scrollUp();
					return;
				}
				scrollDown();
			}
		}
	}

}