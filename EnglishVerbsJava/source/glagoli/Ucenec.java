package glagoli;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.plaf.metal.MetalButtonUI;

import java.awt.GridLayout;

import DB.SqliteConnect;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JProgressBar;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

public class Ucenec extends SqliteConnect {

	private static JFrame frame;
	private static JTextField pomenR1;
	private static JTextField glagolR1;
	private static JTextField tenseR1;
	private static JTextField partR1;
	private static JTextField pomenR2;
	private static JTextField glagolR2;
	private static JTextField tenseR2;
	private static JTextField partR2;
	private static JTextField pomenR3;
	private static JTextField glagolR3;
	private static JTextField tenseR3;
	private static JTextField partR3;
	private static JTextField pomenR4;
	private static JTextField glagolR4;
	private static JTextField tenseR4;
	private static JTextField partR4;
	private static JTextField pomenR5;
	private static JTextField glagolR5;
	private static JTextField tenseR5;
	private static JTextField partR5;
	private static JTextField pomenR6;
	private static JTextField glagolR6;
	private static JTextField tenseR6;
	private static JTextField partR6;
	private static JTextField pomenR7;
	private static JTextField glagolR7;
	private static JTextField tenseR7;
	private static JTextField partR7;
	private static JTextField pomenR8;
	private static JTextField glagolR8;
	private static JTextField tenseR8;
	private static JTextField partR8;
	private static JTextField pomenR9;
	private static JTextField glagolR9;
	private static JTextField tenseR9;
	private static JTextField partR9;

	private JPanel progressPanel;
	private JTable table;

	// Posamezni ArrayListi z glagoli in oblikami
	private static ArrayList<String> prevodArr = new ArrayList<String>();
	private static ArrayList<String> verbArr = new ArrayList<String>();
	private static ArrayList<String> pastSimpleArr = new ArrayList<String>();
	private static ArrayList<String> pastParticipleArr = new ArrayList<String>();

	// ArrayList ki zdruzi VSE TextJield-e za preverjanje
	private static ArrayList<JTextField> vsiJTextFieldi = new ArrayList<>();

	// ArrayList ki zdruzi VSE glagole iz DB
	static ArrayList<String> vsiGlagoliIzDB = new ArrayList<String>();

	// Barve
	static Color incorrect = new Color(255, 99, 99);
	static Color correct = new Color(163, 222, 146);
	static Color barvaGumba = new Color(244, 164, 96);

	// Font
	static Font fontGumbi = new Font("Arial Black", Font.PLAIN, 13);
	static Font fontTekst = new Font("Arial", Font.PLAIN, 13);

	// Gumbi TOP
	protected static JButton gumbPrevod;
	protected static JButton gumbVerb;
	protected static JButton gumbPastSimple;
	protected static JButton gumbPastParticiple;

	// GUMBI BOTTOM
	private JButton btnPonastavi;
	private JButton btnIzpisiVse;
	private JButton btnPreveri;
	private JButton btnIzhod;

	// User Score
	private static int userScore = 0;
	private static int totalPossibleScore = 36;
	private static JProgressBar progressBar;
	private static JLabel tockeTotal;
	private JButton btnPridobiNove;

	// Boilerplate
	public static void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ucenec window = new Ucenec();
					Ucenec.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Ucenec() {

		initialize();

		// Ob zagonu okna naj se poveze na DB in napolni ArrayList z vsemi glagoli za
		// delo in upravljanje z njimi
		conn = poveziBazo();

		fillArrayWithVerbs();
		fetchFromDB();

		getColumns();
		disableButtons();

		// --> Menjava default foreground color barve pri gumbih
		defaultDisabled(gumbPrevod);
		defaultDisabled(gumbVerb);
		defaultDisabled(gumbPastSimple);
		defaultDisabled(gumbPastParticiple);

	}

	private static void disableButtons() {
		gumbPrevod.setEnabled(false);
		gumbVerb.setEnabled(false);
		gumbPastSimple.setEnabled(false);
		gumbPastParticiple.setEnabled(false);

	}

	private static void getColumns() {

		String prvoPolje = String.valueOf(LoginForm.sequence.substring(0, 1));
		String drugoPolje = String.valueOf(LoginForm.sequence.substring(1, 2));
		String tretjePolje = String.valueOf(LoginForm.sequence.substring(2, 3));
		String cetrtoPolje = String.valueOf(LoginForm.sequence.substring(3));

		gumbPrevod.setEnabled(true);
		gumbVerb.setEnabled(true);
		gumbPastSimple.setEnabled(true);
		gumbPastParticiple.setEnabled(true);

		if (Integer.parseInt(prvoPolje) == 1)
			activateColumn(gumbPrevod);
		if (Integer.parseInt(drugoPolje) == 1)
			activateColumn(gumbVerb);
		if (Integer.parseInt(tretjePolje) == 1)
			activateColumn(gumbPastSimple);
		if (Integer.parseInt(cetrtoPolje) == 1)
			activateColumn(gumbPastParticiple);

	}

	// --> Spremeni default disabled color v crno
	private static void defaultDisabled(JButton selectedBTN) {
		selectedBTN.setUI(new MetalButtonUI() {
			@Override
			protected Color getDisabledTextColor() {
				return Color.black;
			}
		});
	}

	// Funkcija preveri, ali je polje (TextField) prazno - če NI, kliče funkcijo
	// check(opisana kasneje)
	// Ce je TextField prazen, oznaci kot napacen odgovor in jo zaklene
	private static void checkEmpty(JTextField jTextField, int n) {

		JTextField pomenVar = jTextField;

		if (!vsiJTextFieldi.get(n).getText().isBlank()) {
			checkInputVSexpected(pomenVar.getText(), vsiGlagoliIzDB.get(n), pomenVar);

		} else {
			pomenVar.setEditable(false);
			pomenVar.setBackground(barvaGumba);
			pomenVar.setForeground(Color.black);
			pomenVar.setText("/");
		}
	}

	public static void activateColumn(JButton columnName) {
		try {
			columnName.doClick();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Prišlo je do napake pri aktiviranju stolpcev. Uporabnik se nima dolocenih glagolov\nProsim, dodajte glagole.",
					"Napaka", JOptionPane.WARNING_MESSAGE);
		}
	}

	// Funkcija ki polni ArrayList od vseh polji z glagoli v vseh oblikah
	// Spisana za lazje delanje z loopi, preverjanje, resetiranje itd
	private static void fillArrayWithVerbs() {

		vsiJTextFieldi.add(pomenR1);
		vsiJTextFieldi.add(pomenR2);
		vsiJTextFieldi.add(pomenR3);
		vsiJTextFieldi.add(pomenR4);
		vsiJTextFieldi.add(pomenR5);
		vsiJTextFieldi.add(pomenR6);
		vsiJTextFieldi.add(pomenR7);
		vsiJTextFieldi.add(pomenR8);
		vsiJTextFieldi.add(pomenR9);

		vsiJTextFieldi.add(glagolR1);
		vsiJTextFieldi.add(glagolR2);
		vsiJTextFieldi.add(glagolR3);
		vsiJTextFieldi.add(glagolR4);
		vsiJTextFieldi.add(glagolR5);
		vsiJTextFieldi.add(glagolR6);
		vsiJTextFieldi.add(glagolR7);
		vsiJTextFieldi.add(glagolR8);
		vsiJTextFieldi.add(glagolR9);

		vsiJTextFieldi.add(tenseR1);
		vsiJTextFieldi.add(tenseR2);
		vsiJTextFieldi.add(tenseR3);
		vsiJTextFieldi.add(tenseR4);
		vsiJTextFieldi.add(tenseR5);
		vsiJTextFieldi.add(tenseR6);
		vsiJTextFieldi.add(tenseR7);
		vsiJTextFieldi.add(tenseR8);
		vsiJTextFieldi.add(tenseR9);

		vsiJTextFieldi.add(partR1);
		vsiJTextFieldi.add(partR2);
		vsiJTextFieldi.add(partR3);
		vsiJTextFieldi.add(partR4);
		vsiJTextFieldi.add(partR5);
		vsiJTextFieldi.add(partR6);
		vsiJTextFieldi.add(partR7);
		vsiJTextFieldi.add(partR8);
		vsiJTextFieldi.add(partR9);

	}

	// Funkcija prebere vnose iz DB in jih vnese v Array za nadaljno delo
	private static void fetchFromDB() {

		pSTMT = null;
		rs = null;
		query = "SELECT glagoli.prevod, glagoli.verb, glagoli.pastSimple, glagoli.pastParticiple\n"
				+ "FROM users2 LEFT OUTER JOIN helperTable\n" + "	ON users2.id = helperTable.ucenec\n"
				+ "LEFT OUTER JOIN glagoli\n" + "	ON glagoli.id = helperTable.glagol\n" + "	WHERE users2.id = "
				+ LoginForm.uporabnikID + ";";

		try {

			pSTMT = conn.prepareStatement(query);
			rs = pSTMT.executeQuery();
			String prevod = null;
			String verb = null;
			String pastSimple = null;
			String pastParticiple = null;

			// Za vsak loop pridobi String in ga vkljuci v array
			while (rs.next()) {

				prevod = rs.getString(1);
				prevodArr.add(prevod);

				verb = rs.getString(2);
				verbArr.add(verb);

				pastSimple = rs.getString(3);
				pastSimpleArr.add(pastSimple);

				pastParticiple = rs.getString(4);
				pastParticipleArr.add(pastParticiple);

			}

			// Izhod iz loopa naj zdruzi 4 liste v enega (addAll v vsiGlagoli)

			vsiGlagoliIzDB = new ArrayList<String>();
			vsiGlagoliIzDB.addAll(prevodArr);
			vsiGlagoliIzDB.addAll(verbArr);
			vsiGlagoliIzDB.addAll(pastSimpleArr);
			vsiGlagoliIzDB.addAll(pastParticipleArr);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Prišlo je do napake pri pridobivanju glagolov iz baze podatkov.\nOpis napake: " + e.toString(),
					"Napaka", JOptionPane.WARNING_MESSAGE);

		}

	}

	// Funkcija za preverjanje vnosa
	private static void checkInputVSexpected(String input, String expected, JTextField cell) {

		if (cell.isEditable()) {

			if (!input.equalsIgnoreCase(expected)) {
				cell.setEnabled(false);
				cell.setBackground(incorrect);
				cell.setForeground(Color.black);
			} else {
				cell.setEditable(false);
				cell.setBackground(correct);
				userScore++;
				progressBar.setValue(userScore);
				tockeTotal.setText(userScore + " / " + totalPossibleScore);
			}
		}

	}

	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Ucenje Glagolov UPORABNIK: " + LoginForm.uporabniskoIme.substring(0, 1).toUpperCase()
				+ LoginForm.uporabniskoIme.substring(1));
		frame.setBounds(100, 100, 969, 638);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				btnIzhod.doClick();

			}
		});

		JPanel uporabnikToolbar = new JPanel();
		uporabnikToolbar.setBackground(SystemColor.inactiveCaption);
		frame.getContentPane().add(uporabnikToolbar, BorderLayout.NORTH);

		JLabel uporabniskoIme = new JLabel(
				LoginForm.uporabniskoIme.substring(0, 1).toUpperCase() + LoginForm.uporabniskoIme.substring(1));
		uporabniskoIme.setFont(new Font("Arial Black", Font.PLAIN, 13));
		uporabnikToolbar.add(uporabniskoIme);

		progressBar = new JProgressBar(0, totalPossibleScore);
		progressBar.setForeground(new Color(163, 222, 146));
		uporabnikToolbar.add(progressBar);

		tockeTotal = new JLabel(userScore + " / " + totalPossibleScore);
		tockeTotal.setFont(fontGumbi);
		uporabnikToolbar.add(tockeTotal);

		JPanel bottomPanelZaGumb = new JPanel();
		bottomPanelZaGumb.setBackground(SystemColor.inactiveCaption);
		frame.getContentPane().add(bottomPanelZaGumb, BorderLayout.SOUTH);

		// --> IZPISI VSE za Debugging
		// TODO: Odstrani iz koncne verzije!

		btnIzpisiVse = new JButton("Izpisi Vse");
		btnIzpisiVse.setFont(fontGumbi);
		btnIzpisiVse.setBackground(barvaGumba);
		btnIzpisiVse.setVisible(false);

		if (LoginForm.role == 1) {
			btnIzpisiVse.setVisible(true);
		}

		btnIzpisiVse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				List<String> combined = new ArrayList<String>();
				combined.clear();

				query = "SELECT glagoli.prevod, glagoli.verb, glagoli.pastSimple, glagoli.pastParticiple\n"
						+ "FROM users2 LEFT OUTER JOIN helperTable\n" + "	ON users2.id = helperTable.ucenec\n"
						+ "LEFT OUTER JOIN glagoli\n" + "	ON glagoli.id = helperTable.glagol\n"
						+ "	WHERE users2.id = " + LoginForm.uporabnikID + ";";

				try {

					pSTMT = conn.prepareStatement(query);
					rs = pSTMT.executeQuery();
					String prevod = null;
					String verb = null;
					String pastSimple = null;
					String pastParticiple = null;

					while (rs.next()) {

						prevod = rs.getString(1);
						prevodArr.add(prevod);

						verb = rs.getString(2);
						verbArr.add(verb);

						pastSimple = rs.getString(3);
						pastSimpleArr.add(pastSimple);

						pastParticiple = rs.getString(4);
						pastParticipleArr.add(pastParticiple);

					}

					// PRIDOBI PODATKE IZ DB, NASTAVI (setText) NA VSAKIH 9 JTEXTFIELDOV
					// PRIDOBLJEDNO VREDNOST

					int cntr = 0;
					for (int j = 0; j < 9; j++) {
						vsiJTextFieldi.get(j).setText(prevodArr.get(cntr));
						cntr++;
					}

					cntr = 0;
					for (int j = 9; j < 18; j++) {
						vsiJTextFieldi.get(j).setText(verbArr.get(cntr));
						cntr++;
					}

					cntr = 0;
					for (int j = 18; j < 27; j++) {
						vsiJTextFieldi.get(j).setText(pastSimpleArr.get(cntr));
						cntr++;
					}

					cntr = 0;
					for (int j = 27; j < 36; j++) {
						vsiJTextFieldi.get(j).setText(pastParticipleArr.get(cntr));
						cntr++;
					}

					// ZDRUZI PRIDOBLJENE PODATKE IZ RESULTSETA IN JIH VNESE V ENEGA

					combined.addAll(prevodArr);
					combined.addAll(verbArr);
					combined.addAll(pastSimpleArr);
					combined.addAll(pastParticipleArr);

				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(null, "Prišlo je do napake pri pridobivanju podatkov iz baze",
							"Napaka", JOptionPane.WARNING_MESSAGE);
				}

				catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Prišlo je do napake pri pridobivanju podatkov iz baze",
							"Napaka", JOptionPane.WARNING_MESSAGE);

				}
			}
		});
		bottomPanelZaGumb.add(btnIzpisiVse);

		btnPreveri = new JButton("Preveri");
		btnPreveri.setFont(fontGumbi);
		btnPreveri.setBackground(barvaGumba);
		btnPreveri.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					for (int i = 0; i < vsiJTextFieldi.size(); i++) {
						checkEmpty(vsiJTextFieldi.get(i), i);
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null,
							"Prišlo je do napake pri preverjanju glagolov. Opis napake:\n" + ex.toString(), "Napaka",
							JOptionPane.WARNING_MESSAGE);

				}

			}
		});
		bottomPanelZaGumb.add(btnPreveri);

		btnIzhod = new JButton("Izhod");
		btnIzhod.setFont(fontGumbi);
		btnIzhod.setBackground(barvaGumba);
		btnIzhod.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {

					int input = JOptionPane.showConfirmDialog(null, "Ali zelite zapreti program?", "Izhod",
							JOptionPane.INFORMATION_MESSAGE);

					if (input == 0) {
						System.exit(0);
					}

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null,
							"Prišlo je do napake pri izhodu iz programa. Opis napake:\n" + ex.toString(), "Napaka",
							JOptionPane.WARNING_MESSAGE);

				}
			}
		}

		);

		btnPonastavi = new JButton("Ponastavi");
		btnPonastavi.setFont(fontGumbi);
		btnPonastavi.setBackground(barvaGumba);
		btnPonastavi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// TOCKE + BAR
				userScore = 0;
				totalPossibleScore = 36;
				tockeTotal.setText(userScore + " / " + totalPossibleScore);
				progressBar.setValue(0);

				for (int i = 0; i < 36; i++) {

					vsiJTextFieldi.get(i).setText("");
					vsiJTextFieldi.get(i).setEditable(true);
					vsiJTextFieldi.get(i).setEnabled(true);
					vsiJTextFieldi.get(i).setBackground(Color.white);
					vsiJTextFieldi.get(i).setForeground(Color.black);
				}

				getColumns();
				disableButtons();

			}
		});
		bottomPanelZaGumb.add(btnPonastavi);
		bottomPanelZaGumb.add(btnIzhod);

		btnPridobiNove = new JButton("Pridobi Nove");
		btnPridobiNove.setToolTipText("BETA - NE DELA PRAVILNO");
		btnPridobiNove.setBackground(barvaGumba);
		btnPridobiNove.setFont(fontGumbi);
		btnPridobiNove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				try {
					prevodArr.clear();
					verbArr.clear();
					pastSimpleArr.clear();
					pastParticipleArr.clear();

					// Flusha vseGlagoleIzDB arraya in jih takoj zatem na novo nafila z novimi
					// glagoli
					vsiGlagoliIzDB.clear();
					fetchFromDB();

					// GET ID
					int idUporabnika = LoginForm.uporabnikID;

					// DELETE ENTRY
					query = "DELETE FROM helperTable WHERE ucenec=?";
					pSTMT = conn.prepareStatement(query);
					pSTMT.setInt(1, idUporabnika);
					pSTMT.execute();
					pSTMT.close();

					// ADD NEW VERB
					query = "INSERT INTO helperTable (ucenec, glagol)" + "VALUES (?, ?);";
					pSTMT = conn.prepareStatement(query);

					for (int i = 0; i < 9; i++) {
						pSTMT.setInt(1, idUporabnika);
						pSTMT.setInt(2, getRDM());
						pSTMT.execute();

					}

					pSTMT.close();

					// Nastavi nove vrednosti

					query = "SELECT glagoli.prevod, glagoli.verb, glagoli.pastSimple, glagoli.pastParticiple\n"
							+ "FROM users2 LEFT OUTER JOIN helperTable\n" + "	ON users2.id = helperTable.ucenec\n"
							+ "LEFT OUTER JOIN glagoli\n" + "	ON glagoli.id = helperTable.glagol\n"
							+ "	WHERE users2.id = " + LoginForm.uporabnikID + ";";

					pSTMT = conn.prepareStatement(query);

					rs = pSTMT.executeQuery();

					String pomen = null;
					String glagol = null;
					String tense = null;
					String part = null;

					while (rs.next()) {

						pomen = rs.getString(1);
						prevodArr.add(pomen);

						glagol = rs.getString(2);
						verbArr.add(glagol);

						tense = rs.getString(3);
						pastSimpleArr.add(tense);

						part = rs.getString(4);
						pastParticipleArr.add(part);

					}

					rs.close();
					pSTMT.close();

					
					// SPODNJI DEL DO CATCH BLOCKA SLUZI SAMO ZA RESET
					// TOCKE + BAR
					userScore = 0;
					totalPossibleScore = 36;
					tockeTotal.setText(userScore + " / " + totalPossibleScore);
					progressBar.setValue(0);

					for (int i = 0; i < 36; i++) {

						vsiJTextFieldi.get(i).setText("");
						vsiJTextFieldi.get(i).setEditable(true);
						vsiJTextFieldi.get(i).setEnabled(true);
						vsiJTextFieldi.get(i).setBackground(Color.white);
						vsiJTextFieldi.get(i).setForeground(Color.black);
					}

					getColumns();

				} catch (SQLException ex) {
					JOptionPane.showMessageDialog(null,
							"Prišlo je do napake pri samodejnemu določanju glagolov.\nOpis napake: " + ex.toString(),
							"Napaka", JOptionPane.WARNING_MESSAGE);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null,
							"Prišlo je do napake pri samodejnemu določanju glagolov.\nOpis napake: " + ex.toString(),
							"Napaka", JOptionPane.WARNING_MESSAGE);
				}

			}

			private int getRDM() {
				return (int) (Math.random() * 64 + 1);
			}
		});
		bottomPanelZaGumb.add(btnPridobiNove);

		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(SystemColor.inactiveCaption);
		frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new GridLayout(0, 4, 20, 30));

		// POSAMEZNI GUMBI
		// PREVOD
		gumbPrevod = new JButton("Prevod");
		gumbPrevod.setFont(fontGumbi);
		gumbPrevod.setBackground(barvaGumba);
		gumbPrevod.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				totalPossibleScore -= 9;
				progressBar.setMaximum(totalPossibleScore);
				tockeTotal.setText(userScore + " / " + totalPossibleScore);
				gumbPrevod.setEnabled(false);

				pSTMT = null;
				rs = null;
				query = "SELECT prevod FROM glagoli";

				try {
					pSTMT = conn.prepareStatement(query);
					rs = pSTMT.executeQuery();
					String prevod = null;

					while (rs.next()) {
						prevod = rs.getString(1);
						prevodArr.add(prevod);
					}

					int cntr = 0;
					for (int j = 0; j < 9; j++) {
						vsiJTextFieldi.get(j).setText(prevodArr.get(cntr));
						cntr++;
						vsiJTextFieldi.get(j).setEditable(false);
					}

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Opis napake: \n " + ex.getMessage(), "Napaka :(",
							JOptionPane.WARNING_MESSAGE);

				}
			}
		});
		mainPanel.add(gumbPrevod);

		// VERB SIMPLE TENSE
		gumbVerb = new JButton("Verb (infinitive)");
		gumbVerb.setFont(fontGumbi);
		gumbVerb.setBackground(barvaGumba);
		gumbVerb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				totalPossibleScore -= 9;
				progressBar.setMaximum(totalPossibleScore);
				tockeTotal.setText(userScore + " / " + totalPossibleScore);
				gumbVerb.setEnabled(false);

				PreparedStatement pstmt = null;
				ResultSet rst = null;
				String myQuery = "SELECT verb FROM glagoli";

				try {

					pstmt = conn.prepareStatement(myQuery);
					rst = pstmt.executeQuery();
					String verb = null;

					while (rst.next()) {
						verb = rst.getString(1);
						verbArr.add(verb);
					}

					int cntr = 0;
					for (int j = 9; j < 18; j++) {
						vsiJTextFieldi.get(j).setText(verbArr.get(cntr));
						cntr++;
						vsiJTextFieldi.get(j).setEditable(false);
					}

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Opis napake: \n " + ex.getMessage(), "Napaka :(",
							JOptionPane.WARNING_MESSAGE);

				}

			}
		});
		mainPanel.add(gumbVerb);

		// PAST SIMPLE GUMB
		gumbPastSimple = new JButton("Past simple form");
		gumbPastSimple.setFont(fontGumbi);
		gumbPastSimple.setBackground(barvaGumba);
		gumbPastSimple.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				totalPossibleScore -= 9;
				progressBar.setMaximum(totalPossibleScore);
				tockeTotal.setText(userScore + " / " + totalPossibleScore);
				gumbPastSimple.setEnabled(false);

				int cntr = 0;
				for (int j = 18; j < 27; j++) {
					vsiJTextFieldi.get(j).setText(pastSimpleArr.get(cntr));
					cntr++;
					vsiJTextFieldi.get(j).setEditable(false);
				}
			}
		});
		mainPanel.add(gumbPastSimple);

		// PAST PARTICIPLE GUMB
		gumbPastParticiple = new JButton("Past participle");
		gumbPastParticiple.setFont(new Font("Arial Black", Font.PLAIN, 13));
		gumbPastParticiple.setBackground(barvaGumba);
		gumbPastParticiple.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				totalPossibleScore -= 9;
				progressBar.setMaximum(totalPossibleScore);
				tockeTotal.setText(userScore + " / " + totalPossibleScore);
				gumbPastParticiple.setEnabled(false);

				int cntr = 0;
				for (int j = 27; j < 36; j++) {
					vsiJTextFieldi.get(j).setText(pastParticipleArr.get(cntr));
					cntr++;
					vsiJTextFieldi.get(j).setEditable(false);
				}

			}
		});
		mainPanel.add(gumbPastParticiple);

		// --> Samo vizualni del
		pomenR1 = new JTextField();
		pomenR1.setBackground(SystemColor.menu);
		pomenR1.setHorizontalAlignment(SwingConstants.CENTER);
		pomenR1.setFont(new Font("Arial", Font.PLAIN, 13));
		mainPanel.add(pomenR1);
		pomenR1.setColumns(10);

		glagolR1 = new JTextField();
		glagolR1.setBackground(SystemColor.menu);
		glagolR1.setHorizontalAlignment(SwingConstants.CENTER);
		glagolR1.setFont(fontTekst);
		mainPanel.add(glagolR1);
		glagolR1.setColumns(10);

		tenseR1 = new JTextField();
		tenseR1.setBackground(SystemColor.menu);
		tenseR1.setHorizontalAlignment(SwingConstants.CENTER);
		tenseR1.setFont(fontTekst);
		mainPanel.add(tenseR1);
		tenseR1.setColumns(10);

		partR1 = new JTextField();
		partR1.setBackground(SystemColor.menu);
		partR1.setHorizontalAlignment(SwingConstants.CENTER);
		partR1.setFont(fontTekst);
		mainPanel.add(partR1);
		partR1.setColumns(10);

		pomenR2 = new JTextField();
		pomenR2.setBackground(SystemColor.menu);
		pomenR2.setHorizontalAlignment(SwingConstants.CENTER);
		pomenR2.setFont(fontTekst);
		pomenR2.setColumns(10);
		mainPanel.add(pomenR2);

		glagolR2 = new JTextField();
		glagolR2.setBackground(SystemColor.menu);
		glagolR2.setHorizontalAlignment(SwingConstants.CENTER);
		glagolR2.setFont(fontTekst);
		glagolR2.setColumns(10);
		mainPanel.add(glagolR2);

		tenseR2 = new JTextField();
		tenseR2.setBackground(SystemColor.menu);
		tenseR2.setHorizontalAlignment(SwingConstants.CENTER);
		tenseR2.setFont(fontTekst);
		tenseR2.setColumns(10);
		mainPanel.add(tenseR2);

		partR2 = new JTextField();
		partR2.setBackground(SystemColor.menu);
		partR2.setHorizontalAlignment(SwingConstants.CENTER);
		partR2.setFont(fontTekst);
		partR2.setColumns(10);
		mainPanel.add(partR2);

		pomenR3 = new JTextField();
		pomenR3.setBackground(SystemColor.menu);
		pomenR3.setHorizontalAlignment(SwingConstants.CENTER);
		pomenR3.setFont(fontTekst);
		pomenR3.setColumns(10);
		mainPanel.add(pomenR3);

		glagolR3 = new JTextField();
		glagolR3.setBackground(SystemColor.menu);
		glagolR3.setHorizontalAlignment(SwingConstants.CENTER);
		glagolR3.setFont(fontTekst);
		glagolR3.setColumns(10);
		mainPanel.add(glagolR3);

		tenseR3 = new JTextField();
		tenseR3.setBackground(SystemColor.menu);
		tenseR3.setHorizontalAlignment(SwingConstants.CENTER);
		tenseR3.setFont(fontTekst);
		tenseR3.setColumns(10);
		mainPanel.add(tenseR3);

		partR3 = new JTextField();
		partR3.setBackground(SystemColor.menu);
		partR3.setHorizontalAlignment(SwingConstants.CENTER);
		partR3.setFont(fontTekst);
		partR3.setColumns(10);
		mainPanel.add(partR3);

		pomenR4 = new JTextField();
		pomenR4.setBackground(SystemColor.menu);
		pomenR4.setHorizontalAlignment(SwingConstants.CENTER);
		pomenR4.setFont(fontTekst);
		pomenR4.setColumns(10);
		mainPanel.add(pomenR4);

		glagolR4 = new JTextField();
		glagolR4.setBackground(SystemColor.menu);
		glagolR4.setHorizontalAlignment(SwingConstants.CENTER);
		glagolR4.setFont(fontTekst);
		glagolR4.setColumns(10);
		mainPanel.add(glagolR4);

		tenseR4 = new JTextField();
		tenseR4.setBackground(SystemColor.menu);
		tenseR4.setHorizontalAlignment(SwingConstants.CENTER);
		tenseR4.setFont(fontTekst);
		tenseR4.setColumns(10);
		mainPanel.add(tenseR4);

		partR4 = new JTextField();
		partR4.setBackground(SystemColor.menu);
		partR4.setHorizontalAlignment(SwingConstants.CENTER);
		partR4.setFont(fontTekst);
		partR4.setColumns(10);
		mainPanel.add(partR4);

		pomenR5 = new JTextField();
		pomenR5.setBackground(SystemColor.menu);
		pomenR5.setHorizontalAlignment(SwingConstants.CENTER);
		pomenR5.setFont(fontTekst);
		pomenR5.setColumns(10);
		mainPanel.add(pomenR5);

		glagolR5 = new JTextField();
		glagolR5.setBackground(SystemColor.menu);
		glagolR5.setHorizontalAlignment(SwingConstants.CENTER);
		glagolR5.setFont(fontTekst);
		glagolR5.setColumns(10);
		mainPanel.add(glagolR5);

		tenseR5 = new JTextField();
		tenseR5.setBackground(SystemColor.menu);
		tenseR5.setHorizontalAlignment(SwingConstants.CENTER);
		tenseR5.setFont(fontTekst);
		tenseR5.setColumns(10);
		mainPanel.add(tenseR5);

		partR5 = new JTextField();
		partR5.setBackground(SystemColor.menu);
		partR5.setHorizontalAlignment(SwingConstants.CENTER);
		partR5.setFont(fontTekst);
		partR5.setColumns(10);
		mainPanel.add(partR5);

		pomenR6 = new JTextField();
		pomenR6.setBackground(SystemColor.menu);
		pomenR6.setHorizontalAlignment(SwingConstants.CENTER);
		pomenR6.setFont(fontTekst);
		pomenR6.setColumns(10);
		mainPanel.add(pomenR6);

		glagolR6 = new JTextField();
		glagolR6.setBackground(SystemColor.menu);
		glagolR6.setHorizontalAlignment(SwingConstants.CENTER);
		glagolR6.setFont(fontTekst);
		glagolR6.setColumns(10);
		mainPanel.add(glagolR6);

		tenseR6 = new JTextField();
		tenseR6.setBackground(SystemColor.menu);
		tenseR6.setHorizontalAlignment(SwingConstants.CENTER);
		tenseR6.setFont(fontTekst);
		tenseR6.setColumns(10);
		mainPanel.add(tenseR6);

		partR6 = new JTextField();
		partR6.setBackground(SystemColor.menu);
		partR6.setHorizontalAlignment(SwingConstants.CENTER);
		partR6.setFont(fontTekst);
		partR6.setColumns(10);
		mainPanel.add(partR6);

		pomenR7 = new JTextField();
		pomenR7.setBackground(SystemColor.menu);
		pomenR7.setHorizontalAlignment(SwingConstants.CENTER);
		pomenR7.setFont(fontTekst);
		pomenR7.setColumns(10);
		mainPanel.add(pomenR7);

		glagolR7 = new JTextField();
		glagolR7.setBackground(SystemColor.menu);
		glagolR7.setHorizontalAlignment(SwingConstants.CENTER);
		glagolR7.setFont(fontTekst);
		glagolR7.setColumns(10);
		mainPanel.add(glagolR7);

		tenseR7 = new JTextField();
		tenseR7.setBackground(SystemColor.menu);
		tenseR7.setHorizontalAlignment(SwingConstants.CENTER);
		tenseR7.setFont(fontTekst);
		tenseR7.setColumns(10);
		mainPanel.add(tenseR7);

		partR7 = new JTextField();
		partR7.setBackground(SystemColor.menu);
		partR7.setHorizontalAlignment(SwingConstants.CENTER);
		partR7.setFont(fontTekst);
		partR7.setColumns(10);
		mainPanel.add(partR7);

		pomenR8 = new JTextField();
		pomenR8.setBackground(SystemColor.menu);
		pomenR8.setHorizontalAlignment(SwingConstants.CENTER);
		pomenR8.setFont(fontTekst);
		pomenR8.setColumns(10);
		mainPanel.add(pomenR8);

		glagolR8 = new JTextField();
		glagolR8.setBackground(SystemColor.menu);
		glagolR8.setHorizontalAlignment(SwingConstants.CENTER);
		glagolR8.setFont(fontTekst);
		glagolR8.setColumns(10);
		mainPanel.add(glagolR8);

		tenseR8 = new JTextField();
		tenseR8.setBackground(SystemColor.menu);
		tenseR8.setHorizontalAlignment(SwingConstants.CENTER);
		tenseR8.setFont(fontTekst);
		tenseR8.setColumns(10);
		mainPanel.add(tenseR8);

		partR8 = new JTextField();
		partR8.setBackground(SystemColor.menu);
		partR8.setHorizontalAlignment(SwingConstants.CENTER);
		partR8.setFont(fontTekst);
		partR8.setColumns(10);
		mainPanel.add(partR8);

		pomenR9 = new JTextField();
		pomenR9.setBackground(SystemColor.menu);
		pomenR9.setHorizontalAlignment(SwingConstants.CENTER);
		pomenR9.setFont(fontTekst);
		pomenR9.setColumns(10);
		mainPanel.add(pomenR9);

		glagolR9 = new JTextField();
		glagolR9.setBackground(SystemColor.menu);
		glagolR9.setHorizontalAlignment(SwingConstants.CENTER);
		glagolR9.setFont(fontTekst);
		glagolR9.setColumns(10);
		mainPanel.add(glagolR9);

		tenseR9 = new JTextField();
		tenseR9.setBackground(SystemColor.menu);
		tenseR9.setHorizontalAlignment(SwingConstants.CENTER);
		tenseR9.setFont(fontTekst);
		tenseR9.setColumns(10);
		mainPanel.add(tenseR9);

		partR9 = new JTextField();
		partR9.setBackground(SystemColor.menu);
		partR9.setHorizontalAlignment(SwingConstants.CENTER);
		partR9.setFont(fontTekst);
		partR9.setColumns(10);
		mainPanel.add(partR9);

		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.inactiveCaption);
		frame.getContentPane().add(panel, BorderLayout.WEST);

		progressPanel = new JPanel();
		progressPanel.setBackground(SystemColor.inactiveCaption);
		frame.getContentPane().add(progressPanel, BorderLayout.EAST);

		table = new JTable();
		progressPanel.add(table);

	}
}
