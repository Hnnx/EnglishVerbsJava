package glagoli;

import java.awt.EventQueue;
import java.sql.*;
import javax.swing.JFrame;

import DB.sqliteConnect;

public class LoginForm {

	//Open Frame, WindowBuilder Boiler Plate
	private JFrame frame;

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
	//Connection to DB
	Connection conn = null;
	
	//Constructor (Boilerplate)
	public LoginForm() {
		initialize();
		conn = sqliteConnect.poveziBazo();
	}

	//init method (Boilerplate)	
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	

}
