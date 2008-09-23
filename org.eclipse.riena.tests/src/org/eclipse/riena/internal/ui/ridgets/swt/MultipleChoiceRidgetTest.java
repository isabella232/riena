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
import org.eclipse.riena.tests.UITestHelper;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.ridgets.IChoiceRidget;
import org.eclipse.riena.ui.ridgets.IMultipleChoiceRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.ISingleChoiceRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.DefaultSwtControlRidgetMapper;
import org.eclipse.riena.ui.swt.ChoiceComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * Tests for the class {@link MultipleChoiceRidget}.
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
	 * Test method getUIControl().
	 */
	public void testGetUIControl() throws Exception {
		IMultipleChoiceRidget ridget = getRidget();
		ChoiceComposite control = getUIControl();

		assertEquals(control, ridget.getUIControl());
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

		optionProvider.setOptions(Arrays.asList("a", "b", "c", "d"));
		optionProvider.setSelectedOptions(Arrays.asList("c", "d"));
		ridget.updateFromModel();

		assertEquals(optionProvider.getOptions().size(), ridget.getObservableList().size());
		assertTrue(ridget.getObservableList().containsAll(optionProvider.getOptions()));
		assertEquals(optionProvider.getSelectedOptions().size(), ridget.getObservableSelectionList().size());
		assertTrue(optionProvider.getSelectedOptions().containsAll(ridget.getObservableSelectionList()));
		assertEquals(optionProvider.getSelectedOptions(), getSelectedControlValues(control));
	}

	/**
	 * Test that updateFromModel() fires events.
	 */
	public void testUpdateFromModelFiresEvents() throws Exception {
		IMultipleChoiceRidget ridget = getRidget();
		String element0 = optionProvider.getOptions().get(0);
		String element1 = optionProvider.getOptions().get(1);
		List<String> selection1 = Arrays.asList(new String[] { element0 });
		List<String> selection2 = Arrays.asList(new String[] { element0, element1 });

		ridget.setSelection(selection2);
		assertEquals(2, ridget.getSelection().size());

		final List<PropertyChangeEvent> events = newEventCatchingList(ridget, IChoiceRidget.PROPERTY_SELECTION);
		optionProvider.setSelectedOptions(selection1);

		assertEquals(0, events.size());

		ridget.updateFromModel();

		assertEquals(1, events.size());
		PropertyChangeEvent event0 = events.get(0);
		assertEquals(IChoiceRidget.PROPERTY_SELECTION, event0.getPropertyName());
		assertEquals(2, ((List<?>) event0.getOldValue()).size());
		assertEquals(1, ((List<?>) event0.getNewValue()).size());

		optionProvider.setSelectedOptions(selection1);
		ridget.updateFromModel();

		assertEquals(1, events.size());

		optionProvider.setSelectedOptions(null);
		ridget.updateFromModel();

		assertEquals(2, events.size());
		PropertyChangeEvent event1 = events.get(1);
		assertEquals(IChoiceRidget.PROPERTY_SELECTION, event0.getPropertyName());
		assertEquals(1, ((List<?>) event1.getOldValue()).size());
		assertEquals(0, ((List<?>) event1.getNewValue()).size());
	}

	public void testSetSelection() {
		IMultipleChoiceRidget ridget = getRidget();
		String element0 = optionProvider.getOptions().get(0);
		String element1 = optionProvider.getOptions().get(1);
		List<String> selection1 = Arrays.asList(new String[] { element0 });
		List<String> selection2 = Arrays.asList(new String[] { element0, element1 });

		ridget.setSelection(selection1);

		assertEquals(1, ridget.getSelection().size());
		assertSame(element0, ridget.getSelection().get(0));
		assertEquals(1, optionProvider.getSelectedOptions().size());
		assertSame(element0, optionProvider.getSelectedOptions().get(0));

		ridget.setSelection(selection2);

		assertEquals(2, ridget.getSelection().size());
		assertSame(element0, ridget.getSelection().get(0));
		assertSame(element1, ridget.getSelection().get(1));
		assertEquals(2, optionProvider.getSelectedOptions().size());
		assertSame(element0, optionProvider.getSelectedOptions().get(0));
		assertSame(element1, optionProvider.getSelectedOptions().get(1));

		ridget.setSelection(null);

		assertEquals(0, ridget.getSelection().size());
		assertEquals(0, optionProvider.getSelectedOptions().size());

		try {
			Object unknownObject = new Object();
			ridget.setSelection(Arrays.asList(unknownObject));
			fail();
		} catch (RuntimeException rex) {
			// expected
		}
	}

	public void testSelectionFiresEvents() {
		IMultipleChoiceRidget ridget = getRidget();
		String element0 = optionProvider.getOptions().get(0);
		String element1 = optionProvider.getOptions().get(1);
		List<String> selection2 = Arrays.asList(new String[] { element0, element1 });
		ridget.setSelection(null);

		expectPropertyChangeEvent(IChoiceRidget.PROPERTY_SELECTION, Collections.EMPTY_LIST, selection2);
		ridget.setSelection(selection2);
		verifyPropertyChangeEvents();

		expectNoPropertyChangeEvent();
		ridget.setSelection(selection2);
		verifyPropertyChangeEvents();

		expectPropertyChangeEvent(IChoiceRidget.PROPERTY_SELECTION, selection2, Collections.EMPTY_LIST);
		ridget.setSelection(null);
		verifyPropertyChangeEvents();

		expectNoPropertyChangeEvent();
		ridget.setSelection(null);
		verifyPropertyChangeEvents();
	}

	public void testSelectionUpdatesControl() {
		IMultipleChoiceRidget ridget = getRidget();
		ChoiceComposite control = getUIControl();
		String element0 = optionProvider.getOptions().get(0);
		String element1 = optionProvider.getOptions().get(1);
		List<String> selection2 = Arrays.asList(new String[] { element0, element1 });

		ridget.setSelection(selection2);

		assertEquals(2, getSelectionCount(control));
		assertTrue(getSelectedControlValues(control).containsAll(selection2));

		ridget.setSelection(null);

		assertEquals(0, getSelectionCount(control));
	}

	public void testUpdateFromModelUpdatesControl() {
		IMultipleChoiceRidget ridget = getRidget();
		ChoiceComposite control = getUIControl();
		String element0 = optionProvider.getOptions().get(0);
		String element1 = optionProvider.getOptions().get(1);
		List<String> selection2 = Arrays.asList(new String[] { element0, element1 });

		optionProvider.setSelectedOptions(selection2);
		ridget.updateFromModel();

		assertEquals(2, getSelectionCount(control));
		assertTrue(getSelectedControlValues(control).containsAll(selection2));

		optionProvider.setSelectedOptions(null);
		ridget.updateFromModel();

		assertEquals(0, getSelectionCount(control));
	}

	public void testUserSetSelection() throws Exception {
		IMultipleChoiceRidget ridget = getRidget();
		ChoiceComposite control = getUIControl();
		optionProvider.setSelectedOptions(new ArrayList<String>());
		ridget.updateFromModel();
		assertTrue("Initially no option selected in model", optionProvider.getSelectedOptions().isEmpty());
		assertEquals("Initially no option selected in ridget", 0, ridget.getSelection().size());

		final List<PropertyChangeEvent> events = newEventCatchingList(ridget, IChoiceRidget.PROPERTY_SELECTION);

		// focus and select first check box
		control.setFocus();
		UITestHelper.sendString(control.getDisplay(), " ");

		assertEquals("Option successfully selected in model", 1, optionProvider.getSelectedOptions().size());
		assertEquals("Option successfully selected in ridget", 1, ridget.getSelection().size());
		assertEquals("PropertyChangedEvent fired", 1, events.size());
		assertEquals("PropertyChangedEvent fired", events.get(0), IChoiceRidget.PROPERTY_SELECTION,
				Collections.EMPTY_LIST, Arrays.asList(optionProvider.getOptions().get(0)));
	}

	/**
	 * Test method addPropertyChangeListener(), removePropertyChangeListener().
	 */
	public void testAddRemovePropertyChangeListener() throws Exception {
		IMultipleChoiceRidget ridget = getRidget();
		TestPropertyChangeListener l = new TestPropertyChangeListener();
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
	public void testBindToModelUsingLabels() throws Exception {
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
	 * Tests that enablement from the ChoiceComposite is applied to children.
	 */
	public void testEnablementIsAppliedToChildren() {
		Shell shell = getShell();
		ChoiceComposite control = new ChoiceComposite(shell, SWT.NONE, true);

		assertTrue(control.isEnabled());

		getRidget().setEnabled(false);
		getRidget().setUIControl(control);

		assertFalse(control.isEnabled());
		Control[] children = control.getChildren();
		assertTrue(children.length > 0);
		for (Control child : children) {
			assertFalse("control should be disabled: " + child, child.isEnabled());
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

	public void testOutputCannotBeChangedFromUI() {
		IMultipleChoiceRidget ridget = getRidget();
		Button button1 = (Button) getUIControl().getChildren()[0];
		Button button2 = (Button) getUIControl().getChildren()[1];
		Button button3 = (Button) getUIControl().getChildren()[2];

		assertTrue(button1.getSelection());
		assertTrue(button2.getSelection());
		assertFalse(button3.getSelection());
		assertEquals(2, ridget.getSelection().size());
		assertTrue(ridget.getSelection().contains("Option A"));
		assertTrue(ridget.getSelection().contains("Option B"));

		ridget.setOutputOnly(true);
		button1.setFocus();
		UITestHelper.sendString(button1.getDisplay(), " ");
		button3.setFocus();
		UITestHelper.sendString(button3.getDisplay(), " ");

		assertTrue(button1.getSelection());
		assertTrue(button2.getSelection());
		assertFalse(button3.getSelection());
		assertEquals(2, ridget.getSelection().size());
		assertTrue(ridget.getSelection().contains("Option A"));
		assertTrue(ridget.getSelection().contains("Option B"));

		ridget.setOutputOnly(false);
		button1.setFocus();
		UITestHelper.sendString(button1.getDisplay(), " ");
		button3.setFocus();
		UITestHelper.sendString(button3.getDisplay(), " ");

		assertFalse(button1.getSelection());
		assertTrue(button2.getSelection());
		assertTrue(button3.getSelection());
		assertEquals(2, ridget.getSelection().size());
		assertTrue(ridget.getSelection().contains("Option B"));
		assertTrue(ridget.getSelection().contains("Option C"));
	}

	/**
	 * Tests that changing the selected state via
	 * {@link IToggleButtonRidget#setSelected(boolean) does not select the
	 * control, when the ridget is disabled.
	 */
	public void testDisabledRidgetDoesNotCheckControlOnRidgetSelection() {
		IMultipleChoiceRidget ridget = getRidget();
		ChoiceComposite control = getUIControl();

		ridget.setSelection(Arrays.asList("Option A"));

		assertEquals("Option A", optionProvider.getSelectedOptions().get(0));
		assertEquals("Option A", ridget.getSelection().get(0));
		assertEquals("Option A", getSelectedControlValues(control).get(0));

		ridget.setEnabled(false);

		assertEquals("Option A", optionProvider.getSelectedOptions().get(0));
		assertEquals("Option A", ridget.getSelection().get(0));
		if (MarkerSupport.HIDE_DISABLED_RIDGET_CONTENT) {
			assertEquals(0, getSelectionCount(control));
		} else {
			assertEquals("Option A", getSelectedControlValues(control).get(0));
		}

		ridget.setSelection(Arrays.asList("Option B"));

		assertEquals("Option B", optionProvider.getSelectedOptions().get(0));
		assertEquals("Option B", ridget.getSelection().get(0));
		if (MarkerSupport.HIDE_DISABLED_RIDGET_CONTENT) {
			assertEquals(0, getSelectionCount(control));
		} else {
			assertEquals("Option B", getSelectedControlValues(control).get(0));
		}

		ridget.setEnabled(true);

		assertEquals("Option B", optionProvider.getSelectedOptions().get(0));
		assertEquals("Option B", ridget.getSelection().get(0));
		assertEquals("Option B", getSelectedControlValues(control).get(0));
	}

	/**
	 * Tests that changing the selected state via a bound model, does not select
	 * the control, when the ridget is disabled.
	 */
	public void testDisabledRidgetDoesNotCheckControlOnModelSelection() {
		IMultipleChoiceRidget ridget = getRidget();
		ChoiceComposite control = getUIControl();

		ridget.setEnabled(false);
		optionProvider.setSelectedOptions(Arrays.asList("Option A"));
		ridget.updateFromModel();

		assertEquals("Option A", optionProvider.getSelectedOptions().get(0));
		assertEquals("Option A", ridget.getSelection().get(0));
		if (MarkerSupport.HIDE_DISABLED_RIDGET_CONTENT) {
			assertEquals(0, getSelectionCount(control));
		} else {
			assertEquals("Option A", getSelectedControlValues(control).get(0));
		}

		optionProvider.setSelectedOptions(Arrays.asList("Option B"));
		ridget.updateFromModel();

		assertEquals("Option B", optionProvider.getSelectedOptions().get(0));
		assertEquals("Option B", ridget.getSelection().get(0));
		if (MarkerSupport.HIDE_DISABLED_RIDGET_CONTENT) {
			assertEquals(0, getSelectionCount(control));
		} else {
			assertEquals("Option B", getSelectedControlValues(control).get(0));
		}

		ridget.setEnabled(true);

		assertEquals("Option B", optionProvider.getSelectedOptions().get(0));
		assertEquals("Option B", ridget.getSelection().get(0));
		assertEquals("Option B", getSelectedControlValues(control).get(0));
	}

	/**
	 * Tests that disabling the ridget does not fire 'selected' events, even
	 * though the control is modified.
	 */
	public void testDisabledDoesNotFireSelected() {
		IMultipleChoiceRidget ridget = getRidget();
		ridget.setEnabled(true);
		ridget.setSelection(Arrays.asList("Option A", "Option B"));

		ridget.addPropertyChangeListener(ISingleChoiceRidget.PROPERTY_SELECTION, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				fail("Unexpected property change event: " + evt);
			}
		});

		ridget.setEnabled(false);

		ridget.setEnabled(true);
	}

	/**
	 * Tests that the disabled state is applied to a new control when set into
	 * the ridget.
	 */
	public void testDisableAndClearOnBind() {
		IMultipleChoiceRidget ridget = getRidget();
		ChoiceComposite control = getUIControl();

		ridget.setUIControl(null);
		ridget.setEnabled(false);
		ridget.setSelection(Arrays.asList("Option B"));
		ridget.setUIControl(control);

		assertFalse(control.isEnabled());
		if (MarkerSupport.HIDE_DISABLED_RIDGET_CONTENT) {
			assertEquals(0, getSelectionCount(control));
		} else {
			assertEquals(1, getSelectionCount(control));
			assertEquals("Option B", getSelectedControlValues(control).get(0));
		}

		ridget.setEnabled(true);

		assertTrue(control.isEnabled());
		assertEquals(1, getSelectionCount(control));
		assertEquals("Option B", getSelectedControlValues(control).get(0));
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

	private void assertEquals(final String message, final PropertyChangeEvent evt, final String name,
			final Object oldValue, final Object newValue) {
		assertEquals(message + " / event name", evt.getPropertyName(), name);
		assertEquals(message + " / event old Value", evt.getOldValue(), oldValue);
		assertEquals(message + " / event new value", evt.getNewValue(), newValue);
	}

	private List<Object> getSelectedControlValues(ChoiceComposite control) {
		List<Object> result = new ArrayList<Object>();
		for (Control child : control.getChildren()) {
			Button button = (Button) child;
			if (button.getSelection()) {
				result.add(button.getData());
			}
		}
		return result;
	}

	private int getSelectionCount(ChoiceComposite control) {
		return getSelectedControlValues(control).size();
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

		public void setOptions(List<String> options) {
			this.options = options;
			optionLabels = null;
			selectedOptions = new ArrayList<String>();
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
