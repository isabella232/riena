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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import junit.framework.TestCase;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.riena.core.marker.IMarkable;
import org.eclipse.riena.core.marker.Markable;
import org.eclipse.riena.tests.collect.NonUITestCase;
import org.eclipse.riena.ui.core.marker.ErrorMarker;
import org.eclipse.riena.ui.core.marker.ErrorMessageMarker;
import org.eclipse.riena.ui.core.marker.IMessageMarker;
import org.eclipse.riena.ui.core.marker.MessageMarker;
import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.ridgets.swt.DefaultRealm;
import org.eclipse.riena.ui.ridgets.validation.ValidationFailure;
import org.eclipse.riena.ui.ridgets.validation.ValidationRuleStatus;

/**
 * Tests for the ValueBindingSupport.
 */
@NonUITestCase
public class ValueBindingSupportTest extends TestCase {

	private DefaultRealm realm;
	private ValueBindingSupport valueBindingSupport;
	private TestBean bean;
	private IObservableValue model;
	private IObservableValue target;
	private IMarkable markable;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		realm = new DefaultRealm();

		bean = new TestBean();
		model = BeansObservables.observeValue(bean, TestBean.PROPERTY);
		target = new WritableValue();

		valueBindingSupport = new ValueBindingSupport(target, model);

		markable = new Markable();
		valueBindingSupport.setMarkable(markable);
	}

	@Override
	protected void tearDown() throws Exception {
		realm.dispose();
		realm = null;
		super.tearDown();
	}

	public void testUpdateFromModelOnRequest() throws Exception {

		assertNull(target.getValue());

		bean.setProperty("TestValue");

		assertNull(target.getValue());

		valueBindingSupport.updateFromModel();

		assertEquals("TestValue", target.getValue());
	}

	public void testUpdateFromTargetImmediately() throws Exception {

		assertNull(bean.getProperty());

		target.setValue("TestValue");

		assertEquals("TestValue", bean.getProperty());
	}

	public void testValidationMessagesAddAndRemove() throws Exception {

		valueBindingSupport.addValidationRule(new EvenNumberOfCharacters(), ValidationTime.ON_UPDATE_TO_MODEL);
		valueBindingSupport.addValidationMessage("TestMessage1");
		valueBindingSupport.addValidationMessage("TestMessage2");
		ErrorMessageMarker messageMarker1 = new ErrorMessageMarker("TestMessage3");
		valueBindingSupport.addValidationMessage(messageMarker1);
		MessageMarker messageMarker2 = new MessageMarker("TestMessage4");
		valueBindingSupport.addValidationMessage(messageMarker2);

		assertEquals(0, markable.getMarkers().size());

		target.setValue("odd");

		assertEquals(5, markable.getMarkers().size());
		assertMessageMarkers("TestMessage1", "TestMessage2", "TestMessage3", "TestMessage4");

		target.setValue("even");

		assertEquals(0, markable.getMarkers().size());

		valueBindingSupport.removeValidationMessage("TestMessage1");
		valueBindingSupport.removeValidationMessage(messageMarker1);

		target.setValue("odd");

		assertEquals(3, markable.getMarkers().size());
		assertMessageMarkers("TestMessage2", "TestMessage4");

		target.setValue("even");

		assertEquals(0, markable.getMarkers().size());

		valueBindingSupport.removeValidationMessage("TestMessage2");
		valueBindingSupport.removeValidationMessage(messageMarker2);

		target.setValue("odd");

		assertEquals(1, markable.getMarkers().size());
		assertTrue(markable.getMarkers().iterator().next() instanceof ErrorMarker);

		target.setValue("even");

		assertEquals(0, markable.getMarkers().size());
	}

	/**
	 * Tests that adding the same validation several times has no effect
	 */
	public void testAddSameValidationMessage() {
		EvenNumberOfCharacters rule = new EvenNumberOfCharacters();
		valueBindingSupport.addValidationRule(rule, ValidationTime.ON_UPDATE_TO_MODEL);
		valueBindingSupport.addValidationMessage("TestMessage1");
		valueBindingSupport.addValidationMessage("TestMessage1");
		valueBindingSupport.addValidationMessage("TestMessage2", rule);
		valueBindingSupport.addValidationMessage("TestMessage2", rule);
		MessageMarker messageMarker = new MessageMarker("TestMessage3");
		valueBindingSupport.addValidationMessage(messageMarker);
		valueBindingSupport.addValidationMessage(messageMarker);
		MessageMarker messageMarker2 = new MessageMarker("TestMessage4");
		valueBindingSupport.addValidationMessage(messageMarker2, rule);
		valueBindingSupport.addValidationMessage(messageMarker2, rule);

		assertEquals(0, markable.getMarkers().size());

		target.setValue("odd");

		assertEquals(4, markable.getMarkersOfType(IMessageMarker.class).size());

		target.setValue("even");

		assertEquals(0, markable.getMarkers().size());

	}

	public void testValidationMessagesAddAndRemoveWhileActive() throws Exception {

		valueBindingSupport.addValidationRule(new EvenNumberOfCharacters(), ValidationTime.ON_UPDATE_TO_MODEL);
		target.setValue("odd");

		assertEquals(1, markable.getMarkers().size());
		assertTrue(markable.getMarkers().iterator().next() instanceof ErrorMarker);

		valueBindingSupport.addValidationMessage("TestMessage1");

		assertEquals(2, markable.getMarkers().size());
		assertMessageMarkers("TestMessage1");

		MessageMarker messageMarker = new MessageMarker("TestMessage2");
		valueBindingSupport.addValidationMessage(messageMarker);

		assertEquals(3, markable.getMarkers().size());
		assertMessageMarkers("TestMessage1", "TestMessage2");

		valueBindingSupport.removeValidationMessage("TestMessage1");

		assertEquals(2, markable.getMarkers().size());
		assertMessageMarkers("TestMessage2");

		valueBindingSupport.removeValidationMessage(messageMarker);

		assertEquals(1, markable.getMarkers().size());
		assertTrue(markable.getMarkers().iterator().next() instanceof ErrorMarker);
	}

	public void testSpecialValidationMessages() throws Exception {

		EvenNumberOfCharacters evenNumberOfCharacters = new EvenNumberOfCharacters();
		NotEndingWithDisaster notEndingWithDisaster = new NotEndingWithDisaster();
		valueBindingSupport.addValidationRule(evenNumberOfCharacters, ValidationTime.ON_UPDATE_TO_MODEL);
		valueBindingSupport.addValidationRule(notEndingWithDisaster, ValidationTime.ON_UPDATE_TO_MODEL);
		valueBindingSupport.addValidationMessage("TestNotEvenMessage1", evenNumberOfCharacters);
		valueBindingSupport.addValidationMessage(new MessageMarker("TestNotEvenMessage2"), evenNumberOfCharacters);
		valueBindingSupport.addValidationMessage("TestDisasterMessage", notEndingWithDisaster);

		assertEquals(0, markable.getMarkers().size());

		target.setValue("Disaster");

		assertEquals(2, markable.getMarkers().size());
		assertMessageMarkers("TestDisasterMessage");

		target.setValue("Disaster Area");

		assertEquals(3, markable.getMarkers().size());
		assertMessageMarkers("TestNotEvenMessage1", "TestNotEvenMessage2");

		target.setValue("We are teetering on the brink of disaster");

		assertEquals(4, markable.getMarkers().size());
		assertMessageMarkers("TestNotEvenMessage1", "TestNotEvenMessage2", "TestDisasterMessage");

		target.setValue("Save again");

		assertEquals(0, markable.getMarkers().size());
	}

	public void testValidationRuleAddAndRemove() {
		final IValidator rule = new EvenNumberOfCharacters();

		boolean isOnEdit1 = valueBindingSupport.addValidationRule(rule, ValidationTime.ON_UI_CONTROL_EDIT);

		assertTrue(isOnEdit1);
		assertTrue(valueBindingSupport.getOnEditValidators().contains(rule));
		assertFalse(valueBindingSupport.getAfterGetValidators().contains(rule));

		boolean isOnEdit2 = valueBindingSupport.addValidationRule(rule, ValidationTime.ON_UPDATE_TO_MODEL);

		assertFalse(isOnEdit2);
		assertTrue(valueBindingSupport.getOnEditValidators().contains(rule));
		assertTrue(valueBindingSupport.getAfterGetValidators().contains(rule));

		valueBindingSupport.removeValidationRule(rule);

		assertFalse(valueBindingSupport.getOnEditValidators().contains(rule));
		assertFalse(valueBindingSupport.getAfterGetValidators().contains(rule));
	}

	public void testValidationRuleAddAndRemoveNull() {
		try {
			valueBindingSupport.addValidationRule(null, ValidationTime.ON_UPDATE_TO_MODEL);
			fail();
		} catch (RuntimeException rex) {
			// expected
		}

		boolean result = valueBindingSupport.removeValidationRule(null);
		assertFalse(result);
	}

	// helping methods
	// ////////////////

	private void assertMessageMarkers(String... messages) {
		Collection<String> missingMessages = new ArrayList<String>(Arrays.asList(messages));

		for (IMessageMarker messageMarker : markable.getMarkersOfType(IMessageMarker.class)) {
			missingMessages.remove(messageMarker.getMessage());
		}

		assertTrue("missing MessageMarker for " + missingMessages, missingMessages.isEmpty());
	}

	// helping clases
	// ///////////////

	private static class EvenNumberOfCharacters implements IValidator {

		public IStatus validate(final Object value) {
			if (value == null) {
				return ValidationRuleStatus.ok();
			}
			if (value instanceof String) {
				final String string = (String) value;
				if (string.length() % 2 == 0) {
					return ValidationRuleStatus.ok();
				}
				return ValidationRuleStatus.error(false, "Odd number of characters.", this);
			}
			throw new ValidationFailure(getClass().getName() + " can only validate objects of type "
					+ String.class.getName());
		}

	}

	private static class NotEndingWithDisaster implements IValidator {

		public IStatus validate(final Object value) {
			if (value == null) {
				return ValidationRuleStatus.ok();
			}
			if (value instanceof String) {
				final String string = (String) value;
				if (!string.toLowerCase().endsWith("disaster")) {
					return ValidationRuleStatus.ok();
				}
				return ValidationRuleStatus.error(false, "It ends with disaster.", this);
			}
			throw new ValidationFailure(getClass().getName() + " can only validate objects of type "
					+ String.class.getName());
		}

	}

}
