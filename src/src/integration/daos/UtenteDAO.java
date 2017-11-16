package integration.daos;

import integration.connector.Connector;
import integration.connector.ConnectorFactory;
import integration.connector.ConnectorSupported;
import integration.exceptions.IntegrationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import transferObject.CarLoanTO;
import transferObject.parameters.entityParameters.UserParameters;
import business.businessObjects.LoginUtente;
import business.businessObjects.Sede;
import business.businessObjects.Utente;

public class UtenteDAO implements DAO<Utente>
{
	private static UtenteDAO instance;
	private String tableName = "utente"; 
	private UtenteDAO() {}
	
	public static DAO<Utente> getInstance()
	{
		if(instance == null) instance = new UtenteDAO();
		
		return instance;
	}
	
	@Override
	public void create(Utente entity) throws IntegrationException 
	{
		String query = "insert into " + tableName + " (nome, cognome, codiceFiscale, "
					 + "dataNascita, sede) values (?, ?, ?, ?, ?);";
		
		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();

		PreparedStatement s;
		try 
		{
			s = c.prepareStatement(query);
			
			s.setString(1, entity.getNome());
			s.setString(2, entity.getCognome());
			s.setString(3, entity.getCodiceFiscale());
			s.setString(4, entity.getDataNascita().getYear() + "-"  + entity.getDataNascita().getMonthValue() + "-" + entity.getDataNascita().getDayOfMonth());
			if(entity.getSede() != null)
				s.setString(5, entity.getSede().getID());
			else
				s.setString(5, null);
			
			s.executeUpdate();
			
			ResultSet rs = s.executeQuery("select max(id) as id from utente;");
			String ID = "";
			while(rs.next())
				ID = rs.getString("id");
			
			LoginUtente login = new LoginUtente(ID, entity.getUsername(), entity.getPassword(), entity.getUserPermission());
			
			DAOFactory.buildDAO("Login").create(login);		
		} catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
	}

	@Override
	public void update(Utente entity) throws IntegrationException {
		String query = "update utente set "
					 + "nome = ?, "
					 + "cognome = ?,"
					 + "codiceFiscale = ?,"
					 + "dataNascita = ?,"
					 + "sede = ? "
					 + "where id = ?;";

		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();

		PreparedStatement s;
		try 
		{
			s = c.prepareStatement(query);
			
			s.setString(1, entity.getNome());
			s.setString(2, entity.getCognome());
			s.setString(3, entity.getCodiceFiscale());
			s.setString(4, entity.getDataNascita().getYear() + "-"  + entity.getDataNascita().getMonthValue() + "-" + entity.getDataNascita().getDayOfMonth());
			if(entity.getSede() != null)
				s.setString(5, entity.getSede().getID());
			else
				s.setString(5, null);
			s.setString(6, entity.getID());
			
			if(s.executeUpdate() == 0)
				throw new IntegrationException("L'operazione non ha apportato modifiche.");
			
			if(entity.getUsername() != null)
				DAOFactory.buildDAO("Login").update(entity);
		} catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
	}

	@Override
	public Utente read(String ID) throws IntegrationException 
	{
		String query = "select * from " + tableName  + " where id = ?;";
		
		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();

		PreparedStatement s;
		ResultSet rs;
		Utente user = null;
		try 
		{
			s = c.prepareStatement(query);
			
			s.setString(1, ID);
			
			rs = s.executeQuery();
			
			CarLoanTO userData = new CarLoanTO();
			while(rs.next())
			{
				String idSede = rs.getString(tableName + ".sede");
				userData.put(UserParameters.USER_ID, rs.getString(tableName + ".id"));
				userData.put(UserParameters.NAME, rs.getString(tableName + ".nome"));
				userData.put(UserParameters.SURNAME, rs.getString(tableName + ".cognome"));
				userData.put(UserParameters.TAX_CODE, rs.getString(tableName + ".codiceFiscale"));
				userData.put(UserParameters.BIRTHDATE, rs.getString(tableName + ".dataNascita"));
				userData.put(UserParameters.WORKPLACE, (Sede)DAOFactory.buildDAO("Sede").read(idSede));
				
				LoginUtente loginData = (LoginUtente)DAOFactory.buildDAO("Login").read(ID);
				userData.put(UserParameters.USERNAME, loginData.getUsername());
				userData.put(UserParameters.PASSWORD, loginData.getPassword());
				userData.put(UserParameters.USER_PERMISSION, loginData.getUserPermission());
				
				user = new Utente(userData);
			}			
		} 
		catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
		
		return user;
	}

	@Override
	public List<Utente> readAll(CarLoanTO params) throws IntegrationException {
		List<Utente> users = new ArrayList<Utente>();
		
		String query = "select ID from " + tableName  + ";";
		
		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();

		Statement s;
		ResultSet rs;
		try 
		{
			s = c.createStatement();
			rs = s.executeQuery(query);
			
			while(rs.next())
			{
				String ID = rs.getString("id");
				users.add(read(ID));
			}
		} 
		catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
		
		return users;
	}

	@Override
	public void delete(String ID) throws IntegrationException {
		String query = "delete from " + tableName + " where id = ?;";
		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();
		PreparedStatement s;
		try 
		{
			s = c.prepareStatement(query);
			DAOFactory.buildDAO("Login").delete(ID);
			s.setString(1, ID);
			
			if(s.executeUpdate() == 0)
				throw new IntegrationException("L'operazione non ha apportato modifiche.");
		} catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
	}

}
