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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.riena.example.client.views.SystemPropertiesSubModuleView;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ISelectableRidget;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.ITextFieldRidget;
import org.eclipse.riena.ui.ridgets.IToggleButtonRidget;
import org.eclipse.riena.ui.ridgets.util.beans.AbstractBean;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.PlatformUI;

/**
 * Controller for the {@link SystemPropertiesSubModuleView} example.
 */
public class SystemPropertiesSubModuleController extends SubModuleController {

	private ITableRidget tableProperties;
	private ITextFieldRidget textKey;
	private ITextFieldRidget textValue;
	private IActionRidget buttonAdd;
	private IToggleButtonRidget toggleDoubleClick;
	private IActionRidget buttonSave;

	/** Manages a collection of PropertyBeans */
	private final List<KeyValueBean> properties;
	/** Bean for holding the value being edited. */
	private final KeyValueBean valueBean;
	/** IActionListener for double click on the table */
	private final IActionListener doubleClickListener;

	public SystemPropertiesSubModuleController(ISubModuleNode navigationNode) {
		super(navigationNode);
		properties = new ArrayList<KeyValueBean>();
		valueBean = new KeyValueBean();
		doubleClickListener = new DoubleClickListener();
	}

	public ITableRidget getTableProperties() {
		return tableProperties;
	}

	public void setTableProperties(ITableRidget tableProperties) {
		this.tableProperties = tableProperties;
	}

	public ITextFieldRidget getTextKey() {
		return textKey;
	}

	public void setTextKey(ITextFieldRidget textKey) {
		this.textKey = textKey;
	}

	public ITextFieldRidget getTextValue() {
		return textValue;
	}

	public void setTextValue(ITextFieldRidget textValue) {
		this.textValue = textValue;
	}

	public IActionRidget getButtonAdd() {
		return buttonAdd;
	}

	public void setButtonAdd(IActionRidget buttonAdd) {
		this.buttonAdd = buttonAdd;
	}

	public IToggleButtonRidget getToggleDoubleClick() {
		return toggleDoubleClick;
	}

	public void setToggleDoubleClick(IToggleButtonRidget toggleDoubleClick) {
		this.toggleDoubleClick = toggleDoubleClick;
	}

	public IActionRidget getButtonSave() {
		return buttonSave;
	}

	public void setButtonSave(IActionRidget buttonSave) {
		this.buttonSave = buttonSave;
	}

	public void afterBind() {
		super.afterBind();
		initRidgets();
	}

	/**
	 * Binds and updates the ridgets.
	 */
	private void initRidgets() {
		tableProperties.setSelectionType(ISelectableRidget.SelectionType.SINGLE);
		tableProperties.setComparator(0, new StringComparator());
		tableProperties.setComparator(1, new StringComparator());
		tableProperties.setMoveableColumns(true);

		Set<Object> keys = System.getProperties().keySet();
		for (Object key : keys) {
			KeyValueBean bean = new KeyValueBean();
			bean.setKey((String) key);
			bean.setValue(System.getProperty((String) key));
			properties.add(bean);
		}

		tableProperties.bindToModel(new WritableList(properties, KeyValueBean.class), KeyValueBean.class, new String[] {
				"key", "value" }, new String[] { "Key", "Value" }); //$NON-NLS-1$ //$NON-NLS-2$
		tableProperties.updateFromModel();

		textKey.bindToModel(valueBean, "key"); //$NON-NLS-1$
		textKey.updateFromModel();
		textValue.bindToModel(valueBean, "value"); //$NON-NLS-1$
		textValue.updateFromModel();

		tableProperties.addPropertyChangeListener(ITableRidget.PROPERTY_SINGLE_SELECTION, new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				List<Object> selection = tableProperties.getSelection();
				if (!selection.isEmpty()) {
					valueBean.setBean((KeyValueBean) selection.get(0));
					textKey.updateFromModel();
					textValue.updateFromModel();
				}
			}
		});

		buttonAdd.setText("&Add");
		buttonAdd.addListener(new IActionListener() {
			private int count = 0;

			public void callback() {
				KeyValueBean bean = new KeyValueBean();
				bean.setKey("key" + ++count); //$NON-NLS-1$
				bean.setValue("newValue"); //$NON-NLS-1$
				properties.add(bean);
				tableProperties.updateFromModel();
				tableProperties.setSelection(bean);
				((Table) tableProperties.getUIControl()).showSelection();
			}
		});

		toggleDoubleClick.setText("Handle &Double Click");
		toggleDoubleClick.addListener(new IActionListener() {
			public void callback() {
				if (toggleDoubleClick.isSelected()) {
					tableProperties.addDoubleClickListener(doubleClickListener);
				} else {
					tableProperties.removeDoubleClickListener(doubleClickListener);
				}
			}
		});

		buttonSave.setText("&Save");
		buttonSave.addListener(new IActionListener() {
			public void callback() {
				valueBean.update();
				tableProperties.updateFromModel();
			}
		});

		if (!properties.isEmpty()) {
			tableProperties.setSelection(0);
		}
	}

	// helping classes
	// ////////////////

	/**
	 * Bean for holding a pair of Strings.
	 */
	public static final class KeyValueBean extends AbstractBean {

		private KeyValueBean bean;
		private String tempKey;
		private String tempValue;

		public String getKey() {
			return tempKey;
		}

		public void setKey(String key) {
			this.tempKey = key;
		}

		public String getValue() {
			return tempValue;
		}

		public void setValue(String value) {
			this.tempValue = value;
		}

		public void setBean(KeyValueBean bean) {
			this.bean = bean;
			setKey(bean.getKey());
			setValue(bean.getValue());
		}

		public void update() {
			bean.setKey(tempKey);
			bean.setValue(tempValue);
		}
	}

	/**
	 * Compares two strings.
	 */
	private static final class StringComparator implements Comparator<Object> {
		public int compare(Object o1, Object o2) {
			String s1 = (String) o1;
			String s2 = (String) o2;
			return s1.compareTo(s2);
		}
	}

	/**
	 * Show a {@link MessageDialog} on double click.
	 */
	private final class DoubleClickListener implements IActionListener {

		public void callback() {
			Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
			String title = "Information";
			String message = "The key ''{0}'' is selected and has the value ''{1}''";
			message = NLS.bind(message, valueBean.getKey(), valueBean.getValue());
			MessageDialog.openInformation(shell, title, message);
		}

	}
}
