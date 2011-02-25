package org.eclipse.riena.ui.swt.lnf;

import org.eclipse.riena.core.singleton.SessionSingletonProvider;
import org.eclipse.riena.core.singleton.SingletonProvider;

/**
 * Accessor for the {@link LnFUpdater} session singleton instance.
 */
public class LnfUpdaterAccessor {

	private static final SingletonProvider<LnFUpdater> LNFU = new SessionSingletonProvider<LnFUpdater>(LnFUpdater.class);

	/**
	 * @return the {@link LnFUpdater} instance bound to the current session
	 */
	public static LnFUpdater getInstance() {
		return LNFU.getInstance();
	}

}
