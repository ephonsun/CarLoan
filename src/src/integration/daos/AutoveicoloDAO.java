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
import transferObject.parameters.entityParameters.CarParameters;
import transferObject.parameters.entityParameters.CarStatusParameters;
import business.businessObjects.Autoveicolo;
import business.businessObjects.Categoria;
import business.businessObjects.Sede;
import business.businessObjects.StatoAutoveicolo;

public class AutoveicoloDAO implements DAO<Autoveicolo>
{
	private static AutoveicoloDAO instance;
	private String tableName = "autoveicolo"; 
	private AutoveicoloDAO() {}
	
	public static DAO<Autoveicolo> getInstance()
	{
		if(instance == null) instance = new AutoveicoloDAO();
		
		return instance;
	}

	@Override
	public void create(Autoveicolo entity) throws IntegrationException {
		String query = "insert into " + tableName + " (marca, cilindrata, numeroPorte, targa, annoImmatricolazione, tipoCambio,"
				+ "accessoriSerie, idCategoria, numeroPosti, modello, idStato) values "
				+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
//				 + "'" + entity.getMarca() + "',"
//				 + " " + entity.getCilindrata() + " ,"
//				 + "'" + entity.getNumeroPorte() + "',"
//				 + "'" + entity.getTarga() + "', "
//				 + "'" + entity.getAnnoImmatricolazione() + "', "
//				 + "'" + entity.getTipoCambio() + "', "
//				 + "'" + entity.getAccessoriSerie() + "', "
//				 + "'" + entity.getCategoria().getID() + "', "
//				 + "'" + entity.getNumeroPosti() + "', "
//		 		 + "'" + entity.getModello() + "', ";

		
		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();

		PreparedStatement s;
		try 
		{
			DAOFactory.buildDAO("StatoAutoveicolo").create(entity.getStatoVeicolo());

			s = c.prepareStatement("select max(ID) as id from stato_autoveicolo");
			ResultSet rs = s.executeQuery();
			String ID = null;
			while(rs.next())
				ID = rs.getString("id");
			
			s = c.prepareStatement(query);
			
			s.setString(1,  entity.getMarca());
			s.setInt   (2,  entity.getCilindrata());
			s.setString(3,  entity.getNumeroPorte());
			s.setString(4,  entity.getTarga());
			s.setString(5,  entity.getAnnoImmatricolazione());
			s.setString(6,  entity.getTipoCambio());
			s.setString(7,  entity.getAccessoriSerie());
			s.setString(8,  entity.getCategoria().getID());
			s.setString(9,  entity.getNumeroPosti());
			s.setString(10, entity.getModello());
			s.setString(11, ID);
			
			s.executeUpdate();			
		} catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
	}

	@Override
	public void update(Autoveicolo entity) throws IntegrationException {
		String query = "update " + tableName + " set "
				+ "marca = ?, "
				+ "cilindrata = ?, "
				+ "numeroPorte = ?, "
				+ "targa = ?, "
				+ "annoImmatricolazione = ?, "
				+ "tipoCambio = ?, "
				+ "accessoriSerie = ?, "
				+ "idCategoria = ?, "
				+ "numeroPosti = ?, "
				+ "modello = ?, "
				+ "idStato = ? "
				+ "where id = ?";
		
		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();

		PreparedStatement s;
		try 
		{
			DAOFactory.buildDAO("StatoAutoveicolo").update(entity.getStatoVeicolo());

			s = c.prepareStatement(query);
			
			s.setString(1,  entity.getMarca());
			s.setInt   (2,  entity.getCilindrata());
			s.setString(3,  entity.getNumeroPorte());
			s.setString(4,  entity.getTarga());
			s.setString(5,  entity.getAnnoImmatricolazione());
			s.setString(6,  entity.getTipoCambio());
			s.setString(7,  entity.getAccessoriSerie());
			s.setString(8,  entity.getCategoria().getID());
			s.setString(9,  entity.getNumeroPosti());
			s.setString(10, entity.getModello());
			s.setString(11, entity.getStatoVeicolo().getID());
			s.setString(12, entity.getID());
			
			if(s.executeUpdate() == 0)
				throw new IntegrationException("L'operazione non ha comportato alcuna modifica");
		} catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
	}

	@Override
	public Autoveicolo read(String ID) throws IntegrationException {
		String query = "select * from " + tableName   + " where id = ?;";
		
		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();

		PreparedStatement s;
		ResultSet rs;
		Autoveicolo auto = null;
		try 
		{
			s = c.prepareStatement(query);
			
			s.setString(1, ID);
			
			rs = s.executeQuery();
			
			while(rs.next())
			{
				CarLoanTO data = new CarLoanTO();

				data.put(CarParameters.CAR_ID, rs.getInt("id"));
				data.put(CarParameters.BRAND, rs.getString("marca"));
				data.put(CarParameters.MODEL, rs.getString("modello"));
				data.put(CarParameters.ENGINE_CAPACITY, rs.getString("cilindrata"));
				data.put(CarParameters.DOOR_NUMBER, rs.getString("numeroPorte"));
				data.put(CarParameters.LICENCE_PLATE, rs.getString("targa"));
				data.put(CarParameters.MATRICULATION, rs.getString("annoImmatricolazione"));
				data.put(CarParameters.GEAR_TYPE, rs.getString("tipoCambio"));
				data.put(CarParameters.BASIC_OPTIONALS, rs.getString("accessoriSerie"));
				data.put(CarParameters.SEATS_NUMBER, rs.getString("numeroPosti"));
				data.put(CarParameters.CATEGORY, (Categoria)DAOFactory.buildDAO("Categoria").read(rs.getString("idCategoria")));
				data.put(CarParameters.CAR_STATUS, (StatoAutoveicolo)DAOFactory.buildDAO("StatoAutoveicolo").read(rs.getString("idStato")));

				auto = new Autoveicolo(data);
			}
		} 
		catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
		
		return auto;
	}

	@Override
	public List<Autoveicolo> readAll(CarLoanTO params) throws IntegrationException {
		List<Autoveicolo> cars = new ArrayList<>();
		
		String query = "select ID from " + tableName  + ";";
		
		Sede sede = null;
		if(params != null)
			sede = (Sede)params.get(CarStatusParameters.WORKPLACE);
		
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
				Autoveicolo a = read(ID);
				if(sede == null)
				{
					cars.add(a);
				}
				else if(a.getStatoVeicolo().getSedeVeicolo() != null && 
						a.getStatoVeicolo().getSedeVeicolo().getID().equals(sede.getID()))
				{
					cars.add(a);
				}
			}
		} 
		catch (SQLException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
		
		return cars;
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
			
			Autoveicolo car = read(ID);
			DAOFactory.buildDAO("StatoAutoveicolo").delete(car.getStatoVeicolo().getID());
			
			if(s.executeUpdate() == 0)
				throw new IntegrationException("L'operazione non ha comportato alcuna modifica");
		} catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}		
	}	
}
