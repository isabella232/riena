package org.eclipse.riena.example.client.model;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Eine normale Meldung. @see MessageData
 * 
 */
public class Message extends AbstractMessage {
	/**
	 * Index des "Ja"-Buttons.
	 */
	public final static int OK_OPTION = 0;

	/**
	 * Index des "Ja"-Buttons.
	 */
	public final static int YES_OPTION = 0;

	/**
	 * Index des "Nein"-Buttons.
	 */
	public final static int NO_OPTION = 1;

	/**
	 * Index des "Abbrechen"-Buttons.
	 */
	public final static int CANCEL_OPTION = 2;

	private MessageData messageData;

	/**
	 * @param mex
	 * @param messageType
	 * @param meldungsText
	 * @param infoText
	 * @param hinweis
	 * @param ansprache
	 * @param buttons
	 */
	public Message(String mex, MessageType messageType, String meldungsText, String infoText, String hinweis,
			String ansprache, String[] buttons) {
		super();

		messageData = new MessageData(mex, messageType, meldungsText, infoText, hinweis, ansprache, buttons);
	}

	static String format(String text, Object[] args) {
		String formattedText = null;

		if (args == null) {
			formattedText = text;
		} else {
			String transformedText = transformText(text);
			formattedText = MessageFormat.format(transformedText, args);
		}

		return formattedText;
	}

	private static String transformText(String text) {
		List<String> parameters = null;

		parameters = new ArrayList();
		Pattern pattern = Pattern.compile("\\$([\\S]+)");
		Matcher matcher = pattern.matcher(text);

		while (matcher.find()) {
			String parameter = matcher.group(1);
			if (!parameters.contains(parameter)) {
				parameters.add(parameter);
			}
		}

		for (int i = 0; i < parameters.size(); i++) {
			String parameter = parameters.get(i);
			text = text.replaceAll("\\$" + parameter, "{" + i + "}");
		}

		return text;
	}

	protected MessageData getMessageData() {
		return messageData;
	}
}
