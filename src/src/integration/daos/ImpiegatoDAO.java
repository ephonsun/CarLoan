package integration.daos;

import integration.exceptions.IntegrationException;

import java.util.ArrayList;
import java.util.List;

import business.businessObjects.Impiegato;
import business.businessObjects.UserPermission;
import business.businessObjects.Utente;
import transferObject.CarLoanTO;

public class ImpiegatoDAO implements DAO<Impiegato>
{
	private static ImpiegatoDAO instance;
	private ImpiegatoDAO() {}
	
	public static DAO<Impiegato> getInstance()
	{
		if(instance == null) instance = new ImpiegatoDAO();
		
		return instance;
	}

	@Override
	public void create(Impiegato entity) throws IntegrationException {
		DAOFactory.buildDAO("Utente").create(entity);
	}

	@Override
	public void update(Impiegato entity) throws IntegrationException {
		DAOFactory.buildDAO("Utente").update(entity);
	}

	@Override
	public Impiegato read(String ID) throws IntegrationException 
	{	
		Utente u = (Utente)DAOFactory.buildDAO("Utente").read(ID);
		
		return u == null ? null : 
				new Impiegato(u.getUsername(), u.getPassword(), u.getNome(), u.getCognome(),
							  u.getID(), u.getDataNascita(), u.getCodiceFiscale(), u.getSede());
	}

	@Override
	public List<Impiegato> readAll(CarLoanTO parameters)
			throws IntegrationException {
		List<Object> users = DAOFactory.buildDAO("Utente").readAll(null);
		List<Impiegato> workers = new ArrayList<Impiegato>();
		for(Object o : users)
		{
			Utente user = (Utente)o;
			
			if(user.getUserPermission().equals(UserPermission.WORKER))
				workers.add(read(user.getID()));
		}
		return workers;
	}

	@Override
	public void delete(String ID) throws IntegrationException {
		DAOFactory.buildDAO("Utente").delete(ID);
	}

}