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
package org.eclipse.riena.communication.publisher.hessian;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.HashMap;

import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.riena.communication.core.RemoteServiceDescription;
import org.eclipse.riena.core.logging.ConsoleLogger;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.communication.publisher.hessian.Activator;
import org.eclipse.riena.internal.communication.publisher.hessian.HessianRemoteServicePublisher;
import org.eclipse.riena.internal.communication.publisher.hessian.MessageContext;
import org.eclipse.riena.internal.communication.publisher.hessian.MessageContextAccessor;

import org.eclipse.equinox.log.Logger;
import org.osgi.service.log.LogService;

import com.caucho.hessian.io.AbstractDeserializer;
import com.caucho.hessian.io.AbstractHessianInput;
import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.AbstractSerializerFactory;
import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.HessianOutput;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.JavaDeserializer;
import com.caucho.hessian.io.Serializer;
import com.caucho.hessian.io.SerializerFactory;
import com.caucho.hessian.server.HessianSkeleton;

/**
 * @author Christian Campo
 * 
 */
@SuppressWarnings("serial")
public class HessianRienaDispatcherServlet extends GenericServlet {

	private SerializerFactory serializerFactory = null;

	private final static Logger logger = new ConsoleLogger(HessianRienaDispatcherServlet.class.getName());

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		serializerFactory = new SerializerFactory();
		serializerFactory.setAllowNonSerializable(true);
		serializerFactory.addFactory(new AbstractSerializerFactory() {
			@Override
			public Deserializer getDeserializer(Class cl) throws HessianProtocolException {
				if (cl.isInterface() && (!cl.getPackage().getName().startsWith("java") || cl == Principal.class)) {
					return new JavaDeserializer(cl);
				}
				return null;
			}

			@Override
			public Serializer getSerializer(Class cl) throws HessianProtocolException {
				return null;
			}
		});
		addSpecialDeserializer(serializerFactory);
		logger.log(LogService.LOG_DEBUG, "initialized");
	}

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {

		HttpServletRequest httpReq = (HttpServletRequest) req;
		HttpServletResponse httpRes = (HttpServletResponse) res;

		// set the message context
		MessageContextAccessor.setMessageContext(new MessageContext(httpReq, httpRes));

		HessianRemoteServicePublisher publisher = getPublisher();
		if (publisher == null) {
			if (httpReq.getMethod().equals("GET")) {
				if (httpReq.getRemoteHost().equals("127.0.0.1")) {
					PrintWriter pw = new PrintWriter(res.getOutputStream());
					pw.write("no webservices available");
					pw.flush();
					pw.close();
					return;
				} else {
					httpRes.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Hessian requires POST");
					return;
				}
			} else {
				httpRes.sendError(HttpServletResponse.SC_NOT_FOUND, "webservice not found");
				return;
			}
		}
		String requestURI = httpReq.getRequestURI();
		String contextPath = httpReq.getContextPath();
		if (contextPath.length() > 1) {
			requestURI = requestURI.substring(contextPath.length());
		}
		RemoteServiceDescription rsd = publisher.findService(requestURI);
		log("call " + rsd);
		if (httpReq.getMethod().equals("GET")) {
			if (httpReq.getRemoteHost().equals("127.0.0.1")) {
				PrintWriter pw = new PrintWriter(res.getOutputStream());
				if (rsd == null) {
					pw.write("call received from browser, no remote service registered with this URL");
				} else {
					pw.write("calls " + rsd);
				}
				pw.flush();
				pw.close();
				return;
			} else {
				httpRes.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Hessian requires POST");
				return;
			}
		}
		if (rsd == null) {
			httpRes.sendError(HttpServletResponse.SC_NOT_FOUND, "unknown url :" + httpReq.getRequestURI());
			return;
		}
		Object instance = rsd.getService();
		HessianSkeleton sk = new HessianSkeleton(instance, rsd.getServiceInterfaceClass());
		Hessian2Input inp = new Hessian2Input(httpReq.getInputStream());
		AbstractHessianOutput out;
		inp.setSerializerFactory(serializerFactory);

		int code = inp.read();
		if (code != 'c') {
			throw new IOException("expected 'c' in hessian input at " + code);
		}
		int major = inp.read();
		// int minor = inp.read();
		inp.read(); // TODO obsolete?? (replaced line above)
		if (major >= 2) {
			out = new Hessian2Output(httpRes.getOutputStream());
		} else {
			out = new HessianOutput(httpRes.getOutputStream());
		}

		out.setSerializerFactory(serializerFactory);

		try {
			sk.invoke(inp, out);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new ServletException(t);
		}
	}

	/**
	 * 
	 * @return the publisher
	 */
	protected HessianRemoteServicePublisher getPublisher() {
		return Activator.getDefault().getPublisher();
	}

	@SuppressWarnings("unchecked")
	private void addSpecialDeserializer(SerializerFactory serializerFactory) {
		HashMap staticDeSerMap = ReflectionUtils.getHidden(serializerFactory.getClass(), "_staticDeserializerMap"); //$NON-NLS-1$
		staticDeSerMap.put(java.io.InputStream.class, new SpecificInputStreamDeserializer());
	}

	static class SpecificInputStreamDeserializer extends AbstractDeserializer {
		public Object readObject(AbstractHessianInput in) throws IOException {

			byte[] bytes = in.readBytes();
			return new ByteArrayInputStream(bytes);
		}
	}

}
