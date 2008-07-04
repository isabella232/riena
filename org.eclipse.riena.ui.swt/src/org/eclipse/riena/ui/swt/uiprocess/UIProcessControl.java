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
	private boolean closing = false;

	public UIProcessControl(Shell parentShell) {
		Assert.isNotNull(parentShell);
		createProcessWindow(parentShell);
	}

	private void createProcessWindow(Shell parentShell) {
		processWindow = new UiProcessWindow(parentShell, this);
		processWindow.addProcessWindowListener(new IProcessWindowListener() {

			public void windowAboutToClose() {
				closing = true;
				stopProcessing();
				fireCanceled();
				closing = false;
			}

		});
	}

	public ApplicationWindow getWindow() {
		return processWindow;

	}

	private void showWindow() {
		processWindow.openWindow();
		startProcessing();
	}

	public void stop() {
		closeWindow();
	}

	private void closeWindow() {
		if (!closing) {
			stopProcessing();
			processWindow.closeWindow();
		}

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
			startUpdateThread();
		}
	}

	private void startUpdateThread() {
		new ProcessUpdateThread().start();
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
							getProgressBar().setSelection(selection[0]);
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
		getPercentLabel().setText(String.valueOf(percentValue) + " %"); //$NON-NLS-1$
		getProgressBar().setSelection(percentValue);
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
		showAndProcess();

	}

	private void showAndProcess() {
		showWindow();
		startProcessing();
	}

	public void setDescription(String text) {
		processWindow.setDescrition(text);
	}

	public void setTitle(String text) {
		processWindow.getShell().setText(text);
	}

	private ListenerList cancelListeners = new ListenerList();

	public void addCancelListener(ICancelListener listener) {
		cancelListeners.add(listener);
	}

	public void removeCancelListener(ICancelListener listener) {
		cancelListeners.remove(listener);
	}

	protected void fireCanceled() {
		for (Object listener : cancelListeners.getListeners()) {
			ICancelListener.class.cast(listener).canceled();
		}
	}
}
