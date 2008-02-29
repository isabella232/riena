package org.eclipse.riena.objecttransaction.simple;

import org.eclipse.riena.objecttransaction.state.State;
import org.eclipse.riena.objecttransaction.state.StateMachine;
import org.eclipse.riena.tests.RienaTestCase;

/**
 * TestCase that tests (a little) of the StateMachine
 * 
 * @author Christian Campo
 */
public class StateMachineTest extends RienaTestCase {

	/**
	 * 
	 */
	public void testSimpleStateMaschine() {
		assertTrue("expect modified state", StateMachine.mergeStates(State.CLEAN, State.MODIFIED) == State.MODIFIED);
	}

}