package org.eclipse.riena.internal.ui.ridgets.swt;

import java.net.URL;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.riena.ui.ridgets.ILabelRidget;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Label;

/**
 * Ridget for an SWT {@link Label} widget.
 */
public class LabelRidget extends AbstractValueRidget implements ILabelRidget {

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$
	private static Image missingImage;

	private String text = EMPTY_STRING;
	private String icon;
	private URL iconLocation;

	public LabelRidget() {
		this(null);
	}

	public LabelRidget(Label label) {
		setUIControl(label);
	}

	/**
	 * @deprecated use BeansObservables.observeValue(ridget instance,
	 *             ILabelRidget.PROPERTY_TEXT);
	 */
	public IObservableValue getRidgetObservable() {
		return BeansObservables.observeValue(this, ILabelRidget.PROPERTY_TEXT);
	}

	@Override
	protected void checkUIControl(Object uiControl) {
		AbstractSWTRidget.assertType(uiControl, Label.class);
	}

	@Override
	protected void bindUIControl() {
		updateTextInControl();
	}

	@Override
	protected void unbindUIControl() {
		// unused
	}

	@Override
	public Label getUIControl() {
		return (Label) super.getUIControl();
	}

	public String getIcon() {
		return icon;
	}

	public URL getIconLocation() {
		return iconLocation;
	}

	public String getText() {
		return text;
	}

	public void setIcon(String icon) {
		String oldIcon = this.icon;
		this.icon = icon;
		if (hasChanged(oldIcon, icon)) {
			updateIconInControl();
		}

	}

	public void setIconLocation(URL location) {
		URL oldUrl = this.iconLocation;
		this.iconLocation = location;
		if (hasChanged(oldUrl, location)) {
			updateIconInControl();
		}
	}

	public void setText(String text) {
		String oldValue = text;
		this.text = text;
		updateTextInControl();
		firePropertyChange(ILabelRidget.PROPERTY_TEXT, oldValue, this.text);
	}

	// helping methods
	// ////////////////

	private void updateTextInControl() {
		Label control = getUIControl();
		if (control != null) {
			control.setText(text);
		}
	}

	private void updateIconInControl() {
		Label control = getUIControl();
		if (control != null) {
			Image image = null;
			if (icon != null) {
				image = getManagedImage(icon);
			} else if (iconLocation != null) {
				String key = iconLocation.toExternalForm();
				image = getManagedImage(key);
			}
			control.setImage(image);
		}
	}

	private Image getManagedImage(String key) {
		Activator activator = Activator.getDefault();
		Image result = null;
		if (activator != null) {
			ImageRegistry registry = activator.getImageRegistry();
			result = registry.get(key);
			if (result == null) {
				ImageDescriptor descr = ImageDescriptor.createFromURL(iconLocation);
				registry.put(key, descr);
				result = registry.get(key);
			}
		} else {
			result = getMissingImage();
		}
		return result;
	}

	private synchronized Image getMissingImage() {
		if (missingImage == null) {
			missingImage = ImageDescriptor.getMissingImageDescriptor().createImage();
		}
		return missingImage;
	}

	private boolean hasChanged(String oldValue, String newValue) {
		return (oldValue == null && newValue != null) || (oldValue != null && newValue == null)
				|| oldValue.equals(newValue);
	}

	private boolean hasChanged(URL oldValue, URL newValue) {
		// avoid URL.equals(...) since it opens a network connection :(
		if ((oldValue == null && newValue != null) || (oldValue != null && newValue == null)) {
			return true;
		}
		String str1 = oldValue.toExternalForm();
		String str2 = newValue.toExternalForm();
		return str1.equals(str2);
	}

}
