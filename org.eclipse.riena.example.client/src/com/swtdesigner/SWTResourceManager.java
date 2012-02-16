/**
 * This class may be freely distributed as part of any application or plugin.
 * <p>
 * Copyright (c) 2003 - 2005, Instantiations, Inc. <br>
 * All Rights Reserved
 */
package com.swtdesigner;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Display;

/**
 * Utility class for managing OS resources associated with SWT controls such as
 * colors, fonts, images, etc.
 * 
 * !!! IMPORTANT !!! Application code must explicitly invoke the
 * <code>dispose()</code> method to release the operating system resources
 * managed by cached objects when those objects and OS resources are no longer
 * needed (e.g. on application shutdown)
 * 
 * This class may be freely distributed as part of any application or plugin.
 * <p>
 * Copyright (c) 2003 - 2005, Instantiations, Inc. <br>
 * All Rights Reserved
 * 
 * @author scheglov_ke
 * @author Dan Rubel
 */
public class SWTResourceManager {

	/**
	 * Dispose of cached objects and their underlying OS resources. This should
	 * only be called when the cached objects are no longer needed (e.g. on
	 * application shutdown)
	 */
	public static void dispose() {
		disposeColors();
		disposeFonts();
		disposeImages();
		disposeCursors();
	}

	//////////////////////////////
	// Color support
	//////////////////////////////

	/**
	 * Maps RGB values to colors
	 */
	private static HashMap<RGB, Color> m_ColorMap = new HashMap<RGB, Color>();

	/**
	 * Returns the system color matching the specific ID
	 * 
	 * @param systemColorID
	 *            int The ID value for the color
	 * @return Color The system color matching the specific ID
	 */
	public static Color getColor(final int systemColorID) {
		final Display display = Display.getCurrent();
		return display.getSystemColor(systemColorID);
	}

	/**
	 * Returns a color given its red, green and blue component values
	 * 
	 * @param r
	 *            int The red component of the color
	 * @param g
	 *            int The green component of the color
	 * @param b
	 *            int The blue component of the color
	 * @return Color The color matching the given red, green and blue componet
	 *         values
	 */
	public static Color getColor(final int r, final int g, final int b) {
		return getColor(new RGB(r, g, b));
	}

	/**
	 * Returns a color given its RGB value
	 * 
	 * @param rgb
	 *            RGB The RGB value of the color
	 * @return Color The color matching the RGB value
	 */
	public static Color getColor(final RGB rgb) {
		Color color = m_ColorMap.get(rgb);
		if (color == null) {
			final Display display = Display.getCurrent();
			color = new Color(display, rgb);
			m_ColorMap.put(rgb, color);
		}
		return color;
	}

	/**
	 * Dispose of all the cached colors
	 */
	public static void disposeColors() {
		for (final Color color : m_ColorMap.values()) {
			color.dispose();
		}
		m_ColorMap.clear();
	}

	//////////////////////////////
	// Image support
	//////////////////////////////

	/**
	 * Maps image names to images
	 */
	private static HashMap<String, Image> m_ClassImageMap = new HashMap<String, Image>();

	/**
	 * Maps images to image decorators
	 */
	private static HashMap<Image, HashMap<Image, Image>> m_ImageToDecoratorMap = new HashMap<Image, HashMap<Image, Image>>();

	/**
	 * Style constant for placing decorator image in top left corner of base
	 * image.
	 */
	public static final int TOP_LEFT = 1;
	/**
	 * Style constant for placing decorator image in top right corner of base
	 * image.
	 */
	public static final int TOP_RIGHT = 2;
	/**
	 * Style constant for placing decorator image in bottom left corner of base
	 * image.
	 */
	public static final int BOTTOM_LEFT = 3;
	/**
	 * Style constant for placing decorator image in bottom right corner of base
	 * image.
	 */
	public static final int BOTTOM_RIGHT = 4;

	/**
	 * Dispose all of the cached images
	 */
	public static void disposeImages() {
		for (final Image image : m_ClassImageMap.values()) {
			image.dispose();
		}
		m_ClassImageMap.clear();
		//
		for (final HashMap<Image, Image> decoratedMap : m_ImageToDecoratorMap.values()) {
			for (final Image image : decoratedMap.values()) {
				image.dispose();
			}
		}
	}

	/**
	 * Dispose cached images in specified section
	 * 
	 * @param section
	 *            the section do dispose
	 */
	public static void disposeImages(final String section) {
		for (final Iterator<String> I = m_ClassImageMap.keySet().iterator(); I.hasNext();) {
			final String key = I.next();
			if (!key.startsWith(section + '|')) {
				continue;
			}
			final Image image = m_ClassImageMap.get(key);
			image.dispose();
			I.remove();
		}
	}

	//////////////////////////////
	// Font support
	//////////////////////////////

	/**
	 * Maps font names to fonts
	 */
	private static HashMap<String, Font> m_FontMap = new HashMap<String, Font>();

	/**
	 * Maps fonts to their bold versions
	 */
	private static HashMap<Font, Font> m_FontToBoldFontMap = new HashMap<Font, Font>();

	/**
	 * Returns a font based on its name, height and style
	 * 
	 * @param name
	 *            String The name of the font
	 * @param height
	 *            int The height of the font
	 * @param style
	 *            int The style of the font
	 * @return Font The font matching the name, height and style
	 */
	public static Font getFont(final String name, final int height, final int style) {
		return getFont(name, height, style, false, false);
	}

	/**
	 * Returns a font based on its name, height and style. Windows-specific
	 * strikeout and underline flags are also supported.
	 * 
	 * @param name
	 *            String The name of the font
	 * @param size
	 *            int The size of the font
	 * @param style
	 *            int The style of the font
	 * @param strikeout
	 *            boolean The strikeout flag (warning: Windows only)
	 * @param underline
	 *            boolean The underline flag (warning: Windows only)
	 * @return Font The font matching the name, height, style, strikeout and
	 *         underline
	 */
	public static Font getFont(final String name, final int size, final int style, final boolean strikeout,
			final boolean underline) {
		final String fontName = name + '|' + size + '|' + style + '|' + strikeout + '|' + underline;
		Font font = m_FontMap.get(fontName);
		if (font == null) {
			final FontData fontData = new FontData(name, size, style);
			if (strikeout || underline) {
				try {
					final Class<?> logFontClass = Class.forName("org.eclipse.swt.internal.win32.LOGFONT"); //$NON-NLS-1$
					final Object logFont = FontData.class.getField("data").get(fontData); //$NON-NLS-1$
					if (logFont != null && logFontClass != null) {
						if (strikeout) {
							logFontClass.getField("lfStrikeOut").set(logFont, new Byte((byte) 1)); //$NON-NLS-1$
						}
						if (underline) {
							logFontClass.getField("lfUnderline").set(logFont, new Byte((byte) 1)); //$NON-NLS-1$
						}
					}
				} catch (final Throwable e) {
					System.err
							.println("Unable to set underline or strikeout" + " (probably on a non-Windows platform). " + e); //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
			font = new Font(Display.getCurrent(), fontData);
			m_FontMap.put(fontName, font);
		}
		return font;
	}

	/**
	 * Return a bold version of the give font
	 * 
	 * @param baseFont
	 *            Font The font for whoch a bold version is desired
	 * @return Font The bold version of the give font
	 */
	public static Font getBoldFont(final Font baseFont) {
		Font font = m_FontToBoldFontMap.get(baseFont);
		if (font == null) {
			final FontData fontDatas[] = baseFont.getFontData();
			final FontData data = fontDatas[0];
			font = new Font(Display.getCurrent(), data.getName(), data.getHeight(), SWT.BOLD);
			m_FontToBoldFontMap.put(baseFont, font);
		}
		return font;
	}

	/**
	 * Dispose all of the cached fonts
	 */
	public static void disposeFonts() {
		// clear fonts
		for (final Font font : m_FontMap.values()) {
			font.dispose();
		}
		m_FontMap.clear();
		// clear bold fonts
		for (final Font font : m_FontToBoldFontMap.values()) {
			font.dispose();
		}
		m_FontToBoldFontMap.clear();
	}

	//////////////////////////////
	// CoolBar support
	//////////////////////////////

	/**
	 * Fix the layout of the specified CoolBar
	 * 
	 * @param bar
	 *            CoolBar The CoolBar that shgoud be fixed
	 */
	public static void fixCoolBarSize(final CoolBar bar) {
		final CoolItem[] items = bar.getItems();
		// ensure that each item has control (at least empty one)
		for (final CoolItem item : items) {
			if (item.getControl() == null) {
				item.setControl(new Canvas(bar, SWT.NONE) {
					@Override
					public Point computeSize(final int wHint, final int hHint, final boolean changed) {
						return new Point(20, 20);
					}
				});
			}
		}
		// compute size for each item
		for (final CoolItem item : items) {
			final Control control = item.getControl();
			control.pack();
			final Point size = control.getSize();
			item.setSize(item.computeSize(size.x, size.y));
		}
	}

	//////////////////////////////
	// Cursor support
	//////////////////////////////

	/**
	 * Maps IDs to cursors
	 */
	private static HashMap<Integer, Cursor> m_IdToCursorMap = new HashMap<Integer, Cursor>();

	/**
	 * Returns the system cursor matching the specific ID
	 * 
	 * @param id
	 *            int The ID value for the cursor
	 * @return Cursor The system cursor matching the specific ID
	 */
	public static Cursor getCursor(final int id) {
		final Integer key = new Integer(id);
		Cursor cursor = m_IdToCursorMap.get(key);
		if (cursor == null) {
			cursor = new Cursor(Display.getDefault(), id);
			m_IdToCursorMap.put(key, cursor);
		}
		return cursor;
	}

	/**
	 * Dispose all of the cached cursors
	 */
	public static void disposeCursors() {
		for (final Cursor cursor : m_IdToCursorMap.values()) {
			cursor.dispose();
		}
		m_IdToCursorMap.clear();
	}
}