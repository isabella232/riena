package org.eclipse.riena.navigation.ui.swt.binding;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.BindingException;
import org.eclipse.riena.internal.ui.ridgets.swt.ActionRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.ComboRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.LabelRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.ListRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.ShellRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.StatusbarNumberRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.StatusbarRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.TableRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.TextRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.ToggleButtonRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.TreeRidget;
import org.eclipse.riena.ui.ridgets.IRidget;
import org.eclipse.riena.ui.ridgets.uibinding.IControlRidgetMapper;
import org.eclipse.riena.ui.swt.Statusbar;
import org.eclipse.riena.ui.swt.StatusbarNumber;
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

	private final static int IGNOR_SWT_STYLE = -99;
	private List<Mapping> mappings;

	public DefaultSwtControlRidgetMapper() {
		mappings = new ArrayList<Mapping>();
		initDefaultMappings();
	}

	/**
	 * Sets the default mapping of UI control-classes to a ridget-classes
	 */
	private void initDefaultMappings() {
		addMapping(Text.class, TextRidget.class);
		addMapping(Label.class, LabelRidget.class);
		addMapping(Table.class, TableRidget.class);
		addMapping(Button.class, ToggleButtonRidget.class, SWT.CHECK);
		addMapping(Button.class, ToggleButtonRidget.class, SWT.TOGGLE);
		addMapping(Button.class, ActionRidget.class);
		addMapping(Shell.class, ShellRidget.class);
		addMapping(Combo.class, ComboRidget.class);
		addMapping(org.eclipse.swt.widgets.List.class, ListRidget.class);
		addMapping(Tree.class, TreeRidget.class);
		addMapping(Statusbar.class, StatusbarRidget.class);
		addMapping(StatusbarNumber.class, StatusbarNumberRidget.class);
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.uibinding.IControlRidgetMapper#addMapping(java.lang.Class,
	 *      java.lang.Class)
	 */
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

	/**
	 * @see org.eclipse.riena.ui.ridgets.uibinding.IControlRidgetMapper#getRidgetClass(java.lang.Class)
	 */
	public Class<? extends IRidget> getRidgetClass(Class<? extends Widget> controlClazz) {
		for (Mapping mapping : mappings) {
			if (mapping.isMatching(controlClazz)) {
				return mapping.getRidgetClazz();
			}
		}
		throw new BindingException("No ridget found for " + controlClazz.getSimpleName()); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.uibinding.IControlRidgetMapper#getRidgetClass(java.lang.Object)
	 */
	public Class<? extends IRidget> getRidgetClass(Widget control) {
		for (Mapping mapping : mappings) {
			if (mapping.isMatching(control)) {
				return mapping.getRidgetClazz();
			}
		}
		return getRidgetClass(control.getClass());
	}

	/**
	 * Mapping of UI control and ridget.
	 */
	public final static class Mapping {

		private Class<? extends Widget> controlClazz;
		private Class<? extends IRidget> ridgetClazz;
		private int controlStyle;

		/**
		 * Create a new mapping of UI control and ridget.
		 * 
		 * @param controlClazz
		 *            - the class of the UI control
		 * @param ridgetClazz
		 *            - the class of the ridget
		 */
		public Mapping(Class<? extends Widget> controlClazz, Class<? extends IRidget> ridgetClazz) {
			this(controlClazz, ridgetClazz, IGNOR_SWT_STYLE);
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
			this.controlClazz = controlClazz;
			this.ridgetClazz = ridgetClazz;
			this.controlStyle = controlStyle;
		}

		/**
		 * Checks if this mapping is for given UI control.
		 * 
		 * @param control
		 *            - the UI control-class
		 * @return true, if the control matches; otherwise false
		 */
		public boolean isMatching(Class<? extends Widget> controlClazz) {
			if (getControlStyle() == IGNOR_SWT_STYLE) {
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
			if (getControlStyle() != IGNOR_SWT_STYLE) {
				if ((control.getStyle() & getControlStyle()) != getControlStyle()) {
					return false;
				}
			}
			return true;

		}

		private Class<?> getControlClazz() {
			return controlClazz;
		}

		public Class<? extends IRidget> getRidgetClazz() {
			return ridgetClazz;
		}

		private int getControlStyle() {
			return controlStyle;
		}

	}

	/**
	 * @see org.eclipse.riena.ui.ridgets.uibinding.IControlRidgetMapper#addSpecialMapping(java.lang.String,
	 *      java.lang.Class)
	 */
	public void addSpecialMapping(String controlName, Class<? extends Object> ridgetClazz) {
		// TODO Auto-generated method stub

	}

}
