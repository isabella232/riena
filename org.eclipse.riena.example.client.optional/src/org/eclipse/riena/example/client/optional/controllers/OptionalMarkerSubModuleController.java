/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.example.client.optional.controllers;

import java.util.Collection;

import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.riena.beans.common.Person;
import org.eclipse.riena.beans.common.PersonFactory;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.model.SimpleNavigationNodeAdapter;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.core.marker.AttentionMarker;
import org.eclipse.riena.ui.ridgets.AbstractCompositeRidget;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.ICompositeTableRidget;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.IRowRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;
import org.eclipse.riena.ui.ridgets.validation.ValidationRuleStatus;

/**
 * Controller for the {@link TextSubModuleView} example.
 */
public class OptionalMarkerSubModuleController extends SubModuleController {

	@Override
	public void configureRidgets() {
		final ICompositeTableRidget compTable = getRidget(ICompositeTableRidget.class, "compTable"); //$NON-NLS-1$
		final WritableList input = new WritableList(PersonFactory.createPersonList(), Person.class);
		compTable.bindToModel(input, Person.class, RowRidget.class);
		compTable.updateFromModel();

		final IRidget[] markables = new IRidget[] { compTable };

		final IToggleButtonRidget checkMandatory = getRidget(IToggleButtonRidget.class, "checkMandatory"); //$NON-NLS-1$
		final IToggleButtonRidget checkError = getRidget(IToggleButtonRidget.class, "checkError"); //$NON-NLS-1$
		final IToggleButtonRidget checkDisabled = getRidget(IToggleButtonRidget.class, "checkDisabled"); //$NON-NLS-1$
		final IToggleButtonRidget checkOutput = getRidget(IToggleButtonRidget.class, "checkOutput"); //$NON-NLS-1$
		final IToggleButtonRidget checkHidden = getRidget(IToggleButtonRidget.class, "checkHidden"); //$NON-NLS-1$
		final IToggleButtonRidget checkHiddenParent = getRidget(IToggleButtonRidget.class, "checkHiddenParent"); //$NON-NLS-1$

		checkMandatory.addListener(new IActionListener() {
			public void callback() {
				final boolean isMandatory = checkMandatory.isSelected();
				for (final IRidget ridget : markables) {
					if (ridget instanceof IMarkableRidget) {
						((IMarkableRidget) ridget).setMandatory(isMandatory);
					} else {
						final String name = ridget.getClass().getSimpleName();
						System.out.println("No mandatory marker support on " + name); //$NON-NLS-1$
					}
				}
				if (isMandatory) {
					compTable.clearSelection();
				}
			}
		});

		checkError.addListener(new IActionListener() {
			// private final IValidator alwaysWrong = new
			// AlwaysWrongValidator();

			public void callback() {
				final boolean isError = checkError.isSelected();
				for (final IRidget ridget : markables) {
					if (ridget instanceof IMarkableRidget) {
						((IMarkableRidget) ridget).setErrorMarked(isError);
					}
				}
				// using this "always wrong" validator for purposes of this
				// demo. It prevents the error marker being removed from the
				// text field on the next revalidation (i.e. when the user
				// types).
				if (isError) {
					// textName.addValidationRule(alwaysWrong,
					// ValidationTime.ON_UI_CONTROL_EDIT);
				} else {
					// textName.removeValidationRule(alwaysWrong);
				}
			}
		});

		checkDisabled.addListener(new DisabledActionListener(markables, checkDisabled));
		checkOutput.addListener(new OutputActionListener(markables, checkOutput));
		checkHidden.addListener(new HiddenActionListener(checkHidden, markables));
		checkHiddenParent.addListener(new HiddenParentActionListener(checkHiddenParent, markables));

		getNavigationNode().addSimpleListener(new SimpleNavigationNodeAdapter() {
			@Override
			public void afterDeactivated(final INavigationNode<?> node) {
				super.afterDeactivated(node);
				final Collection<AttentionMarker> markers = node.getMarkersOfType(AttentionMarker.class);
				for (final AttentionMarker marker : markers) {
					node.removeMarker(marker);
				}
			}
		});
	}

	// helping classes
	// ////////////////

	private static final class DisabledActionListener implements IActionListener {

		private final IRidget[] markables;
		private final IToggleButtonRidget checkDisabled;

		private DisabledActionListener(final IRidget[] markables, final IToggleButtonRidget checkDisabled) {
			this.markables = markables;
			this.checkDisabled = checkDisabled;
		}

		public void callback() {
			final boolean isEnabled = !checkDisabled.isSelected();
			for (final IRidget ridget : markables) {
				ridget.setEnabled(isEnabled);
			}
		}

	}

	private static final class OutputActionListener implements IActionListener {

		private final IRidget[] markables;
		private final IToggleButtonRidget checkOutput;

		private OutputActionListener(final IRidget[] markables, final IToggleButtonRidget checkOutput) {
			this.markables = markables;
			this.checkOutput = checkOutput;
		}

		public void callback() {
			final boolean isOutput = checkOutput.isSelected();
			for (final IRidget ridget : markables) {
				if (ridget instanceof IMarkableRidget) {
					((IMarkableRidget) ridget).setOutputOnly(isOutput);
				} else {
					final String name = ridget.getClass().getSimpleName();
					System.out.println("No output marker support on " + name); //$NON-NLS-1$
				}
			}
		}

	}

	private static final class HiddenActionListener implements IActionListener {

		private final IToggleButtonRidget checkHidden;
		private final IRidget[] markables;

		private HiddenActionListener(final IToggleButtonRidget checkHidden, final IRidget[] markables) {
			this.checkHidden = checkHidden;
			this.markables = markables;
		}

		public void callback() {
			final boolean isVisible = !checkHidden.isSelected();
			for (final IRidget ridget : markables) {
				ridget.setVisible(isVisible);
			}
		}

	}

	private static final class HiddenParentActionListener implements IActionListener {

		private final IToggleButtonRidget checkHiddenParent;
		private final IRidget[] markables;

		private HiddenParentActionListener(final IToggleButtonRidget checkHiddenParent, final IRidget[] markables) {
			this.checkHiddenParent = checkHiddenParent;
			this.markables = markables;
		}

		public void callback() {
			final Composite parent = ((Control) markables[0].getUIControl()).getParent();
			final boolean isVisible = !checkHiddenParent.isSelected();
			parent.setVisible(isVisible);
		}

	}

	/**
	 * Validator that always returns an error status.
	 */
	private static final class AlwaysWrongValidator implements IValidator {
		public IStatus validate(final Object value) {
			return ValidationRuleStatus.error(false, ""); //$NON-NLS-1$
		}
	}

	/**
	 * A row ridget with two text ridgets for use with
	 * {@link ICompositeTableRidget}.
	 */
	public static final class RowRidget extends AbstractCompositeRidget implements IRowRidget {
		private Person rowData;

		public void setData(final Object rowData) {
			this.rowData = (Person) rowData;
		}

		@Override
		public void configureRidgets() {
			final ITextRidget txtLast = getRidget("txtLast"); //$NON-NLS-1$
			txtLast.bindToModel(rowData, Person.PROPERTY_FIRSTNAME);
			txtLast.updateFromModel();
			final ITextRidget txtFirst = getRidget("txtFirst"); //$NON-NLS-1$
			txtFirst.bindToModel(rowData, Person.PROPERTY_FIRSTNAME);
			txtFirst.updateFromModel();
		}
	}

}
