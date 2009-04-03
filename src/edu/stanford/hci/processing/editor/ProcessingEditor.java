package edu.stanford.hci.processing.editor;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class ProcessingEditor extends JFrame {

	public ProcessingEditor() {
		super();
		JTextArea textarea = new JTextArea();
		this.add(textarea);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ProcessingEditor editor = new ProcessingEditor();
		editor.pack();
		editor.setVisible(true);
	}

}
