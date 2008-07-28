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

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.ui.ridgets.util.beans.WordNode;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * Tests for the class {@link TableRidgetLabelProvider}.
 */
public class TableRidgetLabelProviderTest extends TestCase {

	private TableRidgetLabelProvider labelProvider;
	private WordNode elementA;
	private WordNode elementB;

	@Override
	protected void setUp() throws Exception {
		Display display = Display.getDefault();
		Realm realm = SWTObservables.getRealm(display);
		ReflectionUtils.invokeHidden(realm, "setDefault", realm);

		IObservableSet elements = createElements();
		String[] columnProperties = { "word", "upperCase" };
		IObservableMap[] attrMap = BeansObservables.observeMaps(elements, WordNode.class, columnProperties);
		labelProvider = new TableRidgetLabelProvider(attrMap);
	}

	public void testGetText() {
		assertEquals("Alpha", labelProvider.getText(elementA));
		assertEquals("BRAVO", labelProvider.getText(elementB));
	}

	public void testGetColumnText() {
		assertEquals("Alpha", labelProvider.getColumnText(elementA, 0));
		assertEquals("BRAVO", labelProvider.getColumnText(elementB, 0));

		assertEquals("false", labelProvider.getColumnText(elementA, 1));
		assertEquals("true", labelProvider.getColumnText(elementB, 1));

		assertEquals(null, labelProvider.getColumnText(elementA, 99));
	}

	public void testGetImage() {
		assertNull(labelProvider.getImage(elementA));
		assertNull(labelProvider.getImage(elementB));

		IObservableSet elements = createElements();
		String[] columnProperties = { "upperCase" };
		IObservableMap[] attrMap = BeansObservables.observeMaps(elements, WordNode.class, columnProperties);
		labelProvider = new TableRidgetLabelProvider(attrMap);

		Image siUnchecked = Activator.getSharedImage(SharedImages.IMG_UNCHECKED);
		assertNotNull(siUnchecked);
		assertEquals(siUnchecked, labelProvider.getImage(elementA));

		Image siChecked = Activator.getSharedImage(SharedImages.IMG_CHECKED);
		assertNotNull(siChecked);
		assertEquals(siChecked, labelProvider.getImage(elementB));

		assertNotSame(siChecked, siUnchecked);
	}

	public void testGetColumnImage() {
		assertNull(labelProvider.getColumnImage(elementA, 0));
		assertNull(labelProvider.getColumnImage(elementB, 0));

		Image siUnchecked = Activator.getSharedImage(SharedImages.IMG_UNCHECKED);
		assertNotNull(siUnchecked);
		assertEquals(siUnchecked, labelProvider.getColumnImage(elementA, 1));

		Image siChecked = Activator.getSharedImage(SharedImages.IMG_CHECKED);
		assertNotNull(siChecked);
		assertEquals(siChecked, labelProvider.getColumnImage(elementB, 1));

		assertNotSame(siChecked, siUnchecked);

		assertEquals(null, labelProvider.getColumnImage(elementA, 99));
	}

	// helping methods
	// ////////////////

	private IObservableSet createElements() {
		Collection<WordNode> collection = new ArrayList<WordNode>();
		elementA = new WordNode("Alpha");
		elementB = new WordNode("Bravo");
		elementB.setUpperCase(true);
		collection.add(elementA);
		collection.add(elementB);
		IObservableSet elements = new WritableSet(Realm.getDefault(), collection, WordNode.class);
		return elements;
	}

}
