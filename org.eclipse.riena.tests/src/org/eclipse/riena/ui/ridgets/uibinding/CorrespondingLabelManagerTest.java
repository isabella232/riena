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
package org.eclipse.riena.ui.ridgets.uibinding;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IRidgetContainer;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 *
 */
public class CorrespondingLabelManagerTest extends TestCase {

	private CorrespondingLabelMapper labelMapper;

	private final static String FIRSTNAME_LABEL_ID = "labelfirstName";
	private final static String FIRSTNAME_TEXT_ID = "firstName";

	private final static String LASTNAME_LABEL_ID = "lbllastName";
	private final static String LASTNAME_TEXT_ID = "lastName";

	private final static String AGE_LABEL_ID = "fooage";
	private final static String AGE_TEXT_ID = "age";

	private ILabelRidget lblFirstName;

	private ITextRidget txtFirstName;

	private ILabelRidget lblLastName;

	private ITextRidget txtLastName;

	private ILabelRidget lblAge;

	private ITextRidget txtAge;

	private ILabelRidget lblDummyFinder;

	private Shell shell;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		IRidgetContainer ridgetContainer = new StubRidgetContainer();

		shell = new Shell();

		lblFirstName = (ILabelRidget) SwtRidgetFactory.createRidget(UIControlsFactory.createLabel(shell, "FirstName",
				SWT.None, FIRSTNAME_LABEL_ID));
		txtFirstName = (ITextRidget) SwtRidgetFactory.createRidget(UIControlsFactory.createText(shell, SWT.NONE,
				FIRSTNAME_TEXT_ID));

		lblLastName = (ILabelRidget) SwtRidgetFactory.createRidget(UIControlsFactory.createLabel(shell, "LastName",
				SWT.None, LASTNAME_LABEL_ID));
		txtLastName = (ITextRidget) SwtRidgetFactory.createRidget(UIControlsFactory.createText(shell, SWT.NONE,
				LASTNAME_TEXT_ID));

		lblLastName = (ILabelRidget) SwtRidgetFactory.createRidget(UIControlsFactory.createLabel(shell, "LastName",
				SWT.None, LASTNAME_LABEL_ID));
		txtLastName = (ITextRidget) SwtRidgetFactory.createRidget(UIControlsFactory.createText(shell, SWT.NONE,
				LASTNAME_TEXT_ID));

		lblAge = (ILabelRidget) SwtRidgetFactory.createRidget(UIControlsFactory.createLabel(shell, "Age", SWT.None,
				AGE_LABEL_ID));
		txtAge = (ITextRidget) SwtRidgetFactory
				.createRidget(UIControlsFactory.createText(shell, SWT.NONE, AGE_TEXT_ID));

		ridgetContainer.addRidget(FIRSTNAME_LABEL_ID, lblFirstName);
		ridgetContainer.addRidget(FIRSTNAME_TEXT_ID, txtFirstName);

		ridgetContainer.addRidget(LASTNAME_LABEL_ID, lblLastName);
		ridgetContainer.addRidget(LASTNAME_TEXT_ID, txtLastName);

		ridgetContainer.addRidget(AGE_LABEL_ID, lblAge);
		ridgetContainer.addRidget(AGE_TEXT_ID, txtAge);

		labelMapper = new CorrespondingLabelMapper(ridgetContainer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		if (null != shell && !shell.isDisposed()) {
			shell.dispose();
			shell = null;
		}
	}

	public void testConnectCorrespondingLabel() throws Exception {
		boolean foundLabel = labelMapper.connectCorrespondingLabel(txtFirstName, FIRSTNAME_TEXT_ID);
		assertTrue(foundLabel);

		assertTrue(txtFirstName.isEnabled());
		assertTrue(txtLastName.isEnabled());

		txtFirstName.setEnabled(false);
		assertFalse(lblFirstName.isEnabled());

		// ensure that all other labels are still enabled
		assertTrue(lblLastName.isEnabled());
		assertTrue(lblAge.isEnabled());
	}

	public void testConnectCorrespondingLabelLabelNotFound() throws Exception {
		boolean foundLabel = labelMapper.connectCorrespondingLabel(txtLastName, LASTNAME_TEXT_ID);
		assertFalse(foundLabel);
	}

	public void testCustomLabelFinderStrategy() throws Exception {
		lblDummyFinder = (ILabelRidget) SwtRidgetFactory.createRidget(UIControlsFactory.createLabel(shell, "Dummy",
				SWT.BORDER, LASTNAME_TEXT_ID));
		labelMapper.setLabelFinderStrategy(new StubLabelFinderStrategyProperties());

		boolean foundLabel = labelMapper.connectCorrespondingLabel(txtLastName, LASTNAME_TEXT_ID);
		assertTrue(foundLabel);

		assertTrue(lblDummyFinder.isEnabled());
		assertTrue(txtLastName.isEnabled());

		txtLastName.setEnabled(false);
		assertFalse(lblDummyFinder.isEnabled());

		// ensure that all other labels are still enabled
		assertTrue(lblFirstName.isEnabled());
		assertTrue(lblLastName.isEnabled());
		assertTrue(lblAge.isEnabled());
	}

	public void testCustomLabelPrefix() throws Exception {
		labelMapper.setCorrespondingLabelConfig(new ICorrespondingLabelConfigExtension() {
			public String getLabelPrefix() {
				return "foo";
			}
		});

		boolean foundLabel = labelMapper.connectCorrespondingLabel(txtAge, AGE_TEXT_ID);
		assertTrue(foundLabel);

		assertTrue(txtLastName.isEnabled());
		assertTrue(lblAge.isEnabled());

		txtAge.setEnabled(false);
		assertFalse(lblAge.isEnabled());

		// ensure that all other labels are still enabled
		assertTrue(lblFirstName.isEnabled());
		assertTrue(lblLastName.isEnabled());
	}

	private class StubLabelFinderStrategyProperties implements ILabelFinderStrategyExtension {

		public String getClassName() {
			return "org.eclipse.riena.ui.ridgets.uibinding.CorrespondingLabelManagerTest.DummyLabelFinderStrategy";
		}

		public ILabelFinderStrategy createFinderStrategy() {
			return new DummyLabelFinderStrategy();
		}

	}

	private class DummyLabelFinderStrategy implements ILabelFinderStrategy {
		public ILabelRidget findLabelRidget(IRidgetContainer ridgetContainer, String ridgetID) {
			System.out.println("CorrespondingLabelManagerTest.DummyLabelFinderStrategy.findLabelRidget()");
			return lblDummyFinder;
		}

	}

	private static class StubRidgetContainer implements IRidgetContainer {
		private Map<String, IRidget> ridgets;

		public StubRidgetContainer() {
			ridgets = new HashMap<String, IRidget>();
		}

		public void addRidget(String id, IRidget ridget) {
			ridgets.put(id, ridget);

		}

		public IRidget getRidget(String id) {
			return ridgets.get(id);
		}

		public Collection<? extends IRidget> getRidgets() {
			return ridgets.values();
		}

		public void configureRidgets() {
		}
	}
}
