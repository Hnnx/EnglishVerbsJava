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
			
			// Dobi path kjer je nalozen program
			Path x = Paths.get("");
			String path = x.toAbsolutePath().toString();

			// Ustvari folder db kjer je nalozen program
			File file = new File(path + "\\db");
			
			
			if(!file.exists()) {
				
				if (file.mkdirs()) {
					System.out.println("Directory created successfully");
				} else {
					System.out.println("Sorry couldnt create specified directory");
				}
			}
			
			
			
			// Premakni dbFile v folder db, ki bi mogu bit kreiran ob zagonu programa

			String dbFile = path + "\\vilka.db";
			String destination = path+"\\db\\vilka.db";
			File file1 = new File(dbFile);

			File dirFrom = new File(dbFile);
			File dirTo = new File(destination);

			try {
				copyFile(dirFrom, dirTo);
				System.out.println("narjeno");
			} catch (IOException ex) {

			}

			String dbName = path+"\\db\\vilka.db";
			
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
			
			
			
			
			
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


		query ="CREATE TABLE \"glagoli\" (\r\n" + 
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

		query = "CREATE TABLE \"roles\" (\r\n" + 
				"	\"id\"	INTEGER NOT NULL,\r\n" + 
				"	\"role\"	TEXT,\r\n" + 
				"	PRIMARY KEY(\"id\" AUTOINCREMENT)\r\n" + 
				")";
		
		
		Statement stmt = conn.createStatement();
		stmt.execute(query);

	}

	private static void kreirajTabhelperTable() throws SQLException {
		query = "CREATE TABLE \"helperTable\" (\r\n" + 
				"	\"id\"	INTEGER,\r\n" + 
				"	\"ucenec\"	TEXT,\r\n" + 
				"	\"glagol\"	TEXT,\r\n" + 
				"	FOREIGN KEY(\"ucenec\") REFERENCES \"users2\"(\"id\") ON DELETE CASCADE,\r\n" + 
				"	FOREIGN KEY(\"glagol\") REFERENCES \"glagoli\"(\"id\"),\r\n" + 
				"	PRIMARY KEY(\"id\" AUTOINCREMENT)\r\n" + 
				")";
		
		Statement stmt = conn.createStatement();
		stmt.execute(query);

	}

	private static void kreirajTabeloUsers2() throws SQLException {

		query = "CREATE TABLE \"users2\" (\r\n" + 
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
			query = "INSERT INTO users2 (username, password, sequence, role) VALUES (?, ?, ?, ?)";
			pSTMT = conn.prepareStatement(query);	
			
			pSTMT.setString(1, "admin");
			pSTMT.setString(2, "21232f297a57a5a743894a0e4a801fc3");
			pSTMT.setString(3, "1000");
			pSTMT.setInt(4, 2);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		

		
		
	}

}
