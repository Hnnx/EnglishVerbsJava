package glagoli;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import DB.SqliteConnect;

import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;


import java.awt.GridLayout;

public class AddGlagol extends SqliteConnect {

	// GUMBI
	JButton btnUredi;
	JButton btnShrani;

	// DB
	static PreparedStatement pSTMT = null;
	static ResultSet rs = null;
	
	//HashMap
	HashMap<String, Integer> hmap = new HashMap<String, Integer>();
	
	//ComboBoxi z glagoli 
	private static JComboBox<String> combo1;
	private static JComboBox<String> combo2;
	private static JComboBox<String> combo3;
	private static JComboBox<String> combo4;
	private static JComboBox<String> combo5;
	private static JComboBox<String> combo6;
	private static JComboBox<String> combo7;
	private static JComboBox<String> combo8;
	private static JComboBox<String> combo9;

	private JFrame frame;
	private static JComboBox<String> cBoxUcenec;

	public static void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddGlagol window = new AddGlagol();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public AddGlagol() {
		conn = poveziBazo();
		initialize();
		fillComboBoxUcenci();
	}

	private void initialize() {
		frame = new JFrame("Uredi Ucenca / Dodaj glagol");
		frame.setBounds(100, 100, 1000, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		cBoxUcenec = new JComboBox<String>();
		cBoxUcenec.setBounds(21, 23, 125, 30);
		frame.getContentPane().add(cBoxUcenec);

		btnUredi = new JButton("UREDI");
		btnUredi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					String ime = cBoxUcenec.getSelectedItem().toString();
					
					int x = hmap.get(ime);
					System.out.println("id: "+x);
					
					combo1.removeAllItems();
					combo2.removeAllItems();
					combo3.removeAllItems();
					combo4.removeAllItems();
					combo5.removeAllItems();
					combo6.removeAllItems();
					combo7.removeAllItems();
					combo8.removeAllItems();
					combo8.removeAllItems();
					
					
					String query = "SELECT glagoli.pomen, glagoli.glagol, glagoli.tense, glagoli.part\n" + 
							"FROM users LEFT OUTER JOIN helperTable\n" + 
							"	ON users.id = helperTable.ucenec\n" + 
							"LEFT OUTER JOIN glagoli\n" + 
							"	ON glagoli.id = helperTable.glagol\n" + 
							"	WHERE users.id = "+x+";";
					
					
					String allVerbs = "SELECT pomen FROM glagoli";
					
					pSTMT = conn.prepareStatement(allVerbs);
					rs = pSTMT.executeQuery();
					
					while (rs.next()) {
							combo1.addItem(rs.getString(1));
							combo2.addItem(rs.getString(1));
							combo3.addItem(rs.getString(1));
							combo4.addItem(rs.getString(1));
							combo5.addItem(rs.getString(1));
							combo6.addItem(rs.getString(1));
							combo7.addItem(rs.getString(1));
							combo8.addItem(rs.getString(1));
							combo9.addItem(rs.getString(1));
			
					}

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Opis napake: \n " + ex.getMessage(), "Napaka :(",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btnUredi.setBounds(171, 23, 89, 30);
		frame.getContentPane().add(btnUredi);

		// Vertikalni panel z glagoli
		JPanel panel = new JPanel();
		panel.setBounds(21, 77, 377, 573);
		frame.getContentPane().add(panel);
		panel.setLayout(new GridLayout(9, 0, 0, 10));

		// 9 ComboBoxov za vnasanje glagolov
		combo1 = new JComboBox<String>();
		panel.add(combo1);

		combo2 = new JComboBox<String>();
		panel.add(combo2);

		combo3 = new JComboBox<String>();
		panel.add(combo3);

		combo4 = new JComboBox<String>();
		panel.add(combo4);

		combo5 = new JComboBox<String>();
		panel.add(combo5);

		combo6 = new JComboBox<String>();
		panel.add(combo6);

		combo7 = new JComboBox<String>();
		panel.add(combo7);

		combo8 = new JComboBox<String>();
		panel.add(combo8);

		combo9 = new JComboBox<String>();
		panel.add(combo9);
		
		btnShrani = new JButton("SHRANI");
		btnShrani.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			
			}
		});
		btnShrani.setBounds(286, 23, 89, 30);
		frame.getContentPane().add(btnShrani);
	}
	
	// Funkcija za polnjenje drop downa z ucenci
	public void fillComboBoxUcenci() {
		try {
			String query = "SELECT username, id FROM users;";
			pSTMT = conn.prepareStatement(query);
			rs = pSTMT.executeQuery();

			while (rs.next()) {
				cBoxUcenec.addItem(rs.getString("username"));
				hmap.put(rs.getString(1), rs.getInt(2));
			}
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Opis napake: \n " + e.getMessage(), "Napaka :(",
					JOptionPane.WARNING_MESSAGE);
		}

	}
}
