package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.riena.navigation.ISubApplication;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ISubModuleNodeListener;
import org.eclipse.riena.navigation.model.NavigationTreeObserver;
import org.eclipse.riena.navigation.model.SubModuleNodeAdapter;
import org.eclipse.riena.navigation.ui.controllers.SubApplicationViewController;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManagerAccessor;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManager.SwtViewId;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.internal.Workbench;

public class SubApplicationView implements IPerspectiveFactory {

	private SubApplicationViewController controller;
	private ISubModuleNodeListener subModuleNodeListener;
	private NavigationTreeObserver navigationTreeObserver;

	protected SubApplicationViewController createController(String id) {
		ISubApplication subApplication = locateSubApplication(id);
		return createController(subApplication);
	}

	private ISubApplication locateSubApplication(String id) {
		return SwtPresentationManagerAccessor.getManager().getNavigationNode(id, ISubApplication.class);
	}

	protected SubApplicationViewController createController(ISubApplication subApplication) {
		return new SubApplicationViewController(subApplication);
	}

	private void initializeListener() {
		subModuleNodeListener = new SubModuleNodeListener();
		navigationTreeObserver = new NavigationTreeObserver();
		navigationTreeObserver.addListener(subModuleNodeListener);
		navigationTreeObserver.addListenerTo(controller.getNavigationNode());
	}

	public void createInitialLayout(IPageLayout layout) {
		if (controller == null) {
			controller = createController(layout.getDescriptor().getId());
		}
		initializeListener();
		doBaseLayout(layout);
	}

	protected void doBaseLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);
	}

	private boolean navigationUp = false;

	protected SwtViewId getViewId(ISubModuleNode source) {
		return SwtPresentationManagerAccessor.getManager().getSwtViewId(source);
	}

	private class SubModuleNodeListener extends SubModuleNodeAdapter {

		@Override
		public void activated(ISubModuleNode source) {
			checkBaseStructure();
			SwtViewId id = getViewId(source);
			showMultiView(id);
		}

		private void checkBaseStructure() {
			if (!navigationUp) {
				createNavigation();
				createApplications();
				navigationUp = true;
			}
		}

		protected String createNextId() {
			return String.valueOf(System.currentTimeMillis());
		}

		private void createApplications() {
			showView(ApplicationSwitchViewPart.ID, createNextId());
		}

		private void createNavigation() {
			showView(NavigationTreeView.ID, createNextId());
		}

		private void showMultiView(SwtViewId id) {
			showView(id.getId(), id.getSecondary());
		}

		@SuppressWarnings("restriction")
		private void showView(String id, String secondary) {
			try {
				Workbench.getInstance().getActiveWorkbenchWindow().getActivePage().showView(id, secondary, 1);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}

	}

}
