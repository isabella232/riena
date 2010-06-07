/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.beans.common;

/**
 * TODO [ev] javadoc
 * 
 * @since 2.0
 */
public final class SuperHero {

	private String pseudonym;
	private String name;
	private Integer appearance;
	private boolean active;

	public SuperHero(String pseudonym, String name, int appearance) {
		this(pseudonym, name, appearance, true);
	}

	public SuperHero(String pseudonym, String name, int appearance, boolean active) {
		this.pseudonym = pseudonym;
		this.name = name;
		this.appearance = appearance;
		this.active = active;
	}

	public String getPseudonym() {
		return pseudonym;
	}

	public String getName() {
		return name;
	}

	public Integer getAppearance() {
		return appearance;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return String.format("%s (%s)", pseudonym, name); //$NON-NLS-1$
	}
}