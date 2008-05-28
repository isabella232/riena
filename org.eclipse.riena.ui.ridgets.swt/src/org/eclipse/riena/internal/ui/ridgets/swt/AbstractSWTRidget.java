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

import org.eclipse.core.databinding.BindingException;
import org.eclipse.riena.ui.ridgets.AbstractRidget;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

/**
 * Ridget for an SWT control.
 */
public abstract class AbstractSWTRidget extends AbstractRidget {

	private Control uiControl;
	private boolean visible;
	private boolean focusable;
	private String toolTip = null;

	/**
	 * Checks that the given uiControl is assignable to the the given type.
	 * 
	 * @param uiControl
	 *            a uiControl, may be null
	 * @param type
	 *            a class instance (non-null)
	 * @throws BindingException
	 *             if the uiControl is not of the given type
	 */
	public static void assertType(Object uiControl, Class<?> type) {
		if ((uiControl != null) && !(type.isAssignableFrom(uiControl.getClass()))) {
			String expectedClassName = type.getSimpleName();
			String controlClassName = uiControl.getClass().getSimpleName();
			throw new BindingException("uiControl of  must be a " + expectedClassName + " but was a " //$NON-NLS-1$ //$NON-NLS-2$
					+ controlClassName);
		}
	}

	public AbstractSWTRidget() {
		visible = true;
		focusable = true;
	}

	public void setUIControl(Object uiControl) {
		checkUIControl(uiControl);
		unbindUIControl();
		this.uiControl = (Control) uiControl;
		updateVisible();
		updateToolTip();
		// updateFocusable();
		bindUIControl();
	}

	public Control getUIControl() {
		return (Control) uiControl;
	}

	public final void requestFocus() {
		if (isFocusable()) {
			if ((getUIControl() != null) && (getUIControl() instanceof Control)) {
				Control control = (Control) getUIControl();
				control.setFocus();
			}
		}
	}

	public final boolean hasFocus() {
		if ((getUIControl() != null) && (getUIControl() instanceof Control)) {
			Control control = (Control) getUIControl();
			return control.isFocusControl();
		}
		return false;
	}

	public final boolean isFocusable() {
		return focusable;
	}

	public final void setFocusable(boolean focusable) {
		this.focusable = focusable;
	}

	public final boolean isVisible() {
		return uiControl == null ? visible : uiControl.isVisible();
	}

	public void setVisible(boolean visible) {
		if (this.visible != visible) {
			this.visible = visible;
			updateVisible();
		}
	}

	public final void setToolTipText(String toolTipText) {
		this.toolTip = toolTipText;
		updateToolTip();
	}

	public final String getToolTipText() {
		return toolTip;
	}

	/**
	 * <p>
	 * Performs checks on the control about to be bound by this ridget.
	 * </p>
	 * <p>
	 * Implementors must make sure the given <tt>uiControl</tt> has the
	 * expected type.
	 * </p>
	 * 
	 * @param uiControl
	 *            a {@link Widget} instance or null
	 * @throws BindingException
	 *             if the <tt>uiControl</tt> fails the check
	 */
	abstract protected void checkUIControl(Object uiControl);

	/**
	 * <p>
	 * Bind the current <tt>uiControl</tt> to the ridget.
	 * </p>
	 * <p>
	 * Implementors must call {@link #getUIControl()} to obtain the current
	 * control. If the control is non-null they must do whatever necessary to
	 * bind it to the ridget.
	 * </p>
	 */
	abstract protected void bindUIControl();

	/**
	 * <p>
	 * Unbind the current <tt>uiControl</tt> from the ridget.
	 * </p>
	 * <p>
	 * Implementors ensure they dispose the control-to-ridget binding and
	 * dispose any data structures that are not necessary in an unbound state.
	 * </p>
	 */
	abstract protected void unbindUIControl();

	private void updateVisible() {
		if (uiControl != null) {
			uiControl.setVisible(visible);
		}
	}

	private void updateToolTip() {
		if (uiControl != null) {
			uiControl.setToolTipText(toolTip);
		}
	}
}
