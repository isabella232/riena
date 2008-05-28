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
package org.eclipse.riena.ui.core.uiprocess;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.core.runtime.jobs.ProgressProvider;

/**
 * This class is used in conjunction with {@link UIProcess} and provides a
 * {@link IProgressMonitor} that just synchronizes and delegates to another
 * instance of {@link IProgressMonitor}. Serialization is done by a
 * {@link IUISynchronizer}.
 * 
 */
public class UICallbackDispatcher extends ProgressProvider implements IUIMonitorContainer {

	private IUISynchronizer syncher;

	private List<IUIMonitor> uiMonitors;

	private ProcessInfo pInfo;

	private boolean visualize;

	private ThreadSwitcher threadSwitcher;

	public UICallbackDispatcher(IUISynchronizer syncher) {
		this.uiMonitors = new ArrayList<IUIMonitor>();
		this.syncher = syncher;
		this.pInfo = new ProcessInfo();
		this.visualize = true;
	}

	public ProcessInfo getProcessInfo() {
		return pInfo;
	}

	public void addUIMonitor(IUIMonitor uiMontitor) {
		IProcessInfoAware processInfoAware = (IProcessInfoAware) uiMontitor.getAdapter(IProcessInfoAware.class);
		if (processInfoAware != null) {
			processInfoAware.setProcessInfo(pInfo);
		}
		this.uiMonitors.add(uiMontitor);
	}

	/**
	 * @return the synchronizer that serializes to the UI-Thread
	 */
	protected final IUISynchronizer getSyncher() {
		return syncher;
	}

	@Override
	public final IProgressMonitor createMonitor(Job job) {
		threadSwitcher = new ThreadSwitcher(createWrappedMonitor(job));
		observeJob(job);
		return threadSwitcher;
	}

	private void observeJob(Job job) {
		job.addJobChangeListener(new JobChangeAdapter() {

			@Override
			public void done(IJobChangeEvent event) {
				super.done(event);
				threadSwitcher.done();
			}
		});
	}

	private List<IUIMonitor> getActiveMonitors() {
		if (visualize) {
			return new ArrayList<IUIMonitor>(uiMonitors);
		}
		List<IUIMonitor> monitors = new ArrayList<IUIMonitor>();
		for (IUIMonitor monitor : uiMonitors) {
			if (monitor.isActive(this)) {
				monitors.add(monitor);
			}
		}
		return monitors;
	}

	/**
	 * Creates the wrapped {@link IProgressMonitor} which will by serialized to
	 * the UI-Thread.
	 * 
	 * @param job -
	 *            for which the monitor is created
	 * @return - The wrapping monitor with a delegate inside
	 */
	protected IProgressMonitor createWrappedMonitor(Job job) {
		/**
		 * Default implementation of a monitor just delegating to the uiProcess
		 * Methods. This monitor gets called on the ui-Thread as a delegate of
		 * the ThreadSwitcher
		 */
		return new NullProgressMonitor() {
			@Override
			public void beginTask(String name, int totalWork) {
				pInfo.setMaxProgress(totalWork);
				for (IUIMonitor monitor : getActiveMonitors()) {
					monitor.initialUpdateUI(totalWork);
				}
			}

			@Override
			public void worked(int work) {
				pInfo.setActualProgress(work);
				for (IUIMonitor monitor : getActiveMonitors()) {
					monitor.updateProgress(work);
				}
			}

			@Override
			public void done() {
				for (IUIMonitor monitor : getActiveMonitors()) {
					monitor.finalUpdateUI();
				}
			}
		};
	}

	/**
	 * This implementation of the ProgressMonitor delegates to another
	 * ProgressMonitor and serializes to the UI-Thread of the underlying ui
	 * technology.
	 */
	private final class ThreadSwitcher extends NullProgressMonitor {

		private IProgressMonitor delegate;

		private boolean done;

		public ThreadSwitcher(IProgressMonitor wrappedMonitor) {
			this.delegate = wrappedMonitor;
		}

		@Override
		public void beginTask(final String name, final int totalWork) {
			synchronize(new Runnable() {

				public void run() {
					delegate.beginTask(name, totalWork);
				}
			});
		}

		@Override
		public void worked(final int work) {
			synchronize(new Runnable() {

				public void run() {
					delegate.worked(work);
				}
			});
		}

		@Override
		public void done() {
			if (!done) {
				synchronize(new Runnable() {

					public void run() {
						delegate.done();
					}
				});
				done = true;
			}
		}

		public void synchronize(Runnable runnable) {
			syncher.synchronize(runnable);
		}
	}

	public boolean isVisualizing() {
		return visualize;
	}

	public void setVisualizing(boolean visualize) {
		this.visualize = visualize;
	}

}
