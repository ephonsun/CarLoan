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
import transferObject.parameters.entityParameters.RentalRateParameters;
import business.businessObjects.Tariffa;

public class TariffaDAO implements DAO<Tariffa>
{
	private static TariffaDAO instance;
	private String tableName = "tariffa"; 
	private TariffaDAO() {}
	
	public static DAO<Tariffa> getInstance()
	{
		if(instance == null) instance = new TariffaDAO();
		
		return instance;
	}
	
	@Override
	public void create(Tariffa entity) throws IntegrationException 
	{
		String query = "insert into " + tableName + " (nome, prezzoGiornaliero, tipotariffa, descrizione, "
					 + "prezzoKM, prezzoGiornalieroBonus, prezzoKMBonus) values "
					 + "(?, ?, ?, ?, ?, ?, ?);";
		
		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();

		PreparedStatement s;
		try 
		{
			s = c.prepareStatement(query);
			
			s.setString(1, entity.getNome());
			s.setFloat(2, entity.getPrezzoGiornaliero());
			s.setString(3, entity.getTipo());
			s.setString(4, entity.getDescrizione());
			s.setFloat(5, entity.getPrezzoChilometro());
			s.setFloat(6, entity.getPrezzoGiornalieroBonus());
			s.setFloat(7, entity.getPrezzoChilometroBonus());

			s.executeUpdate();
		} catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
	}

	@Override
	public void update(Tariffa entity) throws IntegrationException {
		String query = "update " + tableName + " set "
					 + "nome = ?,"
					 + "prezzoGiornaliero = ?,"
					 + "prezzoKM = ?,"
					 + "prezzoGiornalieroBonus = ?,"
					 + "prezzoKMBonus = ?,"
					 + "descrizione = ?,"
					 + "tipoTariffa = ? "
					 + "where id = ?;";
		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();

		PreparedStatement s;
		try 
		{
			s = c.prepareStatement(query);
			
			s.setString(1, entity.getNome());
			s.setFloat( 2, entity.getPrezzoGiornaliero());
			s.setFloat( 3, entity.getPrezzoChilometro());
			s.setFloat( 4, entity.getPrezzoGiornalieroBonus());
			s.setFloat( 5, entity.getPrezzoChilometroBonus());
			s.setString(6, entity.getDescrizione());
			s.setString(7, entity.getTipo());
			s.setString(8, entity.getID());

			if(s.executeUpdate() == 0)
				throw new IntegrationException("L'operazione non ha apportato modifiche.");
		}
		catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
	}

	@Override
	public Tariffa read(String ID) throws IntegrationException {
		String query = "select * from " + tableName  + " where id = ?;";
		
		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();

		PreparedStatement s;
		ResultSet rs;
		Tariffa tariffa = null;
		try 
		{
			s = c.prepareStatement(query);
			
			s.setString(1, ID);
			
			rs = s.executeQuery();
			while(rs.next())
			{
				CarLoanTO loanRateData = new CarLoanTO();

				loanRateData.put(RentalRateParameters.RATE_ID, rs.getInt("ID"));
				loanRateData.put(RentalRateParameters.NAME, rs.getString("nome"));
				loanRateData.put(RentalRateParameters.DESCRIPTION, rs.getString("descrizione"));
				loanRateData.put(RentalRateParameters.BASE_PRICE, rs.getString("prezzoGiornaliero"));
				loanRateData.put(RentalRateParameters.KM_PRICE, rs.getString("prezzoKM"));
				loanRateData.put(RentalRateParameters.TYPE, rs.getString("tipoTariffa"));
				loanRateData.put(RentalRateParameters.BASE_PRICE_BONUS, rs.getString("prezzoGiornalieroBonus"));
				loanRateData.put(RentalRateParameters.KM_PRICE_BONUS, rs.getString("prezzoKMBonus"));

				tariffa = new Tariffa(loanRateData);
			}
		} 
		catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
		return tariffa;
	}

	@Override
	public List<Tariffa> readAll(CarLoanTO parameters)
			throws IntegrationException {
		List<Tariffa> rates = new ArrayList<Tariffa>();
		
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
				rates.add(read(ID));
			}
		} 
		catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
		
		return rates;
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
		}
		catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
	}
}
