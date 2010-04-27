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
package org.eclipse.riena.example.client.views;

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;

/**
 * TODO [ev] experimental
 */
public class ComboAutocompleter implements KeyListener, ModifyListener {

	private Control control;

	private boolean ignore;

	public ComboAutocompleter(CCombo combo) {
		this.control = combo;
		combo.addKeyListener(this);
		combo.addModifyListener(this);
	}

	public ComboAutocompleter(Combo combo) {
		this.control = combo;
		combo.addKeyListener(this);
		combo.addModifyListener(this);
	}

	public void keyPressed(KeyEvent e) {
		ignore = !Character.isLetterOrDigit(e.character);
		if (e.keyCode == 13) {
			addText();
			int end = getTextFromControl().length();
			setSelection(end, end);
		}
	}

	public void keyReleased(KeyEvent e) {
		// unused
	}

	public void modifyText(ModifyEvent e) {
		if (!ignore) {
			ignore = true;
			completeText();
		}
	}

	// helping methods
	//////////////////

	private void addItem(String item) {
		if (control instanceof Combo) {
			((Combo) control).add(item);
		} else {
			((CCombo) control).add(item);
		}
	}

	private void addText() {
		String prefix = getTextFromControl();
		if (indexOf(prefix) == -1) {
			// TODO [ev] this should actually be added to the model...
			addItem(prefix);
			System.out.println("added: " + prefix); //$NON-NLS-1$
		}
	}

	private void completeText() {
		String prefix = getTextFromControl();
		String match = findMatch(prefix);
		if (match != null) {
			setTextToControl(match);
			int start = prefix.length();
			int end = match.length();
			setSelection(start, end);
		}
	}

	private String findMatch(String prefix) {
		String result = null;
		int count = 0;
		String prefLC = prefix.toLowerCase();
		for (String item : getItems()) {
			if (item.toLowerCase().startsWith(prefLC)) {
				count++;
				result = item;
			}
		}
		return count == 1 ? result : null;
	}

	private String[] getItems() {
		if (control instanceof Combo) {
			return ((Combo) control).getItems();
		}
		return ((CCombo) control).getItems();
	}

	private String getTextFromControl() {
		if (control instanceof Combo) {
			return ((Combo) control).getText();
		}
		return ((CCombo) control).getText();
	}

	private int indexOf(String item) {
		if (control instanceof Combo) {
			return ((Combo) control).indexOf(item);
		}
		return ((CCombo) control).indexOf(item);
	}

	private void setSelection(int start, int end) {
		if (control instanceof Combo) {
			((Combo) control).setSelection(new Point(start, end));
		} else {
			((CCombo) control).setSelection(new Point(start, end));
		}
	}

	private void setTextToControl(String match) {
		if (control instanceof Combo) {
			((Combo) control).setText(match);
		} else {
			((CCombo) control).setText(match);
		}
	}

}