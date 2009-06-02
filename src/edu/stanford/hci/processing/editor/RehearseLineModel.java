package edu.stanford.hci.processing.editor;

/**
 * A class that encapsulates all data associated with one line in the
 * editor. 
 * 
 * @author William Choi
 *
 */
public class RehearseLineModel {

	public boolean isPrintPoint;
	public boolean executedInLastRun;
	public boolean isMostRecentlyExecuted;
	
	public RehearseLineModel() {
		isPrintPoint = false;
		executedInLastRun = false;
		isMostRecentlyExecuted = false;
	}
	
}
