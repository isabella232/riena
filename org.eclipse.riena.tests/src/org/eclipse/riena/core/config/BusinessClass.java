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
package org.eclipse.riena.core.config;

/**
 * Test Business Java POJO that is configured through the ConfigUtility
 * 
 */
public class BusinessClass {

    public String name = "default";

    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
    	return name;
    }

    public void invoke(String step) {
        System.out.println("(" + step + ")" + "  name=" + name + " " + System.currentTimeMillis());
    }
}
