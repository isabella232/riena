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
package org.eclipse.riena.ui.swt.synchronizer;

import junit.framework.TestCase;

import org.easymock.EasyMock;

import org.eclipse.swt.graphics.DeviceData;
import org.eclipse.swt.widgets.Display;

import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.ui.swt.uiprocess.SwtUISynchronizer;

/**
 * {@link TestCase} for {@link SwtUISynchronizer}
 */
@UITestCase
public class SwtUISynchronizerTest extends RienaTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();

	}

	private class MockDisplay extends Display {

		private int syncExecCalls = 0;

		private int asyncExecCalls = 0;

		@Override
		public void syncExec(Runnable runnable) {
			syncExecCalls++;
		}

		@Override
		public void asyncExec(Runnable runnable) {
			asyncExecCalls++;
		}

		@Override
		protected void checkSubclass() {
		}

		@Override
		protected void create(DeviceData data) {
		}

		@Override
		protected void checkDevice() {
		}

	}

	public void testSyncExec() {
		final MockDisplay mockDisplay = new MockDisplay();
		SwtUISynchronizer synchronizer = new SwtUISynchronizer() {
			@Override
			public Display getDisplay() {
				return mockDisplay;
			}

			@Override
			protected boolean hasDisplay() {
				return true;
			}
		};
		synchronizer.syncExec(EasyMock.createNiceMock(Runnable.class));
		assertEquals(1, mockDisplay.syncExecCalls);
		assertEquals(0, mockDisplay.asyncExecCalls);

	}

	public void testASyncExec() {
		final MockDisplay mockDisplay = new MockDisplay();
		SwtUISynchronizer synchronizer = new SwtUISynchronizer() {
			@Override
			public Display getDisplay() {
				return mockDisplay;
			}

			@Override
			protected boolean hasDisplay() {
				return true;
			}
		};
		synchronizer.asyncExec(EasyMock.createNiceMock(Runnable.class));
		assertEquals(0, mockDisplay.syncExecCalls);
		assertEquals(1, mockDisplay.asyncExecCalls);
	}

}
