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
import business.businessObjects.LoginUtente;
import business.businessObjects.UserPermission;

public class LoginDAO implements DAO<LoginUtente>
{
	private static LoginDAO instance; 
	private LoginDAO() {}
	
	public static DAO<LoginUtente> getInstance()
	{
		if(instance == null) instance = new LoginDAO();
		
		return instance;
	}
	
	private static final String tableName = "Login";
	
	@Override
	public void create(LoginUtente entity) throws IntegrationException 
	{
		String query = "insert into login (username, password, tipoPermesso, idUtente)"
				+ " values (?, sha2(?, 512), ?, ?);";
		
		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();

		PreparedStatement s;
		try 
		{
			s = c.prepareStatement(query);
			
			s.setString(1, entity.getUsername());
			s.setString(2, entity.getPassword());
			s.setInt(3, entity.getUserPermission().getNumVal());
			s.setString(4, entity.getID());
			
			s.executeUpdate();
		}
		catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
	}

	@Override
	public void update(LoginUtente entity) throws IntegrationException {
		String loginUpdate = "update login set " 
				   + "username = ?, "
				   + "password = sha2(?, 512) "
				   + "where idUtente = ?;";
		
		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();

		PreparedStatement s;
		try 
		{
			s = c.prepareStatement(loginUpdate);
			
			s.setString(1, entity.getUsername());
			s.setString(2, entity.getPassword());
			s.setString(3, entity.getID());
			
			if(s.executeUpdate() == 0)
				throw new IntegrationException("L'operazione non ha apportato modifiche.");
		}
		catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
	}

	@Override
	public LoginUtente read(String ID) throws IntegrationException {
		String query = "select * from " + tableName + " where idUtente = ?;";
		
		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();

		PreparedStatement s;
		ResultSet rs;
		LoginUtente login = null;
		try 
		{
			s = c.prepareStatement(query);
			
			s.setString(1, ID);
			
			rs = s.executeQuery();
			
			while(rs.next())
			{
				String username = rs.getString("username");
				String password = rs.getString("password");
				UserPermission userPermission = UserPermission.values()[rs.getInt("tipoPermesso")];
								
				login = new LoginUtente(ID, username, password, userPermission);
			}
		} 
		catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
		
		return login;
	}

	@Override
	public List<LoginUtente> readAll(CarLoanTO params) throws IntegrationException 
	{
		List<LoginUtente> users = new ArrayList<LoginUtente>();
		
		String query = "select idUtente from " + tableName + ";";
		
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
				String ID = rs.getString("idUtente");
				users.add(read(ID));
			}
		} 
		catch (SQLException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
		return users;
	}

	@Override
	public void delete(String ID) throws IntegrationException {
		String deleteLoginQuery = "delete from login where idUtente = ?;";
		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();
		PreparedStatement s;
		try 
		{
			s = c.prepareStatement(deleteLoginQuery);
			
			s.setString(1, ID);
		
			if(s.executeUpdate() == 0)
				throw new IntegrationException("L'operazione non ha apportato modifiche.");
		} catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
	}

}
