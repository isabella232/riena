package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.riena.internal.ui.ridgets.swt.UIProcessRidget;
import org.eclipse.riena.navigation.ISubApplication;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.model.NavigationTreeObserver;
import org.eclipse.riena.navigation.model.SubModuleNodeAdapter;
import org.eclipse.riena.navigation.ui.controllers.SubApplicationViewController;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManagerAccessor;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtViewId;
import org.eclipse.riena.ui.swt.uiprocess.UIProcessControl;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class SubApplicationPerspectiveFactory implements IPerspectiveFactory {

	/**
	 * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
	 */
	public void createInitialLayout(IPageLayout layout) {
		SubApplicationViewController controller = createController(layout.getDescriptor().getId());
		initializeListener(controller);
		doBaseLayout(layout);
	}

	protected SubApplicationViewController createController(String id) {
		ISubApplication subApplication = locateSubApplication(id);
		return createController(subApplication);
	}

	private ISubApplication locateSubApplication(String id) {
		return SwtPresentationManagerAccessor.getManager().getNavigationNode(id, ISubApplication.class);
	}

	protected SubApplicationViewController createController(ISubApplication subApplication) {
		SubApplicationViewController subApplicationViewController = new SubApplicationViewController(subApplication);
		UIProcessRidget progressBoxRidget = new UIProcessRidget();
		progressBoxRidget.setUIControl(new UIProcessControl(Display.getDefault().getActiveShell()));
		subApplicationViewController.setProgressBoxRidget(progressBoxRidget);
		return subApplicationViewController;
	}

	/**
	 * Adds a listener for all sub-module nodes of the sub-application.
	 * 
	 * @param controller
	 *            - controller of the sub-application
	 */
	private void initializeListener(SubApplicationViewController controller) {
		NavigationTreeObserver navigationTreeObserver = new NavigationTreeObserver();
		navigationTreeObserver.addListener(new SubModuleNodeListener());
		navigationTreeObserver.addListenerTo(controller.getNavigationNode());
	}

	protected void doBaseLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);
	}

	/**
	 * After a sub-module node was activated, the corresponding view is shown.
	 */
	private static class SubModuleNodeListener extends SubModuleNodeAdapter {

		private boolean navigationUp = false;

		/**
		 * @see org.eclipse.riena.navigation.model.NavigationNodeAdapter#activated(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void activated(ISubModuleNode source) {
			checkBaseStructure();
			SwtViewId id = getViewId(source);
			showMultiView(id);
		}

		/**
		 * @see org.eclipse.riena.navigation.model.NavigationNodeAdapter#disposed(org.eclipse.riena.navigation.INavigationNode)
		 */
		@Override
		public void disposed(ISubModuleNode source) {
			SwtViewId id = getViewId(source);
			hideView(id);
		}

		/**
		 * Returns the view ID of the given sub-module node.
		 * 
		 * @param source
		 *            - sub-module node
		 * @return view ID
		 */
		private SwtViewId getViewId(ISubModuleNode node) {
			return SwtPresentationManagerAccessor.getManager().getSwtViewId(node);
		}

		/**
		 * At the very first time (a sub-module was activated), the view parts
		 * of the sub-application switcher and the navigation tree are shown.
		 */
		private void checkBaseStructure() {
			if (!navigationUp) {
				createNavigation();
				navigationUp = true;
			}
		}

		protected String createNextId() {
			return String.valueOf(System.currentTimeMillis());
		}

		private void createNavigation() {
			showView(NavigationTreeViewPart.ID, createNextId());
		}

		private void showMultiView(SwtViewId id) {
			showView(id.getId(), id.getSecondary());
		}

		private void hideView(SwtViewId id) {
			hideView(id.getId(), id.getSecondary());
		}

		/**
		 * Shows a view in the active page.
		 * 
		 * @param id
		 *            - the id of the view extension to use
		 * @param secondaryId
		 *            - the secondary id to use
		 */
		private void showView(String id, String secondary) {
			try {
				getActivePage().showView(id, secondary, 1);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Hides the view in the active page.
		 * 
		 * @param id
		 *            - the id of the view extension to use
		 * @param secondaryId
		 *            - the secondary id to use
		 */
		private void hideView(String id, String secondary) {
			IViewReference viewRef = getActivePage().findViewReference(id, secondary);
			if (viewRef != null) {
				IViewPart view = viewRef.getView(false);
				getActivePage().hideView(view);
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

}
