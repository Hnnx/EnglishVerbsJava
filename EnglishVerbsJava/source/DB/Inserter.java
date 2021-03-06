package DB;

import java.sql.SQLException;

import javax.swing.JOptionPane;

public class Inserter extends SqliteConnect {

	protected void add(String prevod, String verb, String pastSimple, String pastParticiple) {

		try {
			conn = poveziBazo();

				query = "INSERT INTO glagoli (prevod, verb, pastSimple, pastParticiple) VALUES (?, ?, ?, ?)";
				pSTMT = conn.prepareStatement(query);
				
				pSTMT.setString(1, prevod);
				pSTMT.setString(2, verb);
				pSTMT.setString(3, pastSimple);
				pSTMT.setString(4, pastParticiple);
				
				pSTMT.execute();
				
				pSTMT.close();
				

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Prišlo je do napake pri dodajanju glagolov v tabelo. Opis napake:\n" + e.toString(),
					"Napaka", JOptionPane.WARNING_MESSAGE);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Prišlo je do napake pri dodajanju glagolov v tabelo. Opis napake:\n" + e.toString(),
					"Napaka", JOptionPane.WARNING_MESSAGE);
		}
	}

}
