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
package org.eclipse.riena.ui.swt;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.common.IComplexComponent;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

/**
 * Status line.
 */
public class Statusline extends Composite implements IComplexComponent {

	/**
	 * 
	 */
	private final static String NUMBER_NAME = "statuslineNumberRidget"; //$NON-NLS-1$
	private static final String UIPROCES_NAME = "statuslineUIProcessRidget"; //$NON-NLS-1$
	private List<Object> uiControls;
	private StatuslineMessage message;
	private Class<? extends Control> spacer;

	/**
	 * Creates a new instance of <code>Statusline</code>.
	 * 
	 * @param parent
	 *            - a widget which will be the parent of the new instance
	 *            (cannot be null)
	 * @param style
	 *            - the style of widget to construct
	 */
	public Statusline(Composite parent, int style) {
		super(parent, style | SWT.NO_SCROLL);
	}

	/**
	 * Creates a new instance of <code>Statusline</code>.
	 * 
	 * @param parent
	 *            - a widget which will be the parent of the new instance
	 *            (cannot be null)
	 * @param style
	 *            - the style of widget to construct
	 * @param pSpacer
	 *            - class to create spacer
	 */
	public Statusline(Composite parent, int style, Class<? extends Control> pSpacer) {
		super(parent, style | SWT.NO_SCROLL);
		spacer = pSpacer;
		init();
	}

	/**
	 * Initializes the status line.
	 */
	private void init() {
		setBackground(LnfManager.getLnf().getColor(ILnfKeyConstants.STATUSLINE_BACKGROUND));
		uiControls = new ArrayList<Object>();
		createContents();
	}

	/**
	 * Creates the contents of the status line.
	 */
	protected void createContents() {

		setLayout(new FormLayout());

		StatuslineTime time = new StatuslineTime(this, SWT.NONE);
		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);
		formData.right = new FormAttachment(100, 0);
		time.setLayoutData(formData);
		Control lastControl = time;

		Control spacerControl = createSpacer(this);
		if (spacerControl != null) {
			formData = new FormData();
			formData.top = new FormAttachment(0, 0);
			formData.bottom = new FormAttachment(100, 0);
			formData.right = new FormAttachment(lastControl, 0);
			spacerControl.setLayoutData(formData);
			lastControl = spacerControl;
		}

		StatuslineDate date = new StatuslineDate(this, SWT.NONE);
		formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);
		formData.right = new FormAttachment(lastControl, 0);
		date.setLayoutData(formData);
		lastControl = date;

		spacerControl = createSpacer(this);
		if (spacerControl != null) {
			formData = new FormData();
			formData.top = new FormAttachment(0, 0);
			formData.bottom = new FormAttachment(100, 0);
			formData.right = new FormAttachment(lastControl, 0);
			spacerControl.setLayoutData(formData);
			lastControl = spacerControl;
		}

		StatuslineNumber number = new StatuslineNumber(this, SWT.NONE);
		addUIControl(number, NUMBER_NAME);
		formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);
		formData.right = new FormAttachment(lastControl, 0);
		number.setLayoutData(formData);
		lastControl = number;

		// add StatuslineUIProcess
		spacerControl = createSpacer(this);
		if (spacerControl != null) {
			formData = new FormData();
			formData.top = new FormAttachment(0, 0);
			formData.bottom = new FormAttachment(100, 0);
			formData.right = new FormAttachment(lastControl, 0);
			spacerControl.setLayoutData(formData);
			lastControl = spacerControl;
		}

		StatuslineUIProcess uiProcess = new StatuslineUIProcess(this, SWT.NONE);
		addUIControl(uiProcess, UIPROCES_NAME);
		formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);
		formData.right = new FormAttachment(lastControl, 0);
		uiProcess.setLayoutData(formData);
		lastControl = uiProcess;

		// add StatuslineMessage
		spacerControl = createSpacer(this);
		if (spacerControl != null) {
			formData = new FormData();
			formData.top = new FormAttachment(0, 0);
			formData.bottom = new FormAttachment(100, 0);
			formData.right = new FormAttachment(lastControl, 0);
			spacerControl.setLayoutData(formData);
			lastControl = spacerControl;
		}

		message = new StatuslineMessage(this, SWT.NONE);
		formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);
		formData.left = new FormAttachment(0, 0);
		formData.right = new FormAttachment(lastControl, 0);
		message.setLayoutData(formData);
		lastControl = message;

	}

	/**
	 * Creates a spacer.
	 * 
	 * @param parent
	 * @return spacer
	 */
	private Control createSpacer(Composite parent) {

		Control result = null;
		if (spacer != null) {
			try {
				result = ReflectionUtils.newInstance(spacer, parent, SWT.NONE);
			} catch (Exception e) {
				result = null;
			}
		}

		return result;

	}

	/**
	 * @see org.eclipse.riena.ui.common.IComplexComponent#getUIControls()
	 */
	public List<Object> getUIControls() {
		return uiControls;
	}

	/**
	 * Adds the given control to the list of the controls that will be binded.
	 * 
	 * @param uiControl
	 *            - control to bind
	 * @param propertyName
	 *            - name of the property...
	 */
	protected void addUIControl(Widget uiControl, String propertyName) {
		SWTBindingPropertyLocator.getInstance().setBindingProperty(uiControl, propertyName);
		getUIControls().add(uiControl);
	}

	/**
	 * @return Returns the message.
	 */
	public final StatuslineMessage getMessageComposite() {
		return message;
	}

}
