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

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.riena.ui.ridgets.IValueRidget;
import org.eclipse.riena.ui.ridgets.ValueBindingSupport;

/**
 * Abstract implementation of an {@link IValueRidget} for SWT.
 */
public abstract class AbstractValueRidget extends AbstractSWTRidget implements IValueRidget {

	private ValueBindingSupport valueBindingSupport;

	public AbstractValueRidget() {
		valueBindingSupport = new ValueBindingSupport(this.getRidgetObservable());
		valueBindingSupport.setMarkable(this);
	}

	public void bindToModel(IObservableValue observableValue) {
		valueBindingSupport.bindToModel(observableValue);
	}

	public void bindToModel(Object bean, String propertyName) {
		valueBindingSupport.bindToModel(bean, propertyName);
	}

	public IConverter getModelToUIControlConverter() {
		return valueBindingSupport.getModelToUIControlConverter();
	}

	public void setModelToUIControlConverter(IConverter converter) {
		valueBindingSupport.setModelToUIControlConverter(converter);
	}

	@Override
	public void updateFromModel() {
		super.updateFromModel();
		valueBindingSupport.updateFromModel();
	}

	protected final ValueBindingSupport getValueBindingSupport() {
		return valueBindingSupport;
	}

}
