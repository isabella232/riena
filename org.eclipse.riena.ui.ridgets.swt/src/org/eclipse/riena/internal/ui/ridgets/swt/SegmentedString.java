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

	public static boolean isDigit(char fChar) {
		return Character.isDigit(fChar);
	}

	public static boolean isSeparator(char fChar) {
		return ".:/- ".indexOf(fChar) != -1; //$NON-NLS-1$
	}

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

	public int delete(int from, int to) {
		return delete(from, to, true);
	}

	public int findNewCursorPosition(int caretPosition, int delta) {
		Assert.isLegal(delta == 1 || delta == -1);
		int pos = caretPosition;
		if (caretPosition + delta <= pattern.length() && caretPosition + delta >= 0) {
			pos = caretPosition + delta;
		}
		while (pos < pattern.length() && pos > -1) {
			if (pattern.charAt(pos) == '|') {
				break;
			} else if (pattern.charAt(pos) == 'd' && fields[pos] == ' ') {
				pos = pos + delta;
			} else {
				break;
			}
		}
		return pos;
	}

	public String getPattern() {
		return pattern;
	}

	public int insert(int index, String value) {
		int idx = index;
		boolean proceed = true;
		for (int i = 0; proceed && i < value.length(); i++) {
			char sChar = value.charAt(i);
			proceed = isDigit(sChar) || isSeparator(sChar);
			if (proceed) {
				idx = insert(idx, sChar);
			}
		}
		idx = idx + shiftSpacesLeft(idx);
		return idx;
	}

	public int replace(int from, int to, String value) {
		delete(from, to, false);
		int idx = insert(from, value);
		return idx;
	}

	public int shiftSpacesLeft(final int index) {
		int delta = 0;
		for (int i = 0; i < fields.length - 1; i++) {
			if (isDigit(fields[i]) && fields[i + 1] == ' ' && pattern.charAt(i + 1) == 'd') {
				fields[i + 1] = fields[i];
				fields[i] = ' ';
				if (index <= i + 1) {
					delta++;
				}
				if (i > 0) {
					i = i - 2;
				}
			}
		}
		return delta;
	}

	@Override
	public String toString() {
		return String.valueOf(fields);
	}

	// helping methods
	//////////////////

	private int computeCursorPositionAfterDelete(int from, int to) {
		int result = -1;
		int left = from + 1;
		int right = findGroupEnd(to);
		if (right < pattern.length() - 1) {
			right++;
		}
		for (int i = left; result == -1 && i <= right; i++) {
			if (isDigit(fields[i])) {
				// right of first number in group
				result = i;
			} else if (pattern.charAt(i) == '|') {
				// right of first separator in group
				result = i;
			}
		}
		if (result == -1) {
			result = left;
		}
		// beginning of group
		return result;
	}

	private String createPattern(String format) {
		StringBuilder result = new StringBuilder(format.length());
		for (int i = 0; i < format.length(); i++) {
			char fChar = format.charAt(i);
			if (isDigitPattern(fChar)) {
				result.append('d');
			} else if (isSeparator(fChar)) {
				result.append('|');
			} else {
				throw new IllegalStateException("unsupported format character: " + fChar); //$NON-NLS-1$
			}
		}
		return result.toString();
	}

	private int delete(int from, int to, boolean shift) {
		Assert.isLegal(from > -1, "'from' out of bounds: " + from); //$NON-NLS-1$
		Assert.isLegal(from <= to, String.format("'from' must be less-or-equal than 'to': %d, %d", from, to)); //$NON-NLS-1$
		Assert.isLegal(to < fields.length, "'to' out of bounds: " + to); //$NON-NLS-1$
		for (int i = from; i <= to; i++) {
			if (pattern.charAt(i) != '|') {
				fields[i] = ' ';
			}
		}
		int pos = computeCursorPositionAfterDelete(from, to);
		int delta = 0;
		if (shift) {
			delta = shiftSpacesLeft(to);
		}
		return pos + delta;
	}

	private int findFreePosition(int index) {
		int result = -1;
		final int pos = index < pattern.length() ? index : pattern.length() - 1;
		if (fields[pos] == ' ' && pattern.charAt(pos) == 'd') {
			result = pos;
		} else if (groupHasSpaceOnLeft(pos)) {
			shiftRight(index - 1);
			Assert.isLegal(fields[index - 1] == ' ');
			result = index - 1;
		} else if (groupHasSpaceOnRight(pos)) {
			result = pos + 1;
		}
		return result;
	}

	private void shiftRight(final int index) {
		int spacePos = -1;
		int pos = index;
		while (pos > -1 && spacePos == -1) {
			if (fields[pos] == ' ') {
				spacePos = pos;
			} else {
				pos--;
			}
		}
		String msg = String.format("did not find space in '%s' starting at %d", String.valueOf(fields), index); //$NON-NLS-1$
		Assert.isLegal(spacePos != -1, msg);
		Assert.isLegal(fields[spacePos] == ' ');
		for (int i = spacePos; i < index; i++) {
			fields[i] = fields[i + 1];
			fields[i + 1] = ' ';
		}
	}

	private boolean groupHasSpaceOnLeft(final int index) {
		boolean result = false;
		int pos = index;
		while (pos > 0 && !result) {
			result = pattern.charAt(pos - 1) == 'd' && fields[pos - 1] == ' ';
			if (pattern.charAt(pos - 1) == '|') {
				pos = -1;
			} else {
				pos--;
			}
		}
		return result;
	}

	private boolean groupHasSpaceOnRight(int index) {
		return index < pattern.length() - 1 && pattern.charAt(index) == '|' && pattern.charAt(index + 1) == 'd'
				&& fields[index + 1] == ' ';
	}

	private int findGroupEnd(int index) {
		int right = index;
		while (right < pattern.length() - 1 && pattern.charAt(right + 1) != '|') {
			right++;
		}
		return right;
	}

	private int insert(int index, char ch) {
		Assert.isLegal(index > -1, "index out of bounds: " + index); //$NON-NLS-1$
		Assert.isLegal(index <= fields.length, "index out of bounds: " + index); //$NON-NLS-1$
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
		} else if (index == fields.length) {
			if (isDigit(ch)) {
				int freePosition = findFreePosition(index);
				if (freePosition != -1) {
					fields[freePosition] = ch;
					result = freePosition + 1;
				}
			}
		}
		return result;
	}

	private boolean isDigitPattern(char fChar) {
		return "dMyHms".indexOf(fChar) != -1; //$NON-NLS-1$
	}

}
