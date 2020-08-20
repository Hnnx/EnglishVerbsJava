package glagoli;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;

import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.WindowConstants;

import java.awt.SystemColor;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import DB.SqliteConnect;
import net.proteanit.sql.DbUtils;

import javax.swing.JScrollPane;

public class Ucitelj extends SqliteConnect{

	private JFrame frmAaaa;
	private static JTable table;

	public static void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ucitelj window = new Ucitelj();
					window.frmAaaa.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	static PreparedStatement pSTMT = null;
	static ResultSet rs = null;

	public Ucitelj() {
		conn = poveziBazo();

		initialize();
	}

	protected static void refresh() {
		String query = "SELECT * FROM users";

		try {
			pSTMT = conn.prepareStatement(query);
			rs = pSTMT.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
		} catch (Exception ex) {

		}
	}

	private void initialize() {
		frmAaaa = new JFrame();
		frmAaaa.setTitle("UPORABNIK: " + LoginForm.uporabniskoIme);
		frmAaaa.getContentPane().setBackground(SystemColor.inactiveCaption);
		frmAaaa.setBounds(100, 100, 539, 372);
		frmAaaa.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frmAaaa.getContentPane().setLayout(new BorderLayout(0, 0));

		JToolBar toolBar = new JToolBar();
		toolBar.setBackground(Color.WHITE);
		frmAaaa.getContentPane().add(toolBar, BorderLayout.NORTH);

		JButton dodajUcencaBtn = new JButton("Dodaj Ucenca");
		dodajUcencaBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				AddUcenec.start();
			}
		});
		toolBar.add(dodajUcencaBtn);

		JButton seznamUcencev = new JButton("Seznam Ucencev");
		seznamUcencev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					String query = "SELECT * FROM users";
					pSTMT = conn.prepareStatement(query);

					rs = pSTMT.executeQuery();

					table.setModel(DbUtils.resultSetToTableModel(rs));

					pSTMT.close();
					rs.close();

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Opis napake: \n " + ex.getMessage(), "Napaka :(",
							JOptionPane.WARNING_MESSAGE);
				}

			}
		});

		JButton odstraniUcencaBtn = new JButton("Odstrani Ucenca");
		odstraniUcencaBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {

					DefaultTableModel model = (DefaultTableModel) table.getModel();

					int selectedRow = table.getSelectedRow();
					String cell = table.getModel().getValueAt(selectedRow, 0).toString();

					String query = "DELETE FROM users WHERE id= " + cell;

					pSTMT = conn.prepareStatement(query);
					pSTMT.execute();
					JOptionPane.showMessageDialog(null, "Izbrisano", "Uporabnik je bil uspesno izbrisan",
							JOptionPane.INFORMATION_MESSAGE);

					model.removeRow(selectedRow);

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Opis napake: \n " + ex.getMessage(), "Napaka :(",
							JOptionPane.WARNING_MESSAGE);
				}

			}
		});
		
		JButton dodajGlagolBtn = new JButton("Dodaj glagol");
		dodajGlagolBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					
					AddGlagol.start();					
					
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Opis napake: \n " + ex.getMessage(), "Napaka :(", JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		toolBar.add(dodajGlagolBtn);

		toolBar.add(odstraniUcencaBtn);
		toolBar.add(seznamUcencev);

		JButton izhodBtn = new JButton("Izhod");
		izhodBtn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				try {
					
					int input = JOptionPane.showConfirmDialog(null, "Ali zalite zapreti program?", "Izhod", JOptionPane.INFORMATION_MESSAGE);
					
					if(input == 0) {						
						System.exit(0);					
					} 					
					
					
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Opis napake: \n " + ex.getMessage(), "Napaka :(", JOptionPane.WARNING_MESSAGE);
				}
				
			}
		});
		
		
		toolBar.add(izhodBtn);
		
		JScrollPane scrollPane = new JScrollPane();
		frmAaaa.getContentPane().add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		table.setCellSelectionEnabled(true);
		table.setColumnSelectionAllowed(true);
		scrollPane.setViewportView(table);
	}
}
