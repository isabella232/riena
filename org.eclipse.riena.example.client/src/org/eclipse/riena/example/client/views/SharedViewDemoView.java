package org.eclipse.riena.example.client.views;

import org.eclipse.riena.example.client.controllers.SharedViewDemo;
import org.eclipse.riena.internal.ui.ridgets.swt.LabelRidget;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.swt.views.SWTBindingPropertyLocator;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleNodeView;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
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
		parent.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		final Label helloLabel = new Label(parent, SWT.CENTER);
		helloLabel.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));

		// layout
		FormLayout layout = new FormLayout();
		parent.setLayout(layout);
		ILabelRidget labelFacade = new LabelRidget();
		labelFacade.setUIControl(helloLabel);
		helloLabel.setData(SWTBindingPropertyLocator.BINDING_PROPERTY, "labelFacade"); //$NON-NLS-1$
		addUIControl(helloLabel);
		// getController().setLabelFacade(labelFacade);

		Label someText = new Label(parent, SWT.LEFT);
		someText.setText("Data");
		someText.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
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
		someData.setData(SWTBindingPropertyLocator.BINDING_PROPERTY, "textFacade"); //$NON-NLS-1$
		addUIControl(someData);
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