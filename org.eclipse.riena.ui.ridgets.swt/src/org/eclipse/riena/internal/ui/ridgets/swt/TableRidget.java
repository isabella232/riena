package org.eclipse.riena.internal.ui.ridgets.swt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateListStrategy;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.internal.databinding.viewers.SelectionProviderMultipleSelectionObservableList;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.ITableRidget;
import org.eclipse.riena.ui.ridgets.databinding.UnboundPropertyWritableList;
import org.eclipse.riena.ui.ridgets.util.beans.Person;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;

public class TableRidget extends AbstractSelectableRidget implements ITableRidget {

	private final SelectionListener selectionTypeEnforcer;
	private final MouseListener doubleClickForwarder;

	private Collection<IActionListener> doubleClickListeners;
	private DataBindingContext dbc;
	private TableViewer viewer;
	private String[] renderingMethods;

	//
	// private IObservableList modelOV;
	// // model info
	// private Object model;
	// private String listProperty;
	//
	// private DataBindingContext dataBindingContext;
	//
	// private IObservableValue uiSingleSelectionOV;
	//
	// private String toolTip = null;

	public TableRidget() {
		selectionTypeEnforcer = new SelectionTypeEnforcer();
		doubleClickForwarder = new DoubleClickForwarder();
	}

	@Override
	protected void checkUIControl(Object uiControl) {
		AbstractSWTRidget.assertType(uiControl, Table.class);
	}

	@Override
	protected void bindUIControl() {
		final Table control = (Table) getUIControl();
		if (control != null && getRowObservables() != null) {
			viewer = new TableViewer(control);
			final ObservableListContentProvider viewerCP = new ObservableListContentProvider();
			IObservableMap[] attrMap = BeansObservables.observeMaps(viewerCP.getKnownElements(), Person.class,
					renderingMethods);
			viewer.setLabelProvider(new ObservableMapLabelProvider(attrMap));
			viewer.setContentProvider(viewerCP);
			viewer.setInput(getRowObservables());
			// TODO [ev] applyComparator();

			StructuredSelection currentSelection = new StructuredSelection(getSelection());

			dbc = new DataBindingContext();
			IObservableValue viewerSelection = ViewersObservables.observeSingleSelection(viewer);
			dbc.bindValue(viewerSelection, getSingleSelectionObservable(), new UpdateValueStrategy(
					UpdateValueStrategy.POLICY_UPDATE), new UpdateValueStrategy(UpdateValueStrategy.POLICY_UPDATE));
			IObservableList viewerSelections = new SelectionProviderMultipleSelectionObservableList(dbc
					.getValidationRealm(), viewer, Object.class);
			dbc.bindList(viewerSelections, getMultiSelectionObservable(), new UpdateListStrategy(
					UpdateListStrategy.POLICY_UPDATE), new UpdateListStrategy(UpdateListStrategy.POLICY_UPDATE));

			viewer.setSelection(currentSelection);

			control.addSelectionListener(selectionTypeEnforcer);
			control.addMouseListener(doubleClickForwarder);
		}
	}

	@Override
	protected void unbindUIControl() {
		if (dbc != null) {
			dbc.dispose();
			dbc = null;
		}
		Table control = getUIControl();
		if (control != null) {
			control.removeSelectionListener(selectionTypeEnforcer);
			control.removeMouseListener(doubleClickForwarder);
		}
		viewer = null;
	}

	@Override
	public Table getUIControl() {
		return (Table) super.getUIControl();
	}

	// private class SelectionObservable extends AbstractObservableValue {
	//
	// private IStructuredSelection selection;
	//
	// public SelectionObservable(TableViewer tv) {
	// tv.addSelectionChangedListener(new SelectionListener());
	// }
	//
	// private class SelectionListener implements ISelectionChangedListener {
	//
	// public void selectionChanged(SelectionChangedEvent event) {
	// IStructuredSelection newSelection = (IStructuredSelection)
	// event.getSelection();
	// fireValueChange(Diffs.createValueDiff(selection, newSelection));
	// selection = newSelection;
	// }
	// }
	//
	// @Override
	// protected Object doGetValue() {
	// return selection;
	// }
	//
	// public Object getValueType() {
	// return null;
	// }
	// }

	/**
	 * @see org.eclipse.riena.ui.ridgets.AbstractRidget#updateFromModel()
	 */
	public void updateFromModel() {
		super.updateFromModel();
		// if (modelOV != null) {
		// modelOV.clear();
		// modelOV.addAll(new ArrayList((Collection)
		// BeansObservables.observeValue(model, listProperty).getValue()));
		// tableViewer.refresh();
		// }
	}

	public void bindToModel(IObservableList listObservableValue, Class<? extends Object> rowBeanClass,
			String[] columnPropertyNames, String[] columnHeaders) {
		// this.modelOV = observableRowBeansList;
		// ObservableListContentProvider viewerContentProvider = new
		// ObservableListContentProvider();
		// tableViewer.setContentProvider(viewerContentProvider);
		// IObservableMap[] attributeMaps =
		// BeansObservables.observeMaps(viewerContentProvider.getKnownElements(),
		// rowBeanClass, columnPropertyNames);
		// tableViewer.setLabelProvider(new
		// ObservableMapLabelProvider(attributeMaps));
		// tableViewer.setInput(modelOV);
		// dataBindingContext.bindValue(new SelectionObservable(tableViewer),
		// new
		// WritableValue(), null, null);
		unbindUIControl();

		renderingMethods = new String[columnPropertyNames.length];
		System.arraycopy(columnPropertyNames, 0, renderingMethods, 0, renderingMethods.length);
		setRowObservables(listObservableValue);

		bindUIControl();
	}

	public void bindToModel(Object listBean, String listPropertyName, Class<? extends Object> rowBeanClass,
			String[] columnPropertyNames, String[] columnHeaders) {
		// this.model = rowBeansBean;
		// this.listProperty = rowBeansPropertyName;
		// bindToModel(new WritableList(new ArrayList((List)
		// BeansObservables.observeValue(model, rowBeansPropertyName)
		// .getValue()), rowBeanClass), rowBeanClass, columnPropertyNames, new
		// String[]
		// {});
		IObservableList listObservableValue = new UnboundPropertyWritableList(listBean, listPropertyName);
		bindToModel(listObservableValue, rowBeanClass, columnPropertyNames, columnHeaders);
	}

	public IObservableList getObservableList() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addDoubleClickListener(IActionListener listener) {
		Assert.isNotNull(listener, "listener is null"); //$NON-NLS-1$
		if (doubleClickListeners == null) {
			doubleClickListeners = new ArrayList<IActionListener>();
		}
		doubleClickListeners.add(listener);
	}

	public void removeDoubleClickListener(IActionListener listener) {
		if (doubleClickListeners != null) {
			doubleClickListeners.remove(listener);
		}
	}

	public int[] getSelectionIndices() {
		// TODO Auto-generated method stub
		return null;
	}

	public int indexOfOption(Object option) {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isSortedAscending() {
		return false;
	}

	public void setSortedAscending(boolean ascending) {
	}

	public int getSortedColumn() {
		return -1;
	}

	public void setSortedColumn(int columnIndex) {
	}

	public boolean isColumnSortable(int columnIndex) {
		return false;
	}

	public void setComparator(int columnIndex, Comparator<Object> compi) {
	}

	public void setColumnSortable(int columnIndex, boolean sortable) {
	}

	public int getSelectionIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	// helping methods
	// ////////////////

	private final class SelectionTypeEnforcer extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			if (SelectionType.SINGLE.equals(getSelectionType())) {
				Table control = (Table) e.widget;
				if (control.getSelectionCount() > 1) {
					// ignore this event
					e.doit = false;
					// set selection to most recent item
					control.setSelection(control.getSelectionIndex());
					// fire event
					Event event = new Event();
					event.type = SWT.Selection;
					event.doit = true;
					control.notifyListeners(SWT.Selection, event);
				}
			}
		}
	}

	/**
	 * Notifies doubleClickListeners when the bound widget is double clicked.
	 */
	private final class DoubleClickForwarder extends MouseAdapter {
		@Override
		public void mouseDoubleClick(MouseEvent e) {
			if (doubleClickListeners != null) {
				for (IActionListener listener : doubleClickListeners) {
					listener.callback();
				}
			}
		}
	}

}
