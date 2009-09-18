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

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.ui.common.IComplexComponent;
import org.eclipse.riena.ui.ridgets.controller.AbstractWindowController;
import org.eclipse.riena.ui.swt.RienaDialogRenderer;
import org.eclipse.riena.ui.swt.lnf.LnFUpdater;
import org.eclipse.riena.ui.swt.utils.SWTBindingPropertyLocator;

/**
 * TODO [ev] javadoc
 */
public abstract class AbstractDialogView extends Dialog {

	private static final LnFUpdater LNF_UPDATER = new LnFUpdater();

	private final RienaDialogRenderer dlgRenderer;
	private final ControlledView controlledView;

	private String title;
	private boolean isClosing;

	private static Shell getShellByGuessing() {
		Shell result;
		if (PlatformUI.isWorkbenchRunning()) {
			result = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		} else {
			result = Display.getCurrent().getActiveShell();
		}
		Assert.isNotNull(result);
		return result;
	}

	protected AbstractDialogView(Shell parentShell) {
		super(parentShell != null ? parentShell : getShellByGuessing());
		title = ""; //$NON-NLS-1$
		dlgRenderer = new RienaDialogRenderer(this);
		controlledView = new ControlledView();
		controlledView.setController(createController());
	}

	@Override
	public void create() {
		// compute the 'styled' shell style, before creating the shell
		setShellStyle(dlgRenderer.computeShellStyle());
		super.create();
		applyTitle(getShell());
		addUIControls(getShell());
		bindController();
		LNF_UPDATER.updateUIControlsAfterBind(getShell());
		getShell().addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				if (!isClosing) {
					close();
				}
			}
		});
	}

	@Override
	public boolean close() {
		isClosing = true;
		AbstractWindowController controller = getController();
		setReturnCode(controller.getReturnCode());
		controlledView.unbind(controller);
		return super.close();
	}

	public final AbstractWindowController getController() {
		return controlledView.getController();
	}

	/**
	 * TODO [ev] javadoc
	 * 
	 * @param title
	 */
	public final void setTitle(String title) {
		Assert.isNotNull(title);
		this.title = title;
	}

	/**
	 * @deprecated use open
	 */
	public void build() {
		open();
	}

	// protected methods
	////////////////////

	protected final void addUIControl(Object uiControl, String ridgetId) {
		controlledView.addUIControl(uiControl, ridgetId);
	}

	@Override
	protected final Control createButtonBar(Composite parent) {
		return dlgRenderer.createButtonBar(parent);
	}

	@Override
	protected final Control createContents(Composite parent) {
		Control result = dlgRenderer.createContents(parent);
		super.createContents(dlgRenderer.getCenterComposite());
		return result;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Control result = buildView(parent);
		addUIControl(getShell(), AbstractWindowController.RIDGET_ID_WINDOW);
		LNF_UPDATER.updateUIControls(parent);
		return result;
	}

	/**
	 * TODO [ev] javadoc
	 */
	protected abstract Control buildView(Composite parent);

	/**
	 * TODO [ev] javadoc
	 */
	protected abstract AbstractWindowController createController();

	// helping methods
	//////////////////

	private void applyTitle(Shell shell) {
		if (shell.getText().length() == 0) {
			shell.setText(title);
		}
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

	private void bindController() {
		controlledView.initialize(getController());
		controlledView.bind(getController());
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

	// helping classes
	//////////////////

	private static final class ControlledView extends AbstractControlledView<AbstractWindowController> {
		@Override
		protected void addUIControl(Object uiControl, String propertyName) {
			super.addUIControl(uiControl, propertyName);
		}

		@Override
		protected void setController(AbstractWindowController controller) {
			super.setController(controller);
		}
	}
}
