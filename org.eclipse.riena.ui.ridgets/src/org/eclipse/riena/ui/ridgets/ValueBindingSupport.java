package org.eclipse.riena.ui.ridgets;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.databinding.AggregateValidationStatus;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.riena.core.marker.IMarkable;
import org.eclipse.riena.ui.core.marker.ErrorMarker;
import org.eclipse.riena.ui.ridgets.databinding.RidgetUpdateValueStrategy;
import org.eclipse.riena.ui.ridgets.validation.IValidationRule;
import org.eclipse.riena.ui.ridgets.validation.ValidatorCollection;

/**
 * Helper class for Ridgets to delegate their value binding issues to.
 */
public class ValueBindingSupport {

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

	/**
	 * Adds a validation rule.
	 * 
	 * @param validationRule
	 *            The validation rule to add
	 * @return true, if the onEditValidators were changed, false otherwise
	 * @see #getOnEditValidators()
	 */
	public boolean addValidationRule(IValidator validationRule) {
		if (isValidateOnEdit(validationRule)) {
			onEditValidators.add(validationRule);
			return true;
		} else {
			afterGetValidators.add(validationRule);
			return false;
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
		if (isValidateOnEdit(validationRule)) {
			onEditValidators.remove(validationRule);
			return true;
		} else {
			afterGetValidators.remove(validationRule);
			return false;
		}
	}

	public void bindToTarget(IObservableValue observableValue) {
		targetOV = observableValue;
		rebindToModel();
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IValueRidget#bindToModel(org.eclipse.core.databinding.observable.value.IObservableValue)
	 */
	public void bindToModel(IObservableValue observableValue) {
		modelOV = observableValue;
		rebindToModel();
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.IValueRidget#bindToModel(java.lang.Object,
	 *      java.lang.String)
	 */
	public void bindToModel(Object bean, String propertyName) {
		modelOV = BeansObservables.observeValue(bean, propertyName);
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
			modelBinding.dispose();
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

	private boolean isValidateOnEdit(IValidator validationRule) {
		return validationRule instanceof IValidationRule
				&& ((IValidationRule) validationRule).getValidationTime() == IValidationRule.ValidationTime.ON_UI_CONTROL_EDITED;
	}

}
