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
package org.eclipse.riena.ui.ridgets.validation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;

/**
 * A joined validator, which is a collection of rules to be invoked in the order
 * they are added to this rule.
 */
public class ValidatorCollection implements IValidator, Iterable<IValidator> {

	private final Collection<IValidator> validators = new ArrayList<IValidator>(2);

	/**
	 * Gets an unmodifiable copy of the validators used by this
	 * ValidatorCollection. Adding and removing single validators must be done
	 * through this class' methods {@link #add(IValidator)} and
	 * {@link #remove(IValidator)}.
	 * 
	 * @return a new unmodifiable collection, which contains all validators uses
	 *         in this collection.
	 */
	public Collection<IValidator> getValidators() {
		return Collections.unmodifiableCollection(new ArrayList<IValidator>(validators));
	}

	/**
	 * Returns an unmodifiable iterator which iterates over the used validators.
	 * Removing a rule must be done through this class'
	 * {@link #remove(IValidator)} method.
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<IValidator> iterator() {
		return getValidators().iterator();
	}

	/**
	 * Adds a validator to the collection. A validator added twice will be used
	 * twice.
	 * 
	 * @param validator
	 *            The validator to add.
	 */
	public void add(final IValidator validator) {
		validators.add(validator);
	}

	/**
	 * Removes a validator from the collection.
	 * 
	 * @param validator
	 *            The validator to remove.
	 */
	public void remove(final IValidator validator) {
		validators.remove(validator);
	}

	/**
	 * Validates the value using all validators and returns a joined status.
	 * 
	 * @see org.eclipse.core.databinding.validation.IValidator#validate(java.lang.Object)
	 */
	public IStatus validate(final Object value) {
		final IStatus[] statuses = new IStatus[validators.size()];
		int index = -1;
		for (final IValidator validator : validators) {
			statuses[++index] = validator.validate(value);
		}
		return ValidationRuleStatus.join(statuses);
	}

}
