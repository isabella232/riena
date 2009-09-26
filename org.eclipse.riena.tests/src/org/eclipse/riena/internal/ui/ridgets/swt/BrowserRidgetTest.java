/*******************************************************************************
 * Copyright (c) 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.ui.ridgets.swt;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.riena.beans.common.StringBean;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.ui.ridgets.IBrowserRidget;
import org.eclipse.riena.ui.ridgets.swt.uibinding.SwtControlRidgetMapper;

/**
 * Tests for {@link BrowserRidget}
 */
@UITestCase
public class BrowserRidgetTest extends AbstractSWTRidgetTest {

	@Override
	protected Widget createWidget(Composite parent) {
		return new Browser(parent, SWT.NONE);
	}

	@Override
	protected IBrowserRidget createRidget() {
		return new BrowserRidget();
	}

	@Override
	protected Browser getWidget() {
		return (Browser) super.getWidget();
	}

	@Override
	protected IBrowserRidget getRidget() {
		return (IBrowserRidget) super.getRidget();
	}

	public void testRidgetMapping() {
		SwtControlRidgetMapper mapper = SwtControlRidgetMapper.getInstance();
		assertSame(BrowserRidget.class, mapper.getRidgetClass(getWidget()));
	}

	public void testSetFocusable() {
		// skipping super.testSetFocusable() because of Bug 84532
		ok();
	}

	public void testRequestFocus() {
		// skipping testRequestFocus() because of Bug 84532
		ok();
	}

	public void testBindToModel() {
		IBrowserRidget ridget = getRidget();
		String url1 = "http://www.redview.org";
		String url2 = "http://www.eclipse.org";

		StringBean bean = new StringBean(url1);
		ridget.bindToModel(bean, StringBean.PROP_VALUE);

		assertNull(ridget.getUrl());

		ridget.updateFromModel();

		assertEquals(url1, bean.getValue());
		assertEquals(url1, ridget.getUrl());

		bean.setValue(url2);

		assertEquals(url2, bean.getValue());
		assertEquals(url1, ridget.getUrl());

		ridget.updateFromModel();

		assertEquals(url2, bean.getValue());
		assertEquals(url2, ridget.getUrl());

		ridget.setUrl(url1);

		assertEquals(url1, bean.getValue());
		assertEquals(url1, ridget.getUrl());
	}

	public void testSetUrl() {
		IBrowserRidget ridget = getRidget();

		ridget.setUrl("http://www.redview.org");

		assertEquals("http://www.redview.org", ridget.getUrl());
		// control.getUrl() is not reliable because of timing + network acccess
		// so I'm not testing that...

		ridget.setUrl("b o g u s");

		assertEquals("b o g u s", ridget.getUrl());

		ridget.setUrl("");

		assertEquals("", ridget.getUrl());

		ridget.setUrl(null);

		assertEquals(null, ridget.getUrl());

		ridget.setUrl("about:blank");

		assertEquals("about:blank", ridget.getUrl());
	}

	public void testSetUrlFiresEvents() {
		IBrowserRidget ridget = getRidget();
		final String newValue = "http://www.redview.org";
		final String oldValue = ridget.getUrl();

		assertFalse(newValue.equals(ridget.getUrl()));

		expectPropertyChangeEvent(IBrowserRidget.PROPERTY_URL, oldValue, newValue);
		ridget.setUrl(newValue);
		verifyPropertyChangeEvents();

		expectNoPropertyChangeEvent();
		ridget.setUrl(newValue);
		verifyPropertyChangeEvents();
	}

	public void testSetUrlOnOutputOnly() {
		IBrowserRidget ridget = getRidget();

		assertNull(ridget.getUrl());

		// allow ridget.setUrl() on output only
		ridget.setOutputOnly(true);
		ridget.setUrl("http://www.redview.org");
		ridget.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				System.out.println(evt.getPropertyName() + " " + evt.getNewValue());
			}
		});

		assertEquals("http://www.redview.org", ridget.getUrl());

		// disallow widget.setUrl() on output only - can't test this because
		// widget.getUrl() is not reliable because of timing + network access
	}
}
