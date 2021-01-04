package DB;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import javax.swing.*;

public class SqliteConnect {

	static String dbName = "source//DB//vilka.db";
	public static Connection conn = null;
	public static String query = "";
	public static ResultSet rs = null;
	public static PreparedStatement pSTMT = null;
	public Statement stmt = null;
	
	public static void copyFile( File from, File to ) throws IOException {
	    Files.copy( from.toPath(), to.toPath() );
	} 

	public static Connection poveziBazo() {
		try {

			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
			
			String dbFile = "C:\\Users\\nejcs\\git\\EnglishVerbsJava\\EnglishVerbsJava\\bin\\DB\\vilka.db";
			String destination = "C:\\Users\\nejcs\\git\\EnglishVerbsJava\\EnglishVerbsJava\\bin\\vilka.db";
			File file = new File(dbFile); 
			
			File dirFrom = new File(dbFile);
			File dirTo = new File(destination);

			try {
			        copyFile(dirFrom, dirTo);
			        System.out.println("narjeno");
			} catch (IOException ex) {

			}
			
	          
			
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

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Prišlo je do napake pri povezovanju z bazo podatkov" + e.toString(),
					"Napaka", JOptionPane.WARNING_MESSAGE);
		}

	}

	private static void kreirajTabGlagol() throws SQLException {

		query = "CREATE TABLE IF NOT EXISTS glagoli (\n" + "	id integer NOT NULL,\n" + "	prevod TEXT,\n"
				+ "	verb TEXT,\n" + "	pastSimple TEXT,\n" + "	pastParticiple TEXT,\n"
				+ " PRIMARY KEY(\"id\" AUTOINCREMENT));";

		Statement stmt = conn.createStatement();
		stmt.execute(query);

	}

	private static void kreirajTabRoles() throws SQLException {

		query = "CREATE TABLE IF NOT EXISTS roles (\n" + "	id integer NOT NULL,\n" + "	role TEXT,\n"
				+ " PRIMARY KEY(\"id\" AUTOINCREMENT));";

		Statement stmt = conn.createStatement();
		stmt.execute(query);

	}

	private static void kreirajTabhelperTable() throws SQLException {
		query = "CREATE TABLE IF NOT EXISTS helperTable (\n" + "	id integer NOT NULL,\n" + "	ucenec TEXT,\n"
				+ "	glagol TEXT,\n" + "	glagol TEXT,\n"
				+ "	FOREIGN KEY(\"ucenec\") REFERENCES \"users2\"(\"id\") ON DELETE CASCADE,\n"
				+ " FOREIGN KEY(\"glagol\") REFERENCES \"glagoli\"(\"id\"),\n" + " PRIMARY KEY(\"id\" AUTOINCREMENT));";

		Statement stmt = conn.createStatement();
		stmt.execute(query);

	}

	private static void kreirajTabeloUsers2() throws SQLException {
		query = "CREATE TABLE IF NOT EXISTS users2 (\n" + "	id integer NOT NULL,\n" + "	username TEXT,\n"
				+ "	password TEXT,\n" + "	sequence TEXT,\n" + "	role INTEGER,\n"
				+ " PRIMARY KEY(\"id\" AUTOINCREMENT),\n"
				+ " FOREIGN KEY(\"role\") REFERENCES \"roles\"(\"id\") ON DELETE CASCADE);";

		Statement stmt = conn.createStatement();
		stmt.execute(query);

	}

}
