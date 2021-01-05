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

			// poveze JDBC driver
			Class.forName("org.sqlite.JDBC");

			// Pridobi path in absolutno vrednost, ki jo rabimo za kreiranje DBja v istem
			// folderju, kjer se nahaja aplikacija
			Path g = Paths.get("");
			String path = g.toAbsolutePath().toString();

			// Ustvarimo nov file v folderju, kjer se zažene aplikacija
			File f = new File(path + "//vilka.db");

			if (!f.exists()) {
				f.createNewFile();
			}

			// ime baze, povezovanje baze
			String dbName = "vilka.db";
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
			populateVerbs();
			populateRoles();

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Prišlo je do napake pri povezovanju z bazo podatkov" + e.toString(),
					"Napaka", JOptionPane.WARNING_MESSAGE);
		}

	}

	private static void kreirajTabGlagol() throws SQLException {

		query = "CREATE TABLE IF NOT EXISTS \"glagoli\" (\r\n" + "	\"id\"	INTEGER NOT NULL,\r\n"
				+ "	\"prevod\"	TEXT,\r\n" + "	\"verb\"	TEXT,\r\n" + "	\"pastSimple\"	TEXT,\r\n"
				+ "	\"pastParticiple\"	TEXT,\r\n" + "	PRIMARY KEY(\"id\" AUTOINCREMENT)\r\n" + ")";

		Statement stmt = conn.createStatement();
		stmt.execute(query);

	}

	private static void kreirajTabRoles() throws SQLException {

		query = "CREATE TABLE IF NOT EXISTS \"roles\" (\r\n" + "	\"id\"	INTEGER NOT NULL,\r\n"
				+ "	\"role\"	TEXT,\r\n" + "	PRIMARY KEY(\"id\" AUTOINCREMENT)\r\n" + ")";

		Statement stmt = conn.createStatement();
		stmt.execute(query);

	}

	private static void kreirajTabhelperTable() throws SQLException {
		query = "CREATE TABLE IF NOT EXISTS \"helperTable\" (\r\n" + "	\"id\"	INTEGER,\r\n"
				+ "	\"ucenec\"	TEXT,\r\n" + "	\"glagol\"	TEXT,\r\n"
				+ "	FOREIGN KEY(\"ucenec\") REFERENCES \"users2\"(\"id\") ON DELETE CASCADE,\r\n"
				+ "	FOREIGN KEY(\"glagol\") REFERENCES \"glagoli\"(\"id\"),\r\n"
				+ "	PRIMARY KEY(\"id\" AUTOINCREMENT)\r\n" + ")";

		Statement stmt = conn.createStatement();
		stmt.execute(query);

	}

	private static void kreirajTabeloUsers2() throws SQLException {

		query = "CREATE TABLE IF NOT EXISTS \"users2\" (\r\n" + "	\"id\"	INTEGER NOT NULL,\r\n"
				+ "	\"username\"	TEXT,\r\n" + "	\"password\"	TEXT,\r\n" + "	\"sequence\"	TEXT,\r\n"
				+ "	\"role\"	INTEGER,\r\n"
				+ "	FOREIGN KEY(\"role\") REFERENCES \"roles\"(\"id\") ON DELETE CASCADE,\r\n"
				+ "	PRIMARY KEY(\"id\" AUTOINCREMENT)\r\n" + ")";

		Statement stmt = conn.createStatement();
		stmt.execute(query);

	}

	private static void populateUsers() {

		try {

			// Preveri, če je admin račun že ustvarjen - če je število uporabnikov v DB
			// enako 0, ustvari Admin profil

			query = "SELECT * FROM users2";

			pSTMT = conn.prepareStatement(query);
			rs = pSTMT.executeQuery();

			int count = 0;
			while (rs.next()) {
				count++;
			}

			if (count == 0) {
				query = "INSERT INTO users2 (username, password, sequence, role) VALUES (?, ?, ?, ?)";
				pSTMT = conn.prepareStatement(query);

				pSTMT.setString(1, "admin");
				pSTMT.setString(2, "21232f297a57a5a743894a0e4a801fc3");
				pSTMT.setString(3, "1000");
				pSTMT.setInt(4, 2);

				pSTMT.execute();
			}

			rs.close();
			pSTMT.close();

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
					"Prišlo je do napake vnosu podatkov v bazo. Opis napake:\n" + e.toString(), "Napaka",
					JOptionPane.WARNING_MESSAGE);
		}

		catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Prišlo je do splošne napake pri vnosu podatkov v bazo. Opis napake:\n" + e.toString(), "Napaka",
					JOptionPane.WARNING_MESSAGE);
		}

	}

	private static void populateRoles() {
		
		try {
			
			query = "SELECT * FROM roles";
			pSTMT = conn.prepareStatement(query);
			
			int count = 0;
			
			rs = pSTMT.executeQuery();
			
			while(rs.next()) {
				count++;
			}
			
			if(count == 0) {
				
				
				query = "INSERT INTO roles (role) VALUES (?)";
				pSTMT = conn.prepareStatement(query);
				pSTMT.setString(1, "admin");
				pSTMT.execute();
				
				query = "INSERT INTO roles (role) VALUES (?)";
				pSTMT = conn.prepareStatement(query);
				pSTMT.setString(1, "ucitelj");
				pSTMT.execute();
				
				query = "INSERT INTO roles (role) VALUES (?)";
				pSTMT = conn.prepareStatement(query);
				pSTMT.setString(1, "ucenec");
				pSTMT.execute();
				
			}
			
			pSTMT.close();
			rs.close();
			
			
		}  catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
					"Prišlo je do napake pri vnosu vlog v bazo. Opis napake:\n" + e.toString(), "Napaka",
					JOptionPane.WARNING_MESSAGE);
		}
		
		
		catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Prišlo je do splošne napake pri vnosu vlog v bazo. Opis napake:\n" + e.toString(), "Napaka",
					JOptionPane.WARNING_MESSAGE);
		}
		
	}
	private static void populateVerbs() {
		
		
		
		
		Inserter i = new Inserter();
		
		try {
			
			// Preveri, ce gre za prvi zagon/prazeno tabelo
			query = "SELECT * FROM glagoli";
			pSTMT = conn.prepareStatement(query);
			rs = pSTMT.executeQuery();

			int count = 0;
			while (rs.next()) {
				count++;
			}
			
			if(count == 0) {
				i.add("bivati", "abode", "abode", "abide");
				i.add("postati", "become", "became", "become");
				i.add("začeti", "begin", "began", "begun");
				i.add("staviti", "bet", "bet", "bet");
				i.add("vezati", "bind", "bound", "bound");
				i.add("krvaveti", "bleed", "bled", "bled");
				i.add("ujeti", "catch", "caught", "caught");
				i.add("priti", "come", "came", "come");
				i.add("izbrati", "choose", "chose", "chosen");
				i.add("počiti", "burst", "burst", "burst");
				i.add("graditi", "build", "built", "built");
				i.add("prinesti", "bring", "brought", "brought");
				i.add("zlomiti", "break", "broken", "broken");
				i.add("pihati", "blow", "blew", "blown");
				i.add("gristi", "bite", "bit", "bitten");
				i.add("jesti", "eat", "ate", "eaten");
				i.add("čutiti", "feel", "felt", "felt");
				i.add("najti", "find", "found", "found");
				i.add("bežati", "flee", "flew", "flown");
				i.add("prepovedati", "fobid", "forbade", "forbidden");
				i.add("pozabiti", "forget", "forgot", "forgotten");
				i.add("vreči", "fling", "flung", "flung");
				i.add("predvidevati", "forsee", "forsaw", "forseen");
				i.add("zamrzniti", "freeze", "froze", "frozen");
				i.add("dati", "give", "gave", "given");
				i.add("iti", "go", "went", "gone");
				i.add("imeti", "have", "had", "had");
				i.add("slišati", "hear", "heard", "heard");
				i.add("držati", "hold", "held", "held");
				i.add("posoditi", "lend", "lent", "lent");
				i.add("ležati", "lie", "lay", "lain");
				i.add("pustiti", "let", "let", "let");
				i.add("izgubiti", "lose", "lost", "lost");
				i.add("narediti", "make", "made", "made");
				i.add("srečati", "meet", "met", "met");
				i.add("založiti", "mislay", "mislaid", "mislaid");
				i.add("plačati", "pay", "paid", "paid");
				i.add("položiti", "put", "put", "put");
				i.add("zvoniti", "ring", "rang", "rung");
				i.add("teči", "run", "ran", "run");
				i.add("videti", "see", "saw", "seen");
				i.add("prodati", "sell", "sold", "sold");
				i.add("tresti", "shake", "shook", "shaken");
				i.add("sijati", "shine", "shone", "shone");
				i.add("peti", "sing", "sang", "sung");
				i.add("ubiti", "slay", "slew", "slain");
				i.add("spati", "sleep", "slept", "slept");
				i.add("drseti", "slide", "slid", "slid");
				i.add("govoriti", "speak", "spoke", "spoken");
				i.add("skakati", "spring", "sprang", "sprung");
				i.add("krasti", "steal", "stole", "stolen");
				i.add("napeti", "string", "strung", "strung");
				i.add("plavati", "swim", "swam", "swum");
				i.add("vzeti", "take", "took", "taken");
				i.add("učiti", "teach", "taught", "taught");
				i.add("nihati", "swing", "swung", "swung");
				i.add("strmeti", "stive", "strove", "striven");
				i.add("povedati", "tell", "told", "told");
				i.add("misliti", "think", "thought", "thought");
				i.add("raztrgati", "tear", "tore", "torn");
				i.add("nositi", "wear", "worn", "worn");
				i.add("razumeti", "understand", "understood", "understood");
				i.add("zmagati", "win", "won", "won");
				i.add("jokati", "weep", "wept", "wept");
				i.add("pisati", "write", "wrote", "written");
				
			}
			
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null,
					"Prišlo je do napake pri vnosu glagolov v bazo. Opis napake:\n" + e.toString(), "Napaka",
					JOptionPane.WARNING_MESSAGE);
		} 
		
		catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Prišlo je do splošne napake pri vnosu glagolov v bazo. Opis napake:\n" + e.toString(), "Napaka",
					JOptionPane.WARNING_MESSAGE);
		}
		
		
	}
}
