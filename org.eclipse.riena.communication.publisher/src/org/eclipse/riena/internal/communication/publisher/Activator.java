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
package org.eclipse.riena.internal.communication.publisher;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.eclipse.riena.communication.core.hooks.IServiceHook;
import org.eclipse.riena.communication.core.hooks.ServiceContext;
import org.eclipse.riena.communication.core.publisher.IServicePublishEventDispatcher;
import org.eclipse.riena.communication.core.publisher.IServicePublisher;
import org.eclipse.riena.communication.core.publisher.RSDPublisherProperties;
import org.eclipse.riena.communication.core.util.CommunicationUtil;
import org.eclipse.riena.core.service.ServiceInjector;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;


public class Activator implements BundleActivator {

    private ServicePublishEventDispatcher dispachter;
    private ServiceInjector publisherInjector;
    private UpdateNotifierRemoteService updateNotifierRemoteService;
    private static BundleContext context;

    /*
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        Activator.context = context;

        dispachter = new ServicePublishEventDispatcher(context);
        publisherInjector = new ServiceInjector(context, IServicePublisher.ID, dispachter, "addServicePublisher", "removeServicePublisher");
        publisherInjector.start();

        // register as OSGi service, the start will pick up the OSGi service and
        // publish it
        Dictionary<String, Object> properties = new Hashtable<String, Object>(3);
        properties.put(RSDPublisherProperties.PROP_IS_REMOTE, "true");
        properties.put(RSDPublisherProperties.PROP_REMOTE_PROTOCOL, "hessian");
        properties.put(RSDPublisherProperties.PROP_REMOTE_PATH, "/ServicePublisherWS");

        context.registerService(IServicePublishEventDispatcher.ID, dispachter, properties);

        dispachter.start();

        // register UpdateNotified so all services trigger the
        // servicePublishEventDispatcher
        updateNotifierRemoteService = new UpdateNotifierRemoteService(dispachter);
        context.addServiceListener(updateNotifierRemoteService);
        context.registerService(IServiceHook.ID, new IServiceHook() {

            public void afterService(ServiceContext context) {
                System.out.println("after service (in hook)");
                context.getMessageContext().addResponseHeader("Set-Cookie", "x-scpserver-test-sessionid=11");
            }

            public void beforeService(ServiceContext context) {
                System.out.println("before service (in hook)");
                Map<String, List<String>> headers = context.getMessageContext().listRequestHeaders();
                for (String hName : headers.keySet()) {
                    StringBuffer sb = new StringBuffer();
                    for (String hValue : headers.get(hName)) {
                        sb.append(hValue + ", ");
                    }
                    System.out.println("header: name:" + hName + " value:" + sb);
                }
            }
        }, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        context.removeServiceListener(updateNotifierRemoteService);
        dispachter.stop();
        dispachter = null;
        publisherInjector.dispose();
        publisherInjector = null;
        updateNotifierRemoteService = null;
        Activator.context = null;
    }

    public static BundleContext getContext() {
        return context;
    }

    class UpdateNotifierRemoteService implements ServiceListener {
        ServicePublishEventDispatcher dispatcher;

        UpdateNotifierRemoteService(ServicePublishEventDispatcher dispatcher) {
            this.dispatcher = dispatcher;
        }

        public void serviceChanged(ServiceEvent event) {
            ServiceReference serviceRef = event.getServiceReference();

            // check if we are getting the event for a new remote service
            String isRemote = CommunicationUtil.accessProperty(serviceRef.getProperty(RSDPublisherProperties.PROP_IS_REMOTE), null);
            String remoteType = CommunicationUtil.accessProperty(serviceRef.getProperty(RSDPublisherProperties.PROP_REMOTE_PROTOCOL), null);
            if (remoteType == null || remoteType.length() == 0) {
                return;
            }
            if ("true".equals(isRemote)) {
                if (event.getType() == ServiceEvent.UNREGISTERING) {
                    dispatcher.unpublish(event.getServiceReference());
                } else if (event.getType() == ServiceEvent.REGISTERED) {
                    dispatcher.publish(event.getServiceReference());
                }
            }
        }
    }
}
