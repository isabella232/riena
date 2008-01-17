/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.security.common.authentication.credentials;

/**
 * 
 */
public class ChoiceCredential extends AbstractCredential {

	private String[] choices;
	private int defaultChoice;
	private boolean multipleSelectionsAllowed;
	private int[] selections;

	/**
	 * @param prompt
	 */
	public ChoiceCredential(String prompt, String[] choices, int defaultChoice, boolean multipleSelectionsAllowed) {
		super(prompt);
		this.choices = choices;
		this.defaultChoice = defaultChoice;
		this.multipleSelectionsAllowed = multipleSelectionsAllowed;
	}

	public String[] getChoices() {
		return choices;
	}

	public int getDefaultChoice() {
		return defaultChoice;
	}

	public boolean isMultipleSelectionsAllowed() {
		return multipleSelectionsAllowed;
	}

	public int[] getSelections() {
		return selections;
	}

	public void setSelections(int[] selections) {
		this.selections = selections;
	}

}
