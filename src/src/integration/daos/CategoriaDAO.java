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
import transferObject.parameters.entityParameters.CategoryParameters;
import business.businessObjects.Categoria;

public class CategoriaDAO implements DAO<Categoria>
{
	private static CategoriaDAO instance;
	private String tableName = "categoria"; 
	private CategoriaDAO() {}
	
	public static DAO<Categoria> getInstance()
	{
		if(instance == null) instance = new CategoriaDAO();
		
		return instance;
	}
	
	@Override
	public void create(Categoria entity) throws IntegrationException 
	{
		String query = "insert into " + tableName + " (nome, prezzoChilometrico, descrizione, cilindrata, tipoCambio) "
					 + "values (?, ?, ?, ?, ?)";
		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();

		PreparedStatement s;
		try 
		{
			s = c.prepareStatement(query);
			
			s.setString(1, entity.getNome());
			s.setFloat(2, entity.getPrezzoChilometro());
			s.setString(3, entity.getDescrizione());
			s.setInt(4, entity.getCilindrata());
			s.setString(5, entity.getTipoCambio());
			
			s.executeUpdate();
		} catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
		
	}

	@Override
	public void update(Categoria entity) throws IntegrationException {
		String query = "update " + tableName + " set "
				+ "nome = ?, prezzoChilometrico = ?, descrizione = ?, cilindrata = ?, tipoCambio = ? "
				+ "where id = ?;";
		
		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();

		PreparedStatement s;
		try 
		{
			s = c.prepareStatement(query);
			
			s.setString(1, entity.getNome());
			s.setFloat(2, entity.getPrezzoChilometro());
			s.setString(3, entity.getDescrizione());
			s.setInt(4, entity.getCilindrata());
			s.setString(5, entity.getTipoCambio());
			s.setString(6, entity.getID());
						
			if(s.executeUpdate() == 0)
				throw new IntegrationException("L'operazione non ha apportato modifiche.");
		} catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
	}

	@Override
	public Categoria read(String ID) throws IntegrationException {
		String query = "select * from " + tableName  + " where id = ?;";
		
		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();

		PreparedStatement s;
		ResultSet rs;
		Categoria customer = null;
		try 
		{
			s = c.prepareStatement(query);
			
			s.setString(1, ID);
			
			rs = s.executeQuery();
			
			while(rs.next())
			{
				CarLoanTO categoryData = new CarLoanTO();

				categoryData.put(CategoryParameters.CATEGORY_ID, rs.getInt("ID"));
				categoryData.put(CategoryParameters.NAME, rs.getString("nome"));
				categoryData.put(CategoryParameters.DESCRIPTION, rs.getString("descrizione"));
				categoryData.put(CategoryParameters.ENGINE_CAPACITY, rs.getString("cilindrata"));
				categoryData.put(CategoryParameters.GEAR_TYPE, rs.getString("tipoCambio"));
				categoryData.put(CategoryParameters.PRICE, rs.getString("prezzoChilometrico"));

				customer = new Categoria(categoryData);
			}
		} 
		catch (SQLException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
		
		return customer;
	}

	@Override
	public List<Categoria> readAll(CarLoanTO parameters)
			throws IntegrationException {
		List<Categoria> categories = new ArrayList<Categoria>();
		
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
				categories.add(read(ID));
			}
		} 
		catch (SQLException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
		
		return categories;
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
		} catch (SQLException e) 
		{
			throw new IntegrationException(e.getMessage());
		}		
	}

}
