package UI;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import personnel.GestionPersonnel;

	public class verifiepassword extends JPanel {

	    public verifiepassword(GestionPersonnel gestion) {

	        setLayout(new GridLayout(3, 2));

	        JLabel label = new JLabel("Password :");
	        JPasswordField passwordField = new JPasswordField();
	        JButton bouton = new JButton("Connexion");
	        JLabel message = new JLabel("");

	        add(label);
	        add(passwordField);
	        add(new JLabel());
	        add(bouton);
	        add(message);

	        bouton.addActionListener(e -> {
	            String password = new String(passwordField.getPassword());

	            if (gestion.getRoot().checkPassword(password)) {
	                message.setText("Connexion réussie !"); 
	                //POur l'instant j'ai juste mi co reussie mais apres faudra redirect sur un nouveau pennel menu principal (voir la maquette c la premiere page qui s'affiche quand tu te co
	                
	            } else {
	                message.setText("Mot de passe incorrect");
	            }
	        });
	    }
	
}
