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

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

/**
 * Control for showing a progress or process info window.
 */
public class UIProcessControl implements IProgressControl {

	private static final int UPDATE_DELAY = 200;
	private boolean processing = false;
	private UiProcessWindow processWindow;

	public UIProcessControl(Shell parentShell) {
		Assert.isNotNull(parentShell);
		createProcessWindow(parentShell);
	}

	private void createProcessWindow(Shell parentShell) {
		processWindow = new UiProcessWindow(parentShell, this);
		processWindow.addProcessWindowListener(new IProcessWindowListener() {

			public void windowAboutToClose() {
				stopProcessing();
				fireCanceled(true);
			}

		});
	}

	public ApplicationWindow getWindow() {
		return processWindow;

	}

	private void showWindow() {
		processWindow.openWindow();
	}

	public void stop() {
		closeWindow();
	}

	private void closeWindow() {
		stopProcessing();
		processWindow.closeWindow();

	}

	protected synchronized boolean isProcessing() {
		return processing;
	}

	public synchronized void setProcessing(boolean processing) {
		this.processing = processing;
	}

	public void showProcessing() {
		startProcessing();

	}

	private void startProcessing() {
		if (!isProcessing()) {
			setProcessing(true);
			getProgressBar().setMaximum(90);
			getPercentLabel().setText(""); //$NON-NLS-1$
			if (processUpdateThread == null || !processUpdateThread.isAlive()) {
				startUpdateThread();
			}
		}
	}

	private void startUpdateThread() {
		processUpdateThread = new ProcessUpdateThread();
		processUpdateThread.start();
	}

	private final class ProcessUpdateThread extends Thread {

		@Override
		public void run() {
			setPriority(Thread.MAX_PRIORITY);
			setName("ProcessUpdateThread"); //$NON-NLS-1$
			processUpdateLoop();
		}
	}

	private void processUpdateLoop() {
		final int[] selection = new int[] { 0 };

		while (isProcessing()) {
			if (selection[0] <= 100) {
				selection[0] += 10;
			} else {
				selection[0] = 0;
			}
			if (!getProgressBar().isDisposed()) {
				getProgressBar().getDisplay().asyncExec(new Runnable() {

					public void run() {
						if (!getProgressBar().isDisposed()) {
							if (isProcessing()) {
								getProgressBar().setSelection(selection[0]);
							}
						}
					}
				});
			}

			try {
				Thread.sleep(UPDATE_DELAY);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private ProgressBar getProgressBar() {
		return processWindow.getProgressBar();
	}

	public void showProgress(int value, int maxValue) {
		stopProcessing();
		int percentValue = calcSelection(value, maxValue);
		if (getWindow().getShell() != null && !getWindow().getShell().isDisposed()) {
			getPercentLabel().setText(String.valueOf(percentValue) + " %"); //$NON-NLS-1$
			getProgressBar().setSelection(percentValue);
		}
	}

	private Label getPercentLabel() {
		return processWindow.getPercent();
	}

	private int calcSelection(int value, int maxValue) {
		double v = (double) value / (double) maxValue * 100;
		return (int) v;
	}

	private void stopProcessing() {
		setProcessing(false);
	}

	public void start() {
		showProcessBox();

	}

	private void showProcessBox() {
		showWindow();
	}

	public void setDescription(String text) {
		processWindow.setDescrition(text);
	}

	public void setTitle(String text) {
		processWindow.getShell().setText(text);
	}

	private ListenerList cancelListeners = new ListenerList();
	private ProcessUpdateThread processUpdateThread;

	public void addCancelListener(ICancelListener listener) {
		cancelListeners.add(listener);
	}

	public void removeCancelListener(ICancelListener listener) {
		cancelListeners.remove(listener);
	}

	protected void fireCanceled(boolean windowClosing) {
		for (Object listener : cancelListeners.getListeners()) {
			ICancelListener.class.cast(listener).canceled(windowClosing);
		}
	}
}
