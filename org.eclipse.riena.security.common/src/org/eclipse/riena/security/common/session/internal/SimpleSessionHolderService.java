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
package org.eclipse.riena.security.common.session.internal;

import java.util.HashMap;

import org.eclipse.riena.core.util.ContainerModel;
import org.eclipse.riena.security.common.session.ISessionHolder;
import org.eclipse.riena.security.common.session.ISessionHolderService;

public class SimpleSessionHolderService implements ISessionHolderService {

    private HashMap<Thread, ISessionHolder> sessionHolderMap;
    private ISessionHolder sessionHolder;

    public SimpleSessionHolderService() {
        super();
        if (ContainerModel.isClient()) {
            sessionHolder = new SimpleSessionHolder();
        } else {
            sessionHolderMap = new HashMap<Thread, ISessionHolder>();
        }
    }

    public ISessionHolder fetchSessionHolder() {
        if (ContainerModel.isClient()) {
            return sessionHolder;
        } else {
            ISessionHolder holder = sessionHolderMap.get(Thread.currentThread());
            if (holder == null) {
                holder = new SimpleSessionHolder();
                sessionHolderMap.put(Thread.currentThread(), holder);
            }
            return holder;
        }
    }

}
