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
package org.eclipse.riena.ui.ridgets.validation.tests;

import junit.framework.TestCase;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.riena.tests.collect.NonUITestCase;
import org.eclipse.riena.ui.ridgets.validation.ValidationFailure;
import org.eclipse.riena.ui.ridgets.validation.ValidationRuleStatus;
import org.eclipse.riena.ui.ridgets.validation.ValidatorCollection;

/**
 * Tests for the MinLength rule.
 */
@NonUITestCase
public class ValidatorCollectionTest extends TestCase {

	private static final IValidator ALWAYS_SUCCEED_1 = new IValidator() {
		public IStatus validate(final Object value) {
			return ValidationRuleStatus.ok();
		}
	};

	private static final IValidator ALWAYS_SUCCEED_2 = new IValidator() {
		public IStatus validate(final Object value) {
			return ValidationRuleStatus.ok();
		}
	};

	private static final IValidator ALWAYS_FAIL_1 = new IValidator() {
		public IStatus validate(final Object value) {
			return ValidationRuleStatus.error(false, "always fails", this);
		}
	};

	private static final IValidator ALWAYS_FAIL_2 = new IValidator() {
		public IStatus validate(final Object value) {
			return ValidationRuleStatus.error(false, "always fails", this);
		}
	};

	private static final IValidator ALWAYS_THROW_EXCEPTION = new IValidator() {
		public IStatus validate(final Object value) {
			throw new ValidationFailure(getClass().getName() + "will always throw this excepion!");
		}
	};

	/**
	 * @throws Exception
	 *             Handled by JUnit.
	 */
	public void testJointStatus() throws Exception {
		ValidatorCollection rule = new ValidatorCollection();
		rule.add(ALWAYS_SUCCEED_1);
		rule.add(ALWAYS_SUCCEED_2);
		assertTrue(rule.validate(null).isOK());

		rule = new ValidatorCollection();
		rule.add(ALWAYS_FAIL_1);
		rule.add(ALWAYS_SUCCEED_1);
		assertFalse(rule.validate(null).isOK());

		rule = new ValidatorCollection();
		rule.add(ALWAYS_SUCCEED_1);
		rule.add(ALWAYS_FAIL_2);
		assertFalse(rule.validate(null).isOK());

		rule = new ValidatorCollection();
		rule.add(ALWAYS_FAIL_1);
		rule.add(ALWAYS_FAIL_2);
		assertFalse(rule.validate(null).isOK());
	}

	public void testConcurrentModification() throws Exception {
		// this test may not fail with a ConcurrentModificationException
		ValidatorCollection rule = new ValidatorCollection();
		rule.add(ALWAYS_SUCCEED_1);
		rule.add(ALWAYS_SUCCEED_1);
		rule.add(ALWAYS_SUCCEED_1);
		rule.add(ALWAYS_SUCCEED_2);
		rule.add(ALWAYS_SUCCEED_2);
		rule.add(ALWAYS_SUCCEED_2);
		rule.add(ALWAYS_FAIL_1);
		rule.add(ALWAYS_FAIL_2);
		rule.add(ALWAYS_THROW_EXCEPTION);
		for (final IValidator validator : rule) {
			if (validator == ALWAYS_SUCCEED_1) {
				rule.remove(validator);
			}
		}
		assertEquals(4, rule.getValidators().size());

		for (final IValidator validator : rule.getValidators()) {
			if (validator == ALWAYS_SUCCEED_2) {
				rule.remove(validator);
			}
		}
		assertEquals(3, rule.getValidators().size());

		try {
			rule.iterator().remove();
			fail("the returned iterator may not support the remove() method, modification through rule#remove(IValidator) only.");
		} catch (final RuntimeException e) {
			assert true : "test passed";
		}

		try {
			rule.getValidators().clear();
			fail("the returned collection may not support the remove() method, modification through rule#remove(IValidator) only.");
		} catch (final RuntimeException e) {
			assert true : "test passed";
		}

	}

	public void testExceptions() throws Exception {
		ValidatorCollection rule = new ValidatorCollection();
		rule.add(ALWAYS_THROW_EXCEPTION);
		try {
			rule.validate(new Object());
			fail("expected a thrown " + ValidationFailure.class.getName());
		} catch (final ValidationFailure f) {
			// test passed
		} catch (RuntimeException e) {
			fail("expected a thrown " + ValidationFailure.class.getName());
		}
		// two exception throwing rules
		rule.add(ALWAYS_THROW_EXCEPTION);
		try {
			rule.validate(new Object());
			fail("expected a thrown " + ValidationFailure.class.getName());
		} catch (final ValidationFailure f) {
			// test passed
		} catch (RuntimeException e) {
			fail("expected a thrown " + ValidationFailure.class.getName());
		}

		rule = new ValidatorCollection();
		rule.add(ALWAYS_SUCCEED_1);
		rule.add(ALWAYS_THROW_EXCEPTION);
		try {
			rule.validate(new Object());
			fail("expected a thrown " + ValidationFailure.class.getName());
		} catch (final ValidationFailure f) {
			// test passed
		} catch (RuntimeException e) {
			fail("expected a thrown " + ValidationFailure.class.getName());
		}

		rule = new ValidatorCollection();
		rule.add(ALWAYS_THROW_EXCEPTION);
		rule.add(ALWAYS_SUCCEED_1);
		try {
			rule.validate(new Object());
			fail("expected a thrown " + ValidationFailure.class.getName());
		} catch (final ValidationFailure f) {
			// test passed
		} catch (RuntimeException e) {
			fail("expected a thrown " + ValidationFailure.class.getName());
		}

		rule = new ValidatorCollection();
		rule.add(ALWAYS_FAIL_1);
		rule.add(ALWAYS_THROW_EXCEPTION);
		try {
			rule.validate(new Object());
			fail("expected a thrown " + ValidationFailure.class.getName());
		} catch (final ValidationFailure f) {
			// test passed
		} catch (RuntimeException e) {
			fail("expected a thrown " + ValidationFailure.class.getName());
		}

		rule = new ValidatorCollection();
		rule.add(ALWAYS_FAIL_1);
		rule.add(ALWAYS_THROW_EXCEPTION);
		rule.add(ALWAYS_FAIL_1);
		try {
			rule.validate(new Object());
			fail("expected a thrown " + ValidationFailure.class.getName());
		} catch (final ValidationFailure f) {
			// test passed
		} catch (RuntimeException e) {
			fail("expected a thrown " + ValidationFailure.class.getName());
		}

		rule = new ValidatorCollection();
		rule.add(ALWAYS_FAIL_1);
		rule.add(ALWAYS_THROW_EXCEPTION);
		rule.add(ALWAYS_SUCCEED_1);
		try {
			rule.validate(new Object());
			fail("expected a thrown " + ValidationFailure.class.getName());
		} catch (final ValidationFailure f) {
			// test passed
		} catch (RuntimeException e) {
			fail("expected a thrown " + ValidationFailure.class.getName());
		}

		rule = new ValidatorCollection();
		rule.add(ALWAYS_FAIL_1);
		rule.add(ALWAYS_SUCCEED_1);
		rule.add(ALWAYS_THROW_EXCEPTION);
		try {
			rule.validate(new Object());
			fail("expected a thrown " + ValidationFailure.class.getName());
		} catch (final ValidationFailure f) {
			// test passed
		} catch (RuntimeException e) {
			fail("expected a thrown " + ValidationFailure.class.getName());
		}

	}
}
