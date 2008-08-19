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
package org.eclipse.riena.ui.ridgets;

/**
 * Adapter for the message box. Optionally to the optionTpye, which specifies
 * that a set of standard buttons should be used fot the message box, a couple
 * of used defined buttons can be specified. The array could contain a set of
 * strings or a set of MessageBoxButtonWrapper objects, where the latter carry
 * the names and mnemonics of the user defined buttons. The first entry always
 * specifies die default button.
 * 
 * @author Thorsten Schenkel
 * @author Erich Achilles
 * @author Carsten Drossel
 */

/**
 * @deprecated
 */
@Deprecated
public interface IMessageBoxAdapter extends IAdapter {

	/**
	 * <code>PROPERTY_ICON</code>
	 */
	String PROPERTY_ICON = "icon"; //$NON-NLS-1$
	/**
	 * <code>PROPERTY_RET_VALUE</code>
	 */
	String PROPERTY_RET_VALUE = "retValue"; //$NON-NLS-1$
	/**
	 * <code>PROPERTY_SHOW_MESSAGE_BOX</code>
	 */
	String PROPERTY_SHOW_MESSAGE_BOX = "showMessageBox"; //$NON-NLS-1$
	/**
	 * <code>PROPERTY_MESSAGE_TYPE</code>
	 */
	String PROPERTY_MESSAGE_TYPE = "messageType"; //$NON-NLS-1$
	/**
	 * <code>PROPERTY_MESSAGE_TEXT</code>
	 */
	String PROPERTY_MESSAGE_TEXT = "messageText"; //$NON-NLS-1$
	/**
	 * <code>PROPERTY_MESSAGE_TITLE</code>
	 */
	String PROPERTY_MESSAGE_TITLE = "messageTitle"; //$NON-NLS-1$
	/**
	 * <code>PROPERTY_HELP_TEXT</code>
	 */
	String PROPERTY_HELP_TEXT = "helpText"; //$NON-NLS-1$
	/**
	 * <code>PROPERTY_OPTION_TYPE</code>
	 */
	String PROPERTY_OPTION_TYPE = "optionType"; //$NON-NLS-1$
	/**
	 * <code>PROPERTY_OPTIONS</code>
	 */
	String PROPERTY_OPTIONS = "options"; //$NON-NLS-1$
	/**
	 * <code>PROPERTY_MESSAGE_NUMBER</code>
	 */
	String PROPERTY_MESSAGE_NUMBER = "messageNumber"; //$NON-NLS-1$
	/**
	 * return value if YES is chosen.
	 */
	int YES_BTN = 0;
	/**
	 * return value if NO is chosen.
	 */
	int NO_BTN = 1;
	/**
	 * return value if OK is chosen.
	 */
	int OK_BTN = 0;
	/**
	 * return value if CANCEL is chosen.
	 */
	int CANCEL_BTN = 2;
	/**
	 * Return value if user closes window without selecting anything.
	 */
	int CLOSED_BTN = -1;
	/**
	 * information
	 */
	int INFORMATION_MESSAGE = 1;
	/**
	 * warning
	 */
	int WARNING_MESSAGE = 2;
	/**
	 * error message
	 */
	int ERROR_MESSAGE = 3;
	/**
	 * help message
	 */
	int HELP_MESSAGE = 4;
	/**
	 * question message
	 */
	int QUESTION_MESSAGE = 5;
	/**
	 * plain message (no icon)
	 */
	int PLAIN_MESSAGE = 6;
	/**
	 * button OK appears.
	 */
	int OK_OPTION = 1;
	/**
	 * button OK and Cancel appear.
	 */
	int OK_CANCEL_OPTION = 2;
	/**
	 * button Yes and No appear.
	 */
	int YES_NO_OPTION = 3;
	/**
	 * button Yes, No and Cancel appear.
	 */
	int YES_NO_CANCEL_OPTION = 4;
	/**
	 * no standard option specified, user definded options used.
	 */
	int UNKNOWN_OPTION = -1;

	/**
	 * Shows the message box.
	 */
	void showMessageBox();

	/**
	 * Return the help text.
	 * 
	 * @return help text.
	 * 
	 * @see #setHelpText(String)
	 */
	String getHelpText();

	/**
	 * Sets the help text. An additional button is available in the message box,
	 * if the help text is set. <br>
	 * If the user pushs this button, another dialog pops up and displays the
	 * help text.
	 * 
	 * @param helpText
	 *            - if the <code>helpText</code> is not null an additional
	 *            button, the help button is available.
	 */
	void setHelpText(String helpText);

	/**
	 * Returns the message number, that is displayed in the title of the message
	 * box.
	 * 
	 * @return message number
	 */
	String getMessageNumber();

	/**
	 * Sets the message number, that is display in the title of the message box.
	 * 
	 * @param messageNumber
	 *            - the message number is displayed in the title.
	 */
	void setMessageNumber(String messageNumber);

	/**
	 * Returns the text of the message box.
	 * 
	 * @return title
	 */
	String getMessageTitle();

	/**
	 * Sets the title the message box.
	 * 
	 * @param title
	 *            - text to display in the title bar of the message box
	 */
	void setMessageTitle(String title);

	/**
	 * Returns the text, that is displayed inside the message box.
	 * 
	 * @return message text
	 */
	String getMessageText();

	/**
	 * Sets the text, that is displayed inside the message box.
	 * 
	 * @param messageText
	 *            - the text to display.
	 */
	void setMessageText(String messageText);

	/**
	 * Returns the type of the message.
	 * 
	 * @return message type
	 * 
	 * @see #setMessageType(int)
	 */
	int getMessageType();

	/**
	 * Set the type of message.
	 * 
	 * @param messageType
	 *            - the type of message: <code>INFORMATION_MESSAGE</code>,
	 *            <code>WARNING_MESSAGE</code> or <code>ERROR_MESSAGE</code>.
	 * 
	 * @pre messageType messageType == INFORMATION_MESSAGE || messageType ==
	 *      WARNING_MESSAGE || messageType == ERROR_MESSAGE || messageType ==
	 *      HELP_MESSAGE
	 */
	void setMessageType(int messageType);

	/**
	 * Returns the option type. The option type designates the available
	 * buttons.
	 * 
	 * @return option type
	 * 
	 * @see #setOptionType(int)
	 */
	int getOptionType();

	/**
	 * The return value depends on the kind of buttons used for this messagebox.
	 * If only standard buttons are used, on of the standard identifiers (
	 * YES_BTN, NO_BTN.... ) is returned. If user defined buttons are used, the
	 * return value corresponds to the index of the selected buttons
	 * buttondefinition in the options array.
	 * 
	 * @return the return value.
	 */
	int getReturnValue();

	/**
	 * Set the <code>returnValue</code>.
	 * 
	 * @param preturnValue
	 */
	void setReturnValue(int preturnValue);

	/**
	 * Sets the option type. The option type designates the available buttons.
	 * 
	 * @param optionType
	 *            - an integer designating the buttons available on the dialog:
	 *            <code>OK_OPTION</code>,<code>OK_CANCEL_OPTION</code>,
	 *            <code>YES_NO_OPTION</code> or
	 *            <code>YES_NO_CANCEL_OPTION</code>.
	 * 
	 * @pre optionType == OK_OPTION || optionType == OK_CANCEL_OPTION ||
	 *      optionType == YES_NO_OPTION || optionType == YES_NO_CANCEL_OPTION
	 */
	void setOptionType(int optionType);

	/**
	 * Sets the choices array. The choices array type designates the available
	 * buttons. The first entry always specifies die default button.
	 * 
	 * @param options
	 *            - a list of available choices. maybe strings, components,
	 *            icons, or other.
	 * 
	 */
	void setOptions(Object[] options);

	/**
	 * Returns the choices the user can make . The choices array designates the
	 * available buttons.
	 * 
	 * @return options the array of choices
	 * 
	 * @see #setOptions(Object[])
	 */
	Object[] getOptions();

	/**
	 * get the icon.
	 * 
	 * @return Returns the icon name.
	 */
	String getIcon();

	/**
	 * set the icon.
	 * 
	 * @param icon
	 *            The icon name.
	 */
	void setIcon(String icon);

}
