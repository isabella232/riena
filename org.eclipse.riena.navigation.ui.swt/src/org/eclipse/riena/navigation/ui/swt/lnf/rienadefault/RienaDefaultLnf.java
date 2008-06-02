/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation.ui.swt.lnf.rienadefault;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.eclipse.riena.navigation.ui.swt.lnf.ILnfRenderer;
import org.eclipse.riena.navigation.ui.swt.lnf.ILnfResource;
import org.eclipse.riena.navigation.ui.swt.lnf.ILnfTheme;
import org.eclipse.riena.navigation.ui.swt.lnf.LnfManager;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Resource;

/**
 * Default Look and Feel of Riena.
 */
public class RienaDefaultLnf {

	private final static String DEFAULT_THEME_CLASSNAME = RienaDefaultTheme.class.getName();

	private Map<String, ILnfResource> resourceTable = new Hashtable<String, ILnfResource>();
	private Map<String, ILnfRenderer> rendererTable = new Hashtable<String, ILnfRenderer>();
	private ILnfTheme theme;

	private Map<String, ILnfResource> getResourceTable() {
		return resourceTable;
	}

	private Map<String, ILnfRenderer> getRendererTable() {
		return rendererTable;
	}

	/**
	 * Initializes the Look and Feel. Fills the tables of resources and
	 * renderers.
	 */
	public void initialize() {
		uninitialize();
		initWidgetRendererDefaults();
		initResourceDefaults();
	}

	/**
	 * Uninitialize the Look and Feel. Disposes resources and clears the tables
	 * of resources and renderers.
	 */
	public void uninitialize() {
		disposeAllResources();
		getResourceTable().clear();
		getRendererTable().clear();
	}

	/**
	 * Initializes the table with the renderers.
	 */
	protected void initWidgetRendererDefaults() {
		getRendererTable().put("SubModuleViewRenderer.borderRenderer", new EmbeddedBorderRenderer()); //$NON-NLS-1$
		getRendererTable().put("SubModuleViewRenderer.titlebarRenderer", new EmbeddedTitlebarRenderer()); //$NON-NLS-1$
		getRendererTable().put("SubModuleView.renderer", new SubModuleViewRenderer()); //$NON-NLS-1$
	}

	/**
	 * Initializes the table with the resources.
	 */
	protected void initResourceDefaults() {
		initColorDefaults();
		initFontDefaults();
	}

	/**
	 * Puts the colors to resource table.
	 */
	protected void initColorDefaults() {
		if (getTheme() != null) {
			getTheme().addCustomColors(getResourceTable());
		}
	}

	/**
	 * Puts the fonrs to resource table.
	 */
	protected void initFontDefaults() {
		if (getTheme() != null) {
			getTheme().addCustomFonts(getResourceTable());
		}
	}

	/**
	 * Disposes all resources of the Look and Feel.
	 */
	private void disposeAllResources() {
		Set<String> keys = getResourceTable().keySet();
		for (String key : keys) {
			ILnfResource value = getResourceTable().get(key);
			if (value != null) {
				value.dispose();
			}
		}
	}

	/**
	 * Returns the resource for the given key.
	 * 
	 * @param key -
	 *            key whose associated resource is to be returned.
	 * @return the resource to which this map maps the specified key, or
	 *         <code>null</code> if the map contains no mapping for this key.
	 */
	private Resource getResource(String key) {
		ILnfResource value = getResourceTable().get(key);
		if (value != null) {
			return value.getResource();
		} else {
			return null;
		}
	}

	/**
	 * Returns the color for the given key.
	 * 
	 * @param key -
	 *            key whose associated color is to be returned.
	 * @return the color to which this map maps the specified key, or
	 *         <code>null</code> if the map contains no mapping for this key.
	 */
	public Color getColor(String key) {
		Resource value = getResource(key);
		if (value instanceof Color) {
			return (Color) value;
		} else {
			return null;
		}
	}

	/**
	 * Returns the font for the given key.
	 * 
	 * @param key -
	 *            key whose associated font is to be returned.
	 * @return the font to which this map maps the specified key, or
	 *         <code>null</code> if the map contains no mapping for this key.
	 */
	public Font getFont(String key) {
		Resource value = getResource(key);
		if (value instanceof Font) {
			return (Font) value;
		} else {
			return null;
		}
	}

	/**
	 * Returns the renderer for the given key.
	 * 
	 * @param key -
	 *            key whose associated rendere is to be returned.
	 * @return the rendere to which this renderer maps the specified key, or
	 *         <code>null</code> if the map contains no mapping for this key.
	 */
	public ILnfRenderer getRenderer(String key) {
		return getRendererTable().get(key);
	}

	/**
	 * Loads the theme specified by the given class name.
	 * 
	 * @param themeClassName -
	 *            a string specifying the name of the class that implements the
	 *            theme
	 * @return theme
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private static ILnfTheme createTheme(String themeClassName) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		ClassLoader classLoader = LnfManager.class.getClassLoader();
		Class<?> themeClass = classLoader.loadClass(themeClassName);
		return (ILnfTheme) themeClass.newInstance();
	}

	/**
	 * Returns the current theme of the Look and Feel.
	 * 
	 * @return theme
	 */
	public ILnfTheme getTheme() {
		if (theme == null) {
			try {
				theme = createTheme(DEFAULT_THEME_CLASSNAME);
			} catch (Exception e) {
				e.printStackTrace();
				throw new Error("can't load " + DEFAULT_THEME_CLASSNAME); //$NON-NLS-1$
			}
		}
		return theme;
	}

	/**
	 * Sets the theme to be used by the Look and Feel.
	 * 
	 * @param newTheme -
	 *            the theme to be used
	 */
	public void setTheme(ILnfTheme newTheme) {
		if (theme != newTheme) {
			uninitialize();
			theme = newTheme;
			initialize();
		}
	}

	// getIcon
	// getWidgetRenderer

}
