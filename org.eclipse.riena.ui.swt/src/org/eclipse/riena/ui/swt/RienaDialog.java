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

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.riena.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.ILnfRenderer;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.renderer.AbstractTitleBarRenderer;
import org.eclipse.riena.ui.swt.lnf.renderer.DialogBorderRenderer;
import org.eclipse.riena.ui.swt.lnf.renderer.DialogTitleBarRenderer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class RienaDialog extends Dialog {

	private boolean hideOsBorder;
	private AbstractTitleBarMouseListener mouseListener;

	public RienaDialog(Shell shell) {
		super(shell);
	}

	@Override
	public void create() {
		initDialog();
		super.create();
	}

	/**
	 * Initializes the dialog before it is created.
	 */
	private void initDialog() {

		boolean hide = LnfManager.getLnf().getBooleanSetting(ILnfKeyConstants.DIALOG_HIDE_OS_BORDER);
		setHideOsBorder(hide);

	}

	/**
	 * Updates the style of the dialog shell.
	 */
	private void updateDialogStyle() {

		int style = getShellStyle();
		if (isHideOsBorder()) {
			style |= SWT.DIALOG_TRIM;
			style &= SWT.NO_TRIM;
		} else {
			style &= SWT.DIALOG_TRIM;
			style |= SWT.NO_TRIM;
		}
		setShellStyle(style);

	}

	@Override
	protected Control createContents(Composite parent) {

		int padding = 0;

		Composite contentsComposite = new Composite(parent, SWT.NONE);
		contentsComposite.setLayout(new FormLayout());
		if (isHideOsBorder()) {
			contentsComposite.addPaintListener(new DialogBorderPaintListener());
			DialogBorderRenderer borderRenderer = (DialogBorderRenderer) LnfManager.getLnf().getRenderer(
					ILnfKeyConstants.DIALOG_BORDER_RENDERER);
			padding = borderRenderer.getBorderWidth();
		}
		contentsComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		int titleBarHeight = 0;
		Composite topComposite = new Composite(contentsComposite, SWT.NONE);
		if (isHideOsBorder()) {
			topComposite.addPaintListener(new DialogPaintListener());
			DialogTitleBarRenderer titleBarRenderer = (DialogTitleBarRenderer) LnfManager.getLnf().getRenderer(
					ILnfKeyConstants.DIALOG_RENDERER);
			titleBarHeight = titleBarRenderer.getHeight();
			mouseListener = new DialogTitleBarMouseListener();
			topComposite.addMouseListener(mouseListener);
			topComposite.addMouseMoveListener(mouseListener);
			topComposite.addMouseTrackListener(mouseListener);
		}
		FormData formData = new FormData();
		formData.left = new FormAttachment(0, padding);
		formData.top = new FormAttachment(0, padding);
		formData.right = new FormAttachment(100, -padding);
		formData.bottom = new FormAttachment(0, titleBarHeight);
		topComposite.setLayoutData(formData);

		Composite centerComposite = new Composite(contentsComposite, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		centerComposite.setLayout(layout);
		formData = new FormData();
		formData.left = new FormAttachment(0, padding);
		formData.top = new FormAttachment(topComposite);
		formData.right = new FormAttachment(100, -padding);
		formData.bottom = new FormAttachment(100, -padding);
		centerComposite.setLayoutData(formData);

		super.createContents(centerComposite);

		return contentsComposite;

	}

	@Override
	protected Control createButtonBar(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridData data = new GridData();
		data.heightHint = 0;
		composite.setLayoutData(data);
		return composite;
	}

	public void setHideOsBorder(boolean hideOsBorder) {
		if (this.hideOsBorder != hideOsBorder) {
			this.hideOsBorder = hideOsBorder;
			updateDialogStyle();
		}
	}

	public boolean isHideOsBorder() {
		return hideOsBorder;
	}

	@Override
	public boolean close() {
		boolean close = super.close();
		if (close) {
			if (mouseListener != null) {
				mouseListener.dispose();
				mouseListener = null;
			}
		}

		return close;
	}

	/**
	 * This listener paints the dialog (the border of the shell).
	 */
	private static class DialogBorderPaintListener implements PaintListener {

		/**
		 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
		 */
		public void paintControl(final PaintEvent e) {
			onPaint(e);
		}

		/**
		 * Paints the border of the (titleless) shell.
		 * 
		 * @param e
		 *            - event
		 */
		private void onPaint(final PaintEvent e) {

			if (e.getSource() instanceof Control) {

				final Control dialog = (Control) e.getSource();

				final Rectangle dialogBounds = dialog.getBounds();
				final Rectangle bounds = new Rectangle(0, 0, dialogBounds.width, dialogBounds.height);

				final ILnfRenderer borderRenderer = LnfManager.getLnf().getRenderer(
						ILnfKeyConstants.DIALOG_BORDER_RENDERER);
				borderRenderer.setBounds(bounds);
				borderRenderer.paint(e.gc, null);

			}

		}

	}

	/**
	 * This listener paints the dialog (the border of the shell).
	 */
	private static class DialogPaintListener implements PaintListener {

		/**
		 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events.PaintEvent)
		 */
		public void paintControl(final PaintEvent e) {
			onPaint(e);
		}

		/**
		 * Paints the border of the (titleless) shell.
		 * 
		 * @param e
		 *            - event
		 */
		private void onPaint(final PaintEvent e) {

			if (e.getSource() instanceof Control) {

				final Control control = (Control) e.getSource();

				final Rectangle dialogBounds = control.getBounds();
				final DialogTitleBarRenderer renderer = (DialogTitleBarRenderer) LnfManager.getLnf().getRenderer(
						ILnfKeyConstants.DIALOG_RENDERER);
				renderer.setShell(control.getShell());
				final Rectangle bounds = new Rectangle(0, 0, dialogBounds.width, renderer.getHeight());
				renderer.setBounds(bounds);
				renderer.paint(e.gc, control);

			}

		}
	}

	private class DialogTitleBarMouseListener extends AbstractTitleBarMouseListener {

		@Override
		protected AbstractTitleBarRenderer getTitleBarRenderer() {
			return (DialogTitleBarRenderer) LnfManager.getLnf().getRenderer(ILnfKeyConstants.DIALOG_RENDERER);
		}

	}

}