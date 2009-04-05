package edu.stanford.hci.processing;

import java.awt.Canvas;
import java.awt.Color;

import javax.swing.JFrame;

public class ProcessingMethods {
	Canvas canvas;
	JFrame frame;
	
	public ProcessingMethods (Canvas c, JFrame frame) {
		this.canvas = c;
		this.frame = frame;
	}
	
	public void size(int width, int height) {
		System.out.println("HI GUYS SIZE GOT CALLED");
		canvas.setSize(width, height);
		frame.pack();
	}
	
	public void background(int r, int g, int b) {
		Color c = new Color(r, g, b);
		canvas.setBackground(c);
	}
}
