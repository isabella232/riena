/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.core.uiprocess;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ProcessInfo {
	public static final String PROPERTY_TITLE = "title"; //$NON-NLS-1$
	public static final String PROPERTY_ICON = "icon"; //$NON-NLS-1$
	public static final String PROPERTY_NOTE = "note"; //$NON-NLS-1$
	public static final String PROPERTY_MAX_PROGRESS = "maxProgress"; //$NON-NLS-1$
	public static final String PROPERTY_ACTUAL_PROGRESS = "actualProgress"; //$NON-NLS-1$
	public static final String PROPERTY_CANCELED = "cancel"; //$NON-NLS-1$
	public static final String DIALOG_VISIBLE = "dialog.visible"; //$NON-NLS-1$

	public enum Style {
		Plain(), Dialog();
	}

	public final static Style STYLE_PLAIN = Style.Plain;
	public final static Style STYLE_DIALOG = Style.Dialog;

	private String title;
	private String icon;
	private String note;
	private int maxProgress = 0;
	private int actualProgress = 0;
	private boolean dialogVisible = true;
	private Style style = STYLE_PLAIN;
	private PropertyChangeSupport ppSupport;
	private Object context;
	private boolean canceled;
	private boolean ignoreCancel;

	public ProcessInfo() {
		ppSupport = new PropertyChangeSupport(this);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		ppSupport.addPropertyChangeListener(listener);
	}

	public void setIgnoreCancel(boolean ignoreCancel) {
		this.ignoreCancel = ignoreCancel;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		String old = this.title;
		this.title = title;
		ppSupport.firePropertyChange(PROPERTY_TITLE, old, title);
	}

	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon
	 *            the icon to set
	 */
	public void setIcon(String icon) {
		String old = this.icon;
		this.icon = icon;
		ppSupport.firePropertyChange(PROPERTY_ICON, old, icon);
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		String old = this.note;
		this.note = note;
		ppSupport.firePropertyChange(PROPERTY_NOTE, old, note);

	}

	/**
	 * @return the maxProgress
	 */
	public int getMaxProgress() {
		return maxProgress;
	}

	/**
	 * @param maxProgress
	 *            the maxProgress to set
	 */
	public void setMaxProgress(int maxProgress) {
		int old = this.maxProgress;
		this.maxProgress = maxProgress;
		ppSupport.firePropertyChange(PROPERTY_MAX_PROGRESS, old, maxProgress);
	}

	/**
	 * @return the actualProgress
	 */
	public int getActualProgress() {
		return actualProgress;
	}

	/**
	 * @param actualProgress
	 *            the actualProgress to set
	 */
	public void setActualProgress(int actualProgress) {
		int old = this.actualProgress;
		this.actualProgress = actualProgress;
		ppSupport.firePropertyChange(PROPERTY_ACTUAL_PROGRESS, old, actualProgress);
	}

	/**
	 * @return the style
	 */
	public Style getStyle() {
		return style;
	}

	public boolean isCanceled() {
		return canceled;
	}

	public void cancel() {
		if (!ignoreCancel) {
			canceled = true;
		}
		ppSupport.firePropertyChange(PROPERTY_CANCELED, false, true);
	}

	public void setDialogVisible(boolean visible) {
		boolean old = this.dialogVisible;
		this.dialogVisible = visible;
		ppSupport.firePropertyChange(DIALOG_VISIBLE, old, visible);
	}

	public boolean isDialogVisible() {
		return dialogVisible;
	}

	/**
	 * @param context
	 *            the context to set
	 */
	public void setContext(Object context) {
		this.context = context;
	}

	/**
	 * @return the context
	 */
	public Object getContext() {
		return context;
	}

}
