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
package org.eclipse.riena.internal.communication.core;

import java.util.List;
import java.util.Map;

import org.eclipse.riena.communication.core.hooks.CallContext;
import org.eclipse.riena.communication.core.hooks.ICallHook;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


/**
 * @author Alexander Ziegler
 * @author Christian Campo
 * 
 */
public class Activator implements BundleActivator {

    private static BundleContext CONTEXT;

    /*
     * (non-Javadoc)
     * 
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        CONTEXT = context;
        context.registerService(ICallHook.ID, new ICallHook() {

            public void afterCall(CallContext context) {
                System.out.println("after call (in hook) method=" + context.getMethodName());
                Map<String, List<String>> headers = context.getMessageContext().listResponseHeaders();
                if (headers != null) {
                    for (String hName : headers.keySet()) {
                        StringBuffer sb = new StringBuffer();
                        for (String hValue : headers.get(hName)) {
                            sb.append(hValue + ", ");
                        }
                        System.out.println("header: name:" + hName + " value:" + sb);
                    }
                }
            }

            public void beforeCall(CallContext context) {
                context.getMessageContext().addRequestHeader("Cookie", "x-scpclient-test-sessionid=222");
                System.out.println("before call (in hook) method=" + context.getMethodName());
            }
        }, null);

        // context.registerService(ConfigurationPlugin.class.getName(), new SymbolConfigPlugin(), null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        CONTEXT = null;
    }

    public static BundleContext getContext() {
        return CONTEXT;
    }

}
