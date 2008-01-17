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

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.HashMap;

import org.eclipse.riena.communication.core.RemoteServiceDescription;
import org.eclipse.riena.communication.core.hooks.IServiceMessageContext;
import org.eclipse.riena.communication.core.hooks.IServiceMessageContextAccessor;
import org.eclipse.riena.communication.core.publisher.IServicePublisher;

/**
 * This is a Hessian based implementation of {@link IServicePublisher}. it publish or unpublish OSGi Services as
 * service end points for Hessian protocol.
 * <p>
 * HessianRemoteServicePublisher becomes registered as OSGi Service with name {@link IServicePublisher#ID}. The OSGi
 * Service set the property "riena.protocol=hessian".
 * 
 * @author Alexander Ziegler
 * @author Christian Campo
 * 
 */
public class HessianRemoteServicePublisher implements IServicePublisher {
    private final static String PROTOCOL = "hessian";
    private final static String SERVLET_PATH = "/hessian";
    private IServiceMessageContextAccessor mca = new MsgCxtAcc();

    private HashMap<String, RemoteServiceDescription> webServiceDescriptions;

    public HessianRemoteServicePublisher() {
        webServiceDescriptions = new HashMap<String, RemoteServiceDescription>();
    }

    public void log(String message) {
        System.out.println(message);
    }

    public void log(String message, Exception e) {
        System.out.println(message + "(" + e.getMessage() + ")");
    }

    /*
     * (non-Javadoc)
     * 
     * @see xeval.rcplabs.hessianx.server.IWebServicePublisher#publishService(RemoteServiceDescription rsd )
     */
    public synchronized String publishService(RemoteServiceDescription rsd) {
        String localhost = "localhost";
        try {
            localhost = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
        }
        String url = "http://" + localhost + SERVLET_PATH + rsd.getPath();
        rsd.setURL(url);
        webServiceDescriptions.put(SERVLET_PATH + rsd.getPath(), rsd);
        log("Riena:Communication::HessianRemoteServicePublisher:: DEBUG: published web service. " + rsd);
        log("Riena:Communication::HessianRemoteServicePublisher:: DEBUG: web service count: " + webServiceDescriptions.size());
        return url;
    }

    /*
     * (non-Javadoc)
     * 
     * @see xeval.rcplabs.hessianx.server.IWebServicePublisher#unpublishService(java.lang.String)
     */
    public synchronized void unpublishService(String path) {
        RemoteServiceDescription rsd = webServiceDescriptions.remove(SERVLET_PATH + path);
        log("Riena:communication::HessianRemoteServicePublisher:: DEBUG: unpublished web service. " + rsd);
        log("Riena:communication::HessianRemoteServicePublisher:: DEBUG: web service count: " + webServiceDescriptions.size());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.riena.communication.core.IRemoteServiceProtocol#getProtocol()
     */
    public String getProtocol() {
        return PROTOCOL;
    }

    public synchronized RemoteServiceDescription findService(String requestURI) {
        RemoteServiceDescription rsd = webServiceDescriptions.get(requestURI);
        return rsd;
    }

    public IServiceMessageContextAccessor getMessageContextAccessor() {
        return mca;
    }

    class MsgCxtAcc implements IServiceMessageContextAccessor {
        MsgCxtAcc() {
            super();
        }

        public IServiceMessageContext getMessageContext() {
            return MessageContextAccessor.getMessageContext();
        }
    }
}