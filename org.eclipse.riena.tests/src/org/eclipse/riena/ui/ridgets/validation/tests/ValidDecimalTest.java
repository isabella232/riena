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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import junit.framework.TestCase;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.tests.collect.NonUITestCase;
import org.eclipse.riena.ui.ridgets.validation.ValidDecimal;
import org.eclipse.riena.ui.ridgets.validation.ValidationFailure;

/**
 * Tests for the ValidCharacters rule.
 */
@NonUITestCase
public class ValidDecimalTest extends TestCase {

	/**
	 * @throws Exception
	 *             Handled by JUnit.
	 */
	public final void testUSlocale() throws Exception {
		final ValidDecimal rule = new ValidDecimal(Locale.US);
		assertTrue(rule.validate(null).isOK());

		assertTrue(rule.validate("0.00").isOK());
		assertTrue(rule.validate("1.01").isOK());
		assertTrue(rule.validate("1.000").isOK());
		assertTrue(rule.validate("123.31").isOK());
		assertTrue(rule.validate("1,234.31").isOK());
		assertTrue(rule.validate("1,234.31").isOK());

		assertTrue(rule.validate("-0.00").isOK());
		assertTrue(rule.validate("-1.01").isOK());
		assertTrue(rule.validate("-1.000").isOK());
		assertTrue(rule.validate("-123.31").isOK());
		assertTrue(rule.validate("-1,234.31").isOK());

		assertTrue(rule.validate("- 0.00").isOK());
		assertTrue(rule.validate("- 1.01").isOK());
		assertTrue(rule.validate("- 1.000").isOK());
		assertTrue(rule.validate("- 123.31").isOK());
		assertTrue(rule.validate("- 1,234.31").isOK());

		// only fraction
		assertTrue(rule.validate(".123").isOK());
		// reason why this is accepted:
		assertEquals(0.123d, DecimalFormat.getInstance(Locale.US).parse(".123").doubleValue());

		// only decimal-separator char:
		assertFalse(rule.validate(".").isOK());
		try { // reason why this must fail:
			assertEquals(0.0d, DecimalFormat.getInstance(Locale.US).parse(".").doubleValue());
			fail("expected a ParseException");
		} catch (final ParseException e) {
			assert true : "test passed";
		}

		// last char is fraction digit
		assertTrue(rule.validate("0.").isOK());
		assertEquals(0d, DecimalFormat.getInstance(Locale.US).parse("0.").doubleValue());

		// missing fraction, no partial checks
		assertFalse(rule.validate("0").isOK());
		assertFalse(rule.validate("1").isOK());
		assertFalse(rule.validate("123").isOK());
		assertFalse(rule.validate("145").isOK());
		assertFalse(rule.validate("156").isOK());

		// Validate German Locale numbers with US locale rule:
		// these will not pass, as they contain no decimal separator from the
		// view of an US locale, but only grouping characters
		assertFalse(rule.validate("0,00").isOK());
		assertFalse(rule.validate("1,01").isOK());
		assertFalse(rule.validate("1,000").isOK());
		assertFalse(rule.validate("123,31").isOK());
		// for the US locale it appears that the German grouping-character is
		// behind the decimal-separator:
		assertFalse(rule.validate("1.234,31").isOK());

		// wrong symbols
		assertFalse(rule.validate("A").isOK());
		assertFalse(rule.validate("A123").isOK());
		assertFalse(rule.validate("123A").isOK());
		assertFalse(rule.validate("1.23A").isOK());
		assertFalse(rule.validate("123A321").isOK());
		assertFalse(rule.validate("abcdefghijklmnopqrstuvwxyz").isOK());
		assertFalse(rule.validate("ABCDEFGHIJKLMBNOPQRSTUVWXYZ").isOK());
		try {
			rule.validate(new Object());
			fail("expected a thrown ValidationFailure");
		} catch (final ValidationFailure f) {
			// test passed
		}
	}

	/**
	 * @throws Exception
	 *             Handled by JUnit.
	 */
	public final void testGermanLocale() throws Exception {
		final ValidDecimal rule = new ValidDecimal(Locale.GERMANY);
		assertTrue(rule.validate(null).isOK());
		assertTrue(rule.validate("0,00").isOK());
		assertTrue(rule.validate("1,01").isOK());
		assertTrue(rule.validate("1,000").isOK());
		assertTrue(rule.validate("123,31").isOK());
		assertTrue(rule.validate("1.234,31").isOK());

		assertTrue(rule.validate("-0,00").isOK());
		assertTrue(rule.validate("-1,01").isOK());
		assertTrue(rule.validate("-1,000").isOK());
		assertTrue(rule.validate("-123,31").isOK());
		assertTrue(rule.validate("-1.234,31").isOK());

		assertTrue(rule.validate("- 0,00").isOK());
		assertTrue(rule.validate("- 1,01").isOK());
		assertTrue(rule.validate("- 1,000").isOK());
		assertTrue(rule.validate("- 123,31").isOK());
		assertTrue(rule.validate("- 1.234,31").isOK());

		// only fraction
		assertTrue(rule.validate(",123").isOK());

		// only decimal-separator char:
		assertFalse(rule.validate(",").isOK());

		// missing fraction, no partial checks
		assertFalse(rule.validate("0").isOK());
		assertFalse(rule.validate("1").isOK());
		assertFalse(rule.validate("123").isOK());
		assertFalse(rule.validate("145").isOK());
		assertFalse(rule.validate("156").isOK());

		// Validate US Locale numbers with German locale rule:
		// these will not pass, as they contain no decimal separator from the
		// view of a German locale, but only grouping characters
		assertFalse(rule.validate("0.00").isOK());
		assertFalse(rule.validate("1.01").isOK());
		assertFalse(rule.validate("1.000").isOK());
		assertFalse(rule.validate("123.31").isOK());
		// for the German locale it appears that the US grouping-character is
		// behind the decimal-separator:
		assertFalse(rule.validate("1,234.31").isOK());

		// wrong symbols
		assertFalse(rule.validate("A").isOK());
		assertFalse(rule.validate("A123").isOK());
		assertFalse(rule.validate("123A").isOK());
		assertFalse(rule.validate("1,23A").isOK());
		assertFalse(rule.validate("123A321").isOK());
		assertFalse(rule.validate("abcdefghijklmnopqrstuvwxyz").isOK());
		assertFalse(rule.validate("ABCDEFGHIJKLMBNOPQRSTUVWXYZ").isOK());
		try {
			rule.validate(new Object());
			fail("expected a thrown ValidationFailure");
		} catch (final ValidationFailure f) {
			// test passed
		}
	}

	/**
	 * @throws Exception
	 *             Handled by JUnit.
	 */
	public final void testSwissLocale() throws Exception {
		final ValidDecimal rule = new ValidDecimal(new Locale("de", "CH"));
		assertTrue(rule.validate(null).isOK());
		assertTrue(rule.validate("0.00").isOK());
		assertTrue(rule.validate("1.01").isOK());
		assertTrue(rule.validate("1.000").isOK());
		assertTrue(rule.validate("123.31").isOK());
		assertTrue(rule.validate("1'234.31").isOK());

		assertTrue(rule.validate("-0.00").isOK());
		assertTrue(rule.validate("-1.01").isOK());
		assertTrue(rule.validate("-1.000").isOK());
		assertTrue(rule.validate("-123.31").isOK());
		assertTrue(rule.validate("-1'234.31").isOK());

		assertTrue(rule.validate("- 0.00").isOK());
		assertTrue(rule.validate("- 1.01").isOK());
		assertTrue(rule.validate("- 1.000").isOK());
		assertTrue(rule.validate("- 123.31").isOK());
		assertTrue(rule.validate("- 1'234.31").isOK());

		// only fraction
		assertTrue(rule.validate(".123").isOK());
		// only decimal-separator char:
		assertFalse(rule.validate(".").isOK());
		// only decimal-separator char:
		assertFalse(rule.validate(",").isOK());

		// missing fraction, no partial checks
		assertFalse(rule.validate("0").isOK());
		assertFalse(rule.validate("1").isOK());
		assertFalse(rule.validate("123").isOK());
		assertFalse(rule.validate("145").isOK());
		assertFalse(rule.validate("156").isOK());

		// Validate German Locale numbers with Swiss locale rule:
		// these will not pass, as they contain no decimal separator from the
		// view of a Swiss locale, but an alien character
		assertFalse(rule.validate("0,00").isOK());
		assertFalse(rule.validate("1,01").isOK());
		assertFalse(rule.validate("1,000").isOK());
		assertFalse(rule.validate("123,31").isOK());

		// for the Swiss locale it appears that the German grouping-character is
		// a decimal-separator, the alien character ',' makes the validation
		// fail.
		assertFalse(rule.validate("1.234,31").isOK());

		// wrong symbols
		assertFalse(rule.validate("A").isOK());
		assertFalse(rule.validate("A123").isOK());
		assertFalse(rule.validate("123A").isOK());
		assertFalse(rule.validate("1,23A").isOK());
		assertFalse(rule.validate("123A321").isOK());
		assertFalse(rule.validate("abcdefghijklmnopqrstuvwxyz").isOK());
		assertFalse(rule.validate("ABCDEFGHIJKLMBNOPQRSTUVWXYZ").isOK());
		try {
			rule.validate(new Object());
			fail("expected a thrown ValidationFailure");
		} catch (final ValidationFailure f) {
			// test passed
		}

	}

	/**
	 * @throws Exception
	 *             Handled by JUnit.
	 */
	public final void testFrenchLocale() throws Exception {
		final ValidDecimal rule = new ValidDecimal(Locale.FRANCE);
		assertTrue(rule.validate(null).isOK());
		assertTrue(rule.validate("0,00").isOK());
		assertTrue(rule.validate("1,01").isOK());
		assertTrue(rule.validate("1,000").isOK());
		assertTrue(rule.validate("123,31").isOK());
		assertTrue(rule.validate("1 234,31").isOK());

		assertTrue(rule.validate("-0,00").isOK());
		assertTrue(rule.validate("-1,01").isOK());
		assertTrue(rule.validate("-1,000").isOK());
		assertTrue(rule.validate("-123,31").isOK());
		assertTrue(rule.validate("-1 234,31").isOK());

		assertTrue(rule.validate("- 0,00").isOK());
		assertTrue(rule.validate("- 1,01").isOK());
		assertTrue(rule.validate("- 1,000").isOK());
		assertTrue(rule.validate("- 123,31").isOK());
		assertTrue(rule.validate("- 1 234,31").isOK());

		// only fraction
		assertTrue(rule.validate(",123").isOK());

		// only decimal-separator char:
		assertFalse(rule.validate(",").isOK());

		// missing fraction, no partial checks
		assertFalse(rule.validate("0").isOK());
		assertFalse(rule.validate("1").isOK());
		assertFalse(rule.validate("123").isOK());
		assertFalse(rule.validate("145").isOK());
		assertFalse(rule.validate("156").isOK());

		// Validate US Locale numbers with French locale rule:
		// these will not pass, as they contain no decimal separator from the
		// view of a French locale, but the US grouping-character is
		// alien, so it won't parse the number:
		assertFalse(rule.validate("0.00").isOK());
		assertFalse(rule.validate("1.01").isOK());
		assertFalse(rule.validate("1.000").isOK());
		assertFalse(rule.validate("123.31").isOK());

		// for the French locale it appears that the US grouping-character ','
		// is a decimal-separator, the alien character '.' behind makes the
		// validation fail.
		assertFalse(rule.validate("1,234.31").isOK());

		// wrong symbols
		assertFalse(rule.validate("A").isOK());
		assertFalse(rule.validate("A123").isOK());
		assertFalse(rule.validate("123A").isOK());
		assertFalse(rule.validate("1.23A").isOK());
		assertFalse(rule.validate("123A321").isOK());
		assertFalse(rule.validate("abcdefghijklmnopqrstuvwxyz").isOK());
		assertFalse(rule.validate("ABCDEFGHIJKLMBNOPQRSTUVWXYZ").isOK());
		try {
			rule.validate(new Object());
			fail("expected a thrown ValidationFailure");
		} catch (final ValidationFailure f) {
			// test passed
		}
	}

	public void testPartialCheckUSlocale() throws Exception {
		final ValidDecimal rule = new ValidDecimal(true, Locale.US);
		assertTrue(rule.validate(null).isOK());
		assertTrue(rule.validate("0.00").isOK());
		assertTrue(rule.validate("1.23").isOK());
		assertTrue(rule.validate("1.").isOK());
		assertTrue(rule.validate(".1").isOK());
		assertTrue(rule.validate("1").isOK());
		assertTrue(rule.validate("10").isOK());
		assertTrue(rule.validate("1,000").isOK());

		assertTrue(rule.validate("-0.00").isOK());
		assertTrue(rule.validate("-1.23").isOK());
		assertTrue(rule.validate("-1.").isOK());
		assertTrue(rule.validate("-.1").isOK());
		assertTrue(rule.validate("-1").isOK());
		assertTrue(rule.validate("-10").isOK());
		assertTrue(rule.validate("-1,000").isOK());

		assertTrue(rule.validate("- 0.00").isOK());
		assertTrue(rule.validate("- 1.23").isOK());
		assertTrue(rule.validate("- 1.").isOK());
		assertTrue(rule.validate("- .1").isOK());
		assertTrue(rule.validate("- 1").isOK());
		assertTrue(rule.validate("- 10").isOK());
		assertTrue(rule.validate("- 1,000").isOK());

		assertFalse(rule.validate(".").isOK());

		// wrong symbols
		assertFalse(rule.validate("A").isOK());
		assertFalse(rule.validate("A123").isOK());
		assertFalse(rule.validate("123A").isOK());
		assertFalse(rule.validate("1.23A").isOK());
		assertFalse(rule.validate("123A321").isOK());
		assertFalse(rule.validate("abcdefghijklmnopqrstuvwxyz").isOK());
		assertFalse(rule.validate("ABCDEFGHIJKLMBNOPQRSTUVWXYZ").isOK());
		try {
			rule.validate(new Object());
			fail("expected a thrown ValidationFailure");
		} catch (final ValidationFailure f) {
			// test passed
		}
	}

	public final void testHindiLocale() throws Exception {
		// this language has unusual digits
		final Locale hindi = new Locale("hi", "IN");
		final ValidDecimal rule = new ValidDecimal(hindi);
		final DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance(hindi);
		assertTrue(rule.validate(format.format(12.3d)).isOK());
		assertTrue(rule.validate(format.format(12345.67d)).isOK());
		assertTrue(rule.validate(format.format(-12.3d)).isOK());
		assertTrue(rule.validate(format.format(-12345.67d)).isOK());
		assertFalse(rule.validate(format.format(10)).isOK());

		final ValidDecimal partial = new ValidDecimal(true, hindi);
		assertTrue(partial.validate(format.format(12.3d)).isOK());
		assertTrue(partial.validate(format.format(-12.3d)).isOK());
		assertTrue(rule.validate(format.format(-12345.67d)).isOK());
		assertTrue(partial.validate(format.format(10)).isOK());

		// TODO: more Tests for for Hindi(Indian)
	}

	public final void testThailandLocale() throws Exception {
		// this language has unusual digits
		final Locale thailand = new Locale("th", "TH");
		final ValidDecimal rule = new ValidDecimal(thailand);
		final DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance(thailand);
		assertTrue(rule.validate(format.format(12.3d)).isOK());
		assertTrue(rule.validate(format.format(12345.67d)).isOK());
		assertTrue(rule.validate(format.format(-12.3d)).isOK());
		assertTrue(rule.validate(format.format(-12345.67d)).isOK());
		assertFalse(rule.validate(format.format(10)).isOK());

		final ValidDecimal partial = new ValidDecimal(true, thailand);
		assertTrue(partial.validate(format.format(12.3d)).isOK());
		assertTrue(rule.validate(format.format(12345.67d)).isOK());
		assertTrue(partial.validate(format.format(-12.3d)).isOK());
		assertTrue(rule.validate(format.format(-12345.67d)).isOK());
		assertTrue(partial.validate(format.format(10)).isOK());
		// TODO: more tests for Thai (Thailand,TH)
	}

	public final void testArabLocale() throws Exception {
		// Arab countries seem to have a unified format
		// They have trailing minus signs, instead of leading
		final Locale arabEmirates = new Locale("ar", "AE");
		final ValidDecimal rule = new ValidDecimal(arabEmirates);
		final DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance(arabEmirates);
		assertTrue(rule.validate(format.format(12.3d)).isOK());
		assertTrue(rule.validate(format.format(12345.67d)).isOK());
		assertTrue(rule.validate(format.format(-12.3d)).isOK());
		assertTrue(rule.validate(format.format(-12345.67d)).isOK());
		assertFalse(rule.validate(format.format(10)).isOK());

		final ValidDecimal partial = new ValidDecimal(true, arabEmirates);
		assertTrue(partial.validate(format.format(12.3d)).isOK());
		assertTrue(rule.validate(format.format(12345.67d)).isOK());
		assertTrue(partial.validate(format.format(-12.3d)).isOK());
		assertTrue(rule.validate(format.format(-12345.67d)).isOK());
		assertTrue(partial.validate(format.format(10)).isOK());
		// TODO: more tests for Arab.

	}

	// TODO: Tests for Macedonia (mk_MK) with surrounding brackets

	public void _testPrintAvailableLocaleFormats() {
		final double number = -1234.56;
		for (final Locale locale : NumberFormat.getAvailableLocales()) {
			if (locale.getCountry().length() != 0) {
				System.out.print(locale.getDisplayName() + "(" + locale + "): ");
				final NumberFormat format = DecimalFormat.getInstance(locale);
				System.out.print('"' + format.format(number) + '"');
				try {
					System.out.println(" = [" + format.parse(format.format(number)) + ']');
				} catch (ParseException e) {
					System.out.println(" = [cannot reformat to Number]");
				}
			}
		}
	}

	/**
	 * Tests the method {@code setLocal(String[])}.
	 */
	public final void testSetLocalStringArray() throws Exception {

		MyValidDecimal validator = new MyValidDecimal();
		Locale locale = ReflectionUtils.getHidden(validator, "locale");
		assertSame(locale, Locale.getDefault());

		validator.setLocal(new String[] {});
		locale = ReflectionUtils.getHidden(validator, "locale");
		assertSame(locale, Locale.getDefault());

		validator.setLocal(new String[] { "hi" });
		locale = ReflectionUtils.getHidden(validator, "locale");
		assertEquals("hi", locale.getLanguage());
		assertEquals("", locale.getCountry());
		assertEquals("", locale.getVariant());

		validator.setLocal(new String[] { "hi", "IND" });
		locale = ReflectionUtils.getHidden(validator, "locale");
		assertEquals("hi", locale.getLanguage());
		assertEquals("IND", locale.getCountry());
		assertEquals("", locale.getVariant());

		validator.setLocal(new String[] { "es", "ES", "Traditional_WIN" });
		locale = ReflectionUtils.getHidden(validator, "locale");
		assertEquals("es", locale.getLanguage());
		assertEquals("ES", locale.getCountry());
		assertEquals("Traditional_WIN", locale.getVariant());

	}

	public void testSetInitializationData() throws Exception {

		ValidDecimal validator = new ValidDecimal();
		String localString = Locale.US.getLanguage() + "," + Locale.US.getCountry();
		validator.setInitializationData(null, null, "false," + localString);
		assertTrue(validator.validate("1,234.31").isOK());
		assertFalse(validator.validate("1").isOK());
		assertTrue(validator.validate("1.").isOK());

		validator = new ValidDecimal();
		validator.setInitializationData(null, null, "true," + localString);
		assertTrue(validator.validate("1,234.31").isOK());
		assertTrue(validator.validate("1").isOK());
		assertTrue(validator.validate("1.").isOK());

		validator = new ValidDecimal();
		localString = Locale.GERMANY.getLanguage() + "," + Locale.GERMANY.getCountry();
		validator.setInitializationData(null, null, "false," + localString);
		assertTrue(validator.validate("1.234,31").isOK());
		assertFalse(validator.validate("1").isOK());
		assertTrue(validator.validate("1,").isOK());

	}

	private class MyValidDecimal extends ValidDecimal {

		@Override
		public void setLocal(String[] localArgs) {
			super.setLocal(localArgs);
		}

	}

}
