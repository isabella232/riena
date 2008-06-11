package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.riena.navigation.ISubApplication;
import org.eclipse.riena.navigation.model.ApplicationModel;
import org.eclipse.riena.navigation.ui.swt.component.SubApplicationSwitcherWidget;
import org.eclipse.riena.navigation.ui.swt.presentation.SwtPresentationManagerAccessor;
import org.eclipse.riena.navigation.ui.swt.presentation.stack.TitlelessStackPresentation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class ApplicationSwitchViewPart extends ViewPart {

	public final static String ID = "org.eclipse.riena.navigation.ui.swt.applicationSwicther"; //$NON-NLS-1$

	@Override
	public void createPartControl(final Composite parent) {
		setPartProperty(TitlelessStackPresentation.PROPERTY_APPLICATION, Boolean.TRUE.toString());
		new SubApplicationSwitcherWidget(parent, SWT.NONE, getApplicationModel());
		parent.setLayout(new FillLayout());
	}

	public/**/ApplicationModel getApplicationModel() {
		return getSubApplication().getParent().getTypecastedAdapter(ApplicationModel.class);
	}

	private ISubApplication getSubApplication() {
		String perspectiveID = getViewSite().getPage().getPerspective().getId();
		return SwtPresentationManagerAccessor.getManager().getNavigationNode(perspectiveID, ISubApplication.class);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}

}
