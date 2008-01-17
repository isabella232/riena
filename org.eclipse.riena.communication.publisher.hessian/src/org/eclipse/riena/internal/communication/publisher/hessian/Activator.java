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
package org.eclipse.riena.internal.communication.publisher.hessian;

import java.util.Hashtable;

import org.eclipse.riena.communication.core.publisher.IServicePublisher;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;


/**
 * The activator class controls the plug-in life cycle
 */
public class Activator implements BundleActivator {
    private static BundleContext CONTEXT;
    private ServiceRegistration publisherReg;
    private static HessianRemoteServicePublisher publisher;

    /**
     * The constructor
     */
    public Activator() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        CONTEXT = context;
        System.out.println("start hessian support on server");

        publisher = new HessianRemoteServicePublisher();
        Hashtable<String, Object> properties = new Hashtable<String, Object>(1);
        properties.put(IServicePublisher.PROP_PROTOCOL, publisher.getProtocol());
        publisherReg = context.registerService(IServicePublisher.ID, publisher, properties);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        publisherReg.unregister();
        publisherReg = null;
        publisher = null;

        CONTEXT = null;
        System.out.println("stop hessian support on server");
    }

    public static BundleContext getContext() {
        return CONTEXT;
    }

    /**
     * 
     * @return the publisher or null if the bundle is stopped
     */
    public static HessianRemoteServicePublisher getPublisher() {
        return publisher;
    }
}
