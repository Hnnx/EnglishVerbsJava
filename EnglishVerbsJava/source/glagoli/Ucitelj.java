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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import DB.SqliteConnect;
import net.proteanit.sql.DbUtils;

import javax.swing.JScrollPane;
import java.awt.Font;
import javax.swing.JCheckBox;

public class Ucitelj extends SqliteConnect {

	// Frame
	protected static JFrame frame;
	private static JTable table;

	// Gumbi
	private JButton btnAddUcenec;
	private JButton btnRemoveUcenec;
	private JButton btnAddGlagol;
	static JButton btnIzhod;
	protected static JCheckBox cBoxGesla;

	// BoilerPlate
	public static void start() {
		EventQueue.invokeLater(new Runnable() {
			@SuppressWarnings("static-access")
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
		refreshHidden();
		
		cBoxGesla.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				
				int state = e.getStateChange();
				
				if(state == 1) {
					refreshPassword();
				}
				else {
					refreshHidden();
				}
				
			}
		});
		

		
	}

	// Helper funkcija ki osvezi DB in prikaze aktualne podatke po kliku na gume
	protected static void refreshPassword() {
		query = "SELECT users2.id as 'zaporedna stevilka', users2.username AS uporabnik, roles.role AS pravice, users2.password as geslo FROM users2\r\n" + 
				"JOIN roles ON users2.role = roles.id";

		try {
			pSTMT = conn.prepareStatement(query);
			rs = pSTMT.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
		} catch (Exception ex) {

		}
	}
	
	protected static void refreshHidden() {
		query = "SELECT users2.id as 'zaporedna stevilka', users2.username AS uporabnik, roles.role AS pravice FROM users2\r\n" + 
				"JOIN roles ON users2.role = roles.id";

		try {
			pSTMT = conn.prepareStatement(query);
			rs = pSTMT.executeQuery();
			table.setModel(DbUtils.resultSetToTableModel(rs));
		} catch (Exception ex) {

		}
	}	

	// Boilerplate GUI
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("UPORABNIK: " + LoginForm.uporabniskoIme);
		frame.getContentPane().setBackground(SystemColor.inactiveCaption);
		frame.setBounds(100, 100, 683, 372);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		frame.addWindowListener(new WindowAdapter() {
			  public void windowClosing(WindowEvent we) {
			    btnIzhod.doClick();			  
				  
			  }
			});
		
		

		JToolBar toolBar = new JToolBar();
		toolBar.setBackground(SystemColor.inactiveCaption);
		frame.getContentPane().add(toolBar, BorderLayout.NORTH);

		// Gumb za dodajanje ucenca (odpre novo okno)

		btnAddUcenec = new JButton("Dodaj Uporabnika");
		btnAddUcenec.setBackground(new Color(244, 164, 96));
		btnAddUcenec.setFont(new Font("Arial Black", Font.PLAIN, 13));
		btnAddUcenec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				AddUporabnik.start();
			}
		});
		toolBar.add(btnAddUcenec);

		// Gumb za dodajanje glagolov (odpre novo okno)
		btnAddGlagol = new JButton("Urejanje Glagolov");
		btnAddGlagol.setBackground(new Color(244, 164, 96));
		btnAddGlagol.setFont(new Font("Arial Black", Font.PLAIN, 13));
		btnAddGlagol.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {

					AddGlagol.start();

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null,
							"Opis napake: Prišlo je do napake pri zagonu okna za urejanje glagolov" + ex.getMessage(),
							"Napaka :(", JOptionPane.WARNING_MESSAGE);
				}
			}
		});

		// Gumb za odstranjevanje ucenca
		btnRemoveUcenec = new JButton("Odstrani Ucenca");
		btnRemoveUcenec.setBackground(new Color(244, 164, 96));
		btnRemoveUcenec.setFont(new Font("Arial Black", Font.PLAIN, 13));
		btnRemoveUcenec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {

					DefaultTableModel model = (DefaultTableModel) table.getModel();

					int selectedRow = table.getSelectedRow();
					String cell = table.getModel().getValueAt(selectedRow, 0).toString();

					String query = "DELETE FROM users2 WHERE id= " + cell;

					pSTMT = conn.prepareStatement(query);
					pSTMT.execute();
					JOptionPane.showMessageDialog(null, "Izbrisano", "Uporabnik je bil uspesno izbrisan",
							JOptionPane.INFORMATION_MESSAGE);

					model.removeRow(selectedRow);

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null,
							"Opis napake: Prislo je do napake pri brisanju iz baze podatkov " + ex.getMessage(),
							"Napaka :(", JOptionPane.WARNING_MESSAGE);
				}

			}
		});
		toolBar.add(btnRemoveUcenec);
		toolBar.add(btnAddGlagol);

		btnIzhod = new JButton("Izhod");
		btnIzhod.setBackground(new Color(244, 164, 96));
		btnIzhod.setFont(new Font("Arial Black", Font.PLAIN, 13));
		btnIzhod.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {

					int input = JOptionPane.showConfirmDialog(null, "Ali zelite zapreti program?", "Izhod",
							JOptionPane.INFORMATION_MESSAGE);

					if (input == 0) {
						System.exit(0);
					}

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null,
							"Opis napake: Prislo je do napake pri izhodu iz programa" + ex.getMessage(), "Napaka :(",
							JOptionPane.WARNING_MESSAGE);
				}

			}
		});
		toolBar.add(btnIzhod);
		
		cBoxGesla = new JCheckBox("Prikazi Gesla");
		toolBar.add(cBoxGesla);

		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		table.setFont(new Font("Arial Black", Font.PLAIN, 13));
		table.setCellSelectionEnabled(true);
		table.setColumnSelectionAllowed(true);
		scrollPane.setViewportView(table);
	}
}
