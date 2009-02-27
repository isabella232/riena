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
package org.eclipse.riena.internal.security.common;

import java.util.HashMap;

import org.eclipse.riena.core.util.ContainerModel;
import org.eclipse.riena.security.common.ISubjectHolder;
import org.eclipse.riena.security.common.ISubjectHolderService;

public class SubjectHolderService implements ISubjectHolderService {

    private HashMap<Thread, ISubjectHolder> subjectHolderMap;
    private ISubjectHolder subjectHolder;

    public SubjectHolderService() {
        super();
        if (ContainerModel.isClient()) {
            subjectHolder = new SimpleSubjectHolder();
        } else {
            subjectHolderMap = new HashMap<Thread, ISubjectHolder>();
        }
    }

    public ISubjectHolder fetchSubjectHolder() {
        if (ContainerModel.isClient()) {
            return subjectHolder;
        } else {
            ISubjectHolder holder = subjectHolderMap.get(Thread.currentThread());
            if (holder == null) {
                holder = new SimpleSubjectHolder();
                subjectHolderMap.put(Thread.currentThread(), holder);
            }
            return holder;
        }
    }

}
