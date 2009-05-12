package edu.stanford.hci.processing.editor;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

public class RehearseImageViewer extends JFrame {
	final List<Image> imageList;
	List<String> descriptions;
	int currImage = 0;
	JButton next;
	JButton previous;
	JPanel viewer;
	
	public RehearseImageViewer(List<Image> images) {
		super();
		this.imageList = images;
		setSize(750, 500);
		viewer = new JPanel() {
			public void paint(Graphics g) {
				BufferedImage i = (BufferedImage) imageList.get(currImage);
				
// This was just for debugging but I think we'll need it again...			
//				try {  
//				     ImageIO.write(i, "jpg", new File("C:\\test" + currImage 
//				    		 		+ ".jpg"));  
//				} catch (IOException e) {  
//				     e.printStackTrace();  
//				}  
				g.drawImage(i, 0, 0, i.getHeight(null), 
						i.getWidth(null), null);
			}
		};
		viewer.setSize(500, 500);
		getContentPane().add(viewer, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		next = new JButton("Next");
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				currImage++;
				if (currImage >= imageList.size()) {
					currImage = 0;
				}
				repaint();
			}
		});
		previous = new JButton("Previous");
		previous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				currImage--;
				if (currImage < 0) {
					currImage = imageList.size() - 1;
				}
				repaint();
			}
		});
		buttonPanel.add(previous);
		buttonPanel.add(next);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	}
}
