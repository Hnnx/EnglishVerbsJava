package DB;

import java.sql.*;
import javax.swing.*;

public class SqliteConnect {
	
	static String dbName = "vilka.db";
	public static Connection conn = null;
	public String query = null;
	public ResultSet rs = null;
	public PreparedStatement pSTMT = null;
	public Statement stmt = null;
	
	
	//Replace DB with new one with third table
	
	/*
	 
	 SELECT users.id, users.username, glagoli.pomen, glagoli.glagol, glagoli.tense, glagoli.part
FROM users LEFT OUTER JOIN helperTable
	ON users.id = helperTable.ucenec
LEFT OUTER JOIN glagoli
	ON glagoli.id = helperTable.glagol
	WHERE users.id = 39;
	  
	 
	 */
	

	public static Connection poveziBazo() {

		try {

			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:"+dbName);
			//JOptionPane.showMessageDialog(null, "Povezava z bazo "+ dbName + " vzpostavljena", "Povezava OK", JOptionPane.INFORMATION_MESSAGE);
			return conn;

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Opis napake: \n" + e, "Napaka :(", JOptionPane.WARNING_MESSAGE);
			return null;
		}

	}

}
