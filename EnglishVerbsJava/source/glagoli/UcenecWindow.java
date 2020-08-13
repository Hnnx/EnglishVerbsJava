package glagoli;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class UcenecWindow {

	private JFrame frmUporabnik;

	public static void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UcenecWindow window = new UcenecWindow();
					window.frmUporabnik.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public UcenecWindow() {
		initialize();
	}

	private void initialize() {
		frmUporabnik = new JFrame();
		frmUporabnik.setTitle("Ucenje Glagolov UPORABNIK: " + LoginForm.username);
		frmUporabnik.setBounds(100, 100, 969, 638);
		frmUporabnik.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmUporabnik.getContentPane().setLayout(null);
	}

}
