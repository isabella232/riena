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
 * 
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

	public void initialize() {
		uninitialize();
		initWidgetRendererDefaults();
		initResourceDefaults();
	}

	public void uninitialize() {
		disposeAll();
		getResourceTable().clear();
		getRendererTable().clear();
	}

	protected void initWidgetRendererDefaults() {
		getRendererTable().put("SubModuleView.renderer", new SubModuleViewRenderer());
	}

	protected void initResourceDefaults() {
		initColorDefaults();
		initFontDefaults();
	}

	protected void initColorDefaults() {
		if (getTheme() != null) {
			getTheme().addCustomColors(getResourceTable());
		}
	}

	protected void initFontDefaults() {
		if (getTheme() != null) {
			getTheme().addCustomFonts(getResourceTable());
		}
	}

	private void disposeAll() {
		disposeAllResources();
	}

	private void disposeAllResources() {
		Set<String> keys = getResourceTable().keySet();
		for (String key : keys) {
			ILnfResource value = getResourceTable().get(key);
			if (value != null) {
				value.dispose();
			}
		}
	}

	public Resource getResource(String key) {
		ILnfResource value = getResourceTable().get(key);
		if (value != null) {
			return value.getResource();
		} else {
			return null;
		}
	}

	public Color getColor(String key) {
		Resource value = getResource(key);
		if (value instanceof Color) {
			return (Color) value;
		} else {
			return null;
		}
	}

	public Font getFont(String key) {
		Resource value = getResource(key);
		if (value instanceof Font) {
			return (Font) value;
		} else {
			return null;
		}
	}

	public ILnfRenderer getRenderer(String key) {
		return getRendererTable().get(key);
	}

	private static ILnfTheme createTheme(String themeClassName) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		ClassLoader classLoader = LnfManager.class.getClassLoader();
		Class<?> themeClass = classLoader.loadClass(themeClassName);
		return (ILnfTheme) themeClass.newInstance();
	}

	public ILnfTheme getTheme() {
		if (theme == null) {
			try {
				theme = createTheme(DEFAULT_THEME_CLASSNAME);
			} catch (Exception e) {
				e.printStackTrace();
				throw new Error("can't load " + DEFAULT_THEME_CLASSNAME);
			}
		}
		return theme;
	}

	public void setTheme(ILnfTheme newTheme) {
		if (theme != newTheme) {
			theme = newTheme;
			initialize();
		}
	}

	// getIcon
	// getWidgetRenderer

}
