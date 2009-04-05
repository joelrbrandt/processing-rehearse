package edu.stanford.hci.processing;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class ProcessingCanvas extends Canvas {
	BufferedImage image;
	Graphics2D graphics;
	
	public ProcessingCanvas() {
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		System.out.println("paint component");
		if (image != null) {
			((Graphics2D) g).drawImage(image, null, 0, 0);
		}
	}

	public BufferedImage getImage() {
		return image;
	}
	
	public Graphics2D getImageGraphics() {
		return graphics;
	}
	
	// Nukes the old image.
	public void setImageSize(int width, int height) {
		//image = (BufferedImage) this.createImage(width, height);
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		graphics = image.createGraphics();
		graphics.setPaint(Color.WHITE);
		graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
		graphics.setPaint(Color.BLACK);
	}
}
