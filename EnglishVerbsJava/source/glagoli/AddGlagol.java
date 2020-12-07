package glagoli;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import DB.SqliteConnect;
import net.proteanit.sql.DbUtils;

import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class AddGlagol extends SqliteConnect {

	/*
	 * Preveri če je v helperTable vec kot 1 vnos UPDATE VALUES namesto INSERT
	 * DELETE * FROM helperTable WHERE ucenec = ?
	 */

	// GUMBI
	JButton btnUredi;
	JButton btnShrani;
	JButton btnRandom;

	// DB
	static PreparedStatement pSTMT = null;
	static ResultSet rs = null;
	static private String query;

	// HashMap
	private static HashMap<String, Integer> hmap = new HashMap<String, Integer>();

	// ComboBoxi z glagoli
	private static JComboBox<String> combo1;
	private static JComboBox<String> combo2;
	private static JComboBox<String> combo3;
	private static JComboBox<String> combo4;
	private static JComboBox<String> combo5;
	private static JComboBox<String> combo6;
	private static JComboBox<String> combo7;
	private static JComboBox<String> combo8;
	private static JComboBox<String> combo9;

	// Font
	private static Font TrBold = new Font("TimesRoman", Font.BOLD, 16);
	private static Font TrPlain = new Font("TimesRoman", Font.PLAIN, 14);

	// Frame
	private JFrame frame;
	private JPanel panelZComboBoxi;
	private JLabel seznamLabel;

	// Ostale staticne
	private static JComboBox<String> cBoxUcenec;
	private static JTable helperTable;
	private static String idUporabnikaString;
	private static int idUporabnika;

	// --> WindowBuilder BOILERPLATE
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

	// --> Ob zagonu okna povezi bazo, nafilaj combo box z ucenci, showTable izpise
	// tabelo za referenco za lazje izbiranje glagolov
	public AddGlagol() {
		initialize();
		conn = poveziBazo();
		fillComboBoxUcenci();
		showTable();
	}

	// Funkcija za brisanje/resetiranje comboBoxov - brez tega se glagoli stackajo
	// ob vsakem kliku na gumb (iz baze napolni rw+ )
	private static void resetComboBox() {

		combo1.removeAllItems();
		combo2.removeAllItems();
		combo3.removeAllItems();
		combo4.removeAllItems();
		combo5.removeAllItems();
		combo6.removeAllItems();
		combo7.removeAllItems();
		combo8.removeAllItems();
		combo8.removeAllItems();

	}

	private void initialize() {
		// --> Boilerplate
		frame = new JFrame("Uredi Ucenca / Dodaj glagol");
		frame.setBounds(100, 100, 1000, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		cBoxUcenec = new JComboBox<String>();
		cBoxUcenec.setBounds(21, 23, 80, 30);
		cBoxUcenec.setFont(TrBold);
		frame.getContentPane().add(cBoxUcenec);

		// Gumb uredi najprej izbere uporabnika in izpise njegove glagole
		// jih vnese v bazo
		btnUredi = new JButton("UREDI");
		btnUredi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					// Clear all before populating ComboBox
					resetComboBox();

					query = "SELECT prevod FROM glagoli";

					pSTMT = conn.prepareStatement(query);
					rs = pSTMT.executeQuery();

					while (rs.next()) {
						combo1.addItem(" " + rs.getString(1));
						combo2.addItem(" " + rs.getString(1));
						combo3.addItem(" " + rs.getString(1));
						combo4.addItem(" " + rs.getString(1));
						combo5.addItem(" " + rs.getString(1));
						combo6.addItem(" " + rs.getString(1));
						combo7.addItem(" " + rs.getString(1));
						combo8.addItem(" " + rs.getString(1));
						combo9.addItem(" " + rs.getString(1));
					}

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Opis napake: \n " + ex.getMessage(), "Napaka :(",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		btnUredi.setBounds(111, 23, 89, 30);
		frame.getContentPane().add(btnUredi);

		// Panel z Comboboxi ki vsebujejo izbor glagolov
		panelZComboBoxi = new JPanel();
		panelZComboBoxi.setBounds(21, 77, 390, 573);
		frame.getContentPane().add(panelZComboBoxi);
		panelZComboBoxi.setLayout(new GridLayout(9, 0, 0, 10));

		// 9 ComboBoxov za vnasanje glagolov
		combo1 = new JComboBox<String>();
		combo1.setFont(TrBold);
		panelZComboBoxi.add(combo1);
		combo2 = new JComboBox<String>();
		combo2.setFont(TrBold);
		panelZComboBoxi.add(combo2);
		combo3 = new JComboBox<String>();
		combo3.setFont(TrBold);
		panelZComboBoxi.add(combo3);
		combo4 = new JComboBox<String>();
		combo4.setFont(TrBold);
		panelZComboBoxi.add(combo4);
		combo5 = new JComboBox<String>();
		combo5.setFont(TrBold);
		panelZComboBoxi.add(combo5);
		combo6 = new JComboBox<String>();
		combo6.setFont(TrBold);
		panelZComboBoxi.add(combo6);
		combo7 = new JComboBox<String>();
		combo7.setFont(TrBold);
		panelZComboBoxi.add(combo7);
		combo8 = new JComboBox<String>();
		combo8.setFont(TrBold);
		panelZComboBoxi.add(combo8);
		combo9 = new JComboBox<String>();
		combo9.setFont(TrBold);
		panelZComboBoxi.add(combo9);

		// --> Gumb shrani sprejme vrednosti pridobeljene iz 9ih ComboBoxov in jih vnese
		// v DB
		btnShrani = new JButton("SHRANI");
		btnShrani.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// --> PRIDOBITEV UCENEC ID-JA ZA VNOS IN BRISANJE
					idUporabnikaString = cBoxUcenec.getSelectedItem().toString();
					idUporabnika = hmap.get(idUporabnikaString);

					// --> BRISANJE ENTRYJEV PRED VSAKIM VNOSOM
					query = "DELETE FROM helperTable WHERE ucenec=?";
					pSTMT = conn.prepareStatement(query);
					pSTMT.setInt(1, idUporabnika);
					pSTMT.execute();

					// ---> VNOS ENTRYJEV (NAKLJUCNI)
					query = "INSERT INTO helperTable (ucenec, glagol)" + "VALUES (?, ?);";
					pSTMT = conn.prepareStatement(query);

					for (int i = 0; i < 9; i++) {
						pSTMT.setInt(1, idUporabnika);
						pSTMT.setInt(2, getRDM());
						pSTMT.execute();
					}

					// ---> REFRESH TABELE IN IZPIS GLAGOLOV
					query = "SELECT glagoli.prevod FROM users"+
							"LEFT OUTER JOIN helperTable\n" + 
							"	ON users.id = helperTable.ucenec\n" + 
							"LEFT OUTER JOIN glagoli\n" + 
							"	ON glagoli.id = helperTable.glagol\n" + 
							"	WHERE users.id = "+idUporabnika+";"; 

					pSTMT.close();

				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Opis napake: \n " + ex.getMessage(), "Napaka :(",
							JOptionPane.WARNING_MESSAGE);

				}

			}
		});

		btnShrani.setBounds(210, 23, 89, 30);
		frame.getContentPane().add(btnShrani);

		seznamLabel = new JLabel("Seznam Glagolov");
		seznamLabel.setBounds(480, 31, 122, 30);
		frame.getContentPane().add(seznamLabel);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(480, 77, 477, 573);
		frame.getContentPane().add(scrollPane);
		helperTable = new JTable();
		helperTable.setFont(TrPlain);
		scrollPane.setViewportView(helperTable);

		btnRandom = new JButton("Random");
		btnRandom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		btnRandom.setBounds(309, 23, 89, 30);
		frame.getContentPane().add(btnRandom);

	}

	private static void showTable() {

		try {
			query = "SELECT prevod AS PREVOD, verb AS VERB, pastSimple AS 'PAST SIMPLE', pastParticiple AS 'PAST PARTICIPLE' FROM glagoli";

			pSTMT = conn.prepareStatement(query);
			rs = pSTMT.executeQuery();

			helperTable.setModel(DbUtils.resultSetToTableModel(rs));

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Opis napake: \n " + e.getMessage(), "Napaka :(",
					JOptionPane.WARNING_MESSAGE);
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				System.out.println("Error: " + e.getMessage());
			}
		}
	}

	// Funkcija za polnjenje drop downa z ucenci
	public void fillComboBoxUcenci() {
		try {
			query = "SELECT username, id FROM users;";
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

	private static int getRDM() {
		int rdm = (int) (Math.random() * 64 + 1);
		return rdm;
	}
}
