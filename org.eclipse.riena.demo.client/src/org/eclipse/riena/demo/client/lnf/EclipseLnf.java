/**
 * 
 */
package org.eclipse.riena.demo.client.lnf;

import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;

/**
 * @author christian
 *
 */
public class EclipseLnf extends RienaDefaultLnf {
	
	/**
	 * ID of this Look and Feel
	 */
	private final static String LNF_ID = "EclipseLnf"; //$NON-NLS-1$

	/**
	 * Creates a new instance of {@code ExampleLnf}
	 */
	public EclipseLnf() {
		super();
		setTheme(new EclipseTheme());
	}

	/**
	 * @see org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf#getLnfId()
	 */
	@Override
	protected String getLnfId() {
		return LNF_ID;
	}


}
