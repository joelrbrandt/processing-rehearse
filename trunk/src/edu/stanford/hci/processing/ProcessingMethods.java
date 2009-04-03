package edu.stanford.hci.processing;

import java.awt.Canvas;
import java.awt.Color;

public class ProcessingMethods {
	Canvas canvas;
	public ProcessingMethods (Canvas c) {
		this.canvas = c;
	}
	
	public void size(int width, int height) {
		System.out.println("HI GUYS SIZE GOT CALLED");
		canvas.setSize(width, height);
	}
	
	public void background(int r, int g, int b) {
		Color c = new Color(r, g, b);
		canvas.setBackground(c);
	}
}
