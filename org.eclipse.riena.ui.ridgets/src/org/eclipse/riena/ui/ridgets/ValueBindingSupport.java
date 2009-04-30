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

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.databinding.AggregateValidationStatus;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;

import org.eclipse.riena.core.marker.IMarkable;
import org.eclipse.riena.ui.core.marker.ErrorMarker;
import org.eclipse.riena.ui.core.marker.IMessageMarker;
import org.eclipse.riena.ui.core.marker.MessageMarker;
import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.ridgets.databinding.RidgetUpdateValueStrategy;
import org.eclipse.riena.ui.ridgets.marker.ValidationMessageMarker;
import org.eclipse.riena.ui.ridgets.validation.ValidationRuleStatus;
import org.eclipse.riena.ui.ridgets.validation.ValidatorCollection;

/**
 * Helper class for Ridgets to delegate their value binding issues to.
 */
public class ValueBindingSupport implements IValidationCallback {

	private DataBindingContext context;
	private IObservableValue targetOV;
	private IObservableValue modelOV;
	private Binding modelBinding;
	private ValidatorCollection afterGetValidators;
	private ValidatorCollection onEditValidators;
	private IConverter uiControlToModelConverter;
	private IConverter modelToUIControlConverter;

	private ErrorMarker errorMarker = new ErrorMarker();
	private IMarkable markable;
	private Collection<ValidationMessageMarker> validationMessageMarkers = new ArrayList<ValidationMessageMarker>(0);
	private IStatus lastValidationStatus;

	public ValueBindingSupport(IObservableValue target) {
		bindToTarget(target);
		afterGetValidators = new ValidatorCollection();
		onEditValidators = new ValidatorCollection();
	}

	public ValueBindingSupport(IObservableValue target, IObservableValue model) {
		this(target);
		bindToModel(model);
	}

	public IConverter getUIControlToModelConverter() {
		return uiControlToModelConverter;
	}

	public void setUIControlToModelConverter(IConverter uiControlToModelConverter) {
		this.uiControlToModelConverter = uiControlToModelConverter;
	}

	public IConverter getModelToUIControlConverter() {
		return modelToUIControlConverter;
	}

	public void setModelToUIControlConverter(IConverter modelToUIControlConverter) {
		this.modelToUIControlConverter = modelToUIControlConverter;
	}

	public Collection<IValidator> getValidationRules() {
		ArrayList<IValidator> allValidationRules = new ArrayList<IValidator>(onEditValidators.getValidators());
		allValidationRules.addAll(afterGetValidators.getValidators());
		return allValidationRules;
	}

	public ValidatorCollection getOnEditValidators() {
		return onEditValidators;
	}

	public ValidatorCollection getAfterGetValidators() {
		return afterGetValidators;
	}

	/**
	 * Adds a validation rule.
	 * 
	 * @param validationRule
	 *            The validation rule to add (non null)
	 * @param validationTime
	 *            a value specifying when to evalute the validator (non-null)
	 * @return true, if the onEditValidators were changed, false otherwise
	 * @see #getOnEditValidators()
	 * @throws RuntimeException
	 *             if validationRule is null, or an unsupported ValidationTime
	 *             is used
	 */
	public boolean addValidationRule(IValidator validationRule, ValidationTime validationTime) {
		Assert.isNotNull(validationRule);
		if (validationTime == ValidationTime.ON_UI_CONTROL_EDIT) {
			onEditValidators.add(validationRule);
			return true;
		} else if (validationTime == ValidationTime.ON_UPDATE_TO_MODEL) {
			afterGetValidators.add(validationRule);
			return false;
		} else {
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * Removes a validation rule.
	 * 
	 * @param validationRule
	 *            The validation rule to remove
	 * @return true, if the onEditValidators were changed, false otherwise
	 * @see #getOnEditValidators()
	 */
	public boolean removeValidationRule(IValidator validationRule) {
		if (validationRule == null) {
			return false;
		}
		// first remove in the list of afterGetValidators
		afterGetValidators.remove(validationRule);
		// if it is in the list of On_edit validators, also remove and return
		// true
		if (onEditValidators.contains(validationRule)) {
			onEditValidators.remove(validationRule);
			return true;
		} else {
			return false;
		}
	}

	public void bindToTarget(IObservableValue observableValue) {
		targetOV = observableValue;
		rebindToModel();
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IValueRidget#bindToModel(org.eclipse.core
	 *      .databinding.observable.value.IObservableValue)
	 */
	public void bindToModel(IObservableValue observableValue) {
		modelOV = observableValue;
		rebindToModel();
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IValueRidget#bindToModel(java.lang.Object,
	 *      java.lang.String)
	 */
	public void bindToModel(Object valueHolder, String valuePropertyName) {
		modelOV = PojoObservables.observeValue(valueHolder, valuePropertyName);
		rebindToModel();
	}

	/**
	 * Binds (first time or again) the model to the control.
	 */
	public void rebindToModel() {
		if (modelOV == null || targetOV == null) {
			return;
		}
		UpdateValueStrategy uiControlToModelStrategy = new RidgetUpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE);
		UpdateValueStrategy modelToUIControlStrategy = new RidgetUpdateValueStrategy(
				UpdateValueStrategy.POLICY_ON_REQUEST);
		uiControlToModelStrategy.setAfterGetValidator(afterGetValidators);
		if (uiControlToModelConverter != null) {
			if ((targetOV.getValueType() == uiControlToModelConverter.getFromType())
					&& (modelOV.getValueType() == uiControlToModelConverter.getToType())) {
				uiControlToModelStrategy.setConverter(uiControlToModelConverter);
			}
		}
		if (modelToUIControlConverter != null) {
			if ((targetOV.getValueType() == modelToUIControlConverter.getToType())
					&& (modelOV.getValueType() == modelToUIControlConverter.getFromType())) {
				modelToUIControlStrategy.setConverter(modelToUIControlConverter);
			}
		}

		if (modelBinding != null) {
			// MUST dispose previous binding, otherwise code like this:
			// ridget.bind(modelA);
			// ridget.bind(modelB);
			// causes the ridget to be bound to two models and ui changes are
			// synched with both! 
			modelBinding.dispose();
			getContext().removeBinding(modelBinding);
		}
		modelBinding = getContext().bindValue(targetOV, modelOV, uiControlToModelStrategy, modelToUIControlStrategy);
		AggregateValidationStatus validationStatus = new AggregateValidationStatus(getContext().getBindings(),
				AggregateValidationStatus.MAX_SEVERITY);
		validationStatus.addValueChangeListener(new IValueChangeListener() {
			public void handleValueChange(ValueChangeEvent event) {
				IStatus newStatus = (IStatus) ((ComputedValue) event.getSource()).getValue();
				if (newStatus.isOK()) {
					markable.removeMarker(errorMarker);
				} else {
					markable.addMarker(errorMarker);
				}
				updateValidationMessageMarkers(newStatus);
			}
		});
	}

	public IObservableValue getModelObservable() {
		return modelOV;
	}

	public Binding getModelBinding() {
		return modelBinding;
	}

	public void setMarkable(IMarkable markable) {
		this.markable = markable;
	}

	public void updateFromModel() {
		if (modelBinding != null) {
			modelBinding.updateModelToTarget();
		}
	}

	public void updateFromTarget() {
		if (modelBinding != null) {
			modelBinding.updateTargetToModel();
		}
	}

	public DataBindingContext getContext() {
		if (context == null) {
			context = new DataBindingContext();
		}
		return context;
	}

	public void validationRulesChecked(IStatus status) {
		updateValidationMessageMarkers(status);
	}

	public void addValidationMessage(String message) {
		addValidationMessage(new MessageMarker(message));
	}

	public void addValidationMessage(IMessageMarker messageMarker) {
		ValidationMessageMarker validationMessageMarker = new ValidationMessageMarker(messageMarker);
		doAddMessageMarker(validationMessageMarker);
	}

	public void addValidationMessage(String message, IValidator validationRule) {
		addValidationMessage(new MessageMarker(message), validationRule);
	}

	public void addValidationMessage(IMessageMarker messageMarker, IValidator validationRule) {
		ValidationMessageMarker validationMessageMarker = new ValidationMessageMarker(messageMarker, validationRule);
		doAddMessageMarker(validationMessageMarker);
	}

	public void removeValidationMessage(String message) {
		removeValidationMessage(new MessageMarker(message));
	}

	public void removeValidationMessage(IMessageMarker messageMarker) {
		ValidationMessageMarker validationMessageMarker = new ValidationMessageMarker(messageMarker);
		validationMessageMarkers.remove(validationMessageMarker);
		if (isErrorMarked()) {
			removeValidationMessageMarker(validationMessageMarker);
		}
	}

	public void removeValidationMessage(String message, IValidator validationRule) {
		removeValidationMessage(new MessageMarker(message), validationRule);
	}

	public void removeValidationMessage(IMessageMarker messageMarker, IValidator validationRule) {
		ValidationMessageMarker validationMessageMarker = new ValidationMessageMarker(messageMarker, validationRule);
		validationMessageMarkers.remove(validationMessageMarker);
		if (isErrorMarked()) {
			removeValidationMessageMarker(validationMessageMarker);
		}
	}

	// helping methods
	//////////////////

	private void addValidationMessageMarker(ValidationMessageMarker validationMessageMarker) {
		if (validationMessageMarker.getValidationRule() == null
				|| isSourceOf(validationMessageMarker.getValidationRule(), lastValidationStatus)) {
			markable.addMarker(validationMessageMarker);
		}
	}

	private void doAddMessageMarker(ValidationMessageMarker messageMarker) {
		validationMessageMarkers.add(messageMarker);
		if (isErrorMarked()) {
			addValidationMessageMarker(messageMarker);
		}
	}

	private boolean isErrorMarked() {
		return markable.getMarkers().contains(errorMarker);
	}

	private boolean isSourceOf(IValidator validationRule, IStatus status) {
		if (status instanceof ValidationRuleStatus) {
			return validationRule.equals(((ValidationRuleStatus) status).getSource());
		} else if (status instanceof MultiStatus) {
			for (IStatus childStatus : ((MultiStatus) status).getChildren()) {
				if (isSourceOf(validationRule, childStatus)) {
					return true;
				}
			}
		}
		return false;
	}

	private void removeValidationMessageMarker(ValidationMessageMarker validationMessageMarker) {
		markable.removeMarker(validationMessageMarker);
	}

	private void updateValidationMessageMarkers(IStatus status) {
		lastValidationStatus = status;
		for (ValidationMessageMarker validationMessageMarker : validationMessageMarkers) {
			removeValidationMessageMarker(validationMessageMarker);

			if (!status.isOK()) {
				addValidationMessageMarker(validationMessageMarker);
			}
		}
	}

}
