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
import org.eclipse.riena.ui.ridgets.IComplexComponent;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

/**
 * Status bar (status line)
 */
public class Statusbar extends Composite implements IComplexComponent {

	private final static String NUMBER_NAME = "statusBarNumberRidget"; //$NON-NLS-1$
	private List<Object> uiControls;
	private StatusbarMessage message;
	private Class<? extends Control> spacer;

	/**
	 * Creates a new instance of <code>Statusbar</code>.
	 * 
	 * @param parent
	 *            - a widget which will be the parent of the new instance
	 *            (cannot be null)
	 * @param style
	 *            - the style of widget to construct
	 */
	public Statusbar(Composite parent, int style) {
		super(parent, style | SWT.NO_SCROLL);
		uiControls = new ArrayList<Object>();
		createContents();
	}

	/**
	 * Creates a new instance of <code>Statusbar</code>.
	 * 
	 * @param parent
	 *            - a widget which will be the parent of the new instance
	 *            (cannot be null)
	 * @param style
	 *            - the style of widget to construct
	 * @param pSpacer
	 *            - class to create spacer
	 */
	public Statusbar(Composite parent, int style, Class<? extends Control> pSpacer) {
		super(parent, style | SWT.NO_SCROLL);
		uiControls = new ArrayList<Object>();
		spacer = pSpacer;
		createContents();
	}

	/**
	 * Creates the contents of the status bar.
	 */
	protected void createContents() {

		setLayout(new FormLayout());

		StatusbarTime time = new StatusbarTime(this, SWT.NONE);
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

		StatusbarDate date = new StatusbarDate(this, SWT.NONE);
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

		StatusbarNumber number = new StatusbarNumber(this, SWT.NONE);
		addUIControl(number, NUMBER_NAME);
		formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.bottom = new FormAttachment(100, 0);
		formData.right = new FormAttachment(lastControl, 0);
		number.setLayoutData(formData);
		lastControl = number;

		spacerControl = createSpacer(this);
		if (spacerControl != null) {
			formData = new FormData();
			formData.top = new FormAttachment(0, 0);
			formData.bottom = new FormAttachment(100, 0);
			formData.right = new FormAttachment(lastControl, 0);
			spacerControl.setLayoutData(formData);
			lastControl = spacerControl;
		}

		message = new StatusbarMessage(this, SWT.NONE);
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
	 * @see org.eclipse.riena.ui.ridgets.IComplexComponent#getName()
	 */
	public String getName() {
		return null;
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IComplexComponent#getUIControls()
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
		uiControl.setData(SWTBindingPropertyLocator.BINDING_PROPERTY, propertyName);
		getUIControls().add(uiControl);
	}

	/**
	 * @return Returns the message.
	 */
	public final StatusbarMessage getMessageComposite() {
		return message;
	}

}
