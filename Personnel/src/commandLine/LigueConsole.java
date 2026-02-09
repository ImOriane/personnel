package commandLine;

import static commandLineMenus.rendering.examples.util.InOut.getString;

import java.util.ArrayList;

import commandLineMenus.List;
import commandLineMenus.Menu;
import commandLineMenus.Option;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import personnel.*;

public class LigueConsole 
{
	private GestionPersonnel gestionPersonnel;
	private EmployeConsole employeConsole;
	
	

	public LigueConsole(GestionPersonnel gestionPersonnel, EmployeConsole employeConsole)
	{
		this.gestionPersonnel = gestionPersonnel;
		this.employeConsole = employeConsole;
	}

	Menu menuLigues()
	{
		Menu menu = new Menu("Gérer les ligues", "l");
		menu.add(afficherLigues());
		menu.add(ajouterLigue());
		menu.add(selectionnerLigue());
		menu.addBack("q");
		return menu;
	}

	private Option afficherLigues()
	{
		return new Option("Afficher les ligues", "l", () -> {System.out.println(gestionPersonnel.getLigues());});
	}

	private Option afficher(final Ligue ligue)
	{
		return new Option("Afficher la ligue", "l", 
				() -> 
				{
					System.out.println(ligue);
					System.out.println("administrée par " + ligue.getAdministrateur());
				}
		);
	}
	private Option listerEmployes(final Ligue ligue)
	{
		return new Option("Afficher les employes", "v", () -> {System.out.println(ligue.getEmployes());});
	}
	private List<Employe> afficherEmployes(final Ligue ligue)
	{
	    return new List<>(
	        "Sélectionner un employé", 
	        "m", 
	        () -> new ArrayList<>(ligue.getEmployes()),
	        (employe) -> employeConsole.editerEmploye(employe)
	    );
	}


	private Option ajouterLigue()
	{
		return new Option("Ajouter une ligue", "a", () -> 
		{
			try
			{
				gestionPersonnel.addLigue(getString("nom : "));
			}
			catch(SauvegardeImpossible exception)
			{
				System.err.println("Impossible de sauvegarder cette ligue");
			}
		});
	}
	
	private Menu editerLigue(Ligue ligue)
	{
		Menu menu = new Menu("Editer " + ligue.getNom());
		menu.add(afficher(ligue));
		menu.add(gererEmployes(ligue));
		menu.add(changerNom(ligue));
		menu.add(supprimer(ligue));
		menu.addBack("q");
		return menu;
	}

	private Option changerNom(final Ligue ligue)
	{
		return new Option("Renommer", "r", 
				() -> {ligue.setNom(getString("Nouveau nom : "));});
	}
	

	private List<Ligue> selectionnerLigue()
	{
		return new List<Ligue>("Sélectionner une ligue", "e", 
				() -> new ArrayList<>(gestionPersonnel.getLigues()),
				(element) -> editerLigue(element)
				);
	}
	
	private Option ajouterEmploye(final Ligue ligue)
	{
	    return new Option("ajouter un employé", "a",
	            () -> {
	                try {
	                    LocalDate dateArrivee = LocalDate.parse(
	                        getString("Date d'arrivée (AAAA-MM-JJ) : ")
	                    );

	                    ligue.addEmploye(
	                        getString("nom : "),
	                        getString("prenom : "),
	                        getString("mail : "),
	                        getString("password : "),
	                        dateArrivee
	                    );

	                }catch (DateTimeParseException e) {
	                    System.err.println("Erreur : la date doit exister et être au format AAAA-MM-JJ !");
	                } catch (SauvegardeImpossible e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	          
	            }
	    );
	}

	
	private Menu gererEmployes(Ligue ligue)
	{
		Menu menu = new Menu("Gérer les employés de " + ligue.getNom(), "e");
		menu.add(afficherEmployes(ligue)); //Changer ca pour un menu
		menu.add(listerEmployes(ligue));
		menu.add(ajouterEmploye(ligue));
		menu.addBack("q");
		return menu;
	}		

	
	private Option supprimer(Ligue ligue)
	{
		return new Option("Supprimer", "d", () -> {try {
			ligue.remove();
		} catch (SauvegardeImpossible e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}});
	}
	
}
