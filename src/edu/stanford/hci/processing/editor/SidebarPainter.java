package edu.stanford.hci.processing.editor;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.text.PlainDocument;

import processing.app.Base;
import processing.app.syntax.JEditTextArea;
import processing.app.syntax.TextAreaDefaults;
import processing.app.syntax.TextAreaPainter;
import processing.app.syntax.TokenMarker;

public class SidebarPainter extends JComponent {

	private TextAreaPainter textAreaPainter;
	private JEditTextArea textArea;
	private Set<Integer> highlightedPoints;
	
	public SidebarPainter(JEditTextArea textArea, TextAreaPainter textAreaPainter) {
		this.textArea = textArea;
		this.textAreaPainter = textAreaPainter;
		this.highlightedPoints = new HashSet<Integer>();
		setAutoscrolls(true);
		setDoubleBuffered(true);
		setOpaque(true);
		this.addMouseListener(new MouseEventHandler());
	}
	
	public void paint(Graphics gfx)
	  {
	    Rectangle clipRect = gfx.getClipBounds();

	    gfx.setColor(Color.black);
	    gfx.fillRect(clipRect.x,clipRect.y,clipRect.width,clipRect.height);
	  
	    // We don't use yToLine() here because that method doesn't
	    // return lines past the end of the document
	    int height = textAreaPainter.getFontHeight();
	    int firstLine = textArea.getFirstLine();
	    int firstInvalid = firstLine + clipRect.y / height;
	    // Because the clipRect's height is usually an even multiple
	    // of the font height, we subtract 1 from it, otherwise one
	    // too many lines will always be painted.
	    int lastInvalid = firstLine + (clipRect.y + clipRect.height - 1) / height;

	    for (int line = firstInvalid; line <= lastInvalid; line++) {
	        Color c = (highlightedPoints.contains(line)) ? Color.red : Color.gray;
	        gfx.setColor(c);
		    int y = textArea.lineToY(line);
	        gfx.fillRect(0, y + 3, getWidth(), height);
	    }
	    
	    int h = clipRect.y + clipRect.height;
        repaint(0,h,getWidth(),getHeight() - h);
	  }
	
	class MouseEventHandler extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			int height = textAreaPainter.getFontHeight();
			int line = textArea.getFirstLine() + e.getY() / height;
			System.out.println(line);
			if (highlightedPoints.contains(line)) {
				highlightedPoints.remove(line);
			} else {
				highlightedPoints.add(line);
			}
			SidebarPainter.this.repaint();
		}
	}
}
