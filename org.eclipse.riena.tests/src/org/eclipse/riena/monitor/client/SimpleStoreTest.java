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
package org.eclipse.riena.monitor.client;

import java.io.File;

import org.eclipse.core.runtime.CoreException;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.monitor.common.Collectible;

/**
 *
 */
@NonUITestCase
public class SimpleStoreTest extends RienaTestCase {

	private static final String PAYLOAD = "What goes up, must come down!";
	private static final String CATEGORY = "test";
	private static final String CLIENT_INFO = "unit-test";

	public void testPutCollectibleAndGetCollectible() throws CoreException {
		final SimpleStore store = new SimpleStore();
		final File file = new File("file");
		final Collectible<String> collectible = new Collectible<String>(CLIENT_INFO, CATEGORY, PAYLOAD);
		ReflectionUtils.invokeHidden(store, "putCollectible", collectible, file);
		final Collectible<String> outCollectible = ReflectionUtils.invokeHidden(store, "getCollectible", file);
		assertEquals(CLIENT_INFO, outCollectible.getClientInfo());
		assertEquals(CATEGORY, outCollectible.getCategory());
		assertEquals(PAYLOAD, outCollectible.getPayload());
		file.delete();
	}
}
