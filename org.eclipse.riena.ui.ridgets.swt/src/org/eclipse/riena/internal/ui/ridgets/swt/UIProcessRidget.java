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
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.riena.ui.core.uiprocess.IProgressVisualizer;
import org.eclipse.riena.ui.core.uiprocess.ProcessInfo;
import org.eclipse.riena.ui.ridgets.IProgressBoxRidget;
import org.eclipse.riena.ui.swt.uiprocess.ICancelListener;
import org.eclipse.riena.ui.swt.uiprocess.UIProcessControl;
import org.eclipse.swt.widgets.Display;

public class UIProcessRidget extends AbstractSWTRidget implements IProgressBoxRidget {

	private UIProcessControl control;

	private CancelListener cancelListener;

	private Map<IProgressVisualizer, Integer> totalWork;

	private Stack<IProgressVisualizer> visualizerStack;

	public UIProcessRidget() {
		cancelListener = new CancelListener();
		totalWork = new HashMap<IProgressVisualizer, Integer>();
		visualizerStack = new Stack<IProgressVisualizer>();
	}

	protected UIProcessControl getProgressControl() {
		return control;
	}

	public void open() {
		getProgressControl().start();
		updateUI();
	}

	public void close() {
		getProgressControl().stop();
	}

	private class CancelListener implements ICancelListener {

		public void canceled() {
			cancelCurrentVisualizer();
		}

		private void cancelCurrentVisualizer() {
			getCurrentProcessInfo().cancel();
			if (isLonelyVisualizer(getCurrentVisualizer())) {
				close();
			}
			removeProgressVisualizer(getCurrentVisualizer());
		}

	}

	@Override
	protected void bindUIControl() {
		control.addCancelListener(cancelListener);
	}

	@Override
	public void setUIControl(Object uiControl) {
		checkUIControl(uiControl);
		unbindUIControl();
		this.control = (UIProcessControl) uiControl;
		bindUIControl();
	}

	@Override
	protected void checkUIControl(Object uiControl) {
		AbstractSWTRidget.assertType(uiControl, UIProcessControl.class);

	}

	@Override
	protected void unbindUIControl() {
		if (getProgressControl() != null) {
			getProgressControl().removeCancelListener(cancelListener);
		}
	}

	public void addProgressVisualizer(IProgressVisualizer visualizer) {
		addVisualizer(visualizer);
	}

	public void finalUpdateUI(IProgressVisualizer visualizer) {
		if (isLonelyVisualizer(visualizer)) {
			close();
		}

	}

	private boolean isLonelyVisualizer(IProgressVisualizer visualizer) {
		return visualizerStack.size() == 1 && isActive(visualizer);
	}

	public void initialUpdateUI(IProgressVisualizer visualizer, int totalWork) {
		if (isActive(visualizer)) {
			checkOpen();
			saveTotalWork(visualizer, totalWork);
			updateUI();
		}
	}

	private void addVisualizer(IProgressVisualizer visualizer) {
		if (!visualizerStack.contains(visualizer) && visualizer.getProcessInfo().isDialogVisible()) {
			observeProcessInfo(visualizer.getProcessInfo());
			visualizerStack.push(visualizer);
		}
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

	private class ProcessInfoListener implements PropertyChangeListener {

		public void propertyChange(PropertyChangeEvent evt) {
			ProcessInfo pInfo = ProcessInfo.class.cast(evt.getSource());
			if (isActive(pInfo) && !ProcessInfo.PROPERTY_CANCELED.equals(evt.getPropertyName())) {
				updateUI();
			}
		}

		private boolean isActive(ProcessInfo info) {
			return getCurrentVisualizer() != null && getCurrentVisualizer().getProcessInfo().equals(info);
		}

	}

	protected IProgressVisualizer getCurrentVisualizer() {
		if (visualizerStack.size() == 0) {
			return null;
		}
		return visualizerStack.peek();
	}

	private void updateUI() {
		getWindowDisplay().syncExec(new Runnable() {

			public void run() {
				if (!getProgressControl().getWindow().getShell().isDisposed()) {
					getProgressControl().setDescription(getCurrentProcessInfo().getNote());
					getProgressControl().setTitle(getCurrentProcessInfo().getTitle());
				}
			}
		});
	}

	private Display getWindowDisplay() {
		return getProgressControl().getWindow().getShell().getDisplay();
	}

	private ProcessInfo getCurrentProcessInfo() {
		return getCurrentVisualizer().getProcessInfo();
	}

	private void saveTotalWork(IProgressVisualizer visualizer, int totalWork) {
		this.totalWork.put(visualizer, new Integer(totalWork));
	}

	private void checkOpen() {
		open();
	}

	public void removeProgressVisualizer(IProgressVisualizer visualizer) {
		visualizerStack.remove(visualizer);
		totalWork.remove(visualizer);
	}

	public void setActiveProgressVisualizer(IProgressVisualizer visualizer) {
		// TODO Auto-generated method stub

	}

	protected boolean isActive(IProgressVisualizer visualizer) {
		return visualizer != null && visualizer == getCurrentVisualizer();
	}

	public void updateProgress(IProgressVisualizer visualizer, int progress) {
		if (isActive(visualizer)) {
			getProgressControl().showProgress(progress, getTotalWork(visualizer));
		}
	}

	private Integer getTotalWork(IProgressVisualizer visualizer) {
		return totalWork.get(visualizer);
	}

	public List<IProgressVisualizer> getProgressVisualizers() {
		return new ArrayList<IProgressVisualizer>(visualizerStack);
	}

	public void activate() {
		// nothing to do here
	}

	public void deactivate() {
		// nothing to do here
	}

}
