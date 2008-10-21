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
package org.eclipse.riena.ui.ridgets.swt.uibinding;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.BindingException;
import org.eclipse.core.runtime.Assert;
import org.eclipse.riena.internal.ui.ridgets.swt.ActionRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.ComboRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.DateTextRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.DecimalTextRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.EmbeddedTitleBarRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.LabelRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.ListRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.ModuleTitleBarRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.MultipleChoiceRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.NumericTextRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.ShellRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.SingleChoiceRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.StatuslineNumberRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.StatuslineRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.StatuslineUIProcessRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.TableRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.TextRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.ToggleButtonRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.TreeRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.TreeTableRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.uibinding.IControlRidgetMapper;
import org.eclipse.riena.ui.ridgets.uibinding.IMappingCondition;
import org.eclipse.riena.ui.swt.ChoiceComposite;
import org.eclipse.riena.ui.swt.EmbeddedTitleBar;
import org.eclipse.riena.ui.swt.ModuleTitleBar;
import org.eclipse.riena.ui.swt.Statusline;
import org.eclipse.riena.ui.swt.StatuslineNumber;
import org.eclipse.riena.ui.swt.StatuslineUIProcess;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;

/**
 * Default implementation of {@link IControlRidgetMapper} for SWT.
 */
public class DefaultSwtControlRidgetMapper implements IControlRidgetMapper<Widget> {

	private final static int IGNORE_SWT_STYLE = -99;

	private List<Mapping> mappings;

	public DefaultSwtControlRidgetMapper() {
		mappings = new ArrayList<Mapping>();
		initDefaultMappings();
	}

	/**
	 * Sets the default mapping of UI control-classes to a ridget-classes
	 */
	private void initDefaultMappings() {
		addMapping(Text.class, NumericTextRidget.class, new TypedTextWidgetCondition(UIControlsFactory.TYPE_NUMERIC));
		addMapping(Text.class, DecimalTextRidget.class, new TypedTextWidgetCondition(UIControlsFactory.TYPE_DECIMAL));
		addMapping(Text.class, DateTextRidget.class, new TypedTextWidgetCondition(UIControlsFactory.TYPE_DATE));
		addMapping(Text.class, TextRidget.class);
		addMapping(Label.class, LabelRidget.class);
		addMapping(Table.class, TableRidget.class);
		addMapping(Button.class, ToggleButtonRidget.class, SWT.CHECK);
		addMapping(Button.class, ToggleButtonRidget.class, SWT.TOGGLE);
		addMapping(Button.class, ToggleButtonRidget.class, SWT.RADIO);
		addMapping(Button.class, ActionRidget.class);
		addMapping(ChoiceComposite.class, SingleChoiceRidget.class, new SingleChoiceCondition());
		addMapping(ChoiceComposite.class, MultipleChoiceRidget.class, new MultipleChoiceCondition());
		addMapping(Combo.class, ComboRidget.class);
		addMapping(org.eclipse.swt.widgets.List.class, ListRidget.class);
		addMapping(Tree.class, TreeRidget.class, new TreeWithoutColumnsCondition());
		addMapping(Tree.class, TreeTableRidget.class, new TreeWithColumnsCondition());
		addMapping(Shell.class, ShellRidget.class);
		addMapping(Statusline.class, StatuslineRidget.class);
		addMapping(StatuslineNumber.class, StatuslineNumberRidget.class);
		addMapping(StatuslineUIProcess.class, StatuslineUIProcessRidget.class);
		addMapping(EmbeddedTitleBar.class, EmbeddedTitleBarRidget.class);
		addMapping(ModuleTitleBar.class, ModuleTitleBarRidget.class);
	}

	public void addMapping(Class<? extends Widget> controlClazz, Class<? extends IRidget> ridgetClazz) {
		Mapping mapping = new Mapping(controlClazz, ridgetClazz);
		mappings.add(mapping);
	}

	/**
	 * Adds a mapping of a UI control-class to a ridget-class
	 * 
	 * @param controlClazz
	 *            - the class of the UI control (<code>Widget</code>)
	 * @param ridgetClazz
	 *            - the class of the ridget
	 * @param swtStyle
	 *            - SWT style of the UI control (<code>Widget</code>)
	 */
	public void addMapping(Class<? extends Widget> controlClazz, Class<? extends IRidget> ridgetClazz, int swtStyle) {
		Mapping mapping = new Mapping(controlClazz, ridgetClazz, swtStyle);
		mappings.add(mapping);
	}

	public void addMapping(Class<? extends Widget> controlClazz, Class<? extends IRidget> ridgetClazz,
			IMappingCondition condition) {
		Mapping mapping = new Mapping(controlClazz, ridgetClazz, condition);
		mappings.add(mapping);
	}

	public Class<? extends IRidget> getRidgetClass(Class<? extends Widget> controlClazz) {
		for (Mapping mapping : mappings) {
			if (mapping.isMatching(controlClazz)) {
				return mapping.getRidgetClazz();
			}
		}
		throw new BindingException("No ridget found for " + controlClazz.getSimpleName()); //$NON-NLS-1$
	}

	public Class<? extends IRidget> getRidgetClass(Widget control) {
		for (Mapping mapping : mappings) {
			if (mapping.isMatching(control)) {
				return mapping.getRidgetClazz();
			}
		}
		return getRidgetClass(control.getClass());
	}

	public void addSpecialMapping(String controlName, Class<? extends Object> ridgetClazz) {
		// TODO Auto-generated method stub
	}

	// helping classes
	// ////////////////

	/**
	 * Mapping of UI control and ridget.
	 */
	public static final class Mapping {

		private Class<? extends Widget> controlClazz;
		private Class<? extends IRidget> ridgetClazz;
		private int controlStyle;
		private IMappingCondition condition;

		/**
		 * Create a new mapping of UI control and ridget.
		 * 
		 * @param controlClazz
		 *            - the class of the UI control
		 * @param ridgetClazz
		 *            - the class of the ridget
		 */
		public Mapping(Class<? extends Widget> controlClazz, Class<? extends IRidget> ridgetClazz) {
			this(controlClazz, ridgetClazz, IGNORE_SWT_STYLE, null);
		}

		/**
		 * Create a new mapping of UI control and ridget.
		 * 
		 * @param controlClazz
		 *            - the class of the UI control
		 * @param ridgetClazz
		 *            - the class of the ridget
		 * @param controlStyle
		 *            - the SWT style of the UI control
		 */
		public Mapping(Class<? extends Widget> controlClazz, Class<? extends IRidget> ridgetClazz, int controlStyle) {
			this(controlClazz, ridgetClazz, controlStyle, null);
		}

		/**
		 * Create a new mapping of UI control and ridget.
		 * 
		 * @param controlClazz
		 *            - the class of the UI control
		 * @param ridgetClazz
		 *            - the class of the ridget
		 * @param condition
		 *            - a non-null {@link IMappingCondition} instance
		 */
		public Mapping(Class<? extends Widget> controlClazz, Class<? extends IRidget> ridgetClazz,
				IMappingCondition condition) {
			this(controlClazz, ridgetClazz, IGNORE_SWT_STYLE, condition);
			Assert.isNotNull(condition);
		}

		private Mapping(Class<? extends Widget> controlClazz, Class<? extends IRidget> ridgetClazz, int controlStyle,
				IMappingCondition condition) {
			this.controlClazz = controlClazz;
			this.ridgetClazz = ridgetClazz;
			this.controlStyle = controlStyle;
			this.condition = condition;
		}

		/**
		 * Checks if this mapping is for given UI control.
		 * 
		 * @param control
		 *            - the UI control-class
		 * @return true, if the control matches; otherwise false
		 */
		public boolean isMatching(Class<? extends Widget> controlClazz) {
			if (getControlStyle() == IGNORE_SWT_STYLE && condition == null) {
				return (controlClazz == getControlClazz());
			} else {
				return false;
			}
		}

		/**
		 * Checks if this mapping is for given UI control.
		 * 
		 * @param control
		 *            - the UI control
		 * @return true, if the control matches; otherwise false
		 */
		public boolean isMatching(Widget control) {
			if (control.getClass() != getControlClazz()) {
				return false;
			}
			if (condition != null && !condition.isMatch(control)) {
				return false;
			}
			if (getControlStyle() != IGNORE_SWT_STYLE) {
				if ((control.getStyle() & getControlStyle()) != getControlStyle()) {
					return false;
				}
			}
			return true;

		}

		public Class<? extends IRidget> getRidgetClazz() {
			return ridgetClazz;
		}

		// helping methods
		// ////////////////

		private Class<?> getControlClazz() {
			return controlClazz;
		}

		private int getControlStyle() {
			return controlStyle;
		}

		public IMappingCondition getCondition() {
			return condition;
		}

	}

}
