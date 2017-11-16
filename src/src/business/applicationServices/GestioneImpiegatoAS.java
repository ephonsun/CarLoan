package business.applicationServices;

import integration.daos.DAO;
import integration.daos.DAOFactory;
import integration.exceptions.IntegrationException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import transferObject.CarLoanTO;
import transferObject.parameters.GenericEntityParameters;
import transferObject.parameters.entityParameters.WorkerParameters;
import utils.FieldChecker;
import business.businessObjects.Impiegato;
import business.businessObjects.LoginUtente;
import business.businessObjects.UserPermission;
import business.businessObjects.Utente;
import business.exceptions.BusinessException;
import business.exceptions.WorkerException;

public class GestioneImpiegatoAS implements ApplicationService, CRUD<Impiegato> 
{

	private static GestioneImpiegatoAS instance;

	private GestioneImpiegatoAS() {}

	public static GestioneImpiegatoAS getInstance() {
		if(instance == null) instance = new GestioneImpiegatoAS();

		return instance;
	}
	
	@Override
	public void create(CarLoanTO parameters) throws BusinessException {
		check((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		Impiegato c = new Impiegato((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		
		String username = c.getNome().substring(0, 1).toLowerCase() + "." 
				+ c.getCognome().toLowerCase();
		String password = "p.default";
		
		c.setPassword(password);
		c.setUserPermission(UserPermission.WORKER);
		
		List<Utente> logins = GestioneUtenteAS.getInstance().list(null);
		String actualUsername = username;
		int counter = 1;
		for(LoginUtente login : logins)
		{
			if(actualUsername.equals(login.getUsername()))
				actualUsername = username + "" + counter;
		}
		
		c.setUsername(actualUsername);
		
		try {
			DAOFactory.buildDAO("Impiegato").create(c);
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public void update(CarLoanTO parameters) throws BusinessException {
		check((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		Impiegato c = new Impiegato((CarLoanTO)parameters.get(GenericEntityParameters.ENTITY));
		
		try {
			DAOFactory.buildDAO("Impiegato").update(c);
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public Impiegato read(CarLoanTO parameters) throws BusinessException {
		Impiegato result;
		try {
			DAO<Impiegato> d = DAOFactory.buildDAO("Impiegato");
			result = d.read((String)parameters.get(GenericEntityParameters.ENTITY_IDENTIFIER));
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
		return result;
	}

	@Override
	public List<Impiegato> list(CarLoanTO parameters)
			throws BusinessException {
		List<Impiegato> list;
		try {
			DAO<Impiegato> d = DAOFactory.buildDAO("Impiegato");
			list = d.readAll(parameters);
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
		return list;
	}

	@Override
	public void delete(CarLoanTO parameters) throws BusinessException {
		try {
			DAOFactory.buildDAO("Impiegato").delete((String)parameters.get(GenericEntityParameters.ENTITY_IDENTIFIER));
		} catch (IntegrationException e) {
			throw new BusinessException(e.getMessage());
		}
	}

	@Override
	public boolean check(CarLoanTO entityParameters) throws BusinessException {
		
		String name = (String)entityParameters.get(WorkerParameters.NAME);
		if(!FieldChecker.isValidName(name))
			throw new WorkerException("Errore nell'inserimento del nome");
		
		String surname = (String)entityParameters.get(WorkerParameters.SURNAME);
		if(!FieldChecker.isValidName(surname))
			throw new WorkerException("Errore nell'inserimento del cognome");
		
		String dataNascita = (String)entityParameters.get(WorkerParameters.BIRTHDATE);
		if(!FieldChecker.isValidField(dataNascita))
		{
			throw new WorkerException("Data Nascita non inserita");
		}
		else
		{
			LocalDate data = LocalDate.parse(dataNascita);
			if((int)ChronoUnit.YEARS.between(data, LocalDate.now()) < 18)
				throw new WorkerException("Impiegato troppo giovane per essere assunto");
		}
				
		String ID = (String)entityParameters.get(WorkerParameters.WORKER_ID);
		String codiceFiscale = (String)entityParameters.get(WorkerParameters.TAX_CODE);
		if(!FieldChecker.isValidTaxCode(codiceFiscale))
			throw new WorkerException("Errore nell'inserimento del codice fiscale");
		else if(ID.equals("0"))
		{
			try {
				List<Impiegato> impiegati = list(null);
				for(Impiegato c : impiegati)
				{
					if(c.getCodiceFiscale().equals(codiceFiscale))
						throw new WorkerException("Errore: codice fiscale duplicato!");
				}
			} catch (BusinessException e) {
				throw new WorkerException(e.getMessage());
			}
		}
		
		return true;
	}

}
