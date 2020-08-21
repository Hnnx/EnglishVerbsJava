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
import java.sql.PreparedStatement;
import java.awt.event.ActionEvent;

public class AddUcenec extends SqliteConnect{

	private JFrame frame;
	private JTextField uporabniskoTxt;
	private JTextField pwTxt;

	public static void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddUcenec window = new AddUcenec();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public AddUcenec() {
		initialize();
	}
	
	private static boolean isValidEmail(String mail) {
		
		return mail.length() >= 4 && mail.length() <= 20 ? true:false;
		
	}
	
	private static boolean isValidUsername(String username) {
		
		return username.length() >= 3 && username.length() >= 16 ? true:false;
	}

	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(SystemColor.inactiveCaption);
		frame.setBounds(100, 100, 256, 140);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel uporabniskoAdd = new JLabel("Uporabnik");
		uporabniskoAdd.setBounds(10, 11, 91, 14);
		frame.getContentPane().add(uporabniskoAdd);

		JLabel pwAdd = new JLabel("Geslo");
		pwAdd.setBounds(10, 41, 78, 14);
		frame.getContentPane().add(pwAdd);

		uporabniskoTxt = new JTextField();
		uporabniskoTxt.setBounds(66, 8, 131, 20);
		frame.getContentPane().add(uporabniskoTxt);
		uporabniskoTxt.setColumns(10);

		pwTxt = new JTextField();
		pwTxt.setBounds(66, 38, 131, 20);
		frame.getContentPane().add(pwTxt);
		pwTxt.setColumns(10);
		
		
		JButton addBtn = new JButton("DODAJ");
		addBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				conn = poveziBazo();
				
				String uporabnisko = uporabniskoTxt.getText();
				String password = pwTxt.getText();

				try {
					String query = "INSERT INTO users (username, password) VALUES (?,?)";

					PreparedStatement pSTMT = conn.prepareStatement(query);
					
					//VALIDATION
					if( ! isValidUsername(uporabnisko) && ! isValidEmail(password) ) {
						JOptionPane.showMessageDialog(null, "Uporabnisko ime mora vsebovati med 3 in 16 znakov, geslo pa med 4 in 20", "Napaka",
								JOptionPane.WARNING_MESSAGE);
					} else {
						
						pSTMT.setString(1, uporabnisko.toLowerCase());
						pSTMT.setString(2, password.toLowerCase());
						
						pSTMT.execute();
						
						Ucitelj.refresh();
						
						JOptionPane.showMessageDialog(null, "Shranjeno", "Uporabnik dodan",
								JOptionPane.INFORMATION_MESSAGE);
						
						frame.dispose();
						
						pSTMT.close();
						
					}


				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Opis napake: \n " + ex.getMessage(), "Napaka :(",
							JOptionPane.WARNING_MESSAGE);
				}

			}
		});
		addBtn.setBounds(127, 69, 86, 23);
		frame.getContentPane().add(addBtn);
		
		JButton nazajBtn = new JButton("NAZAJ");
		nazajBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				frame.dispose();
				
			}
		});
		nazajBtn.setBounds(20, 66, 81, 23);
		frame.getContentPane().add(nazajBtn);
	}
}
