package edu.stanford.hci.processing.editor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;

public class RehearseImageViewer extends JFrame implements ChangeListener {
	private List<SnapshotModel> snapshots;
	int currImage = 0;
	JButton next;
	JButton previous;
	JPanel viewer;

	private JSlider snapshotSlider;
	private JTable variableTable;
	private SnapshotImageViewer imageViewer;

	public RehearseImageViewer(List<SnapshotModel> snapshots) {
		super();
		setSize(750, 600);
		this.snapshots = snapshots;
		getContentPane().setLayout(new BorderLayout());

		snapshotSlider = new JSlider(JSlider.HORIZONTAL, 0, snapshots.size() - 1, 0);
		snapshotSlider.setPreferredSize(new Dimension(700, 100));
		snapshotSlider.setMajorTickSpacing(1);
		snapshotSlider.setPaintTicks(true);
		snapshotSlider.setSnapToTicks(true);
		snapshotSlider.addChangeListener(this);
		add(snapshotSlider, BorderLayout.PAGE_START);

		variableTable = new JTable(new VariableTableModel(snapshots.get(0)));
		variableTable.setPreferredSize(new Dimension(200,500));
		JScrollPane scrollPane = new JScrollPane(variableTable);
		variableTable.setFillsViewportHeight(true);
		add(scrollPane, BorderLayout.LINE_END);

		imageViewer = new SnapshotImageViewer(snapshots.get(0).getImage());
		add(imageViewer, BorderLayout.CENTER);
		imageViewer.setPreferredSize(new Dimension(500,500));

		/*
		viewer = new JPanel() {
			public void paint(Graphics g) {
				BufferedImage i = (BufferedImage) imageList.get(currImage);
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
		 */
	}

	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider)e.getSource();
		if (!source.getValueIsAdjusting()) {
			int snapshotIndex = (int)source.getValue();
			setCurrentSnapshot(snapshotIndex);
		}
	}

	private void setCurrentSnapshot(int snapshotIndex) {
		SnapshotModel snapshot = snapshots.get(snapshotIndex);
		imageViewer.setImage(snapshot.getImage());
		variableTable.setModel(new VariableTableModel(snapshot));
	}

	public static class SnapshotImageViewer extends JPanel {

		private Image image;

		public SnapshotImageViewer(Image image) {
			this.image = image;
		}

		public void setImage(Image image) {
			this.image = image;
			repaint();
		}

		public void paint(Graphics g) {
			BufferedImage i = (BufferedImage)image;
			g.drawImage(i, 0, 0, i.getHeight(null), 
					i.getWidth(null), null);
		}
	}

	public static class VariableTableModel extends AbstractTableModel {

		private static String[] columnNames = {"Name", "Values"};
		private SnapshotModel snapshot;
		private List<String> variableNames;
		private List<String> variableValues;

		public VariableTableModel(SnapshotModel snapshot) {
			this.snapshot = snapshot;
			variableNames = new ArrayList<String>();
			variableValues = new ArrayList<String>();

			for (String key : snapshot.getVariableMap().keySet()) {
				variableNames.add(key);
				variableValues.add(snapshot.getVariableMap().get(key));
			}
		}

		public int getColumnCount() {
			return 2;
		}

		public String getColumnName(int column) {
			return columnNames[column];
		}

		public int getRowCount() {
			return snapshot.getVariableMap().size();
		}

		public Object getValueAt(int row, int col) {
			if (col == 0) {
				return variableNames.get(row);
			} else {
				return variableValues.get(row);
			}
		}	
	}
}
