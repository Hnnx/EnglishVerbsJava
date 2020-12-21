package DB;

import java.sql.*;
import javax.swing.*;

public class SqliteConnect {
	
	static String dbName = "vilka.db";
	public static Connection conn = null;
	public static String query = "";
	public static ResultSet rs = null;
	public static PreparedStatement pSTMT = null;
	public Statement stmt = null;
	
	public static Connection poveziBazo() {
		try {

			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:"+dbName);
			
			//Kreiraj vse potrebne tabele
			kreirajTabGlagol();
			
			return conn;

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Opis napake: Prislo je do napake pri povezovanju z bazo podatkov" + e, "Napaka :(", JOptionPane.WARNING_MESSAGE);
			return null;
		}
	}
	
	protected static void kreirajVseTabele() {
		try {
			kreirajTabGlagol();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private static void kreirajTabGlagol() throws SQLException {
		System.out.println("start creating");
		
        	query = "CREATE TABLE IF NOT EXISTS glagoli (\n"
                + "	id integer NOT NULL,\n"
                + "	prevod TEXT,\n"
                + "	verb TEXT,\n"
                + "	pastSimple TEXT,\n"
                + "	pastParticiple TEXT,\n"
                + " PRIMARY KEY(\"id\" AUTOINCREMENT));";

		    Statement stmt = conn.createStatement();
		    stmt.execute(query);
		    
		    System.out.println("konec");
	}
	
	private static void kreirajTabRoles() throws SQLException {
		
	}
	
}

