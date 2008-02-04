package org.eclipse.riena.core;
public class RienaStartupStatus {

	private boolean started = true;
	private static RienaStartupStatus myself = new RienaStartupStatus();

	public static RienaStartupStatus getInstance() {
		return myself;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean startedParm) {
		started = startedParm;
	}

}
