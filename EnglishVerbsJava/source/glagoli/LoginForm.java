package glagoli;

import java.awt.EventQueue;
import java.sql.*;
import javax.swing.JFrame;

import DB.sqliteConnect;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginForm {

	//Open Frame, WindowBuilder Boiler Plate
	private JFrame frame;
	private JTextField userNameField;
	private JPasswordField passwordField;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginForm window = new LoginForm();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	Connection conn = null;
	
	//Constructor (Boilerplate)
	public LoginForm() {
		initialize();
		
		//Connection to DB
		conn = sqliteConnect.poveziBazo();
	}

	//Boilerplate 	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel userNameLabel = new JLabel("Username");
		userNameLabel.setBounds(110, 88, 46, 14);
		frame.getContentPane().add(userNameLabel);
		
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(110, 128, 46, 14);
		frame.getContentPane().add(passwordLabel);
		
		userNameField = new JTextField();
		userNameField.setBounds(166, 85, 86, 20);
		frame.getContentPane().add(userNameField);
		userNameField.setColumns(10);
		
		JButton loginSubmitBtn = new JButton("LOG IN");
		loginSubmitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
					try {
						
						
						//Query
						String query = "SELECT username,password FROM users WHERE username=? AND password=?";
						
						//Prepare Statement
						PreparedStatement pSTMT = conn.prepareStatement(query);
						pSTMT.setString(1,userNameField.getText());
						pSTMT.setString(2, passwordField.getText());
						
						//Result Set
						ResultSet rs = pSTMT.executeQuery();
						int count = 0;
						
						while(rs.next()) {
							count++;
							
						}
						
						if(count == 1) {
							
							JOptionPane.showMessageDialog(null, "Uporabnisko ime in geslo sta pravilna", "Prijava", JOptionPane.INFORMATION_MESSAGE);
						} else if(count > 1) {
							JOptionPane.showMessageDialog(null, "Duplicate username & password", "Prijava", JOptionPane.INFORMATION_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(null, "Uporabnisko ime in geslo nista pravilna", "Prijava", JOptionPane.INFORMATION_MESSAGE);
						}
						
						rs.close();
						pSTMT.close();
						 
						 
					} catch(Exception ex){
						JOptionPane.showMessageDialog(null, "Opis napake: \n"+ex, "Napaka", JOptionPane.WARNING_MESSAGE);
					} 
						
				
			}
		});
		loginSubmitBtn.setBounds(132, 173, 89, 23);
		frame.getContentPane().add(loginSubmitBtn);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(166, 125, 86, 20);
		frame.getContentPane().add(passwordField);
	}
}
