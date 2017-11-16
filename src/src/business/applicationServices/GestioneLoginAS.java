package business.applicationServices;

import integration.daos.DAO;
import integration.daos.DAOFactory;
import integration.exceptions.IntegrationException;

import java.util.Iterator;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

import business.businessObjects.UserPermission;
import business.businessObjects.Utente;
import business.businessObjects.LoginUtente;
import business.exceptions.BusinessException;
import business.exceptions.LoginException;
import transferObject.CarLoanTO;
import transferObject.parameters.EntityOperation;
import transferObject.parameters.GenericEntityParameters;
import transferObject.parameters.ServiceParameters;
import transferObject.parameters.entityParameters.LoginParameters;
import utils.FieldChecker;

public class GestioneLoginAS implements ApplicationService
{
	private static GestioneLoginAS instance;
	
	private GestioneLoginAS() {}
	
	public static GestioneLoginAS getInstance() {
		if(instance == null) instance = new GestioneLoginAS();
		
		return instance;
	}
	
	public Utente Login(CarLoanTO data) throws BusinessException
	{
		check(data);
		
		String username = (String)data.get(LoginParameters.USERNAME);
		String password = (String)data.get(LoginParameters.PASSWORD);
		String id = "";
		UserPermission permission = UserPermission.NONE;

		DAO<LoginUtente> dao;
		try {
			dao = DAOFactory.buildDAO("Login");
		} catch (IntegrationException e1) {
			throw new BusinessException(e1.getMessage());
		}
		
		List<LoginUtente> utenti;
		try {
			utenti = dao.readAll(null);
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
		Iterator<LoginUtente> it = utenti.iterator();
		boolean found = false;
		
		while(!found && it.hasNext())
		{
			LoginUtente user = it.next();
			if(user.getUsername().equals(username))
			{
				id = user.getID();
				permission = user.getUserPermission();
				found = true;
			}
		}
		
		CarLoanTO userDataRequest = new CarLoanTO();
		userDataRequest.put(ServiceParameters.SERVICE_NAME, EntityOperation.VIEW.getServiceName());
		userDataRequest.put(ServiceParameters.SERVICE_ENTITY, "GestioneUtente");
		userDataRequest.put(GenericEntityParameters.ENTITY_IDENTIFIER, id);
		CarLoanTO userLoggedData = ASFactory.getApplicationService().callService(userDataRequest);
		
		Utente u = (Utente)userLoggedData.get(ServiceParameters.SERVICE_RESULT);
		
		u.setUsername(username);
		u.setPassword(password);
		u.setUserPermission(permission);

		return u;
	}

	@Override
	public boolean check(CarLoanTO entityParameters) throws BusinessException {
		DAO<LoginUtente> dao;
		List<LoginUtente> utenti;
		
		String username = (String)entityParameters.get(LoginParameters.USERNAME);
		if(!FieldChecker.isValidUsername(username))
			throw new LoginException("Errore nell'inserimento dell'username");
		else 
		{
			try {
				dao = DAOFactory.buildDAO("Login");
				utenti = dao.readAll(null);
				
				boolean found = false;
				for(LoginUtente c : utenti)
				{
					found |= c.getUsername().equals(username);
				}
				
				if(!found)
					throw new LoginException("Username errato!");
			} catch (BusinessException | 
					 IntegrationException e) {
				throw new LoginException(e.getMessage());
			}
		}
		
		String password = (String)entityParameters.get(LoginParameters.PASSWORD);
		if(!FieldChecker.isValidPassword(password))
			throw new LoginException("Errore nell'inserimento della password");
		else 
		{
			try {
				boolean found = false;
				String hashPassword = Hashing.sha512().hashString(password, Charsets.UTF_8).toString();

				for(LoginUtente c : utenti)
				{
					found |= c.getPassword().equals(hashPassword);
				}
				
				if(!found)
					throw new LoginException("Password errata!");
			} catch (BusinessException e) {
				throw new LoginException(e.getMessage());
			}
		}
		
		return true;
	}
}
