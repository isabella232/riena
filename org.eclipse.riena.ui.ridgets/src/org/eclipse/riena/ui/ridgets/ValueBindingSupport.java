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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
import org.eclipse.core.runtime.Status;

import org.eclipse.riena.core.marker.IMarkable;
import org.eclipse.riena.ui.core.marker.ErrorMarker;
import org.eclipse.riena.ui.core.marker.IMessageMarker;
import org.eclipse.riena.ui.core.marker.MessageMarker;
import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.ridgets.databinding.RidgetUpdateValueStrategy;
import org.eclipse.riena.ui.ridgets.marker.ValidationMessageMarker;
import org.eclipse.riena.ui.ridgets.validation.ValidatorCollection;

/**
 * Helper class for Ridgets to delegate their value binding issues to.
 */
public class ValueBindingSupport {

	/**
	 * This rule fails if the ridget is marked with the error marker
	 */
	private final IValidator DEFAULT_RULE = new IValidator() {
		public IStatus validate(Object value) {
			boolean isOk = markable.getMarkersOfType(ErrorMarker.class).isEmpty();
			return isOk ? Status.OK_STATUS : Status.CANCEL_STATUS;
		}
	};

	private final ErrorMarker errorMarker;
	private final ValidatorCollection afterGetValidators;
	private final ValidatorCollection onEditValidators;

	private Map<IValidator, Set<ValidationMessageMarker>> rule2messages;
	private Map<IValidator, IStatus> rule2status;

	private DataBindingContext context;
	private IObservableValue targetOV;
	private IObservableValue modelOV;
	private Binding modelBinding;
	private IConverter uiControlToModelConverter;
	private IConverter modelToUIControlConverter;

	private IMarkable markable;

	public ValueBindingSupport(IObservableValue target) {
		bindToTarget(target);
		errorMarker = new ErrorMarker();
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

	/**
	 * TODO [ev] javadoc
	 * 
	 * @since 1.2
	 */
	public ValidatorCollection getAllValidators() {
		ValidatorCollection result = new ValidatorCollection();
		for (IValidator validationRule : onEditValidators) {
			result.add(validationRule);
		}
		for (IValidator validationRule : afterGetValidators) {
			result.add(validationRule);
		}
		return result;
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
	 *            a value specifying when to evalute the validationRule
	 *            (non-null)
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
		removeMessages(validationRule);
		clearStatus(validationRule);
		// first remove in the list of afterGetValidators
		afterGetValidators.remove(validationRule);
		// if it is in the list of On_edit validators, also remove and return true
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
				updateErrorMarker(newStatus);
				updateValidationMessages(newStatus);
			}

			private void updateErrorMarker(IStatus newStatus) {
				if (newStatus.isOK()) {
					trace("- errorMarker"); //$NON-NLS-1$
					markable.removeMarker(errorMarker);
				} else {
					trace("+ errorMarker"); //$NON-NLS-1$
					markable.addMarker(errorMarker);
				}
			}

			private void updateValidationMessages(IStatus newStatus) {
				if (targetOV != null) {
					Object value = targetOV.getValue();
					updateValidationMessageMarker(DEFAULT_RULE, newStatus);
					for (IValidator rule : getAfterGetValidators()) {
						if (rule2messages != null && rule2messages.containsKey(rule)) {
							updateValidationMessageMarker(rule, rule.validate(value));
						}
					}
				}
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

	public void addValidationMessage(String message) {
		addValidationMessage(message, DEFAULT_RULE);
	}

	public void addValidationMessage(IMessageMarker messageMarker) {
		addValidationMessage(messageMarker, DEFAULT_RULE);
	}

	public void addValidationMessage(String message, IValidator validationRule) {
		addValidationMessage(new MessageMarker(message), validationRule);
	}

	public void addValidationMessage(IMessageMarker messageMarker, IValidator validationRule) {
		Assert.isNotNull(messageMarker, "messageMarker cannot be null"); //$NON-NLS-1$
		Assert.isNotNull(validationRule, "validationRule cannot be null"); //$NON-NLS-1$
		ValidationMessageMarker validationMessageMarker = new ValidationMessageMarker(messageMarker, validationRule);
		if (rule2messages == null) {
			rule2messages = new HashMap<IValidator, Set<ValidationMessageMarker>>();
		}
		Set<ValidationMessageMarker> messages = rule2messages.get(validationRule);
		if (messages == null) {
			messages = new HashSet<ValidationMessageMarker>();
			rule2messages.put(validationRule, messages);
		}
		messages.add(validationMessageMarker);
		updateValidationMessageMarker(validationRule);
	}

	public void removeValidationMessage(String message) {
		removeValidationMessage(message, DEFAULT_RULE);
	}

	public void removeValidationMessage(IMessageMarker messageMarker) {
		removeValidationMessage(messageMarker, DEFAULT_RULE);
	}

	public void removeValidationMessage(String message, IValidator validationRule) {
		removeValidationMessage(new MessageMarker(message), validationRule);
	}

	public void removeValidationMessage(IMessageMarker messageMarker, IValidator validationRule) {
		ValidationMessageMarker validationMessageMarker = new ValidationMessageMarker(messageMarker, validationRule);
		if (rule2messages != null) {
			Set<ValidationMessageMarker> messages = rule2messages.get(validationRule);
			messages.remove(validationMessageMarker);
			markable.removeMarker(validationMessageMarker);
			if (messages.isEmpty()) {
				rule2messages.remove(validationRule);
			}
		}
	}

	public void updateValidationMessageMarker(IStatus status) {
		updateValidationMessageMarker(DEFAULT_RULE, status);
	}

	/**
	 * TODO [ev] javadoc
	 */
	public void updateValidationMessageMarker(IValidator validationRule, IStatus status) {
		trace("updating rule " + validationRule + " with " + status); // TODO [ev] ex //$NON-NLS-1$ //$NON-NLS-2$
		storeStatus(validationRule, status);
		if (!status.isOK()) {
			addMessages(validationRule);
		} else {
			removeMessages(validationRule);
		}
	}

	// helping methods
	//////////////////

	private void addMessages(IValidator validationRule) {
		if (rule2messages != null && rule2messages.containsKey(validationRule)) {
			Set<ValidationMessageMarker> messages = rule2messages.get(validationRule);
			for (ValidationMessageMarker message : messages) {
				trace("+ " + message.getMessage()); //$NON-NLS-1$ // TODO [ev] ex
				markable.addMarker(message);
			}
		}
	}

	private void clearStatus(IValidator validationRule) {
		if (rule2status != null) {
			rule2status.remove(validationRule);
		}
	}

	private IStatus getStatus(IValidator validationRule) {
		return rule2status != null ? rule2status.get(validationRule) : null;
	}

	private void storeStatus(IValidator validationRule, IStatus status) {
		if (rule2status == null) {
			rule2status = new HashMap<IValidator, IStatus>();
		}
		rule2status.put(validationRule, status);
	}

	private void removeMessages(IValidator validationRule) {
		if (rule2messages != null && rule2messages.containsKey(validationRule)) {
			Set<ValidationMessageMarker> messages = rule2messages.get(validationRule);
			for (ValidationMessageMarker message : messages) {
				trace("- " + message.getMessage()); //$NON-NLS-1$ // TODO [ev] ex
				markable.removeMarker(message);
			}
		}
	}

	private void updateValidationMessageMarker(IValidator validationRule) {
		IStatus status = getStatus(validationRule);
		if (status != null) {
			updateValidationMessageMarker(validationRule, status);
		}
	}

	private void trace(String message) {
		//		// System.out.println(message); // TODO [ev] ex
	}

}
