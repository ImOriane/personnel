package jdbc;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

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
		}
		catch (SQLException e)
		{
			System.out.println(e);
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
}

