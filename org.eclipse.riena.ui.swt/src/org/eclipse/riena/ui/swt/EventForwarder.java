/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import org.eclipse.riena.ui.swt.facades.SWTFacade;

/**
 * TODO [ev] docs
 */
class EventForwarder implements Listener {

	private static final SWTFacade SWT_FACADE = SWTFacade.getDefault();

	private final Control source;
	private final Control target;

	EventForwarder(final Control source, final Control target) {
		final int[] eventTypes = { SWT.FocusIn, SWT.FocusOut, SWT.MenuDetect, SWT.DragDetect, SWT.MouseDoubleClick,
				SWTFacade.MouseMove, SWTFacade.MouseEnter, SWTFacade.MouseExit, SWTFacade.MouseHover,
				SWTFacade.MouseWheel, SWT.MouseDown, SWT.Traverse, };
		for (final int type : eventTypes) {
			source.addListener(type, this);
		}
		this.source = source;
		this.target = target;
	}

	public void handleEvent(final Event event) {
		if (target.isDisposed()) {
			return;
		}
		Event e;
		switch (event.type) {
		case SWT.FocusIn:
		case SWT.FocusOut:
		case SWT.MenuDetect:
			notifyTarget(createEvent(event));
			break;
		case SWT.DragDetect:
		case SWT.MouseDoubleClick:
		case SWTFacade.MouseMove:
		case SWTFacade.MouseEnter:
		case SWTFacade.MouseExit:
		case SWTFacade.MouseHover:
		case SWTFacade.MouseWheel:
			notifyTarget(createEvent(event));
			event.type = SWT.None;
			break;
		case SWT.MouseDown:
			e = createEvent(event);
			notifyTarget(e);
			event.doit = e.doit;
			break;
		case SWT.Traverse:
			e = createEvent(event);
			notifyTarget(e);
			event.doit = e.doit;
			event.detail = e.detail;
			break;
		}
	}

	private Event createEvent(final Event event) {
		final Event result = new Event();
		result.display = event.display;
		result.widget = target;
		result.type = event.type;
		result.detail = event.detail;
		final Point pt = getDisplay().map(source, target, event.x, event.y);
		event.x = pt.x;
		event.y = pt.y;
		result.count = event.count;
		result.time = event.time;
		result.button = event.button;
		result.character = event.character;
		result.keyCode = event.keyCode;
		SWT_FACADE.copyEventKeyLocation(event, result);
		result.stateMask = event.stateMask;
		result.doit = event.doit;
		result.data = event.data;
		return result;
	}

	// helping methods
	//////////////////

	private Display getDisplay() {
		return source.getDisplay();
	}

	private void notifyTarget(final Event event) {
		target.notifyListeners(event.type, event);
	}
}
