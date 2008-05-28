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
package org.eclipse.riena.ui.swt.uiprocess;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.riena.ui.core.uiprocess.IUISynchronizer;
import org.eclipse.riena.ui.core.uiprocess.UICallbackDispatcher;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.internal.progress.ProgressMonitorJobsDialog;

public class DialogProgressProvider extends UICallbackDispatcher {

	public DialogProgressProvider(IUISynchronizer syncher) {
		super(syncher);
	}

	@Override
	protected IProgressMonitor createWrappedMonitor(final Job job) {
		final IProgressMonitor[] monitor = new IProgressMonitor[] { new NullProgressMonitor() };

		getSyncher().synchronize(new Runnable() {

			public void run() {
				final ProgressMonitorDialog dialog = new ProgressMonitorJobsDialog(Display.getDefault()
						.getActiveShell());
				dialog.setBlockOnOpen(false);
				dialog.setCancelable(true);
				dialog.open();
				job.addJobChangeListener(new JobChangeAdapter() {

					@Override
					public void done(IJobChangeEvent event) {
						getSyncher().synchronize(new Runnable() {

							public void run() {
								dialog.close();
							}
						});
					}
				});
				monitor[0] = dialog.getProgressMonitor();
			}
		});
		return monitor[0];
	}

}