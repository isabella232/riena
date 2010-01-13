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


/**
 * TODO [ev] javadoc
 * 
 * @since 2.0
 */
public abstract class AbstractMasterDetailsDelegate implements IMasterDetailsDelegate {

	/**
	 * {@inheritDoc}
	 * <p>
	 * Default implementation for this method. Subclasses may override.
	 */
	public boolean isChanged(Object source, Object target) {
		return true;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Default implementation for this method. Subclasses may override.
	 */
	public String isValid(IRidgetContainer container) {
		return null;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Default implementation for this method. Subclasses may override.
	 */
	public void itemCreated(Object newItem) {
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Default implementation for this method. Subclasses may override.
	 */
	public void itemApplied(Object changedItem) {
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Default implementation for this method. Subclasses may override.
	 */
	public void itemRemoved(Object oldItem) {
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Default implementation for this method. Subclasses may override.
	 */
	public void itemSelected(Object newSelection) {
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Default implementation for this method. Subclasses may override &ndash;
	 * though it should not be necessary in most cases.
	 */
	public void updateDetails(IRidgetContainer container) {
		for (IRidget ridget : container.getRidgets()) {
			ridget.updateFromModel();
		}
	}

}
