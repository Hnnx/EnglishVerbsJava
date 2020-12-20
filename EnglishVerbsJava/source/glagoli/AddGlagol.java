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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class AddGlagol extends SqliteConnect {

	// GUMBI
	protected static JButton btnNakljucno;
	private static JButton btnShrani;

	// DB
	static private String query;

	// HashMap
	private static HashMap<String, Integer> hmap = new HashMap<String, Integer>();

	// Array z cBoxi
	private static ArrayList<JComboBox<String>> cBoxList;

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

	// Frame + Panel
	private JFrame frame;
	private JPanel panelZComboBoxi;
	private JPanel statusPanel;

	// Checkboxi
	private static JPanel panelZcheckBoxi;
	private static JCheckBox checkBoxPrevod;
	private static JCheckBox checkBoxVerb;
	private static JCheckBox checkBoxPastSimple;
	private static JCheckBox checkBoxPastParticiple;

	// Ostale staticne
	private static JComboBox<String> cBoxUcenec;
	private static JTable helperTable;
	private static String idUporabnikaString;
	private static int idUporabnika;
	private static JLabel statusLabel;

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
		showFullTable();

		// Nafilaj cBoxList za nadaljno uporabi pri loopih
		cBoxList = new ArrayList<JComboBox<String>>();

		cBoxList.add(combo1);
		cBoxList.add(combo2);
		cBoxList.add(combo3);
		cBoxList.add(combo4);
		cBoxList.add(combo5);
		cBoxList.add(combo6);
		cBoxList.add(combo7);
		cBoxList.add(combo8);
		cBoxList.add(combo9);

		statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		statusPanel.setBounds(480, 25, 477, 27);
		frame.getContentPane().add(statusPanel);
		statusPanel.setLayout(null);

		statusLabel = new JLabel("");
		statusLabel.setBackground(new Color(244, 164, 96));
		statusLabel.setBounds(0, 0, 419, 27);
		statusPanel.add(statusLabel);
		statusLabel.setFont(new Font("Arial Black", Font.PLAIN, 12));
		statusLabel.setForeground(new Color(0, 100, 0));

		JButton btnVrniLogin = new JButton("PRIJAVA");
		btnVrniLogin.setFont(new Font("Arial Black", Font.PLAIN, 12));
		btnVrniLogin.setBackground(new Color(244, 164, 96));
		btnVrniLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {	
					frame.dispose();
					LoginForm window = new LoginForm();
					window.frame.setVisible(true);
					
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null,
							"Opis napake: Prislo je do napake pri izhodu iz programa" + e2.getMessage(), "Napaka :(",
							JOptionPane.WARNING_MESSAGE);
				}

			}
		});
		btnVrniLogin.setBounds(480, 607, 127, 30);
		frame.getContentPane().add(btnVrniLogin);

		JButton btnVrniUcitelj = new JButton("NAZAJ");
		btnVrniUcitelj.setFont(new Font("Arial Black", Font.PLAIN, 12));
		btnVrniUcitelj.setBackground(new Color(244, 164, 96));
		btnVrniUcitelj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					frame.dispose();

				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null,
							"Opis napake: Prislo je do napake pri izhodu iz programa" + e2.getMessage(), "Napaka :(",
							JOptionPane.WARNING_MESSAGE);
				}

			}
		});
		btnVrniUcitelj.setBounds(628, 607, 127, 30);
		frame.getContentPane().add(btnVrniUcitelj);

		panelZcheckBoxi = new JPanel();
		panelZcheckBoxi.setBackground(new Color(244, 164, 96));
		panelZcheckBoxi.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		panelZcheckBoxi.setBounds(21, 64, 390, 46);
		frame.getContentPane().add(panelZcheckBoxi);
		GridBagLayout gbl_panelZcheckBoxi = new GridBagLayout();
		gbl_panelZcheckBoxi.columnWidths = new int[] { 96, 96, 96, 0, 96, 0 };
		gbl_panelZcheckBoxi.rowHeights = new int[] { 42, 0 };
		gbl_panelZcheckBoxi.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panelZcheckBoxi.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panelZcheckBoxi.setLayout(gbl_panelZcheckBoxi);

		checkBoxPrevod = new JCheckBox("Prevod");
		checkBoxPrevod.setBackground(new Color(244, 164, 96));
		checkBoxPrevod.setToolTipText("polje, ki zelis da je ze izpolnjeno");
		checkBoxPrevod.setSelected(true);
		checkBoxPrevod.setHorizontalAlignment(SwingConstants.CENTER);
		checkBoxPrevod.setFont(new Font("Arial Black", Font.PLAIN, 14));
		GridBagConstraints gbc_checkBoxPrevod = new GridBagConstraints();
		gbc_checkBoxPrevod.fill = GridBagConstraints.BOTH;
		gbc_checkBoxPrevod.insets = new Insets(0, 0, 0, 5);
		gbc_checkBoxPrevod.gridx = 0;
		gbc_checkBoxPrevod.gridy = 0;
		panelZcheckBoxi.add(checkBoxPrevod, gbc_checkBoxPrevod);

		checkBoxVerb = new JCheckBox("Verb");
		checkBoxVerb.setBackground(new Color(244, 164, 96));
		checkBoxVerb.setToolTipText("polje, ki zelis da je ze izpolnjeno");
		checkBoxVerb.setHorizontalAlignment(SwingConstants.CENTER);
		checkBoxVerb.setFont(new Font("Arial Black", Font.PLAIN, 14));
		GridBagConstraints gbc_checkBoxVerb = new GridBagConstraints();
		gbc_checkBoxVerb.fill = GridBagConstraints.BOTH;
		gbc_checkBoxVerb.insets = new Insets(0, 0, 0, 5);
		gbc_checkBoxVerb.gridx = 1;
		gbc_checkBoxVerb.gridy = 0;
		panelZcheckBoxi.add(checkBoxVerb, gbc_checkBoxVerb);

		checkBoxPastSimple = new JCheckBox("Simple");
		checkBoxPastSimple.setBackground(new Color(244, 164, 96));
		checkBoxPastSimple.setToolTipText("polje, ki zelis da je ze izpolnjeno");
		checkBoxPastSimple.setHorizontalAlignment(SwingConstants.CENTER);
		checkBoxPastSimple.setFont(new Font("Arial Black", Font.PLAIN, 14));
		GridBagConstraints gbc_checkBoxPastSimple = new GridBagConstraints();
		gbc_checkBoxPastSimple.fill = GridBagConstraints.BOTH;
		gbc_checkBoxPastSimple.insets = new Insets(0, 0, 0, 5);
		gbc_checkBoxPastSimple.gridx = 2;
		gbc_checkBoxPastSimple.gridy = 0;
		panelZcheckBoxi.add(checkBoxPastSimple, gbc_checkBoxPastSimple);

		checkBoxPastParticiple = new JCheckBox("Participle");
		checkBoxPastParticiple.setBackground(new Color(244, 164, 96));
		checkBoxPastParticiple.setToolTipText("polje, ki zelis da je ze izpolnjeno");
		checkBoxPastParticiple.setFont(new Font("Arial Black", Font.PLAIN, 14));
		GridBagConstraints gbc_checkBoxPastParticiple = new GridBagConstraints();
		gbc_checkBoxPastParticiple.gridwidth = 2;
		gbc_checkBoxPastParticiple.fill = GridBagConstraints.BOTH;
		gbc_checkBoxPastParticiple.gridx = 3;
		gbc_checkBoxPastParticiple.gridy = 0;
		panelZcheckBoxi.add(checkBoxPastParticiple, gbc_checkBoxPastParticiple);

		fillComboBoxGlagoli();
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
		frame.getContentPane().setFont(new Font("Arial", Font.PLAIN, 15));
		frame.getContentPane().setBackground(SystemColor.activeCaption);
		frame.setBounds(100, 100, 1000, 700);
		frame.getContentPane().setLayout(null);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				Ucitelj.btnIzhod.doClick();

			}
		});

		cBoxUcenec = new JComboBox<String>();
		cBoxUcenec.setBounds(21, 23, 154, 30);
		cBoxUcenec.setFont(new Font("Arial", Font.BOLD, 16));
		frame.getContentPane().add(cBoxUcenec);

// Panel z Comboboxi ki vsebujejo izbor glagolov
		panelZComboBoxi = new JPanel();
		panelZComboBoxi.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		panelZComboBoxi.setBackground(new Color(244, 164, 96));
		panelZComboBoxi.setBounds(21, 121, 390, 529);
		frame.getContentPane().add(panelZComboBoxi);
		panelZComboBoxi.setLayout(new GridLayout(9, 0, 0, 10));

// 9 ComboBoxov za vnasanje glagolov
		combo1 = new JComboBox<String>();
		combo1.setFont(new Font("Arial Black", Font.PLAIN, 12));
		panelZComboBoxi.add(combo1);
		combo2 = new JComboBox<String>();
		combo2.setFont(new Font("Arial Black", Font.PLAIN, 12));
		panelZComboBoxi.add(combo2);
		combo3 = new JComboBox<String>();
		combo3.setFont(new Font("Arial Black", Font.PLAIN, 12));
		panelZComboBoxi.add(combo3);
		combo4 = new JComboBox<String>();
		combo4.setFont(new Font("Arial Black", Font.PLAIN, 12));
		panelZComboBoxi.add(combo4);
		combo5 = new JComboBox<String>();
		combo5.setFont(new Font("Arial Black", Font.PLAIN, 12));
		panelZComboBoxi.add(combo5);
		combo6 = new JComboBox<String>();
		combo6.setFont(new Font("Arial Black", Font.PLAIN, 12));
		panelZComboBoxi.add(combo6);
		combo7 = new JComboBox<String>();
		combo7.setFont(new Font("Arial Black", Font.PLAIN, 12));
		panelZComboBoxi.add(combo7);
		combo8 = new JComboBox<String>();
		combo8.setFont(new Font("Arial Black", Font.PLAIN, 12));
		panelZComboBoxi.add(combo8);
		combo9 = new JComboBox<String>();
		combo9.setFont(new Font("Arial Black", Font.PLAIN, 12));
		panelZComboBoxi.add(combo9);

		// --> Gumb shrani sprejme vrednosti pridobeljene iz 9ih ComboBoxov in jih vnese
		// v DB
		
		btnNakljucno = new JButton("NAKLJUCNO");
		btnNakljucno.setBackground(new Color(244, 164, 96));
		btnNakljucno.setToolTipText("Samodejno doloci nakljucne glagole");
		btnNakljucno.setFont(new Font("Arial Black", Font.PLAIN, 12));
		btnNakljucno.addActionListener(new ActionListener() {
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
					pSTMT.close();

					// ---> VNOS ENTRYJEV (NAKLJUCNI)
					query = "INSERT INTO helperTable (ucenec, glagol)" + "VALUES (?, ?);";
					pSTMT = conn.prepareStatement(query);

					for (int i = 0; i < 9; i++) {
						pSTMT.setInt(1, idUporabnika);
						pSTMT.setInt(2, getRDM());
						pSTMT.execute();
					}
					pSTMT.close();

					refreshTable();
					statusLabel.setText("Ucencu " + idUporabnikaString + " ste samodejno dolocili naslednje glagole");
					
					// ---> NASTAVI KATERE COLUMNE IZPISE 
					setColumns();

				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, "Opis napake: \n " + ex.getMessage(), "Napaka :(",
							JOptionPane.WARNING_MESSAGE);

				}

			}
		});

		btnNakljucno.setBounds(292, 24, 119, 30);
		frame.getContentPane().add(btnNakljucno);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(480, 77, 477, 519);
		frame.getContentPane().add(scrollPane);
		helperTable = new JTable();
		helperTable.setFont(new Font("Arial", Font.PLAIN, 12));
		scrollPane.setViewportView(helperTable);

		btnShrani = new JButton("SHRANI");
		btnShrani.setBackground(new Color(244, 164, 96));
		btnShrani.setToolTipText("Rocno doloci glagole");
		btnShrani.setFont(new Font("Arial Black", Font.PLAIN, 12));
		btnShrani.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					// --> Za pridobit id ucenca, ki mu zelimo shraniti glagole
					idUporabnikaString = cBoxUcenec.getSelectedItem().toString();
					idUporabnika = hmap.get(idUporabnikaString);

					// --> Preverjanje, ce ucenec ze ima dolocene glagole
					String query = "SELECT ucenec FROM helperTable WHERE ucenec=?";

					pSTMT = conn.prepareStatement(query);
					pSTMT.setInt(1, idUporabnika);

					rs = pSTMT.executeQuery();

					int count = 0;
					while (rs.next()) {
						count++;
					}

					rs.close();
					pSTMT.close();

					// ---> Delete before I figure out why UPDATE doesn't work :(
					if (count > 0) {
						query = "DELETE FROM helperTable WHERE ucenec =?";
						pSTMT = conn.prepareStatement(query);
						pSTMT.setInt(1, idUporabnika);
						pSTMT.execute();
						pSTMT.close();
					}

					// --> Klasicen insert query
					query = "INSERT INTO helperTable (ucenec, glagol)" + "VALUES (?, ?);";
					pSTMT = conn.prepareStatement(query);

					/*
					 * Komedija: Pridobit moremo glagol id iz ComboBoxa in ga shranimo v idGlagol
					 * Najprej Parsamo z Integer Wrapper Classom, potem izberemo iz ArrayLista,
					 * nafilanega z JComboBoxi z ArrayList metodo .get(i) ko dobimo OBJECT JComboBox
					 * uporabimo getSelectedItem metodo, ki jo je potrebno spremeniti v String - iz
					 * tega Stringa izluscimo STEVILKE z metodo .replaceAll()
					 * 
					 */

					for (int i = 0; i < cBoxList.size(); i++) {

						int idGlagol = Integer
								.parseInt(cBoxList.get(i).getSelectedItem().toString().replaceAll("[^0-9]", ""));

						pSTMT.setInt(1, idUporabnika);
						pSTMT.setInt(2, idGlagol);
						pSTMT.execute();

					}

					refreshTable();
					pSTMT.close();

					statusLabel.setText("Ucencu " + idUporabnikaString + " ste rocno dolocili naslednje glagole");

					// ---> NASTAVI KATERE COLUMNE IZPISE 
					setColumns();

				} catch (Exception e2) {

					e2.printStackTrace();
					JOptionPane.showMessageDialog(null, "Opis napake: \n " + e2.getMessage(), "Napaka :(",
							JOptionPane.WARNING_MESSAGE);

				}

			}
		});
		btnShrani.setBounds(185, 24, 97, 30);
		frame.getContentPane().add(btnShrani);

	}

// --> funkcija AddUcenec.setSeq nastavi default sequence 0,0,0,0 - UPDATE
// modificira sequence
	private void setColumns() {
		try {
			
			String newSeq = "";
			int[] seqArray = new int[4];
			
			if(checkBoxPrevod.isSelected()) {
				seqArray[0] = 1;
			}
			if(checkBoxVerb.isSelected()) {
				seqArray[1] = 1;
			}
			if(checkBoxPastSimple.isSelected()) {
				seqArray[2] = 1;
			}
			if(checkBoxPastParticiple.isSelected()) {
				seqArray[3] = 1;
			}
			
			for (int i : seqArray) {
				newSeq += String.valueOf(i);
			}
			

			query = "UPDATE users2 SET sequence = ? WHERE id = ?";

			pSTMT = conn.prepareStatement(query);

			pSTMT.setString(1, newSeq);
			pSTMT.setInt(2, idUporabnika);

			pSTMT.executeUpdate();

			pSTMT.close();

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private static void fillComboBoxGlagoli() {

		try {
			// Clear all before populating ComboBox
			resetComboBox();

			query = "SELECT prevod, id FROM glagoli ORDER BY prevod;";

			pSTMT = conn.prepareStatement(query);
			rs = pSTMT.executeQuery();

			while (rs.next()) {
				for (int i = 0; i < cBoxList.size(); i++) {
					cBoxList.get(i).addItem(rs.getString(1) + " " + rs.getInt(2));
				}
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null, "Opis napake: \n " + ex.getMessage(), "Napaka :(",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	private static void showFullTable() {

		try {
			query = "SELECT prevod AS PREVOD, verb AS VERB, pastSimple AS 'PAST SIMPLE', pastParticiple AS 'PAST PARTICIPLE' FROM glagoli";

			pSTMT = conn.prepareStatement(query);
			rs = pSTMT.executeQuery();

			helperTable.setModel(DbUtils.resultSetToTableModel(rs));
			
			pSTMT.close();
			rs.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Opis napake: \n " + e.getMessage(), "Napaka :(",
					JOptionPane.WARNING_MESSAGE);
		}
	}

// --> refresh Table
	private static void refreshTable() {
		try {

			// ---> REFRESH TABELE IN IZPIS GLAGOLOV
			query = "SELECT glagoli.prevod, glagoli.verb, glagoli.pastSimple, glagoli.pastParticiple\n"
					+ "FROM users2 LEFT OUTER JOIN helperTable\n" + "	ON users2.id = helperTable.ucenec\n"
					+ "LEFT OUTER JOIN glagoli\n" + "	ON glagoli.id = helperTable.glagol\n" + "	WHERE users2.id = "
					+ idUporabnika + ";";

			pSTMT = conn.prepareStatement(query);
			rs = pSTMT.executeQuery();
			helperTable.setModel(DbUtils.resultSetToTableModel(rs));

			pSTMT.close();
			rs.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Opis napake: Prislo je do napake pri osvezevanje tabele " + e.getMessage(), "Napaka :(",
					JOptionPane.WARNING_MESSAGE);
		}

	}

// Funkcija za polnjenje drop downa z ucenci
	public void fillComboBoxUcenci() {
		try {
			query = "SELECT username, id FROM users2 WHERE role = 3";
			pSTMT = conn.prepareStatement(query);
			rs = pSTMT.executeQuery();

			while (rs.next()) {
				cBoxUcenec.addItem(rs.getString("username"));
				hmap.put(rs.getString(1), rs.getInt(2));
			}
			
			rs.close();
			pSTMT.close();

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Opis napake: Prišlo je do napake pri vnašanju učencev v seznam\n " + e.getMessage(), "Napaka :(",
					JOptionPane.WARNING_MESSAGE);
		}

	}

	protected static int getRDM() {
		return (int) (Math.random() * 64 + 1);
	}
}
