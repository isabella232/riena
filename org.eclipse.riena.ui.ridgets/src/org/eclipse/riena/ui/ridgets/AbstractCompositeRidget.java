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
package org.eclipse.riena.ui.ridgets;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.riena.ui.common.IComplexComponent;

/**
 * 
 */
public abstract class AbstractCompositeRidget extends AbstractRidget implements IComplexRidget {

	private IComplexComponent uiControl;
	private Map<String, IRidget> ridgets;
	private PropertyChangeListener propertyChangeListener;
	protected boolean visible;
	private String toolTip = null;
	private boolean blocked;

	/**
	 * Constructor
	 */
	public AbstractCompositeRidget() {

		super();

		ridgets = new HashMap<String, IRidget>();
		propertyChangeListener = new PropertyChangeHandler();
		visible = true;
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IRidget#isVisible()
	 */
	public boolean isVisible() {
		return uiControl != null && visible;
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IRidget#setVisible(boolean)
	 */
	public void setVisible(boolean visible) {
		if (this.visible != visible) {
			this.visible = visible;
			updateVisible();
		}
	}

	private void updateVisible() {
		if (uiControl != null && uiControl instanceof Component) {
			((Component) uiControl).setVisible(this.visible);
		}
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IRidget#getUIControl()
	 */
	public IComplexComponent getUIControl() {
		return uiControl;
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IRidget#setUIControl(java.lang.Object)
	 */
	public void setUIControl(Object uiControl) {

		if (uiControl != null && !(uiControl instanceof IComplexComponent)) {
			throw new UIBindingFailure("uiControl of a AbstractCompositeRidget must be a IComplexComponent but was a " //$NON-NLS-1$
					+ uiControl.getClass().getSimpleName());
		}

		this.uiControl = (IComplexComponent) uiControl;
		updateVisible();
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IRidgetContainer#addRidget(java.lang.String,
	 *      org.eclipse.riena.ui.internal.ridgets.IRidget)
	 */
	public void addRidget(String id, IRidget ridget) {

		ridget.addPropertyChangeListener(propertyChangeListener);
		ridgets.put(id, ridget);
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IRidgetContainer#getRidget(java.lang.String)
	 */
	public IRidget getRidget(String id) {

		return ridgets.get(id);

	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IRidgetContainer#getRidgets()
	 */
	public Collection<? extends IRidget> getRidgets() {
		return ridgets.values();
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IRidget#requestFocus()
	 */
	public void requestFocus() {
		if (!getRidgets().isEmpty()) {
			getRidgets().iterator().next().requestFocus();
		}
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IRidget#hasFocus()
	 */
	public boolean hasFocus() {
		Collection<? extends IRidget> myRidgets = getRidgets();
		for (IRidget ridget : myRidgets) {
			if (ridget.hasFocus()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IRidget#setFocusable(boolean)
	 */
	public void setFocusable(boolean focusable) {
		Collection<? extends IRidget> r = getRidgets();
		for (Iterator<? extends IRidget> iterator = r.iterator(); iterator.hasNext();) {
			IRidget object = iterator.next();
			object.setFocusable(focusable);

		}
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IRidget#isFocusable()
	 */
	public boolean isFocusable() {
		return false;
	}

	private class PropertyChangeHandler implements PropertyChangeListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @seejava.beans.PropertyChangeListener#propertyChange(java.beans.
		 * PropertyChangeEvent)
		 */
		public void propertyChange(PropertyChangeEvent evt) {

			propertyChangeSupport.firePropertyChange(evt);
		}
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IRidget#getToolTipText()
	 */
	public String getToolTipText() {
		return toolTip;
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IRidget#setToolTipText(java.lang.String)
	 */
	public void setToolTipText(String toolTipText) {
		toolTip = toolTipText;

	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IRidget#isBlocked()
	 */
	public boolean isBlocked() {
		return blocked;
	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IRidget#setBlocked(boolean)
	 */
	public void setBlocked(boolean blocked) {

		Collection<? extends IRidget> r = getRidgets();
		for (Iterator<? extends IRidget> iterator = r.iterator(); iterator.hasNext();) {
			IRidget object = iterator.next();
			object.setBlocked(blocked);

		}

	}

	/**
	 * @see org.eclipse.riena.ui.internal.ridgets.IRidgetContainer#configureRidgets()
	 */
	public void configureRidgets() {
	}

	public String getID() {
		return null;
	}

}
