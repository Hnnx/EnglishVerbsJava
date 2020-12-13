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
import java.security.MessageDigest;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.SwingConstants;
import java.awt.Color;

public class LoginForm extends SqliteConnect {

	// --> WindowBuilder Boiler Plate
	protected JFrame frame;

	// --> podatki uporabnika userID se izpisuje v drugih oknih
	protected static int userID;
	protected static String uporabniskoIme;
	protected static String uporabniskoGeslo;
	protected static String sequence;
	protected static int role;

	// --> LABEL, TEXT ITD
	private JTextField userNameField;
	private JPasswordField passwordField;
	JLabel lblUporabnik;
	JLabel lblGeslo;

	// --> GUMBI
	JButton btnPrijava;
	private JPanel panel;

	// --> Boilerplate
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginForm window = new LoginForm();
					window.frame.setVisible(true);
					
//					AddGlagol.start();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// --> WindowBuilder Boiler Plate
	public LoginForm() {
		initialize();
	}

	private void initialize() {
		frame = new JFrame("Prijava v aplikacijo");
		frame.getContentPane().setBackground(SystemColor.inactiveCaption);
		frame.setBounds(100, 100, 346, 407);
		frame.getContentPane().setLayout(null);
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
							"Opis napake: Prislo je do napake pri izhodu iz programa" + ex.getMessage(), "Napaka :(",
							JOptionPane.WARNING_MESSAGE);
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
		
		
		
		// --> LOGIN
		btnPrijava.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					conn = poveziBazo();

					uporabniskoIme = userNameField.getText().toLowerCase();
					uporabniskoGeslo = String.valueOf(passwordField.getPassword());
					
					//TODO: HASHING PW

					// Poizvedba / Query
					query = "SELECT username, id, password, sequence, role FROM users2 WHERE username=? AND password=?";
					
	
					MessageDigest md = new MessageDigest("MD5") {
						
						@Override
						protected void engineUpdate(byte[] input, int offset, int len) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						protected void engineUpdate(byte input) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						protected void engineReset() {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						protected byte[] engineDigest() {
							// TODO Auto-generated method stub
							return null;
						}
					};
					
					//STARA VERZIJA (DEBUGGING)
					//query = "SELECT username, id, password FROM users WHERE username=? AND password=?";
					

					// Prepare Statement
					pSTMT = conn.prepareStatement(query);
					
					//Zamenja vprasaje v query-ju . da v lowerCase za lazjo kontrolo uporabnikov
					pSTMT.setString(1, uporabniskoIme.toLowerCase());
					pSTMT.setString(2, uporabniskoGeslo.toLowerCase());

					// Result Set
					rs = pSTMT.executeQuery();
					int count = 0;
					
					
					while (rs.next()) {
						userID = rs.getInt("id");
						uporabniskoIme = rs.getString(1);
						sequence = rs.getString("sequence");
						role = rs.getInt("role");
						count++;
					}
					
					
					
					if (count == 1 && role == 2) {
						JOptionPane.showMessageDialog(null,
								"Uporabnisko ime in geslo sta pravilna - pozdravljen "
										+ uporabniskoIme.substring(0, 1).toUpperCase() + uporabniskoIme.substring(1),
								"Prijava", JOptionPane.INFORMATION_MESSAGE);
						Ucitelj.start();
						frame.dispose();

					//Ce je v izpisu samo 1 entry (count == 1), potem je username/pw pravilen
					} else if(count == 1 && role == 1) {
						System.out.println("ADMIN");
						System.exit(0);
						
					}
					else if (count == 1) {
						JOptionPane.showMessageDialog(null,
								"Uporabnisko ime in geslo sta pravilna - pozdravljen "
										+ uporabniskoIme.substring(0, 1).toUpperCase() + uporabniskoIme.substring(1),
								"Prijava", JOptionPane.INFORMATION_MESSAGE);
						Ucenec.start();
						frame.dispose();
					}
					
					
					//Ce je vec kot en entry je prislo do duplikacije
					else if (count > 1) {
						JOptionPane.showMessageDialog(null, "Dvojno uporabnisko ime - preveri z uciteljem", "Prijava",
								JOptionPane.INFORMATION_MESSAGE);
						
					//	Pogoj WHERE user=? AND PASSWORD = ?  ni vrnil rezultatov - geslo/user je napacen
					} else {
						JOptionPane.showMessageDialog(null, "Uporabnisko ime in geslo nista pravilna", "Prijava",
								JOptionPane.INFORMATION_MESSAGE);
					}

					rs.close();
					pSTMT.close();

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null,
							"Opis napake: Prišlo je do napake pri prijavi - podrobnosti napake: " + ex, "Napaka",
							JOptionPane.WARNING_MESSAGE);
				}

			}
		});
	}
}
