package edu.stanford.hci.processing.editor;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

public class SnapshotModel {

	private Image image;
	private Map<String, String> variableMap;
	private int lineNum;
	
	public SnapshotModel() {
		variableMap = new HashMap<String, String>();
	}
	
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public Map<String, String> getVariableMap() {
		return variableMap;
	}
	
	public void setVariableMap(Map<String, String> variableMap) {
		this.variableMap = variableMap;
	}
	public int getLineNum() {
		return lineNum;
	}
	public void setLineNum(int lineNum) {
		this.lineNum = lineNum;
	}
	
	
}
