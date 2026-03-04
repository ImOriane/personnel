package jdbc;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;

import org.mindrot.jbcrypt.BCrypt;

import personnel.*;

public class JDBC implements Passerelle 
{
	Connection connection;

	public JDBC()
	{
		try
		{
			Class.forName(Credentials.getDriverClassName());
			connection = DriverManager.getConnection(Credentials.getUrl(), Credentials.getUser(), Credentials.getPassword());
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Pilote JDBC non installé.");
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
	
	@Override
	public GestionPersonnel getGestionPersonnel() 
	{
		GestionPersonnel gestionPersonnel = new GestionPersonnel();
		try 
		{
			String requete = "select * from ligue";
			Statement instruction = connection.createStatement();
			ResultSet ligues = instruction.executeQuery(requete);
			while (ligues.next())
				gestionPersonnel.addLigue(ligues.getInt(1), ligues.getString(2));
			
			String rootRequete = "SELECT id_personnel_, nom_perso, password_perso "
	                + "FROM personnels WHERE id_ligue IS NULL";
	        ResultSet rootRs = connection.createStatement().executeQuery(rootRequete);
	        
	        String requeteEmployes ="SELECT id_personnel_, nom_perso, prenom_perso, mail_perso, " +
	        	    "role_perso, password_perso, date_arrivée, date_depart_, id_ligue " +
	        	    "FROM personnels";

	        Statement instructionEmployes = connection.createStatement();
	        ResultSet rsEmployes = instructionEmployes.executeQuery(requeteEmployes);

	        	while (rsEmployes.next())
	        	{
	        	    int id = rsEmployes.getInt("id_personnel_");
	        	    String nom = rsEmployes.getString("nom_perso");
	        	    String prenom = rsEmployes.getString("prenom_perso");
	        	    String mail = rsEmployes.getString("mail_perso");
	        	    String role = rsEmployes.getString("role_perso");
	        	    String password = rsEmployes.getString("password_perso");

	        	    LocalDate dateArrivee = rsEmployes.getObject("date_arrivée", LocalDate.class);
	        	    LocalDate dateDepart = rsEmployes.getObject("date_depart_", LocalDate.class);

	        	    int idLigue = rsEmployes.getInt("id_ligue");

	    
	        	    Employe employe = new Employe(
	        	        gestionPersonnel,
	        	        nom,
	        	        password,
	        	        id,
	        	        role,
	        	        prenom,
	        	        mail,
	        	        dateArrivee,
	        	        dateDepart,
	        	        idLigue
	        	    );
	        	}
	        if (rootRs.next())
	        {
	            gestionPersonnel.addRoot( rootRs.getInt("id_personnel_"),rootRs.getString("nom_perso"),rootRs.getString("password_perso"));
	        }
	        else
	        {
	            gestionPersonnel.addRoot("root", "toor");
	        }
		}
		catch (SQLException e)
		{
			System.out.println(e);
		} catch (SauvegardeImpossible e)  { 
			System.out.println("Impossible de créer le root : " + e); 
		}
		return gestionPersonnel;
	}

	@Override
	public void sauvegarderGestionPersonnel(GestionPersonnel gestionPersonnel) throws SauvegardeImpossible 
	{
		close();
	}
	
	public void close() throws SauvegardeImpossible
	{
		try
		{
			if (connection != null)
				connection.close();
		}
		catch (SQLException e)
		{
			throw new SauvegardeImpossible(e);
		}
	}
	
	@Override
	public int insert(Ligue ligue) throws SauvegardeImpossible 
	{
		try 
		{
			PreparedStatement instruction;
			instruction = connection.prepareStatement("insert into ligue (nom) values(?)", Statement.RETURN_GENERATED_KEYS);
			instruction.setString(1, ligue.getNom());		
			instruction.executeUpdate();
			ResultSet id = instruction.getGeneratedKeys();
			id.next();
			return id.getInt(1);
		} 
		catch (SQLException exception) 
		{
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}		
	}
	
	public int insert(Employe employe) throws SauvegardeImpossible
	{
	    try {
	        PreparedStatement instruction = connection.prepareStatement(
	            "INSERT INTO personnels "
	          + "(nom_perso, prenom_perso, mail_perso, role_perso, password_perso, date_arrivée, date_depart_, id_ligue) "
	          + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
	          Statement.RETURN_GENERATED_KEYS
	        );

	        instruction.setString(1, employe.getNom());
	        instruction.setString(2, employe.getPrenom());
	        instruction.setString(3, employe.getMail());
	        instruction.setString(4, employe.getRole());
	        instruction.setString(5, employe.getPassword());
	        
	        if (employe.getDateArrivee() != null) {
	            instruction.setDate(6, java.sql.Date.valueOf(employe.getDateArrivee()));
	        } else {
	            instruction.setNull(6, java.sql.Types.DATE);
	        }
	        
	        if (employe.getDateDepart() != null) {
	            instruction.setDate(7, java.sql.Date.valueOf(employe.getDateDepart()));
	        } else {
	            instruction.setNull(7, java.sql.Types.DATE);
	        }
	        
	        if (employe.getLigue() != null) {
	            instruction.setInt(8, employe.getLigue().getId());
	        } else {
	            instruction.setNull(8, java.sql.Types.INTEGER);
	        }

	        instruction.executeUpdate();

	        ResultSet rs = instruction.getGeneratedKeys();
	        rs.next();
	        return rs.getInt(1);

	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new SauvegardeImpossible(e);
	    }
	}
	
	@Override
	public void remove(Ligue ligue) throws SauvegardeImpossible
	{
	    try {
	        PreparedStatement deleteEmployes = connection.prepareStatement(
	                "DELETE FROM personnels WHERE id_ligue = ?"
	            );
	            deleteEmployes.setLong(1, ligue.getId());
	            deleteEmployes.executeUpdate();
	        PreparedStatement instruction =connection.prepareStatement("DELETE FROM ligue WHERE id_ligue = ?");
	        instruction.setLong(1, ligue.getId());
	        instruction.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new SauvegardeImpossible(e);
	    }
	}
	
	
	@Override
	public void remove(Employe employe) throws SauvegardeImpossible
	{
	    try {
	        PreparedStatement instruction = connection.prepareStatement("DELETE FROM personnels WHERE id_personnel_ = ?");
	        instruction.setLong(1, employe.getId());
	        instruction.executeUpdate();

	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new SauvegardeImpossible(e);
	    }
	}

	@Override
	public void update(Employe employe, String type) throws SauvegardeImpossible{
		 try {
		        PreparedStatement instruction = connection.prepareStatement(
		            "UPDATE personnels SET " +type+ " = ? WHERE id_personnel_ = ?"
		        );
		        if (type.equals("prenom_perso")) {
		        instruction.setString(1, employe.getPrenom());
		        }
		        if (type.equals("nom_perso")) {
			        instruction.setString(1, employe.getNom());
			        }
		        if (type.equals("mail_perso")) {
			        instruction.setString(1, employe.getMail());
			        }
		        if (type.equals("password_perso")) {
			        instruction.setString(1, employe.getPassword());
			        }
		        if (type.equals("date_arrivée")) {
			        instruction.setDate(1, java.sql.Date.valueOf(employe.getDateArrivee()));
			        }
		        if (type.equals("date_depart_")) {
			        instruction.setDate(1, java.sql.Date.valueOf(employe.getDateDepart()));
			        }
		        if (type.equals("role_perso")) {
			        instruction.setString(1, employe.getRole());
			        }
		        instruction.setLong(2, employe.getId());
		        instruction.executeUpdate();
		    } catch (SQLException e) {
		        e.printStackTrace();
		        throw new SauvegardeImpossible(e);
		    }

		}
	
	 public void update (Ligue ligue) throws SauvegardeImpossible {
		 try {
		        PreparedStatement instruction = connection.prepareStatement(
		            "UPDATE ligue SET nom = ? WHERE id_ligue = ?"
		        );
		
		        instruction.setString(1, ligue.getNom());
		       
		        instruction.setLong(2, ligue.getId());
		        instruction.executeUpdate();
		    } catch (SQLException e) {
		        e.printStackTrace();
		        throw new SauvegardeImpossible(e);
		    }
	 }
}


