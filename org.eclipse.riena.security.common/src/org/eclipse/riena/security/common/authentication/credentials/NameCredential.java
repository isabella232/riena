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

public class NameCredential extends AbstractCredential {

	private String defaultName;
	private String name;

	public NameCredential(String prompt) {
		super(prompt);
	}

	public NameCredential(String prompt, String defaultName) {
		this(prompt);
		this.defaultName = defaultName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefaultName() {
		return defaultName;
	}

}
