package personnel;

import java.sql.SQLException;

public interface Passerelle 
{
	public GestionPersonnel getGestionPersonnel();
	public void sauvegarderGestionPersonnel(GestionPersonnel gestionPersonnel)  throws SauvegardeImpossible;
	public int insert(Ligue ligue) throws SauvegardeImpossible;
	 public int insert(Employe employe) throws SauvegardeImpossible;
	 public void remove(Ligue ligue) throws SauvegardeImpossible;
	 public void remove(Employe employe) throws SauvegardeImpossible;
	 public void setnom (Employe employe, String nouveauNom) throws SauvegardeImpossible;
	 public void setprenom (Employe employe, String nouveauPrenom) throws SauvegardeImpossible;
	 public void setmail (Employe employe, String nouveauMail) throws SauvegardeImpossible;
	 public void setpassword (Employe employe, String nouveauMdp) throws SauvegardeImpossible;
}
