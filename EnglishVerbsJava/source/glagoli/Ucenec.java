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
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JTable;
import javax.swing.JProgressBar;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.SwingConstants;

public class Ucenec extends SqliteConnect {

	private JFrame frmUporabnik;
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
	private static ArrayList<JTextField> fieldArray = new ArrayList<>();

	// ArrayList ki zdruzi VSE glagole iz DB
	static ArrayList<String> combined = new ArrayList<String>();

	// Barve
	static Color incorrect = new Color(255, 99, 99);
	static Color correct = new Color(163, 222, 146);

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

	// Boilerplate
	public static void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ucenec window = new Ucenec();
					window.frmUporabnik.setVisible(true);
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


		gumbPrevod.setEnabled(false);
		gumbVerb.setEnabled(false);
		gumbPastSimple.setEnabled(false);
		gumbPastParticiple.setEnabled(false);

		// --> Menjava default foreground color barve pri gumbih
		defaultDisabled(gumbPrevod);
		defaultDisabled(gumbVerb);
		defaultDisabled(gumbPastSimple);
		defaultDisabled(gumbPastParticiple);

	}
	
	
	private static void getColumns() {
		
		String prvoPolje = String.valueOf(LoginForm.sequence.substring(0, 1));
		String drugoPolje = String.valueOf(LoginForm.sequence.substring(1, 2));
		String tretjePolje = String.valueOf(LoginForm.sequence.substring(2, 3));
		String cetrtoPolje = String.valueOf(LoginForm.sequence.substring(3));
		
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

		if (!fieldArray.get(n).getText().isBlank()) {
			checkInputVSexpected(pomenVar.getText(), combined.get(n), pomenVar);

			System.out.println("Input: " + pomenVar.getText());
			System.out.println("Expected: " + combined.get(n));
			System.out.println();
		} else {
			pomenVar.setEditable(false);
			pomenVar.setBackground(new Color(244, 164, 96));
			pomenVar.setForeground(Color.black);
			pomenVar.setText("/");
		}
	}

	public static void activateColumn(JButton columnName) {
		try {
			columnName.doClick();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Opis napake:\nUporabnik se nima dolocenih glagolov\nProsim, dodajte glagole.", "Napaka :(",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	// Funkcija ki polni ArrayList od vseh polji z glagoli v vseh oblikah
	// Spisana za lazje delanje z loopi, preverjanje, resetiranje itd
	private static void fillArrayWithVerbs() {

		fieldArray.add(pomenR1);
		fieldArray.add(pomenR2);
		fieldArray.add(pomenR3);
		fieldArray.add(pomenR4);
		fieldArray.add(pomenR5);
		fieldArray.add(pomenR6);
		fieldArray.add(pomenR7);
		fieldArray.add(pomenR8);
		fieldArray.add(pomenR9);

		fieldArray.add(glagolR1);
		fieldArray.add(glagolR2);
		fieldArray.add(glagolR3);
		fieldArray.add(glagolR4);
		fieldArray.add(glagolR5);
		fieldArray.add(glagolR6);
		fieldArray.add(glagolR7);
		fieldArray.add(glagolR8);
		fieldArray.add(glagolR9);

		fieldArray.add(tenseR1);
		fieldArray.add(tenseR2);
		fieldArray.add(tenseR3);
		fieldArray.add(tenseR4);
		fieldArray.add(tenseR5);
		fieldArray.add(tenseR6);
		fieldArray.add(tenseR7);
		fieldArray.add(tenseR8);
		fieldArray.add(tenseR9);

		fieldArray.add(partR1);
		fieldArray.add(partR2);
		fieldArray.add(partR3);
		fieldArray.add(partR4);
		fieldArray.add(partR5);
		fieldArray.add(partR6);
		fieldArray.add(partR7);
		fieldArray.add(partR8);
		fieldArray.add(partR9);

	}

	// Funkcija prebere vnose iz DB in jih vnese v Array za nadaljno delo
	private static void fetchFromDB() {

		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String query = "SELECT glagoli.prevod, glagoli.verb, glagoli.pastSimple, glagoli.pastParticiple\n"
				+ "FROM users2 LEFT OUTER JOIN helperTable\n" + "	ON users2.id = helperTable.ucenec\n"
				+ "LEFT OUTER JOIN glagoli\n" + "	ON glagoli.id = helperTable.glagol\n" + "	WHERE users2.id = "
				+ LoginForm.userID + ";";

		try {

			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
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

			combined = new ArrayList<String>();
			combined.addAll(prevodArr);
			combined.addAll(verbArr);
			combined.addAll(pastSimpleArr);
			combined.addAll(pastParticipleArr);

		} catch (Exception ex) {
			//TODO: FIX ERROR MSG			
			JOptionPane.showMessageDialog(null, "Opis napake: \n " + ex.getMessage(), "Napaka :(",
					JOptionPane.WARNING_MESSAGE);

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
		frmUporabnik = new JFrame();
		frmUporabnik.setTitle("Ucenje Glagolov UPORABNIK: " + LoginForm.uporabniskoIme.substring(0, 1).toUpperCase()
				+ LoginForm.uporabniskoIme.substring(1));
		frmUporabnik.setBounds(100, 100, 969, 638);
		frmUporabnik.getContentPane().setLayout(new BorderLayout(0, 0));
		frmUporabnik.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		frmUporabnik.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				btnIzhod.doClick();

			}
		});

		JPanel uporabnikToolbar = new JPanel();
		uporabnikToolbar.setBackground(SystemColor.inactiveCaption);
		frmUporabnik.getContentPane().add(uporabnikToolbar, BorderLayout.NORTH);

		JLabel uporabniskoIme = new JLabel(
				LoginForm.uporabniskoIme.substring(0, 1).toUpperCase() + LoginForm.uporabniskoIme.substring(1));
		uporabniskoIme.setFont(new Font("Arial Black", Font.PLAIN, 13));
		uporabnikToolbar.add(uporabniskoIme);

		progressBar = new JProgressBar(0, totalPossibleScore);
		progressBar.setForeground(new Color(163, 222, 146));
		uporabnikToolbar.add(progressBar);

		tockeTotal = new JLabel(userScore + " / " + totalPossibleScore);
		tockeTotal.setFont(new Font("Arial Black", Font.PLAIN, 13));
		uporabnikToolbar.add(tockeTotal);

		JPanel bottomPanelZaGumb = new JPanel();
		bottomPanelZaGumb.setBackground(SystemColor.inactiveCaption);
		frmUporabnik.getContentPane().add(bottomPanelZaGumb, BorderLayout.SOUTH);

		btnIzpisiVse = new JButton("Izpisi Vse");
		btnIzpisiVse.setFont(new Font("Arial Black", Font.PLAIN, 13));
		btnIzpisiVse.setBackground(new Color(244, 164, 96));
		btnIzpisiVse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				PreparedStatement pstmt = null;
				ResultSet rs = null;
				String myQuery = "SELECT glagoli.prevod, glagoli.verb, glagoli.pastSimple, glagoli.pastParticiple\n"
						+ "FROM users2 LEFT OUTER JOIN helperTable\n" + "	ON users2.id = helperTable.ucenec\n"
						+ "LEFT OUTER JOIN glagoli\n" + "	ON glagoli.id = helperTable.glagol\n"
						+ "	WHERE users2.id = " + LoginForm.userID + ";";

				try {

					pstmt = conn.prepareStatement(myQuery);
					rs = pstmt.executeQuery();
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
					int cntr = 0;
					for (int j = 0; j < 9; j++) {
						fieldArray.get(j).setText(prevodArr.get(cntr));
						cntr++;
					}

					cntr = 0;
					for (int j = 9; j < 18; j++) {
						fieldArray.get(j).setText(verbArr.get(cntr));
						cntr++;
					}

					cntr = 0;
					for (int j = 18; j < 27; j++) {
						fieldArray.get(j).setText(pastSimpleArr.get(cntr));
						cntr++;
					}

					for (int j = 27; j < 36; j++) {
						fieldArray.get(j).setText(pastParticipleArr.get(cntr));
						cntr++;
					}

					List<String> combined = new ArrayList<String>();
					combined.addAll(prevodArr);
					combined.addAll(verbArr);
					combined.addAll(pastSimpleArr);
					combined.addAll(pastParticipleArr);

				} catch (Exception ex) {

					JOptionPane.showMessageDialog(null,
							"Opis napake:\nUporabnik se nima dolocenih glagolov\nProsim, dodajte glagole.", "Napaka :(",
							JOptionPane.WARNING_MESSAGE);

				}
			}
		});
		bottomPanelZaGumb.add(btnIzpisiVse);

		btnPreveri = new JButton("Preveri");
		btnPreveri.setFont(new Font("Arial Black", Font.PLAIN, 13));
		btnPreveri.setBackground(new Color(244, 164, 96));
		btnPreveri.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				PreparedStatement pstmt = null;
				ResultSet rst = null;
				String myQuery = "SELECT glagoli.prevod, glagoli.verb, glagoli.pastSimple, glagoli.pastParticiple\n"
						+ "FROM users2 LEFT OUTER JOIN helperTable\n" + "	ON users2.id = helperTable.ucenec\n"
						+ "LEFT OUTER JOIN glagoli\n" + "	ON glagoli.id = helperTable.glagol\n"
						+ "	WHERE users2.id = " + LoginForm.userID + ";";

				try {

					pstmt = conn.prepareStatement(myQuery);
					rst = pstmt.executeQuery();
					String pomen = null;
					String glagol = null;
					String tense = null;
					String part = null;

					while (rst.next()) {

						pomen = rst.getString(1);
						prevodArr.add(pomen);

						glagol = rst.getString(2);
						verbArr.add(glagol);

						tense = rst.getString(3);
						pastSimpleArr.add(tense);

						part = rst.getString(4);
						pastParticipleArr.add(part);

					}

					for (int i = 0; i < fieldArray.size(); i++) {
						checkEmpty(fieldArray.get(i), i);
					}

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Opis napake: \n " + ex.getMessage(), "Napaka :(",
							JOptionPane.WARNING_MESSAGE);

				}

			}
		});
		bottomPanelZaGumb.add(btnPreveri);

		btnIzhod = new JButton("Izhod");
		btnIzhod.setFont(new Font("Arial Black", Font.PLAIN, 13));
		btnIzhod.setBackground(new Color(244, 164, 96));
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
		}

		);

		btnPonastavi = new JButton("Ponastavi");
		btnPonastavi.setFont(new Font("Arial Black", Font.PLAIN, 13));
		btnPonastavi.setBackground(new Color(244, 164, 96));
		btnPonastavi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// TOCKE + BAR
				userScore = 0;
				totalPossibleScore = 36;
				tockeTotal.setText(userScore + " / " + totalPossibleScore);
				progressBar.setValue(0);

				for (int i = 0; i < 36; i++) {

					fieldArray.get(i).setText("");
					fieldArray.get(i).setEditable(true);
					fieldArray.get(i).setEnabled(true);
					fieldArray.get(i).setBackground(Color.white);
					fieldArray.get(i).setForeground(Color.black);
				}

			}
		});
		bottomPanelZaGumb.add(btnPonastavi);
		bottomPanelZaGumb.add(btnIzhod);

		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(SystemColor.inactiveCaption);
		frmUporabnik.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new GridLayout(0, 4, 20, 30));

		// POSAMEZNI GUMBI
		// PREVOD
		gumbPrevod = new JButton("Prevod");
		gumbPrevod.setFont(new Font("Arial Black", Font.PLAIN, 13));
		gumbPrevod.setBackground(new Color(244, 164, 96));
		gumbPrevod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				totalPossibleScore -= 9;
				progressBar.setMaximum(totalPossibleScore);
				tockeTotal.setText(userScore + " / " + totalPossibleScore);
				gumbPrevod.setEnabled(false);

				PreparedStatement pstmt = null;
				ResultSet rst = null;
				String myQuery = "SELECT prevod FROM glagoli";

				try {
					pstmt = conn.prepareStatement(myQuery);
					rst = pstmt.executeQuery();
					String prevod = null;

					while (rst.next()) {
						prevod = rst.getString(1);
						prevodArr.add(prevod);
					}

					int cntr = 0;
					for (int j = 0; j < 9; j++) {
						fieldArray.get(j).setText(prevodArr.get(cntr));
						cntr++;
						fieldArray.get(j).setEditable(false);
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
		gumbVerb.setFont(new Font("Arial Black", Font.PLAIN, 13));
		gumbVerb.setBackground(new Color(244, 164, 96));
		gumbVerb.addActionListener(new ActionListener() {
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
						fieldArray.get(j).setText(verbArr.get(cntr));
						cntr++;
						fieldArray.get(j).setEditable(false);
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
		gumbPastSimple.setFont(new Font("Arial Black", Font.PLAIN, 13));
		gumbPastSimple.setBackground(new Color(244, 164, 96));
		gumbPastSimple.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				totalPossibleScore -= 9;
				progressBar.setMaximum(totalPossibleScore);
				tockeTotal.setText(userScore + " / " + totalPossibleScore);
				gumbPastSimple.setEnabled(false);

				int cntr = 0;
				for (int j = 18; j < 27; j++) {
					fieldArray.get(j).setText(pastSimpleArr.get(cntr));
					cntr++;
					fieldArray.get(j).setEditable(false);
				}
			}
		});
		mainPanel.add(gumbPastSimple);

		// PAST PARTICIPLE GUMB
		gumbPastParticiple = new JButton("Past participle");
		gumbPastParticiple.setFont(new Font("Arial Black", Font.PLAIN, 13));
		gumbPastParticiple.setBackground(new Color(244, 164, 96));
		gumbPastParticiple.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				totalPossibleScore -= 9;
				progressBar.setMaximum(totalPossibleScore);
				tockeTotal.setText(userScore + " / " + totalPossibleScore);
				gumbPastParticiple.setEnabled(false);

				int cntr = 0;
				for (int j = 27; j < 36; j++) {
					fieldArray.get(j).setText(pastParticipleArr.get(cntr));
					cntr++;
					fieldArray.get(j).setEditable(false);
				}

			}
		});
		mainPanel.add(gumbPastParticiple);

		pomenR1 = new JTextField();
		pomenR1.setBackground(SystemColor.menu);
		pomenR1.setHorizontalAlignment(SwingConstants.CENTER);
		pomenR1.setFont(new Font("Arial", Font.BOLD, 13));
		mainPanel.add(pomenR1);
		pomenR1.setColumns(10);

		glagolR1 = new JTextField();
		glagolR1.setBackground(SystemColor.menu);
		glagolR1.setHorizontalAlignment(SwingConstants.CENTER);
		glagolR1.setFont(new Font("Arial", Font.BOLD, 13));
		mainPanel.add(glagolR1);
		glagolR1.setColumns(10);

		tenseR1 = new JTextField();
		tenseR1.setBackground(SystemColor.menu);
		tenseR1.setHorizontalAlignment(SwingConstants.CENTER);
		tenseR1.setFont(new Font("Arial", Font.BOLD, 13));
		mainPanel.add(tenseR1);
		tenseR1.setColumns(10);

		partR1 = new JTextField();
		partR1.setBackground(SystemColor.menu);
		partR1.setHorizontalAlignment(SwingConstants.CENTER);
		partR1.setFont(new Font("Arial", Font.BOLD, 13));
		mainPanel.add(partR1);
		partR1.setColumns(10);

		pomenR2 = new JTextField();
		pomenR2.setBackground(SystemColor.menu);
		pomenR2.setHorizontalAlignment(SwingConstants.CENTER);
		pomenR2.setFont(new Font("Arial", Font.BOLD, 13));
		pomenR2.setColumns(10);
		mainPanel.add(pomenR2);

		glagolR2 = new JTextField();
		glagolR2.setBackground(SystemColor.menu);
		glagolR2.setHorizontalAlignment(SwingConstants.CENTER);
		glagolR2.setFont(new Font("Arial", Font.BOLD, 13));
		glagolR2.setColumns(10);
		mainPanel.add(glagolR2);

		tenseR2 = new JTextField();
		tenseR2.setBackground(SystemColor.menu);
		tenseR2.setHorizontalAlignment(SwingConstants.CENTER);
		tenseR2.setFont(new Font("Arial", Font.BOLD, 13));
		tenseR2.setColumns(10);
		mainPanel.add(tenseR2);

		partR2 = new JTextField();
		partR2.setBackground(SystemColor.menu);
		partR2.setHorizontalAlignment(SwingConstants.CENTER);
		partR2.setFont(new Font("Arial", Font.BOLD, 13));
		partR2.setColumns(10);
		mainPanel.add(partR2);

		pomenR3 = new JTextField();
		pomenR3.setBackground(SystemColor.menu);
		pomenR3.setHorizontalAlignment(SwingConstants.CENTER);
		pomenR3.setFont(new Font("Arial", Font.BOLD, 13));
		pomenR3.setColumns(10);
		mainPanel.add(pomenR3);

		glagolR3 = new JTextField();
		glagolR3.setBackground(SystemColor.menu);
		glagolR3.setHorizontalAlignment(SwingConstants.CENTER);
		glagolR3.setFont(new Font("Arial", Font.BOLD, 13));
		glagolR3.setColumns(10);
		mainPanel.add(glagolR3);

		tenseR3 = new JTextField();
		tenseR3.setBackground(SystemColor.menu);
		tenseR3.setHorizontalAlignment(SwingConstants.CENTER);
		tenseR3.setFont(new Font("Arial", Font.BOLD, 13));
		tenseR3.setColumns(10);
		mainPanel.add(tenseR3);

		partR3 = new JTextField();
		partR3.setBackground(SystemColor.menu);
		partR3.setHorizontalAlignment(SwingConstants.CENTER);
		partR3.setFont(new Font("Arial", Font.BOLD, 13));
		partR3.setColumns(10);
		mainPanel.add(partR3);

		pomenR4 = new JTextField();
		pomenR4.setBackground(SystemColor.menu);
		pomenR4.setHorizontalAlignment(SwingConstants.CENTER);
		pomenR4.setFont(new Font("Arial", Font.BOLD, 13));
		pomenR4.setColumns(10);
		mainPanel.add(pomenR4);

		glagolR4 = new JTextField();
		glagolR4.setBackground(SystemColor.menu);
		glagolR4.setHorizontalAlignment(SwingConstants.CENTER);
		glagolR4.setFont(new Font("Arial", Font.BOLD, 13));
		glagolR4.setColumns(10);
		mainPanel.add(glagolR4);

		tenseR4 = new JTextField();
		tenseR4.setBackground(SystemColor.menu);
		tenseR4.setHorizontalAlignment(SwingConstants.CENTER);
		tenseR4.setFont(new Font("Arial", Font.BOLD, 13));
		tenseR4.setColumns(10);
		mainPanel.add(tenseR4);

		partR4 = new JTextField();
		partR4.setBackground(SystemColor.menu);
		partR4.setHorizontalAlignment(SwingConstants.CENTER);
		partR4.setFont(new Font("Arial", Font.BOLD, 13));
		partR4.setColumns(10);
		mainPanel.add(partR4);

		pomenR5 = new JTextField();
		pomenR5.setBackground(SystemColor.menu);
		pomenR5.setHorizontalAlignment(SwingConstants.CENTER);
		pomenR5.setFont(new Font("Arial", Font.BOLD, 13));
		pomenR5.setColumns(10);
		mainPanel.add(pomenR5);

		glagolR5 = new JTextField();
		glagolR5.setBackground(SystemColor.menu);
		glagolR5.setHorizontalAlignment(SwingConstants.CENTER);
		glagolR5.setFont(new Font("Arial", Font.BOLD, 13));
		glagolR5.setColumns(10);
		mainPanel.add(glagolR5);

		tenseR5 = new JTextField();
		tenseR5.setBackground(SystemColor.menu);
		tenseR5.setHorizontalAlignment(SwingConstants.CENTER);
		tenseR5.setFont(new Font("Arial", Font.BOLD, 13));
		tenseR5.setColumns(10);
		mainPanel.add(tenseR5);

		partR5 = new JTextField();
		partR5.setBackground(SystemColor.menu);
		partR5.setHorizontalAlignment(SwingConstants.CENTER);
		partR5.setFont(new Font("Arial", Font.BOLD, 13));
		partR5.setColumns(10);
		mainPanel.add(partR5);

		pomenR6 = new JTextField();
		pomenR6.setBackground(SystemColor.menu);
		pomenR6.setHorizontalAlignment(SwingConstants.CENTER);
		pomenR6.setFont(new Font("Arial", Font.BOLD, 13));
		pomenR6.setColumns(10);
		mainPanel.add(pomenR6);

		glagolR6 = new JTextField();
		glagolR6.setBackground(SystemColor.menu);
		glagolR6.setHorizontalAlignment(SwingConstants.CENTER);
		glagolR6.setFont(new Font("Arial", Font.BOLD, 13));
		glagolR6.setColumns(10);
		mainPanel.add(glagolR6);

		tenseR6 = new JTextField();
		tenseR6.setBackground(SystemColor.menu);
		tenseR6.setHorizontalAlignment(SwingConstants.CENTER);
		tenseR6.setFont(new Font("Arial", Font.BOLD, 13));
		tenseR6.setColumns(10);
		mainPanel.add(tenseR6);

		partR6 = new JTextField();
		partR6.setBackground(SystemColor.menu);
		partR6.setHorizontalAlignment(SwingConstants.CENTER);
		partR6.setFont(new Font("Arial", Font.BOLD, 13));
		partR6.setColumns(10);
		mainPanel.add(partR6);

		pomenR7 = new JTextField();
		pomenR7.setBackground(SystemColor.menu);
		pomenR7.setHorizontalAlignment(SwingConstants.CENTER);
		pomenR7.setFont(new Font("Arial", Font.BOLD, 13));
		pomenR7.setColumns(10);
		mainPanel.add(pomenR7);

		glagolR7 = new JTextField();
		glagolR7.setBackground(SystemColor.menu);
		glagolR7.setHorizontalAlignment(SwingConstants.CENTER);
		glagolR7.setFont(new Font("Arial", Font.BOLD, 13));
		glagolR7.setColumns(10);
		mainPanel.add(glagolR7);

		tenseR7 = new JTextField();
		tenseR7.setBackground(SystemColor.menu);
		tenseR7.setHorizontalAlignment(SwingConstants.CENTER);
		tenseR7.setFont(new Font("Arial", Font.BOLD, 13));
		tenseR7.setColumns(10);
		mainPanel.add(tenseR7);

		partR7 = new JTextField();
		partR7.setBackground(SystemColor.menu);
		partR7.setHorizontalAlignment(SwingConstants.CENTER);
		partR7.setFont(new Font("Arial", Font.BOLD, 13));
		partR7.setColumns(10);
		mainPanel.add(partR7);

		pomenR8 = new JTextField();
		pomenR8.setBackground(SystemColor.menu);
		pomenR8.setHorizontalAlignment(SwingConstants.CENTER);
		pomenR8.setFont(new Font("Arial", Font.BOLD, 13));
		pomenR8.setColumns(10);
		mainPanel.add(pomenR8);

		glagolR8 = new JTextField();
		glagolR8.setBackground(SystemColor.menu);
		glagolR8.setHorizontalAlignment(SwingConstants.CENTER);
		glagolR8.setFont(new Font("Arial", Font.BOLD, 13));
		glagolR8.setColumns(10);
		mainPanel.add(glagolR8);

		tenseR8 = new JTextField();
		tenseR8.setBackground(SystemColor.menu);
		tenseR8.setHorizontalAlignment(SwingConstants.CENTER);
		tenseR8.setFont(new Font("Arial", Font.BOLD, 13));
		tenseR8.setColumns(10);
		mainPanel.add(tenseR8);

		partR8 = new JTextField();
		partR8.setBackground(SystemColor.menu);
		partR8.setHorizontalAlignment(SwingConstants.CENTER);
		partR8.setFont(new Font("Arial", Font.BOLD, 13));
		partR8.setColumns(10);
		mainPanel.add(partR8);

		pomenR9 = new JTextField();
		pomenR9.setBackground(SystemColor.menu);
		pomenR9.setHorizontalAlignment(SwingConstants.CENTER);
		pomenR9.setFont(new Font("Arial", Font.BOLD, 13));
		pomenR9.setColumns(10);
		mainPanel.add(pomenR9);

		glagolR9 = new JTextField();
		glagolR9.setBackground(SystemColor.menu);
		glagolR9.setHorizontalAlignment(SwingConstants.CENTER);
		glagolR9.setFont(new Font("Arial", Font.BOLD, 13));
		glagolR9.setColumns(10);
		mainPanel.add(glagolR9);

		tenseR9 = new JTextField();
		tenseR9.setBackground(SystemColor.menu);
		tenseR9.setHorizontalAlignment(SwingConstants.CENTER);
		tenseR9.setFont(new Font("Arial", Font.BOLD, 13));
		tenseR9.setColumns(10);
		mainPanel.add(tenseR9);

		partR9 = new JTextField();
		partR9.setBackground(SystemColor.menu);
		partR9.setHorizontalAlignment(SwingConstants.CENTER);
		partR9.setFont(new Font("Arial", Font.BOLD, 13));
		partR9.setColumns(10);
		mainPanel.add(partR9);

		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.inactiveCaption);
		frmUporabnik.getContentPane().add(panel, BorderLayout.WEST);

		progressPanel = new JPanel();
		progressPanel.setBackground(SystemColor.inactiveCaption);
		frmUporabnik.getContentPane().add(progressPanel, BorderLayout.EAST);

		table = new JTable();
		progressPanel.add(table);

	}
}
