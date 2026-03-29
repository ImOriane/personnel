package UI;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import personnel.GestionPersonnel;

	public class connexion extends JFrame {

	    public connexion(GestionPersonnel gestion) {
	        setTitle("Connexion");
	        setSize(300, 150);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setLocationRelativeTo(null);

	        setContentPane(new verifiepassword(gestion));

	        setVisible(true);
	    }
	}
	

