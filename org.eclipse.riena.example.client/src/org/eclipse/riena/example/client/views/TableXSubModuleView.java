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
package org.eclipse.riena.example.client.views;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.riena.example.client.controllers.TableXSubModuleController;
import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.common.IComplexComponent;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.nebula.widgets.compositetable.AbstractNativeHeader;
import org.eclipse.swt.nebula.widgets.compositetable.CompositeTable;
import org.eclipse.swt.nebula.widgets.compositetable.ResizableGridRowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

/**
 * SWT {@link ITableRidget} sample with complex table rows.
 */
public class TableXSubModuleView extends SubModuleView<TableXSubModuleController> {

	public static final String ID = TableXSubModuleView.class.getName();

	private static class Header extends AbstractNativeHeader {
		public Header(Composite parent, int style) {
			super(parent, style);
			setWeights(new int[] { 100, 100, 100 });
			setColumnText(new String[] { "Name", "Gender", "Pet" });
		}
	}

	public static class Row extends Composite implements IComplexComponent {
		private Text txtFirst;
		private Text txtLast;

		public Row(Composite parent, int style) {
			super(parent, style);
			setLayout(new ResizableGridRowLayout());
			createColumnName();
			createColumnGender();
			createColumnPet();
		}

		public List<Object> getUIControls() {
			return Arrays.asList(new Object[] { txtFirst, txtLast });
		}

		private final void createColumnName() {
			Composite cell = createCell();
			txtFirst = UIControlsFactory.createText(cell);
			txtLast = UIControlsFactory.createText(cell);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(txtFirst);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(txtLast);
		}

		private final void createColumnGender() {
			Composite cell = createCell();
			UIControlsFactory.createButtonRadio(cell).setText("Male");
			UIControlsFactory.createButtonRadio(cell).setText("Female");
		}

		private final void createColumnPet() {
			Composite cell = createCell();
			UIControlsFactory.createButtonCheck(cell).setText("Cat");
			UIControlsFactory.createButtonCheck(cell).setText("Dog");
			UIControlsFactory.createButtonCheck(cell).setText("Fish");
		}

		private Composite createCell() {
			Composite cell = new Composite(this, SWT.NONE);
			GridLayoutFactory.swtDefaults().numColumns(1).applyTo(cell);
			cell.setBackground(cell.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
			return cell;
		}

	}

	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(1, false));

		Group group1 = createTableGroup(parent);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(group1);

		addMapping(TableXSubModuleView.Row.class, TableXSubModuleController.RowRidget.class);
	}

	// helping methods
	// ////////////////

	private Group createTableGroup(Composite parent) {
		Group group = UIControlsFactory.createGroup(parent, "&TableX:"); //$NON-NLS-1$
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(1).applyTo(group);

		CompositeTable table = new CompositeTable(group, SWT.BORDER);
		new Header(table, SWT.NONE);
		new Row(table, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(table);

		table.setRunTime(true);
		table.setNumRowsInCollection(100);

		addUIControl(table, "table"); //$NON-NLS-1$

		Composite buttonComposite = createButtonComposite(group);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(buttonComposite);

		return group;
	}

	private Composite createButtonComposite(Group group) {
		Composite buttonComposite = UIControlsFactory.createComposite(group);
		GridLayoutFactory.fillDefaults().numColumns(2).equalWidth(false).applyTo(buttonComposite);

		Button buttonAdd = UIControlsFactory.createButton(buttonComposite);
		int widthHint = UIControlsFactory.getWidthHint(buttonAdd);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.END, SWT.BEGINNING).hint(widthHint, SWT.DEFAULT)
				.applyTo(buttonAdd);
		addUIControl(buttonAdd, "buttonAdd"); //$NON-NLS-1$

		Button buttonDelete = UIControlsFactory.createButton(buttonComposite);
		addUIControl(buttonDelete, "buttonDelete"); //$NON-NLS-1$

		return buttonComposite;
	}
}
