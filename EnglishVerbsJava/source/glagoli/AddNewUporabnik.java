package glagoli;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import javax.swing.JTextField;

import DB.SqliteConnect;

import javax.swing.JButton;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;

public class AddNewUporabnik extends SqliteConnect {

	// --> Okno
	private JFrame frame;

	// --> Podatki uporabnika
	private JTextField txtUporabnik;
	private JTextField txtGeslo;
	private static	int uporabnikID = 0;

	//--> Gumbi
	private JButton btnBack;
	private JButton btnRegister;
	

	// --> Boilerplate/Zagon okna
	public static void start() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					AddNewUporabnik window = new AddNewUporabnik();
					window.frame.setVisible(true);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null,
							"Prišlo je do napake pri zagonu okna za registracijo.\nOpis napake: " + e.toString(), "Napaka",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
	}

	public AddNewUporabnik() {
		initialize();
		conn = poveziBazo();
	}

	// --> Metode za validacijo uporabniskega imena in gesla
	private static boolean isValidPassword(String password, String rx) {
		
		Pattern pattern = Pattern.compile(rx);
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();

	}

	private static boolean isValidUsername(String username, String rx) {
		
		Pattern pattern = Pattern.compile(rx);
		Matcher matcher = pattern.matcher(username);
		return matcher.matches();

	}
	
	private static int getLastRowID() {
		
		try {
			
			// --> Get last ID
			query = "SELECT MAX(id) FROM users2;";
			
			pSTMT = conn.prepareStatement(query);	 
			rs = pSTMT.executeQuery();
			
			while(rs.next()) {
				uporabnikID = rs.getInt(1);
			}		
			
			pSTMT.close();
			
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Prišlo je do napake pri pridobivanju ID-ja uporabnika.\nOpis napake: " + e.toString(), "Napaka",
					JOptionPane.WARNING_MESSAGE);

		}
		 catch (Exception e) {
			 JOptionPane.showMessageDialog(null, "Prišlo je do napake pri pridobivanju ID-ja uporabnika.\nOpis napake: " + e.toString(), "Napaka",
						JOptionPane.WARNING_MESSAGE);

		}
		
		return uporabnikID;
		
	}
	
	private void dolociRandom() {
		try {
			
			uporabnikID = getLastRowID();
			

			// --> Brisanje dosedanjih entryjev iz helperTable
			query = "DELETE FROM helperTable WHERE ucenec = ?";
			pSTMT = conn.prepareStatement(query);
			pSTMT.setInt(1, uporabnikID);
			pSTMT.execute();
			pSTMT.close();
			
			// --> Vnos random entryjev
			
			query = "INSERT INTO helperTable (ucenec, glagol)" + "VALUES (?, ?);";
			pSTMT = conn.prepareStatement(query);
			
			for (int i = 0; i < 9; i++) {
				pSTMT.setInt(1, uporabnikID);
				pSTMT.setInt(2, getRDM());
				pSTMT.execute();
			}
			
		
			
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Prišlo je do napake pri samodejnemu določanju glagolov.\nOpis napake: " + e.toString(), "Napaka",
					JOptionPane.WARNING_MESSAGE);
		} 
		
		
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Prišlo je do napake pri samodejnemu določanju glagolov.\nOpis napake: " + e.toString(), "Napaka",
					JOptionPane.WARNING_MESSAGE);

		}
		
	}
	
	private static int getRDM() {
		return (int) (Math.random() * 64 + 1);
	}

	private void initialize() {
		frame = new JFrame("Dodaj Uporabnika");
		frame.getContentPane().setBackground(SystemColor.inactiveCaption);
		frame.setBounds(430, 100, 365, 356);
		frame.getContentPane().setLayout(null);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JPanel userNamePwPanel = new JPanel();
		userNamePwPanel.setBackground(SystemColor.inactiveCaption);
		userNamePwPanel.setBounds(10, 11, 329, 285);
		frame.getContentPane().add(userNamePwPanel);
		GridBagLayout gbl_userNamePwPanel = new GridBagLayout();
		gbl_userNamePwPanel.columnWidths = new int[] { 329, 0 };
		gbl_userNamePwPanel.rowHeights = new int[] { 59, 30, 59, 30, 30, 0, 0, 0 };
		gbl_userNamePwPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_userNamePwPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		userNamePwPanel.setLayout(gbl_userNamePwPanel);

		JLabel lblUporabnik = new JLabel("UPORABNIK");
		lblUporabnik.setFont(new Font("Arial Black", Font.PLAIN, 18));
		lblUporabnik.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblUporabnik = new GridBagConstraints();
		gbc_lblUporabnik.fill = GridBagConstraints.VERTICAL;
		gbc_lblUporabnik.insets = new Insets(0, 0, 5, 0);
		gbc_lblUporabnik.gridx = 0;
		gbc_lblUporabnik.gridy = 0;
		userNamePwPanel.add(lblUporabnik, gbc_lblUporabnik);

		txtUporabnik = new JTextField();
		txtUporabnik.setFont(new Font("Arial", Font.PLAIN, 15));
		txtUporabnik.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_txtUporabnik = new GridBagConstraints();
		gbc_txtUporabnik.fill = GridBagConstraints.BOTH;
		gbc_txtUporabnik.insets = new Insets(0, 0, 5, 0);
		gbc_txtUporabnik.gridx = 0;
		gbc_txtUporabnik.gridy = 1;
		userNamePwPanel.add(txtUporabnik, gbc_txtUporabnik);
		txtUporabnik.setColumns(10);

		JLabel lblGeslo = new JLabel("GESLO");
		lblGeslo.setFont(new Font("Arial Black", Font.PLAIN, 18));
		lblGeslo.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblGeslo = new GridBagConstraints();
		gbc_lblGeslo.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblGeslo.insets = new Insets(0, 0, 5, 0);
		gbc_lblGeslo.gridx = 0;
		gbc_lblGeslo.gridy = 2;
		userNamePwPanel.add(lblGeslo, gbc_lblGeslo);

		txtGeslo = new JTextField();
		txtGeslo.setFont(new Font("Arial", Font.PLAIN, 15));
		txtGeslo.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_txtGeslo = new GridBagConstraints();
		gbc_txtGeslo.fill = GridBagConstraints.BOTH;
		gbc_txtGeslo.insets = new Insets(0, 0, 5, 0);
		gbc_txtGeslo.gridx = 0;
		gbc_txtGeslo.gridy = 3;
		userNamePwPanel.add(txtGeslo, gbc_txtGeslo);
		txtGeslo.setColumns(10);
		
		
		// --> Gumb Dodaj
		btnRegister = new JButton("USTVARI");
		btnRegister.setBackground(new Color(244, 164, 96));
		btnRegister.setFont(new Font("Arial Black", Font.PLAIN, 15));
		GridBagConstraints gbc_btnRegister = new GridBagConstraints();
		gbc_btnRegister.fill = GridBagConstraints.BOTH;
		gbc_btnRegister.insets = new Insets(0, 0, 5, 0);
		gbc_btnRegister.gridx = 0;
		gbc_btnRegister.gridy = 5;
		userNamePwPanel.add(btnRegister, gbc_btnRegister);
		
		
		// --> Gumb za registracijo - ustvari profil in doloci nakljucne glagole
		btnRegister.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				conn = poveziBazo();

				String uporabnisko = txtUporabnik.getText();
				String password = txtGeslo.getText();
				
				String regexZaUsername = "^[a-zA-Z0-9_-]{3,15}$";
				String regexZaPassword = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$";
				try {
					
					query = "INSERT INTO users2 (username, password, sequence, role) VALUES (?, ?, ?, ?)";

					pSTMT = conn.prepareStatement(query);

					// VALIDATION
					if (!isValidUsername(uporabnisko,regexZaUsername)) {
						JOptionPane.showMessageDialog(null,
								"Uporabnisko lahko vsebuje alfanumerične, _ ter - znake\nDolzina mora biti med 3 in 16", "Napaka",
								JOptionPane.WARNING_MESSAGE);
					} 
					else if(!isValidPassword(password, regexZaPassword)) {

						JOptionPane.showMessageDialog(null,
							"Geslo mora vsebovati vsaj:\n- 8 znakov\n- 1 malo črko\n- 1 veliko črko\n- 1 številko\n- 1 poseben znak", "Napaka",
								JOptionPane.WARNING_MESSAGE);
					}
					
					
					
					else {
						pSTMT.setString(1, uporabnisko.toLowerCase()); // Shrani vse z malo da ni case sensitive
						pSTMT.setString(2, LoginForm.getMD(password)); // Pred vnosom MD5 hash
						pSTMT.setString(3, "1000"); // Default sekvenca 
						pSTMT.setInt(4, 3); 

						pSTMT.execute();
						pSTMT.close();
						
						try {
							dolociRandom();
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(null, "Prišlo je do napake pri samodejnemu določanju glagolov.\nOpis napake: " + ex.toString(), "Napaka",
									JOptionPane.WARNING_MESSAGE);
						}
						
						JOptionPane.showConfirmDialog(null, "Uporabniski profil "+ uporabnisko +" ustvarjen - določeni so bili naključni glagoli", "Uspeh",
								JOptionPane.DEFAULT_OPTION);
					}

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null,
							"Prišlo je do napake pri registraciji.\nOpis napake: " + e.toString(), "Napaka",
							JOptionPane.WARNING_MESSAGE);
				}
			}
		});
		
				btnBack = new JButton("NAZAJ");
				btnBack.setBackground(new Color(244, 164, 96));
				btnBack.setFont(new Font("Arial Black", Font.PLAIN, 15));
				GridBagConstraints gbc_btnBack = new GridBagConstraints();
				gbc_btnBack.fill = GridBagConstraints.BOTH;
				gbc_btnBack.gridx = 0;
				gbc_btnBack.gridy = 6;
				userNamePwPanel.add(btnBack, gbc_btnBack);
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				
				LoginForm prijavaOkno = new LoginForm();
				prijavaOkno.frame.setVisible(true);
				
				
				
			}
		});
	}
}
