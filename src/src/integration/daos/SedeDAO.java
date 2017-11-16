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
import transferObject.parameters.entityParameters.WorkplaceParameters;
import business.businessObjects.Autoveicolo;
import business.businessObjects.Impiegato;
import business.businessObjects.Sede;

public class SedeDAO implements DAO<Sede>
{
	private static SedeDAO instance;
	private String tableName = "sede"; 
	private SedeDAO() {}
	
	public static DAO<Sede> getInstance()
	{
		if(instance == null) instance = new SedeDAO();
		
		return instance;
	}
	
	@Override
	public void create(Sede entity) throws IntegrationException {
		String query = "insert into " + tableName + " (citta, indirizzo, numeroTelefono) values "
					 + "(?, ?, ?);";
		
		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();

		PreparedStatement s;
		try 
		{
			s = c.prepareStatement(query);
			
			s.setString(1, entity.getCitta());
			s.setString(2, entity.getIndirizzo());
			s.setString(3, entity.getNumeroTelefono());
			
			s.executeUpdate();
		} catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
	}

	@Override
	public void update(Sede entity) throws IntegrationException {	
		String query = "update " + tableName + " set "
					 + "citta = ?, "
					 + "indirizzo = ?, "
					 + "numeroTelefono = ? "
					 + "where id = ?;";
	
		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();

		PreparedStatement s;
		try 
		{
			s = c.prepareStatement(query);
			
			s.setString(1, entity.getCitta());
			s.setString(2, entity.getIndirizzo());
			s.setString(3, entity.getNumeroTelefono());
			s.setString(4, entity.getID());
		
			if(s.executeUpdate() == 0)
				throw new IntegrationException("L'operazione non ha apportato modifiche.");
		}
		catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}		
	}

	@Override
	public Sede read(String ID) throws IntegrationException {
		String query = "select * from " + tableName  + " where id = ?;";
		
		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();

		PreparedStatement s;
		ResultSet rs;
		Sede workplace = null;
		try 
		{
			s = c.prepareStatement(query);
			
			s.setString(1, ID);
			
			rs = s.executeQuery();
			
			while(rs.next())
			{
				CarLoanTO workplaceData = new CarLoanTO();
				workplaceData.put(WorkplaceParameters.WORKPLACE_ID, rs.getInt("ID"));
				workplaceData.put(WorkplaceParameters.ADDRESS, rs.getString("indirizzo"));
				workplaceData.put(WorkplaceParameters.CITY, rs.getString("citta"));
				workplaceData.put(WorkplaceParameters.TELEPHONE_NUMBER, rs.getString("numeroTelefono"));
				
				workplace = new Sede(workplaceData);
			}
		} 
		catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
		
		return workplace;
	}

	@Override
	public List<Sede> readAll(CarLoanTO params) throws IntegrationException 
	{
		List<Sede> workplaces = new ArrayList<Sede>();
		
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
				workplaces.add(read(ID));
			}
		} 
		catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
		
		return workplaces;
	}

	@Override
	public void delete(String ID) throws IntegrationException 
	{
		String query = "delete from " + tableName + " where id = ?;";
		
		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();

		PreparedStatement s;
		try 
		{
			List<Object> workers = DAOFactory.buildDAO("Impiegato").readAll(null); 
			for(Object w : workers)
			{
				Impiegato worker = (Impiegato)w;
				if(worker.getSede().getID().equals(ID))
				{
					worker.getSede().setID("0");
					DAOFactory.buildDAO("Impiegato").update(worker);
				}
			}
			
			List<Object> autos = DAOFactory.buildDAO("Autoveicolo").readAll(null); 
			for(Object a : autos)
			{
				Autoveicolo auto = (Autoveicolo)a;
				if(auto.getStatoVeicolo().getSedeVeicolo().getID().equals(ID))
				{
					auto.getStatoVeicolo().getSedeVeicolo().setID("0");
					DAOFactory.buildDAO("StatoAutoveicolo").update(auto.getStatoVeicolo());
				}
			}
			
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
