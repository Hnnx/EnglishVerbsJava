package DB;

import java.sql.*;
import javax.swing.*;


public class sqliteConnect {
	
	static String dbName = "hek.db";
	
	public static Connection poveziBazo() {
		
		try { 
			
			Class.forName("org.sqlite.JDBC");
			Connection conn = DriverManager.getConnection("jdbc:sqlite:"+dbName);
			return conn;			
			
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Opis napake:" + e);
			return null;
		}
		
		
		
	}
	
	

}
