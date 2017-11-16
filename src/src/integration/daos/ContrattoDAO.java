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

import business.businessObjects.Contratto;
import business.businessObjects.Optional;
import transferObject.CarLoanTO;
import transferObject.parameters.entityParameters.ContractParameters;

public class ContrattoDAO implements DAO<Contratto>
{
	private static ContrattoDAO instance;
	private String tableName = "contratto"; 
	private ContrattoDAO() {}
	
	public static DAO<Contratto> getInstance()
	{
		if(instance == null) instance = new ContrattoDAO();
		
		return instance;
	}
	
	@Override
	public void create(Contratto entity) throws IntegrationException {
		String query = "insert into " + tableName + "  "
				+ "(dataInizio, dataFine, statoContratto, IDImpiegato, SedeRitiro, SedeStipula,"
				+ " IDCliente, IDAutoveicolo, IDTariffa, nChilometri, prezzo, dataStipula, dataChiusura)"
				+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
//				+ "'" + entity.getDataInizio().getYear() + "-"  + entity.getDataInizio().getMonthValue() + "-" + entity.getDataInizio().getDayOfMonth() + "', "
//				+ "'" + entity.getDataFine().getYear() + "-"  + entity.getDataFine().getMonthValue() + "-" + entity.getDataFine().getDayOfMonth() + "', "
//				+ "'" + entity.getStatoContratto() + "', " 
//				+ "'" + entity.getImpiegatoStipulatore().getID() + "', " 
//				+ "'" + entity.getSedeConsegna().getID() + "', " 
//				+ "'" + entity.getSedeRitiro().getID() + "', " 
//				+ "'" + entity.getClienteStipulante().getID() + "', " 
//				+ "'" + entity.getAutoveicolo().getID() + "', " 
//				+ "'" + entity.getTariffa().getID() + "', " 
//				+ "'" + entity.getnChilometri() + "',"
//				+ "'" + entity.getCosto() + "', " 
//				+ "'" + entity.getDataStipula().getYear() + "-"  + entity.getDataStipula().getMonthValue() + "-" + entity.getDataStipula().getDayOfMonth() + "', "
//				+ "NULL"
//				+ ");";
		
		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();
		PreparedStatement s;
		try 
		{
			s = c.prepareStatement(query);
			s.setString(1, entity.getDataInizio().getYear() + "-"  + entity.getDataInizio().getMonthValue() + "-" + entity.getDataInizio().getDayOfMonth());
			s.setString(2, entity.getDataFine().getYear() + "-"  + entity.getDataFine().getMonthValue() + "-" + entity.getDataFine().getDayOfMonth());
			s.setString(3, entity.getStatoContratto());
			s.setString(4, entity.getImpiegatoStipulatore().getID());
			s.setString(5, entity.getSedeConsegna().getID());
			s.setString(6, entity.getSedeRitiro().getID());
			s.setString(7, entity.getClienteStipulante().getID());
			s.setString(8, entity.getAutoveicolo().getID());
			s.setString(9, entity.getTariffa().getID());
			s.setInt(10, entity.getnChilometri());
			s.setFloat(11, entity.getCosto());
			s.setString(12, entity.getDataStipula().getYear() + "-"  + entity.getDataStipula().getMonthValue() + "-" + entity.getDataStipula().getDayOfMonth());
			s.setString(13, null);

			s.executeUpdate();
			
			// Get the last contract inserted
			ResultSet rs = s.executeQuery("select max(id) as id from contratto;");
			int ID = 0;
			while(rs.next())
				ID = rs.getInt("id");
			
			for(Optional o : entity.getListaOptional())
			{
				String query2 = "insert into optional_contratto (idContratto, idOptional) values ";
				query2 += "(" + ID + ", " + o.getID() + ");";
				s.executeUpdate(query2);
			}
		} 
		catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}		
	}

	@Override
	public void update(Contratto entity) throws IntegrationException {
//		String dataChiusura = entity.getDataChiusura() == null ? 
//							  "dataChiusura = NULL " : 
//							  "dataChiusura = '" + entity.getDataChiusura().getYear() + "-"  + entity.getDataChiusura().getMonthValue() + "-" + entity.getDataChiusura().getDayOfMonth() + "' ";
//		
//		String autoveicolo = entity.getAutoveicolo() == null ?
//							 "IDAutoveicolo = 0 " :
//							 "IDAutoveicolo = '" + entity.getAutoveicolo().getID() + "'";
//		
//		String tariffa = entity.getTariffa() == null ?
//				 "IDTariffa = 0 " :
//				 "IDTariffa = '" + entity.getTariffa().getID() + "'";
//		
//		String cliente = entity.getClienteStipulante() == null ?
//				 "IDCliente = 0 " :
//				 "IDCliente = '" + entity.getClienteStipulante().getID() + "'";
		
		String query = "update " + tableName + " set "
				+ "dataInizio = ?, " 
				+ "dataFine = ?, "
				+ "statoContratto = ?, " 
				+ "IDImpiegato = ?, " 
				+ "SedeRitiro = ?, " 
				+ "SedeStipula = ?, " 
				+ "IDCliente = ?, " 
				+ "IDAutoveicolo = ?, " 
				+ "IDTariffa = ?, " 
				+ "nChilometri = ?, "
				+ "prezzo = ?, " 
				+ "dataStipula = ?, "
				+ "dataChiusura = ? "
				+ "where id = ?;";
		
		
		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();
		int i = 0;
		PreparedStatement s;
		try 
		{
			s = c.prepareStatement(query);
			i++;
			s.setString(1, entity.getDataInizio().getYear() + "-"  + entity.getDataInizio().getMonthValue() + "-" + entity.getDataInizio().getDayOfMonth());
			i++;
			s.setString(2, entity.getDataFine().getYear() + "-"  + entity.getDataFine().getMonthValue() + "-" + entity.getDataFine().getDayOfMonth());
			i++;
			s.setString(3, entity.getStatoContratto());
			i++;
			s.setString(4, entity.getImpiegatoStipulatore().getID());
			i++;
			i++;
			s.setString(5, entity.getSedeConsegna().getID());
			i++;
			s.setString(6, entity.getSedeRitiro().getID());
			i++;
			s.setString(7, entity.getClienteStipulante().getID());
			i++;
			s.setString(8, entity.getAutoveicolo().getID());
			i++;
			s.setString(9, entity.getTariffa().getID());
			i++;
			s.setInt(10, entity.getnChilometri());
			i++;
			s.setFloat(11, entity.getCosto());
			i++;
			s.setString(12, entity.getDataStipula().getYear() + "-"  + entity.getDataStipula().getMonthValue() + "-" + entity.getDataStipula().getDayOfMonth());
			if(entity.getDataChiusura() != null)
				s.setString(13, entity.getDataChiusura().getYear() + "-"  + entity.getDataChiusura().getMonthValue() + "-" + entity.getDataChiusura().getDayOfMonth());
			else
				s.setString(13, null);
			i++;
			s.setString(14, entity.getID());
			i++;

			if(s.executeUpdate() == 0)
				throw new IntegrationException("L'operazione non ha apportato modifiche.");
			
//			if(entity.getAutoveicolo() != null)
//				if(entity.getAutoveicolo().getStatoVeicolo() != null)
			DAOFactory.buildDAO("StatoAutoveicolo").update(entity.getAutoveicolo().getStatoVeicolo());
			
			s.executeUpdate("delete from optional_contratto where idContratto = " + entity.getID() + ";");
			
			for(Optional o : entity.getListaOptional())
			{
				String query2 = "insert into optional_contratto (idContratto, idOptional) values ";
				query2 += "(" + entity.getID() + ", " + o.getID() + ");";
				s.executeUpdate(query2);		
			}
		} catch (SQLException | NullPointerException e) 
		{
			System.out.println(i);
			throw new IntegrationException(e.getMessage());
		}
	}

	@Override
	public Contratto read(String ID) throws IntegrationException {
		String query = "select * from " + tableName  + " where id = ?;";
		
		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();

		PreparedStatement s;
		ResultSet rs;
		Contratto contract = null;
		try 
		{
			s = c.prepareStatement(query);

			s.setString(1, ID);
			
			rs = s.executeQuery();
			
			while(rs.next())
			{
				CarLoanTO data = new CarLoanTO();

				data.put(ContractParameters.CONTRACT_ID, rs.getString("ID"));
				
				String closingDate = rs.getDate("DataChiusura") == null ? null : rs.getString("DataChiusura");
				if(closingDate != null)
					data.put(ContractParameters.CLOSING_DATE, closingDate);		
				data.put(ContractParameters.END_DATE, rs.getString("DataFine"));		
				data.put(ContractParameters.START_DATE, rs.getString("DataInizio"));	
				data.put(ContractParameters.STIPULATION_DATE, rs.getString("DataStipula"));

				data.put(ContractParameters.PRICE, rs.getString("Prezzo"));
				data.put(ContractParameters.N_KM, rs.getString("nChilometri"));
				data.put(ContractParameters.STATUS, rs.getString("StatoContratto"));
				
				data.put(ContractParameters.CAR, DAOFactory.buildDAO("Autoveicolo").read(rs.getString("IDAutoveicolo")));
				data.put(ContractParameters.CUSTOMER, DAOFactory.buildDAO("Cliente").read(rs.getString("IDCliente")));
				data.put(ContractParameters.END_WORKPLACE, DAOFactory.buildDAO("Sede").read(rs.getString("SedeRitiro")));
				data.put(ContractParameters.START_WORKPLACE, DAOFactory.buildDAO("Sede").read(rs.getString("SedeStipula")));
				data.put(ContractParameters.RENTAL_RATE, DAOFactory.buildDAO("Tariffa").read(rs.getString("IDTariffa")));
				data.put(ContractParameters.WORKER, DAOFactory.buildDAO("Utente").read(rs.getString("IDImpiegato")));

				Statement s1 = c.createStatement();

				ResultSet optResultSet = s1.executeQuery("select IDOptional from optional_contratto where idContratto = " + ID + ";");
				List<Optional> optionals = new ArrayList<Optional>();

				while(optResultSet.next())
				{
					optionals.add((Optional)DAOFactory.buildDAO("Optional").read(optResultSet.getString("IDOptional")));
				}
				
				data.put(ContractParameters.OPTIONALS, optionals);

				contract = new Contratto(data);
			}
		} 
		catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}
		
		return contract;
	}

	@Override
	public List<Contratto> readAll(CarLoanTO parameters)
			throws IntegrationException {
		List<Contratto> list = new ArrayList<Contratto>();
		
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
		String deleteContractQuery = "delete from " + tableName + " where id = " + ID;
		String deleteOptionalQuery = "delete from optional_contratto where idContratto = " + ID + ";";
	
		Connector con = ConnectorFactory.getInstance(ConnectorSupported.MYSQL);
		con.openConnection();
		Connection c = con.getConnection();

		PreparedStatement s;
		try 
		{
			s = c.prepareStatement(deleteContractQuery);
			
			s.executeUpdate(deleteOptionalQuery);

			
			if(s.executeUpdate() == 0)
				throw new IntegrationException("L'operazione non ha apportato modifiche.");
		} catch (SQLException | NullPointerException e) 
		{
			throw new IntegrationException(e.getMessage());
		}

	}

}
