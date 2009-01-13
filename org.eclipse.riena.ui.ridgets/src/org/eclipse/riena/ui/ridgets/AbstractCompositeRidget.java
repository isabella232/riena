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

	public boolean isVisible() {
		return uiControl != null && visible;
	}

	public void setVisible(boolean visible) {
		if (this.visible != visible) {
			this.visible = visible;
			updateVisible();
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

		this.uiControl = (IComplexComponent) uiControl;
		updateVisible();
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
		Collection<? extends IRidget> r = getRidgets();
		for (Iterator<? extends IRidget> iterator = r.iterator(); iterator.hasNext();) {
			IRidget object = iterator.next();
			object.setFocusable(focusable);

		}
	}

	public boolean isFocusable() {
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

	protected void updateVisible() {
		// TODO [ev] javadoc + bugzilla
		if (uiControl != null && uiControl instanceof Component) {
			((Component) uiControl).setVisible(this.visible);
		}
	}

	protected void updateToolTipText() {
		// TODO [ev] javadoc + comment
	}

}
