package org.eclipse.riena.internal.ui.ridgets.swt.uiprocess;

import org.eclipse.riena.ui.core.uiprocess.IProgressVisualizer;
import org.eclipse.riena.ui.core.uiprocess.UIProcess;
import org.eclipse.riena.ui.swt.uiprocess.PROCESS_STATE;
import org.eclipse.riena.ui.swt.uiprocess.ProgressInfoDataObject;

/**
 * class holding detail information about a running {@link UIProcess}
 */
public class ProcessDetail implements Comparable<ProcessDetail> {

	static volatile int keyDec = 0;

	static int nextKey() {
		return keyDec++;
	}

	// the maximum value a pending process can reach
	private final int PENDING_MAXWORK = 100;
	private final int PENDING_STEP = 10;

	IProgressVisualizer visualizer;

	// state
	private PROCESS_STATE state = PROCESS_STATE.PENDING;

	// steady changing progress when in state pending (triggered by update thread)
	private int pendingProgress = 0;

	//state of work
	private int progress;

	// startup time in millis [TS = time stamp]
	private long startupTS;

	private final int key = nextKey();

	public ProcessDetail(long startupTS, IProgressVisualizer visualizer) {
		this.startupTS = startupTS;
		this.visualizer = visualizer;
	}

	public IProgressVisualizer getVisualizer() {
		return visualizer;
	}

	/**
	 * @return - the time the {@link IProgressVisualizer} was first "seen"
	 */
	public long getStartupTS() {
		return startupTS;
	}

	public boolean isPending() {
		return PROCESS_STATE.PENDING.equals(state);
	}

	/**
	 * called by timer when {@link #isPending()} == true
	 */
	public void triggerPending() {
		assert isPending() : ":-( triggerPending called in working state"; //$NON-NLS-1$
		if (pendingProgress <= PENDING_MAXWORK) {
			// go on
			pendingProgress += PENDING_STEP;
		} else {
			// reset
			pendingProgress = 0;
		}
	}

	/**
	 * @return - depends on {@link #isPending()} if {@link #totalWork} or
	 *         {@link #PENDING_MAXWORK} is returned
	 */
	public int getMaxValue() {
		return isPending() ? PENDING_MAXWORK : getVisualizer().getProcessInfo().getMaxProgress();
	}

	/**
	 * @return - depends on {@link #isPending()} if {@link #pendingProgress} or
	 *         {@link #progress} is returned
	 */
	public int getValue() {
		return isPending() ? pendingProgress : progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	/**
	 * sets the state of the process
	 * 
	 * @param state
	 */
	public void setState(PROCESS_STATE state) {
		this.state = state;
	}

	/**
	 * {@link ProcessDetail} instances have a special order in the
	 * {@link ProcessDetailManager}. The follwing is the rule:
	 */
	public int compareTo(ProcessDetail other) {
		return ((Long) other.startupTS).compareTo((Long) startupTS);
	}

	private int calculatePercentage() {
		return calculatePercentage(getValue(), getMaxValue());
	}

	private static int calculatePercentage(int ivalue, int imaxValue) {
		double dmaxValue = (double) imaxValue;
		double dValue = (double) ivalue;
		return (int) ((dValue / dmaxValue) * 100);
	}

	/**
	 * 
	 * @return - a data object describing the {@link ProcessDetail}
	 */
	public ProgressInfoDataObject toPido() {
		return new ProgressInfoDataObject(key, getMaxValue(), calculatePercentage(), getVisualizer().getProcessInfo()
				.getTitle(), state);
	}

}