package org.eclipse.riena.ui.ridgets;

import org.eclipse.riena.ui.ridgets.listener.IWindowRidgetListener;

public interface IWindowRidget extends IRidget {

	void addWindowRidgetListener(IWindowRidgetListener pListener);

	void removeWindowRidgetListener(IWindowRidgetListener pListener);

	void setTitle(String title);

	/**
	 * set the icon.
	 * 
	 * @param icon
	 *            The icon name.
	 */
	void setIcon(String icon);

	/**
	 * Sets the default button.
	 * 
	 * @param defaultButton
	 *            - default button
	 */
	void setDefaultButton(Object defaultButton);

	/**
	 * Answer the windows defaultButton or null
	 * 
	 * @return
	 */
	Object getDefaultButton();

	void setCloseable(boolean closeable);

	void setActive(boolean active);
}
