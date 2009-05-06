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
package org.eclipse.riena.ui.swt.utils;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchSite;
import org.eclipse.ui.part.ViewPart;

import org.eclipse.riena.core.util.ReflectionUtils;

/**
 * Manages one or more "detached" views.
 * <p>
 * Client code must {@link #dispose()} instances when no longer needed.
 */
public class DetachedViewsManager {

	private final Map<String, Shell> id2shell;
	private final IWorkbenchSite site;

	/**
	 * Create a new DetachedViewsManager instance.
	 * <p>
	 * Client code must {@link #dispose()} instances when no longer needed.
	 */
	public DetachedViewsManager(IWorkbenchSite site) {
		Assert.isNotNull(site);
		id2shell = new HashMap<String, Shell>();
		this.site = site;
	}

	/**
	 * Close (=dispose) thew view / shell view the given id.
	 * 
	 * @param id
	 *            id of the view to show; must be unique within this instance
	 */
	public void closeView(String id) {
		Shell shell = id2shell.remove(id);
		if (shell != null) {
			if (!shell.isDisposed()) {
				shell.dispose();
			}
		}
	}

	/**
	 * Deallocate ressources used by this class. Client code must
	 * {@link #dispose()} instances when no longer needed.
	 */
	public void dispose() {
		String[] keys = id2shell.keySet().toArray(new String[0]);
		for (String id : keys) {
			closeView(id);
		}
	}

	/**
	 * Return the shell hosting the view with the given id
	 * 
	 * @param id
	 *            the view / shell id;
	 * @return a Shell instance or null
	 */
	public Shell getShell(String id) {
		return id2shell.get(id);
	}

	/**
	 * Hide the view / shell with the given id
	 * 
	 * @param id
	 *            id of the view / shell to hide
	 */
	public void hideView(String id) {
		Shell shell = id2shell.get(id);
		if (shell != null) {
			shell.setVisible(false);
		}
	}

	/**
	 * Show the view / shell with the given id. If the given id matches no
	 * shell, then a new shell / view is created using the given viewClazz.
	 * 
	 * @param id
	 *            id of the view / shell to show; must be unique within this
	 *            instance; not tied to the view-part id, since the view is
	 *            created by reflection
	 * @param viewClazz
	 *            the class of the view; must have parameterless constructor
	 * @param position
	 *            one of SWT.LEFT, SWT.RIGHT, SWT.TOP, SWT.BOTTOM. Will place
	 *            the view to the specified edge of the main window. Note the
	 *            position prefference is only applied if a new shell is
	 *            created.
	 */
	public void showView(String id, Class<? extends ViewPart> viewClazz, int position) {
		Rectangle viewBounds = site.getWorkbenchWindow().getShell().getBounds();
		Shell shell = id2shell.get(id);
		if (shell == null) {
			int x;
			int y;
			Rectangle bounds;
			switch (position) {
			case SWT.RIGHT:
				x = viewBounds.x + viewBounds.width;
				y = viewBounds.y;
				bounds = new Rectangle(x, y, viewBounds.width / 2, viewBounds.height);
				break;
			case SWT.LEFT:
				x = viewBounds.x - (viewBounds.width / 2);
				y = viewBounds.y;
				bounds = new Rectangle(x, y, viewBounds.width / 2, viewBounds.height);
				break;
			case SWT.TOP:
				x = viewBounds.x;
				y = viewBounds.y - (viewBounds.height / 2);
				bounds = new Rectangle(x, y, viewBounds.width, viewBounds.height / 2);
				break;
			case SWT.BOTTOM:
				x = viewBounds.x;
				y = viewBounds.y + viewBounds.height;
				bounds = new Rectangle(x, y, viewBounds.width, viewBounds.height / 2);
				break;
			default:
				throw new IllegalArgumentException("position=" + position); //$NON-NLS-1$
			}
			shell = openShell(viewClazz, bounds);
			if (shell != null) {
				id2shell.put(id, shell);
			}
		} else {
			showShell(shell);
		}
	}

	/**
	 * Show the view / shell with the given id. If the given id matches no
	 * shell, then a new shell / view is created using the given viewClazz.
	 * 
	 * @param id
	 *            id of the view / shell to show; must be unique within this
	 *            instance; not tied to the view-part id, since the view is
	 *            created by reflection
	 * @param viewClazz
	 *            the class of the view; must have parameterless constructor
	 * @param bounds
	 *            the desired size and location for the shell. Note that this is
	 *            applied only if a new shell is created.
	 */
	public void showView(String viewId, Class<? extends ViewPart> viewClazz, Rectangle bounds) {
		Shell shell = id2shell.get(viewId);
		if (shell == null) {
			openShell(viewClazz, bounds);
			if (shell != null) {
				id2shell.put(viewId, shell);
			}
		} else {
			showShell(shell);
		}
	}

	// protected methods
	////////////////////

	/**
	 * Determines the style bits for new shell instances created by this class.
	 * <p>
	 * Default value is: {@code SWT.TITLE | SWT.RESIZE}
	 * <p>
	 * Implementors may override to use different style bits. Note that the
	 * close button will be disabled even if you use {@link SWT#CLOSE}.
	 * 
	 * @see http://dev.eclipse.org/newslists/news.eclipse.tools/msg07666.html
	 */
	protected int getShellStyle() {
		return SWT.TITLE | SWT.RESIZE;
	}

	// helping methods
	//////////////////

	private void showShell(Shell shell) {
		shell.setVisible(true);
	}

	private Shell openShell(Class<? extends ViewPart> viewClazz, Rectangle bounds) {
		Shell result = null;

		result = new Shell(site.getShell(), getShellStyle());
		result.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				e.doit = false; // prevent manual close just in case
			}
		});
		// TODO [ev] - need help - blocked by 274916
		IViewPart viewPart = (IViewPart) ReflectionUtils.newInstance(viewClazz, (Object[]) null);
		viewPart.createPartControl(result);
		result.setBounds(bounds);
		result.open();
		return result;
	}
}
