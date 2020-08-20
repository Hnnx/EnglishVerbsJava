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
import java.sql.PreparedStatement;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class AddGlagol extends SqliteConnect {

	private JFrame frame;
	private JTextField glagolField;
	private JTextField prevodField;
	private JTextField simpleField;
	private JTextField participleField;

	public static void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddGlagol window = new AddGlagol();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public AddGlagol() {
		initialize();
	}

	private static boolean glagolIsValid(String glagol) {
		return glagol.length() < 25 && glagol.length() >= 2 ? true : false;
	}

	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(SystemColor.inactiveCaption);
		frame.setBounds(100, 100, 312, 221);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel glagolLabel = new JLabel("Glagol");
		glagolLabel.setBounds(10, 30, 46, 14);
		frame.getContentPane().add(glagolLabel);

		JLabel prevodLabel = new JLabel("Prevod");
		prevodLabel.setBounds(10, 55, 46, 14);
		frame.getContentPane().add(prevodLabel);

		JLabel pastSimpleLabel = new JLabel("Past Simple");
		pastSimpleLabel.setBounds(10, 82, 77, 14);
		frame.getContentPane().add(pastSimpleLabel);

		JLabel pastPariticpleLabel = new JLabel("Past Participle");
		pastPariticpleLabel.setBounds(10, 107, 77, 14);
		frame.getContentPane().add(pastPariticpleLabel);

		glagolField = new JTextField();
		glagolField.setBounds(88, 27, 134, 20);
		frame.getContentPane().add(glagolField);
		glagolField.setColumns(10);

		prevodField = new JTextField();
		prevodField.setColumns(10);
		prevodField.setBounds(88, 52, 134, 20);
		frame.getContentPane().add(prevodField);

		simpleField = new JTextField();
		simpleField.setColumns(10);
		simpleField.setBounds(88, 79, 134, 20);
		frame.getContentPane().add(simpleField);

		participleField = new JTextField();
		participleField.setColumns(10);
		participleField.setBounds(88, 104, 134, 20);
		frame.getContentPane().add(participleField);

		JButton dodajGlagolBtn = new JButton("Dodaj");
		dodajGlagolBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				conn = poveziBazo();

				String glagolStr = glagolField.getText();
				String prevodStr = prevodField.getText();
				String simpleStr = simpleField.getText();
				String participleStr = participleField.getText();

				try {

					String query = "INSERT into glagoli (pomen, glagol, tense, part) VALUES (?,?,?,?)";

					pSTMT = conn.prepareStatement(query);

					// VALIDATION
					if (glagolIsValid(glagolStr) && !glagolStr.isBlank() && glagolIsValid(prevodStr)
							&& !prevodStr.isBlank() && glagolIsValid(simpleStr) && !simpleStr.isBlank()
							&& glagolIsValid(participleStr) && !participleStr.isBlank()) {

						pSTMT.setString(1, glagolStr.toLowerCase());
						pSTMT.setString(2, prevodStr.toLowerCase());
						pSTMT.setString(3, simpleStr.toLowerCase());
						pSTMT.setString(4, participleStr.toLowerCase());

						pSTMT.execute();

						JOptionPane.showMessageDialog(null, "Shranjeno", "Glagol dodan",
								JOptionPane.INFORMATION_MESSAGE);

						pSTMT.close();

					} else {
						JOptionPane.showMessageDialog(null, "Glagol mora vsebovati med 2 in 25 znakov", "Napaka",
								JOptionPane.WARNING_MESSAGE);
					}

				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Glagol mora vsebovati med 2 in 25 znakov\nOpis Napake: " + ex,
							"Napaka", JOptionPane.WARNING_MESSAGE);

				}

			}
		});
		dodajGlagolBtn.setBackground(new Color(50, 205, 50));
		dodajGlagolBtn.setBounds(144, 148, 89, 23);
		frame.getContentPane().add(dodajGlagolBtn);

		JButton btnNewButton = new JButton("Nazaj");
		btnNewButton.setBackground(Color.WHITE);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				frame.dispose();
			}
		});
		btnNewButton.setBounds(31, 148, 89, 23);
		frame.getContentPane().add(btnNewButton);
	}
}
