package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.ISubApplication;
import org.eclipse.riena.navigation.ui.controllers.SubApplicationViewController;
import org.eclipse.riena.navigation.ui.ridgets.INavigationTreeRidget;
import org.eclipse.riena.navigation.ui.ridgets.INavigationTreeRidgetListener;
import org.eclipse.riena.navigation.ui.swt.component.SubApplicationNavigationComponent;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManagerAccessor;
import org.eclipse.riena.navigation.ui.swt.presentation.stack.TitlelessStackPresentation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class NavigationTreeView extends ViewPart {

	public final static String ID = "org.eclipse.riena.navigation.ui.navigationtree"; //$NON-NLS-1$

	private ISubApplication subApplicationNode;
	private SubApplicationViewController controller;
	private SubApplicationNavigationComponent subApplicationComponent;

	private Composite baseComposite;

	@Override
	public void createPartControl(Composite parent) {
		setPartProperty(TitlelessStackPresentation.PROPERTY_NAVIGATION, String.valueOf(Boolean.TRUE));
		locateModelNode();
		baseComposite = initLayoutParts(parent);
		createSubApplicationComponent();
	}

	private Composite getBaseComposite() {
		return baseComposite;
	}

	private void locateModelNode() {
		this.subApplicationNode = SwtPresentationManagerAccessor.getManager().getNavigationNode(
				this.getViewSite().getPage().getPerspective().getId(), ISubApplication.class);
		if (subApplicationNode != null) {
			controller = (SubApplicationViewController) subApplicationNode.getPresentation();
			if (controller != null) {
				controller.setNavigationTree(getTreeFacade());
			}
		}
	}

	private INavigationTreeRidget getTreeFacade() {
		return new INavigationTreeRidget() {

			public void addListener(INavigationTreeRidgetListener listener) {
				// TODO Auto-generated method stub

			}

			public void childAdded(INavigationNode<?> node) {
				// TODO Auto-generated method stub

			}

			public void childRemoved(INavigationNode<?> node) {
				// TODO Auto-generated method stub

			}

			public void collapse(INavigationNode<?> node) {
				// TODO Auto-generated method stub

			}

			public void expand(INavigationNode<?> node) {
				// TODO Auto-generated method stub

			}

			public void removeListener(INavigationTreeRidgetListener listener) {
				// TODO Auto-generated method stub

			}

			public void select(INavigationNode<?> node) {
				// TODO Auto-generated method stub

			}

			public void showRoot(ISubApplication node) {
				// TODO Auto-generated method stub

			}

		};
	}

	protected ISubApplication getSubApplicationNode() {
		return subApplicationNode;
	}

	protected SubApplicationNavigationComponent getSubApplicationComponent() {
		return subApplicationComponent;
	}

	protected void createSubApplicationComponent() {
		this.subApplicationComponent = new SubApplicationNavigationComponent(getSubApplicationNode(),
				getBaseComposite());

	}

	private Composite initLayoutParts(Composite parent) {
		Composite c = new Composite(parent, SWT.None);
		c.setLayout(new FillLayout());
		return c;
	}

	@Override
	public void setFocus() {
	}

}
