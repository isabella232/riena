package org.eclipse.riena.ui.swt.uiprocess;

import org.eclipse.riena.ui.core.uiprocess.UIProcess;

/**
 * Every {@link UIProcess} can be in different states. Usually the following
 * states should be walked through:
 * 
 * {@value #PENDING}, {@value #RUNNING},{@value #FINISHED}
 */
public enum PROCESS_STATE {
	PENDING, RUNNING, FINISHED, CANCELED
}