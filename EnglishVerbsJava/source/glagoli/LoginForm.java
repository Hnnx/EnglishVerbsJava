package glagoli;

import java.awt.EventQueue;
import javax.swing.JFrame;

import DB.SqliteConnect;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginForm extends SqliteConnect {

	
	// --> Okno
	protected JFrame frame;

	// --> podatki uporabnika userID se izpisuje v drugih oknih
	protected static int uporabnikID;
	protected static String uporabniskoIme;
	protected static String uporabniskoGeslo;
	protected static String sequence;
	protected static int role;

	// --> LABEL, TEXT ITD
	private JTextField userNameField;
	private JPasswordField passwordField;
	private JLabel lblUporabnik;
	private JLabel lblGeslo;
	private JLabel lblRegister;

	// --> GUMBI
	private JButton btnPrijava;
	private JPanel panel;

	// --> Boilerplate/Zagon okna
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginForm window = new LoginForm();
					window.frame.setVisible(true);

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null,
							"Prišlo je do napake pri zagonu okna za prijavo uporabnika", "Napaka",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
	}

	public LoginForm() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame("Prijava v aplikacijo");
		frame.getContentPane().setBackground(SystemColor.inactiveCaption);
		frame.setBounds(100, 100, 346, 407);
		frame.getContentPane().setLayout(null);
		
		
		// --> Override gumba X za izhod - DO_NOTHING in odpri JOptionPane
		frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				try {

					int input = JOptionPane.showConfirmDialog(null, "Ali zelite zapreti program?", "Izhod",
							JOptionPane.INFORMATION_MESSAGE);
			

					if (input == 0)
						System.exit(0);

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null,
							"Prišlo je do napake pri izhodu iz programa.", "Napaka",
							JOptionPane.WARNING_MESSAGE);
					
					// Dodan system exit, ce pride do exceptiona se program zapre
					System.exit(0);
				}

			}
		});

		
		panel = new JPanel();
		panel.setBackground(SystemColor.inactiveCaption);
		panel.setBounds(23, 4, 279, 353);
		frame.getContentPane().add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 279, 0 };
		gbl_panel.rowHeights = new int[] { 97, 0, 97, 0, 0, 0, 97, 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		lblUporabnik = new JLabel("UPORABNIK");
		lblUporabnik.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblUporabnik = new GridBagConstraints();
		gbc_lblUporabnik.fill = GridBagConstraints.BOTH;
		gbc_lblUporabnik.insets = new Insets(0, 0, 5, 0);
		gbc_lblUporabnik.gridx = 0;
		gbc_lblUporabnik.gridy = 0;
		panel.add(lblUporabnik, gbc_lblUporabnik);
		lblUporabnik.setFont(new Font("Arial Black", Font.PLAIN, 18));

		userNameField = new JTextField();
		userNameField.setFont(new Font("Arial Black", Font.PLAIN, 18));
		userNameField.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_userNameField = new GridBagConstraints();
		gbc_userNameField.fill = GridBagConstraints.BOTH;
		gbc_userNameField.insets = new Insets(0, 0, 5, 0);
		gbc_userNameField.gridx = 0;
		gbc_userNameField.gridy = 1;
		panel.add(userNameField, gbc_userNameField);
		userNameField.setColumns(10);

		lblGeslo = new JLabel("GESLO");
		lblGeslo.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblGeslo = new GridBagConstraints();
		gbc_lblGeslo.fill = GridBagConstraints.BOTH;
		gbc_lblGeslo.insets = new Insets(0, 0, 5, 0);
		gbc_lblGeslo.gridx = 0;
		gbc_lblGeslo.gridy = 2;
		panel.add(lblGeslo, gbc_lblGeslo);
		lblGeslo.setFont(new Font("Arial Black", Font.PLAIN, 18));

		passwordField = new JPasswordField();
		passwordField.setToolTipText("Bodite pozorni na velike in male črke");
		passwordField.setFont(new Font("Arial Black", Font.PLAIN, 18));
		passwordField.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.fill = GridBagConstraints.BOTH;
		gbc_passwordField.insets = new Insets(0, 0, 5, 0);
		gbc_passwordField.gridx = 0;
		gbc_passwordField.gridy = 3;
		panel.add(passwordField, gbc_passwordField);

		btnPrijava = new JButton("PRIJAVA");
		btnPrijava.setBackground(new Color(244, 164, 96));
		btnPrijava.setFont(new Font("Arial Black", Font.PLAIN, 15));
		GridBagConstraints gbc_btnPrijava = new GridBagConstraints();
		gbc_btnPrijava.insets = new Insets(0, 0, 5, 0);
		gbc_btnPrijava.fill = GridBagConstraints.BOTH;
		gbc_btnPrijava.gridx = 0;
		gbc_btnPrijava.gridy = 5;
		panel.add(btnPrijava, gbc_btnPrijava);
		
		lblRegister = new JLabel("Še nimate profila? Ustvari tukaj");
		lblRegister.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				
				lblRegister.setForeground(Color.black);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				
				lblRegister.setForeground(Color.BLUE);
				
			}
			@Override
			
			public void mouseClicked(MouseEvent e) {
				
				AddNewUporabnik.start();
				frame.dispose();
				
				
			}
		});
		
		
		lblRegister.setHorizontalAlignment(SwingConstants.CENTER);
		lblRegister.setForeground(Color.BLUE);
		GridBagConstraints gbc_lblRegister = new GridBagConstraints();
		gbc_lblRegister.insets = new Insets(0, 0, 5, 0);
		gbc_lblRegister.gridx = 0;
		gbc_lblRegister.gridy = 6;
		panel.add(lblRegister, gbc_lblRegister);

		
		// --> LOGIN
		btnPrijava.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					conn = poveziBazo();
					kreirajVseTabele();

					uporabniskoIme = userNameField.getText().toLowerCase();
					uporabniskoGeslo = String.valueOf(passwordField.getPassword());

					// Poizvedba / Query
					query = "SELECT username, id, password, sequence, role FROM users2 WHERE username=? AND password=?";

					// Prepare Statement
					pSTMT = conn.prepareStatement(query);

					// Zamenja vprasaje v query-ju, username.toLowerCase za primerjavo tako da ni case sensitive
					pSTMT.setString(1, uporabniskoIme.toLowerCase());
					pSTMT.setString(2, getMD(uporabniskoGeslo));

					// Shrani vrednosti iz poizvedbe v resultSet
					rs = pSTMT.executeQuery();
					int count = 0;

					while (rs.next()) {
						uporabnikID = rs.getInt("id");
						uporabniskoIme = rs.getString(1);
						sequence = rs.getString("sequence");
						role = rs.getInt("role");
						count++;
					}

					// Ce se ime/password ujemata v bazi ( username AND pw) imamo samo 1 izpis (count == 1)
					// role je samo dodaten check za dolocit, katero okno se odpre
					
					if (count == 1 && role == 2) {
						JOptionPane.showMessageDialog(null,
								"Uporabnisko ime in geslo sta pravilna - pozdravljen "
										+ uporabniskoIme.substring(0, 1).toUpperCase() + uporabniskoIme.substring(1),
								"Prijava", JOptionPane.INFORMATION_MESSAGE);
						Ucitelj.start();
						frame.dispose();

					} else if (count == 1 && role == 1) {
						
						//TODO: Dodaj okno za admina

					
					} else if (count == 1 && role == 3) {
						JOptionPane.showMessageDialog(null,
								"Uporabnisko ime in geslo sta pravilna - pozdravljen "
										+ uporabniskoIme.substring(0, 1).toUpperCase() + uporabniskoIme.substring(1),
								"Prijava", JOptionPane.INFORMATION_MESSAGE);

						// DODAN ŠE EN VALIDATION - ČE UPORABNIK NIMA DOLOCENIH GLAGOLOV NE ODPRE OKNA

						if(imaDoloceneGlagole()) {
							Ucenec.start();
							frame.dispose();
							
						}
					}

					// Ce je vec kot en entry je prislo do duplikacije
					else if (count > 1) {
						JOptionPane.showMessageDialog(null, "Dvojno uporabnisko ime - preveri z uciteljem", "Prijava",
								JOptionPane.INFORMATION_MESSAGE);

						// Pogoj WHERE user=? AND PASSWORD = ? ni vrnil rezultatov - geslo/user je
						// napacen
					} else {
						JOptionPane.showMessageDialog(null, "Uporabnisko ime in geslo nista pravilna", "Prijava",
								JOptionPane.INFORMATION_MESSAGE);
					}

					rs.close();
					pSTMT.close();
				

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null,
							"Prišlo je do napake pri prijavi - opis napake:\n" + ex.toString(), "Napaka",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
	}

	// --> Metoda preveri, ali ima uporabnik ze vnose v helperTable - helperTable se napolni, ko uporabniku prvic dolocimo glagole
	private static boolean imaDoloceneGlagole() {
		boolean openWindow = false;

		try {
			query = "SELECT * FROM helperTable WHERE ucenec = ?";

			pSTMT = conn.prepareStatement(query);
			pSTMT.setInt(1, uporabnikID);

			rs = pSTMT.executeQuery();

			int count = 0;
			while (rs.next())
				count++;

			// Ce je count 0, uporabnik nima glagolov - vrni v Login
			if (count == 0) {
				openWindow = false;
				
				JOptionPane.showMessageDialog(null, "Opis napake:\nUporabnik še nima določenih glagolov");

			}
			else {
				openWindow = true;
			}


		} catch (Exception ex) {
			JOptionPane.showMessageDialog(null,
					"Prišlo je do napake pri preverjanju glagolov - opis napake:\n" + ex.toString(), "Napaka",
					JOptionPane.WARNING_MESSAGE);
			openWindow = false;
		}
		return openWindow;
	}

	// --> Metoda vrne String hashan z MD5 algoritmom
	
	protected static String getMD(String gesloVnos) {
		try {

			//md object s parametrom MD5, mDigest bitni array ki shrani vneseni String v bit array
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] mDigest = md.digest(gesloVnos.getBytes());

			BigInteger num = new BigInteger(1, mDigest);

			String gesloHashed = num.toString(16);

			while (gesloHashed.length() < 32) {
				gesloHashed = "0" + gesloHashed;
			}

			return gesloHashed;

		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}

	}
}
