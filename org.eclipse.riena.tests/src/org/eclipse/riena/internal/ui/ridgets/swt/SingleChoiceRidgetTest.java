/*******************************************************************************
 * Copyright (c) 2007, 2008 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;

import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.ISingleChoiceRidget;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Tests for the class {@link SingleChoiceRidget}.
 */
public class SingleChoiceRidgetTest extends MarkableRidgetTest {

	private OptionProvider optionProvider;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		optionProvider = new OptionProvider();

		getRidget().bindToModel(optionProvider, "options", optionProvider, "selectedOption");
	}

	@Override
	protected Control createUIControl(Composite parent) {
		return new ChoiceComposite(parent, SWT.NONE, false);
	}

	@Override
	protected IRidget createRidget() {
		return new SingleChoiceRidget();
	}

	@Override
	protected ChoiceComposite getUIControl() {
		return (ChoiceComposite) super.getUIControl();
	}

	@Override
	protected ISingleChoiceRidget getRidget() {
		return (ISingleChoiceRidget) super.getRidget();
	}

	// testing methods
	// ////////////////

	/**
	 * Test method getObservableList().
	 */
	public void testGetObservableList() {
		ISingleChoiceRidget ridget = getRidget();

		assertNotNull(ridget.getObservableList());
	}

	/**
	 * Test method getSelectionObservable().
	 */
	public void testGetSelectionObservable() {
		ISingleChoiceRidget ridget = getRidget();

		assertNotNull(ridget.getSelectionObservable());

		ridget.updateFromModel();

		assertEquals(optionProvider.getSelectedOption(), ridget.getSelectionObservable().getValue());
	}

	/**
	 * Test method updateFromModel().
	 */
	public void testUpdateFromModel() {
		ISingleChoiceRidget ridget = getRidget();
		Control control = getUIControl();

		ridget.updateFromModel();

		assertEquals(optionProvider.getOptions().size(), ridget.getObservableList().size());
		assertTrue(ridget.getObservableList().containsAll(optionProvider.getOptions()));
		assertEquals(optionProvider.getSelectedOption(), ridget.getSelection());
		assertEquals(optionProvider.getSelectedOption(), getSelectedControlValue(control));
	}

	/**
	 * Test method getUIControl().
	 */
	public final void testGetUIControl() {
		ISingleChoiceRidget ridget = getRidget();
		Control control = getUIControl();

		assertEquals(control, ridget.getUIControl());
	}

	/**
	 * Test method setSelection().
	 */
	public final void testSetSelection() {
		ISingleChoiceRidget ridget = getRidget();

		ridget.updateFromModel();

		assertEquals(optionProvider.getSelectedOption(), ridget.getSelection());

		ridget.setSelection(optionProvider.getOptions().get(1));
		assertEquals(ridget.getSelection(), optionProvider.getSelectedOption());
	}

	/**
	 * Test the methods addPropertyChangeListener() and
	 * removePropertyChangeListener().
	 */
	public final void testAddRemovePropertyChangeListener() {
		ISingleChoiceRidget ridget = getRidget();

		// TODO [ev] use easymock here
		TestPropertyChangeListener listener = new TestPropertyChangeListener();
		ridget.updateFromModel();
		ridget.addPropertyChangeListener(listener);

		assertEquals(optionProvider.getSelectedOption(), ridget.getSelection());

		ridget.setSelection(optionProvider.getOptions().get(1));

		assertEquals(ridget.getSelection(), optionProvider.getSelectedOption());
		assertEquals(1, listener.eventCounter);

		ridget.removePropertyChangeListener(listener);

		ridget.setSelection(optionProvider.getOptions().get(0));

		assertEquals(ridget.getSelection(), optionProvider.getSelectedOption());
		assertEquals(1, listener.eventCounter);
	}

	/**
	 * Test method bindToModel() using labels.
	 */
	public final void testBindToModelUsingLabels() {
		ISingleChoiceRidget ridget = getRidget();
		Composite control = getUIControl();
		optionProvider = new OptionProvider();

		ridget.bindToModel(optionProvider.getOptions(), optionProvider.getOptionLabels(), optionProvider,
				"selectedOption");

		Object[] labels = optionProvider.getOptionLabels().toArray();
		Control[] children = control.getChildren();
		assertEquals(labels.length, children.length);
		for (int i = 0; i < labels.length; i++) {
			String label = (String) labels[i];
			String caption = ((Button) children[i]).getText();
			assertEquals(label, caption);
		}
	}

	public void testForwardMarkersToOptionRidgets() {
		fail("TODO"); // TODO [ev] what to we test for here?
		// SingleChoiceRidget ridget = (SingleChoiceRidget) getRidget();
		//
		// ErrorMarker errorMarker = new ErrorMarker();
		// MandatoryMarker mandatoryMarker = new MandatoryMarker();
		// OutputMarker outputMarker = new OutputMarker();
		//
		// ridget.addMarker(errorMarker);
		// ridget.addMarker(mandatoryMarker);
		// ridget.addMarker(outputMarker);
		//
		// assertEquals(3, ridget.getMarkers().size());
		// for (IToggleButtonRidget optionRidget : ridget.getOptionRidgets()) {
		// assertEquals(3, optionRidget.getMarkers().size());
		// }
		//
		// ridget.removeMarker(mandatoryMarker);
		//
		// assertEquals(2, ridget.getMarkers().size());
		// for (IToggleButtonRidget optionRidget : ridget.getOptionRidgets()) {
		// assertEquals(2, optionRidget.getMarkers().size());
		// }
		//
		// ridget.removeAllMarkers();
		//
		// assertEquals(0, ridget.getMarkers().size());
		// for (IToggleButtonRidget optionRidget : ridget.getOptionRidgets()) {
		// assertEquals(0, optionRidget.getMarkers().size());
		// }
	}

	public void testDisableMandatoryMarkers() {
		ISingleChoiceRidget ridget = getRidget();

		final MandatoryMarker mandatoryMarker = new MandatoryMarker();

		optionProvider.setSelectedOption(null);
		ridget.updateFromModel();
		ridget.addMarker(mandatoryMarker);

		assertFalse(mandatoryMarker.isDisabled());

		ridget.setSelection(optionProvider.getOptions().get(1));

		assertTrue(mandatoryMarker.isDisabled());

		ridget.setSelection(null);

		assertFalse(mandatoryMarker.isDisabled());
	}

	public void testIsDisableMandatoryMarker() {
		ISingleChoiceRidget ridget = getRidget();

		optionProvider.setSelectedOption(null);
		ridget.updateFromModel();

		assertFalse(ridget.isDisableMandatoryMarker());

		ridget.setSelection(optionProvider.getOptions().get(1));

		assertTrue(ridget.isDisableMandatoryMarker());

		ridget.setSelection(null);

		assertFalse(ridget.isDisableMandatoryMarker());
	}

	// helping methods
	// ////////////////

	private String getSelectedControlValue(Control control) {
		Object result = null;
		ChoiceComposite composite = (ChoiceComposite) control;
		Control[] children = composite.getChildren();
		for (int i = 0; result == null && i < children.length; i++) {
			Button button = (Button) children[i];
			if (button.getSelection()) {
				result = button.getData();
			}
		}
		return result != null ? result.toString() : null;
	}

	// helping classes
	// ////////////////

	private static class OptionProvider {

		private List<String> options = Arrays.asList("Option A", "Option B", "Option C", "Option D", "Option E",
				"Option F");
		private List<String> optionLabels = Arrays.asList("Option label A", "Option label B", "Option label C",
				"Option label D", "Option label E", "Option label F");
		private String selectedOption = options.get(0);

		public List<String> getOptions() {
			return options;
		}

		public String getSelectedOption() {
			return selectedOption;
		}

		public void setSelectedOption(String selectedOption) {
			this.selectedOption = selectedOption;
		}

		public List<String> getOptionLabels() {
			return optionLabels;
		}
	}

	private static class TestPropertyChangeListener implements PropertyChangeListener {
		private int eventCounter = 0;

		public void propertyChange(PropertyChangeEvent evt) {
			eventCounter++;
		}
	};

	// TODO [ev] ex
	// private static class TestSingleChoiceSwingRidget extends
	// SingleChoiceSwingRidget
	// {
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see de.compeople.scp.ui.ridgets.swing.MultipleChoiceSwingRidget#
	// createOptionRidget()
	// */
	// @Override
	// protected IToggleButtonRidget createOptionRidget() {
	// IToggleButtonRidget optionRidget = super.createOptionRidget();
	// optionRidgets.add(optionRidget);
	// return optionRidget;
	// }
	//
	// }

}
