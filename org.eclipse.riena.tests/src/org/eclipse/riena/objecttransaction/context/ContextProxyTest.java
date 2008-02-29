/****************************************************************
 *                                                              *
 * Copyright (c) 2004-2005 compeople AG                              *
 * All rights reserved. The use of this program and the         *
 * accompanying materials are subject to license terms.         *
 *                                                              *
 ****************************************************************/
package org.eclipse.riena.objecttransaction.context;

import junit.framework.TestCase;

/**
 * TestCase for the ContextProxy
 * @author SST
 */

public class ContextProxyTest extends TestCase {

	private IContext activatedContext;
	private IContext passivatedContext;

	/**
	 * Test the management of a context an a non context carrier
	 *
	 */
	public void testContextManagementOnNotContextCarrier() {

		IContextCarrier contextCarrier = new BasicContextCarrier( null );

		IContext context1 = new ContextProxyTestContext();
		IContext context2 = new ContextProxyTestContext();
		assertFalse( context1.equals( context2 ) );

		ContextProxyTestNonContextCarrier nonContextCarrier = new ContextProxyTestNonContextCarrier();

		//test creating proxy on null and setting
		IContextProxyTestInterface nonContextCarrierProxy = ContextProxy.cover( (IContextProxyTestInterface) nonContextCarrier, contextCarrier );

		activatedContext = null;
		passivatedContext = null;
		assertNull( contextCarrier.getContext() );
		contextCarrier.setContext( context1 );
		assertNull( activatedContext );
		assertNull( passivatedContext );
		nonContextCarrierProxy.doSomething();
		assertEquals( context1, activatedContext );
		assertEquals( context1, passivatedContext );

		activatedContext = null;
		passivatedContext = null;
		contextCarrier.setContext( context2 );
		assertEquals( context2, contextCarrier.getContext() );
		assertNull( activatedContext );
		assertNull( passivatedContext );
		nonContextCarrierProxy.doSomething();
		assertEquals( context2, activatedContext );
		assertEquals( context2, passivatedContext );

		//test creating proxy on a context
		activatedContext = null;
		passivatedContext = null;

		contextCarrier.setContext( context1 );

		nonContextCarrierProxy = ContextProxy.cover( (IContextProxyTestInterface) nonContextCarrier, contextCarrier );
		assertEquals( context1, contextCarrier.getContext() );
		assertNull( activatedContext );
		assertNull( passivatedContext );
		nonContextCarrierProxy.doSomething();
		assertEquals( context1, activatedContext );
		assertEquals( context1, passivatedContext );

	}

	/**
	 * Test the management of the context with the contextProxy,
	 * when creating the proxy on an object implementing IContextCarrier
	 */
	public void testContextManagementOnContextCarrier() {

		IContext context1 = new ContextProxyTestContext();
		IContext context2 = new ContextProxyTestContext();
		assertFalse( context1.equals( context2 ) );

		ContextProxyTestContextCarrier contextCarrier = new ContextProxyTestContextCarrier();

		//test creating proxy on null and setting
		IContextProxyTestInterfaceCarrier contextCarrierProxy = ContextProxy.cover( (IContextProxyTestInterfaceCarrier) contextCarrier );

		activatedContext = null;
		passivatedContext = null;
		assertNull( contextCarrierProxy.getContext() );
		contextCarrierProxy.setContext( context1 );
		assertEquals( context1, contextCarrierProxy.getContext() );
		contextCarrierProxy.setContext( context2 );
		assertEquals( context2, contextCarrierProxy.getContext() );
		contextCarrierProxy.doSomething();
		assertEquals( context2, activatedContext );
		assertEquals( context2, passivatedContext );

		contextCarrierProxy.setContext( context1 );
		contextCarrierProxy.doSomething();
		assertEquals( context1, activatedContext );
		assertEquals( context1, passivatedContext );

	}

	private interface IContextProxyTestInterface {
		/**
		 * A dummy method
		 */
		void doSomething();
	}

	private interface IContextProxyTestInterfaceCarrier extends IContextCarrier {
		/**
		 * A dummy method
		 */
		void doSomething();
	}

	private static class ContextProxyTestNonContextCarrier implements IContextProxyTestInterface {
		/**
		 * @see de.compeople.scp.objecttransaction.context.ContextProxyTest.IContextProxyTestInterface#doSomething()
		 */
		public void doSomething() {
			return;
		}
	}

	private static class ContextProxyTestContextCarrier implements IContextProxyTestInterfaceCarrier {

		private IContext context;

		/**
		 * @see de.compeople.scp.objecttransaction.context.ContextProxyTest.IContextProxyTestInterface#doSomething()
		 */
		public void doSomething() {
			return;
		}

		/**
		 * @see de.compeople.scp.objecttransaction.context.IContextCarrier#getContext()
		 */
		public IContext getContext() {
			return context;
		}

		/**
		 * @see de.compeople.scp.objecttransaction.context.IContextCarrier#setContext(de.compeople.scp.objecttransaction.context.IContext)
		 */
		public void setContext( IContext pContext ) {
			context = pContext;
		}

	}

	private class ContextProxyTestContext implements IContext {

		/**
		 * @see de.compeople.scp.objecttransaction.context.IContext#activate()
		 */
		public void activate() {
			activatedContext = this;
		}

		/**
		 * @see de.compeople.scp.objecttransaction.context.IContext#isActivated()
		 */
		public boolean isActivated() {
			return this.equals( activatedContext );
		}

		/**
		 * @see de.compeople.scp.objecttransaction.context.IContext#passivate()
		 */
		public void passivate() {
			passivatedContext = this;

		}

	}

}
