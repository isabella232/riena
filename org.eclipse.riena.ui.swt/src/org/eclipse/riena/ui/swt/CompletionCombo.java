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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;

/**
 * TODO [ev] docs
 * 
 * @since 2.0
 */
public class CompletionCombo extends AbstractCompletionCombo {

	/**
	 * This enumeration is used to configure the the way the autocompletion
	 * works.
	 * 
	 * @deprecated use
	 *             {@link org.eclipse.riena.ui.swt.AbstractCompletionCombo.AutoCompletionMode}
	 */
	public enum AutoCompletionMode {
		/**
		 * The Combo accepts all typed words and and just stops tracking the
		 * list entries if no match is found.
		 * 
		 * @deprecated use
		 *             {@link org.eclipse.riena.ui.swt.AbstractCompletionCombo.AutoCompletionMode#ALLOW_MISSMATCH}
		 */
		ALLOW_MISSMATCH,
		/**
		 * The Combo rejects typed characters that would make the String in the
		 * textfield not match any of the entries in the list.
		 * 
		 * @deprecated use
		 *             {@link org.eclipse.riena.ui.swt.AbstractCompletionCombo.AutoCompletionMode#NO_MISSMATCH}
		 */
		NO_MISSMATCH
	}

	public CompletionCombo(final Composite parent, final int style) {
		super(parent, style);
	}

	/**
	 * @deprecated use
	 *             {@link #setAutoCompletionMode(org.eclipse.riena.ui.swt.AbstractCompletionCombo.AutoCompletionMode)}
	 */
	@Deprecated
	public void setAutoCompletionMode(final AutoCompletionMode autoCompletionMode) {
		if (AutoCompletionMode.ALLOW_MISSMATCH == autoCompletionMode) {
			setAutoCompletionMode(org.eclipse.riena.ui.swt.AbstractCompletionCombo.AutoCompletionMode.ALLOW_MISSMATCH);
		} else if (AutoCompletionMode.NO_MISSMATCH == autoCompletionMode) {
			setAutoCompletionMode(org.eclipse.riena.ui.swt.AbstractCompletionCombo.AutoCompletionMode.NO_MISSMATCH);
		} else {
			throw new IllegalArgumentException("unsupported autoCompletionMode: " + autoCompletionMode); //$NON-NLS-1$
		}
	}

	@Override
	protected Label createLabel(final Composite parent) {
		return null;
	}

	@Override
	protected Control createList(final Composite parent) {
		final int style = getStyle();
		int listStyle = SWT.SINGLE | SWT.V_SCROLL;
		if ((style & SWT.FLAT) != 0) {
			listStyle |= SWT.FLAT;
		}
		if ((style & SWT.RIGHT_TO_LEFT) != 0) {
			listStyle |= SWT.RIGHT_TO_LEFT;
		}
		if ((style & SWT.LEFT_TO_RIGHT) != 0) {
			listStyle |= SWT.LEFT_TO_RIGHT;
		}
		return new List(parent, listStyle);
	}

	@Override
	protected void deselectAll(final Control list) {
		((List) list).deselectAll();
	}

	@Override
	protected Image getImage(final Control list, final int index) {
		return null;
	}

	@Override
	protected Image[] getImages(final Control list) {
		return null;
	}

	@Override
	protected String getItem(final Control list, final int index) {
		return ((List) list).getItem(index);
	}

	@Override
	protected int getItemHeight(final Control list) {
		return ((List) list).getItemHeight();
	}

	@Override
	protected String[] getItems(final Control list) {
		return ((List) list).getItems();
	}

	@Override
	protected int getItemCount(final Control list) {
		return ((List) list).getItemCount();
	}

	@Override
	protected int getSelectionIndex(final Control list) {
		return ((List) list).getSelectionIndex();
	}

	@Override
	protected int indexOf(final Control list, final String string, final int start) {
		return ((List) list).indexOf(string, start);
	}

	@Override
	protected void removeAll(final Control list) {
		((List) list).removeAll();
	}

	@Override
	protected void setItems(final Control list, final String[] items, final Image[] images) {
		((List) list).setItems(items);
	}

	@Override
	protected void setSelection(final Control list, final int index) {
		((List) list).setSelection(index);
	}

	@Override
	protected void setTopIndex(final Control list, final int index) {
		((List) list).setTopIndex(index);
	}
}
