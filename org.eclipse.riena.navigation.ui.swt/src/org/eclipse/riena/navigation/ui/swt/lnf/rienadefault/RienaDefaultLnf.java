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
package org.eclipse.riena.navigation.ui.swt.lnf.rienadefault;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.eclipse.riena.navigation.ui.swt.lnf.ILnfKeyConstants;
import org.eclipse.riena.navigation.ui.swt.lnf.ILnfRenderer;
import org.eclipse.riena.navigation.ui.swt.lnf.ILnfResource;
import org.eclipse.riena.navigation.ui.swt.lnf.ILnfTheme;
import org.eclipse.riena.navigation.ui.swt.lnf.LnfManager;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Resource;

/**
 * Default Look and Feel of Riena.
 */
public class RienaDefaultLnf {

	private final static String DEFAULT_THEME_CLASSNAME = RienaDefaultTheme.class.getName();
	private Map<String, ILnfResource> resourceTable = new Hashtable<String, ILnfResource>();
	private Map<String, Object> settingTable = new Hashtable<String, Object>();
	private Map<String, ILnfRenderer> rendererTable = new Hashtable<String, ILnfRenderer>();
	private ILnfTheme theme;
	private boolean initialized;

	private Map<String, ILnfResource> getResourceTable() {
		return resourceTable;
	}

	private Map<String, ILnfRenderer> getRendererTable() {
		return rendererTable;
	}

	private Map<String, Object> getSettingTable() {
		return settingTable;
	}

	/**
	 * Initializes the Look and Feel. Fills the tables of resources and
	 * renderers.
	 */
	public void initialize() {
		if (!isInitialized()) {
			uninitialize();
			setInitialized(true);
			initWidgetRendererDefaults();
			initResourceDefaults();
		}
	}

	/**
	 * Uninitialize the Look and Feel. Disposes resources and clears the tables
	 * of resources and renderers.
	 */
	public void uninitialize() {
		disposeAllResources();
		getResourceTable().clear();
		getRendererTable().clear();
		getSettingTable().clear();
		setInitialized(false);
	}

	/**
	 * Initializes the table with the renderers.
	 */
	protected void initWidgetRendererDefaults() {

		getRendererTable().put(ILnfKeyConstants.SUB_MODULE_VIEW_BORDER_RENDERER, new EmbeddedBorderRenderer());
		getRendererTable().put(ILnfKeyConstants.SUB_MODULE_VIEW_TITLEBAR_RENDERER, new EmbeddedTitlebarRenderer());
		getRendererTable().put(ILnfKeyConstants.SUB_MODULE_VIEW_HOVER_BORDER_RENDERER, new HoverBorderRenderer());
		getRendererTable().put(ILnfKeyConstants.SUB_MODULE_VIEW_RENDERER, new SubModuleViewRenderer());
		getRendererTable().put(ILnfKeyConstants.MODULE_GROUP_RENDERER, new ModuleGroupRenderer());
		getRendererTable()
				.put(ILnfKeyConstants.SUB_APPLICATION_SWITCHER_RENDERER, new SubApplicationSwitcherRenderer());
		getRendererTable().put(ILnfKeyConstants.SUB_APPLICATION_TAB_RENDERER, new SubApplicationSwitcherRenderer());

	}

	/**
	 * Initializes the table with the resources.
	 */
	protected void initResourceDefaults() {
		initColorDefaults();
		initFontDefaults();
		initImageDefaults();
		initSettingsDefaults();
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
	 * Puts the images to resource table.
	 */
	protected void initImageDefaults() {
		if (getTheme() != null) {
			getTheme().addCustomImages(getResourceTable());
		}
	}

	/**
	 * Puts settings to the table.
	 */
	protected void initSettingsDefaults() {
		if (getTheme() != null) {
			getTheme().addCustomSettings(getSettingTable());
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
	 * Returns the image for the given key.
	 * 
	 * @param key -
	 *            key whose associated image is to be returned.
	 * @return the image to which this map maps the specified key, or
	 *         <code>null</code> if the map contains no mapping for this key.
	 */
	public Image getImage(String key) {
		Resource value = getResource(key);
		if (value instanceof Image) {
			return (Image) value;
		} else {
			return null;
		}
	}

	/**
	 * Returns the renderer for the given key.
	 * 
	 * @param key -
	 *            key whose associated renderer is to be returned.
	 * @return the renderer to which this renderer maps the specified key, or
	 *         <code>null</code> if the map contains no mapping for this key.
	 */
	public ILnfRenderer getRenderer(String key) {
		return getRendererTable().get(key);
	}

	/**
	 * Returns the setting for the given key
	 * 
	 * @param key -
	 *            key whose associated setting is to be returned.
	 * @return the setting to which this setting maps the specified key, or
	 *         <code>null</code> if the map contains no mapping for this key.
	 */
	public Object getSetting(String key) {
		return getSettingTable().get(key);
	}

	/**
	 * Returns the integer value of the setting for the given key
	 * 
	 * @param key -
	 *            key whose associated setting is to be returned.
	 * @return the setting to which this setting maps the specified key, or
	 *         <code>null</code> if the map contains no mapping for this key.
	 */
	public Integer getIntegerSetting(String key) {
		Object value = getSetting(key);
		if (value instanceof Integer) {
			return (Integer) value;
		} else {
			return null;
		}
	}

	/**
	 * Returns the boolean value of the setting for the given key
	 * 
	 * @param key -
	 *            key whose associated setting is to be returned.
	 * @return the setting to which this setting maps the specified key, or
	 *         <code>false</code> if the map contains no mapping for this key.
	 */
	public Boolean getBooleanSetting(String key) {
		Object value = getSetting(key);
		if (value instanceof Boolean) {
			return (Boolean) value;
		} else {
			return false;
		}
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
		if (!isInitialized()) {
			initialize();
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
			theme = newTheme;
			setInitialized(false);
		}
	}

	/**
	 * @return the initialize
	 */
	private boolean isInitialized() {
		return initialized;
	}

	/**
	 * @param initialize
	 *            the initialize to set
	 */
	private void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	// getWidgetRenderer

}
