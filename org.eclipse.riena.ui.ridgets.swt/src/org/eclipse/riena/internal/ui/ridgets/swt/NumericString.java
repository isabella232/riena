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

import org.eclipse.core.runtime.Assert;

/**
 * TODO [ev] docs
 */
public class NumericString {

	private Digit start;
	private final boolean isGrouping;

	/**
	 * TODO [ev] docs
	 * 
	 * @param value
	 * @param isGrouping
	 */
	public NumericString(String value, boolean isGrouping) {
		Assert.isNotNull(value);
		if (value.length() > 0) {
			Digit prev = null;
			for (int i = 0; i < value.length(); i++) {
				char ch = value.charAt(i);
				prev = new Digit(ch, prev);
				if (i == 0) {
					start = prev;
				}
			}
		}
		this.isGrouping = isGrouping;
		if (isGrouping) {
			applyGrouping();
		}
	}

	/**
	 * TODO [ev] docs Delete between {@code from} and {@code to} (inclusive)
	 * preserving separators.
	 * 
	 * @param from
	 *            0-based starting position
	 * @param to
	 *            0-based ending position (inclusive; {@code from <= to <
	 *            pattern.length})
	 * @return the new cursor position
	 * @throws RuntimeException
	 *             if {@code from} or {@code to} are not valid
	 */
	public int delete(int from, int to, char ch) {
		Assert.isLegal(-1 < from);
		Assert.isLegal(from < to);
		final int delta = '\b' == ch ? -1 : 1;
		if (to - from == 1) {
			Digit digit = findDigit(from);
			digit.delete(delta);
		} else {

		}
		applyGrouping();
		return getCursorLocation();
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		if (start != null) {
			Digit current = start;
			while (current != null) {
				result.append(current.toString());
				current = current.getNext();
			}
		}
		return result.toString();
	}

	// helping methods
	//////////////////

	private void applyGrouping() {
		if (!isGrouping) {
			return;
		}
		Digit here = start;
		while (here != null) {
			if (here.isGroupingSeparator()) {
				here.deleteThis();
			}
			here = here.next;
		}
		Digit decSep = null;
		Digit lastDigit = null;
		here = start;
		while (decSep == null && here != null) {
			if (here.isDecimalSeparator()) {
				decSep = here;
			}
			if (here.next == null) {
				lastDigit = here;
			}
			here = here.next;
		}
		Digit firstDigit = decSep != null ? decSep.prev : lastDigit;
		int i = 2;
		while (firstDigit != null) {
			if (i == 0 && firstDigit.prev != null && firstDigit.prev.ch != '-') {
				Digit sep = new Digit(NumericTextRidget.GROUPING_SEPARATOR, null);
				sep.next = firstDigit;
				sep.prev = firstDigit.prev;
				firstDigit.prev.next = sep;
				firstDigit.prev = sep;
				i = 4;

			}
			i--;
			firstDigit = firstDigit.prev;
		}
	}

	private int getCursorLocation() {
		int result = -1;
		Digit here = start;
		int index = 0;
		while (result == -1 && here != null) {
			if (here.isCursorBeforeMe()) {
				result = index;
			} else if (here.isCursorAfterMe()) {
				result = index + 1;
			}
			index++;
			here = here.getNext();
		}
		return result;
	}

	private Digit findDigit(final int index) {
		Digit result = null;
		Digit here = start;
		int count = index;
		while (here != null && count >= 0) {
			if (count == 0) {
				result = here;
			}
			here = here.next;
			count--;
		}
		return result;
	}

	// helping classes
	//////////////////

	private final class Digit {

		private final char ch;
		private boolean cursorAfterMe;
		private boolean cursorBeforeMe;
		private Digit prev;
		private Digit next;

		public Digit(char ch, Digit prev) {
			this.ch = ch;
			this.prev = prev;
			if (prev != null) {
				prev.next = this;
			}
		}

		public void deleteThis() {
			if (prev != null) {
				prev.next = next;
			}
			if (next != null) {
				next.prev = prev;
			}
			if (prev == null) {
				start = next;
			}
			if (cursorAfterMe) {
				if (next != null)
					next.setCursorBefore();
				else if (prev != null)
					prev.setCursorAfter();
			}
			if (cursorBeforeMe) {
				if (prev != null)
					prev.setCursorAfter();
				else if (next != null)
					next.setCursorBefore();
			}
		}

		public void delete(int delta) {
			Assert.isLegal(delta == -1 || delta == 1);
			if (isSeparator()) {
				if (delta == -1) {
					prev.delete(delta);
				} else {
					next.delete(delta);
				}
			} else {
				deleteThis();
				if (prev != null) {
					prev.setCursorAfter();
				} else if (next != null) {
					next.setCursorBefore();
				}
			}
		}

		public Digit getNext() {
			return next;
		}

		public boolean isCursorAfterMe() {
			return cursorAfterMe;
		}

		public boolean isCursorBeforeMe() {
			return cursorBeforeMe;
		}

		public boolean isSeparator() {
			return isDecimalSeparator() || isGroupingSeparator();
		}

		public boolean isDecimalSeparator() {
			return NumericTextRidget.DECIMAL_SEPARATOR == ch;
		}

		public boolean isGroupingSeparator() {
			return NumericTextRidget.GROUPING_SEPARATOR == ch;
		}

		public void setCursorAfter() {
			resetCursorLocation();
			cursorAfterMe = true;
			if (next != null) {
				next.cursorBeforeMe = true;
			}
		}

		public void setCursorBefore() {
			resetCursorLocation();
			cursorBeforeMe = true;
			if (prev != null) {
				prev.cursorAfterMe = true;
			}
		}

		@Override
		public String toString() {
			return String.valueOf(ch);
		}

		private void resetCursorLocation() {
			Digit p = this;
			while (p != null) {
				p.cursorAfterMe = false;
				p.cursorBeforeMe = false;
				p = p.prev;
			}
			Digit n = this.next;
			while (n != null) {
				n.cursorAfterMe = false;
				n.cursorBeforeMe = false;
				n = n.next;
			}
		}

	};

}
