/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.ridgets.swt;

import org.eclipse.core.databinding.BindingException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.ui.core.marker.HiddenMarker;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.swt.ChoiceComposite;
import org.eclipse.riena.ui.swt.CompletionCombo;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Ridget for an SWT control.
 */
public abstract class AbstractSWTRidget extends AbstractSWTWidgetRidget {

	/**
	 * The key of the SWT data property that identifies the (top) composite of a
	 * sub-module view.
	 */
	private static final String IS_SUB_MODULE_VIEW_COMPOSITE = "isSubModuleViewComposite"; //$NON-NLS-1$
	private final FocusManager focusManager = new FocusManager();
	private boolean focusable;

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
	public static void assertType(final Object uiControl, final Class<?> type) {
		if ((uiControl != null) && !(type.isAssignableFrom(uiControl.getClass()))) {
			final String expectedClassName = type.getSimpleName();
			final String controlClassName = uiControl.getClass().getSimpleName();
			throw new BindingException("uiControl of  must be a " + expectedClassName + " but was a " //$NON-NLS-1$ //$NON-NLS-2$
					+ controlClassName);
		}
	}

	public AbstractSWTRidget() {
		focusable = true;
	}

	@Override
	public Control getUIControl() {
		return (Control) super.getUIControl();
	}

	@Override
	public final void requestFocus() {
		if (isFocusable()) {
			if (getUIControl() != null) {
				final Control control = getUIControl();
				control.setFocus();
			}
		}
	}

	@Override
	public final boolean hasFocus() {
		if (getUIControl() != null) {
			final Control control = getUIControl();
			return control.isFocusControl();
		}
		return false;
	}

	@Override
	public final boolean isFocusable() {
		return focusable;
	}

	@Override
	public final void setFocusable(final boolean focusable) {
		if (this.focusable != focusable) {
			this.focusable = focusable;
		}
	}

	public boolean isVisible() {
		// check for "hidden.marker". This marker overrules any other visibility rule
		if (!getMarkersOfType(HiddenMarker.class).isEmpty()) {
			return false;
		}

		if (!SwtUtilities.isDisposed(getUIControl())) {
			// the swt control is bound
			if (isChildOfSubModuleView(getUIControl())) {
				return isControlVisible(getUIControl());
			} else {
				return getUIControl().isVisible();
			}
		}
		// control is not bound
		return savedVisibleState;
	}

	/**
	 * Returns whether the given control is the (top) composite of a sub-module
	 * view.
	 * 
	 * @param uiControl
	 *            UI control
	 * @return {@code true} if control is composite of a sub-module; otherwise
	 *         {@code false}
	 */
	private boolean isSubModuleViewComposite(final Control uiControl) {

		if (!(uiControl instanceof Composite)) {
			return false;
		}

		if (uiControl.getData(IS_SUB_MODULE_VIEW_COMPOSITE) instanceof Boolean) {
			if (((Boolean) uiControl.getData(IS_SUB_MODULE_VIEW_COMPOSITE))) {
				return true;
			}
		}
		return false;

	}

	/**
	 * Returns whether the given control is a child of the (top) composite of a
	 * sub-module view.
	 * 
	 * @param uiControl
	 *            UI control
	 * @return {@code true} if control is child of a sub-module; otherwise
	 *         {@code false}
	 */
	private boolean isChildOfSubModuleView(final Control uiControl) {

		if (uiControl.getVisible()) {
			final Composite parent = uiControl.getParent();
			if (parent == null) {
				return false;
			}
			if (isSubModuleViewComposite(parent)) {
				return true;
			}
			return isChildOfSubModuleView(parent);
		} else {
			return false;
		}

	}

	/**
	 * Returns whether the given control is visible or invisible.
	 * <p>
	 * Similar to the SWT method isVisible of the class {@link Control} this
	 * method also checks if the parent composite are also visible. But this
	 * checks end at the top composite of a sub-module view.
	 * 
	 * @param uiControl
	 *            UI control
	 * @return {@code true} if control is visible; otherwise {@code false}
	 */
	private boolean isControlVisible(final Control uiControl) {

		if (uiControl.getVisible()) {
			final Composite parent = uiControl.getParent();
			if (parent == null) {
				return true;
			}
			if (isSubModuleViewComposite(parent)) {
				return true;
			}
			return isControlVisible(parent);
		} else {
			return false;
		}

	}

	@Override
	protected void unbindUIControl() {
		// save the state
		savedVisibleState = isVisible();
	}

	// helping methods
	// ////////////////

	/**
	 * Adds listeners to the <tt>uiControl</tt> after it was bound to the
	 * ridget.
	 */
	@Override
	protected final void installListeners() {
		super.installListeners();
		if (getUIControl() != null) {
			getUIControl().addFocusListener(focusManager);
			getUIControl().addMouseListener(focusManager);
		}
	}

	/**
	 * Removes listeners from the <tt>uiControl</tt> when it is about to be
	 * unbound from the ridget.
	 */
	@Override
	protected final void uninstallListeners() {
		if (getUIControl() != null) {
			getUIControl().removeFocusListener(focusManager);
			getUIControl().removeMouseListener(focusManager);
		}
		super.uninstallListeners();
	}

	@Override
	protected final void updateEnabled() {
		if (getUIControl() != null) {
			getUIControl().setEnabled(isEnabled());
		}
	}

	@Override
	protected final void updateToolTip() {
		if (getUIControl() != null) {
			getUIControl().setToolTipText(getToolTipText());
		}
	}

	/**
	 * Focus listener that also prevents the widget corresponding to this ridget
	 * from getting the UI focus when the ridget is not focusable or output
	 * only.
	 * <p>
	 * The algorithm is as follows:
	 * <ul>
	 * <li>if the widget is non-focusable, select the next focusable widget</li>
	 * <li>if the widget is output only, select the next focusable widget</li>
	 * <li>if the widget is output only and was clicked, accept focus</li>
	 * <li>in any other case, accept focus</li>
	 * </ul>
	 * Implementation note: SWT will invoke the focusGained, focusLost methods
	 * before the mouseDown method.
	 * 
	 * TODO [ev] update javadoc
	 * 
	 * @see AbstractSWTRidget#setFocusable(boolean).
	 */
	private final class FocusManager extends MouseAdapter implements FocusListener {

		private boolean clickToFocus;

		public void focusGained(final FocusEvent e) {
			if (isFocusable()) {
				trace("## focus gained: %s %d", e.widget, e.widget.hashCode());
				fireFocusGained(new org.eclipse.riena.ui.ridgets.listener.FocusEvent(null, AbstractSWTRidget.this));
			} else {
				final Control target = findFocusTarget((Control) e.widget);
				if (target != null) {
					trace("## %s %d -> %s %d", e.widget, e.widget.hashCode(), target, target.hashCode());
					target.setFocus();
				} else { // no suitable control found, try one level up
					trace("!! %s %d -> NO TARGET", e.widget, e.widget.hashCode());
				}
			}
		}

		public void focusLost(final FocusEvent e) {
			if (isFocusable()) {
				clickToFocus = false;
				fireFocusLost(new org.eclipse.riena.ui.ridgets.listener.FocusEvent(AbstractSWTRidget.this, null));
			}
		}

		@Override
		public void mouseDown(final MouseEvent e) {
			if (focusable && isOutputOnly()) {
				trace("## mouse DOWN: %s %d", e.widget, e.widget.hashCode());
				clickToFocus = true;
				((Control) e.widget).setFocus();
			}
		}

		// helping methods
		//////////////////

		/**
		 * Tests whether the given control can get the focus or cannot.
		 * 
		 * @param control
		 *            UI control
		 * @return {@code true} if control can get the focus; otherwise
		 *         {@code false}.
		 */
		private boolean canGetFocus(final Control control) {
			// skip disabled or hidden
			if (!control.isEnabled() || !control.isVisible()) {
				return false;
			}
			// skip read-only
			if (SwtUtilities.hasStyle(control, SWT.READ_ONLY)) {
				return false;
			}
			if (control instanceof Text && !((Text) control).getEditable()) {
				return false;
			}
			if (control instanceof ChoiceComposite && !((ChoiceComposite) control).getEditable()) {
				return false;
			}
			if (control instanceof CompletionCombo && !((CompletionCombo) control).getEditable()) {
				return false;
			}
			// skip IMarkableRidgets that are not focusable  or output only
			final Object data = control.getData("ridget");
			if (data instanceof IMarkableRidget) {
				final IMarkableRidget markableRidget = (IMarkableRidget) data;
				return markableRidget.isFocusable() && !markableRidget.isOutputOnly();
			}
			// skip IRidgets that are not focusable
			if (data instanceof IRidget) {
				final IRidget ridget = (IRidget) data;
				return ridget.isFocusable();
			}
			// skip Composites that have no children that can get focus
			if (control instanceof Composite) {
				return findFocusTarget(null, (Composite) control) != null;
			}
			return true;
		}

		private Control findFocusTarget(final Control control) {
			Control result = null;
			Control start = control;
			while (start.getParent() != null && result == null) {
				final Composite parent = start.getParent();
				result = findFocusTarget(start, parent);
				start = parent;
			}
			return result;
		}

		private Control findFocusTarget(final Control start, final Composite parent) {
			Control result = null;
			final Control[] siblings = parent.getTabList();
			int myIndex = -1;
			// find index for control
			for (int i = 0; myIndex == -1 && i < siblings.length; i++) {
				if (siblings[i] == start) {
					myIndex = i;
				}
			}
			// find next possible control
			for (int i = myIndex + 1; result == null && i < siblings.length; i++) {
				final Control candidate = siblings[i];
				if (canGetFocus(candidate)) {
					result = candidate;
				}
			}
			// find previous possible control
			for (int i = 0; result == null && i < myIndex; i++) {
				final Control candidate = siblings[i];
				if (canGetFocus(candidate)) {
					result = candidate;
				}
			}
			return result;
		}

		private boolean isFocusable() {
			return (focusable && !isOutputOnly()) || clickToFocus;
		}

		private void trace(final String format, final Object... args) {
			// System.out.println(String.format(format, args));
		}
	}

}
