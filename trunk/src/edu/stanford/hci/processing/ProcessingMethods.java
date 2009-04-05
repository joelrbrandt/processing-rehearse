package edu.stanford.hci.processing;

import java.awt.Canvas;
import java.awt.Color;
import java.io.*;

import javax.swing.JFrame;

public class ProcessingMethods {
	Canvas canvas;
	JFrame frame;
	Reader in;
	PrintStream out;
	PrintStream err;
	
	public ProcessingMethods (Canvas c, JFrame frame) {
		this.canvas = c;
		this.frame = frame;
	}
	
	public void size(int width, int height) {
		canvas.setSize(width, height);
		frame.pack();
	}
	
	public void background(int r, int g, int b) {
		Color c = new Color(r, g, b);
		canvas.setBackground(c);
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
