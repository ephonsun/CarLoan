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
import transferObject.parameters.entityParameters.OptionalParameters;
import business.businessObjects.Optional;

public class OptionalDAO implements DAO<Optional>
{
	private static OptionalDAO instance;
	private String tableName = "optional"; 
	private OptionalDAO() {}
	
	public static DAO<Optional> getInstance()
	{
		if(instance == null) instance = new OptionalDAO();
		
		return instance;
	}
	
	@Override
	public void create(Optional entity) throws IntegrationException {
		String query = "insert into " + tableName + " (nome, descrizione, prezzo) values (?, ?, ?);";
		
		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();
		PreparedStatement s;
		try 
		{
			s = c.prepareStatement(query);
			
			s.setString(1, entity.getNome());
			s.setString(2, entity.getDescrizione());
			s.setFloat(3, entity.getPrezzo());

			s.executeUpdate();
		} catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
	}

	@Override
	public void update(Optional entity) throws IntegrationException {
		String query = "update " + tableName + " set nome = ?, prezzo = ?, descrizione = ? where id = ?;";
		
		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();

		PreparedStatement s;
		try 
		{
			s = c.prepareStatement(query);
			
			s.setString(1, entity.getNome());
			s.setFloat(2, entity.getPrezzo());
			s.setString(3, entity.getDescrizione());
			s.setString(4, entity.getID());

			if(s.executeUpdate() == 0)
				throw new IntegrationException("L'operazione non ha apportato modifiche.");
		} catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}	
	}

	@Override
	public Optional read(String ID) throws IntegrationException {
		String query = "select * from " + tableName + " where id = ?;";
		
		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();

		PreparedStatement s;
		ResultSet rs;
		Optional optional = null;
		try 
		{
			s = c.prepareStatement(query);
			
			s.setString(1, ID);
			
			rs = s.executeQuery();
			
			while(rs.next())
			{
				CarLoanTO optionalData = new CarLoanTO();
				optionalData.put(OptionalParameters.OPTIONAL_ID, rs.getInt("ID"));
				optionalData.put(OptionalParameters.NAME, rs.getString("nome"));
				optionalData.put(OptionalParameters.PRICE, rs.getString("prezzo"));
				optionalData.put(OptionalParameters.DESCRIPTION, rs.getString("descrizione"));
				
				optional = new Optional(optionalData);
			}
		} 
		catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
		
		return optional;
	}

	@Override
	public List<Optional> readAll(CarLoanTO parameters)
			throws IntegrationException {
		
		List<Optional> optionals = new ArrayList<Optional>();
		
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
				optionals.add(read(ID));
			}
		} 
		catch (SQLException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
		
		return optionals;
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
			
			s.setString(1, ID);
			
			if(s.executeUpdate() == 0)
				throw new IntegrationException("L'operazione non ha apportato modifiche.");			
		} catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
	}

}
