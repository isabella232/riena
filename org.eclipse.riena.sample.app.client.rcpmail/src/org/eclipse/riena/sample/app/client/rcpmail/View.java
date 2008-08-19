package org.eclipse.riena.sample.app.client.rcpmail;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.riena.ui.ridgets.swt.SwtBindingDelegate;
import org.eclipse.riena.ui.ridgets.viewcontroller.IController;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public class View extends ViewPart {

	public static final String ID = "org.eclipse.riena.sample.app.client.rcpmail.view";
	
	private SwtBindingDelegate delegate = new SwtBindingDelegate();
	private IController controller = new ViewController();
	
	public void createPartControl(Composite parent) {
		Composite top = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		top.setLayout(layout);
		// top banner
		Composite banner = new Composite(top, SWT.NONE);
		banner.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL, GridData.VERTICAL_ALIGN_BEGINNING, true, false));
		layout = new GridLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 10;
		layout.numColumns = 2;
		banner.setLayout(layout);
		
		// setup bold font
		Font boldFont = JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT);    
		
		Label l = new Label(banner, SWT.WRAP);
		l.setText("Subject:");
		l.setFont(boldFont);
		delegate.addUIControl(new Label(banner, SWT.WRAP), "subject");
		
		l = new Label(banner, SWT.WRAP);
		l.setText("From:");
		l.setFont(boldFont);
		delegate.addUIControl(new Label(banner, SWT.WRAP), "from");
    
		l = new Label(banner, SWT.WRAP);
		l.setText("Date:");
		l.setFont(boldFont);
		delegate.addUIControl(new Label(banner, SWT.WRAP), "date");

		// message contents
		Text text = new Text(top, SWT.MULTI | SWT.WRAP);
		text.setLayoutData(new GridData(GridData.FILL_BOTH));
		delegate.addUIControl(text, "message");
		
		delegate.injectAndBind(controller);
		parent.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				delegate.unbind(controller);
			}
		});
	}

	public void setFocus() {
	}
}
