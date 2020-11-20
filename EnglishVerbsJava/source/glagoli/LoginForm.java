package glagoli;

import java.awt.EventQueue;
import java.sql.*;

import javax.swing.JFrame;

import DB.SqliteConnect;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.SystemColor;

public class LoginForm extends SqliteConnect {

	// Open Frame, WindowBuilder Boiler Plate
	private JFrame frame;
	private JTextField userNameField;
	private JPasswordField passwordField;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					/*
					LoginForm window = new LoginForm();
					window.frame.setVisible(true);
					*/
					
					UcenecWindow.start();
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	static String uporabniskoIme = null;

	// Constructor (Boilerplate)
	public LoginForm() {
		initialize();

		// Connection to DB
		conn = poveziBazo();
	}

	// Boilerplate
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(SystemColor.inactiveCaption);
		frame.setBounds(100, 100, 346, 247);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel userNameLabel = new JLabel("Username");
		userNameLabel.setFont(new Font("Bahnschrift", Font.BOLD, 16));
		userNameLabel.setBounds(43, 52, 97, 14);
		frame.getContentPane().add(userNameLabel);

		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setFont(new Font("Bahnschrift", Font.BOLD, 16));
		passwordLabel.setBounds(43, 89, 97, 14);
		frame.getContentPane().add(passwordLabel);

		userNameField = new JTextField();
		userNameField.setBounds(150, 48, 118, 20);
		frame.getContentPane().add(userNameField);
		userNameField.setColumns(10);

		JButton loginSubmitBtn = new JButton("LOG IN");
		loginSubmitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					
					uporabniskoIme = userNameField.getText().toLowerCase();
					String uporabniskoGeslo = String.valueOf( passwordField.getPassword() );
					
					// Query
					String query = "SELECT username,password FROM users WHERE username=? AND password=?";

					// Prepare Statement
					PreparedStatement pSTMT = conn.prepareStatement(query);
					pSTMT.setString(1, uporabniskoIme.toLowerCase());
					pSTMT.setString(2, uporabniskoGeslo.toLowerCase());

					// Result Set
					ResultSet rs = pSTMT.executeQuery();
					int count = 0;
					while (rs.next()) {
						count++;
						uporabniskoIme = rs.getString(1);

					}

					if (count == 1 && uporabniskoIme.toLowerCase().equals("test")) {
						JOptionPane.showMessageDialog(null,
								"Uporabnisko ime in geslo sta pravilna - pozdravljen " + uporabniskoIme, "Prijava",
								JOptionPane.INFORMATION_MESSAGE);
						Ucitelj.start();
						frame.dispose();

					} else if (count == 1) {
						JOptionPane.showMessageDialog(null,
								"Uporabnisko ime in geslo sta pravilna - pozdravljen " + uporabniskoIme, "Prijava",
								JOptionPane.INFORMATION_MESSAGE);
						UcenecWindow.start();
						frame.dispose();
					}

					else if (count > 1) {
						JOptionPane.showMessageDialog(null, "Dvojno uporabnisko ime - preveri z uciteljem", "Prijava",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "Uporabnisko ime in geslo nista pravilna", "Prijava",
								JOptionPane.INFORMATION_MESSAGE);
					}

					rs.close();
					pSTMT.close();

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Opis napake: \n" + ex, "Napaka", JOptionPane.WARNING_MESSAGE);
				}

			}
		});
		loginSubmitBtn.setBounds(95, 136, 105, 38);
		frame.getContentPane().add(loginSubmitBtn);

		passwordField = new JPasswordField();
		passwordField.setBounds(150, 89, 118, 20);
		frame.getContentPane().add(passwordField);
	}
}
