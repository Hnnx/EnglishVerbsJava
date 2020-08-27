package glagoli;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.sound.midi.Soundbank;
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
import java.sql.Statement;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JProgressBar;
import javax.swing.JTable;

public class UcenecWindow extends SqliteConnect {

	private JFrame frmUporabnik;
	static private JTextField pomenR1;
	static private JTextField glagolR1;
	static private JTextField tenseR1;
	static private JTextField partR1;
	static private JTextField pomenR2;
	static private JTextField glagolR2;
	static private JTextField tenseR2;
	static private JTextField partR2;
	static private JTextField pomenR3;
	static private JTextField glagolR3;
	static private JTextField tenseR3;
	static private JTextField partR3;
	static private JTextField pomenR4;
	static private JTextField glagolR4;
	static private JTextField tenseR4;
	static private JTextField partR4;
	static private JTextField pomenR5;
	static private JTextField glagolR5;
	static private JTextField tenseR5;
	static private JTextField partR5;
	static private JTextField pomenR6;
	static private JTextField glagolR6;
	static private JTextField tenseR6;
	static private JTextField partR6;
	static private JTextField textField_24;
	static private JTextField textField_25;
	static private JTextField textField_26;
	private JTextField textField_27;
	private JTextField textField_28;
	private JTextField textField_29;
	private JTextField textField_30;
	private JTextField textField_31;
	private JTextField textField_32;
	private JTextField textField_33;
	private JTextField textField_34;
	private JTextField textField_35;
	private JPanel progressPanel;
	private JButton izpisiGlagoleBtn;
	private JTable table;
	private JButton btnNewButton;
	private static ArrayList<String> pomenArr = new ArrayList<String>();
	private static ArrayList<String> glagolArr = new ArrayList<String>();
	private static ArrayList<String> tenseArr = new ArrayList<String>();
	private static 	ArrayList<String> partArr = new ArrayList<String>();
	private static ArrayList<JTextField> fieldsArr = new ArrayList<JTextField>();
	
	static Color incorrect = new Color(255,102,102);
	static Color correct = new Color(102,255,102);

	public static void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UcenecWindow window = new UcenecWindow();
					window.frmUporabnik.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public UcenecWindow() {
		initialize();
	}
	
	
	//Starting fill field method, later implement check() method which runs on fieldsArray
	private static void fillField() {
		
		fieldsArr.add(pomenR1);
		fieldsArr.add(pomenR2);
		
	}
	
	private static void check(String input, String expected, JTextField cell) {
		
		if(input.isEmpty() || input.isBlank()  || expected.isBlank() || expected.isEmpty()) {
			cell.setBackground(Color.gray);
			cell.setEnabled(false);
			
		} else if ( ! input.equals(expected)) {
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
		
		izpisiGlagoleBtn = new JButton("New button");
		izpisiGlagoleBtn.addActionListener(new ActionListener() {
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
					
					
					
					while(rst.next()) {
						
						pomen = rst.getString(1);
						pomenArr.add(pomen);
						
						glagol = rst.getString(2);
						glagolArr.add(glagol);
						
						tense = rst.getString(3);
						tenseArr.add(tense);
						
						part = rst.getString(4);
						partArr.add(part);
						
					}
					
					pomenR1.setText(pomenArr.get(0));
					pomenR2.setText(pomenArr.get(1));
					pomenR3.setText(pomenArr.get(2));
					pomenR4.setText(pomenArr.get(3));
					pomenR5.setText(pomenArr.get(4));
					pomenR6.setText(pomenArr.get(5));
						
					
					glagolR1.setText(glagolArr.get(0));
					glagolR2.setText(glagolArr.get(1));
					glagolR3.setText(glagolArr.get(2));
					glagolR4.setText(glagolArr.get(3));
					glagolR5.setText(glagolArr.get(4));
					glagolR6.setText(glagolArr.get(5));
					
					tenseR1.setText(tenseArr.get(0));
					tenseR2.setText(tenseArr.get(1));
					tenseR3.setText(tenseArr.get(2));
					tenseR4.setText(tenseArr.get(3));
					tenseR5.setText(tenseArr.get(4));
					tenseR6.setText(tenseArr.get(5));
					
					partR1.setText(partArr.get(0));
					partR2.setText(partArr.get(1));
					partR3.setText(partArr.get(2));
					partR4.setText(partArr.get(3));
					partR5.setText(partArr.get(4));
					partR6.setText(partArr.get(5));
					
					
				} catch(Exception ex) {
					System.out.println("error" + ex);
					
				}
			}
		});
		bottomPanelZaGumb.add(izpisiGlagoleBtn);
		
		
		
		JButton preveriBtn = new JButton("Preveri");
		preveriBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				if(!pomenR1.getText().isBlank() || pomenArr.get(0).isBlank()) {
					
					check(pomenR1.getText(), pomenArr.get(0), pomenR1);
				}
				else {
					pomenR1.setEnabled(false);
					pomenR1.setBackground(incorrect);
					
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
		
		textField_24 = new JTextField();
		textField_24.setColumns(10);
		mainPanel.add(textField_24);
		
		textField_25 = new JTextField();
		textField_25.setColumns(10);
		mainPanel.add(textField_25);
		
		textField_26 = new JTextField();
		textField_26.setColumns(10);
		mainPanel.add(textField_26);
		
		textField_27 = new JTextField();
		textField_27.setColumns(10);
		mainPanel.add(textField_27);
		
		textField_28 = new JTextField();
		textField_28.setColumns(10);
		mainPanel.add(textField_28);
		
		textField_29 = new JTextField();
		textField_29.setColumns(10);
		mainPanel.add(textField_29);
		
		textField_30 = new JTextField();
		textField_30.setColumns(10);
		mainPanel.add(textField_30);
		
		textField_31 = new JTextField();
		textField_31.setColumns(10);
		mainPanel.add(textField_31);
		
		textField_32 = new JTextField();
		textField_32.setColumns(10);
		mainPanel.add(textField_32);
		
		textField_33 = new JTextField();
		textField_33.setColumns(10);
		mainPanel.add(textField_33);
		
		textField_34 = new JTextField();
		textField_34.setColumns(10);
		mainPanel.add(textField_34);
		
		textField_35 = new JTextField();
		textField_35.setColumns(10);
		mainPanel.add(textField_35);
		
		JPanel panel = new JPanel();
		frmUporabnik.getContentPane().add(panel, BorderLayout.WEST);
		
		progressPanel = new JPanel();
		frmUporabnik.getContentPane().add(progressPanel, BorderLayout.EAST);
		
		table = new JTable();
		progressPanel.add(table);
		
	}
}
