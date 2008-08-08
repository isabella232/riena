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
package org.eclipse.riena.ui.ridgets.marker;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.eclipse.riena.ui.core.marker.IMessageMarker;
import org.eclipse.riena.ui.ridgets.IMarkableRidget;

/**
 * Visualizes certain types of message markers by setting the tooltip of the
 * Ridget.
 */
public class TooltipMessageMarkerViewer extends AbstractMessageMarkerViewer {

	private PropertyChangeListener markerPropertyChangeListener = new MarkerPropertyChangeListener();
	private HashMap<IMarkableRidget, String> tooltipMessage = new LinkedHashMap<IMarkableRidget, String>();
	private HashMap<IMarkableRidget, String> originalTooltipMessage = new LinkedHashMap<IMarkableRidget, String>();

	/**
	 * @see org.eclipse.riena.ui.ridgets.marker.AbstractMessageMarkerViewer#addRidget(org.eclipse.riena.ui.ridgets.IMarkableRidget)
	 */
	@Override
	public void addRidget(IMarkableRidget markableRidget) {
		super.addRidget(markableRidget);
		markableRidget.addPropertyChangeListener(markerPropertyChangeListener);
	}

	protected void showMessages(IMarkableRidget markableRidget) {
		Collection messageMarker = this.getMessageMarker(markableRidget);
		String message = constructMessage(messageMarker).trim();
		// show the message only if there is something to show
		if (message.length() > 0 && isVisible()) {
			showMessages(markableRidget, message);
		} else {
			hideMessages(markableRidget);
		}
	}

	private void showMessages(IMarkableRidget markableRidget, String message) {
		if (tooltipMessage.get(markableRidget) == null) {
			String tooltiptext = markableRidget.getToolTipText();
			if (tooltiptext != null) {
				if (!message.equals(tooltiptext.trim())) {
					originalTooltipMessage.put(markableRidget, tooltiptext.trim());
				}
			} else {
				originalTooltipMessage.put(markableRidget, null);
			}
		}
		markableRidget.setToolTipText(message);
		tooltipMessage.put(markableRidget, message);
	}

	protected void hideMessages(IMarkableRidget markableRidget) {
		markableRidget.setToolTipText(originalTooltipMessage.get(markableRidget));
		tooltipMessage.put(markableRidget, null);
	}

	private String constructMessage(Collection messageMarker) {
		StringWriter sw = new StringWriter();
		IMessageMarker nextMarker = null;
		if (messageMarker != null) {
			for (Iterator i = messageMarker.iterator(); i.hasNext();) {
				nextMarker = (IMessageMarker) i.next();
				if (sw.toString().trim().length() > 0) {
					sw.write("; "); //$NON-NLS-1$
				}
				if (nextMarker.getMessage() != null) {
					sw.write(nextMarker.getMessage());
				}
			}
		}
		return sw.toString().trim();
	}

	private class MarkerPropertyChangeListener implements PropertyChangeListener {

		/**
		 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
		 */
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(IMarkableRidget.PROPERTY_MARKER)
					&& evt.getSource() instanceof IMarkableRidget) {
				showMessages((IMarkableRidget) evt.getSource());
			}
		}

	}

}
