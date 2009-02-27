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
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.riena.internal.navigation.ui.swt.utils.RcpUtilities;
import org.eclipse.riena.ui.ridgets.controller.AbstractWindowController;
import org.eclipse.riena.ui.swt.RienaDialog;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * base class for SWT dialogs.
 */
public abstract class DialogView extends AbstractControlledView<AbstractWindowController> {

	private final static LnFUpdater LNF_UPDATER = new LnFUpdater();

	private Composite parent;
	protected Dialog dialog;

	/**
	 * @param parent
	 *            the parent control.
	 */
	public DialogView(Composite parent) {

		super();

		initializeParent(parent);
	}

	/**
	 * Creates the dialog of this view
	 */
	protected ControlledRienaDialog createDialog() {

		return new ControlledRienaDialog(getParentShell());
	}

	protected void createAndBindController() {

		AbstractWindowController controller = createController();
		initialize(controller);
		bind(controller);

	}

	protected Control buildView(Composite parent) {

		addUIControl(dialog.getShell(), AbstractWindowController.RIDGET_ID_WINDOW);

		return parent;
	}

	protected abstract AbstractWindowController createController();

	/**
	 * Build and open the a dialog.
	 */
	public void build() {

		if (dialog == null) {
			dialog = createDialog();
		}

		dialog.open();
	}

	private void initializeParent(Composite parent) {
		if (parent == null) {
			this.parent = RcpUtilities.getWorkbenchShell();
		} else {
			this.parent = parent;
		}
	}

	private Shell getParentShell() {

		if (parent != null) {
			return parent.getShell();
		} else {
			return null;
		}
	}

	protected void onClose() {
		// Do nothing by default
	}

	private final class ControlledRienaDialog extends RienaDialog {

		private boolean closing;

		private ControlledRienaDialog(Shell shell) {
			super(shell);
			closing = false;
		}

		/**
		 * Closes this dialog. But before closing the controller is unbinded.
		 * 
		 * @see org.eclipse.jface.dialogs.Dialog#close()
		 */
		@Override
		public boolean close() {

			closing = true;
			onClose();
			unbind(getController());
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

			createAndBindController();
			LNF_UPDATER.updateUIControlsAfterBind(getShell());

			getShell().addDisposeListener(new DisposeListener() {
				public void widgetDisposed(DisposeEvent e) {
					if (!closing) {
						close();
					}
				}
			});
			getShell().pack();

		}

		@Override
		protected Control createDialogArea(Composite parent) {
			Control dlgContente = buildView(parent);
			LNF_UPDATER.updateUIControls(parent);
			return dlgContente;
		}

	}

}
