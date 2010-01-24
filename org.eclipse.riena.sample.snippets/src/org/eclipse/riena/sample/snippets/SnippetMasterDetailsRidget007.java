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
package org.eclipse.riena.sample.snippets;

import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.beans.common.PersonFactory;
import org.eclipse.riena.ui.core.marker.ValidationTime;
import org.eclipse.riena.ui.ridgets.AbstractMasterDetailsDelegate;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IMasterDetailsRidget;
import org.eclipse.riena.ui.ridgets.IRidgetContainer;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.ridgets.validation.NotEmpty;
import org.eclipse.riena.ui.swt.DefaultButtonManager;
import org.eclipse.riena.ui.swt.MasterDetailsComposite;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * TODO [ev] docs - experimental
 */
public final class SnippetMasterDetailsRidget007 {

	private SnippetMasterDetailsRidget007() {
		// "utility class"
	}

	/**
	 * A master details widget with a text fields for renaming a person.
	 */
	private static final class PersonMasterDetails extends MasterDetailsComposite {

		PersonMasterDetails(Composite parent, int style) {
			super(parent, style, SWT.BOTTOM);
			setMargins(5, 5);
		}

		@Override
		protected void createDetails(Composite parent) {
			GridLayoutFactory.fillDefaults().numColumns(3).margins(20, 20).spacing(10, 10).equalWidth(false).applyTo(
					parent);
			GridDataFactory hFill = GridDataFactory.fillDefaults().grab(true, false);

			UIControlsFactory.createLabel(parent, "Last Name:"); //$NON-NLS-1$
			Text txtLast = UIControlsFactory.createText(parent);
			hFill.copy().span(2, 1).applyTo(txtLast);
			addUIControl(txtLast, "txtLast"); //$NON-NLS-1$

			UIControlsFactory.createLabel(parent, "First Name:"); //$NON-NLS-1$
			Text txtFirst = UIControlsFactory.createText(parent, SWT.NONE, "txtFirst"); //$NON-NLS-1$
			hFill.applyTo(txtFirst);

			UIControlsFactory.createButton(parent, "Push", "btnPush"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * A IMasterDetailsDelegate that renames a person.
	 */
	private static final class PersonDelegate extends AbstractMasterDetailsDelegate {

		private final Person workingCopy = createWorkingCopy();

		public void configureRidgets(IRidgetContainer container) {
			ITextRidget txtLast = (ITextRidget) container.getRidget("txtLast"); //$NON-NLS-1$
			txtLast.bindToModel(workingCopy, Person.PROPERTY_LASTNAME);
			txtLast.addValidationRule(new NotEmpty(), ValidationTime.ON_UI_CONTROL_EDIT);
			txtLast.updateFromModel();

			ITextRidget txtFirst = (ITextRidget) container.getRidget("txtFirst"); //$NON-NLS-1$
			txtFirst.bindToModel(workingCopy, Person.PROPERTY_FIRSTNAME);
			txtFirst.updateFromModel();

			final IActionRidget btnPush = (IActionRidget) container.getRidget("btnPush"); //$NON-NLS-1$
			btnPush.addListener(new IActionListener() {
				public void callback() {
					System.out.println("Button pushed... " + btnPush.hashCode()); //$NON-NLS-1$
				}
			});
		}

		public Person copyBean(Object source, Object target) {
			Person from = source != null ? (Person) source : createWorkingCopy();
			Person to = target != null ? (Person) target : createWorkingCopy();
			to.setFirstname(from.getFirstname());
			to.setLastname(from.getLastname());
			return to;
		}

		public Person createWorkingCopy() {
			return new Person("", ""); //$NON-NLS-1$ //$NON-NLS-2$
		}

		public Person getWorkingCopy() {
			return workingCopy;
		}

		@Override
		public boolean isChanged(Object source, Object target) {
			Person p1 = (Person) source;
			Person p2 = (Person) target;
			boolean equal = p1.getFirstname().equals(p2.getFirstname()) && p1.getLastname().equals(p2.getLastname());
			return !equal;
		}
	}

	public static void main(String[] args) {
		Display display = Display.getDefault();
		final Shell shell = new Shell(display);
		shell.setText(SnippetMasterDetailsRidget007.class.getSimpleName());
		shell.setLayout(new FillLayout());

		DefaultButtonManager buttonMan = new DefaultButtonManager(shell);
		for (int i = 0; i < 2; i++) {
			PersonMasterDetails details = new PersonMasterDetails(shell, SWT.NONE);

			final IMasterDetailsRidget ridget = (IMasterDetailsRidget) SwtRidgetFactory.createRidget(details);
			ridget.setDelegate(new PersonDelegate());
			WritableList input = new WritableList(PersonFactory.createPersonList(), Person.class);
			String[] properties = { Person.PROPERTY_LASTNAME, Person.PROPERTY_FIRSTNAME };
			String[] headers = { "Last Name", "First Name" }; //$NON-NLS-1$ //$NON-NLS-2$
			ridget.bindToModel(input, Person.class, properties, headers);
			ridget.updateFromModel();

			buttonMan.addButton(details, details.getButtonApply());
		}

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}

		display.dispose();
	}
}
