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

import java.beans.EventHandler;

import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Item;

/**
 * Ridget of a menu item.
 */
public abstract class AbstractItemRidget extends AbstractSWTWidgetRidget implements IActionRidget {

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private Item item;
	private String text;
	private String icon;
	private ActionObserver actionObserver;
	private boolean textAlreadyInitialized;
	private boolean useRidgetIcon;
	private AbstractItemProperties itemProperties;
	private String itemId;

	/**
	 * Creates a new instance of {@link AbstractItemRidget}.
	 */
	public AbstractItemRidget() {
		actionObserver = new ActionObserver();
		textAlreadyInitialized = false;
		useRidgetIcon = false;
	}

	/**
	 * Creates a class that stores all properties of the given item.
	 * 
	 * @return item properties
	 */
	abstract AbstractItemProperties createProperties();

	@Override
	protected void bindUIControl() {
		Item control = getUIControl();
		if (control != null) {
			item = control;
			itemId = super.getID();
			initText();
			updateUIText();
			updateUIIcon();
			setItemProperties(createProperties());

		}
	}

	@Override
	public final String getID() {
		String idString = super.getID();
		if (StringUtils.isEmpty(idString)) {
			idString = itemId;
		}
		return idString;
	}

	/**
	 * If the text of the ridget has no value, initialize it with the text of
	 * the UI control.
	 */
	private void initText() {
		if ((text == null) && (!textAlreadyInitialized)) {
			if ((getUIControl()) != null && !(getUIControl().isDisposed())) {
				text = getUIControl().getText();
				if (text == null) {
					text = EMPTY_STRING;
				}
				textAlreadyInitialized = true;
			}
		}
	}

	@Override
	protected void unbindUIControl() {
		item = null;
	}

	@Override
	public Item getUIControl() {
		return (Item) super.getUIControl();
	}

	public final void addListener(IActionListener listener) {
		getActionObserver().addListener(listener);
	}

	public final void addListener(Object target, String action) {
		addListener(EventHandler.create(IActionListener.class, target, action));
	}

	public final void removeListener(IActionListener listener) {
		getActionObserver().removeListener(listener);
	}

	/**
	 * Always returns true because mandatory markers do not make sense for this
	 * ridget.
	 */
	@Override
	public boolean isDisableMandatoryMarker() {
		return true;
	}

	public String getIcon() {
		return icon;
	}

	public final String getText() {
		return text;
	}

	public void setIcon(String icon) {
		boolean oldUseRidgetIcon = useRidgetIcon;
		useRidgetIcon = true;
		String oldIcon = this.icon;
		this.icon = icon;
		if (hasChanged(oldIcon, icon) || !oldUseRidgetIcon) {
			updateUIIcon();
		}
	}

	public final void setText(String newText) {
		this.text = newText;
		updateUIText();
	}

	// helping methods
	// ////////////////

	/**
	 * Updates (sets) the text of the menu item.
	 */
	private void updateUIText() {
		if (item != null) {
			item.setText(text);
		}
	}

	/**
	 * Updates (sets) the icon of the menu item.
	 */
	private void updateUIIcon() {
		Item control = getUIControl();
		if (control != null) {
			Image image = null;
			if (getIcon() != null) {
				image = getManagedImage(getIcon());
			}
			if ((image != null) || useRidgetIcon) {
				control.setImage(image);
			}
		}
	}

	/**
	 * Does nothing; item's don't have a tooltip.
	 */
	@Override
	protected void updateToolTip() {
		// does nothing
	}

	protected ActionObserver getActionObserver() {
		return actionObserver;
	}

	private void setItemProperties(AbstractItemProperties itemProperties) {
		this.itemProperties = itemProperties;
	}

	AbstractItemProperties getItemProperties() {
		return itemProperties;
	}

	/**
	 * Creates a new item on base of the stored item properties.
	 */
	void createItem() {
		Item item = getUIControl();
		if ((item == null) || (item.isDisposed())) {
			getItemProperties().createItem();
		}
	}

}
