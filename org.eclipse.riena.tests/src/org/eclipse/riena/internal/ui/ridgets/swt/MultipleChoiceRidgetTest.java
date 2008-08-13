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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.riena.navigation.ui.swt.binding.DefaultSwtControlRidgetMapper;
import org.eclipse.riena.tests.UITestHelper;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.ridgets.IChoiceRidget;
import org.eclipse.riena.ui.ridgets.IMultipleChoiceRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * Tests for the class {@link SingleChoiceRidget}.
 */
public final class MultipleChoiceRidgetTest extends MarkableRidgetTest {

	private OptionProvider optionProvider;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		optionProvider = new OptionProvider();

		getRidget().bindToModel(optionProvider, "options", optionProvider, "selectedOptions");
		getRidget().updateFromModel();
	}

	@Override
	protected Control createUIControl(Composite parent) {
		return new ChoiceComposite(parent, SWT.NONE, true);
	}

	@Override
	protected IRidget createRidget() {
		return new MultipleChoiceRidget();
	}

	@Override
	protected ChoiceComposite getUIControl() {
		return (ChoiceComposite) super.getUIControl();
	}

	@Override
	protected IMultipleChoiceRidget getRidget() {
		return (IMultipleChoiceRidget) super.getRidget();
	}

	// testing methods
	// ////////////////

	/**
	 * Test that the control is mapped to the expected ridget.
	 */
	public void testRidgetMapping() {
		DefaultSwtControlRidgetMapper mapper = new DefaultSwtControlRidgetMapper();
		assertSame(MultipleChoiceRidget.class, mapper.getRidgetClass(getUIControl()));
	}

	/**
	 * Test method getObservableSelectionList().
	 */
	public void testGetObservableSelectionList() throws Exception {
		IMultipleChoiceRidget ridget = getRidget();

		assertNotNull(ridget.getObservableSelectionList());
	}

	/**
	 * Test method updateFromModel().
	 */
	public void testUpdateFromModel() throws Exception {
		IMultipleChoiceRidget ridget = getRidget();
		ChoiceComposite control = getUIControl();

		ridget.updateFromModel();

		assertEquals(optionProvider.getOptions().size(), ridget.getObservableList().size());
		assertTrue(ridget.getObservableList().containsAll(optionProvider.getOptions()));
		assertEquals(optionProvider.getSelectedOptions().size(), ridget.getObservableSelectionList().size());
		assertTrue(optionProvider.getSelectedOptions().containsAll(ridget.getObservableSelectionList()));
		assertEquals(optionProvider.getSelectedOptions(), getSelectedControlValues(control));
	}

	/**
	 * Test method getUIControl().
	 */
	public final void testGetUIControl() throws Exception {
		IMultipleChoiceRidget ridget = getRidget();
		ChoiceComposite control = getUIControl();

		assertEquals(control, ridget.getUIControl());
	}

	/**
	 * Test method setSelection().
	 */
	public final void testSetSelection() throws Exception {
		IMultipleChoiceRidget ridget = getRidget();

		final List<PropertyChangeEvent> events = newEventCatchingList(ridget, IChoiceRidget.PROPERTY_SELECTION);

		ridget.updateFromModel(); // will fire one event per selection

		assertEquals(optionProvider.getSelectedOptions(), ridget.getSelection());
		assertEquals(2, events.size()); // 2 selections made -> 2 events
		assertEquals("[] -> [Option A]", events.get(0), IChoiceRidget.PROPERTY_SELECTION, Collections.EMPTY_LIST,
				Arrays.asList(optionProvider.getOptions().get(0)));
		assertEquals("[Option A] -> [Option A, Option B]", events.get(1), IChoiceRidget.PROPERTY_SELECTION, Arrays
				.asList(optionProvider.getOptions().get(0)), Arrays.asList(optionProvider.getOptions().get(0),
				optionProvider.getOptions().get(1)));
		events.clear();

		ridget.setSelection(Arrays.asList(optionProvider.getOptions().get(1)));
		assertEquals(ridget.getSelection(), optionProvider.getSelectedOptions());
		assertEquals(1, events.size());
		assertEquals("[Option A, Option B] -> [Option B]", events.get(0), IChoiceRidget.PROPERTY_SELECTION, Arrays
				.asList(optionProvider.getOptions().get(0), optionProvider.getOptions().get(1)), Arrays
				.asList(optionProvider.getOptions().get(1)));

	}

	public final void testUserSetSelection() throws Exception {
		IMultipleChoiceRidget ridget = getRidget();
		ChoiceComposite control = getUIControl();

		final List<PropertyChangeEvent> events = newEventCatchingList(ridget, IChoiceRidget.PROPERTY_SELECTION);

		optionProvider.setSelectedOptions(new ArrayList<String>());

		ridget.updateFromModel();
		assertTrue("Initially no option selected in model", optionProvider.getSelectedOptions().isEmpty());
		assertEquals("Initially no option selected in ridget", 0, ridget.getSelection().size());

		control.setFocus();
		UITestHelper.sendString(control.getDisplay(), "\t ");
		// jfcTestHelper.sendKeyAction(new KeyEventData(this, control,
		// KeyEvent.VK_TAB));
		// jfcTestHelper.sendKeyAction(new KeyEventData(this, control,
		// KeyEvent.VK_SPACE));

		assertEquals("Option successfully selected in model", 1, optionProvider.getSelectedOptions().size());
		assertEquals("Option successfully selected in ridget", 1, ridget.getSelection().size());
		assertEquals("PropertyChangedEvent fired", 1, events.size());
		assertEquals("PropertyChangedEvent fired", events.get(0), IChoiceRidget.PROPERTY_SELECTION,
				Collections.EMPTY_LIST, Arrays.asList(optionProvider.getOptions().get(0)));
	}

	/**
	 * Test method addPropertyChangeListener(), removePropertyChangeListener().
	 */
	public final void testAddRemovePropertyChangeListener() throws Exception {
		IMultipleChoiceRidget ridget = getRidget();

		TestPropertyChangeListener l = new TestPropertyChangeListener();
		ridget.updateFromModel();
		ridget.addPropertyChangeListener(l);

		assertEquals(optionProvider.getSelectedOptions(), ridget.getSelection());

		ridget.setSelection(Arrays.asList(optionProvider.getOptions().get(1)));

		assertEquals(ridget.getSelection(), optionProvider.getSelectedOptions());
		assertEquals(1, l.eventCounter);

		ridget.removePropertyChangeListener(l);

		ridget.setSelection(Arrays.asList(optionProvider.getOptions().get(0)));

		assertEquals(ridget.getSelection(), optionProvider.getSelectedOptions());
		assertEquals(1, l.eventCounter);
	}

	/**
	 * Test method bindToModel() using labels.
	 */
	public final void testBindToModelUsingLabels() throws Exception {
		IMultipleChoiceRidget ridget = getRidget();
		Composite control = getUIControl();
		optionProvider = new OptionProvider();

		ridget.bindToModel(optionProvider.getOptions(), optionProvider.getOptionLabels(), optionProvider,
				"selectedOptions");
		ridget.updateFromModel();

		Object[] labels = optionProvider.getOptionLabels().toArray();
		Control[] children = control.getChildren();
		assertEquals(labels.length, children.length);
		for (int i = 0; i < labels.length; i++) {
			String label = (String) labels[i];
			String caption = ((Button) children[i]).getText();
			assertEquals(label, caption);
		}
	}

	/**
	 * @throws Exception
	 *             Handled by JUnit.
	 */
	public void testDisableMandatoryMarkers() throws Exception {
		IMultipleChoiceRidget ridget = getRidget();

		final MandatoryMarker mandatoryMarker = new MandatoryMarker();

		optionProvider.setSelectedOptions(new ArrayList<String>());
		ridget.updateFromModel();
		ridget.addMarker(mandatoryMarker);

		assertFalse(mandatoryMarker.isDisabled());

		ridget.setSelection(Arrays.asList(optionProvider.getOptions().get(1)));

		assertTrue(mandatoryMarker.isDisabled());

		ridget.setSelection(Collections.EMPTY_LIST);

		assertFalse(mandatoryMarker.isDisabled());

		// marker should be enabled when selecting something that does not exist
		Exception expectedEx = null;
		try {
			ridget.setSelection(Arrays.asList(new Object()));
		} catch (RuntimeException ex) {
			expectedEx = ex;
		}

		assertNotNull("IllegalArgumentException expected", expectedEx);
		assertFalse(mandatoryMarker.isDisabled());
	}

	/**
	 * Tests that the isDisabledMandatoryMarker true when we have a selection.
	 */
	@Override
	public void testIsDisableMandatoryMarker() {
		IMultipleChoiceRidget ridget = getRidget();

		optionProvider.setSelectedOptions(null);
		ridget.updateFromModel();

		assertFalse(ridget.isDisableMandatoryMarker());

		List<String> newSelection = new ArrayList<String>();
		newSelection.add(optionProvider.getOptions().get(1));
		ridget.setSelection(newSelection);

		assertTrue(ridget.isDisableMandatoryMarker());

		ridget.setSelection(null);

		assertFalse(ridget.isDisableMandatoryMarker());
	}

	/**
	 * Tests that colors from the ChoiceComposite are applied to children.
	 */
	public void testColorsAreAppliedToChildren() {
		Shell shell = getShell();
		ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, true);
		Color colorRed = shell.getDisplay().getSystemColor(SWT.COLOR_RED);
		Color colorGreen = shell.getDisplay().getSystemColor(SWT.COLOR_GREEN);
		control.setForeground(colorGreen);
		control.setBackground(colorRed);
		getRidget().setUIControl(control);

		Control[] children = control.getChildren();
		assertTrue(children.length > 0);
		for (Control child : children) {
			assertEquals("wrong foreground on " + child, colorGreen, child.getForeground());
			assertEquals("wrong background on " + child, colorRed, child.getBackground());
		}
	}

	/**
	 * Test validation of the bindToModel(...) method.
	 */
	public void testBindToModelWithObservables() {
		IMultipleChoiceRidget ridget = getRidget();

		try {
			ridget.bindToModel(null, BeansObservables
					.observeList(Realm.getDefault(), optionProvider, "selectedOptions"));
			fail();
		} catch (RuntimeException rex) {
			// expected
		}
		try {
			ridget.bindToModel(BeansObservables.observeList(Realm.getDefault(), optionProvider, "options"), null);
			fail();
		} catch (RuntimeException rex) {
			// expected
		}
	}

	/**
	 * Test validation of the bindToModel(...) method.
	 */
	public void testBindToModelWithBeans() {
		IMultipleChoiceRidget ridget = getRidget();

		try {
			ridget.bindToModel(null, "options", optionProvider, "selectedOptions");
			fail();
		} catch (RuntimeException rex) {
			// expected
		}
		try {
			ridget.bindToModel(optionProvider, null, optionProvider, "selectedOptions");
			fail();
		} catch (RuntimeException rex) {
			// expected
		}
		try {
			ridget.bindToModel(optionProvider, "options", null, "selectedOptions");
			fail();
		} catch (RuntimeException rex) {
			// expected
		}
		try {
			ridget.bindToModel(optionProvider, "options", optionProvider, null);
			fail();
		} catch (RuntimeException rex) {
			// expected
		}
	}

	/**
	 * Test validation of the bindToModel(...) method.
	 */
	public void testBindToModelWithOptionLabelList() {
		IMultipleChoiceRidget ridget = getRidget();

		try {
			ridget.bindToModel(null, optionProvider.getOptionLabels(), optionProvider, "selectedOptions");
			fail();
		} catch (RuntimeException rex) {
			// expected
		}
		try {
			ridget.bindToModel(optionProvider.getOptions(), (List<String>) null, optionProvider, "selectedOptions");
			// ok
		} catch (RuntimeException rex) {
			fail();
		}
		try {
			ridget.bindToModel(optionProvider.getOptions(), new ArrayList<String>(), optionProvider, "selectedOptions");
			fail();
		} catch (RuntimeException rex) {
			// expected
		}
		try {
			ridget.bindToModel(optionProvider.getOptions(), optionProvider.getOptionLabels(), null, "selectedOptions");
			fail();
		} catch (RuntimeException rex) {
			// expected
		}
		try {
			ridget.bindToModel(optionProvider.getOptions(), optionProvider.getOptionLabels(), optionProvider, null);
			fail();
		} catch (RuntimeException rex) {
			// expected
		}
	}

	// helping methods
	// ////////////////

	private static List<PropertyChangeEvent> newEventCatchingList(final IRidget ridget, final String eventName) {
		final List<PropertyChangeEvent> events = new ArrayList<PropertyChangeEvent>();
		// note: somehow expectPropertyChangeEvent(..) and
		// verifyPropertyChangeEvents() did not work properly here

		ridget.addPropertyChangeListener(eventName, new PropertyChangeListener() {
			public void propertyChange(final PropertyChangeEvent evt) {
				events.add(evt);
			}
		});
		return events;
	}

	private static void assertEquals(final String message, final PropertyChangeEvent evt, final String name,
			final Object oldValue, final Object newValue) {
		assertEquals(message + " / event name", evt.getPropertyName(), name);
		assertEquals(message + " / event old Value", evt.getOldValue(), oldValue);
		assertEquals(message + " / event new value", evt.getNewValue(), newValue);
	}

	private static List<Object> getSelectedControlValues(ChoiceComposite control) {
		List<Object> result = new ArrayList<Object>();
		for (Control child : control.getChildren()) {
			Button button = (Button) child;
			if (button.getSelection()) {
				result.add(button.getData());
			}
		}
		return result;
	}

	// helping classes
	// ////////////////

	private static final class OptionProvider {

		private List<String> options = Arrays.asList("Option A", "Option B", "Option C", "Option D", "Option E",
				"Option F");
		private List<String> optionLabels = Arrays.asList("Option label A", "Option label B", "Option label C",
				"Option label D", "Option label E", "Option label F");
		private List<String> selectedOptions = Arrays.asList(options.get(0), options.get(1));

		public List<String> getOptions() {
			return options;
		}

		public List<String> getSelectedOptions() {
			return selectedOptions;
		}

		public void setSelectedOptions(List<String> selectedOptions) {
			this.selectedOptions = selectedOptions;
		}

		public List<String> getOptionLabels() {
			return optionLabels;
		}
	}

	private static final class TestPropertyChangeListener implements PropertyChangeListener {
		private int eventCounter = 0;

		public void propertyChange(PropertyChangeEvent evt) {
			eventCounter++;
		}
	};

}
