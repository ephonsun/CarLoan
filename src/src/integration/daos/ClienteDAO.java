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
import transferObject.parameters.entityParameters.CustomerParameters;
import business.businessObjects.Cliente;

public class ClienteDAO implements DAO<Cliente>{

	private static ClienteDAO instance;
	private String tableName = "cliente"; 
	private ClienteDAO() {}
	
	public static DAO<Cliente> getInstance()
	{
		if(instance == null) instance = new ClienteDAO();
		
		return instance;
	}
	
	@Override
	public void create(Cliente entity) throws IntegrationException 
	{
		String query = "insert into " + tableName + " (nome, cognome, numeroTelefono, "
				+ "dataNascita, cittaResidenza, IndirizzoResidenza, codiceFiscale, tipoPatente, "
				+ "numeroPatente, scadenzaPatente) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
//				+ "'" + entity.getNome() + "', " 
//				+ "'" + entity.getCognome() + "', " 
//				+ "'" + entity.getNumeroTelefono() + "', " 
//				+ "'" + entity.getDataNascita().getYear() + "-"  + entity.getDataNascita().getMonthValue() + "-" + entity.getDataNascita().getDayOfMonth() + "', " 
//				+ "'" + entity.getCittaResidenza() + "', " 
//				+ "'" + entity.getIndirizzoResidenza() + "', " 
//				+ "'" + entity.getCodiceFiscale() + "', " 
//				+ "'" + entity.getTipoPatente() + "', " 
//				+ "'" + entity.getNumeroPatente() + "', " 
//				+ "'" + entity.getScadenzaPatente().getYear() + "-"  + entity.getScadenzaPatente().getMonthValue() + "-" + entity.getScadenzaPatente().getDayOfMonth() + "'"
//				+ ");";
		
		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();

		PreparedStatement s;
		try 
		{
			s = c.prepareStatement(query);
			
			s.setString(1, entity.getNome());
			s.setString(2, entity.getCognome());
			s.setString(3, entity.getNumeroTelefono());
			s.setString(4, entity.getDataNascita().getYear() + "-"  + entity.getDataNascita().getMonthValue() + "-" + entity.getDataNascita().getDayOfMonth());
			s.setString(5, entity.getCittaResidenza());
			s.setString(6, entity.getIndirizzoResidenza());
			s.setString(7, entity.getCodiceFiscale());
			s.setString(8, entity.getTipoPatente());
			s.setString(9, entity.getNumeroPatente());
			s.setString(10,entity.getScadenzaPatente().getYear() + "-"  + entity.getScadenzaPatente().getMonthValue() + "-" + entity.getScadenzaPatente().getDayOfMonth());
			
			s.executeUpdate();
		} catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
	}

	@Override
	public void update(Cliente entity) throws IntegrationException 
	{
		String query = "update " + tableName + " set "
					 + "nome = ?, cognome = ?, numeroTelefono = ?, dataNascita = ?, cittaResidenza = ?, "
					 + "indirizzoResidenza = ?, codiceFiscale = ?, tipoPatente = ?, numeroPatente = ?, "
					 + "scadenzaPatente = ? where id = ?;";
		
		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();

		PreparedStatement s;
		try 
		{
			s = c.prepareStatement(query);
			
			s.setString(1 ,	entity.getNome());
			s.setString(2 ,	entity.getCognome());
			s.setString(3 ,	entity.getNumeroTelefono());
			s.setString(4 ,	entity.getDataNascita().getYear() + "-"  + entity.getDataNascita().getMonthValue() + "-" + entity.getDataNascita().getDayOfMonth());
			s.setString(5 ,	entity.getCittaResidenza());
			s.setString(6 ,	entity.getIndirizzoResidenza());
			s.setString(7 , entity.getCodiceFiscale());
			s.setString(8 , entity.getTipoPatente());
			s.setString(9 , entity.getNumeroPatente());
			s.setString(10, entity.getScadenzaPatente().getYear() + "-"  + entity.getScadenzaPatente().getMonthValue() + "-" + entity.getScadenzaPatente().getDayOfMonth());
			s.setString(11, entity.getID());
			
			if(s.executeUpdate() == 0)
				throw new IntegrationException("L'operazione non ha apportato modifiche.");
		} catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
	}

	@Override
	public Cliente read(String ID) throws IntegrationException 
	{
		String query = "select * from " + tableName  + " where id = ?;";
		
		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();

		PreparedStatement s;
		ResultSet rs;
		Cliente customer = null;
		try 
		{
			s = c.prepareStatement(query);
			
			s.setString(1, ID);
			
			rs = s.executeQuery();
			
			while(rs.next())
			{
				CarLoanTO customerData = new CarLoanTO();

				customerData.put(CustomerParameters.CUSTOMER_ID, rs.getString("ID"));
				customerData.put(CustomerParameters.NAME, rs.getString("nome"));
				customerData.put(CustomerParameters.SURNAME, rs.getString("cognome"));
				customerData.put(CustomerParameters.TELEPHONE_NUMBER, rs.getString("numeroTelefono"));
				customerData.put(CustomerParameters.TAX_CODE, rs.getString("codiceFiscale"));
				customerData.put(CustomerParameters.BIRTHDATE, rs.getDate("dataNascita").toString());
				customerData.put(CustomerParameters.CITY, rs.getString("cittaResidenza"));
				customerData.put(CustomerParameters.ADDRESS, rs.getString("indirizzoResidenza"));
				customerData.put(CustomerParameters.LICENCE_TYPE, rs.getString("tipoPatente"));
				customerData.put(CustomerParameters.LICENCE_NUMBER, rs.getString("numeroPatente"));
				customerData.put(CustomerParameters.LICENCE_EXPIRATION_DATE, rs.getDate("scadenzaPatente").toString());				

				customer = new Cliente(customerData);
			}
		} 
		catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
		
		return customer;
	}

	@Override
	public List<Cliente> readAll(CarLoanTO params) throws IntegrationException {
		List<Cliente> customers = new ArrayList<Cliente>();
		
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
				customers.add(read(ID));
			}
		} 
		catch (SQLException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
		
		return customers;
	}

	@Override
	public void delete(String ID) throws IntegrationException 
	{
		String query = "delete from " + tableName + " where id = ?;";

		System.out.println(query);
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
