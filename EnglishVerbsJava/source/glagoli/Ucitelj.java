package glagoli;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.SystemColor;
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import java.awt.Font;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;

public class Ucitelj {
	
	private JFrame frame;
	private JTable table;

	public static void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ucitelj window = new Ucitelj();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public Ucitelj() {
		initialize();
	}
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(SystemColor.inactiveCaption);
		frame.setBounds(100, 100, 539, 372);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JToolBar toolBar = new JToolBar();
		toolBar.setBackground(Color.WHITE);
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		JButton dodajUcencaBtn = new JButton("Dodaj Ucenca");
		toolBar.add(dodajUcencaBtn);
		
		JButton urediUcenca = new JButton("Uredi Ucenca");
		toolBar.add(urediUcenca);
		
		JButton seznamUcencev = new JButton("Seznam Ucencev");
		toolBar.add(seznamUcencev);
		
		JButton izhodBtn = new JButton("Izhod");
		toolBar.add(izhodBtn);
		
		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		scrollPane.setViewportView(table);
	}
}
