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
package org.eclipse.riena.example.client.views;

import org.eclipse.riena.example.client.controllers.UIProcessDemoController;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleNodeView;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */
public class UiProcessDemoView extends SubModuleNodeView<UIProcessDemoController> {

	public final static String ID = "org.eclipse.riena.sample.app.client.uiprocessdemoview"; //$NON-NLS-1$

	@Override
	protected void basicCreatePartControl(Composite parent) {
		Button button = new Button(parent, SWT.None);
		button.setText("Start UIProcess");
		addUIControl(button, "actionRidget"); //$NON-NLS-1$

		// layout
		parent.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		FormLayout layout = new FormLayout();
		parent.setLayout(layout);

		FormData fd = new FormData();
		fd.height = 20;
		fd.width = 90;
		fd.top = new FormAttachment(0, 25);
		fd.left = new FormAttachment(0, 5);
		button.setLayoutData(fd);
	}

	// private static final class DialogProgressProvider extends
	// SynchingProgressProvider {
	//
	// public DialogProgressProvider(IUISyncher syncher) {
	// super(syncher);
	// }
	//
	// @Override
	// protected IProgressMonitor createWrappedMonitor(final Job job) {
	// final IProgressMonitor[] monitor = new IProgressMonitor[] { new
	// NullProgressMonitor() };
	//
	// getSyncher().synch(new Runnable() {
	//
	// public void run() {
	// final ProgressMonitorDialog dialog = new
	// ProgressMonitorJobsDialog(Display.getDefault()
	// .getActiveShell());
	// dialog.setBlockOnOpen(false);
	// dialog.setCancelable(true);
	// dialog.open();
	// job.addJobChangeListener(new JobChangeAdapter() {
	//
	// @Override
	// public void done(IJobChangeEvent event) {
	// getSyncher().synch(new Runnable() {
	//
	// public void run() {
	// dialog.close();
	// }
	// });
	// }
	// });
	// monitor[0] = dialog.getProgressMonitor();
	// }
	// });
	// return monitor[0];
	// }
	//
	// }

	@Override
	protected UIProcessDemoController createController(ISubModuleNode subModuleNode) {
		return new UIProcessDemoController(getCurrentNode());
	}

}
