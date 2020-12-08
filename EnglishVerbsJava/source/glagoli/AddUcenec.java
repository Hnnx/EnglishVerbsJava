package glagoli;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import javax.swing.JTextField;

import DB.SqliteConnect;

import javax.swing.JButton;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AddUcenec extends SqliteConnect {

	// Framee
	private JFrame frame;

	// Podatki uporabnika
	private JTextField uporabniskoTxt;
	private JTextField pwTxt;
	private static int idUporabnika;

	// Gumbi
	private static JButton btnBack;
	private static JButton btnAdd;

	// --> WindowBuilder BOILERPLATE
	public static void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddUcenec window = new AddUcenec();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public AddUcenec() {
		initialize();
	}

	private static boolean isValidEmail(String mail) {

		return mail.length() >= 4 && mail.length() <= 20 ? true : false;

	}

	private static boolean isValidUsername(String username) {

		return username.length() >= 3 && username.length() >= 16 ? true : false;
	}

	private int getLastId() {
		int last = 0;
		try {
			
			query = "SELECT MAX(id) FROM users;";
			pSTMT = conn.prepareStatement(query);
			rs = pSTMT.executeQuery();
			
			while(rs.next()) {
				last = rs.getInt(1);
			}
			
		} catch (Exception e) {
			System.out.println("UH OH" + e.getMessage()+"\n"+ e.getStackTrace());
		}
		return last;
	}

	private void setSeq(int vnos) {
		try {
			query = "INSERT INTO infoBox (ucenec, sequence)" + "VALUES (?, ?);";
			pSTMT = conn.prepareStatement(query);

			pSTMT.setInt(1, vnos);
			pSTMT.setString(2, "0,0,0,0");

			pSTMT.execute();
			pSTMT.close();

		} catch (Exception e) {
			System.out.println("UH OH" + e.getMessage()+"\n"+ e.getStackTrace());
		}

	}

	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(SystemColor.inactiveCaption);
		frame.setBounds(100, 100, 256, 140);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel uporabniskoAdd = new JLabel("Uporabnik");
		uporabniskoAdd.setBounds(10, 11, 91, 14);
		frame.getContentPane().add(uporabniskoAdd);

		JLabel pwAdd = new JLabel("Geslo");
		pwAdd.setBounds(10, 41, 78, 14);
		frame.getContentPane().add(pwAdd);

		uporabniskoTxt = new JTextField();
		uporabniskoTxt.setBounds(66, 8, 131, 20);
		frame.getContentPane().add(uporabniskoTxt);
		uporabniskoTxt.setColumns(10);

		pwTxt = new JTextField();
		pwTxt.setBounds(66, 38, 131, 20);
		frame.getContentPane().add(pwTxt);
		pwTxt.setColumns(10);

		btnAdd = new JButton("DODAJ");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				conn = poveziBazo();

				String uporabnisko = uporabniskoTxt.getText();
				String password = pwTxt.getText();

				try {
					query = "INSERT INTO users (username, password) VALUES (?,?)";
					pSTMT = conn.prepareStatement(query);

					// VALIDATION
					if (!isValidUsername(uporabnisko) && !isValidEmail(password)) {
						JOptionPane.showMessageDialog(null,
								"Uporabnisko ime mora vsebovati med 3 in 16 znakov, geslo pa med 4 in 20", "Napaka",
								JOptionPane.WARNING_MESSAGE);
					} else {

						pSTMT.setString(1, uporabnisko.toLowerCase());
						pSTMT.setString(2, password.toLowerCase());

						pSTMT.execute();

						Ucitelj.refreshUcenecList();

						JOptionPane.showMessageDialog(null, "Shranjeno", "Uporabnik dodan",
								JOptionPane.INFORMATION_MESSAGE);

						// frame.dispose();


						pSTMT.close();

					}
					
					int v = getLastId();
					setSeq(v);

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Opis napake: \n " + ex.getMessage(), "Napaka :(",
							JOptionPane.WARNING_MESSAGE);
				}

			}
		});
		btnAdd.setBounds(127, 69, 86, 23);
		frame.getContentPane().add(btnAdd);

		btnBack = new JButton("NAZAJ");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				frame.dispose();

			}
		});
		btnBack.setBounds(20, 66, 81, 23);
		frame.getContentPane().add(btnBack);
	}
}
