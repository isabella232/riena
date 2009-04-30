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
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.beans.PropertyDescriptor;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.beans.IBeanObservable;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.jface.databinding.viewers.ObservableListTreeContentProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

import org.eclipse.riena.ui.ridgets.IColumnFormatter;
import org.eclipse.riena.ui.ridgets.swt.AbstractSWTWidgetRidget;

/**
 * Label provider for TreeViewers that provides different images based on the
 * expansion state of a tree element:
 * <ul>
 * <li>expandable node - collapsed</li>
 * <li>expandable node - expanded</li>
 * <li>leaf (i.e. node with no children)</li>
 *</ul>
 *<p>
 * In addition, nodes corresponding to 'disabled' model values will be colored
 * in a distinct color.
 */
public final class TreeRidgetLabelProvider extends TableRidgetLabelProvider implements IColorProvider {

	private static final UpdateIconsTreeListener LISTENER = new UpdateIconsTreeListener();

	private final TreeViewer viewer;
	private final IObservableMap enablementAttribute;
	private final IObservableMap imageAttribute;

	/**
	 * Creates a new instance.
	 * 
	 * @param viewer
	 *            a non-null {@link TreeViewer} instance
	 * @param treeElementClass
	 *            the type of the elements in the tree (i.e. for treeRoot and
	 *            all children).
	 * @param knownElements
	 *            a non-null set of observable elements. The label provider may
	 *            track this set to update the tree as necessary - see
	 *            {@link ObservableListTreeContentProvider#getKnownElements()}
	 * @param valueAccessors
	 *            a non-null; non-empty array of Strings. Each String specifies
	 *            an accessor for obtaining an Object value from each child
	 *            object (example "value" specifies "getValue()"). The order in
	 *            the array corresponds to the initial order of the columns,
	 *            i.e. the 1st accessor will be used for column one/the tree,
	 *            the 2nd for column two, the 3rd for column three and so on
	 * @param enablementAccessor
	 *            a String specifying an accessor for obtaining a boolean value
	 *            from each child. The returned value will determine the
	 *            enabled/disabled state of this child. Example: 'enabled'
	 *            specifies "isEnabled()" or "getEnabled()". The parameter can
	 *            be {@code null} to enable all children
	 */
	public static TreeRidgetLabelProvider createLabelProvider(TreeViewer viewer, Class<?> treeElementClass,
			IObservableSet knownElements, String[] valueAccessors, String enablementAccessor, String imageAccessor,
			IColumnFormatter[] formatters) {
		IObservableMap[] map = createAttributeMap(treeElementClass, knownElements, valueAccessors, enablementAccessor,
				imageAccessor);
		int numColumns = valueAccessors.length;
		return new TreeRidgetLabelProvider(viewer, map, enablementAccessor, imageAccessor, formatters, numColumns);
	}

	/**
	 * Create an array of attributes that this label provides will observe. If
	 * observing a bean, and the observed attributes change the label provider
	 * will update the appropriate element.
	 */
	private static IObservableMap[] createAttributeMap(Class<?> treeElementClass, IObservableSet knownElements,
			String[] valueAccessors, String enablementAccessor, String imageAccessor) {
		IObservableMap[] result;
		String[] attributes = computeAttributes(valueAccessors, enablementAccessor, imageAccessor);
		if (AbstractSWTWidgetRidget.isBean(treeElementClass)) {
			result = BeansObservables.observeMaps(knownElements, treeElementClass, attributes);
		} else {
			result = PojoObservables.observeMaps(knownElements, treeElementClass, attributes);
		}
		return result;
	}

	private static String[] computeAttributes(String[] valueAccessors, String enablementAccessor, String imageAccessor) {
		int length = valueAccessors.length;
		if (enablementAccessor != null) {
			length++;
		}
		if (imageAccessor != null) {
			length++;
		}
		String[] attributes = new String[Math.max(length, valueAccessors.length)];
		System.arraycopy(valueAccessors, 0, attributes, 0, valueAccessors.length);
		if (length > valueAccessors.length) {
			int index = valueAccessors.length;
			if (enablementAccessor != null) {
				// add the enablement attribute to the list of observed attributes for the label provider
				attributes[index++] = enablementAccessor;
			}
			if (imageAccessor != null) {
				// add the image accessor to the list of observed attributes for the label provider
				attributes[index++] = imageAccessor;
			}
		}
		return attributes;
	}

	private TreeRidgetLabelProvider(TreeViewer viewer, IObservableMap[] attributeMap, String enablementAccessor,
			String imageAccessor, IColumnFormatter[] formatters, int numColumns) {
		super(attributeMap, formatters, numColumns);
		viewer.getTree().removeTreeListener(LISTENER);
		viewer.getTree().addTreeListener(LISTENER);
		enablementAttribute = findAttribute(attributeMap, enablementAccessor);
		imageAttribute = findAttribute(attributeMap, imageAccessor);
		this.viewer = viewer;
	}

	@Override
	public Image getImage(Object element) {
		String key = getImageKey(element);
		return Activator.getSharedImage(key);
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		Image result = null;
		if (columnIndex == 0) {
			// tree column 0 is special, because it contains the node & leaf icons
			// a. use the icon from formatter, if present
			// b. if formatter returns null (=don't care) or no formatter present,
			//    use the standard node / leaf icons
			// c. no automatic checkbox (=boolean) icons for column 0
			IColumnFormatter formatter = getFormatter(columnIndex);
			if (formatter != null) {
				result = (Image) formatter.getImage(element);
			}
			if (result == null) {
				result = getImage(element);
			}
		} else {
			// other columns: 
			// a. use icon from formatter, if present
			// b. use automatic checkbox icons for boolean values
			result = super.getColumnImage(element, columnIndex);
		}
		return result;
	}

	// IColorProvider methods
	/////////////////////////

	public Color getBackground(Object element) {
		return null;
	}

	public Color getForeground(Object element) {
		Color result = null;
		if (enablementAttribute != null) {
			Object value = enablementAttribute.get(element);
			if (Boolean.FALSE.equals(value)) {
				result = viewer.getControl().getDisplay().getSystemColor(SWT.COLOR_GRAY);
			}
		}
		return result;
	}

	// helping methods
	// ////////////////

	private IObservableMap findAttribute(IObservableMap[] attributeMap, String accessor) {
		IObservableMap result = null;
		if (accessor != null) {
			for (int i = attributeMap.length - 1; result == null && i > -1; i--) {
				IObservableMap attribute = attributeMap[i];
				IBeanObservable beanObservable = (IBeanObservable) attribute;
				PropertyDescriptor pd = beanObservable.getPropertyDescriptor();
				String property = pd != null ? pd.getName() : null;
				if (accessor.equals(property)) {
					result = attribute;
				}
			}
		}
		return result;
	}

	/**
	 * Returns the image key for the given element. If the element is a folder,
	 * the image key for an closed or open folder will be returned. If the
	 * element is a leaf and the element has its own image key, the image key of
	 * the element will be returned; otherwise the default image key of a leaf
	 * will be returned.
	 * 
	 * @param element
	 * @return image key
	 */
	private String getImageKey(Object element) {

		try {
			if (viewer.isExpandable(element)) {

				// folder
				boolean isExpanded = viewer.getExpandedState(element);
				if (isExpanded) {
					return SharedImages.IMG_NODE_EXPANDED;
				} else {
					return SharedImages.IMG_NODE_COLLAPSED;
				}

			} else {

				// leaf
				if (imageAttribute != null) {
					Object value = imageAttribute.get(element);
					if (value != null) {
						String key = (String) value;
						if (Activator.getSharedImage(key) != null) {
							return key;
						}
					}
				}
				return SharedImages.IMG_LEAF;

			}
		} catch (Exception e) {
			e.printStackTrace();
			return SharedImages.IMG_LEAF;
		}

	}

	// helping classes
	// ////////////////

	/**
	 * This listener is in charge of updating a tree item's icon whenever the
	 * item is collapsed or expanded.
	 */
	private static final class UpdateIconsTreeListener implements TreeListener {

		public void treeCollapsed(TreeEvent e) {
			// cannot use treeItem.getExpanded() because it has the old value
			updateIcon((TreeItem) e.item, false);
		}

		public void treeExpanded(TreeEvent e) {
			// cannot use treeItem.getExpanded() because it has the old value
			updateIcon((TreeItem) e.item, true);
		}

		private void updateIcon(TreeItem item, boolean isExpanded) {
			String key = isExpanded ? SharedImages.IMG_NODE_EXPANDED : SharedImages.IMG_NODE_COLLAPSED;
			Image image = Activator.getSharedImage(key);
			item.setImage(image);
		}
	}

}
