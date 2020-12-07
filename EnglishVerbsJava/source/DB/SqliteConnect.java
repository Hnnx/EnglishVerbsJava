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
			return conn;

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Opis napake: Prislo je do napake pri povezovanju z bazo podatkov" + e, "Napaka :(", JOptionPane.WARNING_MESSAGE);
			return null;
		}

	}

}
