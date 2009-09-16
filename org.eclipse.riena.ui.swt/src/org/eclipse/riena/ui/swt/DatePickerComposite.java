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
package org.eclipse.riena.ui.swt;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Composite of a DatePicker and a SWT Text widget.
 * 
 * @since 1.2
 */
public class DatePickerComposite extends Composite {

	private final static int BUTTON_WIDTH = 16;
	private final static int BUTTON_HEIGHT = 16;

	private final Text textfield;
	private final Button calendarButton;
	private final DatePicker datePicker;

	public DatePickerComposite(Composite parent, int textStyles) {
		super(parent, SWT.BORDER);

		GridLayoutFactory.fillDefaults().numColumns(2).spacing(0, 0).applyTo(this);
		textfield = new Text(this, checkStyle(textStyles));
		setBackground(getDisplay().getSystemColor(SWT.COLOR_WHITE));
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, true).applyTo(textfield);

		calendarButton = new Button(this, SWT.ARROW | SWT.DOWN);
		calendarButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!datePicker.isVisible()) {
					Point p = textfield.toDisplay(textfield.getLocation().x, textfield.getLocation().y);
					datePicker.setLocation(p.x, p.y + textfield.getBounds().height);
					datePicker.open(parseDateRegex(textfield.getText()));
				} else {
					datePicker.close();
				}
			}
		});

		GridDataFactory.fillDefaults().grab(false, false).align(SWT.RIGHT, SWT.FILL).hint(BUTTON_WIDTH, BUTTON_HEIGHT)
				.applyTo(calendarButton);
		datePicker = new DatePicker(textfield, calendarButton);
	}

	public Text getTextfield() {
		return textfield;
	}

	@Override
	public void dispose() {
		datePicker.dispose();
		super.dispose();
	}

	// helping methods
	//////////////////

	/**
	 * Removes the {@link SWT.BORDER} style, to prevent a awkward representation
	 * 
	 * @param style
	 *            the SWT style bits
	 * @return the style bits without SWT.BORDER
	 */
	private int checkStyle(int style) {
		if ((style & SWT.BORDER) != 0) {
			style &= ~SWT.BORDER;
		}
		return style;
	}

	private Calendar parseDateRegex(String dateString) {
		Calendar result = null;
		Pattern pattern = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+).*?"); //$NON-NLS-1$
		Matcher matcher = pattern.matcher(dateString);

		if (matcher.matches() && matcher.groupCount() == 3) {
			int month = Integer.parseInt(matcher.group(2));
			month -= 1;

			int year = Integer.parseInt(matcher.group(3));
			if (year < 100) {
				year += 1900;
			}

			result = Calendar.getInstance();
			result.set(Calendar.DAY_OF_MONTH, Integer.parseInt(matcher.group(1)));
			result.set(Calendar.MONTH, month);
			result.set(Calendar.YEAR, year);
		}
		return result;
	}

	// helping classes
	//////////////////

	/**
	 * This class shows and hides a DateTime "date picker" on request.
	 */
	private static final class DatePicker {

		private Shell shell;
		private DateTime calendar;
		private final Text text;

		/**
		 * Create a new DatePicker instance.
		 * <p>
		 * You must invoke {@link #dispose()} to give up native resources held
		 * by this class.
		 * 
		 * @param text
		 *            a SWT text field that will received the picked date.
		 * @param pickerButton
		 *            the button that will trigger showing the date picker.
		 */
		protected DatePicker(final Text text, final Button pickerButton) {
			this.text = text;
			// TODO [ev] if you create it, you must dispose it
			// see SWT: Managing system resources - http://www.eclipse.org/articles/swt-design-2/swt-design-2.html
			shell = new Shell(pickerButton.getDisplay(), SWT.NO_TRIM | SWT.ON_TOP);
			shell.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_BLACK));
			GridLayoutFactory.fillDefaults().margins(1, 1).applyTo(shell);

			calendar = new DateTime(shell, SWT.CALENDAR | SWT.SHORT);
			calendar.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
			shell.pack();

			calendar.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseUp(MouseEvent e) {
					// ignore clicks on the header
					if (e.y < 45) {
						return;
					}

					Calendar cal = Calendar.getInstance();
					cal.set(Calendar.YEAR, calendar.getYear());
					cal.set(Calendar.MONTH, calendar.getMonth());
					cal.set(Calendar.DAY_OF_MONTH, calendar.getDay());

					String out = new SimpleDateFormat("dd.MM.yyyy").format(cal.getTime()); //$NON-NLS-1$
					setDateToTextfield(out);
					close();
				}
			});

			calendar.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					Display display = e.widget.getDisplay();
					Control focusControl = display.getCursorControl();
					if (focusControl != pickerButton) {
						close();
					}
				}
			});
		}

		public void dispose() {
			if (!SwtUtilities.isDisposed(shell)) {
				shell.dispose();
			}
		}

		public void setLocation(int x, int y) {
			shell.setLocation(x, y);
		}

		public boolean isVisible() {
			return shell.isVisible();
		}

		public void close() {
			shell.setVisible(false);
		}

		public void open(Calendar newDate) {
			if (isVisible()) {
				return;
			}

			shell.open();
			shell.setVisible(true);

			// if no date was supplied, use the current date
			if (null == newDate) {
				newDate = Calendar.getInstance();
				newDate.setTime(new Date());
			}

			calendar.setYear(newDate.get(Calendar.YEAR));
			calendar.setMonth(newDate.get(Calendar.MONTH));
			calendar.setDay(newDate.get(Calendar.DAY_OF_MONTH));
		}

		private void setDateToTextfield(String dateString) {
			String oldText = text.getText();

			if (oldText.contains(":")) { //$NON-NLS-1$
				Pattern pattern = Pattern.compile("([ \\d\\.]+)\\s+(.*?:.*?)"); //$NON-NLS-1$
				Matcher matcher = pattern.matcher(oldText);

				if (matcher.matches()) {
					String oldTime = matcher.group(2);
					text.setText(dateString + " " + oldTime); //$NON-NLS-1$
				}
			} else {
				text.setText(dateString);
			}
			text.setFocus();
		}
	}
}
