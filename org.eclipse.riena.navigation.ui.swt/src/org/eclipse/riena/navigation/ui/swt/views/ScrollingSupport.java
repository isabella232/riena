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

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;

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

	/**
	 * 
	 * @param parent
	 *            the composite parent
	 * @param flags
	 *            SWT style flags
	 * @param navigationComponentProvider
	 */
	public ScrollingSupport(Composite parent, int flags, IModuleNavigationComponentProvider navigationComponentProvider) {
		this.navigationComponentProvider = navigationComponentProvider;
		scrollControlComposite = new ScrollControlComposite(parent, flags);
		setBodyCompositeOffset(0);
		initMouseWheelObserver(this.navigationComponentProvider.getNavigationComponent());
	}

	/**
	 * Scrolls to the active navigation element
	 */
	public void scroll() {
		scrollControlComposite.setVisible(mayScroll());
		scrollToActive();
	}

	/**
	 * @return the scrolled composite which is placed relative to the navigation
	 *         main composite
	 */
	public Composite getScrollComposite() {
		return scrollControlComposite;
	}

	// helping methods
	//////////////////

	private void initMouseWheelObserver(Composite navigationComponent) {
		final Display display = navigationComponent.getDisplay();
		final MouseWheelAdapter wheelAdapter = new MouseWheelAdapter();
		display.addFilter(SWT.MouseWheel, wheelAdapter);
		navigationComponent.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				display.removeFilter(SWT.MouseWheel, wheelAdapter);
			}
		});
	}

	/**
	 * scrolls up
	 */
	private void scrollUp(int pixels) {
		int offset = Math.min(scrolledCompositeOffset + pixels, 0);
		setBodyCompositeOffset(offset);
	}

	/**
	 * scroll down
	 */
	private void scrollDown(int pixels) {
		int offset = Math.max(getNavigationComponentHeight() - getScrolledComponentHeight(), scrolledCompositeOffset
				- pixels);
		setBodyCompositeOffset(offset);
	}

	private void scrollToActive() {
		if (!mayScroll()) {
			resetScrolling();
			return;
		}
		INavigationNode<?> activeNode = getActiveNode();
		if (!scrollTo(activeNode)) {
			ensureFilledToBottom();
		}
	}

	private boolean scrollTo(INavigationNode<?> activeNode) {
		boolean result = false;
		if (activeNode instanceof IModuleGroupNode) {
			ModuleGroupView view = navigationComponentProvider.getModuleGroupViewForNode((IModuleGroupNode) activeNode);
			result = scrollTo(view, view);
		} else if (activeNode instanceof IModuleNode) {
			result = scrollTo((IModuleNode) activeNode);
		} else if (activeNode instanceof ISubModuleNode) {
			IModuleNode module = (IModuleNode) activeNode.getParent();
			result = scrollTo(module);
		}
		return result;
	}

	private boolean scrollTo(IModuleNode module) {
		boolean result = false;
		if (module != null) {
			ModuleView moduleView = navigationComponentProvider.getModuleViewForNode(module);
			if (moduleView == null) {
				return result;
			}
			boolean isClosed = moduleView.getOpenHeight() == 0;
			if (isClosed) {
				result = scrollTo(moduleView.getTitle(), moduleView.getBody());
			} else {
				int moduleHeight = moduleView.getParent().getSize().y;
				if (moduleHeight < getNavigationComponentHeight()) {
					// only show title if the whole module fits into the available height
					// prevents flicker
					result = scrollTo(moduleView.getTitle(), moduleView.getBody());
				}
				result = result || scrollTo(moduleView.getTree());
			}
		}
		return result;
	}

	private boolean scrollTo(Composite topComp, Composite bottomComp) {
		int compBottomEdge = bottomComp.toDisplay(0, bottomComp.getSize().y).y;
		boolean result = scrollToBottom(compBottomEdge);
		int compTopEdge = topComp.toDisplay(0, 0).y;
		result = result || scrollToTop(compTopEdge);
		return result;
	}

	private boolean scrollTo(Tree tree) {
		boolean result = false;
		if (tree.getSelectionCount() > 0) {
			TreeItem item = tree.getSelection()[0];
			Rectangle itemBounds = item.getBounds();
			int treeBottomEdge = tree.toDisplay(itemBounds.x, itemBounds.y + tree.getItemHeight()).y;
			result = scrollToBottom(treeBottomEdge);
			int treeTopEdge = tree.toDisplay(itemBounds.x, itemBounds.y).y;
			result = result || scrollToTop(treeTopEdge);
		}
		return result;
	}

	private boolean scrollToBottom(int bottomEdgeToDisplay) {
		int bottomEdge = getNavigationComponent().toDisplay(0, getNavigationComponentHeight()).y;
		boolean scroll = bottomEdgeToDisplay > bottomEdge;
		if (scroll) {
			scrollDown(bottomEdgeToDisplay - bottomEdge);
		}
		return scroll;
	}

	private boolean scrollToTop(int topEdgeToDisplay) {
		int topEdge = getNavigationComponent().toDisplay(0, 0).y;
		boolean scroll = topEdgeToDisplay < topEdge;
		if (scroll) {
			scrollUp(topEdge - topEdgeToDisplay);
		}
		return scroll;
	}

	private INavigationNode<?> getActiveNode() {
		IModuleGroupNode group = navigationComponentProvider.getActiveModuleGroupNode();
		IModuleNode module = null;
		ISubModuleNode submodule = null;
		if (group != null) {
			for (IModuleNode candidate : group.getChildren()) {
				if (candidate.isActivated()) {
					module = candidate;
					break;
				}
			}
		}
		if (module != null) {
			submodule = getActiveSubModuleNode(module.getChildren());
		}
		return submodule != null ? submodule : module != null ? module : group;
	}

	private ISubModuleNode getActiveSubModuleNode(List<ISubModuleNode> nodes) {
		ISubModuleNode result = null;
		for (ISubModuleNode candidate : nodes) {
			if (candidate.isActivated()) {
				ISubModuleNode activeChild = getActiveSubModuleNode(candidate.getChildren());
				result = activeChild != null ? activeChild : candidate;
				break;
			}
		}
		return result;
	}

	/**
	 * determines if scrolling is needed
	 * 
	 * @return true if we need scrolling
	 */
	private boolean mayScroll() {
		int navigationComponentHeight = getNavigationComponentHeight();
		return getScrolledComponentHeight() > navigationComponentHeight && navigationComponentHeight > 0;
	}

	private void ensureFilledToBottom() {
		// use height
		int bodyBottomY = scrolledCompositeOffset + getScrolledComponentHeight();
		if (bodyBottomY < getNavigationComponentHeight()) {
			setBodyCompositeOffset(scrolledCompositeOffset + (getNavigationComponentHeight() - bodyBottomY));
		}
	}

	private void resetScrolling() {
		setBodyCompositeOffset(0);
		scrollControlComposite.setVisible(false);
	}

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

	/**
	 * this method moves the scrolled component upwards and downwards for
	 * simulation of scrolling
	 * 
	 * @param yScrolledOffset
	 *            the new vertical offset of the scrolled composite
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

	private final class MouseWheelAdapter implements Listener {

		// for saving last event time
		private int lastEventTime = 0;

		public void handleEvent(Event event) {
			// only go further if the event has a new time stamp
			if (mayScroll() && acceptEvent(event)) {
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
							scrollUp(SCROLLING_STEP);
						} else {
							scrollDown(SCROLLING_STEP);
						}
					}

				}
			}
		}

		private boolean acceptEvent(Event event) {
			// check that this is the latest event
			boolean isCurrent = event.time > lastEventTime;
			// 282089: check this window is has the focus, to avoid scrolling when
			// the mouse pointer happens to be over another overlapping window
			Control control = (Control) event.widget;
			boolean isActive = control.getShell() == getActiveShell();
			// 282091: check that this navigation component is visible. Since
			// we are using a display filter the navigation componentes of _each_
			// subapplicatio are notified when scrolling!
			boolean isVisible = getNavigationComponent().isVisible();
			return isCurrent && isActive && isVisible;
		}

		private Shell getActiveShell() {
			IWorkbenchWindow activeWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			return activeWindow != null ? activeWindow.getShell() : null;
		}
	}

	/**
	 * this composite contains the "up"- and "down" buttons. Manual scrolling is
	 * triggered here!
	 */
	private final class ScrollControlComposite extends Composite {

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

		private final class NavigationResizeListener extends ControlAdapter {
			@Override
			public void controlResized(ControlEvent e) {
				setVisible(mayScroll());
			}
		}

		/**
		 * action delegation for manual scrolling
		 */
		private final class ScrollDelegation extends SelectionAdapter {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.getSource() == upButton) {
					scrollUp(SCROLLING_STEP);
				} else {
					scrollDown(SCROLLING_STEP);
				}
			}
		}
	}

}