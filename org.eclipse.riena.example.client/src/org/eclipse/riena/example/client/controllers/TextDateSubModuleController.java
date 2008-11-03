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
package org.eclipse.riena.example.client.controllers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.runtime.Assert;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.ridgets.IDateTextRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.util.beans.SingleSelectionListBean;
import org.eclipse.riena.ui.ridgets.util.beans.StringBean;
import org.eclipse.riena.ui.ridgets.util.beans.TypedBean;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

/**
 * Controller for the {@link IDateTextRidget} example.
 */
public class TextDateSubModuleController extends SubModuleController {

	/**
	 * Binds and updates the ridgets.
	 */
	public void configureRidgets() {
		System.out.println("TextDateSubModuleController.configureRidgets()"); //$NON-NLS-1$
		String[] ids = { "dd.MM.yyyy", "dd.MM.yy", "dd.MM", "MM.yyyy", "yyyy", "HH:mm:ss", "HH:mm", "dd.MM.yyyy_HH:mm" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
		DataBindingContext dbc = new DataBindingContext();
		for (String id : ids) {
			bind(dbc, id);
		}

		// date

		bindToModel("dd.MM.yyyy", new StringBean("01.10.2008")); //$NON-NLS-1$ //$NON-NLS-2$
		bindToModel("dd.MM.yy", new StringBean("01.10.08")); //$NON-NLS-1$ //$NON-NLS-2$
		bindToModel("dd.MM", new StringBean("01.10")); //$NON-NLS-1$ //$NON-NLS-2$
		bindToModel("MM.yyyy", new StringBean("10.2008")); //$NON-NLS-1$ //$NON-NLS-2$
		bindToModel("yyyy", new StringBean("2008")); //$NON-NLS-1$ //$NON-NLS-2$

		// time && date/time

		bindToModel("HH:mm:ss", new StringBean("23:55:00")); //$NON-NLS-1$ //$NON-NLS-2$
		bindToModel("HH:mm", new StringBean("23:55")); //$NON-NLS-1$ //$NON-NLS-2$
		bindToModel("dd.MM.yyyy_HH:mm", new StringBean("01.10.2008 23:55")); //$NON-NLS-1$ //$NON-NLS-2$

		IDateTextRidget justEights = (IDateTextRidget) getRidget("inJustEights"); //$NON-NLS-1$
		justEights.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);
		justEights.setOutputOnly(true);
		justEights.bindToModel(new StringBean("88.88.8888"), StringBean.PROP_VALUE); //$NON-NLS-1$
		justEights.updateFromModel();

		IDateTextRidget justSpaces = (IDateTextRidget) getRidget("inJustSpaces"); //$NON-NLS-1$
		justSpaces.setFormat(IDateTextRidget.FORMAT_DDMMYYYY);
		justSpaces.setOutputOnly(true);
		justSpaces.bindToModel(new StringBean("  .  .    "), StringBean.PROP_VALUE); //$NON-NLS-1$
		justSpaces.updateFromModel();

		final FontManager fontManager = new FontManager(PlatformUI.getWorkbench().getDisplay());
		fontManager.addRidget(justEights);
		fontManager.addRidget(justSpaces);

		IComboRidget comboFonts = (IComboRidget) getRidget("comboFonts"); //$NON-NLS-1$
		SingleSelectionListBean fonts = new SingleSelectionListBean(new Object[] { "Arial", "Courier New", "Verdana" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		fonts.setSelection("Arial"); //$NON-NLS-1$
		comboFonts.bindToModel(fonts, SingleSelectionListBean.PROPERTY_VALUES, String.class, null, fonts,
				SingleSelectionListBean.PROPERTY_SELECTION);
		comboFonts.addPropertyChangeListener(IComboRidget.PROPERTY_SELECTION, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				String name = (String) evt.getNewValue();
				fontManager.setName(name);
			}
		});
		comboFonts.updateFromModel();

		SingleSelectionListBean sizes = new SingleSelectionListBean(
				new Object[] { "6", "7", "8", "9", "10", "11", "12" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
		sizes.setSelection("9"); //$NON-NLS-1$
		IComboRidget comboSizes = (IComboRidget) getRidget("comboSizes"); //$NON-NLS-1$
		comboSizes.bindToModel(sizes, SingleSelectionListBean.PROPERTY_VALUES, String.class, null, sizes,
				SingleSelectionListBean.PROPERTY_SELECTION);
		comboSizes.addPropertyChangeListener(IComboRidget.PROPERTY_SELECTION, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				String size = (String) evt.getNewValue();
				fontManager.setSize(size);
			}
		});
		comboSizes.updateFromModel();
	}

	// helping methods
	//////////////////

	private void bind(DataBindingContext dbc, String id) {
		IRidget inputRidget = (IRidget) getRidget("in" + id); //$NON-NLS-1$
		IRidget outputRidget = (IRidget) getRidget("out" + id); //$NON-NLS-1$
		dbc.bindValue(BeansObservables.observeValue(inputRidget, ITextRidget.PROPERTY_TEXT), BeansObservables
				.observeValue(outputRidget, ITextRidget.PROPERTY_TEXT), new UpdateValueStrategy(
				UpdateValueStrategy.POLICY_UPDATE), new UpdateValueStrategy(UpdateValueStrategy.POLICY_NEVER));
	}

	private void bindToModel(String id, StringBean value) {
		IDateTextRidget ridget = (IDateTextRidget) getRidget("in" + id); //$NON-NLS-1$
		ridget.setFormat(id.replace('_', ' '));
		ridget.bindToModel(value, TypedBean.PROP_VALUE);
		ridget.updateFromModel();
	}

	// helping classes
	//////////////////

	/**
	 * TODO [ev] docs
	 */
	private static final class FontManager {

		private final Display display;

		// TODO [ev] this is leaked, to we know when the view is disposed?
		private Font font;
		private String name;
		private String size;
		private Set<IRidget> ridgets;

		FontManager(Display display) {
			Assert.isNotNull(display);
			this.display = display;
			name = ""; //$NON-NLS-1$
			size = ""; //$NON-NLS-1$
			ridgets = new HashSet<IRidget>();
		}

		void addRidget(IRidget control) {
			ridgets.add(control);
		}

		void setName(String name) {
			Assert.isNotNull(name);
			if (!this.name.equals(name)) {
				this.name = name;
				updateControls();
			}
		}

		void setSize(String size) {
			Assert.isNotNull(size);
			if (!this.size.equals(size)) {
				this.size = size;
				updateControls();
			}
		}

		private void updateControls() {
			if (name == null || size == null) {
				return;
			}
			if (font != null) {
				font.dispose();
			}
			font = new Font(display, name, Integer.valueOf(size).intValue(), SWT.NORMAL);
			for (IRidget ridget : ridgets) {
				Object control = ridget.getUIControl();
				if (control instanceof Control) {
					((Control) control).setFont(font);
				}
			}
		}
	}
}
