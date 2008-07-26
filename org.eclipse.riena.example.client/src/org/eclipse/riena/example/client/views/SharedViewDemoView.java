package org.eclipse.riena.example.client.views;

import org.eclipse.riena.example.client.controllers.SharedViewDemo;
import org.eclipse.riena.internal.example.client.utils.UIControlsFactory;
import org.eclipse.riena.internal.ui.ridgets.swt.LabelRidget;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleNodeView;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class SharedViewDemoView extends SubModuleNodeView<SharedViewDemo> {

	public static final String ID = "sharedviewdemoid"; //$NON-NLS-1$

	private static final int FIELD_WIDTH = 100;
	private static final int TOP = 10;
	private static final int LEFT = 10;
	private static final int LABEL_WIDTH = 90;

	/**
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void basicCreatePartControl(Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(ILnfKeyConstants.SUB_MODULE_BACKGROUND));
		final Label helloLabel = UIControlsFactory.createLabel(parent, "", SWT.CENTER); //$NON-NLS-1$

		// layout
		FormLayout layout = new FormLayout();
		parent.setLayout(layout);
		ILabelRidget labelFacade = new LabelRidget();
		labelFacade.setUIControl(helloLabel);
		addUIControl(helloLabel, "labelFacade"); //$NON-NLS-1$
		// getController().setLabelFacade(labelFacade);

		Label someText = UIControlsFactory.createLabel(parent, "Data", SWT.LEFT);
		FormData fd = new FormData();
		fd.top = new FormAttachment(0, TOP);
		fd.left = new FormAttachment(0, LEFT);
		someText.setLayoutData(fd);

		Text someData = new Text(parent, SWT.BORDER | SWT.SINGLE);
		fd = new FormData();
		fd.top = new FormAttachment(someText, 0, SWT.TOP);
		fd.left = new FormAttachment(someText, LABEL_WIDTH, SWT.LEFT);
		fd.width = FIELD_WIDTH;
		someData.setLayoutData(fd);
		// ta = new TextFieldRidget(someData);
		addUIControl(someData, "textFacade"); //$NON-NLS-1$
		// getController().setTextFacade(ta);
		// layout
		FormData data = new FormData();
		data.top = new FormAttachment(0, 45);
		data.left = new FormAttachment(someText, LABEL_WIDTH, SWT.LEFT);
		helloLabel.setLayoutData(data);
	}

	@Override
	public void setFocus() {
		super.setFocus();
		// FIXME implement generic way to rebind controllers. just for
		// evaluation.
		// if (ta != null && getController() != null) {
		// getController().setLabelFacade(labelFacade);
		// getController().setTextFacade(ta);
		// }
		// getController().afterBind();
	}

	@Override
	protected SharedViewDemo createController(ISubModuleNode subModuleNode) {
		return new SharedViewDemo(subModuleNode);
	}

}