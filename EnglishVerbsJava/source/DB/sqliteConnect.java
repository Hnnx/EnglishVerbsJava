package DB;

import java.sql.*;
import javax.swing.*;


public class sqliteConnect {
	
	static String dbName = "vilka.db";
	
	public static Connection poveziBazo() {
		
		try { 
			
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:"+dbName);
			JOptionPane.showMessageDialog(null, "Povezava z bazo "+ dbName + " vzpostavljena", "Povezava OK", JOptionPane.INFORMATION_MESSAGE);
			return conn;			
			
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Opis napake: \n" + e,"Napaka :(",JOptionPane.WARNING_MESSAGE);
			return null;
		}
		
		
		
	}
	
	

}
