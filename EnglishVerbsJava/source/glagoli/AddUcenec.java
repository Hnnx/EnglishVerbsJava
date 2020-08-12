package glagoli;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import javax.swing.JTextField;

import DB.SqliteConnect;

import javax.swing.JButton;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.awt.event.ActionEvent;

public class AddUcenec {

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
				
				Connection conn = SqliteConnect.poveziBazo();
				
				try {
				String query = "INSERT INTO users (username, password) VALUES (?,?)";
				
				PreparedStatement pSTMT = conn.prepareStatement(query);
				
				pSTMT.setString(1, uporabniskoTxt.getText());
				pSTMT.setString(2, pwTxt.getText());
				
				pSTMT.execute();
				
				Ucitelj.refresh();
				
				JOptionPane.showMessageDialog(null, "Shranjeno", "Uporabnik dodan", JOptionPane.INFORMATION_MESSAGE);
				
				
				
				frame.dispose();
				
				pSTMT.close();
				
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Opis napake: \n " + ex.getMessage(), "Napaka :(",
						JOptionPane.WARNING_MESSAGE);
			}
				
				
				
			}
		});
		addBtn.setBounds(66, 66, 89, 23);
		frame.getContentPane().add(addBtn);
	}
}
