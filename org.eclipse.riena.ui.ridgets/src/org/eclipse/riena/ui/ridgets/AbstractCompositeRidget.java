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
package org.eclipse.riena.ui.ridgets;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.databinding.BindingException;

import org.eclipse.riena.ui.common.IComplexComponent;

/**
 * 
 */
public abstract class AbstractCompositeRidget extends AbstractRidget implements IComplexRidget {

	private IComplexComponent uiControl;
	private Map<String, IRidget> ridgets;
	private PropertyChangeListener propertyChangeListener;
	protected boolean markedHidden; // TODO add MarkerSupport instead of boolean flag

	private boolean enabled = true;
	private String toolTip = null;
	private boolean blocked;

	/**
	 * Constructor
	 */
	public AbstractCompositeRidget() {
		super();
		ridgets = new HashMap<String, IRidget>();
		propertyChangeListener = new PropertyChangeHandler();
		markedHidden = false;
	}

	public boolean isVisible() {
		// check for "hidden.marker". This marker overrules any other visibility rule
		if (markedHidden) {
			return false;
		}

		if (getUIControl() != null) {
			// the swt control is bound
			return isUIControlVisible();
		}
		// control is not bound
		return savedVisibleState;
	}

	protected boolean isUIControlVisible() {
		return true;
	}

	public void setVisible(boolean visible) {
		if (this.markedHidden == visible) {
			this.markedHidden = !visible;
			updateVisible();
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		if (this.enabled != enabled) {
			this.enabled = enabled;
			updateEnabled();
		}
	}

	public IComplexComponent getUIControl() {
		return uiControl;
	}

	public void setUIControl(Object uiControl) {
		if (uiControl != null && !(uiControl instanceof IComplexComponent)) {
			throw new UIBindingFailure("uiControl of a AbstractCompositeRidget must be a IComplexComponent but was a " //$NON-NLS-1$
					+ uiControl.getClass().getSimpleName());
		}
		checkUIControl(uiControl);
		unbindUIControl();
		// save state
		this.savedVisibleState = getUIControl() != null ? isUIControlVisible() : savedVisibleState;
		this.uiControl = (IComplexComponent) uiControl;
		updateVisible();
		updateEnabled();
		updateToolTipText();
		bindUIControl();
	}

	/**
	 * Performs checks on the control about to be bound by this ridget.
	 * <p>
	 * Implementors must make sure the given <tt>uiControl</tt> has the expected
	 * type.
	 * 
	 * @param uiControl
	 *            a {@link Widget} instance or null
	 * @throws BindingException
	 *             if the <tt>uiControl</tt> fails the check
	 * @since 1.2
	 */
	protected void checkUIControl(Object uiControl) {
		// implementors should overwrite
	}

	/**
	 * Bind the current <tt>uiControl</tt> to the ridget.
	 * <p>
	 * Implementors must call {@link #getUIControl()} to obtain the current
	 * control. If the control is non-null they must do whatever necessary to
	 * bind it to the ridget.
	 * 
	 * @since 1.2
	 */
	protected void bindUIControl() {
		// implementors should overwrite
	}

	/**
	 * Unbind the current <tt>uiControl</tt> from the ridget.
	 * <p>
	 * Implementors ensure they dispose the control-to-ridget binding and
	 * dispose any data structures that are not necessary in an unbound state.
	 * 
	 * @since 1.2
	 */
	protected void unbindUIControl() {
		// implementors should overwrite
	}

	public void addRidget(String id, IRidget ridget) {

		ridget.addPropertyChangeListener(propertyChangeListener);
		ridgets.put(id, ridget);
	}

	public IRidget getRidget(String id) {

		return ridgets.get(id);

	}

	public Collection<? extends IRidget> getRidgets() {
		return ridgets.values();
	}

	public void requestFocus() {
		if (!getRidgets().isEmpty()) {
			getRidgets().iterator().next().requestFocus();
		}
	}

	public boolean hasFocus() {
		Collection<? extends IRidget> myRidgets = getRidgets();
		for (IRidget ridget : myRidgets) {
			if (ridget.hasFocus()) {
				return true;
			}
		}
		return false;
	}

	public void setFocusable(boolean focusable) {
		for (IRidget ridget : getRidgets()) {
			ridget.setFocusable(focusable);
		}
	}

	public boolean isFocusable() {
		for (IRidget ridget : getRidgets()) {
			if (ridget.isFocusable()) {
				return true;
			}
		}
		return false;
	}

	private class PropertyChangeHandler implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {

			propertyChangeSupport.firePropertyChange(evt);
		}
	}

	public String getToolTipText() {
		return toolTip;
	}

	public void setToolTipText(String toolTipText) {
		String oldValue = toolTip;
		toolTip = toolTipText;
		updateToolTipText();
		firePropertyChange(IRidget.PROPERTY_TOOLTIP, oldValue, toolTip);
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {

		Collection<? extends IRidget> r = getRidgets();
		for (Iterator<? extends IRidget> iterator = r.iterator(); iterator.hasNext();) {
			IRidget object = iterator.next();
			object.setBlocked(blocked);

		}

	}

	public void configureRidgets() {
	}

	public String getID() {
		return null;
	}

	// protected methods
	////////////////////

	/**
	 * Updates the visibility of the complex UI control (and of the UI controls
	 * it contains). This default implementation does nothing and should be
	 * overridden by subclasses.
	 */
	protected void updateVisible() {
		// empty default implementation
	}

	/**
	 * Updates the enabled state of the complex UI control (and of the UI
	 * controls it contains). This default implementation does nothing and
	 * should be overridden by subclasses.
	 */
	protected void updateEnabled() {
		// empty default implementation
	}

	/**
	 * Does nothing by default.
	 * <p>
	 * Subclasses should override to update the tooltip(s) of their controls in
	 * an appropriate way.
	 */
	protected void updateToolTipText() {
		// does nothing
	}

}
