package glagoli;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridLayout;
import javax.swing.SwingConstants;

import DB.SqliteConnect;
import net.proteanit.sql.DbUtils;

import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTable;

public class UcenecWindow extends SqliteConnect {

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
	private JButton izpisiGlagoleBtn;
	private JTable table;
	private JButton btnNewButton;
	private static ArrayList<String> pomenArr = new ArrayList<String>();
	private static ArrayList<String> glagolArr = new ArrayList<String>();
	private static ArrayList<String> tenseArr = new ArrayList<String>();
	private static ArrayList<String> partArr = new ArrayList<String>();
	
	private static ArrayList<JTextField> fieldArray = new ArrayList<>();
	

	static Color incorrect = new Color(255, 102, 102);
	static Color correct = new Color(102, 255, 102);

	public static void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UcenecWindow window = new UcenecWindow();
					window.frmUporabnik.setVisible(true);
					
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
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void checkEmpty(ArrayList<JTextField> pomen, int n) {
		
		
		if(!fieldArray.get(n).getText().isBlank() || pomenArr.get(n).isBlank()) {
			check(pomen.get(n).getText(), pomenArr.get(n), pomen.get(n));
		}
		else {
			pomen.get(n).setEnabled(false);
			pomen.get(n).setBackground(incorrect);
		}
	}
	
	
	

	public UcenecWindow() {
		initialize();
	}

	private static void check(String input, String expected, JTextField cell) {

		if (input.isEmpty() || input.isBlank() || expected.isBlank() || expected.isEmpty()) {
			cell.setBackground(Color.gray);
			cell.setEnabled(false);

		} else if (!input.equals(expected)) {
			cell.setEnabled(false);
			cell.setBackground(incorrect);
			cell.setForeground(Color.black);
		} else {
			cell.setEnabled(true);
			cell.setBackground(correct);
		}

	}

	private void initialize() {
		frmUporabnik = new JFrame();
		frmUporabnik.setTitle("Ucenje Glagolov UPORABNIK: " + LoginForm.uporabniskoIme);
		frmUporabnik.setBounds(100, 100, 969, 638);
		frmUporabnik.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmUporabnik.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel uporabnikToolbar = new JPanel();
		frmUporabnik.getContentPane().add(uporabnikToolbar, BorderLayout.NORTH);

		JLabel uporabniskoIme = new JLabel(LoginForm.uporabniskoIme);
		uporabnikToolbar.add(uporabniskoIme);

		JPanel bottomPanelZaGumb = new JPanel();
		frmUporabnik.getContentPane().add(bottomPanelZaGumb, BorderLayout.SOUTH);

		izpisiGlagoleBtn = new JButton("Izpisi");
		izpisiGlagoleBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				conn = poveziBazo();
				PreparedStatement pstmt = null;
				ResultSet rst = null;
				String myQuery = "SELECT pomen, glagol, tense, part FROM glagoli";

				try {

					pstmt = conn.prepareStatement(myQuery);
					rst = pstmt.executeQuery();
					String pomen = null;
					String glagol = null;
					String tense = null;
					String part = null;

					while (rst.next()) {

						pomen = rst.getString(1);
						pomenArr.add(pomen);

						glagol = rst.getString(2);
						glagolArr.add(glagol);

						tense = rst.getString(3);
						tenseArr.add(tense);

						part = rst.getString(4);
						partArr.add(part);

					}
					
					
					int cntr = 0;
					for (int j = 0; j < 9; j++) {
						fieldArray.get(j).setText(pomenArr.get(cntr));
						cntr++;
					}
					cntr = 0;
					
					for (int j = 9; j < 18; j++) {
						fieldArray.get(j).setText(glagolArr.get(cntr));
						cntr++;
					}
					
					cntr = 0;
					for (int j = 18; j < 27; j++) {	
						fieldArray.get(j).setText(tenseArr.get(cntr));
						cntr++;
					}
					
					cntr = 0;					
					for (int j = 27; j < 36; j++) {	
						fieldArray.get(j).setText(partArr.get(cntr));
						cntr++;
					}
					
							

				} catch (Exception ex) {
					System.out.println("error" + ex);

				}
			}
		});
		bottomPanelZaGumb.add(izpisiGlagoleBtn);

		JButton preveriBtn = new JButton("Preveri");
		preveriBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				conn = poveziBazo();
				PreparedStatement pstmt = null;
				ResultSet rst = null;
				String myQuery = "SELECT pomen, glagol, tense, part FROM glagoli";

				try {

					conn = poveziBazo();
					pstmt = conn.prepareStatement(myQuery);
					rst = pstmt.executeQuery();
					String pomen = null;
					String glagol = null;
					String tense = null;
					String part = null;

					while (rst.next()) {

						pomen = rst.getString(1);
						pomenArr.add(pomen);

						glagol = rst.getString(2);
						glagolArr.add(glagol);

						tense = rst.getString(3);
						tenseArr.add(tense);

						part = rst.getString(4);
						partArr.add(part);

					}

				} catch (Exception ex) {
					System.out.println("error" + ex);

				}
				
				//ZACETEK PROBLEMA
				
				int checkCounter = 0;
				while(  checkCounter < 18) {
					checkEmpty(fieldArray, checkCounter);
					checkCounter++;
				}
				
				
			}
		});
		bottomPanelZaGumb.add(preveriBtn);
		

		JButton izhod = new JButton("Izhod");
		izhod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				System.exit(0);
			}
		});
		bottomPanelZaGumb.add(izhod);

		btnNewButton = new JButton("TableBtn");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {

					String query = "SELECT * FROM glagoli";
					pSTMT = conn.prepareStatement(query);

					rs = pSTMT.executeQuery();

					table.setModel(DbUtils.resultSetToTableModel(rs));

				} catch (Exception ex) {

				}
			}
		});
		bottomPanelZaGumb.add(btnNewButton);

		JPanel mainPanel = new JPanel();
		frmUporabnik.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new GridLayout(0, 4, 20, 30));

		JLabel sloLabel = new JLabel("Pomen");
		sloLabel.setHorizontalAlignment(SwingConstants.LEFT);
		mainPanel.add(sloLabel);

		JLabel glagolLabel = new JLabel("Glagol");
		mainPanel.add(glagolLabel);

		JLabel tenseLabel = new JLabel("Past Tense");
		mainPanel.add(tenseLabel);

		JLabel partLabel = new JLabel("Past Participle");
		mainPanel.add(partLabel);

		pomenR1 = new JTextField();
		mainPanel.add(pomenR1);
		pomenR1.setColumns(10);

		glagolR1 = new JTextField();
		mainPanel.add(glagolR1);
		glagolR1.setColumns(10);

		tenseR1 = new JTextField();
		mainPanel.add(tenseR1);
		tenseR1.setColumns(10);

		partR1 = new JTextField();
		mainPanel.add(partR1);
		partR1.setColumns(10);

		pomenR2 = new JTextField();
		pomenR2.setColumns(10);
		mainPanel.add(pomenR2);

		glagolR2 = new JTextField();
		glagolR2.setColumns(10);
		mainPanel.add(glagolR2);

		tenseR2 = new JTextField();
		tenseR2.setColumns(10);
		mainPanel.add(tenseR2);

		partR2 = new JTextField();
		partR2.setColumns(10);
		mainPanel.add(partR2);

		pomenR3 = new JTextField();
		pomenR3.setColumns(10);
		mainPanel.add(pomenR3);

		glagolR3 = new JTextField();
		glagolR3.setColumns(10);
		mainPanel.add(glagolR3);

		tenseR3 = new JTextField();
		tenseR3.setColumns(10);
		mainPanel.add(tenseR3);

		partR3 = new JTextField();
		partR3.setColumns(10);
		mainPanel.add(partR3);

		pomenR4 = new JTextField();
		pomenR4.setColumns(10);
		mainPanel.add(pomenR4);

		glagolR4 = new JTextField();
		glagolR4.setColumns(10);
		mainPanel.add(glagolR4);

		tenseR4 = new JTextField();
		tenseR4.setColumns(10);
		mainPanel.add(tenseR4);

		partR4 = new JTextField();
		partR4.setColumns(10);
		mainPanel.add(partR4);

		pomenR5 = new JTextField();
		pomenR5.setColumns(10);
		mainPanel.add(pomenR5);

		glagolR5 = new JTextField();
		glagolR5.setColumns(10);
		mainPanel.add(glagolR5);

		tenseR5 = new JTextField();
		tenseR5.setColumns(10);
		mainPanel.add(tenseR5);

		partR5 = new JTextField();
		partR5.setColumns(10);
		mainPanel.add(partR5);

		pomenR6 = new JTextField();
		pomenR6.setColumns(10);
		mainPanel.add(pomenR6);

		glagolR6 = new JTextField();
		glagolR6.setColumns(10);
		mainPanel.add(glagolR6);

		tenseR6 = new JTextField();
		tenseR6.setColumns(10);
		mainPanel.add(tenseR6);

		partR6 = new JTextField();
		partR6.setColumns(10);
		mainPanel.add(partR6);

		pomenR7 = new JTextField();
		pomenR7.setColumns(10);
		mainPanel.add(pomenR7);

		glagolR7 = new JTextField();
		glagolR7.setColumns(10);
		mainPanel.add(glagolR7);

		tenseR7 = new JTextField();
		tenseR7.setColumns(10);
		mainPanel.add(tenseR7);

		partR7 = new JTextField();
		partR7.setColumns(10);
		mainPanel.add(partR7);

		pomenR8 = new JTextField();
		pomenR8.setColumns(10);
		mainPanel.add(pomenR8);

		glagolR8 = new JTextField();
		glagolR8.setColumns(10);
		mainPanel.add(glagolR8);

		tenseR8 = new JTextField();
		tenseR8.setColumns(10);
		mainPanel.add(tenseR8);

		partR8 = new JTextField();
		partR8.setColumns(10);
		mainPanel.add(partR8);

		pomenR9 = new JTextField();
		pomenR9.setColumns(10);
		mainPanel.add(pomenR9);

		glagolR9 = new JTextField();
		glagolR9.setColumns(10);
		mainPanel.add(glagolR9);

		tenseR9 = new JTextField();
		tenseR9.setColumns(10);
		mainPanel.add(tenseR9);

		partR9 = new JTextField();
		partR9.setColumns(10);
		mainPanel.add(partR9);

		JPanel panel = new JPanel();
		frmUporabnik.getContentPane().add(panel, BorderLayout.WEST);

		progressPanel = new JPanel();
		frmUporabnik.getContentPane().add(progressPanel, BorderLayout.EAST);

		table = new JTable();
		progressPanel.add(table);

	}
}
