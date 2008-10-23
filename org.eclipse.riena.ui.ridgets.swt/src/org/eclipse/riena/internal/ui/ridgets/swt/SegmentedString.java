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
public class SegmentedString {

	private final char[] fields;
	private String pattern;

	public SegmentedString(String format) {
		pattern = createPattern(format);
		fields = new char[pattern.length()];
		for (int i = 0; i < pattern.length(); i++) {
			char pChar = pattern.charAt(i);
			fields[i] = pChar != 'd' ? format.charAt(i) : ' ';
		}
	}

	public SegmentedString(String format, String value) {
		pattern = createPattern(format);
		fields = new char[pattern.length()];
		for (int i = 0; i < value.length(); i++) {
			fields[i] = value.charAt(i);
		}
	}

	public void delete(int from, int to) {
		Assert.isLegal(from > -1, "'from' out of bounds: " + from);
		Assert.isLegal(from <= to, String.format("'from' must be less-or-equal than 'to': %d, %d", from, to));
		Assert.isLegal(to < fields.length, "'to' out of bounds: " + to);
		for (int i = from; i <= to; i++) {
			if (pattern.charAt(i) != '|') {
				fields[i] = ' ';
			}
		}
		shiftSpacesLeft();
	}

	public String getPattern() {
		return pattern;
	}

	public void insert(int index, String value) {
		int idx = index;
		boolean proceed = true;
		for (int i = 0; proceed && i < value.length(); i++) {
			char sChar = value.charAt(i);
			proceed = isDigit(sChar) || isSeparator(sChar);
			if (proceed) {
				idx = insert(idx, sChar);
			}
		}
		shiftSpacesLeft();
	}

	public void replace(int from, int to, String value) {
		delete(from, to);
		insert(from, value);
	}

	@Override
	public String toString() {
		return String.valueOf(fields);
	}

	// helping methods
	//////////////////

	private String createPattern(String format) {
		StringBuilder result = new StringBuilder(format.length());
		for (int i = 0; i < format.length(); i++) {
			char fChar = format.charAt(i);
			if (isDigitPattern(fChar)) {
				result.append('d');
			} else if (isSeparator(fChar)) {
				result.append('|');
			} else {
				throw new IllegalStateException("unsupported format character: " + fChar);
			}
		}
		return result.toString();
	}

	private int findFreePosition(int index) {
		int result = -1;
		int left = index;
		while (left > 0 && pattern.charAt(left - 1) != '|') {
			left--;
		}
		int right = index + 1;
		while (right < pattern.length() - 1 && pattern.charAt(right + 1) != '|') {
			right++;
		}
		// System.out.println(left + ", " + right);
		for (int i = left; result == -1 && i <= right; i++) {
			if (fields[i] == ' ') {
				result = i;
			}
		}
		return result;
	}

	private int insert(int index, char ch) {
		Assert.isLegal(index > -1, "index out of bounds: " + index);
		Assert.isLegal(index <= fields.length, "index out of bounds: " + index);
		int result = index;
		if (index < fields.length) {
			if (isSeparator(ch) && isSeparator(fields[index])) {
				result = index + 1;
			} else if (isDigit(ch)) {
				int freePosition = findFreePosition(index);
				if (freePosition != -1) {
					fields[freePosition] = ch;
					result = freePosition + 1;
				}
			}
		}
		return result;
	}

	private boolean isDigit(char fChar) {
		return Character.isDigit(fChar);
	}

	private boolean isDigitPattern(char fChar) {
		return "dMyHms".indexOf(fChar) != -1;
	}

	private boolean isSeparator(char fChar) {
		return ".:/- ".indexOf(fChar) != -1;
	}

	private void shiftSpacesLeft() {
		for (int i = 0; i < fields.length - 1; i++) {
			if (isDigit(fields[i]) && fields[i + 1] == ' ' && pattern.charAt(i + 1) == 'd') {
				fields[i + 1] = fields[i];
				fields[i] = ' ';
			}
		}
	}
}
