package edu.stanford.hci.processing;

import java.awt.Canvas;
import java.awt.Color;
import java.io.*;

import javax.swing.JFrame;

public class ProcessingMethods {
	ProcessingCanvas canvas;
	Reader in;
	PrintStream out;
	PrintStream err;
	
	Color fillColor = null;
	
	public ProcessingMethods (ProcessingCanvas c) {
		this.canvas = c;
	}
	
	public void size(int width, int height) {
		canvas.setSize(width, height);
		canvas.setImageSize(width, height);
	}
	
	public void background(int r, int g, int b) {
		Color c = new Color(r, g, b);
		canvas.setBackground(c);
	}
	
	public void rect(int x, int y, int width, int height) {
		if (fillColor == null)
			canvas.getImageGraphics().drawRect(x, y, width, height);
		else
			canvas.getImageGraphics().fillRect(x, y, width, height);
		canvas.repaint();
	}
	
	public void line(int x1, int y1, int x2, int y2) {
		canvas.getImageGraphics().drawLine(x1, y1, x2, y2);
		canvas.repaint();
	}
	
	public void ellipse(int x, int y, int width, int height) {
		if (fillColor == null)
			canvas.getImageGraphics().drawOval(x, y, width, height);
		else
			canvas.getImageGraphics().fillOval(x, y, width, height);
		canvas.repaint();
	}
	
	public void fill(int x, int y, int z) {
		fillColor = new Color(x, y, z);
		canvas.getImageGraphics().setColor(fillColor);
	}
	
	public void noFill() {
		fillColor = null;
	}
	
	public void println(String s) {
		if (out != null)
			out.println(s);
	}
	
	public void setOut(PrintStream out) {
		this.out = out;
	}
	
	public void setErr(PrintStream err) {
		this.err = err;
	}
}
