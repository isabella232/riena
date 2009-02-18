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

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

/**
 * Label provider that shows checked / unchecked icons, if the attribute in a
 * column is of Boolean type.
 * 
 * @see TableRidget
 */
public class TableRidgetLabelProvider extends ObservableMapLabelProvider implements ITableColorProvider,
		ITableFontProvider {

	private final IObservableMap[] attributeMap;
	private final Object[] labelProviders;

	/**
	 * Create a new instance.
	 * 
	 * @param viewer
	 *            a non-null {@link TreeViewer} instance
	 * @param attributeMap
	 *            a non-null {@link IObservableMap} instance
	 */
	public TableRidgetLabelProvider(IObservableMap[] attributeMap) {
		this(attributeMap, new Object[attributeMap.length]);
	}

	/**
	 * Create a new instance
	 * 
	 * @param viewer
	 *            a non-null {@link TreeViewer} instance
	 * @param attributeMap
	 *            a non-null {@link IObservableMap} instance
	 * @param labelProviders
	 *            an array of objects that implement one or more of
	 *            {@link ILabelProvider}, {@link IColorProvider},
	 *            {@link IFontProvider} or null. The array must have the same
	 *            number of entries as attributeMap, however individual entries
	 *            can be null.
	 * @throws RuntimeException
	 *             if attributeMap and labelProviders have not the same number
	 *             of entries
	 */
	public TableRidgetLabelProvider(IObservableMap[] attributeMap, Object[] labelProviders) {
		super(attributeMap);
		Assert.isLegal(attributeMap.length == labelProviders.length);
		this.attributeMap = new IObservableMap[attributeMap.length];
		System.arraycopy(attributeMap, 0, this.attributeMap, 0, this.attributeMap.length);
		this.labelProviders = new Object[labelProviders.length];
		System.arraycopy(labelProviders, 0, this.labelProviders, 0, this.labelProviders.length);
	}

	@Override
	public Image getImage(Object element) {
		return getColumnImage(element, 0);
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		if (columnIndex < attributeMap.length) {
			Object result = attributeMap[columnIndex].get(element);
			if (result instanceof Boolean) {
				String key = ((Boolean) result).booleanValue() ? SharedImages.IMG_CHECKED : SharedImages.IMG_UNCHECKED;
				return Activator.getSharedImage(key);
			}
		}
		return super.getColumnImage(element, columnIndex);
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (columnIndex < labelProviders.length) {
			Object labelProvider = this.labelProviders[columnIndex];
			if (labelProvider instanceof ILabelProvider) {
				return ((ILabelProvider) labelProvider).getText(element);
			}
		}
		return super.getColumnText(element, columnIndex);
	}

	public Color getForeground(Object element, int columnIndex) {
		if (columnIndex < labelProviders.length) {
			Object colorProvider = this.labelProviders[columnIndex];
			if (colorProvider instanceof IColorProvider) {
				return ((IColorProvider) colorProvider).getForeground(element);
			}
		}
		return null;
	}

	public Color getBackground(Object element, int columnIndex) {
		if (columnIndex < labelProviders.length) {
			Object colorProvider = this.labelProviders[columnIndex];
			if (colorProvider instanceof IColorProvider) {
				return ((IColorProvider) colorProvider).getBackground(element);
			}
		}
		return null;
	}

	public Font getFont(Object element, int columnIndex) {
		if (columnIndex < labelProviders.length) {
			Object fontProvider = this.labelProviders[columnIndex];
			if (fontProvider instanceof IFontProvider) {
				return ((IFontProvider) fontProvider).getFont(element);
			}
		}
		return null;
	}

	/**
	 * Returns the value of the given element at the specified column index.
	 * 
	 * @param element
	 * @param columnIndex
	 *            - column index
	 * @return value or {@code null} if column index is not correct
	 */
	public Object getColumnValue(Object element, int columnIndex) {
		if (columnIndex < attributeMap.length) {
			return attributeMap[columnIndex].get(element);
		}
		return null;
	}

}
