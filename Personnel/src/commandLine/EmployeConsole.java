package commandLine;

import static commandLineMenus.rendering.examples.util.InOut.getString;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import commandLineMenus.ListOption;
import commandLineMenus.Menu;
import commandLineMenus.Option;
import personnel.DateException;
import personnel.Employe;

public class EmployeConsole 
{
	private Option afficher(final Employe employe)
	{
		return new Option("Afficher l'employé", "l", () -> {System.out.println(employe);});
	}

	ListOption<Employe> editerEmploye()
	{
		return (employe) -> editerEmploye(employe);		
	}

	Option editerEmploye(Employe employe)
	{
			Menu menu = new Menu("Gérer le compte " + employe.getNom(), "c");
			menu.add(afficher(employe));
			menu.add(changerNom(employe));
			menu.add(changerPrenom(employe));
			menu.add(changerMail(employe));
			menu.add(changerPassword(employe));
			menu.add(finContrat(employe));
			menu.add(suppEmployee(employe));
			
			menu.addBack("q");
			return menu;
	}
	

	private void supprimerEmploye(final Employe employe) {
		employe.remove()
		;

		
		
	}
	private Option suppEmployee(final Employe employe)
	{
	    return new Option(
	        "Supprimer l'employé",
	        "z",
	        () -> { supprimerEmploye(employe);  System.out.println("Employé supprimé !");}

	    );
	}
	
	private Option changerNom(final Employe employe)
	{
		return new Option("Changer le nom", "n", 
				() -> {employe.setNom(getString("Nouveau nom : "));}
			);
	}

	private Option changerPrenom(final Employe employe)
	{
		return new Option("Changer le prénom", "p", () -> {employe.setPrenom(getString("Nouveau prénom : "));});
	}
	
	private Option changerMail(final Employe employe)
	{
		return new Option("Changer le mail", "e", () -> {employe.setMail(getString("Nouveau mail : "));});
	}
	
	private Option changerPassword(final Employe employe)
	{
		return new Option("Changer le password", "x", () -> {employe.setPassword(getString("Nouveau password : "));});
	}
	
	private Option finContrat(final Employe employe)
	{
	    return new Option("Ajouter la date de fin de contrat", "r", () -> {
	    	try {
	        LocalDate dateDepart = LocalDate.parse(getString("Date de depart (yyyy-MM-dd) :"));
	        employe.setDateDepart(dateDepart);
	    	}catch (DateTimeParseException e) {
	    	    System.err.println("Erreur : la date doit exister et être au format AAAA-MM-JJ !");
	    	} catch (DateException e) {
	    		System.err.println("Erreur : la date d'arrivée ne doit pas être ultérieure à la date de départ !");
			}
	        
	    });
	}

}


