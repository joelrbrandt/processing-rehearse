package edu.stanford.hci.processing;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class ProcessingCanvas extends JFrame {
	BufferedImage image;
	Graphics2D graphics;
	
	public ProcessingCanvas() {
	}
	
	public void paintComponent(Graphics g) {
		System.out.println("paint component");
		super.paintComponents(g);
		((Graphics2D) g).drawImage(image, null, 0, 0);
	}

	public BufferedImage getImage() {
		return image;
	}
	
	public Graphics2D getGraphics() {
		return graphics;
	}
	
	// Nukes the old image.
	public void setImageSize(int width, int height) {
		image = (BufferedImage) this.createImage(width, height);
		graphics = (Graphics2D) image.getGraphics();
//		System.out.println(graphics.getColor());
//		graphics.setPaint(Color.BLUE);
//		System.out.println(graphics.getColor());
//		graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
	}
}
