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
package org.eclipse.riena.internal.ui.ridgets.swt.uiprocess;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.riena.internal.ui.ridgets.swt.AbstractSWTRidget;
import org.eclipse.riena.ui.core.uiprocess.IProgressVisualizer;
import org.eclipse.riena.ui.core.uiprocess.ProcessInfo;
import org.eclipse.riena.ui.ridgets.AbstractRidget;
import org.eclipse.riena.ui.ridgets.IProgressBoxRidget;
import org.eclipse.riena.ui.swt.uiprocess.ICancelListener;
import org.eclipse.riena.ui.swt.uiprocess.UIProcessControl;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * The {@link UIProcessRidget} is not a standard {@link AbstractSWTRidget} as it
 * does not bind a {@link Control} but a {@link UIProcessControl}. Another
 * difference is that it does not hold any detail state of the uiProcessControl.
 */
public class UIProcessRidget extends AbstractRidget implements IProgressBoxRidget {

	private UIProcessControl uiProcessControl;

	private CancelListener cancelListener;

	private Map<IProgressVisualizer, Progress> visualizerProgress;

	private Map<Object, VisualizerContainer> contexts;

	private IVisualContextManager contextLocator;

	private boolean focusAble;

	public UIProcessRidget() {
		cancelListener = new CancelListener();
		visualizerProgress = new HashMap<IProgressVisualizer, Progress>();
		contexts = new HashMap<Object, VisualizerContainer>();
	}

	class ContextDataComparator implements Comparator<VisualizerContainer> {

		public int compare(VisualizerContainer o1, VisualizerContainer o2) {
			int time1 = getVisualizerTime(o1);
			int time2 = getVisualizerTime(o2);
			if (time1 > time2) {
				return -1;
			}
			if (time1 == time2) {
				return 0;
			}

			return 1;
		}

		private int getVisualizerTime(VisualizerContainer data) {
			return data.get(data.getCurrentVisualizer());
		}

	}

	private VisualizerContainer getActiveContextContainer() {
		List<VisualizerContainer> data = getActiveContextContainerList();
		Collections.sort(data, new ContextDataComparator());
		if (data.size() > 0) {
			return data.get(0);
		}
		return null;
	}

	private List<VisualizerContainer> getActiveContextContainerList() {
		List<Object> activeContexts = getActiveContexts();
		List<VisualizerContainer> data = new ArrayList<VisualizerContainer>();
		for (Object object : activeContexts) {
			data.add(contexts.get(object));
		}
		return data;
	}

	private List<Object> getActiveContexts() {
		return getContextLocator().getActiveContexts(new LinkedList<Object>(contexts.keySet()));
	}

	/*
	 * holds the progress of a visualized UiProcess
	 */
	private class Progress {
		int totalWork = -1;
		int completed = -1;
	}

	private void showProcessing() {
		getUIControl().showProcessing();
	}

	public void open() {
		getUIControl().start();
		updateUi();
	}

	public void close() {
		getUIControl().stop();
	}

	private class CancelListener implements ICancelListener {

		public void canceled(boolean windowClosed) {
			if (getCurrentVisualizer() != null) {
				cancelCurrentVisualizer(windowClosed);
			}
		}

		private void cancelCurrentVisualizer(boolean windowClosed) {
			if (windowClosed) {// TODO make smoother
				cancelAllVisualizersInContext();
			} else {
				getCurrentProcessInfo().cancel();
			}
			if (isLonelyVisualizer(getCurrentVisualizer()) && !windowClosed) {
				close();
			}
			removeProgressVisualizer(getCurrentVisualizer());
		}

		private void cancelAllVisualizersInContext() {
			List<VisualizerContainer> activeContextDataList = getActiveContextContainerList();
			for (VisualizerContainer visualizerContextData : activeContextDataList) {
				for (IProgressVisualizer visualizer : visualizerContextData.keySet()) {
					visualizer.getProcessInfo().cancel();
				}
			}
		}

	}

	protected void bindUIControl() {
		uiProcessControl.addCancelListener(cancelListener);
	}

	public void setUIControl(Object uiControl) {
		checkUIControl(uiControl);
		unbindUIControl();
		uiProcessControl = (UIProcessControl) uiControl;
		bindUIControl();
	}

	protected void checkUIControl(Object uiControl) {
		AbstractSWTRidget.assertType(uiControl, UIProcessControl.class);

	}

	protected void unbindUIControl() {
		if (getUIControl() != null) {
			getUIControl().removeCancelListener(cancelListener);
		}
	}

	public void addProgressVisualizer(IProgressVisualizer visualizer) {
		Object context = visualizer.getProcessInfo().getContext();
		// is there any contextData for this context? (any other visualizers for
		// the same context?)
		VisualizerContainer contextData = contexts.get(context);
		if (contextData == null) {
			// create Container for context to hold all visualizers for the
			// context
			contextData = new VisualizerContainer();
			contexts.put(context, contextData);
			contextLocator.addContextUpdateListener(contextChangeHandler, context);
		}
		// save when the visualizers was started
		saveVisualizerStartupTime(visualizer, contextData);
		// create the Progress Object to save progress and total work for the
		// visualizer
		createVisualizerProgress(visualizer);
		// observe processInfo changes(description, title, ..)
		observeProcessInfo(visualizer.getProcessInfo());
	}

	private ContextChangeHandler contextChangeHandler = new ContextChangeHandler();

	private class ContextChangeHandler implements IContextUpdateListener {

		public void contextUpdated(Object context) {
			checkContexts();

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.riena.internal.ui.ridgets.swt.IContextUpdateListener#
		 * beforeContextUpdate(java.lang.Object)
		 */
		public void beforeContextUpdate(Object context) {
			// save the bounds in all context parts
			List<Object> activeContexts = getActiveContexts();
			for (Object subContext : activeContexts) {
				saveBounds(subContext);
			}

		}

	}

	private void checkContexts() {
		if (getActiveContexts().size() == 0) {
			close();
		} else {
			if (getCurrentVisualizer() != null) {
				open();
				Object currentContext = null;

				for (Object context : contexts.keySet()) {
					VisualizerContainer container = contexts.get(context);
					if (container.getCurrentVisualizer() == getCurrentVisualizer()) {
						currentContext = context;
						break;
					}
				}
				if (currentContext != null) {
					getUIControl().getWindow().getShell().setBounds(contexts.get(currentContext).getBounds());
				}
				int progress = getProgress(getCurrentVisualizer()).completed;
				if (progress <= 0) {
					showProcessing();
				}
			}
		}
	}

	public void finalUpdateUI(IProgressVisualizer visualizer) {
		// if it´s the only visualizer for the current context: close window
		if (isActive(visualizer) && isLonelyVisualizer(visualizer)) {
			close();
		}

	}

	private boolean isLonelyVisualizer(IProgressVisualizer visualizer) {
		List<VisualizerContainer> activeContextContainerList = getActiveContextContainerList();
		if (activeContextContainerList.size() == 0) {
			return true;
		}

		int count = 0;
		for (VisualizerContainer visualizerContainer : activeContextContainerList) {
			count += visualizerContainer.size();
			if (count > 1) {
				return false;
			}
		}

		return true;
	}

	public void initialUpdateUI(IProgressVisualizer visualizer, int totalWork) {
		if (isActive(visualizer)) {
			// all this makes sense if the visualizers is part of one of the
			// active contexts
			open();
			showProcessing();
			saveTotalWork(visualizer, totalWork);
			updateUi();
		}
	}

	private void createVisualizerProgress(IProgressVisualizer visualizer) {
		// for progress data (total & completed work)
		visualizerProgress.put(visualizer, new Progress());
	}

	private void saveVisualizerStartupTime(IProgressVisualizer visualizer, VisualizerContainer contextData) {
		long time = System.currentTimeMillis();
		contextData.put(visualizer, new Integer((int) time));
	}

	private void observeProcessInfo(ProcessInfo processInfo) {
		processInfo.addPropertyChangeListener(getProcessInfoListener());
	}

	private PropertyChangeListener processInfoListener;

	private PropertyChangeListener getProcessInfoListener() {
		if (processInfoListener == null) {
			processInfoListener = new ProcessInfoListener();
		}
		return processInfoListener;
	}

	/*
	 * observes the processInfo of a visualizer
	 */
	private class ProcessInfoListener implements PropertyChangeListener {

		public void propertyChange(PropertyChangeEvent evt) {
			ProcessInfo pInfo = ProcessInfo.class.cast(evt.getSource());
			if (isActive(pInfo) && !ProcessInfo.PROPERTY_CANCELED.equals(evt.getPropertyName())) {
				updateUi();
			}
		}

		private boolean isActive(ProcessInfo info) {
			return getCurrentVisualizer() != null && getCurrentVisualizer().getProcessInfo().equals(info);
		}

	}

	/*
	 * the current visualizer is part of the list of active contexts at the
	 * specific point in time. the current visualizer is the last one added to
	 * the merged list of visualizers in all contexts.
	 */
	protected IProgressVisualizer getCurrentVisualizer() {
		VisualizerContainer activeContextContainer = getActiveContextContainer();
		if (activeContextContainer != null) {
			return activeContextContainer.getCurrentVisualizer();
		}
		return null;
	}

	/*
	 * update ui but take care of disposed widgets!
	 */
	private void updateUi() {
		Shell windowShell = getWindowShell();
		if (windowShell != null) {
			windowShell.getDisplay().syncExec(new Runnable() {

				public void run() {
					Shell shell = getUIControl().getWindow().getShell();
					if (shell != null && !shell.isDisposed()) {
						getUIControl().setDescription(getCurrentProcessInfo().getNote());
						getUIControl().setTitle(getCurrentProcessInfo().getTitle());
						// show the progress
						reinitializeProgress();

					}
				}

				private void reinitializeProgress() {
					int progress = visualizerProgress.get(getCurrentVisualizer()).completed;
					if (progress > -1) {
						updateProgress(getCurrentVisualizer(), progress);
					} else {
						showProcessing();
					}
				}
			});
		}
	}

	private Shell getWindowShell() {
		Shell shell = getUIControl().getWindow().getShell();
		return shell;
	}

	private ProcessInfo getCurrentProcessInfo() {
		return getCurrentVisualizer().getProcessInfo();
	}

	private void saveTotalWork(IProgressVisualizer visualizer, int totalWork) {
		this.visualizerProgress.get(visualizer).totalWork = totalWork;
	}

	public void removeProgressVisualizer(IProgressVisualizer visualizer) {
		removeVisualizerFromContextData(visualizer);
		removeVisualizerProgress(visualizer);
		if (getCurrentVisualizer() != null) {
			updateUi();
		}
	}

	private void removeVisualizerProgress(IProgressVisualizer visualizer) {
		visualizerProgress.remove(visualizer);
	}

	private void removeVisualizerFromContextData(IProgressVisualizer visualizer) {
		Collection<VisualizerContainer> contextDataKeys = contexts.values();
		for (VisualizerContainer contextData : contextDataKeys) {
			contextData.remove(visualizer);
		}
	}

	public void setActiveProgressVisualizer(IProgressVisualizer visualizer) {
		// we do not accept this from outside. It depends on the context which
		// is the active visualizer

	}

	protected boolean isActive(IProgressVisualizer visualizer) {
		return visualizer != null && visualizer == getCurrentVisualizer();
	}

	public void updateProgress(IProgressVisualizer visualizer, int progress) {
		saveProgress(visualizer, progress);
		if (isActive(visualizer)) {
			getUIControl().showProgress(progress, getTotalWork(visualizer));
		}
	}

	private void saveProgress(IProgressVisualizer visualizer, int progressValue) {
		Progress progress = getProgress(visualizer);
		if (progress != null) {
			progress.completed = progressValue;
		}
	}

	private Progress getProgress(IProgressVisualizer visualizer) {
		return visualizerProgress.get(visualizer);
	}

	private Integer getTotalWork(IProgressVisualizer visualizer) {
		return getProgress(visualizer).totalWork;
	}

	public List<IProgressVisualizer> getProgressVisualizers() {
		return new ArrayList<IProgressVisualizer>(visualizerProgress.keySet());
	}

	public void activate() {
		// we do not need to activate anything here
	}

	public void deactivate() {
		// we do not need to DeActivate anything here
	}

	private void saveBounds(Object visualContext) {
		if (visualContext != null) {
			Shell shell = getUIControl().getWindow().getShell();
			if (shell != null) {
				contexts.get(visualContext).setBounds(shell.getBounds());
			}
		}
	}

	public void setContextLocator(IVisualContextManager contextLocator) {
		this.contextLocator = contextLocator;
	}

	/**
	 * @return the contextLocator
	 */
	public IVisualContextManager getContextLocator() {
		return contextLocator;
	}

	public String getToolTipText() {
		if (getWindowShell() != null && !getWindowShell().isDisposed() && isFocusable()) {
			return getWindowShell().getToolTipText();
		}
		return ""; //$NON-NLS-1$
	}

	public UIProcessControl getUIControl() {
		return uiProcessControl;
	}

	public boolean hasFocus() {
		if (getWindowShell() != null && !getWindowShell().isDisposed() && isFocusable()) {
			return getWindowShell().isFocusControl();
		}
		return false;
	}

	public boolean isFocusable() {
		return focusAble;
	}

	public void setFocusable(boolean focusable) {
		this.focusAble = focusable;
	}

	public boolean isVisible() {
		if (getWindowShell() != null && !getWindowShell().isDisposed()) {
			return getWindowShell().isVisible();
		}
		return false;
	}

	public void requestFocus() {
		if (getWindowShell() != null && !getWindowShell().isDisposed() && isFocusable()) {
			getWindowShell().forceFocus();
		}
	}

	// No Blocking
	public void setBlocked(boolean blocked) {
	}

	public boolean isBlocked() {
		return false;
	}

	public void setToolTipText(String toolTipText) {
		if (getWindowShell() != null && !getWindowShell().isDisposed()) {
			getWindowShell().setToolTipText(toolTipText);
		}
	}

	public void setVisible(boolean visible) {
		if (visible) {
			open();
		} else {
			close();
		}
	}

}
