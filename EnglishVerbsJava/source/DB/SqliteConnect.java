package DB;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import javax.swing.*;

public class SqliteConnect {

	public static Connection conn = null;
	public static String query = "";
	public static ResultSet rs = null;
	public static PreparedStatement pSTMT = null;
	public Statement stmt = null;

	public static void copyFile(File from, File to) throws IOException {
		Files.copy(from.toPath(), to.toPath());
	}

	public static Connection poveziBazo() {
		try {
			
			Class.forName("org.sqlite.JDBC");
			
			
			Path g = Paths.get("");
			String path = g.toAbsolutePath().toString();
			
			File f = new File(path+"//vilka.db");
			
			if(!f.exists()) {
				f.createNewFile();
			}
			
			String dbName = "vilka.db";
			
			conn = DriverManager.getConnection("jdbc:sqlite:"+dbName);
			
			
			return conn;

		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Prišlo je do napake pri povezovanju z bazo podatkov" + e.toString(),
					"Napaka", JOptionPane.WARNING_MESSAGE);
			return null;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Prišlo je do napake pri povezovanju z bazo podatkov" + e.toString(),
					"Napaka", JOptionPane.WARNING_MESSAGE);
			return null;
		}
	}

	protected static void kreirajVseTabele() {
		try {
			kreirajTabGlagol();
			kreirajTabRoles();
			kreirajTabhelperTable();
			kreirajTabeloUsers2();
			populateUsers();

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Prišlo je do napake pri povezovanju z bazo podatkov" + e.toString(),
					"Napaka", JOptionPane.WARNING_MESSAGE);
		}

	}

	private static void kreirajTabGlagol() throws SQLException {


		query = "CREATE TABLE IF NOT EXISTS \"glagoli\" (\r\n" + 
				"	\"id\"	INTEGER NOT NULL,\r\n" + 
				"	\"prevod\"	TEXT,\r\n" + 
				"	\"verb\"	TEXT,\r\n" + 
				"	\"pastSimple\"	TEXT,\r\n" + 
				"	\"pastParticiple\"	TEXT,\r\n" + 
				"	PRIMARY KEY(\"id\" AUTOINCREMENT)\r\n" + 
				")";
		
		Statement stmt = conn.createStatement();
		stmt.execute(query);

	}

	private static void kreirajTabRoles() throws SQLException {

		query = "CREATE TABLE IF NOT EXISTS \"roles\" (\r\n" + 
				"	\"id\"	INTEGER NOT NULL,\r\n" + 
				"	\"role\"	TEXT,\r\n" + 
				"	PRIMARY KEY(\"id\" AUTOINCREMENT)\r\n" + 
				")";
		
		
		Statement stmt = conn.createStatement();
		stmt.execute(query);

	}

	private static void kreirajTabhelperTable() throws SQLException {
		query = "CREATE TABLE IF NOT EXISTS \"helperTable\" (\r\n" + 
				"	\"id\"	INTEGER,\r\n" + 
				"	\"ucenec\"	TEXT UNIQUE,\r\n" + 
				"	\"glagol\"	TEXT,\r\n" + 
				"	FOREIGN KEY(\"ucenec\") REFERENCES \"users2\"(\"id\") ON DELETE CASCADE,\r\n" + 
				"	FOREIGN KEY(\"glagol\") REFERENCES \"glagoli\"(\"id\"),\r\n" + 
				"	PRIMARY KEY(\"id\" AUTOINCREMENT)\r\n" + 
				")";
		
		Statement stmt = conn.createStatement();
		stmt.execute(query);

	}

	private static void kreirajTabeloUsers2() throws SQLException {

		query = "CREATE TABLE IF NOT EXISTS \"users2\" (\r\n" + 
				"	\"id\"	INTEGER NOT NULL,\r\n" + 
				"	\"username\"	TEXT,\r\n" + 
				"	\"password\"	TEXT,\r\n" + 
				"	\"sequence\"	TEXT,\r\n" + 
				"	\"role\"	INTEGER,\r\n" + 
				"	FOREIGN KEY(\"role\") REFERENCES \"roles\"(\"id\") ON DELETE CASCADE,\r\n" + 
				"	PRIMARY KEY(\"id\" AUTOINCREMENT)\r\n" + 
				")";
		
		
		Statement stmt = conn.createStatement();
		stmt.execute(query);

	}
	
	private static void populateUsers() {
		
		try {
			
			query = "SELECT * FROM users2";
			
			pSTMT = conn.prepareStatement(query);
			
			rs = pSTMT.executeQuery();
			
			int count = 0;
			while(rs.next()) {
				count++;
			}
			
			if(count == 0) {
				query = "INSERT INTO users2 (username, password, sequence, role) VALUES (?, ?, ?, ?)";
				pSTMT = conn.prepareStatement(query);	
				
				pSTMT.setString(1, "admin");
				pSTMT.setString(2, "21232f297a57a5a743894a0e4a801fc3");
				pSTMT.setString(3, "1000");
				pSTMT.setInt(4, 2);
				
				pSTMT.execute();
				
				pSTMT.close();
				
			}
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		

		
		
	}

}
