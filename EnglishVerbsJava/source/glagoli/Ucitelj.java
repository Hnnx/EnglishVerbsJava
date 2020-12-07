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
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import DB.SqliteConnect;
import net.proteanit.sql.DbUtils;

import javax.swing.JScrollPane;

public class Ucitelj extends SqliteConnect {

	// Frame
	private JFrame frame;
	private static JTable table;

	// Gumbi
	private JButton btnAddUcenec;
	private JButton btnRemoveUcenec;
	private JButton btnAddGlagol;
	private JButton btnIzhod;

//BoilerPlate
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
		conn = poveziBazo();
		initialize();
		refreshUcenecList();
	}

//Helper funkcija ki osvezi DB in prikaze aktualne podatke po kliku na gume
	protected static void refreshUcenecList() {
		query = "SELECT * FROM users";

		try {
			pSTMT = conn.prepareStatement(query);
			rs = pSTMT.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
		} catch (Exception ex) {

		}
	}

//Boilerplate GUI
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("UPORABNIK: " + LoginForm.uporabniskoIme);
		frame.getContentPane().setBackground(SystemColor.inactiveCaption);
		frame.setBounds(100, 100, 683, 372);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		

		JToolBar toolBar = new JToolBar();
		toolBar.setBackground(Color.WHITE);
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);

//Gumb za dodajanje ucenca (odpre novo okno)

		btnAddUcenec = new JButton("Dodaj Ucenca");
		btnAddUcenec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				AddUcenec.start();
			}
		});
		toolBar.add(btnAddUcenec);

		//Gumb za dodajanje glagolov (odpre novo okno)
		btnAddGlagol = new JButton("Urejanje Glagolov");
		btnAddGlagol.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {

					AddGlagol.start();

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Opis napake: Prišlo je do napake pri zagonu okna za urejanje glagolov" + ex.getMessage(), "Napaka :(",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		// Gumb za odstranjevanje ucenca
		btnRemoveUcenec = new JButton("Odstrani Ucenca");
		btnRemoveUcenec.addActionListener(new ActionListener() {
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
					JOptionPane.showMessageDialog(null, "Opis napake: Prislo je do napake pri brisanju iz baze podatkov " + ex.getMessage(), "Napaka :(",
							JOptionPane.WARNING_MESSAGE);
				}

			}
		});
		toolBar.add(btnRemoveUcenec);
		toolBar.add(btnAddGlagol);

		btnIzhod = new JButton("Izhod");
		btnIzhod.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {

					int input = JOptionPane.showConfirmDialog(null, "Ali zalite zapreti program?", "Izhod",
							JOptionPane.INFORMATION_MESSAGE);

					if (input == 0) {
						System.exit(0);
					}

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Opis napake: Prislo je do napake pri izhodu iz programa" + ex.getMessage(), "Napaka :(",
							JOptionPane.WARNING_MESSAGE);
				}

			}
		});
		toolBar.add(btnIzhod);

		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		table.setCellSelectionEnabled(true);
		table.setColumnSelectionAllowed(true);
		scrollPane.setViewportView(table);
	}
}
