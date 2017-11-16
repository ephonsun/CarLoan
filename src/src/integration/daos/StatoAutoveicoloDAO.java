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
import transferObject.parameters.entityParameters.CarStatusParameters;
import business.businessObjects.Sede;
import business.businessObjects.StatoAutoveicolo;

public class StatoAutoveicoloDAO implements DAO<StatoAutoveicolo>
{
	private static StatoAutoveicoloDAO instance;
	private String tableName = "stato_autoveicolo"; 
	private StatoAutoveicoloDAO() {}
	
	public static DAO<StatoAutoveicolo> getInstance()
	{
		if(instance == null) instance = new StatoAutoveicoloDAO();
		
		return instance;
	}
	@Override
	public void create(StatoAutoveicolo entity) throws IntegrationException {
		String query = "insert into " + tableName + " (numeroChilometri, stato, dettagli, idSede) values "
					 + "(?, ?, ?, ?);";

		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();

		PreparedStatement s;
		try 
		{
			s = c.prepareStatement(query);
			
			s.setInt(1, entity.getNumeroChilometri());
			s.setString(2, entity.getStato());
			s.setString(3, entity.getDettagli());
			s.setString(4, entity.getSedeVeicolo().getID());			
			
			s.executeUpdate();
		} catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
	}

	@Override
	public void update(StatoAutoveicolo entity) throws IntegrationException {
		String query = "update " + tableName + " set "
					 + "numeroChilometri = ?, " 
					 + "stato = ?, " 
					 + "dettagli = ?, " 
					 + "idSede = ? " 
					 + "where id = ?;";

		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();

		PreparedStatement s;
		try 
		{
			s = c.prepareStatement(query);
			
			s.setInt(1, entity.getNumeroChilometri());
			s.setString(2, entity.getStato());
			s.setString(3, entity.getDettagli());
			s.setString(4, entity.getSedeVeicolo().getID());
			s.setString(5, entity.getID());
			
			if(s.executeUpdate() == 0)
				throw new IntegrationException("L'operazione non ha apportato modifiche.");
		}
		catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
	}

	@Override
	public StatoAutoveicolo read(String ID) throws IntegrationException {
		String query = "select * from " + tableName  + " where id = ?;";
		
		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();

		PreparedStatement s;
		ResultSet rs;
		StatoAutoveicolo status = null;
		try 
		{
			s = c.prepareStatement(query);
			
			s.setString(1, ID);
			
			rs = s.executeQuery();
			
			while(rs.next())
			{
				CarLoanTO data = new CarLoanTO();

				data.put(CarStatusParameters.STATUS_ID, rs.getInt("ID"));
				data.put(CarStatusParameters.DETAILS, rs.getString("dettagli"));
				data.put(CarStatusParameters.N_KM, rs.getString("numeroChilometri"));
				data.put(CarStatusParameters.STATUS, rs.getString("stato"));
				data.put(CarStatusParameters.WORKPLACE, (Sede)DAOFactory.buildDAO("Sede").read(rs.getString("IDSede")));

				status = new StatoAutoveicolo(data);
			}
		} 
		catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
		
		return status;
	}

	@Override
	public List<StatoAutoveicolo> readAll(CarLoanTO parameters)
			throws IntegrationException {
		List<StatoAutoveicolo> list = new ArrayList<StatoAutoveicolo>();
		
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
				list.add(read(ID));
			}
		} 
		catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
		
		return list;
	}

	@Override
	public void delete(String ID) throws IntegrationException {
		String query = "delete from " + tableName +  " where id = ?;";

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
