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
package org.eclipse.riena.ui.ridgets.swt.views;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.ui.common.IComplexComponent;
import org.eclipse.riena.ui.ridgets.controller.AbstractWindowController;
import org.eclipse.riena.ui.swt.RienaDialog;
import org.eclipse.riena.ui.swt.lnf.LnFUpdater;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;

/**
 * Base class for SWT dialogs.
 * 
 * @deprecated use {@link AbstractDialogView}
 */
public abstract class DialogView extends RienaDialog {

	private final static LnFUpdater LNF_UPDATER = new LnFUpdater();
	private boolean closing;
	private AbstractControlledView<AbstractWindowController> controlledViewDelegate;
	private Shell parentShell;

	/**
	 * @param parent
	 *            the parent control.
	 */
	public DialogView(Composite parent) {
		super(null != parent ? parent.getShell() : Display.getDefault().getActiveShell());

		controlledViewDelegate = new AbstractControlledView<AbstractWindowController>() {
		};
		controlledViewDelegate.setController(createController());
		closing = false;

		if (parent != null) {
			parentShell = parent.getShell();
		}
	}

	private boolean isChildOfComplexComponent(Control uiControl) {
		if (uiControl.getParent() == null) {
			return false;
		}
		if (uiControl.getParent() instanceof IComplexComponent) {
			return true;
		}
		return isChildOfComplexComponent(uiControl.getParent());
	}

	private void addUIControls(Composite composite) {
		Control[] controls = composite.getChildren();
		for (Control uiControl : controls) {

			String bindingProperty = SWTBindingPropertyLocator.getInstance().locateBindingProperty(uiControl);
			if (!StringUtils.isEmpty(bindingProperty)) {
				if (isChildOfComplexComponent(uiControl)) {
					continue;
				}
				addUIControl(uiControl, bindingProperty);
			}
			if (uiControl instanceof Composite) {
				addUIControls((Composite) uiControl);
			}
		}
	}

	/**
	 * @return
	 * @since 2.0
	 */
	public AbstractWindowController getController() {
		return controlledViewDelegate.getController();
	}

	/**
	 * @param shell
	 * @param ridgetIdWindow
	 */
	public void addUIControl(Object uiControl, String ridgetId) {
		controlledViewDelegate.addUIControl(uiControl, ridgetId);
	}

	private void bindController() {
		controlledViewDelegate.initialize(getController());
		controlledViewDelegate.bind(getController());
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	protected Control buildView(Composite parent) {
		return parent;
	}

	protected abstract AbstractWindowController createController();

	/**
	 * Build and open the a dialog.
	 */
	public void build() {
		open();
	}

	/**
	 * @since 2.0
	 */
	protected Shell getParentShell() {
		return parentShell;
	}

	protected void onClose() {
		// Do nothing by default
	}

	/**
	 * Closes this dialog. But before closing the controller is unbound.
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#close()
	 */
	@Override
	public boolean close() {
		closing = true;
		onClose();
		controlledViewDelegate.unbind(getController());
		boolean result = super.close();
		closing = false;
		return result;
	}

	/**
	 * Creates the dialog (also indirectly the view) and the corresponding
	 * controller. Binds view and controller
	 * 
	 * @see org.eclipse.riena.ui.swt.RienaDialog#create()
	 */
	@Override
	public void create() {
		super.create();
		addUIControls(getShell());
		bindController();
		LNF_UPDATER.updateUIControlsAfterBind(getShell());

		getShell().addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				if (!closing) {
					close();
				}
			}
		});
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Control dlgContente = buildView(parent);
		addUIControl(getShell(), AbstractWindowController.RIDGET_ID_WINDOW);
		LNF_UPDATER.updateUIControls(parent);
		return dlgContente;
	}
}
